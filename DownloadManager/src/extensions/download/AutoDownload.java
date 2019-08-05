package extensions.download;

import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Date;
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
        System.setProperty("javax.net.ssl.trustStore", "icetruststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");

        // get url and output path
        Scanner console = new Scanner(System.in);
        System.out.print("Please input the video url: ");
        String url = console.nextLine();
        System.out.print("Pleae input the path for generating video(s): ");
        File out = new File(console.nextLine());

        org.dom4j.Document xml = Dom4jUtil.getDocument("websites.xml");
        String XPath = "/config/methods/host[@id='" + new URL(url).getHost() + "']";
        Element host = (Element) xml.selectSingleNode(XPath);

        // 要得到父节点判断是哪个方法
        if(host.attributeValue("method").equals("m3u8Append")) {
            m3u8Appended(url, out, host);
            System.out.print("Please input the path for combination: ");
            String path = console.nextLine();
            path = path + DownloadManager.getURLTitle(url) + ".ts";
            File result = new File(path);
            extensions.copy.TestThread.waitThreads(
                    extensions.copy.TestThread.doCombine(out.listFiles(), result));
            
            if (out.getName().equals("raw")) {
            	for (File f : out.listFiles()) {
            		f.delete();
            	}
            }
            
            // 得到系统
            String convert = host.attributeValue("autoConvert");
            if (convert != null && convert.equals("true")) {
	            String os = System.getProperty("os.name").toLowerCase();
	            List<String> command = new ArrayList<>();
	            boolean execution = false;
	            if (os.contains("win")) {
	            	command.add("cmd.exe");
	            	command.add("/c"); 
	            	command.add("start");
	            	command.add("./ffmpeg.exe");
	            	execution = true;
	            } else if (os.contains("mac")) {
	            	command.add("./ffmpeg");
	            	execution = true;
	            } else {
	            	System.out.println("Not supported operating system! Please do the convertion by yourself!");
	            }
	            
	            // set input file and output file
	            command.add("-i");
	            command.add(path);
	            command.add(path.substring(0, path.length() - 2) + "mp4");
	            
	            // run script
	            if (execution) {
	            	videoConvert(command);
	            	result.delete();
	            }
            }
        } else {
            System.out.print("Please input the first common part of the sequence url: ");
            String part1 = console.nextLine().trim();
            System.out.print("Please input the second part of the common url: ");
            String part2 = console.nextLine().trim();
            System.out.print("Please input the starting index: ");
            int start = Integer.parseInt(console.nextLine());
            System.out.print("Please input the ending index: ");
            int end = Integer.parseInt(console.nextLine());
            DownloadManager.downloadTSFileListCustom(part1, part2, start, end, out,1);
        }

        // write history
        boolean recordHistory = ((Element) xml.selectSingleNode("/config/history"))
                .attributeValue("record")
                .equals("true");
        if (recordHistory) {
            String path = ((Element) xml.selectSingleNode("/config/history")).attributeValue("path");
            File history = new File(path);
            if (!history.exists()) {
                history.createNewFile();
            }
            RandomAccessFile r = new RandomAccessFile(path, "rwd");
            r.seek(history.length());
            r.writeChars(new Date() + "    " + url + "    " + DownloadManager.getURLTitle(url) + "\r\n");
            r.close();
        }
        console.close();
        System.out.println("Finish...");
    }

    public static void m3u8Appended(String l, File dir, Element host) throws Exception {
        Document doc = Jsoup.connect(l).get();

        // read selected "host" in xml to get the first m3u8 file
        String link = doc.getElementsByTag(host.element("tag").getTextTrim())
                .get(Integer.parseInt(host.element("index").getTextTrim()))
                .attributes()
                .get(host.element("attribute").getTextTrim());
        if (host.attributeValue("addProtocal").equals("true")) {
        	link = l.substring(0, l.indexOf('/')) + link;
        }
        link = link.substring(link.lastIndexOf("http"));
        URL url = new URL(link);
        File out = new File("./" + DownloadManager.getURLFileName(url));
        ProgressBar pb = new ProgressBar("Download", 1, "Threads",
                1, "1");
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
            url = new URL(link);
            out = new File("./" + DownloadManager.getURLFileName(url));
            pb = new ProgressBar("Download", 1, "Threads", 1, "1");
            pb.start();
            DownloadManager.doDownloadSingleFile(url, out, 1, pb.getPipedWriter());
            pb.join();
            // set "out" to be "out2"
            read.close();
        }

        // download ts files
        List<String> list = getAppendedList(link, out, null);
        int threads = 10;
        if (list.size() / threads == 0) {
            throw new IllegalArgumentException("list.size() / threads == 0!");
        }
        DownloadManager.downloadTSFileList(list, dir, 10);

        for (File f : dir.listFiles()) {
            if (f.length() == 0) {
                f.delete();
            }
        }
    }

    public static List<String> getAppendedList(String link, File m3u8, String from) throws Exception {
        Scanner read = new Scanner(m3u8);
        List<String> list = new ArrayList<String>();
        boolean startCombine = (from == null);

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
        read.close();
        return list;
    }
    
	public static void videoConvert(List<String> command) throws IOException {
		// 执行操作
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();
		InputStream errorStream = process.getErrorStream();
		InputStreamReader isr = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		if (br != null) {
			br.close();
		}
		if (isr != null) {
			isr.close();
		}
		if (errorStream != null) {
			errorStream.close();
		}
	}
}
