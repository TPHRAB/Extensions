package test.GUI;

import java.awt.*;
import java.awt.event.*;

public class FifthWindow {
	public static void main(String[] args) {
		Frame f = new Frame("Fifth Window");
		f.setBounds(10, 10, 340, 240);
		f.setLayout(new FlowLayout());
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// create MenuBar
		MenuBar mb = new MenuBar();
		// create Menu
		Menu m = new Menu("first menu");
		// create Menu under menu
		Menu m2 = new Menu("menu in menu");
		// create MenuItem
		MenuItem item = new MenuItem("set red");
		// add event to item
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				f.setTitle(item.getLabel());
				f.setBackground(Color.RED);
			}
		});
		
		
		// add menu
		m2.add(item);
		m.add(m2);
		mb.add(m);
		// set menu bar
		f.setMenuBar(mb);
		
		
		f.setVisible(true);
	}
}
