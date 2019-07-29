package extensions.download;

import java.io.File;

public class WatchFileSize implements Runnable {
	
	private File target;
	private long resultSize;
	private Thread thread;

	public WatchFileSize(File f, long resultSize) {
		this.target = f;
		this.resultSize = resultSize;
		this.thread = new Thread(this);
	}
	
	@Override
	public void run() {
		try {
			long start = System.currentTimeMillis();
			while (target.length() < resultSize) {
				Thread.sleep(500);
			}
			System.out.println(System.currentTimeMillis() - start);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		thread.start();
	}
	
	public void join() throws Exception {
		thread.join();
	}
	

}
