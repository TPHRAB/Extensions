import java.io.*;
import java.util.*;

public class GetSpending {
   public static void main(String[] args) throws Exception {
      System.out.print("Please input the spending file: ");
      Scanner sc = new Scanner(System.in);
      File f = new File(sc.nextLine());
      File out = new File("out.txt");
      out.createNewFile();
      PrintWriter printer = new PrintWriter(out);
      sc = new Scanner(f);
      double partial = 0;
      double total = 0;
      while (sc.hasNextLine()) {
         String thisLine = sc.nextLine();
         if (!thisLine.isEmpty() && thisLine.charAt(0) == '\t') {
            partial += Double.valueOf(thisLine.substring(thisLine.indexOf('-') + 1).trim());
         } else if (thisLine.trim().isEmpty() && partial != 0) {
            printer.println("\t\tparital total: " + partial);
            total += partial;
            partial = 0;
         } 
         printer.println(thisLine);
      }
      if (partial != 0) {
         printer.println("\t\tparital total: " + partial);
      }  
      printer.println();
      printer.println("********** Total: " + total + " **********"); 
      printer.close();
   }
}