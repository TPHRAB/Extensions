package extensions.download;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import org.dom4j.*;
import org.dom4jExtension.*;

public class AutoDownload {

    public static final String DEFAULT_OUTPUT_PATH = "C:\\Users\\Timmy\\Desktop";

    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

        Scanner console = new Scanner(System.in);
        System.out.print("Please input the video url: ");
        String url = console.nextLine();

        URL u = new URL(url);
        
    }

    public static void orangle(String url) throws Exception {
        Document doc = Jsoup.connect(url.toString()).get();

        // get first m3u8
        String link = doc.getElementsByTag("source").get(0).attributes().get("src");
        File out1 = new File(DEFAULT_OUTPUT_PATH + "\\" + link.substring(link.lastIndexOf('/') + 1));
        out1.createNewFile();
        DownloadManager.doDownloadSingleFile(new URL(link), out1, 1);

        // get second m3u8
        Scanner read = new Scanner(out1);
        String file = null;
        while (read.hasNextLine()) {
            file = read.nextLine();
        }
        link = link.substring(0, link.lastIndexOf('/') + 1) + file;
        File out2 = new File("C:\\Users\\Timmy\\Desktop\\result.m3u8");
        out2.createNewFile();
        DownloadManager.doDownloadSingleFile(new URL(link), out2, 1);

        // download ts files
        List<String> list = getList(link, out2, 1, null);
        File outputPath = new File("C:\\Users\\Timmy\\Desktop\\raw");
        int threads = 10;
        int num = list.size() / threads;
        if (list.size() % threads != 0) {
            threads++;
        }
        int start = 0;
        for (int i = 0; i < threads; i++) {
            int end = start + num - 1;
            // hahahaha
            end = i == threads - 1 ? list.size() - 1 : end;
            new MultithreadDownloadList(list.get(start), list.get(end), outputPath).start();
        }
    }

    public static List<String> getList(String link, File m3u8, int threadNum,
                                       String from) throws Exception {
        Scanner read = new Scanner(m3u8);
        List<String> list = new ArrayList<String>();
        boolean startCombine = (from == null);
        while (read.hasNextLine()) {
            String line = read.nextLine();
            if (line.equals(from)) {
                startCombine = true;
            }
            if (startCombine && line.charAt(0) != '#') {
                list.add(link.substring(0, link.lastIndexOf('/') + 1) + line);
            }
        }
        return list;
    }

}
