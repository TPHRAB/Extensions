package test.GUI;

import java.awt.*;
import java.awt.event.*;

public class SecondWindow {
	public static void main(String[] args) {
	    Frame f = new Frame("Second Window");
	    f.setBounds(10, 10, 340, 240);
	    f.setLayout(new FlowLayout());
	    f.addWindowListener(new WindowAdapter() {
	      // @override
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    
	    // create TextField
	    final TextField tf = new TextField(20);
	    // create button
	    Button bu = new Button("data transfer");
	    // create TextArea
	    final TextArea ta = new TextArea(10, 40); //带本文本域可以放10行40列
	      
	    // add event to button
	    bu.addActionListener(new ActionListener() {
	      // @override
	      public void actionPerformed(ActionEvent e) {
	        // get string in text field
	        String tfString = tf.getText().trim();
	        // append string to text area
	        ta.append(tfString + "\r\n");
	        // clear text field
	        tf.setText("");
	        
	        // set focus to text field
	        tf.requestFocus();
	      }
	    });
	    
	    // add components to frame
	    f.add(tf);
	    f.add(bu);
	    f.add(ta);
	    
	    // set frame visible
	    f.setVisible(true);
	  }
}
