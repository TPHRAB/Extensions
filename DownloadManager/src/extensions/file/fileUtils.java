package extensions.file;

import java.io.File;
import java.util.Arrays;

public class fileUtils {
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
}
