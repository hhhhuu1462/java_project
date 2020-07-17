package Cafe_Main;

// 결제창 gui

import javax.swing.JFrame;
import java.awt.Panel;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Payment extends JFrame {

	private static final long serialVersionUID = 1980824391072986087L;

	JTextField payMoney;
	JButton payBtn;
	Panel panel; 
	JLabel recieveMoney;	

	public Payment() {
		super("결제창");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 328, 105);
		getContentPane().setLayout(null);

		panel = new Panel();
		panel.setBounds(10, 10, 292, 52);
		getContentPane().add(panel);
		panel.setLayout(null);

		payMoney = new JTextField();
		payMoney.setBounds(76, 10, 123, 32);
		panel.add(payMoney);
		payMoney.setColumns(5);

		payBtn = new JButton("결제");
		payBtn.setBounds(211, 14, 69, 23);
		panel.add(payBtn);

		recieveMoney = new JLabel("받은 금액");
		recieveMoney.setBounds(12, 13, 60, 24);
		panel.add(recieveMoney);

		setVisible(true);
	}

}