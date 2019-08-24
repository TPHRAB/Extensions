package test.GUI;

import java.awt.*;
import java.awt.event.*;
public class FourthWindow {
	public static void main(String[] args) {
		Frame f = new Frame("Fourth Window");
		f.setBounds(10, 10, 340, 240);
		f.setLayout(new FlowLayout());
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// set Label
		Label label = new Label("Please input your phone number, cannot be letter: ");
		TextField tf = new TextField(40);
		// set KeyListener
		// all three methods need to be synchronized since only consuming KeyEvent in keyTyped will not cause other methods 
		// to consume the same event
		tf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// cancel event if the character input is not a number
				// 1.get character
				char c = e.getKeyChar();
				// 2.cancel event
				if (c < '0' || c > '9')
					e.consume();
			}
			
			public void keyPressed(KeyEvent e) {
				// cancel event if the character input is not a number
				// 1.get character
				char c = e.getKeyChar();
				// 2.cancel event
				if (c < '0' || c > '9')
					e.consume();
			}
		});
		
		// add to frame
		f.add(label);
		f.add(tf);
		
		f.setVisible(true);
	}
}
