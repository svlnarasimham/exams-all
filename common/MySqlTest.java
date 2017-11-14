package common;
import java.sql.*;

public class MySqlTest
{
	
	public static void main(String[] argv)
	{
		Connection con;
		Statement stmt;
		//ResultSet rs;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/qisexams", "root", "");
			stmt = con.createStatement();
			String sql = "INSERT INTO students (srollno,name,sfname,smname,sgender,sdob,saddr1,saddr2,saddr3,spin,sphone,semail,sparentPhone,sdoj,sdoc,ccode,bcode,sdiscontinued,scompleted,scurYear,scurSem,scat,spwd,sregl,slatentry) VALUES (";
			for(int i=0;i<24;i++)
			{
				sql += ("'X',");
			}
			sql += "'X');";
			System.out.println("SQL: " + sql);
			int res = stmt.executeUpdate(sql);
			System.out.println("Update Count: " + res);
			/*
			String sql = "select * from courses;";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				System.out.println(rs.getString(1) + "\t" + rs.getString(2)+"\t" + rs.getString(3));
			}
			rs.close();
			stmt.close();
			*/
			stmt.close();
			con.close();
		}
		catch (Exception e)
		{
			System.out.println("Connection Error....");
			e.printStackTrace();
			return;
		}
	}
}
