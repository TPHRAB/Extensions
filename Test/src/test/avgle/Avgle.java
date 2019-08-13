package test.avgle;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.net.*;
import java.io.*;

public class Avgle {
	
	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		
		HttpURLConnection connection = (HttpURLConnection) new URL("https://ip144929711.cdn.qooqlevideo.com/key=B5xhj0hq2QVVtD4UtOJSrw,s=,end=1565671987,limit=2/data=1565671987/state=208e/referer=force,.avgle.com/reftag=56109644/media=hlsA/ssd2/177/3/178965793.mp4/seg-5-v1-a1.ts").openConnection();
		
		Map<String, String> requestProperties = new HashMap<>();
		requestProperties.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		requestProperties.put("Accept-Encoding", "utf-8");
		requestProperties.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		requestProperties.put("Cache-Control", "max-age=0");
		requestProperties.put("Connection", "keep-alive");
		requestProperties.put("Host", "cdn.qooqlevideo.com");
		requestProperties.put("Upgrade-Insecure-Requests", "1");
		requestProperties.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:68.0) Gecko/20100101 Firefox/68.0");
		requestProperties.put("Referer", "https://avgle.com/video/IiVmzm9D4mn/shkd-858-%"
				+ "E5%A5%B3%E5%A4%A7%E5%AD%B8%E7%94%9F%E8%BC%AA%E5%A7%A6%E7%A4%BE%E5%9C%98-%E4%"
				+ "BA%8C%E5%AE%AE%E5%85%89-%E4%B8%AD%E6%96%87%E5%AD%97%E5%B9%95");
		
		for (String property : requestProperties.keySet()) {
			connection.setRequestProperty(property, requestProperties.get(property));
		}
		
//		connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		connection.setRequestProperty("Accept-Encoding", "utf-8");
//		connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
//		connection.setRequestProperty("Cache-Control", "max-age=0");
//		connection.setRequestProperty("Connection", "keep-alive");
//		connection.setRequestProperty("Host", "cdn.qooqlevideo.com");
//		connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
//		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:68.0) Gecko/20100101 Firefox/68.0");
//		connection.setRequestProperty("Referer", "https://avgle.com/video/IiVmzm9D4mn/shkd-858-%"
//				+ "E5%A5%B3%E5%A4%A7%E5%AD%B8%E7%94%9F%E8%BC%AA%E5%A7%A6%E7%A4%BE%E5%9C%98-%E4%"
//				+ "BA%8C%E5%AE%AE%E5%85%89-%E4%B8%AD%E6%96%87%E5%AD%97%E5%B9%95");
		BufferedInputStream reader = new BufferedInputStream(connection.getInputStream());
		File test = new File("/Users/zhaochenze/Desktop/1.ts");
		test.createNewFile();
		FileOutputStream writer = new FileOutputStream(test);
		byte[] buffer = new byte[1024];
		int count = -1;
		while ((count = reader.read(buffer)) != -1) {
			writer.write(buffer, 0, count);
		}
		
	}

}
