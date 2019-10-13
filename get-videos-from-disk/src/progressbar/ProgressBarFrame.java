package progressbar;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.JProgressBar;
import java.awt.Font;

import javax.swing.JScrollPane;


import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProgressBarFrame extends JFrame {

	private JPanel contentPane;
	private JProgressBar pb;
	private JTextArea area;
	private JRadioButton showDetail;
	private JButton finish;
	private JFrame rootFrame;
	public final int DEFAULT_WIDTH = 450;
	public final int DEFAULT_HEIGHT = 300;
	public final int X_DEVIATION = 6;
	private JPanel panel;
	private JScrollPane scrollPane_1;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] main) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(extensions.view.GuiUtils.FAST_LOOK_AND_FEEL);
					ProgressBarFrame frame = new ProgressBarFrame();
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
	public ProgressBarFrame() throws IOException {
		this.rootFrame = this;
		setBounds(100, 100, 469, 131);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// progressbar
		pb = new JProgressBar();
		pb.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		pb.setBounds(70 - X_DEVIATION, 19, rootFrame.getWidth() - 70 * 2, 28);
		contentPane.add(pb);
		
		// finish button
		finish = new JButton("Finish");
		finish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JFrame) contentPane.getTopLevelAncestor()).dispose();
			}
		});
		finish.setBounds(rootFrame.getWidth() - 116, rootFrame.getHeight() - 69, 90, 21);
		finish.setEnabled(false);
		contentPane.add(finish);
		
		// show detail button
		int distance = finish.getY() - pb.getY() - pb.getHeight();
		showDetail = new JRadioButton("Show Detail");
		showDetail.setBounds(6, rootFrame.getHeight() - 69, 141, 23);
		// set finish button
		finish.setLocation(rootFrame.getWidth() - 116, rootFrame.getHeight() - 69);
		
		// show scroll panel
		panel = new JPanel();
		panel.setBounds(29, pb.getY() + distance + pb.getHeight(), rootFrame.getWidth() - 29 * 2 - 10,
				finish.getY() - pb.getY() - distance * 2 - pb.getHeight());
		panel.setLayout(null);

		// information field
		area = new JTextArea();
		area.setEditable(false);
		
		// scroll pane
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 6, panel.getWidth() - 6 * 2, panel.getHeight() - 6 * 2);
		scrollPane_1.setViewportView(area);
		panel.add(scrollPane_1);
		contentPane.add(panel);
		// set area event
		area.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
//				System.out.printf("JFrame: %d, %d, JProgressbar: %d, %d", rootFrame.getWidth(), rootFrame.getHeight(), pb.getValue(), pb.getMaximum());
				
				int scrollbarMax = finish.getY() - pb.getY() - distance * 2 - pb.getHeight();
				JScrollBar vertical = scrollPane_1.getVerticalScrollBar();
				if (vertical.getValue() > scrollbarMax && vertical.getMaximum() - vertical.getValue() > 200 && showDetail.isSelected()) {
					return;
				}
				
				try {
					area.setCaretPosition(area.getLineEndOffset(area.getLineCount() - 1));
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
	    });
		
		// show show detail event
		showDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (showDetail.isSelected()) {
					rootFrame.setResizable(true);
					resizeWindow(DEFAULT_WIDTH, DEFAULT_HEIGHT, distance);
				} else if (!showDetail.isSelected()) {
					rootFrame.setResizable(false);
					setBounds(rootFrame.getX(), rootFrame.getY(), 469, 131);
				}
			}
		});
		contentPane.add(showDetail);
		
		// window resize event 
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (rootFrame.getWidth() < 450 || rootFrame.getHeight() < 300 && showDetail.isSelected()) {
					resizeWindow(DEFAULT_WIDTH, DEFAULT_HEIGHT, distance);
				} else {
					resizeWindow(rootFrame.getWidth(), rootFrame.getHeight(), distance);
				}
			}
		});
		
		// window exit event
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (finish.isEnabled() == false) {
					int choice = JOptionPane.showConfirmDialog(rootPane, 
							"Process not complete. Do you still want to exit?", "Message", JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				} else {
					rootFrame.dispose();
				}
			}
		});
	}
	
	public void init() {
		extensions.view.GuiUtils.setFrameCenter(this);
		this.setResizable(false);
	}

	public void initPB(String title, int total) throws IOException {
		pb.setMaximum(total);
		this.setTitle(title);
		this.setVisible(true);
	}
	
	public JProgressBar getPB() {
		return pb;
	}
	
	public JTextArea getTextArea() {
		return area;
	}
	
	public void enableFinish() {
		finish.setEnabled(true);;
	}
	
	private void resizeWindow(int width, int heigth, int distance) {
		// set root frame
		rootFrame.setBounds(rootFrame.getX(), rootFrame.getY(), width, heigth);
		
		// set progressbar
		pb.setBounds(70 - X_DEVIATION, 19, rootFrame.getWidth() - 70 * 2, 28);
		
		// set finish button
		finish.setLocation(rootFrame.getWidth() - 116, rootFrame.getHeight() - 69);
		
		// set show detail button
		showDetail.setBounds(6, rootFrame.getHeight() - 69, 141, 23);
		
		// show scroll panel
		panel.setBounds(29 - X_DEVIATION, pb.getY() + distance + pb.getHeight(), rootFrame.getWidth() - 29 * 2,
				finish.getY() - pb.getY() - distance * 2 - pb.getHeight());
		
		scrollPane_1.setBounds(6, 6, panel.getWidth() - 6 * 2, panel.getHeight() - 6 * 2);
	}
}
