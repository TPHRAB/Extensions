import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JProgressBar;
import java.awt.Font;

import javax.swing.JScrollPane;


import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;

public class ProgressBarFrame extends JFrame {

	private JPanel contentPane;
	private JProgressBar pb;
	private JTextArea area;
	private JButton finish;
	private JRadioButton rdbtnShowDetail;
	private JScrollBar horizontal;
	private JScrollBar vertical;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 115);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		pb = new JProgressBar();
		pb.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		pb.setBounds(65, 19, 313, 28);
		contentPane.add(pb);
		
		// information field
		area = new JTextArea();
		area.setEditable(false);
		area.validate();
		
		finish = new JButton("Finish");
		finish.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				((JFrame) contentPane.getTopLevelAncestor()).dispose();
			}
		});
		finish.setBounds(354, 62, 90, 21);
		finish.setEnabled(false);
		contentPane.add(finish);
		
		rdbtnShowDetail = new JRadioButton("Show Detail");
		rdbtnShowDetail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (rdbtnShowDetail.isSelected()) {
					JFrame rootFrame = ((JFrame) contentPane.getTopLevelAncestor());
					rootFrame.setBounds(rootFrame.getX(), rootFrame.getY(), 450, 300);
					JPanel panel = new JPanel();
					panel.setBounds(29, 47, 391, 195);
					contentPane.add(panel);
					panel.setLayout(null);
					
					JScrollPane scrollPane_1 = new JScrollPane();
					scrollPane_1.setBounds(6, 6, 379, 183);
					panel.add(scrollPane_1);
					
					scrollPane_1.setViewportView(area);
					
					contentPane.remove(rdbtnShowDetail);
					finish.setLocation(354, 247);
					
					// get scrollbars
					horizontal = scrollPane_1.getHorizontalScrollBar();
					vertical = scrollPane_1.getVerticalScrollBar();
				}
			}
		});
		rdbtnShowDetail.setBounds(6, 59, 141, 23);
		contentPane.add(rdbtnShowDetail);
		
	}
	
	public void init() {
		extensions.view.GuiUtils.setFrameCenter(this);
		this.setResizable(false);
	}

	public void initPB(String title, int total) throws IOException {
		pb.setMaximum(total);
		this.setTitle(title);
	}
	
	public JProgressBar getPB() {
		return pb;
	}
	
	
	public JTextArea getTextArea() {
		return area;
	}
	
}
