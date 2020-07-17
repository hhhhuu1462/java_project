package Cafe_Main;

// menu 할인 gui

import javax.swing.JButton;
import javax.swing.JFrame;

public class Discount extends JFrame {

	private static final long serialVersionUID = 8951179806606896315L;
	
	JButton btn10, btn20, btnFree;

	public Discount() {
		setTitle("할인");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(1020, 100, 373, 100);
		getContentPane().setLayout(null);
		
		btn20 = new JButton("20%할인");
		btn20.setBounds(110, 10, 97, 45);
		getContentPane().add(btn20);
		
		btnFree = new JButton("스마트멤버십쿠폰");
		btnFree.setBounds(208, 10, 147, 45);
		getContentPane().add(btnFree);
		
		btn10 = new JButton("10%할인");
		btn10.setBounds(12, 10, 97, 45);
		getContentPane().add(btn10);
		
		setResizable(false);
		setVisible(true);
	}
}