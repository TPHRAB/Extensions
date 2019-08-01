package extensions.download;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.PipedWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static extensions.download.ProgressBarThread.getBold;

public class DownloadManager {

    // post : download the file from the url and output it to "out"
    public static void doDownloadSingleFile(URL url, File out, int threadNum, PipedWriter pW) throws Exception {
        out.createNewFile();
        Download threads = new Download(url, out, threadNum, pW);
        threads.start();
        threads.join();
    }

    // post : donwload ts files from "first" to "last" and output them to dir
    public static void downloadTSFileList(String first, String last, File dir, int threadNum) throws Exception {
        if (!dir.exists()) {
            dir.mkdir();
        }
        String difference = getDifference(first, last);
        int bound = Integer.parseInt(difference);
        int startingIndex = last.lastIndexOf(difference);
        String part1 = last.substring(0, startingIndex);
        String part2 = last.substring(startingIndex + difference.length());
        List<String> list = new ArrayList<String>();
        for (int i = 0; i <= bound; i++) {
            String var = addZeros(difference.length() - String.valueOf(i).length()) + i;
            list.add(part1 + var + part2);
        }
        downloadTSFileList(list, dir, threadNum);
    }

    public static void downloadTSFileListCustom(String part1, String part2, int start, int end, File dir,
                                                int threads) throws Exception {
        List<String> list = new ArrayList<String>();
        for (int i = start; i <= end; i++) {
            list.add(part1 + i + part2);
        }
        downloadTSFileList(list, dir, 10);
    }

    public static void downloadTSFileList(List<String> list, File dir, int threadNum) throws Exception {
        if (!dir.exists()) {
            dir.mkdir();
        }
        int num = list.size() / threadNum;
        if (list.size() % threadNum != 0) {
            threadNum++;
        }
        int start = 0;
        List<MultithreadDownloadList> threadsPool = new ArrayList<MultithreadDownloadList>();
        ProgressBar pb = new ProgressBar("Download", list.size(), "files", 1,
                Integer.toString(threadNum));
        pb.start();
        // start all threads
        for (int i = 0; i < threadNum; i++) {
            int end = start + num - 1;
            // hahahaha
            end = i == threadNum - 1 ? list.size() - 1 : end;
            MultithreadDownloadList t = new MultithreadDownloadList(list, start, end, dir, pb.getPipedWriter());
            threadsPool.add(t);
            t.start();
            start += num;
        }
        // wait for all subthreads to join
        for (MultithreadDownloadList t : threadsPool) {
            t.join();
        }
        pb.join();
    }

    // post : donwload ts files from "first" to "last" and output them to dir
    public static void doDownloadTSFileList(List<String> list, int first, int last, File dir, PipedWriter pW) throws Exception {
        if (!dir.exists()) {
            dir.mkdir();
        }

        for (int i = first; i <= last; i++) {
            URL url = new URL(list.get(i));
            File out = new File(dir.getAbsoluteFile() + getURLFileName(url));
            out.createNewFile();
            doDownloadSingleFile(url, out, 1, pW);
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

    public static String getURLTitle(String link) throws Exception {
        Document doc = Jsoup.connect(link).get();
        return doc.title();
    }

    // post : return the URL file's length
    public static long getURLFileLength(URL url) throws Exception {
        long contentLength = -1;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(20000);
            connection.setRequestMethod("HEAD");
            if (connection.getResponseCode() == 200) {
                contentLength = connection.getContentLength();
            } else {
                System.out.println(getBold("Error: " + url + "\t(" + connection.getResponseCode() + ")"));
            }
        } catch (SocketTimeoutException e) {
            System.out.println(getBold("Time out: " + url));
        }
        return contentLength;
    }

    private static long getListFilesLength(List<String> list) throws Exception {
        long totalBytes = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            long fileLength = getURLFileLength(new URL(list.get(i)));
            if (fileLength != -1) {
                totalBytes += fileLength;
            } else {
                list.remove(i);
            }
            Thread.sleep(100);
        }
        return totalBytes;
    }
}
