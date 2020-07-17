package Cafe_Main;

// menu 생성 gui

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateMenu extends JFrame {

	private static final long serialVersionUID = -7512119197350873613L;
	
	private JTextField menuCodetf;
	private JTextField menuNametf;
	private JTextField menuPricetf;
	
	public CreateMenu() {
		setTitle("메뉴생성");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 259, 344);
		getContentPane().setLayout(null);
		
		JPanel panel1 = new JPanel();
		panel1.setBounds(12, 10, 82, 220);
		getContentPane().add(panel1);
		panel1.setLayout(null);
		
		JLabel menuCodeLbl = new JLabel("메뉴 코드");
		menuCodeLbl.setBounds(12, 30, 65, 36);
		panel1.add(menuCodeLbl);
		
		JLabel menuNameLbl = new JLabel("메뉴 이름");
		menuNameLbl.setBounds(12, 90, 65, 36);
		panel1.add(menuNameLbl);
		
		JLabel menuPriceLbl = new JLabel("메뉴 가격");
		menuPriceLbl.setBounds(12, 150, 65, 36);
		panel1.add(menuPriceLbl);
		
		JPanel panel3 = new JPanel();
		panel3.setBounds(95, 10, 140, 220);
		getContentPane().add(panel3);
		panel3.setLayout(null);
		
		menuCodetf = new JTextField();
		menuCodetf.setBounds(12, 33, 104, 33);
		panel3.add(menuCodetf);
		menuCodetf.setColumns(10);
		
		menuNametf = new JTextField();
		menuNametf.setColumns(10);
		menuNametf.setBounds(12, 94, 104, 33);
		panel3.add(menuNametf);
		
		menuPricetf = new JTextField();
		menuPricetf.setColumns(10);
		menuPricetf.setBounds(12, 153, 104, 33);
		panel3.add(menuPricetf);
		
		JPanel panel2 = new JPanel();
		panel2.setBounds(12, 240, 223, 60);
		getContentPane().add(panel2);
		panel2.setLayout(null);
		
		JButton btnNewButton = new JButton("생성");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CoffeeDAO coffeeDAO = new CoffeeDAO();	
				String menuCode = menuCodetf.getText();
				String menuName = menuNametf.getText();
				// 메뉴가격은 int로 받아야 하기 때문에 textfield에 작성한 값을 integer로 형변환 시켜야 함
				int menuPrice = Integer.parseInt(menuPricetf.getText());
				
				int n = coffeeDAO.menuAdd(menuCode, menuName, menuPrice);
				// 결과값이 1이 나오면
				if(n==1) {
					// JOptionPane.YES_OPTION : 확인버튼이 있는 ConfirmDialog
					int result = JOptionPane.showConfirmDialog(null, "메뉴가 생성되었습니다.", "메뉴생성", JOptionPane.YES_OPTION);
					if(result == JOptionPane.YES_OPTION) {
						dispose();
					}
				}
			}
		});
		btnNewButton.setBounds(58, 0, 97, 40);
		panel2.add(btnNewButton);		
		
		setVisible(true);
	}
}