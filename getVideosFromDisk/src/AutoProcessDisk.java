// Timmy Zhao

// 08/19/19

// Github---extensions

// AutoProcessDisk class can 1. move videos in a disk to desktop 2. rename videos from disks to disks' name

import java.io.File;
import java.nio.file.Files;
import java.util.Scanner;
import static extensions.file.fileUtils.*;

public class AutoProcessDisk {

	public static final String DIRECTORY_SEPERATOR = System.getProperty("os.name").toLowerCase().contains("win") ? "\\" : "/";
	
	public static void main(String[] args) throws Exception {
		Scanner console = new Scanner(System.in);
		System.out.println("(1)Move videos from disk");
		System.out.println("(2)Sort videos after moving");
		System.out.print("Your choice: ");
		int choice = console.nextInt();
		console.nextLine(); // skip \n
		if (choice == 1) {
			moveVideosFromDisk(console);
		} else if (choice == 2) {
			sortVideos(console);
		} else {
			System.out.println("No such choice!");
		}
	}

	public static void moveVideosFromDisk(Scanner console) throws Exception {
		boolean nextTurn1 = true;
		
		File out = null;
		while (nextTurn1) {
			System.out.print("Please input the output directory: ");
			out = new File(console.nextLine());
			if (!out.exists()) {
				System.out.println("Directory or file doesn't exits!");
			} else {
				nextTurn1 = false;
			}
		}
		nextTurn1 = true;
		
		while (nextTurn1) {
			boolean nextTurn2 = true;
			
			File in = null;
			while (nextTurn2) {
				System.out.print("Please input the video's directory: ");
				in = new File(console.nextLine());
				if (!in.exists()) {
					System.out.println("Directory or file doesn't exits!");
				} else if (!in.isDirectory()) {
					System.out.println("Please input the path of a directory!");
				} else {
					nextTurn2 = false;
				}
			}
			nextTurn2 = true;
			
			// get destination
			int count = 0;
			for (File f : out.listFiles()) {
				if (!f.isHidden() && f.isDirectory()) 
					count++;
			}
			File dir = new File(out.getAbsolutePath() + DIRECTORY_SEPERATOR + (count + 1));
			dir.mkdir();
			
			// get videos's directory and copy to destination
			File[] list = in.listFiles();
			list = extensions.file.fileUtils.mergeSortOnFilesNames(list);
			for (File f : list) {
				if (!f.getName().split("\\.")[1].equals("VOB")) {
					continue;
				}
				File result = new File(dir.getAbsolutePath() + DIRECTORY_SEPERATOR + f.getName());
				Files.copy(f.toPath(), result.toPath());
			}
			
			// add another disk
			System.out.print("Add another disk?(Y/~) ");
			nextTurn1 = console.nextLine().charAt(0) == 'Y';
		}
	}
	
	public static void sortVideos(Scanner console) {
		boolean nextTurn = true;
		
		// get output path
		File in = null;
		while (nextTurn) {
			System.out.print("Please input the path of the dirctory: ");
			in = new File(console.nextLine());
			if (!in.exists()) {
				System.out.println("Directory or file doesn't exist!");
			} else {
				nextTurn = false;
			}
		}
		nextTurn = true;
		
		// get prefix for videos
		String prefix = in.getName() + " | ";
		
		// process videos
		File[] list = in.listFiles();
		list = mergeSortOnFilesNames(list);
		for (int i = 0; i < list.length; i++) {
			File dir = list[i];
			if (dir.isHidden()) {
				continue;
			}
			File[] videos = dir.listFiles();
			videos = mergeSortOnFilesNames(videos);
			for (int j = 0; j < videos.length; j++) {
				File vob = videos[j];
				String regex1 = ".*_.*_[1-2]\\.VOB";
				String regex2 = ".*_TS\\.VOB";
				if (vob.isHidden() || !(vob.getName().matches(regex1) || vob.getName().matches(regex2)))
					continue;
				String fileName = vob.getName().substring(vob.getName().indexOf('_') + 1);
				vob.renameTo(new File(in.getAbsolutePath() + DIRECTORY_SEPERATOR + prefix + "D" + dir.getName() 
							 + " | V" + fileName));
			}
			dir.delete();
		}
	}
}
