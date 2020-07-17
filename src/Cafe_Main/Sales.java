package Cafe_Main;

// 매출 gui

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;

public class Sales extends JFrame {

	private static final long serialVersionUID = -6764304939888109722L;

	Vector<Info> rowData = null;
	CoffeeDAO coffeeDAO = null;

	String[] ColName = {"결제 방법", "MenuCode", "메뉴", "가격", "날짜"};
	String[] sellName = {"메뉴", "판매잔수"};
	JTable table;
	JTable table2;
	JLabel totalLabel;
	JLabel cashLabel;
	JLabel cardLabel;	

	public Sales() {		
		super("매출목록");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(1020, 120, 592, 461);

		coffeeDAO = new CoffeeDAO();
		rowData = coffeeDAO.GetAllSellList();

		Panel tablePanel = new Panel();
		tablePanel.setBounds(10, 10, 562, 285);
		getContentPane().add(tablePanel);
		tablePanel.setLayout(null);	

		table = new JTable(coffeeDAO.makeArr(coffeeDAO.GetAllSellList()),ColName);
		// 셀 간격 조절
		table.getColumn(ColName[4]).setPreferredWidth(170);
		table.setRowHeight(38);
		table.setBounds(10, 10, 562, 285);
		tablePanel.add(table);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 0, 562, 285);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePanel.add(scrollPane);

		Panel buttonPanel = new Panel();
		buttonPanel.setBounds(10, 357, 562, 65);
		getContentPane().add(buttonPanel);

		JButton posBtn = new JButton("POS");
		posBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//창닫기
				setVisible(false);
			}
		});
		buttonPanel.setLayout(new GridLayout(0, 4, 20, 0));
		buttonPanel.add(posBtn);

		JButton salesBtn = new JButton("매출");
		salesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//테이블에 전체 매출 리스트 출력
				rowData =coffeeDAO.GetAllSellList();
				int totalSum=0;
				for(int i=0; i<rowData.size();i++){					
					totalSum += rowData.get(i).getPrice();
				}			
				rowData = coffeeDAO.GetTotalCash();
				int totalCash = 0;
				for (int i = 0; i < rowData.size(); i++) {
					totalCash += rowData.get(i).getTotalCash();
				}
				rowData = coffeeDAO.GetTotalCard();
				int totalCard = 0;
				for (int i = 0; i < rowData.size(); i++) {
					totalCard += rowData.get(i).getTotalCard();
				}		
				totalLabel.setText("총 매출 : " + Integer.toString(totalSum));				
				cardLabel.setText("카드 매출 : " + Integer.toString(totalCard));
				cashLabel.setText("현금 매출 : " + Integer.toString(totalCash));
			}
		});
		buttonPanel.add(salesBtn);

		JButton todayBtn = new JButton("금일 매출");
		todayBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
				Date now = new Date();
				String currentTime = simpleDateFormat.format (now);
				
				rowData =coffeeDAO.currentCellTotal();
				int currentTotal = 0;
				String beforeDate;
				for (int i = 0; i < rowData.size(); i++) {				
					beforeDate = rowData.get(i).getDate();
					if (currentTime.compareTo(beforeDate) == 0) {
						currentTotal = rowData.get(i).getPrice();	
					}									
				}
				
				rowData =coffeeDAO.currentCellCash();
				int currentCash = 0;
				for (int i = 0; i < rowData.size(); i++) {				
					beforeDate = rowData.get(i).getDate();
					if (currentTime.compareTo(beforeDate) == 0) {
						currentCash = rowData.get(i).getTotalCash();	
					}									
				}
				
				rowData =coffeeDAO.currentCellCard();
				int currentCard = 0;
				for (int i = 0; i < rowData.size(); i++) {				
					beforeDate = rowData.get(i).getDate();
					if (currentTime.compareTo(beforeDate) == 0) {
						currentCard = rowData.get(i).getTotalCard();	
					}									
				}				
				totalLabel.setText("금일총 매출 : " + Integer.toString(currentTotal));		
				cashLabel.setText("금일 현금 매출 : " + Integer.toString(currentCash));
				cardLabel.setText("금일 카드 매출 : " + Integer.toString(currentCard));
				
			}
		});
		buttonPanel.add(todayBtn);


		JButton menuBtn = new JButton("메뉴별판매");
		menuBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//테이블에 메뉴별 판매리스트
				rowData =coffeeDAO.GetAllSellList();
				scrollPane.setVisible(false);
				
				// 첫 번째 매개변수는 테이블에 들어갈 데이터이며, 오브젝트 타입의 2차원 배열을 받는다. 
				// 두 번째 매개변수는 테이블의 첫 번째 행에 들어가 데이터를 구분 짓는 열의 이름이 들어간다.
				table2 = new JTable(coffeeDAO.makelistArr(coffeeDAO.Getsellcount()), sellName);
				table2.setRowHeight(38);
				table2.setBounds(1, 27, 450, 288);
				tablePanel.add(table2);

				JScrollPane scrollPane = new JScrollPane(table2);
				scrollPane.setBounds(0, 0, 562, 285);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				tablePanel.add(scrollPane);				
			}
		});
		buttonPanel.add(menuBtn);

		Panel salesPanel = new Panel();
		salesPanel.setBounds(10, 309, 574, 39);
		getContentPane().add(salesPanel);
		salesPanel.setLayout(null);

		totalLabel = new JLabel("");
		totalLabel.setFont(new Font("HY나무M", Font.PLAIN, 15));
		totalLabel.setBounds(12, 0, 173, 35);
		salesPanel.add(totalLabel);

		cashLabel= new JLabel("");
		cashLabel.setFont(new Font("HY나무M", Font.PLAIN, 15));
		cashLabel.setBounds(204, 0, 173, 35);
		salesPanel.add(cashLabel);

		cardLabel = new JLabel("");
		cardLabel.setFont(new Font("HY나무M", Font.PLAIN, 15));
		cardLabel.setBounds(401, 0, 173, 35);
		salesPanel.add(cardLabel);

		setResizable(false);
		getContentPane().setLayout(null);

		setVisible(true);
	}
}