import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JProgressBar;
import java.awt.Font;
import javax.swing.JScrollPane;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProgressBarFrame extends JFrame {

	private JPanel contentPane;
	private JProgressBar pb;
	private JTextArea area;
	private JButton finish;

	/**
	 * Launch the application.
	 */
	public static void main(String[] main) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		pb = new JProgressBar();
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
		
		area = new JTextArea();
		area.setEditable(false);
		DefaultCaret caret = (DefaultCaret)area.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane_1.setViewportView(area);
		
		finish = new JButton("Finish");
		finish.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				((JFrame) contentPane.getTopLevelAncestor()).dispose();
			}
		});
		finish.setBounds(354, 250, 90, 21);
		finish.setEnabled(false);
		contentPane.add(finish);
		
		// init
		// init();
		
	}
	
	public void init() {
		extensions.view.GuiUtils.setFrameCenter(this);
		this.setResizable(false);
	}

	public void setProgressBarValue(int value) {
		pb.setValue(value);
	}
	
	public int getProgressBarValue() {
		return pb.getValue();
	}

	public void startPB(int total, PipedReader pR) {
		pb.setMaximum(total);
		System.out.println(total);
		new Runnable() {
			private Thread thread = new Thread(this);
			
			public void run() {
				while (pb.getPercentComplete() < 1.0) {
					try {
						char[] buffer = new char[1024];
						int count = pR.read(buffer);
						if (count == -1) {
							continue;
						}
						System.out.println(String.valueOf(buffer));
						area.append(String.valueOf(buffer, 0, count));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			public void start() {
				thread.start();
			}
		}.start();
		if (pb.getPercentComplete() == 1.0) {
			finish.setEnabled(true);
		}
	}
}
