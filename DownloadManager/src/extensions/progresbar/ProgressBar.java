package extensions.progresbar;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class ProgressBar {
    private ProgressBarThread pb;
    private PipedReader pR;
    private PipedWriter pW;

    public ProgressBar(String taskName, long total, String unit, int num, String threadNum) throws IOException {
        this.pR = new PipedReader();
        this.pW = new PipedWriter();
        this.pR.connect(pW);
        this.pb = new ProgressBarThread(pR, taskName, total, unit, num, threadNum);
    }

    public PipedWriter getPipedWriter() {
        return pW;
    }

    public void start() {
        pb.start();
    }

    public void join() throws InterruptedException {
        pb.join();
    }
}
