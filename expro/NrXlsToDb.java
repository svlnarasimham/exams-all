package expro;
import java.sql.*;

import common.G;
import utility.*;

public class NrXlsToDb
{
	// Students table field numbers
	public static final int DB_SROLLNO			= 1 ;
	public static final int DB_SNAME			= 2 ;
	public static final int DB_SFNAME			= 3 ;
	public static final int DB_SMNAME			= 4 ;
	public static final int DB_SGENDER			= 5 ;
	public static final int DB_SDOB				= 6 ;
	public static final int DB_SADDR1			= 7 ;
	public static final int DB_SADDR2			= 8 ;
	public static final int DB_SADDR3			= 9 ;
	public static final int DB_SPIN				= 10;
	public static final int DB_SPHONE			= 11;
	public static final int DB_SEMAIL			= 12;
	public static final int DB_SPARENTPHONE		= 13;
	public static final int DB_SDOJ				= 14;
	public static final int DB_SDOC				= 15;
	public static final int DB_CCODE			= 16;
	public static final int DB_BCODE			= 17;
	public static final int DB_SDISCONTINUED	= 18;
	public static final int DB_SCOMPLETED		= 19;
	public static final int DB_SCURYEAR			= 20;
	public static final int DB_SCURSEM			= 21;
	public static final int DB_SCAT				= 22;
	public static final int DB_SPWD				= 23;
	public static final int DB_SREGL			= 24;
	public static final int DB_SLATENTRY		= 25;
	public static final int DB_SUID				= 26;
	
	/// XLS file column numbers (sequence of columns)
	public static final int SLNO		= 0;
	public static final int ROLLNO		= 1;
	public static final int NAME		= 2;
	public static final int FNAME		= 3;
	public static final int MNAME		= 4;
	public static final int GENDER		= 5;
	public static final int DOB			= 6;
	public static final int ADDR1		= 7;
	public static final int ADDR2		= 8;
	public static final int ADDR3		= 9;
	public static final int PIN			= 10;
	public static final int MOBILE		= 11;
	public static final int EMAIL		= 12;
	public static final int PPHONE		= 13;
	public static final int DOJ			= 14;
	public static final int COURSE		= 15;
	public static final int BRANCH		= 16;
	public static final int SSTAT		= 17;
	public static final int REGL		= 18;
	public static final int LATENTRY	= 19;
	public static final int AADHARID	= 21;
	public static final int COLCOUNT	= 22;
	public static PreparedStatement pstmt;
	
	public static void doImport()
	{
		int j;
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
		xlsFileName = Utilities.chooseFile("g:/qis/data", Utilities.OPEN,	Utilities.FILES, "..*xls", "Choose XLS Students File...");
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
		for(j=1;j<rows;j++) // Rows .. Leave 0th row (header row).
		{
			setPreparedStatementValues(pstmt, sa[j]);
			// insert record into db
			int res=0;
			try
			{
				//System.out.println(pstmt.toString());
				res = pstmt.executeUpdate();
			}
			catch (SQLException e)
			{
				res = 0;
				e.printStackTrace();
			}
			if(res != 1)
			{
				System.out.println("Can not insert Record: " + sa[j][ROLLNO]);
				continue;
			}
			count++;
		}
		System.out.println("Imported.. " + count + " Records");
	}
	
	public static PreparedStatement getPreparedStatement()
	{
		String sql1 = "INSERT INTO stux (" +
				"srollno," +
				"sname," +
				"sfname," +
				"smname," +
				"sgender," +
				"sdob," +
				"saddr1," +
				"saddr2," +
				"saddr3," +
				"spin," +
				"sphone," +
				"semail," +
				"sparentPhone," +
				"sdoj," +
				"sdoc," +
				"ccode," +
				"bcode," +
				"sdiscontinued," +
				"scompleted," +
				"scurYear," +
				"scurSem," +
				"scat," +
				"spwd," +
				"sregl," +
				"slatentry," +
				"suid) VALUES (" +
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, md5(?),?,?,?);";
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
		if(x[ROLLNO].trim().length() > 0 && x[NAME].trim().length() > 0)
		{
			try
			{
				// Process Fields
				ps.clearParameters();
				ps.setString(DB_SROLLNO, x[ROLLNO].trim().toUpperCase());
				ps.setString(DB_SNAME, x[NAME].trim().toUpperCase());
				ps.setString(DB_SFNAME, x[FNAME].trim().toUpperCase());
				ps.setString(DB_SMNAME, x[MNAME].trim().toUpperCase());
				ps.setString(DB_SGENDER, x[GENDER].trim().toUpperCase());
				ps.setString(DB_SDOB, getDate(x[DOB].trim()));
				ps.setString(DB_SADDR1, cleanAddress(x[ADDR1].trim(),50));
				ps.setString(DB_SADDR2, cleanAddress(x[ADDR2].trim(),50));
				ps.setString(DB_SADDR3, cleanAddress(x[ADDR3].trim(),50));
				ps.setString(DB_SPIN, x[PIN].trim());
				ps.setString(DB_SPHONE, x[MOBILE].trim());
				ps.setString(DB_SEMAIL, x[EMAIL].trim());
				ps.setString(DB_SPARENTPHONE, x[PPHONE].trim());
				ps.setString(DB_SDOJ, getDate(x[DOJ].trim()));
				ps.setString(DB_SDOC, getDate("0-0-0"));
				ps.setString(DB_CCODE, x[COURSE].trim().toUpperCase());
				ps.setString(DB_BCODE, Utilities.getPaddedString(x[BRANCH].trim(),2,"0"));
				ps.setString(DB_SDISCONTINUED,"N");
				ps.setString(DB_SCOMPLETED,"N");
				ps.setString(DB_SCURYEAR,"1");
				ps.setString(DB_SCURSEM,"1");
				ps.setString(DB_SCAT, x[SSTAT].trim().toUpperCase());
				ps.setString(DB_SPWD, x[ROLLNO].trim().toUpperCase());
				ps.setString(DB_SREGL, x[REGL].trim().toUpperCase());
				ps.setString(DB_SLATENTRY, x[LATENTRY].trim().toUpperCase());
				ps.setString(DB_SUID, x[AADHARID].trim());
				
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
	public static String getDate(String s)
	{
		String dt;
		String sa[] = s.trim().split("[^0-9]"); // split on any nonzero character
		if(sa.length == 3)
		{
			dt = sa[2]+"-" + sa[1] + "-" + sa[0];
		}
		else 
		{
			dt = "0000-00-00"; // yyyy-mm-dd format
		}
		return dt;
	}
	
	public static String getHelpMessage()
	{
		String msg = "FIRST ROW SHOULD CONTAIN HEADDINGS\n";	
		msg += "Columns order should be:\n";
		msg += "SLNO\n";
		msg += "ROLLNO\n";
		msg += "NAME\n";
		msg += "Father NAME\n";
		msg += "Mother NAME\n";
		msg += "GENDER - (M/F)\n";
		msg += "Date of Birth - (DD-MM-YYYY format)\n";
		msg += "ADDR1 (50 chars maximum and No ' and \")\n";
		msg += "ADDR2\n";
		msg += "ADDR3\n";
		msg += "PIN Code\n";
		msg += "MOBILE\n";
		msg += "EMAIL\n";
		msg += "Parent Phone\n";
		msg += "Date of Joining (DD-MM-YYYY format)\n";
		msg += "Date of Completion (DD-MM-YYYY format)\n";
		msg += "Course Code (A|D|E|F)\n";
		msg += "Branch Code (1, 2, ...)\n";
		msg += "Social Status\n";
		msg += "Regulation (YY format (Ex: 15))\n";
		msg += "Lateral Entry (Y|N)\n";
		msg += "Aadhar Card No\n";
		
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
		NrXlsToDb.doImport();
	}
}
