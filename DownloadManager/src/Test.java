import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import java.awt.Font;
import javax.swing.JScrollPane;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class Test extends JFrame {

	private JPanel contentPane;
	private BufferedReader reader;
	private JProgressBar pb;
	private String title;
	private int total;
	private PipedReader pR;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test frame = new Test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public Test() throws IOException {
		// set title
		this.setTitle(title);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		pb = new JProgressBar(0, 10000);
		pb.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		pb.setBounds(65, 19, 313, 28);
		contentPane.add(pb);
		
		JPanel panel = new JPanel();
		panel.setBounds(29, 47, 391, 195);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 6, 379, 183);
		panel.add(scrollPane_1);
		
		JTextArea area1 = new JTextArea();
		scrollPane_1.setViewportView(area1);
		
		JButton btnFinish = new JButton("Finish");
		btnFinish.setBounds(354, 250, 90, 21);
		btnFinish.setEnabled(false);
		contentPane.add(btnFinish);
		
//		init();
		
		new Runnable() {
			private Thread thread = new Thread(this);
			
			public void run() {
				try {
					for (int i = 0; i < 10000; i++) {
						Thread.sleep(5);
						pb.setValue(pb.getValue() + 1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			public void start() {
				thread.start();
			}
		}.start();
	}
	
//	public void init() {
//		extensionssions.view.GuiUtils.setFrameCenter(this);
//		this.setResizable(false);
//	}
	
}
