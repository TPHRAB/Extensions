package extensions.download;

import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
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
        String url = "http://www.dilidili.name/watch3/14697/";
        System.out.print("Pleae input the path for generating video(s): ");
        String path = "/Users/zhaochenze/Desktop/raw";

        org.dom4j.Document xml = Dom4jUtil.getDocument("websites.xml");
        String XPath = "/config/methods/host[@id='" + new URL(url).getHost() + "']";
        Element host = (Element) xml.selectSingleNode(XPath);
        // 要得到父节点判断是哪个方法
        if(host.attributeValue("method").equals("m3u8Append")) {
            m3u8Appended(url, new File(path), host);
        }

    }

    public static void m3u8Appended(String l, File dir, Element host) throws Exception {
        Document doc = Jsoup.connect(l.toString()).get();

        // read selected "host" in xml to get the first m3u8 file
        String link = doc.getElementsByTag(host.element("tag").getTextTrim())
                .get(Integer.parseInt(host.element("index").getTextTrim()))
                .attributes()
                .get(host.element("attribute").getTextTrim());
        link = link.substring(link.lastIndexOf("http"));
        File out = new File("./tmp1.m3u8");
        out.createNewFile();
        URL url = new URL(link);
        ProgressBar pb = new ProgressBar("Download", DownloadManager.getURLFileLength(url));
        pb.start();
        DownloadManager.doDownloadSingleFile(url, out, 1, pb.getPipedWriter());
        pb.join();

        if (host.attributeValue("trace").equals("true")) {
            // get second m3u8
            Scanner read = new Scanner(out);
            String file = null;
            while (read.hasNextLine()) {
                file = read.nextLine();
            }
            link = link.substring(0, link.lastIndexOf('/') + 1) + file;
            File out2 = new File("./tmp2.m3u8");
            out2.createNewFile();
            url = new URL(link);
            pb = new ProgressBar("Download", DownloadManager.getURLFileLength(url));
            pb.start();
            DownloadManager.doDownloadSingleFile(url, out2, 1, pb.getPipedWriter());
            pb.join();
            // set "out" to be "out2"
            out.delete();
            out = out2;
        }

        // download ts files
        List<String> list = getAppendedList(link, out, 1, null);
        int threads = 10;
        if (list.size() / threads == 0) {
            throw new IllegalArgumentException("list.size() / threads == 0!");
        }
        DownloadManager.downloadTSFileList(list, dir, 10);

        // delete tmp files
        // out.delete();
    }

    public static List<String> getAppendedList(String link, File m3u8, int threadNum,
                                       String from) throws Exception {
        Scanner read = new Scanner(m3u8);
        List<String> list = new ArrayList<String>();
        boolean startCombine = (from == null);

        int count = 0;

        while (read.hasNextLine()) {
            String line = read.nextLine();
            if (line.equals(from)) {
                startCombine = true;
            }
            if (startCombine && line.charAt(0) != '#') {
                if (line.indexOf("http") == -1) {
                    list.add(link.substring(0, link.lastIndexOf('/') + 1) + line);
                } else {
                    list.add(line);
                }
            }


        }
        return list;
    }

}
