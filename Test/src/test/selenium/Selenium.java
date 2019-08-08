package test.selenium;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DriverCommand;

public class Selenium {

	public static void main(String[] args) {
		System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		System.setProperty("webdriver.gecko.driver", "./geckodriver");
		
		WebDriver browser = new FirefoxDriver();
		// set time interval to wait after loading a new page so that xpath could work fine
		browser.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		browser.get("https://myplan.uw.edu");

		// page 1
		WebElement a1 = browser.findElement(By.xpath("//a[@href='/home/login/netid?rd=/home/api/redirect']"));
		a1.click();
		
		// page 2
		if (browser.getTitle().equals("UW NetID sign-in")) {
			WebElement b1 = browser.findElement(By.id("weblogin_netid"));
			b1.clear();
			b1.sendKeys("");
			WebElement b2 = browser.findElement(By.id("weblogin_password"));
			b2.clear();
			b2.sendKeys("");
			WebElement b3 = browser.findElement(By.id("submit_button"));
			b3.click();
		}
		
		// page 3
		WebElement c1 = browser.findElement(By.xpath("//a[@href='/course']"));
		c1.click();
		
		// page 4
		WebElement d1 = browser.findElement(By.name("searchQuery"));
		d1.clear();
		d1.sendKeys("CSE 373");
		WebElement d2 = browser.findElement(By.xpath("//button[text()='Search']"));
		d2.click();
		WebElement d3 = browser.findElement(By.xpath("//span[text()='CSE 373']"));
		d3.click();
		
		// page 5
		List<WebElement> courseList = browser.findElements(
				By.xpath("//div[@class='course-sections-container']/table[1]/tbody"));
		for (int i = 1; i < courseList.size(); i++) {
			String courseCode = courseList.get(i).findElement(
					By.xpath("./tr[1]/td[2]/div/span[2]")).getText();
			String courseStatus = courseList.get(i).findElement(
					By.xpath("./tr[1]/td[7]/div/div[1]")).getText();
			System.out.println(courseCode + ": " + courseStatus);
		}
		Scanner console = new Scanner(System.in);
		browser.close();
	}

}
