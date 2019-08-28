package getVideosFromDisk;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {
	private JFrame rootFrame;
	private JPanel contentPane;
	private JTextField destinationPath;
	private JTextField sourcePath;
	private JTextField sourcePath1;
	private JTextField sourcePath2;
	private JTextField destinationPath2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(extensions.view.GuiUtils.FAST_LOOK_AND_FEEL);
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		this.rootFrame = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 816, 309);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnMoveVideos = new JButton("Move Videos");
		btnMoveVideos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout c = (CardLayout) contentPane.getLayout();
				c.show(contentPane, "moveVideos");
			}
		});
		menuBar.add(btnMoveVideos);
		
		JButton btnSortVideos = new JButton("Sort Videos");
		btnSortVideos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout c = (CardLayout) contentPane.getLayout();
				c.show(contentPane, "sortVideos");
			}
		});
		menuBar.add(btnSortVideos);
		
		JButton btnCombineVideos = new JButton("Combine Videos");
		btnCombineVideos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout c = (CardLayout) contentPane.getLayout();
				c.show(contentPane, "combineVideos");
			}
		});
		menuBar.add(btnCombineVideos);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel moveVideos = new JPanel();
		moveVideos.setBackground(Color.WHITE);
		contentPane.add(moveVideos, "moveVideos");
		moveVideos.setLayout(null);
		
		JLabel labelSourcePath = new JLabel("Source Path:");
		labelSourcePath.setFont(new Font("΢���ź� Light", Font.PLAIN, 14));
		labelSourcePath.setBounds(102, 62, 120, 15);
		moveVideos.add(labelSourcePath);
		
		JLabel labelDestinationPath = new JLabel("Destination Path: ");
		labelDestinationPath.setFont(new Font("΢���ź� Light", Font.PLAIN, 14));
		labelDestinationPath.setBounds(102, 112, 120, 15);
		moveVideos.add(labelDestinationPath);
		
		destinationPath = new JTextField();
		destinationPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!destinationPath.getBackground().equals(Color.WHITE)) {
					destinationPath.setText("");
					destinationPath.setBackground(Color.WHITE);
				}
			}
		});
		destinationPath.setBounds(240, 110, 351, 21);
		moveVideos.add(destinationPath);
		destinationPath.setColumns(10);
		
		sourcePath = new JTextField();
		sourcePath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!sourcePath.getBackground().equals(Color.WHITE)) {
					sourcePath.setText("");
					sourcePath.setBackground(Color.WHITE);
				}
			}
		});
		sourcePath.setBounds(240, 60, 351, 21);
		moveVideos.add(sourcePath);
		sourcePath.setColumns(10);
		
		JLabel diskNumber = new JLabel("Disk Added: 0");
		diskNumber.setFont(new Font("΢���ź� Light", Font.PLAIN, 12));
		diskNumber.setBounds(692, 209, 98, 15);
		moveVideos.add(diskNumber);
		
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File in = new File(sourcePath.getText().trim());
				if (!in.exists() || !in.isDirectory() || !in.getName().equals("VIDEO_TS")) {
					JOptionPane.showMessageDialog(contentPane, "Please the DVD's videos' directory!");
					sourcePath.setBackground(Color.RED);
					return;
				}
				File out = new File(destinationPath.getText().trim());
				if (!out.exists() || !out.isDirectory()) {
					JOptionPane.showMessageDialog(contentPane, "Please input a exising directory!");
					destinationPath.setBackground(Color.RED);
					return;
				}
				
				try {
					rootFrame.setVisible(false);
					AutoProcessDisk1.moveVideosFromDisk(in, out, rootFrame);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(contentPane, "Error encountered, program exiting");
					System.exit(ERROR);
				}
				
				String[] info = diskNumber.getText().split(": ");
				int count = Integer.parseInt(info[1]);
				diskNumber.setText(info[0] + ": " + (count + 1));
				int choice = JOptionPane.showConfirmDialog(contentPane, "Message", "Add another disk?", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.OK_OPTION) {
					sourcePath.setText("");
					sourcePath.setBackground(Color.WHITE);
					destinationPath.setText("");
					destinationPath.setBackground(Color.WHITE);
					sourcePath.requestFocus();
				} else {
					sourcePath.setBackground(Color.GREEN);
					destinationPath.setBackground(Color.GREEN);
				}
			}
		});
		btnStart.setBounds(361, 155, 108, 23);
		moveVideos.add(btnStart);
		
		JButton btnChoose = new JButton("browse");
		btnChoose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i = jFileChooser.showOpenDialog(null);
				if(i== jFileChooser.APPROVE_OPTION){ //���ļ�
					sourcePath.setText(jFileChooser.getSelectedFile().getAbsolutePath());
				} else {
					sourcePath.setText("No file selected");
				}
			}
		});
		btnChoose.setBounds(635, 59, 79, 23);
		moveVideos.add(btnChoose);
		
		JButton button_1 = new JButton("browse");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i = jFileChooser.showOpenDialog(null);
				if(i == jFileChooser.APPROVE_OPTION){ //���ļ�
					destinationPath.setText(jFileChooser.getSelectedFile().getAbsolutePath());
				} else {
					destinationPath.setText("No file selected");
				}
			}
		});
		button_1.setBounds(635, 109, 79, 23);
		moveVideos.add(button_1);
		
		JPanel sortVideos = new JPanel();
		sortVideos.setForeground(Color.WHITE);
		sortVideos.setBackground(Color.GRAY);
		contentPane.add(sortVideos, "sortVideos");
		sortVideos.setLayout(null);
		
		JLabel lblSourcePath_1 = new JLabel("Source Path:");
		lblSourcePath_1.setForeground(Color.WHITE);
		lblSourcePath_1.setFont(new Font("΢���ź� Light", Font.PLAIN, 14));
		lblSourcePath_1.setBounds(102, 93, 120, 15);
		sortVideos.add(lblSourcePath_1);
		
		sourcePath1 = new JTextField();
		sourcePath1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!sourcePath1.getBackground().equals(Color.WHITE)) {
					sourcePath1.setBackground(Color.WHITE);
					sourcePath1.setText("");
				}
			}
		});
		sourcePath1.setBounds(240, 91, 351, 21);
		sortVideos.add(sourcePath1);
		sourcePath1.setColumns(10);
		
		JButton btnStart_1 = new JButton("Start");
		btnStart_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File in = new File(sourcePath1.getText());
				if (!in.exists()) {
					JOptionPane.showMessageDialog(contentPane, "Directory doesn't exist!");
					sourcePath1.setBackground(Color.RED);
					return;
				}
				AutoProcessDisk1.sortVideos(in);
			}
		});
		btnStart_1.setBounds(361, 131, 108, 23);
		sortVideos.add(btnStart_1);
		
		JButton button_2 = new JButton("browse");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFChooser = new JFileChooser();
				jFChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i = jFChooser.showOpenDialog(null);
				if (i == jFChooser.APPROVE_OPTION) {
					sourcePath1.setText(jFChooser.getSelectedFile().getAbsolutePath());
				} else {
					sourcePath1.setText("No file selected");
				}
			}
		});
		button_2.setBounds(635, 90, 79, 23);
		sortVideos.add(button_2);
		
		JPanel combineVideos = new JPanel();
		combineVideos.setBackground(Color.DARK_GRAY);
		combineVideos.setForeground(Color.WHITE);
		contentPane.add(combineVideos, "combineVideos");
		combineVideos.setLayout(null);
		
		JLabel label = new JLabel("Source Path:");
		label.setBounds(102, 62, 120, 15);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("΢���ź� Light", Font.PLAIN, 14));
		combineVideos.add(label);
		
		JLabel label_1 = new JLabel("Destination Path: ");
		label_1.setForeground(Color.WHITE);
		label_1.setBounds(102, 112, 120, 15);
		label_1.setFont(new Font("΢���ź� Light", Font.PLAIN, 14));
		combineVideos.add(label_1);
		
		sourcePath2 = new JTextField();
		sourcePath2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!sourcePath2.getBackground().equals(Color.WHITE)) {
					sourcePath2.setText("");
					sourcePath2.setBackground(Color.WHITE);
				}
			}
		});
		sourcePath2.setBounds(240, 60, 351, 21);
		sourcePath2.setColumns(10);
		combineVideos.add(sourcePath2);
		
		destinationPath2 = new JTextField();
		destinationPath2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!destinationPath2.getBackground().equals(Color.WHITE)) {
					destinationPath2.setText("");
					destinationPath2.setBackground(Color.WHITE);
				}
			}
		});
		destinationPath2.setBounds(240, 110, 351, 21);
		destinationPath2.setColumns(10);
		combineVideos.add(destinationPath2);
		
		JButton button = new JButton("Start");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// get in and out
				File in = new File(sourcePath2.getText().trim());
				if (!in.exists() || !in.isDirectory()) {
					JOptionPane.showMessageDialog(contentPane, "Please the DVD's videos' directory!");
					sourcePath2.setBackground(Color.RED);
					return;
				}
				File out = new File(destinationPath2.getText().trim());
				if (!out.exists() || !out.isDirectory()) {
					JOptionPane.showMessageDialog(contentPane, "Please input a exising directory!");
					destinationPath2.setBackground(Color.RED);
					return;
				} else if (out.listFiles().length != 0) {
					JOptionPane.showMessageDialog(contentPane, "Please new create a directory and select it as path!");
					destinationPath2.setBackground(Color.RED);
					return;
				}
				
				
				try {
					rootFrame.setVisible(false);
					AutoProcessDisk1.combineVideos(in, out, rootFrame);
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(contentPane, "Program will exit after confirming...");
					System.exit(ERROR);
				} 
				
				// set text field background color
				sourcePath2.setBackground(Color.GREEN);
				destinationPath2.setBackground(Color.GREEN);
			}
		});
		
		button.setBounds(361, 155, 108, 23);
		combineVideos.add(button);
		
		JButton button_3 = new JButton("browse");
		button_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFChooser = new JFileChooser();
				jFChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i = jFChooser.showOpenDialog(null);
				if (i == jFChooser.APPROVE_OPTION) {
					sourcePath2.setText(jFChooser.getSelectedFile().getAbsolutePath());
				} else {
					sourcePath2.setText("No file selected");
				}
			}
		});
		button_3.setBounds(635, 59, 79, 23);
		combineVideos.add(button_3);
		
		JButton button_4 = new JButton("browse");
		button_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFChooser = new JFileChooser();
				jFChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i = jFChooser.showOpenDialog(null);
				if (i == jFChooser.APPROVE_OPTION) {
					destinationPath2.setText(jFChooser.getSelectedFile().getAbsolutePath());
				} else {
					destinationPath2.setText("No file selected");
				}
			}
		});
		button_4.setBounds(635, 109, 79, 23);
		combineVideos.add(button_4);
		
		// init
		init();
	}
	
	public void init() {
		extensions.view.GuiUtils.setFrameCenter(this);
		this.setResizable(false);
	}
}
