package Cafe_Main;

// login class

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Login extends JFrame {

	private static final long serialVersionUID = -4628569482773516880L;

	Vector<Info> rowData = null;
	CoffeeDAO coffeeDAO = null;
	
	private JTextField textField;
	private JPasswordField passwordField;

	public Login() {
	
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 379, 190);
		getContentPane().setLayout(null);

		// panel
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 125, 91);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("ID :");
		lblNewLabel.setBounds(96, 10, 29, 29);
		panel.add(lblNewLabel);

		JLabel lblPaword = new JLabel("PASSWORD :");
		lblPaword.setBounds(40, 49, 85, 29);
		panel.add(lblPaword);

		// panel1
		Panel panel_1 = new Panel();
		panel_1.setBounds(131, 0, 243, 91);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		textField = new JTextField();
		textField.setBounds(0, 10, 181, 28);
		panel_1.add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(0, 51, 181, 28);
		panel_1.add(passwordField);

		// panel2
		Panel panel_2 = new Panel();
		panel_2.setBounds(0, 97, 374, 64);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);

		JButton loginBtn = new JButton("로그인");
		loginBtn.setBounds(200, 10, 111, 34);
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = textField.getText();
				String pw = "";
				// passwordfield는 char배열에 넣어준 후 string으로 형변환 시켜주어야 한다
				char[] secret_pw  = passwordField.getPassword();
				
				CoffeeDAO coffeeDAO = new CoffeeDAO();
				
				for (int i = 0; i < secret_pw.length; i++) {
					pw += secret_pw[i];
				}
				
//				for (char cha : secret_pw ) {
//					Character.toString(cha);
//					//pw 에 저장하기, pw 에 값이 비어있으면 저장, 값이 있으면 이어서 저장하는 삼항연산자
//					pw += (pw.equals("")) ? ""+cha+"" : ""+cha+"";
//				}
				
				try {
					// coffeeDAO.login(id, pw) 결과값이 1이면
					if(coffeeDAO.login(id, pw) == 1) {
						new Main();
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "id 또는 password가 틀립니다");
						System.exit(0);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		panel_2.add(loginBtn);
		
		JButton resetBtn = new JButton("reset");
		resetBtn.setBounds(66, 10, 111, 34);
		resetBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				passwordField.setText("");
			}
		});
		panel_2.add(resetBtn);
		
		setVisible(true);	
		setResizable(false);
	}

	public static void main(String[] args) {		
		new Login();
	}
}