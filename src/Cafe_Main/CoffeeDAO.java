package Cafe_Main;

// db 연동 class

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

// db연결
public class CoffeeDAO {

	private String driver = "oracle.jdbc.driver.OracleDriver" ;
	private String url = "jdbc:oracle:thin:@localhost:1521:xe" ;
	private String username = "scott" ; // db id
	private String password = "tiger" ; // db password
	// 데이터베이스와 연결하는 객체 Connection
	private Connection conn = null ;	

	public CoffeeDAO() {		
		try {
			// DriverManager는 Class.forName( ) 메소드를 통해서 생성
			// 1. JDBC 드라이버 로딩
			Class.forName(driver) ;			
		} catch (ClassNotFoundException e) {
			System.out.println("클래스가 발견되지 않습니다(jar 파일 누락)"); 
			e.printStackTrace();		
		}
	}

	private Connection getConnection() {
		try {
			// DriverManager.getConnection(연결문자열, DB_ID, DB_PW) 으로 Connection 객체를 생성
			// 2. Connection 생성
			conn = DriverManager.getConnection(url, username, password) ;
		} catch (SQLException e) {
			System.out.println("커넥션 생성 오류");
			e.printStackTrace();
		}
		return conn ;
	}

	private void closeConnection() {
		try {
			 //Connection 사용 후 Close
			if(conn != null) {conn.close(); }
		} catch (Exception e2) {
			e2.printStackTrace(); 
		}		
	}

	// 로그인
	public int login(String id, String passwd) throws Exception{

		Connection conn= null;
		
		// statement와의 차이점 : 캐시의 유무
		// 반복적인 쿼리 수행에 대해선 PreparedStatement가 성능이 좋음
		// SQL 구문을 실행하는 역할
		// 스스로 sql문을 이해하는 것이 아닌 전달하는 역할 -> 텍스트 sql 호출
		PreparedStatement pstmt = null;
		
		ResultSet rs =null;
		String sql;
		String password;
		int result = -1;

		try{
			conn =getConnection();
			sql ="select password from login where id = ?";
			pstmt =conn.prepareStatement(sql);
			
			pstmt.setString(1, id);

			rs=pstmt.executeQuery(); // select문은 executeQuery()

			if(rs.next()){
				// select문이 실행되어 password를 찾았다면 password변수에 삽입 
				password =rs.getString("password");
				// 입력한 passwd와 찾은 password가 일치한다면
				if(password.equals(passwd)) {
					result=1; //인증성공
				} else {
					result=0; //비밀번호 틀림
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}try {
			if(rs != null) {rs.close(); }
			if(pstmt != null) {pstmt.close(); }
			closeConnection() ;
		} catch (Exception e2) {
			e2.printStackTrace(); 
		}
		return result;
	}

	// 메뉴 추가
	public int menuAdd(String menuCode, String menuName, int menuPrice){
		int result =-1;
		PreparedStatement pstmt =null;
		ResultSet rs = null;		

		try {
			conn = getConnection();
			String sql = "insert into coffeemenu (menucode, menu, price) values (?, ?, ?)";
			pstmt= conn.prepareStatement(sql);

			pstmt.setString(1, menuCode);
			pstmt.setString(2, menuName);
			pstmt.setInt(3, menuPrice);

			// executeUpdate : insert / delete / update 관련 구문에서는 반영된 레코드의 건수를 반환
			result = pstmt.executeUpdate(); // insert문 : executeUpdate()
			conn.commit();	// 반드시 commit()
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback(); 
			} catch (Exception e2) {
				e2.printStackTrace();  
			}
		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}

		return result;
	}//menuAdd

	// 메뉴 삭제
	public int delete (String menu) throws Exception {
		int result = -1;		
		PreparedStatement pstmt = null;		

		try {
			conn = getConnection();
			String sql = "delete coffeemenu where menu = ?";
			pstmt = conn.prepareStatement(sql); // SQL 해석
			
			pstmt.setString(1, menu);
			
			if(pstmt.executeUpdate()==1){
				JOptionPane.showMessageDialog(null, "삭제 완료");
			}else{
				JOptionPane.showMessageDialog(null, "삭제 오류");
			}

			result = pstmt.executeUpdate(); // delete문 : executeUpdate()
			
			conn.commit(); // 반드시 commit()
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return result;
	}

	// 결제 후 매출목록 추가
	public int coffeeadd(String payway, String menucode, String menu, int price, String ordertime){
		int result =-1;
		PreparedStatement pstmt =null;
		ResultSet rs = null;		

		try {
			conn = getConnection();
			String sql = "insert into coffee (payway, menucode, menu, price, ordertime) values (?, (select menucode from coffeemenu where menu=?), ?, ?, ?)";
			pstmt= conn.prepareStatement(sql);

			pstmt.setString(1,  payway);
			// select menucode from coffeemenu where menu=? 값에 menu를 넣어 menucode값 얻기
			pstmt.setString(2, menu);
			pstmt.setString(3, menu);
			pstmt.setInt(4, price);
			pstmt.setString(5, ordertime);

			result = pstmt.executeUpdate();
			conn.commit();	
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback(); 
			} catch (Exception e2) {
				e2.printStackTrace();  
			}
		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}

		return result;
	}

	// 현금 판매한 매출의 합
	// 자바의 배열은 고정 길이를 사용하기에 한 번 정한 크기의 배열은 절대 변경 불가능하다.
	// 본 프로그램과 같은 경우에는 고정배열을 사용하면 의미가 없기 때문에 
	// 벡터를 사용해 동적인 길이로 여러 데이터를 저장할 수 있는 Vector 클래스를 사용하였다.
	public Vector<Info> GetTotalCash() {
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		// price의 합계 구하기
		String sql = "select sum(price) from coffee where payway like '현금'";
		Vector<Info> lists = new Vector<Info>();
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ; 

			rs = pstmt.executeQuery() ;

			// 값이 하나가 아니기 때문에 while문을 사용하여 모든 값 반영
			while(rs.next()){
				Info totalCash = new Info() ;
				totalCash.setTotalCash(rs.getInt("sum(price)"));					
				lists.add( totalCash ) ;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return lists ;
	}

	// 카드 판매 한 매출의 합
	public Vector<Info> GetTotalCard() {
		//모든 상품 목록들을 리턴한다.
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		// price의 합계 구하기
		String sql = "select sum(price) from coffee where payway like '카드'";
		Vector<Info> lists = new Vector<Info>();
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ; 

			rs = pstmt.executeQuery() ;

			while(rs.next()){
				Info totalCard = new Info() ;
				totalCard.setTotalCard(rs.getInt("sum(price)"));					
				lists.add( totalCard ) ;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return lists ;
	}
	
	// 메뉴코드가 H로 시작하는 메뉴 리스트 (hot coffee)
	public Vector<Info> GetHotCoffee() {
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		String sql = "select menu, price from coffeemenu where menucode like 'H%'";
		Vector<Info> lists = new Vector<Info>();
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ; 

			rs = pstmt.executeQuery() ;

			while(rs.next()){
				Info hot = new Info() ;
				hot.setMenu(rs.getString("menu"));	
				hot.setPrice(rs.getInt("price"));
				lists.add( hot ) ;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return lists ;
	}

	// 메뉴코드가 I로 시작하는 메뉴 리스트 (ice coffee)
	public Vector<Info> GetIceCoffee() {//db에서 데이터를 받아서 벡터로 반환하는 메소드
		//모든 상품 목록들을 리턴한다.
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		String sql = "select menu, price from coffeemenu where menucode like 'I%'";
		Vector<Info> lists = new Vector<Info>();
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ; 

			rs = pstmt.executeQuery() ;

			while(rs.next()){
				Info ice = new Info() ;
				ice.setMenu(rs.getString("menu"));	
				ice.setPrice(rs.getInt("price"));
				lists.add( ice ) ;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return lists ;
	}

	// 메뉴코드가 B로 시작하는 메뉴 리스트 (beverage coffee)
	public Vector<Info> GetBeverageCoffee() {//db에서 데이터를 받아서 벡터로 반환하는 메소드
		//모든 상품 목록들을 리턴한다.
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		String sql = "select menu, price from coffeemenu where menucode like 'B%'";
		Vector<Info> lists = new Vector<Info>();
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ; 

			rs = pstmt.executeQuery() ;

			while(rs.next()){
				Info beverage = new Info() ;
				beverage.setMenu(rs.getString("menu"));		
				beverage.setPrice(rs.getInt("price"));
				lists.add( beverage ) ;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return lists ;
	}

	// 메뉴별 판매량
	public  Vector<Info> Getsellcount(){
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		// 메뉴 이름과 그 메뉴의 개수를 전체 행에서 파악
		// group by : 메뉴를 기준으로 그룹화 함 (by 판매개수)
		// order by :정렬기준
		String sql = "select menu , count(*)  from coffee group by menu order by count(*) desc" ;
		Vector<Info> lists = new Vector<Info>();
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ; 

			rs = pstmt.executeQuery() ;

			while(rs.next()){
				Info coffee = new Info() ;
				coffee.setMenu (rs.getString("menu"));
				coffee.setCount( rs.getInt("count(*)") ); 

				lists.add( coffee ) ;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return lists ;
	}

	// 매출리스트
	public Vector<Info> GetAllSellList() {
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		String sql = "select * from coffee" ;
		Vector<Info> lists = new Vector<Info>();
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ; 

			rs = pstmt.executeQuery() ;

			while(rs.next()){
				Info info = new Info() ;
				info.setPayway(rs.getString("payway"));
				info.setMenuCode(rs.getString("menucode"));
				info.setMenu(rs.getString("menu"));
				info.setPrice( rs.getInt("price") ); 
				info.setDate(rs.getString("ordertime"));

				lists.add( info ) ;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return lists ;
	}

	// 당일 카드매출
	public Vector<Info> currentCellCard() {		
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		// SUBSTR(ordertime,1,13) : ordertime 속성의 첫번째부터 13번째 글자까지만 나타내어라
		String sql = "select sum(price), SUBSTR(ordertime,1,13) from coffee where payway like '카드' group by SUBSTR(ordertime,1,13)";
		Vector<Info> lists = new Vector<Info>();
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ; 

			rs = pstmt.executeQuery() ;

			while(rs.next()){
				Info totalCard = new Info() ;
				totalCard.setTotalCard(rs.getInt("sum(price)"));	
				totalCard.setDate(rs.getString("SUBSTR(ordertime,1,13)"));
				lists.add( totalCard ) ;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return lists ;
	}

	// 당일 현금매출
	public Vector<Info> currentCellCash() {
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		String sql = "select sum(price), SUBSTR(ordertime,1,13) from coffee where payway like '현금' group by SUBSTR(ordertime,1,13)";
		Vector<Info> lists = new Vector<Info>();
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ; 

			rs = pstmt.executeQuery() ;

			while(rs.next()){
				Info totalCash = new Info() ;
				totalCash.setTotalCash(rs.getInt("sum(price)"));	
				totalCash.setDate(rs.getString("SUBSTR(ordertime,1,13)"));
				lists.add( totalCash ) ;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return lists ;
	}

	// 당일 전체 매출
	public Vector<Info> currentCellTotal() {
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		String sql = "select sum(price), SUBSTR(ordertime,1,13) from coffee group by SUBSTR(ordertime,1,13)";
		Vector<Info> lists = new Vector<Info>();
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ; 

			rs = pstmt.executeQuery() ;

			while(rs.next()){
				Info total = new Info() ;
				total.setPrice(rs.getInt("sum(price)"));	
				total.setDate(rs.getString("SUBSTR(ordertime,1,13)"));
				lists.add( total ) ;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			try {
				if(rs != null) {rs.close(); }
				if(pstmt != null) {pstmt.close(); }
				closeConnection() ;
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
		}
		return lists ;
	}

	//벡터를 받아서 매출리스트를 2차원 배열로 만들어주는 메소드
	public Object[][] makeArr(Vector<Info> lists){

		Object [][] coffeearr = new Object [lists.size()][5]; 			

		for(int i=0; i<lists.size();i++){
			coffeearr[i][0]=lists.get(i).getPayway();
			coffeearr[i][1]=lists.get(i).getMenuCode();
			coffeearr[i][2]=lists.get(i).getMenu();
			coffeearr[i][3]=lists.get(i).getPrice();
			coffeearr[i][4]=lists.get(i).getDate();
		}			

		return coffeearr;

	}

	//벡터를 받아서 메뉴별 판대량을 2차원 배열로 만들어주는 메소드
	public Object[][] makelistArr(Vector<Info> lists){

		Object [][] coffeearr = new Object [lists.size()][2]; 

		for(int i=0; i<lists.size();i++){
			coffeearr[i][0]=lists.get(i).getMenu();
			coffeearr[i][1]=lists.get(i).getCount();
		}		

		return coffeearr;

	}

}