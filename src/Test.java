import java.sql.*;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection(
			"jdbc:mysql://192.168.56.101:4567/university","yeong","1234");
			
			Statement stmt=con.createStatement();
			
			// 학생 전체 정보 출력하기
			ResultSet rs=stmt.executeQuery("SELECT * FROM STUDENT");
			 
			while(rs.next()) {
				System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4));
			}
			System.out.println();
			
			//교수 전체 정보 출력하기 
			rs=stmt.executeQuery("SELECT * FROM PROFESSOR");
			
			while(rs.next()) {
				System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4));
			}
			
			con.close();
			
			
			
		}catch(Exception e){ System.out.println(e);}
	}

}
