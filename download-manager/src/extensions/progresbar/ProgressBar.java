// Timmy Zhao

// 07/31/2019

// "Progressbar" class can create "ProgressbarThread" can return the PipedWriter that is connected
// with "ProgressBarThread"'s PipedReader for sending current progress

package extensions.progresbar;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class ProgressBar {
    private ProgressBarThread pb; // the thread of the progressbar
    private PipedReader pR;       // PipedReader to read progress from
    private PipedWriter pW;       // PipedWriter to write progress to 

    // pre    : "pR" can connect to "pW" (throws IOException if not)
    // post   : construct a "ProgressBar"
    // params : taskName      --- name of the task to do
    //          total         --- total number to read from pR before stop
    //          title         --- title of the additional information
    //          unit          --- total number (read from pR) * "unit" = "total"
    //          info          --- additional information to add at the end of the progressbar
    public ProgressBar(String taskName, long total, String title, int unit, String info)
    		throws IOException {
        this.pR = new PipedReader();
        this.pW = new PipedWriter();
        this.pR.connect(pW);
        this.pb = new ProgressBarThread(pR, taskName, total, title, unit, info);
    }

    // post : return the PipedWriter to write progress to 
    public PipedWriter getPipedWriter() {
        return pW;
    }

    // post : start this instance thread
    public void start() {
        pb.start();
    }

    // pre  : this intance thread can join properly (throws InterrupetedException if not)
    // post : wait for this instance thread to join
    public void join() throws InterruptedException {
        pb.join();
    }
}
