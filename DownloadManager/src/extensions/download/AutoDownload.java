package extensions.download;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.*;

public class AutoDownload {
    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

        Scanner console = new Scanner(System.in);
        System.out.println("Please input the video url: ");
        Document doc = Jsoup.connect(console.nextLine()).get();
        String link = doc.getElementsByTag("source").get(0).attributes().get("src");

        File out1 = new File("C:\\Users\\Timmy\\Desktop\\tmp.m3u8");
        out1.createNewFile();
        out1.deleteOnExit();
        doDownloadSingleFile(new URL(link), out1, 1);

        Scanner read = new Scanner(out1);
        String file = null;
        while (read.hasNextLine()) {
            file = read.nextLine();
        }
        link = link.substring(0, link.lastIndexOf('/') + 1) + file;

        File out2 = new File("C:\\Users\\Timmy\\Desktop\\result.m3u8");
        out2.createNewFile();
        doDownloadSingleFile(new URL(link), out2, 1);
        doDownloadFileList(link, out2, "C:\\Users\\Timmy\\Desktop\\raw", 1);

    }
    public static void doDownloadSingleFile(URL url, File out, int threadNum) throws Exception {
        Download threads = new Download(url, out, threadNum);
        threads.start();
        threads.join();
    }

    public static void doDownloadFileList(String link, File m3u8, String dir, int threadNum) throws Exception {
        Scanner read = new Scanner(m3u8);
        while (read.hasNextLine()) {
            String line = read.nextLine();
            if (line.charAt(0) != '#') {
                File out = new File(dir + "\\" + line);
                URL url = new URL(link.substring(0, link.lastIndexOf('/') + 1) + line);
                doDownloadSingleFile(url, out, 1);
            }
        }
    }
}
