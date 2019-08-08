package test;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4jExtension.Dom4jUtil;

public class XPath {

	public static void main(String[] args) throws Exception {
		Document d = Dom4jUtil.getDocument("XPath.xml");
		Element BBB = (Element) d.selectSingleNode("//BBB");
		System.out.println(BBB.asXML());
		Element CCC = (Element) BBB.selectSingleNode("./CCC[1]");
		System.out.println(CCC.asXML());
	}

}
