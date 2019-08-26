package test.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Window extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws UnsupportedLookAndFeelException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(extensions.view.GuiUtils.FAST_LOOK_AND_FEEL);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
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
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFirstMenu = new JMenu("First Menu");
		menuBar.add(mnFirstMenu);
		
		JMenuItem mntmRed = new JMenuItem("Red");
		mntmRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.setBackground(Color.RED);
			}
		});
		mnFirstMenu.add(mntmRed);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("UserName: ");
		lblUsername.setBounds(61, 73, 74, 16);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("PassWord:");
		lblPassword.setBounds(61, 103, 74, 16);
		contentPane.add(lblPassword);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c < '0' || c > '9') {
					e.consume();
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (c < '0' || c > '9') {
					e.consume();
				}
			}
		});
		textField.setBounds(161, 68, 130, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c < '0' || c > '9') {
					e.consume();
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (c < '0' || c > '9') {
					e.consume();
				}
			}
		});
		textField_1.setBounds(161, 98, 130, 26);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		// init have to be at the end since set windows at the center need to 
		// be performed at the end to resolve any setting location before init()
		init();
	}
	
	public void init() {
		extensions.view.GuiUtils.setFrameImage(this, "source\\1.jpg");
		extensions.view.GuiUtils.setFrameCenter(this);
	}
}
