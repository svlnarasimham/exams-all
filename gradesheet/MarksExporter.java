package gradesheet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;
import javax.swing.JOptionPane;

import utility.DbUtils;
import utility.Utilities;

/**
 * This class takes a marks file as input and export the marks to the database<br>
 * This should be run after finalizing and publishing the result.
 * @author svl
 *
 */
public class MarksExporter
{
	/**
	 * Exports marks to the marks table of the database
	 * @param mksFile Marks File generated from  process
	 * @param subFile Subjects File generated from process
	 * @param ecode Examination code
	 * @return Vector of strings where each string is an error message if any.
	 */
	public Vector<String> exportMarks(String mksFile, String subFile, String ecode)
	{
		Vector<String> errv = new Vector<String>();
		
	// Initialize database
		if(!DbUtils.isInitialized())
		{
			DbUtils.initConnection();
		}
		
		// Load Subjects
		Subjects subjects = new Subjects();
		subjects.loadSubjects(subFile);
		if(subjects.getSubjectCount() == 0)
		{
			System.out.println("Can not load subjects from file: "+ subFile);
			errv.add("Can not load Subjects from file: " + subFile);
			return errv;
		}
		
		// Construct SQL statement from the marks file
		String line;
		String arr[];
		String htno="", scode="", res="", grade = "";
		int im, tem, cr;
		int nrec = 0;
		StringBuffer sqlSB = new StringBuffer(""); // SQL String Buffer
		
		// File will be opened, then each line is parsed, and each record
		// will be converted to an insert values string and appended to the string buffer
		// Finally this string buffer will be executed for every 10000 records.
		try
		{
			// Open marks file
			FileReader fr = new FileReader(mksFile);
			BufferedReader br = new BufferedReader(fr);
			// For each line of the file 
			// Skip if it starts with # (comment lines)
			// Skip if length is less than 30 characters
			//      since there are 15 fields and 15 delimiters at least
			// Skip if number of fields is less than 15
			while((line=br.readLine()) != null)
			{
				line = line.trim().toUpperCase();
				if(line.startsWith("#") || line.length() <30)// skip comment and empty or short lines
				{
					continue;
				}
				arr = line.split("\t"); // split line with tab to get fields
				if(arr.length < 15) // Check field count
				{
					System.out.print("At least 15 fields reqired at line: " + line);
					System.out.println(" but obtained only " + arr.length + " fields");
					continue;
				}
				// The marks file has the fields in this order:
				// 0            1     2     3   4     5     6  7   8   9    10    11    12  13  14  
				//HTNO        ccode bcode year sem subcode im em smod adj spmod totem total cr res
				//Example: 
				//15491E0001	E	0	0	1	E1501	39	44	0	0	0	44	83	3	P
				htno = arr[0]; // Roll no
				scode = arr[5]; // Subject code
				im = Utilities.parseMarks(arr[6]); // Internal Marks;
				tem = Utilities.parseMarks(arr[11]); // External Total Marks;
				cr = Utilities.parseMarks(arr[13]); // Credits
				res = arr[14]; // Result
				
				Subject sub = subjects.getSubject(scode);
				if(sub == null)
				{
					System.out.println("Can not find subject: " + scode);
					continue;
				}
				boolean isTheory = sub.tl.equals("T");
				grade = Grades.getGrade(im, tem, sub.mxt, res, isTheory);
				
				int gp = Grades.getGradePoints(grade);
				
				addRecordToSQL(sqlSB, htno, ecode, scode, im, tem, cr, res, grade, gp);
				nrec++;
			}
			br.close();
			fr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Records in SQL: " + nrec);
		if(sqlSB.length() > 0)
		{
			int recCount = DbUtils.executeUpdate(sqlSB.toString());
			System.out.println("Records added to DB: " + recCount);
		}
		return errv;
	}
	
	public boolean addRecordToSQL(
			StringBuffer sb,
			String htno, 
			String ecode, 
			String scode, 
			int im, 
			int em, 
			int cr, 
			String res, 
			String grade, 
			int gp)
	{
		boolean result = true;
		String sql1 = "insert into marks "
				+ "(srollno, ecode, scode, mim, mem, mcr, mresult, mgrade, mgp) values ";
		if(sb.length() == 0)
		{
			sb.append(sql1);
		}
		else 
		{
			sb.append(", ");
		}
		
		String sql = "(" 
				+ "'"+htno+"', "
				+ "'"+ecode+"', "
				+ "'"+scode+"', "
				+ im+", "
				+ em+", "
				+ cr+", "
				+ "'"+res+"', "
				+ "'"+grade+"', "
				+ gp+")";
		sb.append(sql);
		return result;
	}
	
	/**
	 * Exports data to marks table from given subject file and marks files<p>
	 * The file names should be in the format <i><b>ecode</b></i>-mks.txt and <i><b>ecode</b></i>-sub.txt<p>
	 * <b>ecode</b> is 14 character code like <i><b>A151521R160600</b></i>
	 */
	public void doExportMarksToDb()
	{
		String marksFile, subFile, ecode;
		boolean debug = false;
		// Choose Marks File
		marksFile = Utilities.chooseFile("C:\\Download\\qis-all-results", Utilities.OPEN,Utilities.FILES, "[A-F][0-9]{6}[RS][0-9]{6}-mks.txt","Choose Marks File");
		if(debug) System.out.println("Marks File: " + marksFile);
		if(marksFile.length() == 0)
		{
			return;
		}
		
		// get Path and Exam code from marks file
		marksFile = marksFile.replace('\\', '/');
		int idx = marksFile.lastIndexOf('/');
		String path = "/";
		if(idx != -1)
		{
			path = marksFile.substring(0,idx);
		}
		ecode = marksFile.substring(idx+1).split("-")[0];
		
		// Choose Subjects file
		subFile = Utilities.chooseFile(path, Utilities.OPEN,Utilities.FILES, "[A-F][0-9]{6}[RS][0-9]{6}-sub.txt","Choose Subjects File");
		if(debug) System.out.println("Subjects File: " + subFile);
		
		// Get Exam Code 
		ecode = JOptionPane.showInputDialog("Enter Exam Code: ", ecode);
		if(debug) System.out.println("Exam Code: " + ecode);
		
		MarksExporter me = new MarksExporter();
		me.exportMarks(marksFile, subFile, ecode);
	}
	
	public static void main(String[] args)
	{
		new MarksExporter().doExportMarksToDb();
	}
}
