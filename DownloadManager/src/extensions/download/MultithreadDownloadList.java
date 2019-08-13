package extensions.download;

import java.io.File;
import java.io.PipedWriter;
import java.util.List;
import java.util.Map;

public class MultithreadDownloadList implements Runnable {
    private File out;
    private List<String> list;
    private int start;
    private int end;
    private PipedWriter pW;
    private Map<String, String> requesProperties;
    private Thread thread;

    public MultithreadDownloadList(List<String> list, int start, int end, File out, PipedWriter pW, 
    		Map<String, String> requestProperties) {
        this.out = out;
        this.start = start;
        this.end = end;
        this.pW = pW;
        this.list = list;
        this.requesProperties = requestProperties;
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        try {
            DownloadManager.doDownloadTSFileList(list, start, end, out, pW, requesProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        thread.start();
    }
    public void join() throws Exception { thread.join(); }
}
