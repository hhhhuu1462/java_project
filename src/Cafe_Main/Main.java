package Cafe_Main;

// main class

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.stream.IntStream;

public class Main extends JFrame { 
	private static final long serialVersionUID = 7340961398071354159L;
	
	Vector<Info> rowData = null;
	CoffeeDAO coffeeDAO = null;

	JPanel panel1, panel2, panel3, panel4, panel5, panel6;
	JTabbedPane menuTab = new JTabbedPane();  //JTabbedPane생성
	JTextField tf = new JTextField(); // 총 금액
	JButton[] HotCoffeeBtn, ICECoffeeBtn, ShakeFlatchinoBtn; // 메뉴 버튼	

	// 주문 테이블
	String [] ColName = {"메뉴","수량","가격"};
	String [][] Data ;	
	int count =1;
	DefaultTableModel model = new DefaultTableModel(Data,ColName);
	JTable menuTable = new JTable(model);
	JScrollPane menuScroll = new JScrollPane();

	// 메뉴이름, 가격
	String[] HotCoffee;			Integer  hotCoffeePrice[];
	String[] ICECoffee;			Integer  iceCoffeePrice[];
	String[] ShakeFlatchino;	Integer  shakeFlatchinoPrice[];	

	public Main() {
		super("Ezen Cafeteria");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 902, 563);
		
		launched();
		createMenuBar();		

		ImageIcon img = new ImageIcon("C:\\git!\\java\\Projects\\Cafe_Main\\짱구.jpg");
		setIconImage(img.getImage());
		setResizable(false);
		setVisible(true);
	}

	// 메뉴바 만들기
	public void createMenuBar() {
		JMenuBar mb = new JMenuBar(); 
		JMenuItem [] menuItem = new JMenuItem [1];
		String[] itemTitle = {"Exit"};
		JMenu fileMenu = new JMenu("File");		

		FileMenuActionListener fileMenuActionListener = new FileMenuActionListener();
		for (int i = 0; i < menuItem.length; i++) {
			menuItem[i] = new JMenuItem(itemTitle[i]); 
			menuItem[i].addActionListener(fileMenuActionListener); 
			fileMenu.add(menuItem[i]);
		}		
		mb.add(fileMenu);
		setJMenuBar(mb);		

		JMenu editMenu = new JMenu("Edit");
		JMenu tabMenu = new JMenu("Tab");
		editMenu.add(tabMenu);
		JMenu hotCoffeeMenu = new JMenu("HotCoffee");
		JMenu iceCoffeeMenu = new JMenu("IceCoffee");
		JMenu shakeFlatchinoMenu = new JMenu("ShakeFlatchino");
		tabMenu.add(hotCoffeeMenu);
		tabMenu.add(iceCoffeeMenu);
		tabMenu.add(shakeFlatchinoMenu);

		JMenuItem [] editItem = new JMenuItem[2];
		String[] hotCoffeeEdit = {"추가", "삭제"};
		String[] iceCoffeeEdit = {"추가", "삭제"};
		String[] shakeFlatchinoEdit = {"추가", "삭제"};
		EditMenuActionListener editMenuActionListener = new EditMenuActionListener();
		for (int i = 0; i < editItem.length; i++) {
			editItem[i] = new JMenuItem(hotCoffeeEdit[i]); 
			editItem[i].addActionListener(editMenuActionListener); 
			hotCoffeeMenu.add(editItem[i]);
		}		
		for (int i = 0; i < editItem.length; i++) {
			editItem[i] = new JMenuItem(iceCoffeeEdit[i]); 
			editItem[i].addActionListener(editMenuActionListener); 
			iceCoffeeMenu.add(editItem[i]);
		}		
		for (int i = 0; i < editItem.length; i++) {
			editItem[i] = new JMenuItem(shakeFlatchinoEdit[i]); 
			editItem[i].addActionListener(editMenuActionListener); 
			shakeFlatchinoMenu.add(editItem[i]);
		}		

		mb.add(editMenu);
	}

	// file 메뉴바 : exit메뉴
	class FileMenuActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch (cmd) {
			case "Exit" :
				System.exit(0);
				break;
			}
		}
	}

	// file 메뉴바 : edit 메뉴
	class EditMenuActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch (cmd) {
			case "추가" :
				new CreateMenu();	
				break;
			case "삭제" :
				// InputDialog에 적은 메뉴를 delete값에 저장
				String delete = (String) JOptionPane.showInputDialog("삭제할 메뉴를 적어주세요");
				CoffeeDAO coffeeDAO = new CoffeeDAO();
				String menu = delete;
				try {
					coffeeDAO.delete(menu);
					for (int i = 0; i < HotCoffeeBtn.length; i++) {
						if(HotCoffeeBtn[i].getText().equals(delete)) {
							HotCoffeeBtn[i].setText("");
							hotCoffeePrice[i]=0;
							HotCoffee[i] = "";											
						}
					}
					for (int i = 0; i < ICECoffeeBtn.length; i++) {
						if(ICECoffeeBtn[i].getText().equals(delete)) {
							ICECoffeeBtn[i].setText("");
							iceCoffeePrice[i]=0;
							ICECoffee[i] = "";											
						}
					}
					for (int i = 0; i < ShakeFlatchinoBtn.length; i++) {
						if(ShakeFlatchinoBtn[i].getText().equals(delete)) {
							ShakeFlatchinoBtn[i].setText("");
							shakeFlatchinoPrice[i]=0;
							ShakeFlatchino[i] = "";											
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	// main창 핵심 메소드
	public void launched() {
		getContentPane().setLayout(null);
		menuTab.setBounds(394, 24, 476, 371);
		getContentPane().add(menuTab);

		// 메뉴이름, 가격 db에서 불러와서 생성
		HotCoffee = new String[11];			hotCoffeePrice = new Integer[11];
		ICECoffee  = new String[11];			iceCoffeePrice = new Integer[11];
		ShakeFlatchino  = new String[11];	shakeFlatchinoPrice = new Integer[11];
		
		CoffeeDAO coffeeDAO = new CoffeeDAO();
		rowData = coffeeDAO.GetHotCoffee();
		// hotcoffee에 대한 이름과 가격 db를 통해 받아오기
		for (int i = 0; i < HotCoffee.length; i++) {
			if (i < rowData.size()) {
				HotCoffee[i] = rowData.get(i).getMenu();
				hotCoffeePrice[i] = rowData.get(i).getPrice();
			} else {
				HotCoffee[i] = "";
				hotCoffeePrice[i] = 0;
			}
		}
		
		rowData = coffeeDAO.GetIceCoffee();
		// icecoffee에 대한 이름과 가격 db를 통해 받아오기
		for (int i = 0; i < ICECoffee.length; i++) {
			if (i < rowData.size()) {
				ICECoffee[i] = rowData.get(i).getMenu();
				iceCoffeePrice[i] = rowData.get(i).getPrice();
			} else {
				ICECoffee[i] = "";
				iceCoffeePrice[i] = 0;
			}
		}
		
		rowData = coffeeDAO.GetBeverageCoffee();
		// beverage에 대한 이름과 가격 db를 통해 받아오기
		for (int i = 0; i < ShakeFlatchino.length; i++) {
			if (i < rowData.size()) {
				ShakeFlatchino[i] = rowData.get(i).getMenu();
				shakeFlatchinoPrice[i] = rowData.get(i).getPrice();
			} else {
				ShakeFlatchino[i] = "";
				shakeFlatchinoPrice[i] = 0;
			}
		}
		
		// HotCoffeeBtn		
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(4, 4, 10, 10));		
		HotCoffeeBtn = new JButton[11];
		coffeeDAO = new CoffeeDAO();
		rowData = coffeeDAO.GetHotCoffee();
		for (int i = 0; i < HotCoffeeBtn.length; i++) {	
			// rowData는 벡터이기 때문에 1부터 시작하므로 -1을 해주어야 값이 맞음
			if (i <= rowData.size()-1) {
				HotCoffeeBtn[i] = new JButton(rowData.get(i).getMenu());
				panel1.add(HotCoffeeBtn[i]);	
			} else {
				HotCoffeeBtn[i] = new JButton();
				panel1.add(HotCoffeeBtn[i]);	
			}
		}
		menuTab.add("HotCoffee", panel1);

		// ICECoffeeBtn
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(4, 4, 10, 10));	
		ICECoffeeBtn = new JButton[11];
		rowData = coffeeDAO.GetIceCoffee();
		for (int i = 0; i < ICECoffeeBtn.length; i++) {		
			if (i <= rowData.size()-1) {
				ICECoffeeBtn[i] = new JButton(rowData.get(i).getMenu());
				panel2.add(ICECoffeeBtn[i]);	
			} else {
				ICECoffeeBtn[i] = new JButton();
				panel2.add(ICECoffeeBtn[i]);	
			}
		}
		menuTab.add("IceCoffee", panel2);

		// ShakeFlatchinoBtn
		panel3 = new JPanel();
		panel3.setLayout(new GridLayout(4, 4, 10, 10));	
		ShakeFlatchinoBtn = new JButton[11];
		rowData = coffeeDAO.GetBeverageCoffee();
		for (int i = 0; i < ShakeFlatchinoBtn.length; i++) {		
			if (i <= rowData.size()-1) {
				ShakeFlatchinoBtn[i] = new JButton(rowData.get(i).getMenu());
				panel3.add(ShakeFlatchinoBtn[i]);	
			} else {
				ShakeFlatchinoBtn[i] = new JButton();
				panel3.add(ShakeFlatchinoBtn[i]);	
			}
		}
		menuTab.add("Beverage", panel3);

		// 운영버튼
		panel4 = new JPanel();
		panel4.setBounds(26, 410, 844, 85);
		getContentPane().add(panel4);
		panel4.setLayout(new GridLayout(1, 6, 10, 10));		
		String[] operation = {"매출", "결제", "할인", "선택취소", "전체취소"};
		JButton[] inventoryManagement = new JButton[5];
		//운영버튼 생성
		for (int i = 0; i < inventoryManagement.length; i++) {
			inventoryManagement[i] = new JButton(operation[i]);
			panel4.add(inventoryManagement[i]);
		}

		//매출목록
		inventoryManagement[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Sales();						
			}
		});

		// 결제 방식
		panel6 = new JPanel();
		panel6.setBounds(26, 294, 353, 37);
		getContentPane().add(panel6);
		panel6.setLayout(null);

		JLabel payWay = new JLabel("결제 방식");
		payWay.setFont(new Font("고딕", Font.BOLD, 15));
		payWay.setBounds(8, 7, 109, 18);
		JRadioButton cash = new JRadioButton("현금");
		cash.setBounds(125, 5, 76, 23);
		JRadioButton card = new JRadioButton("카드");
		card.setBounds(212, 5, 76, 23);
		panel6.add(payWay);
		panel6.add(cash);
		panel6.add(card);	
		ButtonGroup  group = new ButtonGroup(); 
		group.add(card);
		group.add(cash);

		//결제		
		inventoryManagement[1].addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				int rowCont = menuTable.getRowCount();		
				int pay[] = new int[rowCont];
				for(int i=0;i<rowCont;i++) {
					pay[i] = (int)menuTable.getValueAt(i, 2);
				}
				IntStream intStream = IntStream.of(pay);
				int sum = intStream.sum();
				tf.setText(String.valueOf(" 총 금액 : " + sum + "원"));
				tf.setFont(new Font("고딕", Font.BOLD, 15));

				if(cash.isSelected()==true) {
					int exitOptionCash = JOptionPane.showConfirmDialog(null, "현금결제 하시겠습니까?", "결제창", JOptionPane.YES_OPTION);
					if (exitOptionCash == JOptionPane.YES_OPTION) {
						Payment payment = new Payment();
						payment.payBtn.addActionListener(new ActionListener() {							
							@Override
							public void actionPerformed(ActionEvent e) {
								String str = "";
								str = payment.payMoney.getText();
								str = str.trim();
								int paymoney = 0;
								paymoney = Integer.parseInt(str);	
								if(sum <= paymoney) {
									CoffeeDAO coffeeDAO = new CoffeeDAO();	
									int price = 0;
									for (int i = 0; i < menuTable.getRowCount(); i++) {
										SimpleDateFormat format2 = new SimpleDateFormat ("yyyy년 MM월 dd일 HH시mm분ss초");
										String format_time2 = format2.format (System.currentTimeMillis());

										String payway = (String) cash.getText();
										String menucode = (String) menuTable.getValueAt(i, 0);	
										String menu = (String) menuTable.getValueAt(i, 0);		
										price = (int) menuTable.getValueAt(i, 2);
										String ordertime = format_time2;
										
										coffeeDAO.coffeeadd(payway, menucode, menu, price, ordertime);
									}									
									JOptionPane.showMessageDialog(null, "결제되었습니다. 거스름돈은 " + (paymoney - sum) + "원 입니다", "결제창", JOptionPane.PLAIN_MESSAGE);
									payment.dispose();
									// 테이블값 초기화
									DefaultTableModel model = (DefaultTableModel)menuTable.getModel();
									model.setNumRows(0);
									tf.setText("");
								} else {
									JOptionPane.showMessageDialog(null, "돈이 부족해요!", "결제창", JOptionPane.INFORMATION_MESSAGE);
									payment.dispose();
								}
							}
						});
					} 
				}else if(card.isSelected()==true) {
					int exitOptionCard = JOptionPane.showConfirmDialog(null, "카드결제 하시겠습니까?", "결제창", JOptionPane.YES_OPTION);
					if (exitOptionCard == JOptionPane.YES_OPTION) {
						Payment payment = new Payment();
						payment.payBtn.addActionListener(new ActionListener() {							
							@Override
							public void actionPerformed(ActionEvent e) {
								String str = "";
								str = payment.payMoney.getText();
								int paymoney = 0;
								paymoney = Integer.parseInt(str);	
								if(sum <= paymoney) {
									CoffeeDAO coffeeDAO = new CoffeeDAO();									
									for (int i = 0; i < menuTable.getRowCount(); i++) {
										SimpleDateFormat format2 = new SimpleDateFormat ("yyyy년 MM월 dd일 HH시mm분ss초");
										String format_time2 = format2.format (System.currentTimeMillis());

										String payway = (String) card.getText();
										String menucode = (String) menuTable.getValueAt(i, 0);	
										String menu = (String) menuTable.getValueAt(i, 0);		
										int price = (int) menuTable.getValueAt(i, 2);
										String ordertime = format_time2;

										coffeeDAO.coffeeadd(payway, menucode, menu, price, ordertime);
									}									
									JOptionPane.showMessageDialog(null, "결제되었습니다", "결제창", JOptionPane.PLAIN_MESSAGE);
									payment.dispose();
									DefaultTableModel model = (DefaultTableModel)menuTable.getModel();
									model.setNumRows(0);
									tf.setText("");
								} else {
									JOptionPane.showMessageDialog(null, "잔액 부족입니다. 다른 결제 수단을 이용해 주세요:(", "결제창", JOptionPane.INFORMATION_MESSAGE);
									payment.dispose();
								}
							}
						});
					} 					
				}	
			}
		});

		// 할인
		inventoryManagement[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Discount discount = new Discount();
				discount.btn10.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
						int result = (int) (menuTable.getValueAt(menuTable.getSelectedRow(), 2));
						menuTable.setValueAt((int)(result * 0.9), menuTable.getSelectedRow(), 2);
						discount.dispose();
					}
				});
				discount.btn20.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
						int result = (int) (menuTable.getValueAt(menuTable.getSelectedRow(), 2));
						menuTable.setValueAt((int)(result * 0.8), menuTable.getSelectedRow(), 2);
						discount.dispose();
					}
				});
				discount.btnFree.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
						menuTable.setValueAt(0, menuTable.getSelectedRow(), 2);
						discount.dispose();
					}
				});
			}
		});

		// 선택취소
		inventoryManagement[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel m = (DefaultTableModel)menuTable.getModel();
				m.removeRow(menuTable.getSelectedRow());
			}
		});

		// 전체취소
		inventoryManagement[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel m = (DefaultTableModel)menuTable.getModel();
				m.setRowCount(0);
				tf.setText(String.valueOf(""));
			}
		});

		// 주문리스트
		menuTable.setRowHeight(38);
		menuTable.getColumn(ColName[0]).setPreferredWidth(140);
		menuScroll.setBounds(26, 24, 353, 264);
		menuScroll.setViewportView(menuTable);
		getContentPane().add(menuScroll);
		for(int i=0;i<HotCoffeeBtn.length;i++) {
			final int index =i;
			HotCoffeeBtn[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultTableModel m = (DefaultTableModel)menuTable.getModel();
					String menu = HotCoffee[index];
					m.addRow(new Object[]{menu,count, hotCoffeePrice[index]});
					if(menu.equals("")) {
						m.removeRow(m.getRowCount()-1);				
					}
				}
			});
			ICECoffeeBtn[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultTableModel m = (DefaultTableModel)menuTable.getModel();
					String menu = ICECoffee[index];
					m.addRow(new Object[]{menu,count, iceCoffeePrice[index]});
					if(menu.equals("")) {
						m.removeRow(m.getRowCount()-1);				
					}
				}
			});
			ShakeFlatchinoBtn[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultTableModel m = (DefaultTableModel)menuTable.getModel();
					String menu = ShakeFlatchino[index];
					m.addRow(new Object[]{menu,count, shakeFlatchinoPrice[index]});
					if(menu.equals("")) {
						m.removeRow(m.getRowCount()-1);				
					}
				}
			});
		}

		// 총 금액
		panel5 = new JPanel();
		panel5.setBounds(26, 337, 353, 58);
		getContentPane().add(panel5);
		panel5.setLayout(null);
		tf.setBounds(0, 0, 353, 58);
		panel5.add(tf);

	}
	
}