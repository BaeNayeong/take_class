import java.sql.*;
import java.util.Scanner;

class University{
	
	Connection con = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
	private int number; //수업을 수강할 때 로그인할 학번
	private String email; //수업을 수강할 때 로그인할 이메일	
	
	// 생성자에서  MySQL 연결
	public University() { 
		
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://192.168.56.101:4567/university","yeong","1234");
			stmt=con.createStatement();
			
		}catch(Exception e){ System.out.println(e);}
		
	}
	
	public void setNumber(int number) {
		this.number=number;
	}
	
	public void setMail(String mail) {
		this.email=mail;
	}
	
	public int getNumber() {
		return number;
	}
	
	public String getMail() {
		return email;
	}
	
	// 학생의 학번과 메일로 로그인하는 메소드 
	public boolean loginStudent(int number, String mail) {
		try {
			pstmt=con.prepareStatement("SELECT * FROM STUDENT WHERE Sno=? and mail=?");
			pstmt.setInt(1, getNumber());
			pstmt.setString(2, getMail());
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("<<로그인 성공!>>");
		return true;
	}
	
	// 전체 수업 리스트를 확인하는 메소드 
	public void printClassList() {
		try {
			ResultSet rs=stmt.executeQuery("SELECT * FROM CLASS");
			System.out.println("<전체 수업 목록>"); 
			System.out.println("수업번호\t수업명\t\t학점\t교수번호");
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
			}
			System.out.println();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("classList() error"); 
		}
	}
	
	// 로그인한 학생이 듣는 과목들을 출력하는 메소드 
	public void printStudentClass() {
		try {
			pstmt=con.prepareStatement("SELECT C.Cno 수업번호, C.Cname 수업명, C.credit 학점, P.Pname 교수명 "
					+ "FROM CLASS C, TAKE_CLASS TC, PROFESSOR P WHERE TC.Snum=? AND C.Cno=TC.Cnum AND P.Pno=C.Pnum");
			pstmt.setInt(1, number);
			rs=pstmt.executeQuery();
			
			// 검색된 데이터 출력 
			System.out.println("수업번호\t수업명\t\t학점\t교수이름");
			
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
			}

			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	// 학생이 수업을 신청할 수 있도록 하는 메소드 
	public void registerClass() {
		
	}
	
	
	public void showMain() {
		System.out.println("==========================================");
		System.out.println("1. 수업 목록 보기");
		System.out.println("2. 내 수강 리스트 출력하기");
		System.out.println("3. 수강하기");
		System.out.println("0. 종료");
		System.out.println("==========================================");
	}
	
	
	public void closeDB() {
		try {
			if(rs!=null) rs.close();
			if(stmt!=null) stmt.close();
			if(pstmt!=null) pstmt.close();
			
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}

public class Main {
	
	public static void main(String[] args) {
		
		int choice=100;
		Scanner scan=new Scanner(System.in);
		University univ=new University();
		
		// 로그인 화면
		boolean result; //로그인 성공 결과를 저장해줄 변수
		do {
			System.out.println("==========================================");
			System.out.println("로그인 화면");
			System.out.println("==========================================");

			System.out.println("학번 : ");
			int num=scan.nextInt();
			scan.nextLine();	//개행문자 제거 
			System.out.println("메일 : ");
			String mail=scan.nextLine();
			
			univ.setNumber(num);
			univ.setMail(mail);
			result=univ.loginStudent(num, mail);
			
		}while(result==false);
		
		// 메인 메뉴 리스트
		do {
			
			univ.showMain();	// 메인 메뉴 리스트 출력 
			choice=scan.nextInt();	// 사용자가 몇번을 수행할지 선택함 
			
			switch(choice) {
			//전체 수업 목록 보기
			case 1:	
				univ.printClassList();
				break;
			case 2:
				univ.printStudentClass();
				break;
			case 3:
				univ.registerClass();
				break;
			}
			
		}while(choice!=0);
		
		
		univ.closeDB();
	}//main문 괄호 닫음
	
}// Test 클래스 괄호 닫음