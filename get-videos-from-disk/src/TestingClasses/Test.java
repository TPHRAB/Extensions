package TestingClasses;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class Test {
	public static void main(String[] args) throws IOException {
		System.out.println("0");
		SwingWorker<Void, Void> thread = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				// TODO Auto-generated method stub
				System.out.println("1");
				return null;
			}
			@Override
			protected void done() {
				System.out.println("Done");
			}	
		};
		thread.execute();
		try {
			thread.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void recursiveHelper(File source, int[] counter) {
		for (File f : source.listFiles()) {
			String regex1 = ".*_.*_[1-2]\\.VOB";
			String regex2 = ".*_TS\\.VOB";
			if (f.isDirectory()) {
				recursiveHelper(f, counter);
			} else if (f.getName().matches(regex1) || f.getName().matches(regex2)) {
//				System.out.println(f.getName());
				counter[1]++;
			} else {
				System.out.println(f.getName());
			}
			counter[0]++;
		}
	}
}
