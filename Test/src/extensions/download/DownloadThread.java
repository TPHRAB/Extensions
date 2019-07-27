// Timmy Zhao

// 7/25/19

// Github---extensions

package extensions.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

public class DownloadThread implements Runnable {
	public static final int BUFFER_LENGTH = 1024;
	private BufferedInputStream in;
	private RandomAccessFile out;
	private Thread thread;
	
	public DownloadThread(BufferedInputStream in, File out, int start) throws Exception {
		this.in = in;
		out.createNewFile();
		this.out = new RandomAccessFile(out, "rws");
		this.out.seek(start);
		this.thread = new Thread(this);
	}
	
	public DownloadThread(HttpURLConnection connection, String out, int start) throws Exception {
		this(new BufferedInputStream(connection.getInputStream()), new File(out), start);
	}
	
	public DownloadThread(HttpURLConnection connection, File out, int start) throws Exception {
		this(new BufferedInputStream(connection.getInputStream()), out, start);
	}

	@Override
	public void run() {
		try {
			byte[] buffer = new byte[BUFFER_LENGTH];
			int count = 0;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void start() {
		thread.start();
	}
	public void join() throws Exception {
		thread.join();
	}

}
