package getVideosFromDisk;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GenerateTestingDirectory {
	
	public static String DIRECTORY_SEPERATOR = System.getProperty("os.name").toLowerCase().contains("win") ? "\\" : "/";
	
	public static void main(String[] args) throws Exception {
		boolean nextTurn = true;
		Scanner console = new Scanner(System.in);
		
		File desktop = null; // desktop
		while (nextTurn) {
			System.out.print("Please input desktop's path: ");
			desktop = new File(console.nextLine());
			if (desktop.exists() && desktop.getName().equals("Desktop")) {
				nextTurn = false;
			} else {
				System.out.println("\tWrong path!");
			}
		}
		nextTurn = true;
		
		File source = null; // directory to imitate
		while (nextTurn) {
			System.out.print("Please input the directory for imitating hiearchy: ");
			source = new File(console.nextLine());
			if (!source.exists()) {
				System.out.println("\tDirectory doesn't exist");
			} else if (!source.isDirectory()) {
				System.out.println("Path doesn't point to a directory");
			} else {
				nextTurn = false;
			}
		}
		nextTurn = true;
		
		File out = new File(desktop.getAbsoluteFile()+ DIRECTORY_SEPERATOR + "imitation");
		out.mkdir();
		recursiveCopyHelper(source, out);
		
	}
	
	public static void recursiveCopyHelper(File source, File out) throws IOException {
		for (File f : source.listFiles()) {
			File result = new File(out.getAbsolutePath() + DIRECTORY_SEPERATOR + f.getName());
			if (f.isDirectory()) {
				result.mkdir();
				recursiveCopyHelper(f, result);
			} else {
				result.createNewFile();
			}
		}
	}
}
