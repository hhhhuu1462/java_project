package Cafe_Main;

// getter / setter

public class Info extends Object {

	String id;
	String pw;

	String menuCode;
	String menu;
	int price;
	String date;
	String payway;
	int totalCash;
	int totalCard;
	int count;

	public int getCount() {	return count;}
	public void setCount(int count) {this.count = count;}
	public int getTotalCash() {	return totalCash;}
	public void setTotalCash(int totalCash) {this.totalCash = totalCash;}
	public int getTotalCard() {return totalCard;}
	public void setTotalCard(int totalCard) {this.totalCard = totalCard;}
	public String getPayway() {return payway;}
	public void setPayway(String payway) {	this.payway = payway;	}
	public String getMenuCode() {	return menuCode;}
	public void setMenuCode(String menuCode) {this.menuCode = menuCode;}
	public String getMenu() {return menu;	}
	public void setMenu(String menu) {	this.menu = menu;	}
	public int getPrice() {return price;}
	public void setPrice(int price) {this.price = price;}
	public String getDate() {return date;}
	public void setDate(String date) {this.date = date;}	
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	public String getPw() {return pw;}
	public void setPw(String pw) {this.pw = pw;}
	
}