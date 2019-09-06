// Timmy Zhao

// 08/24/19

// "DownloadManager" class supplies static methods for downloading files

package extensions.download;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.seleniumhq.jetty9.server.session.FileSessionDataStore;

import extensions.progresbar.ProgressBar;

import static extensions.progresbar.ProgressBarThread.getBold;

import java.io.File;
import java.io.IOException;
import java.io.PipedWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadManager {

	// pre   : 1. threadNum >= 1 && fileLength / threadNum != 0 (IllegalArgumentException)
	//         2. pW has connected to the PipedReader which is used by the progressbar
	//         3. out exists or out can be created && url is accessible (throws IOException if not)
    // post  : download the file from the url and output it to "out"
	// params: url               --- file to download
	//         out               --- file to generate (overwrite the file if it already exists)
	//         threadNum         --- number of threads to use to download the file
	//         pW                --- PipedWriter that has been connected to a PipedReader to send
	//                               current progress
	//         requestProperties --- additional http request properties to add on the default 
	//                               download request (null means nothing to add)                      
    public static void doDownloadSingleFile(URL url, File out, int threadNum, PipedWriter pW, 
    		Map<String, String> requestProperties) throws IOException, IllegalArgumentException {
    	if (out.exists()) {
    		out.delete();
    	}
    	out.createNewFile(); // throw IOException if the file cannot been created
    	
    	try {
	        Download threads = new Download(url, out, threadNum, pW, requestProperties);
	        threads.start();
	        threads.join();
    	} catch (InterruptedException e1) {
    		// when thread cannot join
    		e1.printStackTrace();
    	}
        
        // update last modified time
        out.setLastModified(System.currentTimeMillis());
    }

    // pre    : dir is a directory that hasn't been created yet && "first" and "last"
    //          have the same length (throws IllegalArgumentException if not)
    // post   : donwload ts files from "first" to "last" and output them to dir
    //          (without using zero as prefix)
    // params : first     --- first part of common string in ts list
    //          last      --- last part of common string ts list
    //          dir       --- directory to output
    //          threadNum --- number of threads to use to download the list
    //          requestProperties --- additional http request properties to add on the default 
	//                                download request (null means nothing to add)
    public static void downloadTSFileList(String first, String last, File dir, int threadNum, 
    		Map<String, String> requestProperties) throws IllegalArgumentException {
    	if (dir.exists()) {
    		if (dir.isDirectory()) {	
    			throw new IllegalArgumentException();
    		}
    	} else {
            dir.mkdir();
        }
        String difference = getDifference(first, last);
        int bound = Integer.parseInt(difference);
        int startingIndex = last.lastIndexOf(difference);
        String part1 = last.substring(0, startingIndex);
        String part2 = last.substring(startingIndex + difference.length());
        List<String> list = new ArrayList<String>();
        for (int i = 0; i <= bound; i++) {
            String var = addZeros(difference.length() - String.valueOf(i).length()) + i;
            list.add(part1 + var + part2);
        }
        downloadTSFileList(list, dir, threadNum, requestProperties);
    }

    // pre    : dir is a directory that hasn't been created yet
    //          (throws IllegalArgumentException if not)
    // post   : donwload ts files from "first" to "last" and output them to dir
    //          (without using zero as prefix)
    // params : first     --- first part of common string in ts list
    //          last      --- last part of common string ts list
    //          dir       --- directory to output
    //          threadNum --- number of threads to use to download the list
    //          requestProperties --- additional http request properties to add on the default 
	//                                download request (null means nothing to add)
    public static void downloadTSFileListCustom(String part1, String part2, int start, int end,
    		File dir, int threads, Map<String, String> requestProperties)
    				throws IllegalArgumentException {
        List<String> list = new ArrayList<String>();
        for (int i = start; i <= end; i++) {
            list.add(part1 + i + part2);
        }
        downloadTSFileList(list, dir, 10, requestProperties);
    }

    // pre    : dir is a directory that hasn't been created yet
    //          (throws IllegalArgumentException if not)
    // post   : download files from urls represented by string stored in "list"
    // params : list              --- store file url represented by string for downloading
    //          dir               --- directory to output
    //          threadNum         --- thread number to use to download files in "list"
    //          requestProperties --- additional http request properties to add on the default 
	//                                download request (null means nothing to add)
    public static void downloadTSFileList(List<String> list, File dir, int threadNum,
    		Map<String, String> requestProperties) throws IllegalArgumentException {
    	if (dir.exists()) {
    		if (!dir.isDirectory()) {	
    			throw new IllegalArgumentException("Directory already exists!");
    		}
    	} else {
            dir.mkdir();
        }
        int num = list.size() / threadNum;
        if (list.size() % threadNum != 0) {
            threadNum++;
        }
        int start = 0;
        List<MultithreadDownloadList> threadsPool = new ArrayList<MultithreadDownloadList>();
        ProgressBar pb = null;
		try {
			pb = new ProgressBar("Download ts file list", list.size(), "Files", 1,
			        Integer.toString(list.size()));
			pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // start all threads
        for (int i = 0; i < threadNum; i++) {
            int end = start + num - 1;
            end = i == threadNum - 1 ? list.size() - 1 : end;
            MultithreadDownloadList t = new MultithreadDownloadList(list, start, end, dir,
            		pb.getPipedWriter(), requestProperties);
            threadsPool.add(t);
            t.start();
            start += num;
        }
        try {
	        // wait for all subthreads to join
	        for (MultithreadDownloadList t : threadsPool) {
	            t.join();
	        }
	        // if pb created successfully, wait for for pb thread to join
	        if (pb != null) {
				pb.join();
	        }
        } catch (InterruptedException e2) {
        	e2.printStackTrace();
        }
    }

	// pre    : 1. threadNum >= 1 && fileLength / threadNum != 0 (IllegalArgumentException)
	//          2. pW has connected to the PipedReader which is used by the progressbar
	//          3. out exists or out can be created && url is accessible
    //             (throws IOException if not)
    // post   : donwload ts files from "first" to "last" and output them to dir
    // params : list              --- list that contains the url to download
    //          first             --- starting index of the url in "list" to download
    //          last              --- last index of the url in "list" to download
    //          dir               --- directory to output
    //          pW                --- PipedWriter that has been connected to a PipedReader to send
	//                                current progress
    //          requestProperties --- additional http request properties to add on the default 
	//                                download request (null means nothing to add)
    public static void doDownloadTSFileList(List<String> list, int first, int last, File dir,
    		PipedWriter pW, Map<String, String> requestProperties) {
        for (int i = first; i <= last && last < list.size(); i++) {
        	try {
	            URL url = new URL(list.get(i));
	            File out = new File(dir.getAbsoluteFile() + AutoDownload.DIRECTORY_SEPERATOR +
	            		getURLFileName(url));
	            doDownloadSingleFile(url, out, 1, pW, requestProperties);
        	} catch (IOException e1) {
        		e1.printStackTrace();
        	} catch (IllegalArgumentException e2) {
        		e2.printStackTrace();
        	}
        }
    }

    // post  : return number of zeros specifed by count
    // param : count --- number of zeros to be returned
    private static String addZeros(int count) {
        if (count <= 0) {
            return "";
        } else {
            return addZeros(count - 1) + "0";
        }
    }

    // pre    : "a" and "b" have the same length
    // post   : return the difference between "a" and "b"
    // params : "a" --- string to compare with "b"
    //          "b" --- string to compare with "a"
    private static String getDifference(String a, String b) throws IllegalArgumentException {
    	if (a.length() != b.length()) {
    		throw new IllegalArgumentException("Two strings have difference length!");
    	}
    	
        int begin = 0;
        while (a.charAt(begin) == b.charAt(begin)) {
            begin++;
        }
        int end = a.length() - 1;
        while (a.charAt(end) == b.charAt(end)) {
            end--;
        }
        return b.substring(begin, end + 1);
    }

    // pre   : url is not null
    // post  : return the file's name in this url
    // param : url --- the url to get the file's name
    public static String getURLFileName(URL url) {
        return url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
    }

    // pre   : link is a valid url that has a title tag in its static html page 
    //         (throws IOException if not)
    // post  : return the title of the link
    // param : link --- the url to get title
    public static String getURLTitle(String link) throws IOException  {
        Document doc = Jsoup.connect(link).get();
        return doc.title();
    }

    // pre    : url is accessible (throws IOException if not)
    // post   : return the URL file's length
    // params : url               --- file to download
    //          requestProperties --- additional http request properties to add on the default 
	//                                download request (null means nothing to add)
    public static long getURLFileLength(URL url, Map<String, String> requestProperties)
    		throws IOException {
        long contentLength = -1;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(20000);
        connection.setRequestMethod("HEAD");
        if (requestProperties != null) {
	        for (String property : requestProperties.keySet()) {
				connection.setRequestProperty(property, requestProperties.get(property));
			}
        }
        if (connection.getResponseCode() == 200) {
            contentLength = connection.getContentLength();
        } else {
        	System.out.println(getBold("Error: " + url + "\t(" + connection.getResponseCode() +
        			")"));
            throw new SocketTimeoutException();
        }
        return contentLength;
    }

}
