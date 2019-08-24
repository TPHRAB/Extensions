package test.GUI;

import java.awt.*;
import java.awt.event.*;

public class ThirdWindow {
	public static void main(String[] args) {
		Frame f = new Frame("ThridWindow");
		f.setBounds(10, 10, 340, 240);
		f.setLayout(new FlowLayout());
		f.addWindowListener(new WindowAdapter() {
			// @override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// red button
		Button red = new Button("red");
		red.addMouseListener(new MouseAdapter() {
			@Override
			// mouse event that happens after clicking once
			public void mouseClicked(MouseEvent e) {
				f.setBackground(Color.RED);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				f.setBackground(Color.RED);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				f.setBackground(Color.WHITE);
			}
		});
		
		// green button
		Button green = new Button("green");
		green.addMouseListener(new MouseAdapter() {
			@Override
			// mouse event that happens after clicking once
			public void mouseClicked(MouseEvent e) {
				f.setBackground(Color.GREEN);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				f.setBackground(Color.GREEN);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				f.setBackground(Color.WHITE);
			}
		});
		// blue button
		Button blue = new Button("blue");
		blue.addMouseListener(new MouseAdapter() {
			@Override
			// mouse event that happens after clicking once
			public void mouseClicked(MouseEvent e) {
				f.setBackground(Color.BLUE);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				f.setBackground(Color.BLUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				f.setBackground(Color.WHITE);
			}
		});
		// add componets
		f.add(red);
		f.add(green);
		f.add(blue);
		// show frame
		f.setVisible(true);
	}
}
