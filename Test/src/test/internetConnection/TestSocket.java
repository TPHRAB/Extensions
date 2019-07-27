package test.internetConnection;

import java.net.*;
import java.io.*;

public class TestSocket {

	public static String FORM_1 = "GET / HTTP/1.1\n" + "Host: www.baidu.com\n"
			+ "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:68.0) Gecko/20100101 Firefox/68.0\n"
			+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n"
			+ "Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\n"
			+ "Accept-Encoding: gzip, deflate\n" + "Connection: close\n"
			+ "Cookie: BAIDUID=435331B40717DB0FFC5F2F3F530A4FEB:FG=1; BIDUPSID=C10B2C3769069BC37E6EA13F51B7EA6C; PSTM=1547006925; BD_UPN=133252; COOKIE_SESSION=3964_0_4_0_25_2_0_0_3_4_1_3_0_0_5_0_1563437417_0_1563441376%7C9%230_0_1563441376%7C1; ZD_ENTRY=baidu; delPer=0; BD_HOME=0; H_PS_PSSID=1450_21087_29523_29521_28518_29098_28839_29221_26350_29072_28701; ___wk_scode_token=EffGkbM5tLOw5S3HNIBSwn581cuy0GYNkvpf8IOoq%2FE%3D; session_id=1563504015615; session_name=www.bing.com; rsv_jmp_slow=1563509467235; BD_CK_SAM=1; PSINO=7; H_PS_645EC=8672EEFydcrwDaiTJTWZ2S8e30mRVkoaqPS1pEcmI561zCvkYZmHtO3caJI\n"
			+ "Upgrade-Insecure-Requests: 1\n" + "\n";

	public static void main1(String[] args) {
		Socket socket = null;
		try {
			socket = new Socket(InetAddress.getByName("www.baidu.com"), 80);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			PrintStream out = new PrintStream(socket.getOutputStream());
			// out.print(FORM_1);
			out.print(FORM_1);
			print(socket);
			print(socket);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void print(Socket socket) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = in.readLine();
			boolean afterBlank = false;
			while (line != null) {
				System.out.println(line);
				line = in.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
