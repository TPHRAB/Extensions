// Timmy Zhao

// 08/13/2019

// "FileUtils" class provides static methods for handling files

package extensions.file;

import java.io.File;
import java.util.Arrays;

public class FileUtils {
	
	// pre   : list != null
	// post  : return the sorted file list based on names of the files
	// param : list  --- directory that contains the file to sort
	public static File[] mergeSortOnFilesNames(File[] list) {
		if (list.length <= 1) {
			return list;
		} else {
			File[] part1 = mergeSortOnFilesNames(Arrays.copyOfRange(list, 0, list.length / 2));
			File[] part2 = mergeSortOnFilesNames(Arrays.copyOfRange(list, list.length / 2, list.length));
			File[] result = new File[part1.length + part2.length];
			int index1 = 0;
			int index2 = 0;
			int index3 = 0;
			while (index1 < part1.length && index2 < part2.length) {
				if (compareFilesNames(part1[index1], part2[index2]) < 0) {
					result[index3] = part1[index1];
					index1++;
				} else {
					result[index3] = part2[index2];
					index2++;
				}
				index3++;
			}
			while (index1 < part1.length) {
				result[index3] = part1[index1];
				index1++;
				index3++;
			}
			while (index2 < part2.length) {
				result[index3] = part2[index2];
				index2++;
				index3++;
			}
			return result;
		}
	}
	
	// pre    : a != null && b != null
	// post   : return the comparision result of a's name and b's name
	// params : a --- file to compare name with b
	//          b --- file to compare name with a
	public static int compareFilesNames(File a, File b) {
		String f1 = a.getName();
		String f2 = b.getName();
		if (f1.length() < f2.length()) {
			return -1;
		} else if (f1.length() > f2.length()) {
			return 1;
		} else {
			int index1 = 0;
			int index2 = 0;
			while (index1 < f1.length() && index2 < f2.length() && f1.charAt(index1) == f2.charAt(index2)) {
				index1++;
				index2++;
			}
			if (index1 < f1.length() && index2 == f2.length()) {
				return -1;
			} else if (index1 == f1.length() && index2 < f2.length()) {
				return 1;
			} else {
				return f1.charAt(index1) - f2.charAt(index2);
			}
		}
	}
	
	// pre   : dir != null
	// post  : recursivly delete the directory
	// param : dir --- the directory to delete recursively
	public static void deleteDirectory(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isFile()) {
				f.delete();
			} else {
				deleteDirectory(f);
				f.delete();
			}
		}
	}
}
