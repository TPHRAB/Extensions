package test.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Calculator extends JFrame {
	private JPanel contentPane;
	private JTextField var1;
	private JTextField var2;
	private JTextField var3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator frame = new Calculator();
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
	public Calculator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 209);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFirstNumber = new JLabel("first number");
		lblFirstNumber.setBounds(39, 59, 79, 15);
		contentPane.add(lblFirstNumber);
		
		JLabel lblSecondLabel = new JLabel("second label");
		lblSecondLabel.setBounds(170, 59, 79, 15);
		contentPane.add(lblSecondLabel);
		
		JLabel lblResult = new JLabel("result");
		lblResult.setBounds(283, 59, 54, 15);
		contentPane.add(lblResult);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"+", "-", "*", "/"}));
		comboBox.setToolTipText("");
		comboBox.setBounds(128, 81, 32, 21);
		contentPane.add(comboBox);
		
		var1 = new JTextField();
		var1.setBounds(39, 81, 79, 21);
		contentPane.add(var1);
		var1.setColumns(10);
		
		var2 = new JTextField();
		var2.setBounds(170, 81, 79, 21);
		contentPane.add(var2);
		var2.setColumns(10);
		
		JLabel label = new JLabel("=");
		label.setBounds(257, 84, 16, 15);
		contentPane.add(label);
		
		var3 = new JTextField();
		var3.setBounds(283, 81, 79, 21);
		contentPane.add(var3);
		var3.setColumns(10);
		
		JButton btnCalculate = new JButton("calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String regex = "\\d+";
				String num1 = var1.getText();
				String num2 = var2.getText();
				if (!num1.matches(regex) || !num2.matches(regex)) {
					JOptionPane.showMessageDialog(contentPane, "characters cannot be calculated");
				} else {
					String operator = comboBox.getSelectedItem().toString();
					int n1 = Integer.parseInt(num1);
					int n2 = Integer.parseInt(num2);
					switch(operator) {
					case "+":
						var3.setText(n1 + n2 + "");
						break;
					case "-":
						var3.setText(n1 - n2 + "");
						break;
					case "/":
						var3.setText(n1 / n2 + "");
						break;
					case "*":
						var3.setText(n1 * n2 + "");
						break;
					}
				}
			}
		});
		btnCalculate.setBounds(279, 121, 95, 23);
		contentPane.add(btnCalculate);
	}
}
