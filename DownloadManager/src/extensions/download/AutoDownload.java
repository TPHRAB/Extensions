package extensions.download;

import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import extensions.progresbar.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.HashMap;
import org.dom4jExtension.*;


public class AutoDownload {

    public static final String DIRECTORY_SEPERATOR = System.getProperty("os.name").toLowerCase().contains("win") ? "\\" : "/";

    public static void main(String[] args) throws Exception {
    	
        // set up ssl
        System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        System.setProperty("javax.net.ssl.trustStore", "icetruststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");

        org.dom4j.Document xml = Dom4jUtil.getDocument("websites.xml");
        
        // get url and output path
        Scanner console = new Scanner(System.in);
        System.out.print("Please input the video url: ");
        String url = console.nextLine();
        System.out.print("Pleae input the path for generating video(s): ");
        String outputDirectory = console.nextLine();
        if (outputDirectory.trim().isEmpty()) {
        	String xpath = "/config/defaultPath[@seperator='" + DIRECTORY_SEPERATOR + "']";
        	outputDirectory = xml.selectSingleNode(xpath).getText();
        }
        File out = new File(outputDirectory);
        
        String xpath = "/config/methods/host[@id='" + new URL(url).getHost() + "']";
        Element host = (Element) xml.selectSingleNode(xpath);

        // 瑕佸緱鍒扮埗鑺傜偣鍒ゆ柇鏄摢涓柟娉�
        if(host.attributeValue("method").equals("m3u8Append")) {
            m3u8Appended(url, out, host);
            videoCombine(console, url, out, host);
        } else if (host.attributeValue("method").equals("directMP4")) {
        	directMP4(url, out, host);
        } else if (host.attributeValue("method").equals("seleniumMP4")) {
    		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
    		System.setProperty("webdriver.gecko.driver", "./geckodriver");
    		
    		WebDriver browser = new FirefoxDriver();
    		// set time interval to wait after loading a new page so that xpath could work fine
    		browser.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
    		browser.get(url);
    		// get video file
    		String link = null;
    		List<WebElement> target = browser.findElements(By.xpath("//iframe"));
    		for (WebElement e : target) {
    			if (e.getAttribute("src").contains("?videoid=")) {
    				browser.switchTo().frame(e);
    				WebElement realTarget = browser.findElement(By.tagName("source"));
    				link = realTarget.getAttribute("src");
    				break;
    			}
    		}
    		browser.close();
    		File result = new File(out.getAbsolutePath() + DIRECTORY_SEPERATOR + DownloadManager.getURLTitle(url) + ".mp4");
    		ProgressBar pb = new ProgressBar("Download", 11, "Thread", 1, "10");
    		pb.start();
    		DownloadManager.doDownloadSingleFile(new URL(link), result, 10, pb.getPipedWriter(), new HashMap<String, String>());
    		pb.join();
        } else if (host.attributeValue("method").equals("tsCustom")) {
            System.out.print("Please input the first common part of the sequence url: ");
            String part1 = console.nextLine().trim();
            System.out.print("Please input the second part of the common url: ");
            String part2 = console.nextLine().trim();
            System.out.print("Please input the starting index: ");
            int start = Integer.parseInt(console.nextLine());
            System.out.print("Please input the ending index: ");
            int end = Integer.parseInt(console.nextLine());
            Map<String, String> requestProperties = new HashMap<>();
    		requestProperties.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    		requestProperties.put("Accept-Encoding", "utf-8");
    		requestProperties.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
    		requestProperties.put("Cache-Control", "max-age=0");
    		requestProperties.put("Connection", "keep-alive");
    		requestProperties.put("Host", "cdn.qooqlevideo.com");
    		requestProperties.put("Upgrade-Insecure-Requests", "1");
    		requestProperties.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:68.0) Gecko/20100101 Firefox/68.0");
    		requestProperties.put("Referer", url);
            DownloadManager.downloadTSFileListCustom(part1, part2, start, end, out, 1, requestProperties);
            videoCombine(console, url, out, host);
        } else if (host.attributeValue("method").equals("directMP4")) {
        	directMP4(url, out, host);
        } else {
        	System.out.println("Not supported website!");
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
        DownloadManager.doDownloadSingleFile(url, out, 1, pb.getPipedWriter(), new HashMap<String, String>());
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
            DownloadManager.doDownloadSingleFile(url, out, 1, pb.getPipedWriter(), new HashMap<String, String>());
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
        DownloadManager.downloadTSFileList(list, dir, 10, new HashMap<String, String>());
    }

    public static void directMP4(String url, File out, Element host) throws Exception {
    	if (DIRECTORY_SEPERATOR.equals("/")) {
    		System.setProperty("webdriver.gecko.driver", "./geckodriver");
    	} else {
    		System.setProperty("webdriver.gecko.driver", "./geckodriver.exe");
    	}

    	WebDriver browser = new FirefoxDriver();
    	browser.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
    	browser.get(url);
    	String link = browser.findElements(By.tagName("video")).get(0).findElement(By.tagName("source")).getAttribute("src");
    	String fileName = browser.getTitle().replaceAll("\\**", "");
        File result = new File(out.getAbsolutePath() + DIRECTORY_SEPERATOR + fileName + ".mp4");
        browser.close();
        int threads = 10;
        long fileLength = DownloadManager.getURLFileLength(new URL(link), new HashMap<String, String>());
        if (fileLength % 10 > 0) threads++;
        ProgressBar pb = new ProgressBar("Download single file", threads, "Size", 1, String.valueOf(fileLength));
        pb.start();
        DownloadManager.doDownloadSingleFile(new URL(link), result, 10, pb.getPipedWriter(), new HashMap<String, String>());
        pb.join();
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
    
    public static boolean videoConvert(String path) throws Exception {
        
            List<String> command = new ArrayList<>();
            boolean execution = false;
            if (DIRECTORY_SEPERATOR.equals("\\")) {
            	command.add("cmd.exe");
            	command.add("/c"); 
            	command.add("start");
            	command.add("./ffmpeg.exe");
            	execution = true;
            } else if (DIRECTORY_SEPERATOR.equals("/")) {
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
            	return true;
            } else {
            	return false;
            }
    }
    
	public static void videoConvert(List<String> command) throws IOException {
		// 鎵ц鎿嶄綔
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
	
	public static void videoCombine(Scanner console, String url, File dir, Element host) throws Exception {
		System.out.print("Please input the path for combination: ");
        String path = console.nextLine();
        path = path + DIRECTORY_SEPERATOR + DownloadManager.getURLTitle(url) + ".ts";
        File result = new File(path);
        extensions.copy.TestThread.waitThreads(
                extensions.copy.TestThread.doCombine(dir.listFiles(), result));
        
        if (dir.getName().equals("raw")) {
        	for (File f : dir.listFiles()) {
        		f.delete();
        	}
        	dir.delete();
        }
        
        String convert = host.attributeValue("autoConvert");
        if (convert != null && convert.equals("true")) {
        	if (videoConvert(path)) result.delete();
        }
	}
}
