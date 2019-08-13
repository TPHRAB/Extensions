package test.avgle;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FindTargetStringInJS {
	
	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		System.setProperty("webdriver.gecko.driver", "./geckodriver");
		
		
		String targetURL = "{\"url\":";
		
		WebDriver browser = new FirefoxDriver();
		// set time interval to wait after loading a new page so that xpath could work fine
		browser.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		browser.get("https://avgle.com/video/IiVmzm9D4mn/sh"
				+ "kd-858-%E5%A5%B3%E5%A4%A7%E5%AD%B8%E7%94%9F%E8%BC%AA%E5%A7"
				+ "%A6%E7%A4%BE%E5%9C%98-%E4%BA%8C%E5%AE%AE%E5%85%89-%E4%B8%A"
				+ "D%E6%96%87%E5%AD%97%E5%B9%95");
		List<WebElement> list = browser.findElements(By.xpath("//iframe"));
		for (WebElement frame : list) {
			browser.switchTo().frame(frame);
			List<WebElement> list2 = browser.findElements(By.xpath("//script[@src]"));
			for (WebElement e : list2) {
				String link = e.getAttribute("src");
				if (link != null && link.contains(".js")) {
					if (link.charAt(0) == '/' && link.charAt(1) != '/') {
						link = "https://avgle.com" + link;
					}
					if (link.charAt(0) != '/') {
						HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
						BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String text = "";
						String line = in.readLine();
						while (line != null) {
							text += line;
							line = in.readLine();
						}
						if (text.contains(targetURL)) {
							System.out.println("TargetURL: " + link);
							break;
						}
						System.out.println(link);
					}
				} else {
					String text = e.getText();
					if (text.contains(targetURL)) {
						System.out.println(text);
						break;
					}
				}
			}
			browser.switchTo().defaultContent();
		}
		browser.close();
	}
}

// 用selenium找js加载出的js代码
