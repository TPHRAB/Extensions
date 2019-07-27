package test.internetConnection;

import java.net.*;
import java.io.*;

public class Test {

	public static final String FORM_1 = "GET /hello/AServlet HTTP/1.1\n" + "Host: 127.0.0.1:8080\n"
			+ "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:68.0) Gecko/20100101 Firefox/68.0\n"
			+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n"
			+ "Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\n"
			+ "Accept-Encoding: gzip, deflate\n" + "Connection: close\n"
			+ "Cookie: JSESSIONID=42C8232F7E3CF19DB4474029B9638A90\n" + "Upgrade-Insecure-Requests: 1\n" + "\n";

	public static void testURL(String[] args) throws Exception {
		URL url = new URL("http://localhost:8080/hello/index.jsp");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = in.readLine();
		while (line != null) {
			line = in.readLine();
		}
	}
}