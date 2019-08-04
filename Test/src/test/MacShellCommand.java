package test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MacShellCommand {
	public static void main(String[] args) throws Exception {
//		System.out.println(System.getProperty("os.name"));
//		String[] command = {"/bin/bash", "-c", "(cd /Users/zhaochenze/Desktop;mkdir aaaaa;ls)"};
//		BufferedReader r = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream()));
//		String nextLine = r.readLine();
//		while(nextLine != null) {
//			System.out.println(nextLine);
//			nextLine = r.readLine();
//		}
//		URL url = new URL("https://cdn-6.haku99.com/hls/2019/08/03/M5KEVEAu/playlist.m3u8");
//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setConnectTimeout(20000);
//        connection.setRequestMethod("HEAD");
//        long contentLength = -1;
//        if (connection.getResponseCode() == 200) {
//            contentLength = connection.getContentLength();
//        }
//        System.out.println(contentLength);
		
//		 String p1 = "\"" + "/Users/zhaochenze/Desktop/" + "普通攻击是全体二连击这样的妈妈你喜欢吗 第1话 以为开启了少年的一场波澜壮阔的冒险...但这是什么情况...。丨嘀哩嘀哩.ts" + "\"";
//		 String p2 = "\"" + "/Users/zhaochenze/Desktop/" + "普通攻击是全体二连击这样的妈妈你喜欢吗 第1话 以为开启了少年的一场波澜壮阔的冒险...但这是什么情况...。丨嘀哩嘀哩2.mp4" + "\"";
//		String p1 = "1 2 3 4";
//		String p2 = "5 6 7 8";
//        String script = "convert.sh";
//        String command = "/bin/bash" + " " + script + " " + p1 + " " + p2;
//        System.out.println(command);
		// String[] command = new String[]{"./ffmpeg",  "-i", "\"肌肉少女：哑铃能举多少公斤？ 第1话丨嘀哩嘀哩.ts\"", "\"肌肉少女：哑铃能举多少公斤？ 第1话丨嘀哩嘀哩.mp4\""};  
//		String[] command = {"./ffmpeg", "-i", "/Users/zhaochenze/Desktop/肌肉少女：哑铃能举多少公斤？ 第1话丨嘀哩嘀哩.ts", 
//				"/Users/zhaochenze/Desktop/肌肉少女：哑铃能举多少公斤？ 第1话丨嘀哩嘀哩.mp4"};
		
		videoConvert("./ffmpeg", "/Users/zhaochenze/Desktop/肌肉少女：哑铃能举多少公斤？ 第1话丨嘀哩嘀哩.ts", 
				"/Users/zhaochenze/Desktop/肌肉少女：哑铃能举多少公斤？ 第1话丨嘀哩嘀哩.mp4");

	}
	
	public static void videoConvert(String ffmpegPath, String videoInputPath, String videoOutputPath) throws IOException {
		// 构建命令
		List<String> command = new ArrayList<>();
		command.add(ffmpegPath);
		command.add("-i");
		command.add(videoInputPath);
		command.add(videoOutputPath);
		// 执行操作
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();
		InputStream errorStream = process.getErrorStream();
		InputStreamReader isr = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		while ((line = br.readLine()) != null) {
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
