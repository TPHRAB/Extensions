// Timmy Zhao

// 7/23/19

// "Copy" is a class that serves to manipulate CopyThread class by creating multiple Copy threads 
// to improve efficiency.

package extensions.copy;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Copy {

	public static final int DEFAULT_THREAD_NUM = 5; // default number of threads to use in 
													// downloading files
	
	private List<CopyThread> threadPool; 			// list of reference to all the threads
	private File out; 					 			// file to write
	private File in;  					 			// file to read

	// pre   : 1. in.exists() (throws IOException if not)
	//         2. threadNum >= 1 && in.length() / threadNum > 0 
	//            && offset >= 0 (throws IllegalArgumentException if not)
	// post  : construct a Copy Class
	// param : threadNum --- number of threads to use while copying
	//		   in        --- file to be copied
	//         out 		 --- file to generate (may not been created yet)
	//         offset    --- starting position to write in "out"
	public Copy(int threadNum, File in, File out, long offset) throws IllegalArgumentException, IOException {
		if (threadNum < 1) {
			throw new IllegalArgumentException("Thread number is less than 1!");
		}
		
		this.in = in;
		this.out = out;
		this.threadPool = new ArrayList<CopyThread>();
		
		long blockSize = in.length() / threadNum;
		if (blockSize <= 0) {
			throw new IllegalArgumentException("please enter a smaller thread number!");
		}
		if (in.length() % threadNum != 0) {
			threadNum++;
		}
		
		long start = 0;
		for (int i = 0; i < threadNum; i++) {
			long end = start + blockSize - 1;
			if (end > in.length() - 1) {
				end = in.length() - 1;
			}
			threadPool.add(new CopyThread(in, out, start, end, offset));
			start += blockSize;
		}
	}

	// pre   : 1. "in" is an existing file (throws IOException if not)
	//         2. threadNum >= 1 && in.length() / threadNum > 0 
	//            && offset >= 0 (throws IllegalArgumentException if not)
	// post  : construct a Copy Class
	// param : threadNum --- number of threads to use while copying
	//		   in        --- filePath for the file to be copied
	//         out 		 --- filePath for the file to be generated (may not been created yet)
	//         offset    --- starting position to write in "out"
	public Copy(int threadNum, String in, String out, long offset) throws Exception {
		this(threadNum, new File(in), new File(out), offset);
	}

	// pre   : 1. "in" is an existing file (throws IOException if not)
	//         2. in.length() / threadNum > 0 
	//            && offset >= 0 (throws IllegalArgumentException if not)
	// post  : construct a Copy Class (set threadNum to be default number)
	// param : in     --- filePath for the file to be copied
	//         out    --- filePath for the file to be generated (may not been created yet)
	//         offset --- starting position to write in "out"
	public Copy(String in, String out, long offset) throws Exception {
		this(DEFAULT_THREAD_NUM, new File(in), new File(out), offset);
	}

	// pre   : 1. "in" is an existing file (throws IOException if not)
	//         2. in.length() / threadNum > 0 
	//            && offset >= 0 (throws IllegalArgumentException if not)
	// post  : construct a Copy Class (set threadNum to be default number)
	// param : in  --- File to be copied
	//         out --- File to be generated (may not been created yet)
	//         offset --- starting position to write in "out"
	public Copy(File in, File out, long offset) throws Exception {
		this(5, in, out, offset);
	}

	// post : start running all the threads in "threadPool"
	public void start() {
		for (CopyThread thread : threadPool) {
			thread.start();
		}
	}

	// post : wait for all the threads in "threadPool" to die
	public void join() throws Exception {
		for (CopyThread thread : threadPool) {
			thread.join();
		}
	}

	// post : return the length of "out"
	public long getOutLength() {
		return out.length();
	}

	// post : return the length of "in"
	public long getInLength() {
		return in.length();
	}

}
