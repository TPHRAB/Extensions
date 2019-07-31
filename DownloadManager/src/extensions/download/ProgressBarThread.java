package extensions.download;

import java.io.PipedReader;

public class ProgressBarThread implements Runnable {

    private PipedReader pR;
    private long total;
    private String taskName;
    private String unit;
    private int num;
    private String threadNum;
    private Thread thread;

    public ProgressBarThread(PipedReader pR, String taskName, long total, String unit, int num, String threadNum) {
        this.threadNum = threadNum;
        this.pR = pR;
        this.taskName = taskName;
        this.total = total;
        this.unit = unit;
        this.num = num;
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        System.out.print("[*** " + taskName + ": 0% " + "(0/" + total + "), " + total + " " + unit + " | Threads: "
                + threadNum + " ***]");
        long current = 0;
        long counter = 0;
        long start = System.currentTimeMillis();
        while (current < total) {
            try {
                char[] buffer = new char[1024];
                int count = pR.read(buffer);
                for (int i = 0; i < count; i++) {
                    counter += buffer[i];
                }
                current = counter / num;
                double currentRounded = Math.round(current * 100.0 / total);
                System.out.print("\r" + "[*** " + taskName + ": " + currentRounded + "% ("
                        + current + "/" + total + "), " + "Total: " + total + " " + unit  + " | Threads: "
                        + threadNum + " ***]");
            } catch (Exception e) {}
        }
        System.out.println("\r" + taskName + ": " + 100 + "% (" + current + "/" + total +
                "), " + total + " " + unit  + " | Threads: " + threadNum + ", done.");
        System.out.println(getBold("Time consumed: " + (System.currentTimeMillis() - start) / 1000.0
                +  " seconds"));
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
