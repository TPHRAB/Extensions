package extensions.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestURL {

	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		
		URL url = new URL("https://dldir1.qq.com/qqfile/QQforMac/QQ_V6.5.5.dmg");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Range", "bytes=" + 0 + "-1024");
		BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
		System.out.println(in.available());
		File result = new File("/Users/zhaochenze/Desktop/1.exe");
		result.createNewFile();
		FileOutputStream out = new FileOutputStream(result);
		byte[] buffer = new byte[10240];
		int count = 0;
		while ((count = in.read(buffer)) != -1) {
			out.write(buffer, 0, count);
		}
		
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Range", "bytes=" + result.length() + "-");
		in = new BufferedInputStream(connection.getInputStream());
		System.out.println(connection.getContentLength());
		count = 0;
		while ((count = in.read(buffer)) != -1) {
			out.write(buffer, 0, count);
		}
	}
}
