package getVideosFromDisk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Test {
	public static void main(String[] args) throws IOException {
		File source = new File("/Users/zhaochenze/Desktop/imitation");
		int[] counter = new int[2];
		recursiveHelper(source, counter);
		System.out.println("total: " + counter[0] + "matches: " + counter[1]);
	}
	
	public static void recursiveHelper(File source, int[] counter) {
		for (File f : source.listFiles()) {
			String regex1 = ".*_.*_[1-2]\\.VOB";
			String regex2 = ".*_TS\\.VOB";
			if (f.isDirectory()) {
				recursiveHelper(f, counter);
			} else if (f.getName().matches(regex1) || f.getName().matches(regex2)) {
//				System.out.println(f.getName());
				counter[1]++;
			} else {
				System.out.println(f.getName());
			}
			counter[0]++;
		}
	}
}
