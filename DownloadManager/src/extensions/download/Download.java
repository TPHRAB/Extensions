// 7/26/19

// Github---extensions

package extensions.download;

import java.util.ArrayList;
import java.util.List;

import me.tongfei.progressbar.ProgressBarBuilder;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

public class Download {

	private List<DownloadThread> threadsPool;
	private long fileLength;
	private String fileName;

	public Download(URL url, File out, int threadNum, PipedWriter pW) throws Exception {
		if (threadNum < 1) {
			throw new IllegalArgumentException("thread number should be at least 1!");
		}

		if (!out.exists()) {
			throw new IllegalArgumentException("output file not initialized!");
		}

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection = (HttpURLConnection) url.openConnection();
		long fileLength = DownloadManager.getURLFileLength(url);
		this.threadsPool = new ArrayList<DownloadThread>();
		if (fileLength == -1) {
			System.out.println(ProgressBarThread.getBold("#### Error: " + url + " ####"));
			pW.write(threadNum);
		} else {
			long blockSize = fileLength / threadNum;
			if (blockSize == 0) {
				throw new IllegalArgumentException("please enter a smaller thread number!");
			}

			int start = 0;
			for (int i = 0; i < threadNum; i++) {
				long end = start + blockSize - 1;
				// connection = (HttpURLConnection) new URL(url.toString()).openConnection();
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
	}

	public Download(String url, String file, int threadNum, PipedWriter pW) throws Exception {
		this(new URL(url), new File(file), threadNum, pW);
	}

	public Download(URL url, String file, int threadNum, PipedWriter pW) throws Exception {
		this(url, new File(file), threadNum, pW);
	}

	public Download(String url, File file, int threadNum, PipedWriter pW) throws Exception {
		this(new URL(url), file, threadNum, pW);
	}

	public void start() {
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
