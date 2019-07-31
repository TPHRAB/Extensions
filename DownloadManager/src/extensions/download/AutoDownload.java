package extensions.download;

import org.dom4j.Node;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.PipedWriter;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import org.dom4jExtension.*;

public class AutoDownload {

    public static final String DEFAULT_OUTPUT_PATH = "C:\\Users\\Timmy\\Desktop\\";

    public static void main(String[] args) throws Exception {
        // set up ssl
        System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

        // get url and output path
        Scanner console = new Scanner(System.in);
        System.out.print("Please input the video url: ");
        String url = console.nextLine();
        System.out.print("Pleae input the path for generating video(s): ");
        String path = console.nextLine();

        org.dom4j.Document xml = Dom4jUtil.getDocument("websites.xml");
        String XPath = "/methods/m3u8/host[@id='" + new URL(url).getHost() + "']";
        Node orange = xml.selectNodes(XPath).get(0);
        if(orange.getText().equals("orange")) {
            orange(url);
        }
        File dir = new File("/Users/zhaochenze/Desktop/raw/");
        DownloadManager.downloadTSFileList("https://cdn-5.haku99.com/hls/2019/04/22/AZTlkq9B/out000.ts",
                "https://cdn-5.haku99.com/hls/2019/04/22/AZTlkq9B/out019.ts", dir, 1);
    }

    public static void orange(String l) throws Exception {
        Document doc = Jsoup.connect(l.toString()).get();

        // get first m3u8
        String link = doc.getElementsByTag("source").get(0).attributes().get("src");
        File out1 = new File(DEFAULT_OUTPUT_PATH + link.substring(link.lastIndexOf('/') + 1));
        out1.createNewFile();
        URL url = new URL(link);
        ProgressBar pb = new ProgressBar("Download", DownloadManager.getURLFileLength(url));
        pb.start();
        DownloadManager.doDownloadSingleFile(url, out1, 1, pb.getPipedWriter());
        pb.join();

        // get second m3u8
        Scanner read = new Scanner(out1);
        String file = null;
        while (read.hasNextLine()) {
            file = read.nextLine();
        }
        link = link.substring(0, link.lastIndexOf('/') + 1) + file;
        File out2 = new File("C:\\Users\\Timmy\\Desktop\\result.m3u8");
        out2.createNewFile();
        url = new URL(link);
        pb = new ProgressBar("Download", DownloadManager.getURLFileLength(url));
        pb.start();
        DownloadManager.doDownloadSingleFile(url, out2, 1, pb.getPipedWriter());
        pb.join();

        // download ts files
        List<String> list = getOrangeList(link, out2, 1, null);
        File outputPath = new File("C:\\Users\\Timmy\\Desktop\\raw");
        int threads = 10;
        if (list.size() / threads == 0) {
            throw new IllegalArgumentException("list.size() / threads == 0!");
        }
        DownloadManager.downloadTSFileList(list, outputPath, 10);
    }

    public static List<String> getOrangeList(String link, File m3u8, int threadNum,
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
