package extensions.download;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TestThread {

	public static void main(String[] args) throws Exception {
		// set up
		System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		Scanner console = new Scanner(System.in);
//		System.out.println("Directory Path: ");
//		String path = console.nextLine();
//		File dir = new File(path);
		File f = new File("C:\\Users\\Timmy\\Desktop\\\"Classy Babe becomes an Unclassy Slut! *4k* Samantha Flair - Pornhub.com.mp4\"");
		System.out.println(f.getAbsolutePath());
		f.createNewFile();
	}

}

// C:\Users\Timmy\Desktop\download