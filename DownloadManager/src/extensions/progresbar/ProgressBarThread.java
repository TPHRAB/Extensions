package extensions.progresbar;

import java.io.PipedReader;

public class ProgressBarThread implements Runnable {

    private PipedReader pR;
    private long total;
    private String taskName;
    private int unit;
    private String additionalInformation;
    private Thread thread;

    public ProgressBarThread(PipedReader pR, String taskName, long total, String additionalInfo, int unit, String info) {
        this.pR = pR;
        this.taskName = taskName;
        this.total = total;
        this.unit = unit;
        this.additionalInformation = " | " + additionalInfo + ": " + info;
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        System.out.print("[*** " + taskName + ": 0% " + "(0/" + total + "), " + total + " threads" + additionalInformation + " ***]");
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
                current = counter / unit;
                double currentRounded = Math.round(current * 100.0 / total);
                System.out.print("\r" + "[*** " + taskName + ": " + currentRounded + "% ("
                        + current + "/" + total + "), " + "Total: " + total + " threads"
                		+ additionalInformation + " ***]");
            } catch (Exception e) {}
        }
        System.out.println("\r" + taskName + ": " + 100 + "% (" + current + "/" + total +
                "), " + total + " threads"  + additionalInformation + ", done.");
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
