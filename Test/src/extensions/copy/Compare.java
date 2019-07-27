package extensions.copy;

import java.io.*;

public class Compare {
    public static void main(String[] args) throws Exception {
        getDifference();;
    }

    public static void getDifference() throws Exception {
        File f1 = new File("C:\\Users\\Timmy\\Desktop\\raw");
        File f2 = new File("C:\\Users\\Timmy\\Desktop\\1.ts");
        InputStream in1 = new FileInputStream(f2);
        File[] list = f1.listFiles();
        int b1 = 0;
        int b2 = 0;
        for (File f : list) {
            int count = 0;
            InputStream in2 = new FileInputStream(f);
            while ((b1 = in1.read()) != -1 && (b2 = in2.read()) != -1) {
                if (b1 != b2) {
                    System.out.println(f.getName() + " count: " + count);
                    System.out.println(f.length());
                    Thread.sleep(100000000);
                }
                count++;
            }
        }
    }
}
