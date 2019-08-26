import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

import extensions.download.DownloadManager;
import extensions.download.DownloadThread;
import extensions.progresbar.ProgressBar;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File out = new File("/Users/Timmy/Desktop/1.m3u8");
		out.createNewFile();
		URL url = new URL("https://ip19664247.cdn.qooqlevideo.com/key=MPA7DhLd4s2bbCiCwk67Lw,s=,end=1566730949,limit=2/data=1566730949/state=Vcsv/referer=force,.avgle.com/reftag=56109644/media=hlsA/1/177/3/58248913.m3u8");
    	Map<String, String> requestProperties = new HashMap<>();
    	requestProperties.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		requestProperties.put("Accept-Encoding", "utf-8");
		requestProperties.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		requestProperties.put("Connection", "keep-alive");
		requestProperties.put("Host", "ip174216091.ahcdn.com");
		requestProperties.put("Upgrade-Insecure-Request", "1");
		requestProperties.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:68.0) Gecko/20100101 Firefox/68");
		ProgressBar pb = new ProgressBar("Download", 1, "File", 1, "1");
		pb.start();
		DownloadManager.doDownloadSingleFile(url, out, 1, pb.getPipedWriter(), requestProperties);
		pb.join();
	}

}
