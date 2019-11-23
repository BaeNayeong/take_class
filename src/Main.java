import java.sql.*;
import java.util.Scanner;

class University{
	
	Connection con = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
	private int number; //������ ������ �� �α����� �й�
	private String email; //������ ������ �� �α����� �̸���	
	
	// �����ڿ���  MySQL ����
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
	
	// �л��� �й��� ���Ϸ� �α����ϴ� �޼ҵ� 
	public boolean loginStudent(int number, String mail) {
		try {
			pstmt=con.prepareStatement("SELECT * FROM STUDENT WHERE Sno=? and mail=?");
			pstmt.setInt(1, getNumber());
			pstmt.setString(2, getMail());
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("<<�α��� ����!>>");
		return true;
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
			pstmt.setInt(1, number);
			rs=pstmt.executeQuery();
			
			// �˻��� ������ ��� 
			System.out.println("������ȣ\t������\t\t����\t�����̸�");
			
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
			}

			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	// �л��� ������ ��û�� �� �ֵ��� �ϴ� �޼ҵ� 
	public void registerClass() {
		
	}
	
	
	public void showMain() {
		System.out.println("==========================================");
		System.out.println("1. ���� ��� ����");
		System.out.println("2. �� ���� ����Ʈ ����ϱ�");
		System.out.println("3. �����ϱ�");
		System.out.println("0. ����");
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
		
		// �α��� ȭ��
		boolean result; //�α��� ���� ����� �������� ����
		do {
			System.out.println("==========================================");
			System.out.println("�α��� ȭ��");
			System.out.println("==========================================");

			System.out.println("�й� : ");
			int num=scan.nextInt();
			scan.nextLine();	//���๮�� ���� 
			System.out.println("���� : ");
			String mail=scan.nextLine();
			
			univ.setNumber(num);
			univ.setMail(mail);
			result=univ.loginStudent(num, mail);
			
		}while(result==false);
		
		// ���� �޴� ����Ʈ
		do {
			
			univ.showMain();	// ���� �޴� ����Ʈ ��� 
			choice=scan.nextInt();	// ����ڰ� ����� �������� ������ 
			
			switch(choice) {
			//��ü ���� ��� ����
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
	}//main�� ��ȣ ����
	
}// Test Ŭ���� ��ȣ ����