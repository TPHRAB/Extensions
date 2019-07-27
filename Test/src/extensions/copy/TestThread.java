// Timmy Zhao

// 7/24/19

// Github--extensions

// Class TestThread is the main class for calling CopyThread and Copy. It can use
// command line to communicate with its users.

package extensions.copy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
import me.tongfei.progressbar.ProgressBar;

// Use Multiple threads to copy or combine files
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

	// copy a file from "in" to "out"
	public static Copy doCopySingleFile(File in, File out, long offset) throws Exception {
		Copy task = new Copy(4, in, out, offset);
		task.start();
		return task;
	}

	// copy directory to "out"
	public static List<Copy> doCopyDirectory(File[] list, File out, boolean showPB) throws Exception {
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

	// combine all the files in "list" and output them to "out"
	public static List<Copy> doCombine(File[] list, File out) throws Exception {
		if (out.exists()) {
			throw new IllegalArgumentException(out.getName() + " already exists!");
		}
		out.createNewFile();
		long totalSize = 0;
		List<Copy> threads = new ArrayList<Copy>();
		Arrays.parallelSort(list);
		long offset = 0;
		for (File f : list) {
			if (f.isFile()) {
				threads.add(doCopySingleFile(f, out, totalSize));
				totalSize += f.length();
			}
		}
		showFilePB(out, totalSize);
		return threads;
	}

	// show ProgressBar while generating a single file
	private static void showFilePB(File out, long totalSize) {
		try (ProgressBar pb = new ProgressBar(out.getName(), totalSize)) {
			while (out.length() < totalSize) {
				pb.stepTo(out.length()); // step directly to n
			}
		}
	}

	// return totalSize for files in "threads"
	private static long getDirectorySize(List<Copy> threads) {
		long totalSize = 0;
		for (Copy c : threads) {
			totalSize += c.getOutLength();
		}
		return totalSize;
	}

	// show ProgressBar while generating a directory
	private static void showDirectoryPB(List<Copy> threads, long totalSize) throws Exception {
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

	// wait for threads to die
	private static void waitThreads(List<Copy> threads) throws Exception {
		System.out.println("waiting for saving...");
		for (Copy c : threads) {
			c.join();
		}
		System.out.println("Done!");
	}

}
