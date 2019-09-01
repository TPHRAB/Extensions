// Timmy Zhao

// 7/24/19

// Github--extensions

// Class TestThread is the main class for calling CopyThread and Copy. It use Multiple threads 
// to copy or combine files

package extensions.copy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import me.tongfei.progressbar.ProgressBar;

public class TestThread {

	public static final char FILE_SYMBOL = '.';

	public static void main(String[] args) throws Exception {
		// get input and output path
		Scanner console = new Scanner(System.in);
		System.out.print("Source path (with file's or directory's name in it): ");
		String before = console.nextLine();
		System.out.print("Output path (with file's or directory's name in it): ");
		String after = console.nextLine();

		// initialize input and output path
		File in = new File(before);
		File out = new File(after);

		// automatically pick an option
		if (before.indexOf(FILE_SYMBOL) != -1) {
			if (after.lastIndexOf(FILE_SYMBOL) != -1) { // is a file
				doCopySingleFile(in, out, 0);
			} else { // is a directory
				console.close();
				throw new IllegalArgumentException("You can't change a file to a directory! "
						+ "Please add you new file's name at the end of the path!");
			}
		} else if (before.indexOf(FILE_SYMBOL) == -1) {
			if (after.indexOf(FILE_SYMBOL) != -1) { // is a file
				waitThreads(doCombine(in.listFiles(), out));
			} else { // is a directory
				waitThreads(doCopyDirectory(in.listFiles(), out, true));
			}

		}
		
		console.close();
	}

	// pre    : 1. in.exists() (throws IOException if not)
	//          2. !out.exist() && offset >= 0 (throws IllegalArgumentException if not)
	// post   : copy a file from "in" to "out"
	// params : in     --- file to read
	//          out    --- file to write
	//          offset --- starting position to write in "out"
	public static Copy doCopySingleFile(File in, File out, long offset) throws IllegalArgumentException, IOException {
		if (out.exists()) {
			throw new IllegalArgumentException("Output file already exist!");
		}
		
		Copy task = new Copy(4, in, out, offset);
		task.start();
		return task;
	}

	// pre    : 1. list != null (throws IllegalStateException if not)
	//          2. !out.exists() (throws IllegalArgumentException if not)
	// post   : copy directory to "out"
	// params : list   --- file list to do copying
	//          out    --- output directory
	//          showPB --- whether or not to show the progressbar
	public static List<Copy> doCopyDirectory(File[] list, File out, boolean showPB) throws IllegalArgumentException, IOException {
		if (list == null) {
			throw new IllegalStateException("Directory list is null");
		}

		if (out.exists()) {
			throw new IllegalArgumentException(out.getName() + " alreay exits!");
		}
		out.mkdir();
		
		List<Copy> threads = new ArrayList<Copy>();
		long totalSize = 0;
		for (File f : list) {
			if (f.isDirectory()) {
				List<Copy> tmp = doCopyDirectory(f.listFiles(), new File(out.getAbsoluteFile() + "/" + f.getName()), false);
				for (Copy c : tmp) {
					totalSize += c.getInLength();
					threads.add(c);
				}
			} else {
				Copy c = doCopySingleFile(f, new File(out.getAbsolutePath() + "/" + f.getName()), 0);
				totalSize += c.getInLength();
				threads.add(c);
			}
		}
		if (showPB) {
			showDirectoryPB(threads, totalSize);
		}
		return threads;
	}

	// pre    : 1. list != null (throws IllegalStateException if not)
	//          2. !out.exists() (throws IllegalArgumentException if not)
	// post   : combine all the files in "list" and output them to "out"
	// params : list --- file list to do copying
	public static List<Copy> doCombine(File[] list, File out) throws IOException {
		if (list == null) {
			throw new IllegalStateException("Directory list is null");
		}

		
		if (out.exists()) {
			throw new IllegalArgumentException(out.getName() + " already exists!");
		}
		
		out.createNewFile();
		long totalSize = 0;
		List<Copy> threads = new ArrayList<Copy>();
		list = extensions.file.fileUtils.mergeSortOnFilesNames(list);
		int i = 0;
		int count = 0;
		while (i < list.length) {
			if (count >= 500) {
				waitThreads(threads);
				threads.clear();
				count = 0;
			} else {
				File f = list[i];
				if (f.isFile() && f.length() > 0) {
					threads.add(doCopySingleFile(f, out, totalSize));
					totalSize += f.length();
				}
				i++;
				count++;
			}
		}
		showFilePB(out, totalSize);
		return threads;
	}

	// pre    : out != null (throws IllegalStateException if not)
	// post   : show ProgressBar while generating a single file
	// params : out       --- file to generate
	//          totalSize --- "out"'s size
	private static void showFilePB(File out, long totalSize) {
		if (out == null) {
			throw new IllegalStateException("Output file has not been created!");
		}
		
		try (ProgressBar pb = new ProgressBar(out.getName(), totalSize)) {
			while (out.length() < totalSize) {
				pb.stepTo(out.length()); // step directly to n
			}
		}
	}

	// pre   : "threads" != null
	// post  : return total size for files in "threads"
	// param : threads --- thread pool which contains "Copy" objects
	private static long getDirectorySize(List<Copy> threads) {
		long totalSize = 0;
		for (Copy c : threads) {
			totalSize += c.getOutLength();
		}
		return totalSize;
	}

	// pre    : "threads" != null
	// post   : show progressbar while processing a directory in command line
	// params : threads   --- thread pool which contains "Copy" objects
	//          totalSize --- total size of the directory
	private static void showDirectoryPB(List<Copy> threads, long totalSize) {
		try (ProgressBar pb = new ProgressBar("Copy Directory", totalSize)) {
			pb.stepTo(0);
			long currentSize = getDirectorySize(threads);
			while (currentSize < totalSize) {
				pb.stepTo(currentSize);
				currentSize = getDirectorySize(threads);
			}
			pb.stepTo(totalSize);
		}
	}

	// pre   : "threads" != null
	// post  : wait for threads to die
	// param : threads --- thread pool which contains "Copy" objects
	public static void waitThreads(List<Copy> threads) {
		if (threads == null) {
			throw new IllegalArgumentException("Thread pool to wait for dying is null!");
		}
		
		for (Copy c : threads) {
			try {
				c.join();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
