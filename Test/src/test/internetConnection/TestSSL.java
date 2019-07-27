package test.internetConnection;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;


public class TestSSL {

	// Remember to change Accept-Encoding's value to "UTF-8", otherwise characters will be unreadable
	public static final String FORM_1 = "GET / HTTP/1.1\n" + "Host: www.baidu.com\n"
			+ "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:68.0) Gecko/20100101 Firefox/68.0\n"
			+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n"
			+ "Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\n"
			+ "Accept-Encoding: “UTF-8\n" + "Connection: close\n"
			+ "Cookie: BAIDUID=435331B40717DB0FFC5F2F3F530A4FEB:FG=1; BIDUPSID=C10B2C3769069BC37E6EA13F51B7EA6C; PSTM=1547006925; BD_UPN=133252; COOKIE_SESSION=73261_0_4_0_29_4_0_0_2_4_1_2_0_0_5_0_1563437417_0_1563514637%7C9%230_0_1563514637%7C1; ___wk_scode_token=EffGkbM5tLOw5S3HNIBSwn581cuy0GYNkvpf8IOoq%2FE%3D\n"
			+ "Upgrade-Insecure-Requests: 1" + "\n" + "\n";

	public static final String HOST = "www.baidu.com"; // default webpage to connect
	public static final int PORT = 443; // default ssl port to connect

	public static void main(String[] args) throws Exception {
		// set key store 
		System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		
		// get SSLSocket
		SSLSocket sslSocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(InetAddress.getByName(HOST), PORT);
		
		// get I/O
		BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
		PrintStream out = new PrintStream(sslSocket.getOutputStream());

		// send form
		out.print(FORM_1);
		out.flush();

		writeFile(in);
		
	}
	
	public static File writeFile(BufferedReader in) throws Exception {
		File result = new File("result.html");
		result.createNewFile();
		PrintStream writeFile = new PrintStream(result);
		String line = in.readLine();
		boolean htmlTag = false;
		while (line != null) {
			if (line.toLowerCase().equals("<html>")) {
				htmlTag = true;
			}
			if (htmlTag) {
				writeFile.println(line);
			}
			line = in.readLine();
		}
		writeFile.close();
		return result;
	}
	

}
