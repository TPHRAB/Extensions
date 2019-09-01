// Timmy Zhao

// 7/22/19

// Github---extensions

// Class CopyThread generates a thread to do copy process for a file. This class supports program 
// to read or write a file from any starting position.


package extensions.copy;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CopyThread implements Runnable {

	public static final int BUFFER_LENGTH = 1024; // length of the buffer to read from the file
	private RandomAccessFile in;                  // File to read
	private RandomAccessFile out;                 // File to write
	private Thread thread; 		                  // Thread for this object
	private long end; 			                  // position for this thread to end

	// pre   : 1. in.exists() && end < in.length() (throws IOException if not)
	//         2. start <= end && offset >= 0 (throws IllegalArgumentException if not)
	// post  : construct a Copy class
	// param : in     --- File to read
	// 		   out    --- File to write
	// 		   start  --- start position to read from "in"
	// 		   end    --- end position to read from "in"
	//         offset --- starting position to write in "out"
	public CopyThread(File in, File out, long start, long end, long offset) throws 
			IllegalArgumentException, IOException {
		// checking conditions
		if (start > end) {
			throw new IllegalArgumentException("Starting Position is greater than ending position!");
		}
		
		if (offset < 0) {
			throw new IllegalArgumentException("Offset value is less than zero");
		}
		
		// assign values to fields
		this.in = new RandomAccessFile(in, "r");
		this.out = new RandomAccessFile(out, "rw");
		this.in.seek(start);
		this.out.seek(start + offset);
		this.end = end;
		this.thread = new Thread(this);
	}
	
	
	// post : copy task for one thread
	public void run() {
		try {
			byte[] buffer = new byte[BUFFER_LENGTH];
			while (in.getFilePointer() + BUFFER_LENGTH - 1 <= end) {
				int length = in.read(buffer);
				out.write(buffer, 0, length);
			}
			if (in.getFilePointer() <= end) {
				int length = (int) (end - in.getFilePointer() + 1);
				in.read(buffer, 0, length);
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// post : start this thread
	public void start() {
		thread.start();
	}

	// post : wait for this thread to die
	public void join() throws Exception {
		thread.join();
	}
}
