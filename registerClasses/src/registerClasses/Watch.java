package registerClasses;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

public class Watch {
	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassoword", "123456");

		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_60);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getCookieManager().setCookiesEnabled(true);
		// webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setActiveXNative(true);
		webClient.getCookieManager().setCookiesEnabled(true);

	    // initial page
		HtmlPage doc = webClient.getPage("https://myplan.uw.edu/home/goDiscovery?rd=/home/api/redirect");
		HtmlElement a = (HtmlElement) doc.getAnchorByHref("/home/login/netid?rd=/home/api/redirect");
		HtmlPage loginPage = a.click();
		
		// logedIn page
		HtmlElement usrname = (HtmlElement) loginPage.getElementById("weblogin_netid");
		usrname.focus();
		usrname.type("");
		HtmlElement password = (HtmlElement) loginPage.getElementById("weblogin_password");
		password.focus();
		password.type("");
		HtmlPage logedIn = loginPage.getElementById("submit_button").click();
		webClient.waitForBackgroundJavaScript(10000);
		System.out.println(logedIn.asXml());
		
		// get Page
		HtmlPage classToRegister = webClient.getPage(
				"https://myplan.uw.edu/course/#/courses/CSE%20373");
		webClient.waitForBackgroundJavaScript(10000);
		System.out.println(classToRegister.asXml());
		
		
	}
}
