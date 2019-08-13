package extensions.file;

import java.io.File;
import java.util.Arrays;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File[] test = new File("/Users/zhaochenze/Desktop/raw").listFiles();
		test = fileUtils.mergeSortOnFilesNames(test);
		for (File f : test) {
			System.out.println(f.getName());
		}
	}

}
