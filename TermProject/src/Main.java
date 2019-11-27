/*
 * <데이터베이스 시스템 term project>
 * 작성자 : 배나영
 * 학번 : 2015023025
 * 학과 : 소프트웨어학과
 * Mini world : 학교 수강 시스템
*/

import java.sql.*;
import java.util.Scanner;

/*
 * Person class
 * 기능과 목적 : 
 * mysql과 connect하는 기능, Scanner 생성
 * Student와 Professor가 상속하도록 하여 두 클래스에서 별도로 mysql에 연결해줄 필요가 없도록 한다.  
*/
class Person{
	
	Scanner scan=new Scanner(System.in);
	
	Connection con = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
	protected int number; // 학번 또는 교수번호
	protected String email; // 로그인 할 때 사용할 이메일 	
	
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
	
	// 사용자가 메뉴를 선택할 수 있도록 하는 메소드 
	public int getChoice() { 
		int choice=scan.nextInt();
		return choice;
	}
    
    // 생성자에서 mysql connection
    public Person() {
    	try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://192.168.56.101:4567/university","yeong","1234");
			stmt=con.createStatement();
			
		}catch(Exception e){ System.out.println(e);}
    }
    
    // ResultSet, PreparedStatement, Statement, Connection, Scanner 닫아줌
    public void closeAll() {
		try {
			if(rs!=null) rs.close();
			if(stmt!=null) stmt.close();
			if(pstmt!=null) pstmt.close();
			if(con!=null) con.close();
			
		} catch(Exception e) {
			System.out.println(e);
		}
	}
    
    // 시작화면
    // 학생으로 로그인할 것인지 교수로 로그인할 것인지 결정한다
    public int startMenu() {
    	
    	int perNum = 0; // 누구로 로그인할 것인지 결정하는 값을 받아주는 변수
    	
    	System.out.println("===========================================");
    	System.out.println("로그인 화면");
    	System.out.println("학생으로 로그인할 것인지 교수로 로그인할 것인지 고르시오");
    	System.out.println("(1. 학생 로그인 / 2. 교수 로그인)");
    	System.out.println("===========================================");
    	
    	perNum=scan.nextInt();
    	
    	return perNum;
    }
}

/*
 * Student class (학생 클래스)
 * mini world에서 학생에 해당한다.
 * 학번과 이메일로 로그인
 * 전체 수업 목록 확인, 자신이 듣는 과목 확인, 수강 취소, 특정한 교수가 담당하는 과목 확인을 수행할 수 있다.
 */
class Student extends Person{
	
	// 생성자에서  MySQL 연결
	public Student() { 
		super();
	}
	
	// 학생의 학번과 메일로 로그인하는 메소드 
	public boolean loginStudent() {
		
		System.out.println("학번 : ");
		int inputNum = scan.nextInt();
		System.out.println("이메일 : ");
		scan.nextLine();
		String inputMail = scan.nextLine();
		
		try {
			pstmt=con.prepareStatement("SELECT * FROM STUDENT WHERE Sno=? and mail=?");
			pstmt.setInt(1, inputNum);
			pstmt.setString(2, inputMail);
					
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		System.out.println("<<로그인 성공!>>");		
		
		// 로그인에 성공했으므로 학번과 이메일 변수에 저장해준다
		setNumber(inputNum);
		setMail(inputMail);

		return true;
	}
	
	// 로그인 다음 보여지는 화면
	// 시스템에서 학생의 동작 수행
	public void showMain() {
		System.out.println("==========================================");
		System.out.println("1. 수업 목록 보기");
		System.out.println("2. 내 수강 리스트 출력하기");
		System.out.println("3. 수강하기");
		System.out.println("4. 수강 취소하기");
		System.out.println("5. 교수 담당 과목 확인");
		System.out.println("0. 종료");
		System.out.println("==========================================");
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
			pstmt.setInt(1, getNumber());
			rs=pstmt.executeQuery();
			
			// 검색된 데이터 출력 
			System.out.println("수업번호\t수업명\t\t학점\t교수이름");
			
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getInt(3)+"\t"+rs.getString(4));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	// 학생이 수업을 신청할 수 있도록 하는 메소드 
	public void registerClass() {
		
		System.out.println("수강하고 싶은 과목 번호를 입력하시오."); 
		scan.nextLine();	
		
		int subject=scan.nextInt(); // 과목 번호
		
		try {
			
			pstmt=con.prepareStatement("INSERT INTO TAKE_CLASS VALUES(?, ?)");
			
			pstmt.setInt(1, getNumber());
			pstmt.setInt(2, subject);
			pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	// 학생이 수업을 취소할 수 있도록 하는 메소드
	public void cancelClass() {
		
		System.out.println("수강 취소하고 싶은 과목 이름을 입력하시오"); scan.nextLine();
		String subject=scan.nextLine();
		
		try {
			
			pstmt=con.prepareStatement("DELETE FROM TAKE_CLASS WHERE Snum=? AND Cnum=?");
			pstmt.setInt(1, number);
			pstmt.setString(2, subject);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();	
		}
	}
	
	// 검색한 교수가 맡은 수업을 출력하는 메소드
	public void classPerProfessor() {
		
		System.out.println("검색하고자 하는 교수 이름을 입력하세요 : "); scan.nextLine();
		String professor=scan.nextLine();
		
		try {
			
			pstmt=con.prepareStatement("select P.Pname, C.Cname from PROFESSOR P, CLASS C where P.Pno=C.Pnum and P.Pname=?");
			pstmt.setString(1, professor);
			rs = pstmt.executeQuery();
			
			System.out.println("교수이름\t수업명");
			
			while(rs.next()) {
				System.out.println(rs.getString(1)+"\t"+rs.getString(2));
			}
			
			pstmt=null;
			
		}catch(Exception e) {
			e.printStackTrace();	
		}
	}
	
}
/*
 * Professor class (교수 클래스)
 * mini world에서 교수에 해당한다.
 * 교수번호과 이메일로 로그인
 */
class Professor extends Person{
	
	//DB Connection
	public Professor() {
		super();
	}
	
	// 교수 로그인 
	public boolean loginProfessor() {
		
		System.out.println("교수번호 : ");
		int inputNum = scan.nextInt();
		System.out.println("이메일 : ");
		scan.nextLine();
		String inputMail = scan.nextLine();
		
		try {
			pstmt=con.prepareStatement("SELECT * FROM PROFESSOR WHERE Pno=? and mail=?");
			pstmt.setInt(1, inputNum);
			pstmt.setString(2, inputMail);
						
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		System.out.println("<<로그인 성공!>>");		
		
		// 로그인에 성공했으므로 교수번호와 이메일 변수에 저장해준다
		setNumber(inputNum);
		setMail(inputMail);

		return true;
	}
	
	// 교수로 로그인했을 때 나타날 메뉴 화면
	public void showMain() {
		System.out.println("==========================================");
		System.out.println("1. 수업 목록 보기");
		System.out.println("2. 내 수업 리스트 출력하기");
		System.out.println("3. 수업 개설하기");
		System.out.println("4. 출석부 보기");
		System.out.println("0. 종료");
		System.out.println("==========================================");
	}
	
	// 전체 수업 목록 확인 
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
			System.out.println("printClassList() error"); 
		}
	}
	
	// 교수 수업 리스트 출력
	public void checkMyClass() {
		try {
			pstmt=con.prepareStatement("SELECT C.Cno, C.Cname FROM PROFESSOR P, CLASS C WHERE P.Pno=C.Pnum AND Pno=?");
			pstmt.setInt(1, getNumber());
			rs=pstmt.executeQuery();
			
			// 검색된 데이터 출력 
			System.out.println("수업번호\t수업명");
			
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	// 수업 개설하기 
	public void createClass() {
		
		/*
Cno INTEGER PRIMARY KEY,
Cname VARCHAR(20) NOT NULL,
credit INTEGER,
Cnum INTEGER,
Pnum INTEGER,
		 */
		
		int classNo;
		String className;
		int credit;
		int cRoomNum;
		
		System.out.println("수업 번호를 입력하세요 : ");
		classNo=scan.nextInt();
		System.out.println("수업 이름을 입력하세요 : "); scan.nextLine();
		className=scan.nextLine();
		System.out.println("학점을 입력하세요 : ");
		credit=scan.nextInt();
		System.out.println("강의실 번호를 입력하세요 : ");
		cRoomNum=scan.nextInt();
		
		try {
			pstmt=con.prepareStatement("INSERT INTO CLASS VALUES(?,?,?,?,?)");
			pstmt.setInt(1, classNo);
			pstmt.setString(2, className);
			pstmt.setInt(3, credit);
			pstmt.setInt(4, cRoomNum);
			pstmt.setInt(5, getNumber());
			pstmt.executeUpdate();
						
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 주어진 번호가 해당 교수가 강의하는 강의인지 확인 
	public boolean whetherMyClass(int classNumber) {
		
		try {
			pstmt=con.prepareStatement("SELECT * FROM CLASS WHERE Pnum=? AND Cno=?");
			pstmt.setInt(1, getNumber());
			pstmt.setInt(2, classNumber);
			rs=pstmt.executeQuery();
			
			if(!rs.next()) return false;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	// 출석부 보기 
	public void showStudentList() {
		
		System.out.println("==========================================");
		System.out.println("내 수업 목록");
		checkMyClass();
		System.out.println("==========================================");
		System.out.println("출석부를 확인할 수업 번호를 입력하시오 : ");
		
		int classNumber = scan.nextInt();
		
		// 교수가 강의를 맡는 수업이 아닌 경우
		if(whetherMyClass(classNumber)==false) {
			System.out.println("사용자의 수업이 아닙니다.");
		}
		// 교수가 강의를 맡는 수업이라면 출석부를 열람할 수 있다
		else {
		
			try {
				pstmt=con.prepareStatement("SELECT C.Cno, C.Cname, S.Sname FROM CLASS C, STUDENT S, TAKE_CLASS TC WHERE TC.Cnum=C.Cno AND TC.Snum=S.Sno AND C.Cno=? ORDER BY S.Sname");
				pstmt.setInt(1, classNumber);
				rs=pstmt.executeQuery();
			
				// 검색된 데이터 출력 
				System.out.println("수업번호\t수업명\t\t학생이름");
			
				while(rs.next()) {
					System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3));
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		
		}
	}
	
} // Professor 클래스 닫는 괄호

public class Main {
	
	public static void main(String[] args) {
		
		int choice=100;
		Person per = new Person();

		boolean result; //로그인 성공 결과를 저장해줄 변수
		int loginWho;
		
		do {
			loginWho = per.startMenu();
			
			if(loginWho==1) {
				Student stu=new Student();
				result = stu.loginStudent();
				
				// 메인 메뉴 리스트
				do {
					stu.showMain();	// 메인 메뉴 리스트 출력 
					choice=stu.getChoice(); // 어떤 메뉴를 수행할지 결정
					
					switch(choice) {
					//전체 수업 목록 보기
					case 1:	
						stu.printClassList();
						break;
					
					//로그인한 학생의 수강 리스트 출력하기 
					case 2:
						stu.printStudentClass();
						break;
						
					//로그인한 학생이 과목 수강하기 
					case 3:
						stu.registerClass();
						break;
					
					// 선택한 과목 수강 취소하기 
					case 4:
						stu.cancelClass();
						break;
					
					// 교수 이름을 받아서 해당 교수가 강의하는 과목을 보여준다 
					case 5:
						stu.classPerProfessor();
						break;
					}
					
				}while(choice!=0);
				
				stu.closeAll();
			} 
			
			// 교수로 로그인 했을 때 
			else if (loginWho==2){
				Professor pro = new Professor();
				result = pro.loginProfessor();
				
				// 메인 메뉴 리스트
				do {
					pro.showMain();	// 메인 메뉴 리스트 출력 
					choice=pro.getChoice(); // 어떤 메뉴를 수행할지 결정
					
					switch(choice) {
					//전체 수업 목록 보기
					case 1:	
						pro.printClassList();
						break;
					
					// 수업 리스트 출력하기  
					case 2:
						pro.checkMyClass();
						break;
						
					// 수업 개설하기  
					case 3:
						pro.createClass();
						break;
						
					// 출석부 확인하기 
					case 4:
						pro.showStudentList();
						break;
				
					}
					
				}while(choice!=0);
				
				pro.closeAll();
				
			}
			
		}while((loginWho!=1)&&(loginWho!=2));
		
		
	}//main문 괄호 닫음
	
}// Test 클래스 괄호 닫음