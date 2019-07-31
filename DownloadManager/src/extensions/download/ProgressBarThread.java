package extensions.download;

import java.io.PipedReader;

public class ProgressBarThread implements Runnable {

    private PipedReader pR;
    private long totalBytes;
    private String taskName;
    private Thread thread;

    public ProgressBarThread(PipedReader pR, String taskName, long totalBytes) {
        this.pR = pR;
        this.taskName = taskName;
        this.totalBytes = totalBytes;
        this.thread = new Thread(this);
    }
    @Override
    public void run() {
        double totalRounded = Math.round(totalBytes / 1024.0 / 1024.0 * 100.0) / 100.0;
        System.out.print("[*** " + taskName + ": 0% " + "(0/" + totalBytes + "), " + totalRounded + " MiB ***]");
        long currentBytes = 0;
        long start = System.currentTimeMillis();
        while (currentBytes < totalBytes) {
            try {
                char[] buffer = new char[1024];
                int count = pR.read(buffer);
                for (int i = 0; i < count; i++) {
                    currentBytes += buffer[i];
                }
                double currentRounded = Math.round(currentBytes * 100.0 / totalBytes);
                System.out.print("\r" + "[*** " + taskName + ": " + currentRounded + "% ("
                        + currentBytes + "/" + totalBytes + "), " + totalRounded + " MiB ***]");
            } catch (Exception e) {}
        }
        System.out.println("\r" + taskName + ": " + 100 + "% (" + currentBytes + "/" + totalBytes +
                "), " + totalRounded + " MiB, done.");
        System.out.println(getBold("Total time consumed: " + (System.currentTimeMillis() - start) / 1000.0
                +  "seconds"));
    }

    public void start() {
        thread.start();
    }

    public void join() throws Exception {
        thread.join();
    }

    public static String getBold(String str) {
        return "\33[;;1m" + str + "\33[;;0m";
    }
}
