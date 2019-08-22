package test.GUI;

import java.awt.*;
import java.awt.event.*;

import javax.management.monitor.Monitor;

public class FirstWindow {

	public static void main(String[] args) throws InterruptedException {
		Frame frame = new Frame("First Window");
		frame.setBackground(Color.DARK_GRAY);
		frame.setSize(400, 300);
		Panel p = new Panel();
		p.setLocation(20, 20);
		p.setSize(360, 260);
		p.setBackground(Color.YELLOW);
		Button b = new Button("click");
		Monitor m = new Monitor();
		frame.setVisible(true);
		frame.add(p);
	}

}
