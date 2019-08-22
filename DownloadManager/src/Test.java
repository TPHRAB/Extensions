import java.net.HttpURLConnection;
import java.net.URL;
import java.io.File;

import extensions.download.DownloadThread;
import extensions.progresbar.ProgressBar;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File out = new File("/Users/zhaochenze/Desktop/1.mp4");
		out.createNewFile();
		URL url = new URL("http://v.avgigi.com/acg/1903/190301_K9cj3BC.mp4");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Refer", "http://avbebe.com/archives/48318");
		connection.setRequestProperty("Range", "bytes=" + 0 + "-");
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setRequestProperty("Cache-Control", "max-age=0");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:68.0) Gecko/20100101 Firefox/68.0");
		ProgressBar pb = new ProgressBar("Download", 1, "File", 1, "1");
		pb.start();
		new DownloadThread(connection, out, 0, pb.getPipedWriter()).start();;
		pb.join();
	}

}
