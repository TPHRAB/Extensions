// Timmy Zhao

// 08/15/2019

// "ProgressBarThread" class serves to be a thread to to show command line progressbar

package extensions.progresbar;

import java.io.PipedReader;

public class ProgressBarThread implements Runnable {

    private PipedReader pR;               // pipe to read the current progress from
    private long total;                   // total number to read from pR before stop
    private String taskName;              // name of the task to do
    private int unit;                     // total number (read from pR) * "unit" = "total"
    private String additionalInformation; // additional information to add at the end of
                                          // the progressbar
    private Thread thread;                // the thread which represents the instance object

    // pre    : pR has been connected to a pW
    // post   : construct a ProgressBarThread waiting to start
    // params : pR            --- pipe to read the current progress from
    //          taskName      --- name of the task to do
    //          total         --- total number to read from pR before stop
    //          title         --- title of the additional information
    //          unit          --- total number (read from pR) * "unit" = "total"
    //          info          --- additional information to add at the end of the progressbar
    public ProgressBarThread(PipedReader pR, String taskName, long total, String title,
    		int unit, String info) {
        this.pR = pR;
        this.taskName = taskName;
        this.total = total;
        this.unit = unit;
        this.additionalInformation = " | " + title + ": " + info;
        this.thread = new Thread(this);
    }

    @Override
    // post : task for the instance thread to do
    public void run() {
        System.out.print("[*** " + taskName + ": 0% " + "(0/" + total + "), " + total +
        		" threads" + additionalInformation + " ***]");
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
        System.out.println(getBold("Time consumed: " + (System.currentTimeMillis() - start) /
        		1000.0 +  " seconds"));
    }

    // post : start the instatnce thread
    public void start() {
        thread.start();
    }

    // pre  : this intance thread can join properly (throws InterrupetedException if not)
    // post : wait for the instance thread to join
    public void join() throws InterruptedException {
        thread.join();
    }

    // post  : return a command line bold representation of "str"
    // param : str to get bold
    public static String getBold(String str) {
        return "\33[;;1m" + str + "\33[;;0m";
    }
}
