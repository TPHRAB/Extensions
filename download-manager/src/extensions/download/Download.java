// Timmy Zhao

// 7/26/19

// "Download" class serves to create and allocate the downloading task to "DownloadThread"

package extensions.download;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

public class Download {

	private List<DownloadThread> threadsPool; // list for tracking download threads

	// pre    : 1. threadNum >= 1 && fileLength / threadNum != 0 
	//          (throws IllegalArgumentException if not)
	//          2. pW has connected to the PipedReader which is used by the progressbar
	//          3. out exists or out can be created && url points to an accessible file 
	//          (throws IOException if not)
	// post   : create a "Download" class which stores a list of "DownloadThread" that are not 
	//          started yet
	// params : url               --- file to download
	//          out               --- file to generate (may not been created yet)
    //          threadNum         --- number of threads to perform during download
	//          pW                --- PipedWriter which has been connected to the progressbar's
	//                                PipedReader for sending current progress
	//          requestProperties --- http request properties to add on the request during 
	//                                downloading (null means use default request properties)
	public Download(URL url, File out, int threadNum, PipedWriter pW, 
			Map<String, String> requestProperties) throws IllegalArgumentException, IOException {
		if (threadNum < 1) {
			throw new IllegalArgumentException("thread number should be at least 1!");
		}

		// get file length
		long fileLength = -1;
		fileLength = DownloadManager.getURLFileLength(url, requestProperties);
		
		// distribute download work to threads
		this.threadsPool = new ArrayList<DownloadThread>();
		long blockSize = fileLength / threadNum;
		if (blockSize == 0) {
			throw new IllegalArgumentException("please enter a smaller thread number!");
		}
		if (fileLength % threadNum != 0) {
			threadNum++;
		}
		int start = 0;
		HttpURLConnection connection = null;
		for (int i = 0; i < threadNum - 1; i++) {
			long end = start + blockSize - 1;
			// connection = (HttpURLConnection) new URL(url.toString()).openConnection();
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Range", "bytes=" + start + "-" + end);
			// set request properties
			if (requestProperties != null) {
				for (String property : requestProperties.keySet()) {
					connection.setRequestProperty(property, requestProperties.get(property));
				}
			}
			threadsPool.add(new DownloadThread(connection, out, start, pW));
			start += blockSize;
		}
		// fence post
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Range", "bytes=" + start + "-");
		// set request properties
		if (requestProperties != null) {
			for (String property : requestProperties.keySet()) {
				connection.setRequestProperty(property, requestProperties.get(property));
			}
		}
		threadsPool.add(new DownloadThread(connection, out, start, pW));
	}

	// post : start the threads in "threadPool"
	public void start() {
		for (DownloadThread d : threadsPool) {
			d.start();
		}
	}
	
	// post : wait for threads in threadPool to die
	public void join() throws InterruptedException {
		for (DownloadThread d : threadsPool) {
			d.join();
		}
	}
}
