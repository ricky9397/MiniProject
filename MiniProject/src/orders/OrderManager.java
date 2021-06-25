package orders;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import admin.DBconn;
import member.MemberManager;
import product.Product;
import product.ProductDao;
import product.ProductManager;


public class OrderManager {
	ProductManager p;
	ProductDao pdao;
	Scanner sc;
	OrderDao odao ;
	ArrayList<Product> pro;
	ArrayList<Order> arr;
	long ordercode;
	private static Connection conn = DBconn.getConnection();
	
//	String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";
//	String user = "hr";
//	String pw = "tiger";


	public OrderManager(OrderDao order, ProductDao product) {
		this.pdao = product;
		this.odao = order;
		p = new ProductManager(pdao);
		arr = new ArrayList<Order>();
		sc = new Scanner(System.in);
	}


	void orderinsert() {
//		Connection conn = null;
		arr.clear();
		try {
			Order or =null;

//			conn = DriverManager.getConnection(jdbcUrl, user, pw);

			conn.setAutoCommit(false);
			pro = pdao.getProductList(conn);


			while (true) {
				or = new Order(MemberManager.idx);
				p.productList();
				System.out.println("주문하실 메뉴 번호를 선택해주세요.");
				int a = sc.nextInt();
				or.setIcode(a);
				System.out.println("주문 수량을 선택해주세요.");
				int b = sc.nextInt();
				or.setCount(b);
				
				for (int i = 0; i < pro.size(); i++) {
					if(pro.get(i).getIcode() == a) {
						or.setOprice(pro.get(i).getIprice()*b);
						break;
					}
				}

				arr.add(or);
				
				System.out.println(" [1]  계속 주문하기 ,  [2] 결제하기  , [3] 돌아가기 ");
				String input = sc.next();
				

				if (input.equals("1")) {
					
					System.out.println();
					continue;

				} else if (input.equals("2")) {
					System.out.println("감사합니다. ");
					break;
				}else if (input.equals("3")) {
					arr.clear();
					System.out.println("이전으로 돌아갑니다.");
					return;
				
				} else {
					System.out.println("잘못입력하셨습니다. .");
					return;
				}
			}			
			
			ordercode = System.nanoTime(); //같은 회원의 주문 확인을 위한 주문테이블의 키값 .//구동중인 JVM에서 임의로 고정된 구간으로부터 현재 나노세컨즈nanoseconds 값을 반환

			for (int i = 0; i < arr.size(); i++) {

				arr.get(i).setOrdercode(ordercode);
				int result = odao.orderInsert(conn, arr.get(i));
			}
			for (int i = 0; i < arr.size(); i++) {

				int result = odao.updateProduct(conn, arr.get(i));
			}

			conn.commit();

		} catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}


		orderList(ordercode);

	}

	void orderList(long ordercode) {
//		Connection conn = null;
		Order order = new Order();
		try {		
//			conn = DriverManager.getConnection(jdbcUrl, user, pw);

			arr = odao.getOrderList(conn, order);

			int sum =0;
			System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■영수증■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
			System.out.println("-----------------------------------------------------------------");
			for (int i = 0; i < arr.size(); i++) {
				if(ordercode ==arr.get(i).getOrdercode()) {

					System.out.printf(" 상품번호 : %d번 | 구매 수량 : %d개 | 구매가격 : %d원 |주문날짜 :%s \n" ,arr.get(i).getIcode(),arr.get(i).getCount(),arr.get(i).getOprice(),arr.get(i).getOrderdate());
					sum += arr.get(i).getOprice();

				}

			}
			System.out.println("-----------------------------------------------------------------");
			System.out.println("      총 구매 내역 :  \t\t "+sum +"원");
			System.out.println("-----------------------------------------------------------------");
			System.out.println();
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void menu() {
		System.out.println("주문을 시작합니다. ");
		while (true) {
			System.out.println("[1]주문하기   [0]돌아가기");
			int input = sc.nextInt();
			switch (input) {
			case 1:
				orderinsert();
				break;
			case 0:
				return;
			default : 
				System.out.println("잘못입력하셨습니다.");
				continue;
			}
		}
	}
}

