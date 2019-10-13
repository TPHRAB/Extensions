// Timmy Zhao

// 07/31/2019

// "MultithreadDownloadList" class serves to be a thread class to for downloading list of url

package extensions.download;

import java.io.File;
import java.io.PipedWriter;
import java.util.List;
import java.util.Map;

public class MultithreadDownloadList implements Runnable {
    private File dir;                             // directory to output files to
    private List<String> list;                    // list of url files to download
    private int start;                            // starting index of the url in "list" to
                                                  // download
    private int end;                              // last index of the url in "list" to download
    private PipedWriter pW;                       // PipedWriter to write progress to 
    private Map<String, String> requesProperties; // additional http request properties to add on 
                                                  // the default download request 
    private Thread thread;                        // thread of this instance object

    // pre    : pW is from "ProgressBar"
    // post   : construct a "MultithreadDownloadList" class
    // params : list              --- list that contains the url to download
    //          start             --- starting index of the url in "list" to download
    //          end               --- last index of the url in "list" to download
    //          dir               --- directory to output
    //          pW                --- PipedWriter that has been connected to a PipedReader to send
	//                                current progress
    //          requestProperties --- additional http request properties to add on the default 
	//                                download request  
    public MultithreadDownloadList(List<String> list, int start, int end, File dir, PipedWriter pW, 
    		Map<String, String> requestProperties) {
        this.dir = dir;
        this.start = start;
        this.end = end;
        this.pW = pW;
        this.list = list;
        this.requesProperties = requestProperties;
        this.thread = new Thread(this);
    }

    @Override
    // post : task for this thread
    public void run() {
        DownloadManager.doDownloadTSFileList(list, start, end, dir, pW, requesProperties);
    }

    // post : start this instance thread
    public void start() {
        thread.start();
    }
    
    // pre  : this intance thread can join properly (throws InterrupetedException if not)
    // post : wait for this instance thread to join
    public void join() throws InterruptedException  { 
    	thread.join(); 
    }
}
