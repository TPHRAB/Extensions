package extensions.download;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TestThread {

	public static void main(String[] args) throws Exception {
		// set up
		System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");

		PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter();
		pr.connect(pw);
		// char : 0-65535, (char) ((int) -1) == 65535
//		pw.write(65535);
//		pw.write(1);
//		pw.write(65535);
//
//		System.out.println(pr.read());
//		System.out.println(pr.read());
//		System.out.println(pr.read());

		Scanner console = new Scanner(System.in);
		boolean loop = true;
		int option = -1;
		// get user's choice
		while (loop) {
			System.out.println("Please choose an option:");
			System.out.println("(1) Download single file");
			System.out.println("(2) Download file list");
			System.out.print("Your choice: ");
			option = console.nextInt();
			if (option != 1 && option != 2) {
				System.out.println("Please input \"1\" or \"2\"!");
			} else {
				loop = false;
			}
		}
		console.nextLine(); // skip "\n"
		// execution
		if (option == 1) {
			downloadSingleFile(console);
		} else {
			downloadFileList(console);
		}

	}

	public static void downloadSingleFile(Scanner console) throws Exception {
		boolean loop = true;
		URL url = null;
		while (loop) {
			System.out.print("Please input the file's link: ");
			String link = console.nextLine();
			try {
				url = new URL(link);
				url.openConnection().connect();
				loop = false;
			} catch (Exception e) {
				System.out.println("Internet connection error or link doesn't exit! ");
				getBold("Please copy the link directly from browser.");
				System.out.println();
			}
		}
		File path = null;
		loop = true;
		while (loop) {
			boolean forward1 = false;
			System.out.print("Please input the path for generating file: ");
			path = new File(console.nextLine());
			if (path.exists() && path.isDirectory()) {
				forward1 = true;
			} else {
				getBold("Please input a path of an existing directory!");
			}
			if (forward1) {
				boolean forward2 = false;
				String outputPath = path + getURLFileName(url);
				File out = new File(outputPath);
				if (out.exists()) {
					System.out.print("Do you want to overwrite " + " " + outputPath + "(Y or n)? ");
					if (console.next().equals("Y")) {
						forward2 = true;
						console.nextLine(); // skip \n
					}
				} else {
					forward2 = true;
				}
				if (forward2) {
					doDownloadSingleFile(url, out, 10);
					loop = false;
				}
			}
		}
	}

	public static void doDownloadSingleFile(URL url, File out, int threadNum) throws Exception {
		Download threads = new Download(url, out, threadNum);
		threads.start();
		threads.join();
	}

	public static void downloadFileList(Scanner console) throws Exception {
		boolean loop = true;
		String first = null;
		String last = null;
		while (loop) {
			System.out.print("Please input the starting file's link: ");
			first = console.nextLine();
			System.out.print("Please input the ending file's link: ");
			last = console.nextLine();
			try {
				new URL(first).openConnection().connect();
				new URL(last).openConnection().connect();
				loop = false;
			} catch (Exception e) {
				System.out.println("Links don't exist or internet connection error!");
				getBold("Please check the links and input them again.");
				System.out.println();
			}
		}
		loop = true;
		System.out.println("Please input the path of a directory to generate output: ");
		File dir = null;
		dir = new File(console.nextLine());
		if (!dir.exists()) {
			dir.mkdir();
		}
		doDownloadFileList(first, last, dir);
	}

	public static void doDownloadFileList(String first, String last, File dir) throws Exception {
		String difference = getDifference(first, last);
		int bound = Integer.parseInt(difference);
		int startingIndex = last.indexOf(difference);
		String part1 = last.substring(0, startingIndex);
		String part2 = last.substring(startingIndex + difference.length());
		for (int i = 0; i <= bound; i++) {
			String var = addZeros(difference.length() - String.valueOf(i).length()) + i;
			URL url = new URL(part1 + var + part2);
			File out = new File(dir.getAbsoluteFile() + getURLFileName(url));
			doDownloadSingleFile(url, out, 1);
		}
	}

	public static String addZeros(int count) {
		if (count == 0) {
			return "";
		} else {
			return addZeros(count - 1) + "0";
		}
	}

	// pre : "a" and "b" have the same length
	public static String getDifference(String a, String b) {
		int begin = 0;
		while (a.charAt(begin) == b.charAt(begin)) {
			begin++;
		}
		int end = a.length() - 1;
		while (a.charAt(end) == b.charAt(end)) {
			end--;
		}
		return b.substring(begin, end + 1);
	}

	public static String getURLFileName(URL url) {
		return url.getFile().substring(url.getFile().lastIndexOf('/'));
	}

	public static void getBold(String str) {
		System.out.println("\33[;;1m" + str + "\33[;;0m");
	}

}
