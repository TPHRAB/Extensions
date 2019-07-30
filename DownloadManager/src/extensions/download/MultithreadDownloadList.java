package extensions.download;

import java.io.File;
import java.util.List;

public class MultithreadDownloadList implements Runnable {
    private File out;
    private String start;
    private String end;
    private Thread thread;

    public MultithreadDownloadList(String start, String end, File out) {
        this.out = out;
        this.start = start;
        this.end = end;
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        try {
            DownloadManager.doDownloadTSFileList(start, end, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        thread.start();
    }
    public void join() throws Exception { thread.join(); }
}
