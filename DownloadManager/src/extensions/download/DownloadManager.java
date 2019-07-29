package extensions.download;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

public class DownloadManager {

    // post : download the file from the url and output it to "out"
    public static void doDownloadSingleFile(URL url, File out, int threadNum) throws Exception {
        Download threads = new Download(url, out, threadNum);
        threads.start();
        threads.join();
    }

    // post : donwload ts files from "first" to "last" and output them to dir
    public static void doDownloadTSFileList(String first, String last, File dir) throws Exception {
        if (!dir.exists()) {
            dir.mkdir();
        }
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

    // post : helper method for doDownloadTSFileList to complete the url list
    private static String addZeros(int count) {
        if (count == 0) {
            return "";
        } else {
            return addZeros(count - 1) + "0";
        }
    }

    // pre  : "a" and "b" have the same length
    // post : return the difference between "a" and "b"
    private static String getDifference(String a, String b) {
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

    // post : return the file's name in this url
    public static String getURLFileName(URL url) {
        return url.getFile().substring(url.getFile().lastIndexOf('/'));
    }
}
