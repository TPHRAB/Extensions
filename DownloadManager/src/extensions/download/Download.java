// 7/26/19

// Github---extensions

package extensions.download;

import java.util.ArrayList;
import java.util.List;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

public class Download {

	private List<DownloadThread> threadsPool;
	private long fileLength;
	private String fileName;
	private ProgressBarThread pb;

	public Download(URL url, File out, int threadNum) throws Exception {
		if (threadNum < 1) {
			throw new IllegalArgumentException("thread number should be at least 1!");
		}
		out.createNewFile();

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// test whether the site support multithread
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Range", "bytes=0-1");
		if (connection.getResponseCode() != 206) {
			threadNum = 1;
		}


		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("HEAD");
		this.fileName = out.getName();
		this.fileLength = connection.getContentLengthLong();
		long blockSize = fileLength / threadNum;
		if (blockSize == 0) {
			throw new IllegalArgumentException("please enter a smaller thread number!");
		}

		PipedReader pR = new PipedReader();
		PipedWriter pW = new PipedWriter();
		pW.connect(pR);
		this.pb = new ProgressBarThread(pR, "Downloading", fileLength);

		int start = 0;
		this.threadsPool = new ArrayList<DownloadThread>();
		for (int i = 0; i < threadNum; i++) {
			long end = start + blockSize - 1;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Range", "bytes=" + start + "-" + end);
			threadsPool.add(new DownloadThread(connection, out, start, pW));
			start += blockSize;
		}
		if (connection.getContentLengthLong() % threadNum != 0) {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Range", "bytes=" + start + "-");
			threadsPool.add(new DownloadThread(connection, out, start, pW));
		}
	}

	public Download(String url, String file, int threadNum) throws Exception {
		this(new URL(url), new File(file), threadNum);
	}

	public Download(URL url, String file, int threadNum) throws Exception {
		this(url, new File(file), threadNum);
	}

	public Download(String url, File file, int threadNum) throws Exception {
		this(new URL(url), file, threadNum);
	}

	public void start() {
		pb.start();
		for (DownloadThread d : threadsPool) {
			d.start();
		}
	}
	
	public void join() throws Exception {
		for (DownloadThread d : threadsPool) {
			d.join();
		}
	}
}
