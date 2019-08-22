package test.GUI;

import java.awt.*;
import java.awt.event.*;

import javax.management.monitor.Monitor;

public class FirstWindow {

	public static void main(String[] args) throws InterruptedException {
		Frame frame = new Frame("First Window");
		frame.setBounds(400, 200, 700, 500);
		frame.setLayout(new FlowLayout());
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// create button
		Button bu = new Button("Button");
		bu.setBounds(10, 10, 340, 240);
		bu.addActionListener(new ActionListener() {
			private int count = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				bu.setLabel(count + "");
				count++;
			}
		});
		frame.add(bu);
		
		frame.setVisible(true);
	}

}
