package test;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.logging.Level;

public class SearchVideoLink {

    public static final String TARGET_VALUE = "application/x-mpegURL";

    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.keyStore", "icekeystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

        WebClient webClient = new WebClient();
        // webClient.getOptions().setCssEnabled(false);
        // webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage page = webClient.getPage("https://www.baidu.com");
        HtmlForm form = page.getFormByName("f");
        HtmlSubmitInput button = form.getInputByValue("百度一下");
        HtmlTextInput text = form.getInputByName("wd");
        text.type("abc");
        HtmlPage result = button.click();
        System.out.println(result.asXml());
    }
}
