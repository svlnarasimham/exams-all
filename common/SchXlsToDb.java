package common;
import java.sql.*;
import utility.*;

public class SchXlsToDb
{
	// Students table field numbers
	public static final int DB_DATE			= 1 ;
	public static final int DB_ECODE			= 2 ;
	public static final int DB_SCODE			= 3 ;

	/// XLS file column numbers (sequence of columns)
	public static final int SLNO		= 0;
	public static final int DATE		= 1;
	public static final int ECODE		= 2;
	public static final int SCODE0	= 3;

	public static PreparedStatement pstmt;
	
	public static void doImport()
	{
		int i;
		int count=0;
		String sa[][];
		String xlsFileName = "";
		if(!G.dataLoaded)
		{
			G.initialize();
		}
		if(G.dbcon.getConnection() == null)
		{
			G.dbcon.initConnection(G.dbDriver, G.dbUrl, G.dbUser, G.dbPassword);
		}
		//xlsFileName = Utilities.chooseFile(G.basePath, Utilities.OPEN,	Utilities.FILES, "..*xls", "Choose XLS Students File...");
		xlsFileName = Utilities.chooseFile("f:/qis/data", Utilities.OPEN,	Utilities.FILES, "..*xls", "Choose XLS Students File...");
		if(xlsFileName == null || xlsFileName.length() == 0)
		{
			return; // do nothing if file is not chosen
		}
		pstmt = getPreparedStatement(); // Get Prepared statement 
		sa = ExcelTools.ExcelToArray(xlsFileName, 1); // file, SheetNo
		int rows = sa.length;
		int cols = sa[0].length;
		System.out.println("Number of Columns: " + cols);
		String tdate="", tscode="", tecode="";
		int tslno=0;
		if(cols < 4) // minimum 4 columns (slno, date, ecode, at least one subject code
		{
			String msg = getHelpMessage();
			Utilities.showMessage(G.jfParent, "Insufficient Column Count");
			Utilities.showMessage(G.jfParent, msg);
			return;
		}
		if(G.dbcon.getConnection() == null)
		{
			G.initialize();
			G.dbcon.initConnection(G.dbDriver, G.dbUrl, G.dbUser, G.dbPassword);
			if(!G.dbcon.getErrorMessage().equals(G.dbcon.SUCCESS))
			{
				Utilities.showMessage(G.jfParent, G.dbcon.getErrorMessage());
			}
		}
		for(i=1;i<rows;i++) // Rows .. Leave 0th row (header row).
		{
			tslno = utility.Utilities.parseInt(sa[i][SLNO]);
			tdate = sa[i][DATE];
			tecode = sa[i][ECODE];
			for(int k = SCODE0; k< cols; k++)
			{
				if(sa[i][k].trim().length() > 0)
				{
					int res = 0;
					tscode = sa[i][k].trim().toUpperCase();
					try
					{
						pstmt.clearParameters();
						pstmt.setInt(1, tslno);
						pstmt.setString(2, tdate);
						pstmt.setString(3, tecode);
						pstmt.setString(4, tscode);
						res = pstmt.executeUpdate();// insert record into db
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}
					if(res != 1)
					{
						System.out.println("Can not insert Record: " + tdate);
						continue;
					}
					count++;
				}
			
			}
		}
		System.out.println("Imported.. " + count + " Records");
	}
	
	public static PreparedStatement getPreparedStatement()
	{
		String sql1 = "INSERT INTO schedule (slno,sdate,ecode,scode) VALUES (?,?,?,?);" ; 

		//System.out.println("Prepared Statement: \n" + sql1);
		//System.out.flush();
		PreparedStatement ps=null;
		try
		{
			Connection con = G.dbcon.getConnection();
			if(con == null)
			{
				System.out.println("Connection is NULL");
				System.exit(1);
			}
			else
			{
				ps = con.prepareStatement(sql1);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ps;
	}
	
	public static void setPreparedStatementValues(PreparedStatement ps, String x[])
	{
		if(x[DATE].trim().length() > 0 && x[ECODE].trim().length() > 0)
		{
			try
			{
				// Process Fields
				ps.clearParameters();
				ps.setString(DB_DATE, x[0].trim());
				ps.setString(DB_ECODE, x[1].trim().toUpperCase());
				ps.setString(DB_SCODE, x[2].trim().toUpperCase());
				System.out.println("Date:" + x[0].trim()+":");
				System.out.println("ECODE:" + x[1] + ";");
				System.out.println("SCODE:" + x[2] + ":");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return;
	}
	
	/**
	 * Converts a string to proper ANSI YYYY-MM-DD date format
	 * @param s Date as string in MM-DD-YYYY format 
	 * @return ANSI Date string with 'YYYY-MM-DD' format
	 */
	public static String getHelpMessage()
	{
		String msg = "FIRST ROW SHOULD CONTAIN HEADDINGS\n";	
		msg += "Columns order should be:\n";
		msg += "Date of Exam (dd-mm-yyyy format)\n";
		msg += "Exam Code (14 char)\n";
		msg += "Subject Codes (1 per column)";
		return msg;
	}
	
	
	public static String cleanAddress(String addr, int len)
	{
		String a1;
		a1 = addr.toUpperCase().replace("'", "\\'");
		if(a1.length() > len)
		{
			a1 = a1.substring(0,len);
		}
		return a1;
	}
	
	public static void main(String args[])
	{
		SchXlsToDb.doImport();
	}
}
