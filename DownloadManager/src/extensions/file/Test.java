package extensions.file;

import java.io.File;
import java.util.Arrays;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Thread.sleep(10);
		File dir = new File("/Users/zhaochenze/Desktop/The Wisdom of History");
		System.out.println(dir.exists());
		File[] test = dir.listFiles();
		test = fileUtils.mergeSortOnFilesNames(test);
		for (File f : test) {
			System.out.println(f.getName());
		}
	}

}
