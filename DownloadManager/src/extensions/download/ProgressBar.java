package extensions.download;

import java.io.PipedReader;
import java.io.PipedWriter;

public class ProgressBar {
    private ProgressBarThread pb;
    private PipedReader pR;
    private PipedWriter pW;

    public ProgressBar(String taskName, long totalBytes) throws Exception {
        this.pR = new PipedReader();
        this.pW = new PipedWriter();
        this.pR.connect(pW);
        this.pb = new ProgressBarThread(pR, taskName, totalBytes);
    }

    public PipedWriter getPipedWriter() {
        return pW;
    }

    public void start() {
        pb.start();
    }

    public void join() throws Exception {
        pb.join();
    }
}
