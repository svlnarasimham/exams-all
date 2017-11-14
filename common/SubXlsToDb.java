package common;
import java.sql.*;
import utility.*;

public class SubXlsToDb
{
	// Students table field numbers
	public static final int DB_SCODE			= 1 ;
	public static final int DB_SNAME			= 2 ;
	public static final int DB_CCODE			= 3 ;
	public static final int DB_BCODE			= 4 ;
	public static final int DB_SBATCH		= 5 ;
	public static final int DB_EREGULATION	= 6 ;
	public static final int DB_SYEAR			= 7 ;
	public static final int DB_SSEM			= 8 ;
	public static final int DB_SRS			= 9 ;
	public static final int DB_SMXE			= 10;
	public static final int DB_SMXT			= 11;
	public static final int DB_SMNE			= 12;
	public static final int DB_SMNT			= 13;
	public static final int DB_SCREDITS		= 14;
	public static final int DB_STYPE			= 15;
	public static final int DB_SCAT			= 16;
	public static final int DB_ECODE			= 17;
	public static final int DB_SAPPEARED	= 18;
	public static final int DB_SPASSED		= 19;
	public static final int DB_SMOD			= 20;
	public static final int DB_SLNO			= 21;

	/// XLS file column numbers (sequence of columns)
	public static final int COURSE		= 0;
	public static final int BRANCH		= 1;
	public static final int REGULATION	= 2;
	public static final int SUBCODE		= 3;
	public static final int TITLE			= 4;
	public static final int YEAR			= 5;
	public static final int SEM			= 6;
	public static final int MAX_EXT		= 7;
	public static final int MAX_TOT		= 8;
	public static final int MIN_EXT		= 9;
	public static final int MIN_TOT		= 10;
	public static final int TYPE			= 11;
	public static final int CATEGORY		= 12;
	public static final int CREDITS		= 13;
	public static final int BATCH			= 14;
	public static final int EXAMCODE		= 15;
	public static final int REGSUP		= 16;
	public static final int SLNO			= 17;

	public static final int COLCOUNT = 18;
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
		pstmt = getPreparedStatement();
		sa = ExcelTools.ExcelToArray(xlsFileName, 1); // file, SheetNo
		int rows = sa.length;
		int cols = sa[0].length;
		if(cols < COLCOUNT)
		{
			String msg = getHelpMessage();
			Utilities.showMessage(G.jfParent, "Insufficient Column Count");
			Utilities.showMessage(G.jfParent, msg);
			return;
		}
		G.initialize();
		G.dbcon.initConnection(G.dbDriver, G.dbUrl, G.dbUser, G.dbPassword);
		if(!G.dbcon.getErrorMessage().equals(G.dbcon.SUCCESS))
		{
			Utilities.showMessage(G.jfParent, G.dbcon.getErrorMessage());
		}
		for(i=1;i<rows;i++) // Rows .. Leave 0th row (header row).
		{
			setPreparedStatementValues(pstmt, sa[i]);
			// insert record into db
			int res=0;
			try
			{
				res = pstmt.executeUpdate();
			}
			catch (SQLException e)
			{
				res = 0;
				e.printStackTrace();
			}
			if(res != 1)
			{
				System.out.println("Can not insert Record: " + sa[i][SUBCODE]);
				continue;
			}
			count++;
		}
		System.out.println("Imported.. " + count + " Records");
	}
	
	public static PreparedStatement getPreparedStatement()
	{
		String sql1 = "INSERT INTO subjects (" + 
				"SCODE," + 
				"SNAME," + 
				"CCODE," + 
				"BCODE," + 
				"SBATCH," + 
				"EREGULATION," + 
				"SYEAR," + 
				"SSEM," + 
				"SRS," + 
				"SMXE," + 
				"SMXT," + 
				"SMNE," + 
				"SMNT," + 
				"SCREDITS," + 
				"STYPE," + 
				"SCAT," + 
				"ECODE," + 
				"SAPPEARED," + 
				"SPASSED," + 
				"SMOD," + 
				"SLNO) VALUES (" + 
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);" ; 

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
		if(x[SUBCODE].trim().length() > 0 && x[TITLE].trim().length() > 0)
		{
			try
			{
				// Process Fields
				ps.clearParameters();
				ps.setString(DB_SCODE, x[SUBCODE].trim().toUpperCase());
				ps.setString(DB_SNAME, x[TITLE].trim());
				ps.setString(DB_CCODE, x[COURSE].trim().toUpperCase());
				ps.setString(DB_BCODE, Utilities.getPaddedString(x[BRANCH].trim(), 2, "0"));
				ps.setString(DB_SBATCH, x[BATCH].trim());
				ps.setString(DB_EREGULATION, x[REGULATION].trim());
				ps.setString(DB_SYEAR, x[YEAR].trim());
				ps.setString(DB_SSEM, x[SEM].trim());
				ps.setString(DB_SRS, x[REGSUP].trim().toUpperCase());
				ps.setInt(DB_SMXE, Utilities.parseInt(x[MAX_EXT].trim()));
				ps.setInt(DB_SMXT, Utilities.parseInt(x[MAX_TOT].trim()));
				ps.setInt(DB_SMNE, Utilities.parseInt(x[MIN_EXT].trim()));
				ps.setInt(DB_SMNT, Utilities.parseInt(x[MIN_TOT].trim()));
				ps.setInt(DB_SCREDITS, Utilities.parseInt(x[CREDITS].trim()));
				ps.setString(DB_STYPE, x[TYPE].trim().toUpperCase());
				ps.setString(DB_SCAT, x[CATEGORY].trim().toUpperCase());
				ps.setString(DB_ECODE, x[EXAMCODE].trim().toUpperCase());
				ps.setInt(DB_SAPPEARED,0);
				ps.setInt(DB_SPASSED,0);
				ps.setInt(DB_SMOD,0);
				ps.setInt(DB_SLNO,Utilities.parseInt(x[SLNO].trim()));
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
		msg += "COURSE CODE\n";
		msg += "BRANCH CODE\n";
		msg += "REGULATION (YY)\n";
		msg += "SUB CODE\n";
		msg += "SUB TITLE\n";
		msg += "YEAR\n";
		msg += "SEMESTER\n";
		msg += "MAX EXTERNAL MARKS\n";
		msg += "MAX TOTAL MARKS\n";
		msg += "MIN EXTERNAL MARKS FOR PASS\n";
		msg += "MIN TOTAL MARKS FOR PASS\n";
		msg += "TYPE (T/L/S/D)\n";
		msg += "CATEGORY (C/L/O)\n";
		msg += "CREDITS\n";
		msg += "BATCH (YY)\n";
		msg += "EXAM CODE (14 CHAR)\n";
		msg += "REG/SUPPL (R/S)\n";
		msg += "SLNO OF SUBJECT\n";

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
		SubXlsToDb.doImport();
	}
}
