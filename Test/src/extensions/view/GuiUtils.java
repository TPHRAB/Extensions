package extensions.view;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class GuiUtils {
	public static String FAST_LOOK_AND_FEEL = "com.jtattoo.plaf.fast.FastLookAndFeel";
	
	// set image for frame
	public static void setFrameImage(JFrame jf, String image) {
		// get toolkit class
		Toolkit tk = Toolkit.getDefaultToolkit();
		// get image from path
		Image i = tk.getImage(image);
		// set image for JFrame
		jf.setIconImage(i);
	}
	
	// set frame open in the middle of the screen
	public static void setFrameCenter(JFrame jf) {
		// get toolkit class
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		// get screen dimension
		Dimension d = tk.getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		// get frame dimension
		int frameWidth = jf.getWidth();
		int frameHeight = jf.getHeight();
		
		// set frame location
		int x = (int) ((width - frameWidth) / 2);
		int y = (int) ((height - frameHeight) / 2);
		jf.setLocation(x, y);
		
	}
	
}
