package getVideosFromDisk;
// Timmy Zhao

// 08/19/19

// Github---extensions

// AutoProcessDisk class can 1. move videos in a disk to desktop 2. rename videos from disks to disks' name

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;

import progressbar.ProgressBarFrame;

import static extensions.file.fileUtils.*;

public class AutoProcessDisk1 {

	public static final String DIRECTORY_SEPERATOR = System.getProperty("os.name").toLowerCase().contains("win") ? "\\"
			: "/";
	public static final String PROGRAM = System.getProperty("os.name").toLowerCase().contains("win") ? "./ffmpeg.exe"
			: "./ffmpeg";
	public static final long MB = -1024 * 1024;

//	public static void main(String[] args) throws Exception {
//		Scanner console = new Scanner(System.in);
//		System.out.println("(1)Move videos from disk");
//		System.out.println("(2)Sort videos after moving");
//		System.out.println("(3)Combine Videos");
//		System.out.print("Your choice: ");
//		int choice = console.nextInt();
//		console.nextLine(); // skip \n
//
//		boolean nextTurn = true;
//		if (choice == 1) {
//			File out = null;
//			while (nextTurn) {
//				System.out.print("Please input the output directory: ");
//				out = new File(console.nextLine());
//				if (!out.exists()) {
//					System.out.println("Directory or file doesn't exits!");
//				} else {
//					nextTurn = false;
//				}
//			}
//			nextTurn = true;
//
//			while (nextTurn) {
//				File in = null;
//				while (nextTurn) {
//					System.out.print("Please input the video's directory: ");
//					in = new File(console.nextLine());
//					if (!in.exists()) {
//						System.out.println("Directory or file doesn't exits!");
//					} else if (!in.isDirectory()) {
//						System.out.println("Please input the path of a directory!");
//					} else {
//						nextTurn = false;
//					}
//				}
//				nextTurn = true;
//
//				// move videos
//				moveVideosFromDisk(in, out, null);
//
//				// add another disk
//				System.out.print("Add another disk?(Y/~) ");
//				nextTurn = console.nextLine().charAt(0) == 'Y';
//			}
//		} else if (choice == 2) {
//			// get output path
//			File in = null;
//			while (nextTurn) {
//				System.out.print("Please input the path of the dirctory: ");
//				in = new File(console.nextLine());
//				if (!in.exists()) {
//					System.out.println("Directory or file doesn't exist!");
//				} else {
//					nextTurn = false;
//				}
//			}
//			nextTurn = true;
//			sortVideos(in);
//		} else if (choice == 3) {
//			File out = null;
//			while (nextTurn) {
//				System.out.print("Please input the path for generating combined videos: ");
//				out = new File(console.nextLine());
//				if (!out.exists()) {
//					System.out.println("Directory doesn't exist!");
//				} else {
//					nextTurn = false;
//				}
//			}
//			nextTurn = true;
//
//			File source = null;
//			while (nextTurn) {
//				System.out.print("Please input the path of videos to combine: ");
//				source = new File(console.nextLine());
//				if (!source.exists()) {
//					System.out.println("Directory doesn't exist!");
//				} else {
//					nextTurn = false;
//				}
//			}
//			nextTurn = true;
//			combineVideos(source, out, null);
//		} else {
//			System.out.println("No such choice!");
//		}
//	}

	public static void moveVideosFromDisk(File in, File out, JFrame rootFrame) throws Exception {
		// get destination
		int count = 0;
		for (File f : out.listFiles()) {
			if (f.isDirectory())
				count++;
		}
		File dir = new File(out.getAbsolutePath() + DIRECTORY_SEPERATOR + (count + 1));
		dir.mkdir();

		// get videos's directory and copy to destination
		File[] list = in.listFiles();
		list = extensions.file.fileUtils.mergeSortOnFilesNames(list);
		List<File> videosSource = new ArrayList<>();
		List<File> videosDestination = new ArrayList<>();
		for (File f : list) {
			String regex1 = "VTS_0[2-9]_[1-2]\\.VOB";
			if (f.isHidden() || f.length() <= MB || !f.getName().matches(regex1)) {
				continue;
			}
			File result = new File(dir.getAbsolutePath() + DIRECTORY_SEPERATOR + f.getName());
			videosSource.add(f);
			videosDestination.add(result);
		}
		convertVideos(videosSource, videosDestination, rootFrame);
	}

	public static void convertVideos(List<File> source,
			List<File> out, JFrame rootFrame) throws IOException, InterruptedException {
		// initialize command
		List<String> command = new ArrayList<>();
		command.add(PROGRAM);
		command.add("-i");
		command.add("-b:v");
		command.add("500k");
		command.add("-r");
		command.add("25");
		command.add("-s");
		command.add("320*180");
	
		// initialize frame
		ProgressBarFrame pbf = new ProgressBarFrame();
		pbf.initPB("Convert Videos", source.size());
		JTextArea area = pbf.getTextArea();
		JProgressBar pb = pbf.getPB();
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				for (int i = 0; i < source.size(); i++) {
					// add source and output path
					command.add(2, source.get(i).getAbsolutePath());
					command.add(out.get(i).getAbsolutePath());
					// set progress
					processStart(command, area);
					pb.setValue(pb.getValue() + 1);
					// remove source and output path
					command.remove(command.size() - 1);
					command.remove(2);
				}
				pbf.enableFinish();
				rootFrame.setVisible(true);
				rootFrame.requestFocus();
				pbf.requestFocus();
				return null;
			}
		}.execute();
	}

	public static void sortVideos(File in) {
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
			for (File vob : videos) {
				if (vob.isHidden()) {
					continue;
				}
				String fileName = vob.getName().substring(vob.getName().indexOf('_') + 1);
				vob.renameTo(
						new File(in.getAbsolutePath() + DIRECTORY_SEPERATOR + prefix + dir.getName() + "_" + fileName));
			}
			dir.delete();
		}
	}

	public static void combineVideos(File source,
			File out, JFrame rootFrame) throws IOException, InterruptedException {
		// ffmpeg command
		List<String> command = new ArrayList<>();
		command.add(PROGRAM);
		command.add("-i");
		command.add("-c");
		command.add("copy");
		File[] list = extensions.file.fileUtils.mergeSortOnFilesNames(source.listFiles());
		
		// initialize frame
		ProgressBarFrame pbf = new ProgressBarFrame();
		pbf.setTitle("Convert Videos");
		pbf.initPB("Combine Videos", list.length);
		JTextArea area = pbf.getTextArea();
		JProgressBar pb = pbf.getPB();
		new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				String firstIndex = "";
				String filesToConcat = "";
				int namingIndex = 1;
				String diskNumber = "1";
				for (int i = 0; i < list.length; i++) {
					File f = list[i];
					if (f.isHidden() || f.isDirectory() || !f.getName().split("\\.")[1].equals("VOB")) {
						pb.setValue(pb.getValue() + 1);
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
						/*
						 * command.add(out.getAbsolutePath() + DIRECTORY_SEPERATOR
						 *		+ f.getName().substring(0, f.getName().lastIndexOf('_') - 2) + namingIndex + ".mp4");
						 */
						command.add(out.getAbsolutePath() + DIRECTORY_SEPERATOR
								+ f.getName().substring(0, f.getName().lastIndexOf('_') - 3) + '-' + namingIndex + ".mp4");
						namingIndex++;
					} else if (f.getName().matches(regex1) && firstIndex.equals(f.getName().split("_")[2])) {
						filesToConcat = filesToConcat + "|" + f.getAbsolutePath();
					} else {
						command.add(2, filesToConcat);
						// set progress
						processStart(command, area);
						
						command.remove(command.size() - 1);
						command.remove(2);
						firstIndex = "";
						filesToConcat = "";
						i--; // not going forward
					}
					pb.setValue(pb.getValue() + 1);
				}
				// if last file's index is the same as the file before it, then it need to add manually
				if (!firstIndex.isEmpty()) {
					command.add(2, filesToConcat);
					// set progress
					processStart(command, area);
					pb.setValue(pb.getValue() + 1);
				}
				pbf.enableFinish();
				rootFrame.setVisible(true);
				rootFrame.requestFocus();
				pbf.requestFocus();
				return null;
			}
		}.execute();
	}

	public static void processStart(List<String> command, JTextArea area)
			throws IOException, InterruptedException {
		Process process = new ProcessBuilder(command).start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (area.getLineCount() >= 400) {
				try {
					area.replaceRange("", 0, area.getLineEndOffset(200));
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			area.append(line + "\r\n");
		} 
		reader.close();
		reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while ((line = reader.readLine()) != null) {
			if (area.getLineCount() >= 400) {
				try {
					area.replaceRange("", 0, area.getLineEndOffset(200));
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			area.append(line + "\r\n");
		}
		reader.close();
	}
}
