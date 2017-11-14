package utility;
import java.sql.*;
import java.util.*;

import common.G;

public class DbUtils
{
	public static final String SUCCESS = "SUCCESS";
	public static final int ERROR_INT = Integer.MIN_VALUE;

	public Connection con = null;
	public Statement stmt = null;
	public String errorString="";
	public String noSSL = "?autoReconnect=true&useSSL=false";
	public String useSSL="?verifyServerCertificate=true&useSSL=true&requireSSL=true";
	public boolean initConnection()
	{
		return initConnection(
				"com.mysql.jdbc.Driver", 
				"jdbc:mysql://localhost/vnrmasters" + noSSL,
				"root", 
				"");
	}
	
	public boolean initConnection(String driver, String url, String uname, String pwd)
	{
		//System.out.println("Driver:" + driver);
		boolean res = true;
		errorString = SUCCESS;
		if(con == null)
		{
			try
	      {
		      Class.forName(driver);
		      con = DriverManager.getConnection(url+noSSL, uname,pwd);
		      stmt = con.createStatement();
	      }
	      catch (Exception e)
	      {
	      	errorString = "DB Error: " + e. getMessage();
	      	System.out.println(errorString);
	      	e.printStackTrace();
		      res = false;
	      }
		}
		return res;
	}

	public boolean isInitialized()
	{
		return (con != null);
	}
	public boolean isConnected()
	{
		return (con != null);
	}
	
	public Connection getConnection()
	{
		return con;
	}
	
	public Statement getStatement()
	{
		return stmt;
	}
	
	public void closeConnection()
	{
		errorString = SUCCESS;
		if(stmt != null)
		{
			try
	      {
				stmt.close();
	      }
	      catch (Exception e)
	      {
	      	errorString = "DB Error: " + e. getMessage();
	      }
		}
		if(con != null)
		{
			try
	      {
				con.close();
	      }
	      catch (Exception e)
	      {
	      	errorString = "DB Error: " + e. getMessage();
	      }
		}
	}
	
	public String getErrorMessage()
	{
		return errorString;
	}

	public int getResultInt(String sql)
	{
		errorString = SUCCESS;
		int res=0;
		try
		{
			ResultSet rs = stmt.executeQuery(sql);
			if(rs != null && rs.next())
			{
				res = rs.getInt(1);
			}
			rs.close();
		}
		catch(Exception e)
		{
			errorString = e.getMessage();
			res = ERROR_INT;
			Utilities.showMessage("DB Error: " + errorString + "(Error) in executing: " + sql );
			//e.printStackTrace();
		}
		return res;
	}
	
	public String getResultString(String sql)
	{
		errorString = SUCCESS;
		String res="";
		try
		{
			ResultSet rs = stmt.executeQuery(sql);
			if(rs != null && rs.next())
			{
				res = rs.getString(1);
			}
			rs.close();
		}
		catch(Exception e)
		{
			errorString = "DB Error: " + e.getMessage();
			res = "";
			//e.printStackTrace();
		}
		return res;
	}
	
	public ArrayList<String[]> executeSQL(String sql)
	{
		errorString = SUCCESS;
		ArrayList<String[]> v = new ArrayList<String[]>();
		String t[];
		int ncol;
		try
		{
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			ncol = rsmd.getColumnCount();
			while(rs!= null && rs.next())
			{
				t = new String[ncol];
				for(int i=0;i<ncol; i++)
				{
					t[i] = rs.getString(i+1);
				}
				v.add(t);
			}
			rs.close();
		}
		catch(Exception e)
		{
			errorString = "DB Error: " + e.getMessage();
			//e.printStackTrace();
		}
		
		if(!errorString.equals(SUCCESS))
		{
			Utilities.showMessage(errorString);
			v=null;
		}
		return v;
	}
	
	public int executeUpdate(String sql)
	{
		errorString = SUCCESS;
		int res=0;
		try
		{
			res = stmt.executeUpdate(sql);
		}
		catch(Exception e)
		{
			errorString = "DB Error: " + e.getMessage();
			//e.printStackTrace();
		}
		if(!errorString.equals(SUCCESS))
		{
			Utilities.showMessage("DB Error: " + errorString);
			res = -1;
		}
		return res;
	}
	
	public ArrayList<String[]> getTableMetaData(String dbName, String tableName)
	{
		ArrayList<String[]> al = new ArrayList<>();
    	String   cat  = null; // Schema
    	String   scp  = dbName; // Schema  Name Pattern
    	String   tbp  = tableName; // Table Name Pattern
    	String   clp  = null; // Column Name pattern
    	try
    	{
    	DatabaseMetaData dbmd = con.getMetaData();
    	ResultSet result =  dbmd.getColumns(cat, scp,  tbp, clp);
    	while(result.next()){
    		al.add(new String[] {result.getString(4), result.getString(6), result.getString(16)});
    	}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return al;
	}
	/* */ 
	public static void main(String[] args)
	{
		DbUtils dbu = new DbUtils();
		dbu.initConnection(G.dbDriver, G.dbUrl, G.dbUser, G.dbPassword);
		int res = dbu.getResultInt("select count(*) from students");
		System.out.println("res: " + res);
		ArrayList<String[]> al = dbu.getTableMetaData("qisexams", "students");
		for(String arr[]:al)
		{
			System.out.println(arr[0] + ": " + arr[1] + ": " + arr[2]);
		}
		dbu.closeConnection();
	}
	/* */
}
