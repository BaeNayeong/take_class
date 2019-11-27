/*
 * <�����ͺ��̽� �ý��� term project>
 * �ۼ��� : �質��
 * �й� : 2015023025
 * �а� : ����Ʈ�����а�
 * Mini world : �б� ���� �ý���
*/

import java.sql.*;
import java.util.Scanner;

/*
 * Person class
 * ��ɰ� ���� : 
 * mysql�� connect�ϴ� ���, Scanner ����
 * Student�� Professor�� ����ϵ��� �Ͽ� �� Ŭ�������� ������ mysql�� �������� �ʿ䰡 ������ �Ѵ�.  
*/
class Person{
	
	Scanner scan=new Scanner(System.in);
	
	Connection con = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
	protected int number; // �й� �Ǵ� ������ȣ
	protected String email; // �α��� �� �� ����� �̸��� 	
	
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
	
	// ����ڰ� �޴��� ������ �� �ֵ��� �ϴ� �޼ҵ� 
	public int getChoice() { 
		int choice=scan.nextInt();
		return choice;
	}
    
    // �����ڿ��� mysql connection
    public Person() {
    	try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://192.168.56.101:4567/university","yeong","1234");
			stmt=con.createStatement();
			
		}catch(Exception e){ System.out.println(e);}
    }
    
    // ResultSet, PreparedStatement, Statement, Connection, Scanner �ݾ���
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
    
    // ����ȭ��
    // �л����� �α����� ������ ������ �α����� ������ �����Ѵ�
    public int startMenu() {
    	
    	int perNum = 0; // ������ �α����� ������ �����ϴ� ���� �޾��ִ� ����
    	
    	System.out.println("===========================================");
    	System.out.println("�α��� ȭ��");
    	System.out.println("�л����� �α����� ������ ������ �α����� ������ ���ÿ�");
    	System.out.println("(1. �л� �α��� / 2. ���� �α���)");
    	System.out.println("===========================================");
    	
    	perNum=scan.nextInt();
    	
    	return perNum;
    }
}

/*
 * Student class (�л� Ŭ����)
 * mini world���� �л��� �ش��Ѵ�.
 * �й��� �̸��Ϸ� �α���
 * ��ü ���� ��� Ȯ��, �ڽ��� ��� ���� Ȯ��, ���� ���, Ư���� ������ ����ϴ� ���� Ȯ���� ������ �� �ִ�.
 */
class Student extends Person{
	
	// �����ڿ���  MySQL ����
	public Student() { 
		super();
	}
	
	// �л��� �й��� ���Ϸ� �α����ϴ� �޼ҵ� 
	public boolean loginStudent() {
		
		System.out.println("�й� : ");
		int inputNum = scan.nextInt();
		System.out.println("�̸��� : ");
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
		
		System.out.println("<<�α��� ����!>>");		
		
		// �α��ο� ���������Ƿ� �й��� �̸��� ������ �������ش�
		setNumber(inputNum);
		setMail(inputMail);

		return true;
	}
	
	// �α��� ���� �������� ȭ��
	// �ý��ۿ��� �л��� ���� ����
	public void showMain() {
		System.out.println("==========================================");
		System.out.println("1. ���� ��� ����");
		System.out.println("2. �� ���� ����Ʈ ����ϱ�");
		System.out.println("3. �����ϱ�");
		System.out.println("4. ���� ����ϱ�");
		System.out.println("5. ���� ��� ���� Ȯ��");
		System.out.println("0. ����");
		System.out.println("==========================================");
	}
	
	// ��ü ���� ����Ʈ�� Ȯ���ϴ� �޼ҵ� 
	public void printClassList() {
		try {
			ResultSet rs=stmt.executeQuery("SELECT * FROM CLASS");
			System.out.println("<��ü ���� ���>"); 
			System.out.println("������ȣ\t������\t\t����\t������ȣ");
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
			}
			System.out.println();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("classList() error"); 
		}
	}
	
	// �α����� �л��� ��� ������� ����ϴ� �޼ҵ� 
	public void printStudentClass() {
		try {
			pstmt=con.prepareStatement("SELECT C.Cno ������ȣ, C.Cname ������, C.credit ����, P.Pname ������ "
					+ "FROM CLASS C, TAKE_CLASS TC, PROFESSOR P WHERE TC.Snum=? AND C.Cno=TC.Cnum AND P.Pno=C.Pnum");
			pstmt.setInt(1, getNumber());
			rs=pstmt.executeQuery();
			
			// �˻��� ������ ��� 
			System.out.println("������ȣ\t������\t\t����\t�����̸�");
			
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getInt(3)+"\t"+rs.getString(4));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	// �л��� ������ ��û�� �� �ֵ��� �ϴ� �޼ҵ� 
	public void registerClass() {
		
		System.out.println("�����ϰ� ���� ���� ��ȣ�� �Է��Ͻÿ�."); 
		scan.nextLine();	
		
		int subject=scan.nextInt(); // ���� ��ȣ
		
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
	
	// �л��� ������ ����� �� �ֵ��� �ϴ� �޼ҵ�
	public void cancelClass() {
		
		System.out.println("���� ����ϰ� ���� ���� �̸��� �Է��Ͻÿ�"); scan.nextLine();
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
	
	// �˻��� ������ ���� ������ ����ϴ� �޼ҵ�
	public void classPerProfessor() {
		
		System.out.println("�˻��ϰ��� �ϴ� ���� �̸��� �Է��ϼ��� : "); scan.nextLine();
		String professor=scan.nextLine();
		
		try {
			
			pstmt=con.prepareStatement("select P.Pname, C.Cname from PROFESSOR P, CLASS C where P.Pno=C.Pnum and P.Pname=?");
			pstmt.setString(1, professor);
			rs = pstmt.executeQuery();
			
			System.out.println("�����̸�\t������");
			
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
 * Professor class (���� Ŭ����)
 * mini world���� ������ �ش��Ѵ�.
 * ������ȣ�� �̸��Ϸ� �α���
 */
class Professor extends Person{
	
	//DB Connection
	public Professor() {
		super();
	}
	
	// ���� �α��� 
	public boolean loginProfessor() {
		
		System.out.println("������ȣ : ");
		int inputNum = scan.nextInt();
		System.out.println("�̸��� : ");
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
		
		System.out.println("<<�α��� ����!>>");		
		
		// �α��ο� ���������Ƿ� ������ȣ�� �̸��� ������ �������ش�
		setNumber(inputNum);
		setMail(inputMail);

		return true;
	}
	
	// ������ �α������� �� ��Ÿ�� �޴� ȭ��
	public void showMain() {
		System.out.println("==========================================");
		System.out.println("1. ���� ��� ����");
		System.out.println("2. �� ���� ����Ʈ ����ϱ�");
		System.out.println("3. ���� �����ϱ�");
		System.out.println("4. �⼮�� ����");
		System.out.println("0. ����");
		System.out.println("==========================================");
	}
	
	// ��ü ���� ��� Ȯ�� 
	public void printClassList() {
		
		try {
			ResultSet rs=stmt.executeQuery("SELECT * FROM CLASS");
			System.out.println("<��ü ���� ���>"); 
			System.out.println("������ȣ\t������\t\t����\t������ȣ");
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
			}
			System.out.println();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("printClassList() error"); 
		}
	}
	
	// ���� ���� ����Ʈ ���
	public void checkMyClass() {
		try {
			pstmt=con.prepareStatement("SELECT C.Cno, C.Cname FROM PROFESSOR P, CLASS C WHERE P.Pno=C.Pnum AND Pno=?");
			pstmt.setInt(1, getNumber());
			rs=pstmt.executeQuery();
			
			// �˻��� ������ ��� 
			System.out.println("������ȣ\t������");
			
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	// ���� �����ϱ� 
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
		
		System.out.println("���� ��ȣ�� �Է��ϼ��� : ");
		classNo=scan.nextInt();
		System.out.println("���� �̸��� �Է��ϼ��� : "); scan.nextLine();
		className=scan.nextLine();
		System.out.println("������ �Է��ϼ��� : ");
		credit=scan.nextInt();
		System.out.println("���ǽ� ��ȣ�� �Է��ϼ��� : ");
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
	
	// �־��� ��ȣ�� �ش� ������ �����ϴ� �������� Ȯ�� 
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
	
	// �⼮�� ���� 
	public void showStudentList() {
		
		System.out.println("==========================================");
		System.out.println("�� ���� ���");
		checkMyClass();
		System.out.println("==========================================");
		System.out.println("�⼮�θ� Ȯ���� ���� ��ȣ�� �Է��Ͻÿ� : ");
		
		int classNumber = scan.nextInt();
		
		// ������ ���Ǹ� �ô� ������ �ƴ� ���
		if(whetherMyClass(classNumber)==false) {
			System.out.println("������� ������ �ƴմϴ�.");
		}
		// ������ ���Ǹ� �ô� �����̶�� �⼮�θ� ������ �� �ִ�
		else {
		
			try {
				pstmt=con.prepareStatement("SELECT C.Cno, C.Cname, S.Sname FROM CLASS C, STUDENT S, TAKE_CLASS TC WHERE TC.Cnum=C.Cno AND TC.Snum=S.Sno AND C.Cno=? ORDER BY S.Sname");
				pstmt.setInt(1, classNumber);
				rs=pstmt.executeQuery();
			
				// �˻��� ������ ��� 
				System.out.println("������ȣ\t������\t\t�л��̸�");
			
				while(rs.next()) {
					System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3));
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		
		}
	}
	
} // Professor Ŭ���� �ݴ� ��ȣ

public class Main {
	
	public static void main(String[] args) {
		
		int choice=100;
		Person per = new Person();

		boolean result; //�α��� ���� ����� �������� ����
		int loginWho;
		
		do {
			loginWho = per.startMenu();
			
			if(loginWho==1) {
				Student stu=new Student();
				result = stu.loginStudent();
				
				// ���� �޴� ����Ʈ
				do {
					stu.showMain();	// ���� �޴� ����Ʈ ��� 
					choice=stu.getChoice(); // � �޴��� �������� ����
					
					switch(choice) {
					//��ü ���� ��� ����
					case 1:	
						stu.printClassList();
						break;
					
					//�α����� �л��� ���� ����Ʈ ����ϱ� 
					case 2:
						stu.printStudentClass();
						break;
						
					//�α����� �л��� ���� �����ϱ� 
					case 3:
						stu.registerClass();
						break;
					
					// ������ ���� ���� ����ϱ� 
					case 4:
						stu.cancelClass();
						break;
					
					// ���� �̸��� �޾Ƽ� �ش� ������ �����ϴ� ������ �����ش� 
					case 5:
						stu.classPerProfessor();
						break;
					}
					
				}while(choice!=0);
				
				stu.closeAll();
			} 
			
			// ������ �α��� ���� �� 
			else if (loginWho==2){
				Professor pro = new Professor();
				result = pro.loginProfessor();
				
				// ���� �޴� ����Ʈ
				do {
					pro.showMain();	// ���� �޴� ����Ʈ ��� 
					choice=pro.getChoice(); // � �޴��� �������� ����
					
					switch(choice) {
					//��ü ���� ��� ����
					case 1:	
						pro.printClassList();
						break;
					
					// ���� ����Ʈ ����ϱ�  
					case 2:
						pro.checkMyClass();
						break;
						
					// ���� �����ϱ�  
					case 3:
						pro.createClass();
						break;
						
					// �⼮�� Ȯ���ϱ� 
					case 4:
						pro.showStudentList();
						break;
				
					}
					
				}while(choice!=0);
				
				pro.closeAll();
				
			}
			
		}while((loginWho!=1)&&(loginWho!=2));
		
		
	}//main�� ��ȣ ����
	
}// Test Ŭ���� ��ȣ ����