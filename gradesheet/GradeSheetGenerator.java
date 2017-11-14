package gradesheet;
import utility.DbUtils;
import utility.Utilities;

import java.io.*;
import java.util.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import common.Branches;
import common.Courses;
import common.G;
import common.Mark;
import common.Subject;
import common.Subjects;
import expro.ExamData;
import expro.Nroll;
import expro.Nrolls;

public class GradeSheetGenerator extends PdfGenerator
{
	public static final int GRADESHEET = 0; 
	public static final int ESSENTIAL  = 1;
	/**
	 * Storage Class that holds Credits, Grade points and subject credits
	 */
	private class GradePointData
	{
		int credits;
		int subCredits;
		int gradePoints;
		int year;
		int sem;
		/**
		 * Constructor
		 * @param cr Credits Obtained
		 * @param gp Grade Points obtained
		 * @param scr Subject Credits
		 */
		public GradePointData(int cr, int gp, int scr, int year, int sem)
		{
			credits = cr;
			subCredits = scr;
			gradePoints = gp;
			this.year = year;
			this.sem = sem;
		}
	} // End of storage class

	// //////////// Required variables ////////////////////
	public Courses			crs			= null;
	public Branches			brs			= null;
	public Subjects			subs		= null;
	public Nrolls			nrs			= null;
	public ExamData			ed			= null;
	public Document			doc			= null;
	public PdfWriter		pw			= null;
	public PdfContentByte	pcb			= null;
	
	public float dwidths[] = { 6.5f, 1f };
	public float dtblWidth = 7.25f; // Total data table width
	//public float mwidths[] = { 3f, 6f, 40f, 3f, 3f, 3f }; // Marks
	public float mwidths[] = { 2.2f, 3f, 25f, 3f, 3f, 3f }; // Marks
	public float mtblWidth = 7.25f;	// Marks
	public float fwidths[] = { 0.6f, 3.65f, 1.0f, 2.5f }; // Footer
	public float ftblWidth = 7.25f; // Footer
	public String mksTblHdr[] = { "S.No.", "Subject Code", "Subject Title", "Grade", "Grade Points", "Credits" };

	// ///////////////////////////////////// Get Secret Code
	/**
	 * Get a specially computed unique code from the student details 
	 * @param nr Nominal Rolls object
	 * @return Computed unique code
	 */
	public int getCode(Nroll nr)
	{
		int mf = 19;
		// char at 0 + 128 * length of name
		// Name Based Value
		int nameVal = nr.name.length() * 128 + nr.name.charAt(0);
		// Exam Title based value
		int etVal = ed.examTitle.length() * 128 + ed.examTitle.toUpperCase().charAt(0);
		int mksVal = 0;
		int crsum = 0;
		StringBuffer bs = new StringBuffer("");
		for(int i = 0; i < nr.nregd; i++)
		{
			bs.append(nr.mks.get(i).grade);
			crsum += nr.mks.get(i).cr;
		}
		String mks = bs.toString().toUpperCase();
		for(int i = 0; i < mks.length(); i++)
		{
			mksVal += ((i + 1) * mks.charAt(i) * mf);
		}
		return 10000000 + nameVal * 1000 + etVal * 100 + mksVal * 10 + crsum;
	}
	
	// //////////////////////////////////////// Create Header Block data
	/**
	 * Creates the Data Array from the NR
	 * @param nr NR object
	 * @return Array of Strings containing name=value pair information
	 */
	public String[] createDataArray(Nroll nr, int type)
	{
		ArrayList<String> ar = new ArrayList<String>();
		if(type == GRADESHEET)
		{
			ar.add("sheetTitle=GRADE SHEET");
		}
		else
		{
			ar.add("sheetTitle=GRADE SHEET (Additional Credit Course)");
		}
		
		ar.add("sheetType=Original");
		ar.add("slnoLabel=Code");
		ar.add("slnoValue=" + getCode(nr));
		ar.add("examTitle=" + ed.examTitle);
		ar.add("photoFile=" + ed.photofName + "/" + nr.rollno + ".jpg");
		ar.add("defaultPhotoFile=c:/qis-exams/defaults/default-photo.jpg");
		ar.add("brnLabel=Branch");
		ar.add("brnValue=" + (brs.getBranch(nr.ccode, nr.bcode)).bname);
		ar.add("rlnoLabel=Roll No");
		ar.add("rlnoValue=" + nr.rollno);
		ar.add("nameLabel=Name");
		ar.add("nameValue=" + nr.name);
		ar.add("fnameLabel=Father Name");
		ar.add("fnameValue=" + nr.fname);
		ar.add("mnameLabel=Mother Name");
		ar.add("mnameValue=" + nr.mname);
		ar.add("myLabel=Month and Year of Exam");
		ar.add("myValue=" + ed.examMY);
		String x[] = new String[ar.size()];
		return ar.toArray(x);
	}
	
	/**
	 * Creates Marks Table data as an array of two dimensional strings
	 * @param nr NRoll Object
	 * @return Marks table array
	 */
	public String[][] getMarksTableData(Nroll nr, int type)
	{
		String x[][] = new String[nr.nregd][6];
		int nrows = nr.nregd;
		int slno = 0;
		for(int i = 0; i < nrows; i++)
		{
			Mark m = nr.mks.get(i);
			Subject sb = subs.getSubject(m.scode);
			if(type == GRADESHEET && sb != null && sb.type.toUpperCase().equals("E"))
			{
				continue;
			}
			if(type == ESSENTIAL && sb != null && !sb.type.toUpperCase().equals("E"))
			{
				continue;
			}
			x[slno][0] = String.format("%2d", (slno + 1)); // SLNO
			x[slno][1] = m.scode; // Subject Code
			x[slno][2] = m.sname; // Subject name
			x[slno][3] = m.grade; // Grade obtained
			x[slno][4] = String.format("%2d", m.gradePoints); // Grade points obtained
			x[slno][5] = String.format("%2d", m.cr); // Credits obtained
			slno++;
		}
		return x;
	}
	
	/**
	 * Sets Grade, grade points, Subject name, subject credits for the given subject 
	 * @param m
	 * @return
	 */
	private boolean setGrade(Mark m)
	{
		Subject sb = subs.getSubject(m.scode);
		if(sb == null)
		{
			Utilities.showMessage(G.jfParent, "Can not find subject: " + m.scode);
			return false;
		}
		m.sname = sb.name;
		m.scr = sb.cr;
		boolean isTheory = sb.type.equals("T");
		String grade = Grades.getGrade(m.im, m.tem, sb.mxt, m.res, (sb.type.toUpperCase().equals("E")?true:isTheory));
		m.grade = grade;
		m.gradePoints = Grades.getGradePoints(grade);
		return true;
	}
	
	/**
	 * Reads marks from marks file for a given roll number
	 * @param nr Nroll object that contains the Roll Number of the student
	 * @return number of marks loaded
	 */
	public int loadMarks()
	{
		// LIne format:
		// Rollno Course Branch Year Semester Scode IM EM SM SPM GM EXTOT TOT CR
		// RES GRD
		// 15491A0101 A 1 1 1 A0001 29 41 0 0 0 41 70 3 P F
		
		int cnt = 0;
		String line;
		
		FileReader fr = null;
		BufferedReader br = null;
		
		try
		{
			fr = new FileReader(ed.mksfName);
			br = new BufferedReader(fr);
			while ((line = br.readLine()) != null)
			{
				line = line.toUpperCase();
				if(line.length() < 30)
				{
					continue;
				}
				
				Scanner sc = new Scanner(line);
				sc.useDelimiter("\t");
				String rno = sc.next(); // skip rollno
				Nroll nr = nrs.getNroll(rno);
				if(nr == null)
				{
					G.out.println("Can not find NR for " + rno + " but marks present");
					continue;
				}
				nr.ccode = sc.next();
				nr.bcode = sc.nextInt();
				nr.year = sc.next();
				nr.sem = sc.next();
				Mark m = new Mark();
				m.slno = cnt + 1;
				// SCODE IM EM SM SPM GM EXTOT TOT CR RES GRD
				m.scode = sc.next();
				m.im = sc.nextInt();
				m.em = sc.nextInt();
				m.sm = sc.nextInt();
				m.spm = sc.nextInt();
				m.gm = sc.nextInt();
				m.tem = sc.nextInt();
				m.tm = sc.nextInt();
				m.cr = sc.nextInt();
				m.res = sc.next();
				boolean res = setGrade(m);
				if(!res)
				{
					G.out.println("Can not get Grade for RollNo:" + nr.rollno + " subject: " + m.scode);
					sc.close();
					continue;
				}
				nr.mks.add(m);
				nr.resetRegdCount();
				sc.close();
				cnt++;
			}
			try
			{
				fr.close();
				br.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
		}
		G.out.println("Records Loaded: " + cnt);
		return cnt;
	}
	
	
	// /////////////////////////////// Compute GPA and CGPA
	/**
	 * Computes GPA and CGPA using Data from nr and old data from Database
	 * @param nr Nroll object which contains current semester marks
	 * @param res Array of two float values which will be filled with GPA and CGPA
	 */
	public void computeCGPA(Nroll nr, float res[])
	{
		int curYear = parseRoman(ed.year); // current year
		int curSem = parseRoman(ed.semester); // current semester

		// Create a hash table for holding grade data
		Hashtable<String, GradePointData> mht = new Hashtable<String, GradePointData>();
		// Get Old Data from Database
		String sql1 = "select distinct a.scode, a.mgp, a.mcr, b.scredits, b.syear, ssem " + 
				"from marks a, subjects b, exams c " + 
				"where a.srollno = '" + nr.rollno + "' and a.scode = b.scode and c.ecode=a.ecode and  a.ecode <= '" + ed.examCode + "' "
						+ " and b.ecode=a.ecode  order by a.ecode;"; //
		Vector<String[]> rv = G.dbcon.executeSQL(sql1);
		
		// push all data into the hash table
		for(int i=0; i<rv.size(); i++)
		{
			String x[] = rv.get(i);
			String scode = x[0].trim();
			int gp = Utilities.parseInt(x[1]);
			int cr = Utilities.parseInt(x[2]);
			int scr = Utilities.parseInt(x[3]);
			int yr = Utilities.parseInt(x[4]);
			int sm = Utilities.parseInt(x[5]);
			
			GradePointData tmpGpd = mht.get(scode);
			Subject sb = subs.getSubject(scode);
			if(sb != null && sb.type.toUpperCase().equals("E"))
			{
				continue;
			}
			if(tmpGpd == null)
			{
				mht.put(scode, new GradePointData(cr, gp, scr, yr, sm)); // Add record 
				//G.out.println("1"+ ":"+scode+","+cr+ "," +gp);
			}
			else // already in the data
			{
				if(tmpGpd.credits == 0) // if failed earlier
				{
					mht.put(scode, new GradePointData(cr, gp, scr, yr, sm)); // add replace record.
					//G.out.println("2"+ ":"+scode+","+cr+ "," +gp);
				}
			}

			// Add record to hash table
		}
		
		// Now add current data to hash table
		for(int i=0;i<nr.nregd; i++)
		{
			Mark m = nr.mks.get(i); // get i-th marks
			GradePointData tmpGpd = mht.get(m.scode);
			Subject sb = subs.getSubject(m.scode);
			if(sb!=null && sb.type.equalsIgnoreCase("E"))
			{
				continue;
			}
			if(tmpGpd == null) // no record found, hence add record 
			{
				mht.put(m.scode, new GradePointData(m.cr, m.gradePoints, m.scr, curYear, curSem)); 
				//G.out.println("3" +"," +m.scode+ ","+ ","+m.cr+","+m.gradePoints);
			}
			else // already exists, so check if failed and then only add 
			{
				if(tmpGpd.credits == 0) // if failed earlier
				{
					//G.out.println("4" +"," +m.scode+ "," + ","+m.cr+":"+m.gradePoints);
					//G.out.println("5" +"," +m.scode+ ","+ ","+m.cr+","+m.gradePoints);
					mht.put(m.scode, new GradePointData(m.cr, m.gradePoints, m.scr, curYear, curSem)); 
				}
			}
		}
		
		// GPA and CGPA calculation
		Enumeration<String> keys = mht.keys();
		
		int csum = 0, cgpaCrxgpsum=0;
		int gsum = 0, gpaCrxgpsum= 0;
		// Print Header
		//G.out.println("scode gPts crdt  gsum gCrxgp csum cgxgpsum");
		while (keys.hasMoreElements())
		{
			String sc = keys.nextElement();
			GradePointData gpd = mht.get(sc); // get one object
			//G.out.println("cy cs is" +"\t" + "gy gs:" +" "+curYear +","+curSem+ "and \t"+gpd.year+","+gpd.sem);
			if(gpd.year == curYear && gpd.sem == curSem)
			{
				gsum += gpd.subCredits; // add subject credits
				gpaCrxgpsum += (gpd.credits*gpd.gradePoints);
			}
			
			csum += gpd.subCredits; // get subject credits
			cgpaCrxgpsum += (gpd.credits*gpd.gradePoints); // get credits x Grade Points
			//G.out.println(sc + ": " + gpd.gradePoints + ", " + gpd.credits + ", " + gsum + ", " + gpaCrxgpsum + ", " + csum + ", " + cgpaCrxgpsum);
		}

		if(nr.rollno.equalsIgnoreCase("15491A05E9"))
		{
			// Compute GPA and CGPA and put them in an array
			G.out.println("SGPA: Cr x GP Sum: " + gpaCrxgpsum + "  ");
			G.out.println("SGPA: Grades SUM: "+ gsum + "  ");
			G.out.println("CGPA: Cr x GP Sum: " + cgpaCrxgpsum + "  ");
			G.out.println("CGPA: Grades SUM: "+ csum);
		}
		
		res[0] = (gsum != 0)? ((float) gpaCrxgpsum / (float) gsum) : 0.00f;
		res[1] = (csum != 0)? ((float) cgpaCrxgpsum / (float) csum) : 0.00f;
	}

	// ///////////////////////////////// Get Footer Data 
	/**
	 * Gets Footer Array for the Marks Memo
	 * @param nr Nominal Rolls
	 * @return Array of Strings containing = separated name value pairs
	 */
	public String[] getFooterArray(Nroll nr)
	{
		// Compute GPA and CGPA first
		float gpas[] = new float[2];
		computeCGPA(nr, gpas);
		// Fill various key value pairs
		ArrayList<String> ar = new ArrayList<String>();
		ar.add("gpaLabel=SGPA: ");
		ar.add("gpaValue=" + String.format("%6.4f", gpas[0]));
		ar.add("cgpaLabel=CGPA: ");
		ar.add("cgpaValue=" + String.format("%6.4f", gpas[1]));
		//ar.add("prsigLabel=Examinations In-charge");
		//ar.add("prsigFile=" + ed.prfName);
		ar.add("cesigLabel=Controller of Examinations");
		ar.add("cesigFile=" + ed.cefName);
		ar.add("dateLabel=Date: ");
		ar.add("dateValue=" + ed.ddr);
		ar.add("footer1=Note: SGPA = Semester Grade Point Average");
		ar.add("footer2=         CGPA = Cumulative Grade Point Average");
		ar.add("footer3=See overleaf for information");
		String x[] = new String[ar.size()];
		return ar.toArray(x);
	}
	
	/**
	 * Adds a grade sheet to the PDF Document
	 * @param nr Nroll object filled with one student's data
	 * @return True if successful false otherwise
	 */
	public boolean addGradeSheet(Nroll nr, int type)
	{
		float x = 0.65f;
		float y = 1.5f;
		//offsetX = 0f;
		//offsetY = 0f;
		
		// Marks Table
		String mksTbl[][] = getMarksTableData(nr, type);
		int subcnt = getSubCount(mksTbl);
		if(type == ESSENTIAL && (mksTbl == null || subcnt != 1))
		{
			return false;
		}
		
		// Student and Exam Data Table
		String[] dd = createDataArray(nr, type);
		PdfPTable pth = MarksTable.getDataTable(dwidths, dtblWidth, dd);
		putTable(pcb, pth, x, y);
		
		// Get Marks Table (header table and marks data table)
		// Marks Table Header
		y += (0.25f + pth.getTotalHeight() / 72f);
		PdfPTable pt1 = MarksTable.getMarksTableHeader(mwidths, mtblWidth, mksTblHdr);
		putTable(pcb, pt1, x, y);

		// Marks Table
		//String mksTbl[][] = getMarksTableData(nr, type);
		PdfPTable pt2 = MarksTable.getMarksTable(mwidths, mtblWidth, "", "", mksTbl, 15);
		y += 0.03f + (pt1.getTotalHeight() / 72f);
		putTable(pcb, pt2, x, y);
		
		// Footer table
		dd = getFooterArray(nr);
		PdfPTable ptf = MarksTable.getFooterTable(fwidths, 7.25f, dd, type);
		y += (0.15f + pt2.getTotalHeight() / 72f);
		putTable(pcb, ptf, x, y);
		try
		{
			doc.newPage();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	private int getSubCount(String[][] mksTbl) {
		int cnt =0;
		if(mksTbl==null)
		{
			return 0;
		}
		for(int i=0;i<mksTbl.length; i++)
		{
			if(mksTbl[i][0] != null)
			{
				cnt++;
			}
		}
		return cnt;
	}

	public int parseRoman(String s)
	{
		s = s.trim().toUpperCase();
		int res = 0;
		if(s.equals("I"))
		{
			res = 1;
		}
		else if(s.equals("II"))
		{
			res = 2;
		}
		else if(s.equals("III"))
		{
			res = 3;
		}
		else if(s.equals("IV"))
		{
			res = 4;
		}
		else if(s.equals("V"))
		{
			res = 5;
		}
		else if(s.equals("VI"))
		{
			res = 6;
		}
		else if(s.equals("VII"))
		{
			res = 7;
		}
		else if(s.equals("IX"))
		{
			res = 9;
		}
		else if(s.equals("VIII"))
		{
			res = 8;
		}
		else if(s.equals("X"))
		{
			res = 10;
		}
		return res;
	}
	
	/**
	 * Main method
	 * @param args command line arguments
	 */
	public static void main(String[] args)
	{
		GradeSheetGenerator gsg = new GradeSheetGenerator();
		int cnt =0;
		boolean res;
		// 1. Initialize Globals and Database
		G.initialize();
		// 2. Get File names and String constants
		gsg.ed = new ExamData();
		gsg.ed.getExamDetails();
		if(!gsg.ed.ok)
		{
			Utilities.showMessage(G.jfParent, "Can not get Exam Data Files..");
			return;
		}
		// 3. Load NR from NR File
		gsg.nrs = new Nrolls();
		gsg.nrs.loadNrolls(gsg.ed.nrfName);
		
		// 4. Load subjects from subjects file
		gsg.subs = new Subjects();
		gsg.subs.loadSubjectsFromFile(gsg.ed.subfName);
		
		// Load Courses and Branches
		gsg.crs = new Courses();
		gsg.crs.loadCourses();
		
		// Load branches
		gsg.brs = new Branches();
		gsg.brs.loadBranches(gsg.ed.courseCode);
		
		// 5. Open PDF File
		gsg.doc = new Document(PageSize.A4, 0, 0, 0, 0); // Create PDF document
		try
		{
			gsg.pw = PdfWriter.getInstance(gsg.doc, new FileOutputStream(gsg.ed.outfName));
			gsg.doc.open(); // Open Document
			gsg.pcb = gsg.pw.getDirectContent();
		}
		catch (Exception e)
		{
			Utilities.showMessage(G.jfParent, "Can not open PDF file for writing..");
			return;
		}
		
		// 6. Open Marks file and for each student add grade sheet to Document
		Nroll nr = null;
		
		// Clear all registrations from each NRoll
		for(int i = 0; i < gsg.nrs.getNrollCount(); i++)
		{
			nr = gsg.nrs.getNroll(i);
			nr.mks.clear();
		}
		// Load Marks from file into corresponding NR all at once
		gsg.loadMarks();
		
		for(int i = 0; i < gsg.nrs.getNrollCount(); i++)
		{
			nr = gsg.nrs.getNroll(i);
			/// Debug code
			//if(!(nr.rollno).equalsIgnoreCase("15491A0104"))
			//{
			//	continue;
			//}
			// End of debug code
			
			// Add Record to Document
			res = gsg.addGradeSheet(nr, GradeSheetGenerator.GRADESHEET);
			if(!res)
			{
				G.out.println("Can not add " + nr.rollno);
				continue;
			}
			cnt++;
			//G.out.println(" " +cnt+": " + nr.rollno);
		}
		// Close Document
		try
		{
			gsg.pw.close();
			gsg.doc.close();
		}
		catch (Exception e)
		{
		}
		
		G.out.println("Gradesheet PDF Document created (" + gsg.ed.outfName + ") ...");
		//////////////// Creating Essential Grade sheet
		String essDocName = gsg.ed.outfName.substring(0,gsg.ed.outfName.length()-4)+"-essential.pdf";
		gsg.doc = new Document(PageSize.A4, 0,0,0,0);
		try
		{
			gsg.pw = PdfWriter.getInstance(gsg.doc, new FileOutputStream(essDocName));
			gsg.doc.open(); // Open Document
			gsg.pcb = gsg.pw.getDirectContent();
		}
		catch (Exception e)
		{
			G.out.println("Can not open PDF file for writing..");
			return;
		}
		
		// 6. Open Marks file and for each student add grade sheet to Document
		nr = null;
		cnt = 0;
		for(int i = 0; i < gsg.nrs.getNrollCount(); i++)
		{
			nr = gsg.nrs.getNroll(i);
			
			// Add Record to Document
			res = gsg.addGradeSheet(nr, GradeSheetGenerator.ESSENTIAL);
			if(!res)
			{
				//G.out.println("Skipping Gradesheet for: " + nr.rollno);
				continue;
			}
			cnt++;
			//G.out.println(" " +cnt+": " + nr.rollno);
		}
		
		// Extra credits sheet generator
		
		
		// Close Document
		try
		{
			gsg.pw.close();
			gsg.doc.close();
		}
		catch (Exception e)
		{
		}
		
		G.out.println("Essentials Document created with " + cnt + " records...");
	}
	

}
