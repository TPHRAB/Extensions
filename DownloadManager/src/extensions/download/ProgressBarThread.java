package extensions.download;

import java.io.PipedReader;

public class ProgressBarThread implements Runnable {

    private PipedReader pipeR;
    private long totalBytes;
    private String taskName;
    private Thread thread;

    public ProgressBarThread(PipedReader pipeR, String taskName, long totalBytes) {
        this.pipeR = pipeR;
        this.taskName = taskName;
        this.totalBytes = totalBytes;
        this.thread = new Thread(this);
    }
    @Override
    public void run() {
        double totalRounded = Math.round(totalBytes / 1024.0 / 1024.0 * 100.0) / 100.0;
        System.out.print(taskName + ": 0% " + "(0/" + totalBytes + "), " + totalRounded + " MiB");
        long currentBytes = 0;
        while (currentBytes < totalBytes) {
            try {
                char[] buffer = new char[1024];
                int count = pipeR.read(buffer);
                for (int i = 0; i < count; i++) {
                    currentBytes += buffer[i];
                }
                double currentRounded = Math.round(currentBytes * 100.0 / totalBytes);
                System.out.print("\r" + taskName + ": " + currentRounded + "% (" + currentBytes + "/" + totalBytes +
                        "), " + totalRounded + " MiB");
            } catch (Exception e) {}
        }
    }

    public void start() {
        thread.start();
    }

    public void join() throws Exception {
        thread.join();
    }
}
