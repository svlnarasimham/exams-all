package expro;
import utility.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;
public class Importer
{
	
	public Importer()
	{
		if(!Globals.isInitialized())
		{
			Globals.initialize();
		}
	}
	
	public void importNR(String examcode)
	{
		String nrsql = "select distinct a.srollno, a.sname, a.bcode, b.scode from students a, registration b where a.srollno = b.srollno and b.ecode='"+examcode+"' order by a.bcode,a.srollno, b.scode;";
		Vector<String[]> v = DbUtils.executeSQL(nrsql);
		String cc = examcode.substring(0,1);
		String yr = examcode.substring(5,6);
		String sm = examcode.substring(6,7);
		String res = "";
		int sz = v.size();
		int nrec=0;
		//System.out.println("SQL: " + nrsql);
		String htn="";
		String snm="";
		String bc ="";
		int nsub =0;
		Vector<String>subcodes = new Vector<String>();
		String arr[];
		if(sz == 0)
		{
			System.out.println("SQL: " + nrsql);
			System.out.println("Can not get NR records from DB..");
			System.exit(1);
		}
		FileWriter fw=null;
		try
		{
			fw = new FileWriter(examcode+"-nr.txt");
			nrec++;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		// arr order = "srollno, sname, bcode, scode";
		for(int i=0;i<v.size(); i++)
		{
			arr = v.get(i);
			if(!arr[0].equalsIgnoreCase(htn)) // new record
			{
				// Write old record first and reset all values
				if(htn.length()>0) // if HTNO not empty
				{
					res = htn+"\t"+snm+"\t"+cc+"\t"+bc+"\t"+yr+"\t"+sm+"\t"+nsub;
					writeNrRec(fw, res, subcodes);
					nrec++;
				}
				subcodes.removeAllElements();
				nsub = 0;
				htn = arr[0];   
				snm = arr[1];
				bc = arr[2];
				subcodes.add(arr[3]);
				nsub++;
			}
			else
			{
				subcodes.add(arr[3]);
				nsub++;
			}
		}
		res = htn+"\t"+snm+"\t"+cc+"\t"+bc+"\t"+yr+"\t"+sm+"\t"+nsub;
		writeNrRec(fw, res, subcodes);
		nrec++;
		try
		{
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Records Written: " + nrec + " to file: " + examcode+"-nr.txt");
		
	}
	
	public void writeNrRec(FileWriter fw, String res, Vector<String>subcodes)
	{
		for(int j=0;j<subcodes.size();j++)
		{
			res += ("\t"+subcodes.get(j));
		}
		res += "\n";
		try
		{
			fw.write(res);
		}
		catch(Exception e){e.printStackTrace();System.exit(1);}

	}

	public void importSubjects(String examcode)
	{
		String subsql = "select scode,sname,ccode,bcode, eregulation,syear,ssem,smxe,smxt,smne,smnt,scredits,stype from subjects where ecode = '"+examcode+"' order by bcode,slno";
		//Code	Title	Course	Branch	Regulation	Year	Sem	MX Ext	MX Tot	MN Ext	MN Tot	Cr	Theory/lab
		//1GC11	ENGINEERING MATHEMATICS - I	A	2	10	1	0	70	100	25	40	4	T

		Vector<String[]> v = DbUtils.executeSQL(subsql);
		String res = "";
		int sz = v.size();
		int nrec=0;
		//System.out.println("SQL: " + subsql);
		String arr[];
		if(sz == 0)
		{
			System.out.println("SQL: " + subsql);
			System.out.println("Can not get Subject records from DB..");
			System.exit(1);
		}
		FileWriter fw=null;
		try
		{
			fw = new FileWriter(examcode+"-sub.txt");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		// arr order = "srollno, sname, bcode, scode";
		for(int i=0;i<v.size(); i++)
		{
			arr = v.get(i);
			res = arr[0];
			for(int j=1;j<arr.length;j++)
			{
				res += ("\t"+arr[j]);
			}
			res += "\n";
			try
			{
				fw.write(res);
				nrec++;
			}
			catch(Exception e){e.printStackTrace();System.exit(1);}
		}
		
		try
		{
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Records Written: " + nrec + " to file: " + examcode+"-sub.txt");
		
	}
	
	public void importIm(String examCode)
	{
		String xlsFile = Globals.basePath + "/" + examCode+"/" + examCode + "-im.xls";
		String targetFolder = Globals.basePath+"/"+examCode;
		ImportTools.importInternals(xlsFile, targetFolder);
	}

	public boolean importEm()
	{
		return true;
	}

	public static void main(String[] args)
	{
	
		DbUtils.initConnection(Globals.dbDriver, Globals.dbUrl, Globals.dbUser, Globals.dbPassword);
		if(!DbUtils.getErrorMessage().equals(DbUtils.SUCCESS))
		{
			System.out.println("Can not connect to Database...");
			System.exit(1);
		}
		else
		{
			System.out.println("Connected to Database..");
		}
		Importer im = new Importer();
		String ecode = JOptionPane.showInputDialog(null,"Enter Exam Code:", "");
		if(ecode != null && ecode.length()>0)
		{
			//im.importNR(ecode.toUpperCase());
			//im.importSubjects(ecode);
			im.importIm(ecode);
		}
		
		DbUtils.closeConnection();
		if(!DbUtils.getErrorMessage().equals(DbUtils.SUCCESS))
		{
			System.out.println("Can not close DB");
		}
		else
		{
			System.out.println("Database connection closed...");
		}
	}
}
