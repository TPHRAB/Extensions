// Timmy Zhao

// 7/25/19

// "DownloadThread" class serves to be "Thread" to download files while also sending current progress to pipe

package extensions.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

public class DownloadThread implements Runnable {
	public static final int BUFFER_LENGTH = 1024; // length of the buffer to store data
	private BufferedInputStream in;               // source to read
	private RandomAccessFile out;                 // output file
	private PipedWriter pW;                       // pipe to send current progress
	private Thread thread;                        // object's "Thread"
	
	// pre    : 1. pW has connected to the PipedReader which is used by the progressbar
	//          2. out exists or out can be created (throws IOException if not)
	// post   : create a "DownloadThread" which read from "in" and write to "out" which 
	//          starts at the specified position "start"
	// params : in    --- source to read from
	//          out   --- file to write (may not been created yet)
	//          start --- starting position to write in "out"
	//          pW    --- PipedWriter that is connected to progressbar's PipedReader for
	//                    sending current progress
	public DownloadThread(BufferedInputStream in, File out, int start, PipedWriter pW)
			throws IOException {
		this.in = in;
		this.out = new RandomAccessFile(out, "rws");
		this.out.seek(start);
		this.pW = pW;
		this.thread = new Thread(this);
	}
	
	// pre    : 1. pW has connected to the PipedReader which is used by the progressbar
	//          2. out exists or out can be created (throws IOException if not)
	// pose   : create a "DownloadThread" which read from "connection"'s inputstream and write to 
	//          "out", starting at the specified position "start" 
	// params : connection --- source to get data to read
	//          out        --- file to write (may not been created yet)
	//          start      --- starting position to write in "out"'s file
	//          pW         --- PipedWriter that is connected to progressbar's PipedReader
	//                         for sending current progress
	public DownloadThread(HttpURLConnection connection, File out, int start, PipedWriter pW)
			throws IOException {
		this(new BufferedInputStream(connection.getInputStream()), out, start, pW);
	}

	@Override
	// post : write everything from "in" to "out"
	public void run() {
		try {
			byte[] buffer = new byte[BUFFER_LENGTH];
			int count = 0;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
			pW.write(1);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// post : start "thread"
	public void start() {
		thread.start();
	}
	
	// post : wait for "thread" to die
	public void join() throws InterruptedException {
		thread.join();
	}

}
