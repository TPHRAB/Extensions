package extensions.dom4j;

import java.io.*;
import org.dom4j.*;
import org.dom4j.io.*;

// Dom4jUtil simplifies procedure to manipulate SAX and XMLWriter
public class Dom4jUtil {
   
   // post : return document for the xml file with the specified "filePath"
   public static Document getDocument(String filePath) {
      try {
         SAXReader reader = new SAXReader();
         return reader.read(filePath);
      } catch (Exception e) {
         e.printStackTrace();
      }  
      return null;
   }
   
   // post : write "document" to "filePath"
   public static void writeBack(String filePath, Document document) {
      try {
         XMLWriter writer = new XMLWriter(new FileOutputStream(filePath), 
                                          OutputFormat.createPrettyPrint());
         writer.write(document);
         writer.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
