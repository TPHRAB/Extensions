package getVideosFromDisk;
// Timmy Zhao

// 08/19/19

// Github---extensions

// AutoProcessDisk class can 1. move videos in a disk to desktop 2. rename videos from disks to disks' name

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import static extensions.file.fileUtils.*;

public class AutoProcessDisk {

	public static final String DIRECTORY_SEPERATOR = System.getProperty("os.name").toLowerCase().contains("win") ? "\\" : "/";
	public static final String PROGRAM = System.getProperty("os.name").toLowerCase().contains("win") ? "./ffmpeg.exe" : "./ffmpeg";
	public static final long MB = -1024 * 1024;
	
	public static void main(String[] args) throws Exception {
		Scanner console = new Scanner(System.in);
		System.out.println("(1)Move videos from disk");
		System.out.println("(2)Sort videos after moving");
		System.out.println("(3)Convert videos after sorting");
		System.out.println("(4)Combine Videos");
		System.out.print("Your choice: ");
		int choice = console.nextInt();
		console.nextLine(); // skip \n
		if (choice == 1) {
			moveVideosFromDisk(console);
		} else if (choice == 2) {
			sortVideos(console);
		} else if (choice == 3) {
			convertVideos(console);
		} else if (choice == 4) {
			combineVideos(console);
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
		String prefix = in.getName() + "_";
		
		// process videos
		File[] list = in.listFiles();
		list = mergeSortOnFilesNames(list);
		for (File dir : list) {
			if (dir.isHidden()) {
				continue;
			}
			File[] videos = dir.listFiles();
			System.out.println(Arrays.toString(videos));
			videos = mergeSortOnFilesNames(videos);
			int useIndexName = 0;
			for (File vob : videos) {
				String regex1 = ".*_0[2-9]_[1-2]\\.VOB";
				if (vob.isHidden() || vob.length() <= MB || !vob.getName().matches(regex1)) {
					useIndexName++;
					continue;
				} 
				String fileName = vob.getName().substring(vob.getName().indexOf('_') + 1);
				vob.renameTo(new File(in.getAbsolutePath() + DIRECTORY_SEPERATOR + prefix + dir.getName() 
							 + "_" + fileName));
			}
		}
	}
	
	public static void convertVideos(Scanner console) throws IOException {
		boolean nextTurn = true;
		
		// get output path
		File out = null;
		while (nextTurn) {
			System.out.print("Please input the path for generating videos: ");
			out = new File(console.nextLine());
			if (!out.exists()) {
				System.out.println("Directory or file doesn't exist!");
			} else {
				nextTurn = false;
			}
		}
		nextTurn = true;
		
		// get input path
		File source = null;
		while (nextTurn) {
			System.out.print("Please input the videos' path to convert: ");
			source = new File(console.nextLine());
			if (!source.exists()) {
				System.out.println("Directory or file doesn't exist!");
			} else {
				nextTurn = false;
			}
		}
		nextTurn = true;
		
		List<String> command = new ArrayList<>();
		command.add(PROGRAM);
		command.add("-i");
		command.add("-b:v");
		command.add("500k");
		command.add("-r");
		command.add("25");
		command.add("-s");
		command.add("320*180");
		
		File[] list = extensions.file.fileUtils.mergeSortOnFilesNames(source.listFiles());
		for (File f : list) {		
			if (f.isHidden() || f.isDirectory() || !f.getName().split("\\.")[1].equals("VOB")) {
				continue;
			}
			// add source and output path
			command.add(2, f.getAbsolutePath());
			command.add(out.getAbsolutePath() + DIRECTORY_SEPERATOR + f.getName());
			processStart(command);
			// remove source and output path
			command.remove(command.size() - 1);
			command.remove(2);
		}
		
//		System.out.print("Do you want to delete the previous processed directory?(Y/~) ");
//		if (console.nextLine().charAt(0) == 'Y') {
//			extensions.file
//		}
	}
	
	public static void combineVideos(Scanner console) throws IOException {
		boolean nextTurn = true;
		
		File out = null;
		while (nextTurn) {
			System.out.print("Please input the path for generating combined videos: ");
			out = new File(console.nextLine());
			if (!out.exists()) {
				System.out.println("Directory doesn't exist!");
			} else {
				nextTurn = false;
			}
		}
		nextTurn = true;
		
		File source = null;
		while(nextTurn) {
			System.out.print("Please input the path of videos to combine: ");
			source = new File(console.nextLine());
			if (!source.exists()) {
				System.out.println("Directory doesn't exist!");
			} else {
				nextTurn = false;
			}
		}
		nextTurn = true;
		
		// ffmpeg command
		List<String> command = new ArrayList<>();
		command.add(PROGRAM);
		command.add("-i");
		command.add("-c");
		command.add("copy");
		File[] list = extensions.file.fileUtils.mergeSortOnFilesNames(source.listFiles());
		String firstIndex = "";
		String filesToConcat = "";
		int namingIndex = 1;
		String diskNumber = "1";
		for (int i = 0; i < list.length; i++) {
			File f = list[i];
			if (f.isHidden() || f.isDirectory() || !f.getName().split("\\.")[1].equals("VOB")) {
				continue;
			}
			String regex1 = ".*_.*_\\d*_\\d.VOB";
			String[] namingParts = f.getName().split("_");
			if (!namingParts[1].equals(diskNumber)) {
				namingIndex = 1;
				diskNumber = namingParts[1];
			}
			
			if (firstIndex.isEmpty()) {
				firstIndex = namingParts[2];
				filesToConcat = "concat:" + f.getAbsolutePath();
				command.add(out.getAbsolutePath() + DIRECTORY_SEPERATOR + 
						f.getName().substring(0, f.getName().lastIndexOf('_') - 2) + namingIndex + ".mp4");
				namingIndex++;
			} else if (f.getName().matches(regex1) && firstIndex.equals(f.getName().split("_")[2])) {
				filesToConcat = filesToConcat +  "|" + f.getAbsolutePath();
			} else {
				command.add(2, filesToConcat);
				processStart(command);
				command.remove(command.size() - 1);
				command.remove(2);
				firstIndex = "";
				filesToConcat = "";
				i--; // not going forward
			}	
		}
		// if last file's index is the same as the file before it, then it need to add manually
		if (!firstIndex.isEmpty()) {
			command.add(2, filesToConcat);
			processStart(command);
		}
	}
	
	public static void processStart(List<String> command) throws IOException {
		Process process = new ProcessBuilder(command).start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line = null;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		reader.close();
	}
}
