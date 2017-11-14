package omr;
import java.io.*;
import java.util.Vector;
import utility.MyDate;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import common.G;
import common.SlnoBcHt;

public class TomrGenerator
{
	public static void main(String args[])
	{
		int i, j, n;
		//Courses courses = null;
		//Branches branches = null;
		//Subjects subjects = null;
		//String oldEcode = "";
		// Initialize Global Data
		G.initialize();
		// Initialize DB Object with driver, url, user and password
		G.dbcon.initConnection(G.dbDriver, G.dbUrl, G.dbUser, G.dbPassword);
		if(G.dbcon.getConnection() == null)
		{
			utility.Utilities.showMessage("Can not connect to Database..");
			System.exit(1);
		}
		
		// Get Dates from schedule
		String sql = "select distinct(sdate) from schedule order by slno;";
		Vector<String[]> rv = G.dbcon.executeSQL(sql);
		String dates[] = new String[rv.size()];
		for(i = 0; i< rv.size(); i++)
		{
			dates[i] = rv.get(i)[0];
			//System.out.println(dates[i]);
		}
		System.out.println("Dates Loaded: " + dates.length);
		// Load Courses
		//courses = new Courses();
		//int nrec = courses.loadCourses();
		//System.out.println("Courses loaded:" + nrec);
		//if(nrec <= 0)
		//{
		//	System.out.println("Can not load courses from database");
		//	System.exit(1);
		//}
		
		// Load Branches 
		//branches = new Branches();
		//nrec = branches.loadBranches();
		//if(nrec <= 0)
		//{
		//	System.out.println("Can not find Branches from database");
		//	System.exit(1);
		//}
		//System.out.println("Branches Loaded: " + nrec);
		
		String imageFile = G.basePath+G.pathSep+G.defaultsFolder+G.pathSep+G.cesigFile;
		Image ceImage = null;
		try
		{
			if(!new File(imageFile).exists())
			{
				imageFile = G.basePath+G.pathSep+G.defaultsFolder+G.pathSep+G.defaultCesigFile;
			}
			ceImage = Image.getInstance(imageFile);
			ceImage.setBorderWidth(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if(ceImage == null)
		{
			System.out.println("Can not load CE Signature File");
			System.exit(1);
		}
		
		/////////////////////////////////// For each Date
		for(i=0;i<dates.length; i++)
		{
			//if(i>0) continue; // debug code
			// load all students for that day with details required for cover page
			sql = "select distinct(b.srollno), a.scode, c.sname, d.bname, e.sname, f.ename, f.emy "
					+ "from schedule a, registration b, students c, branches d, subjects e, exams f "
					+ "where "
					+ "a.sdate = '"+dates[i]+"' and "
					+ "a.ecode = b.ecode and "
					+ "a.scode = b.scode and "
					+ "b.srollno = c.srollno and "
					+ "c.bcode = d.bcode and "
					+ "c.ccode = d.ccode and "
					+ "a.scode = e.scode and "
					+ "a.ecode = e.ecode and "
					+ "a.ecode = f.ecode and "
					+ "e.stype = 'T' "
					+ "order by c.bcode, c.srollno";
			
			rv = G.dbcon.executeSQL(sql);
			int sz = rv.size();
			if(sz == 0)
			{
				System.out.println("No records to process.. Date:" + dates[i]);
				continue;
			}
			// Create Subject vs Barcode vs HTNO object
			SlnoBcHt sbh = new SlnoBcHt(); 
			String sbhFileName = G.basePath+G.pathSep+G.barcodesFolder+G.pathSep+MyDate.getAnsiDate(dates[i])+".txt";
			n = sbh.loadSBH(sbhFileName); // Load values from text file.
			
			if(n < sz)
			{
				System.out.println("Can not load Slno Vs Barcodes from:" + sbhFileName);
				continue;
			}
			// Create Document File
			Document doc;

			// Open Document 
	   	doc = new Document(PageSize.A4);
	   	doc.setMargins(48, 48, 24, 0);// left, right, top and bottom
	   	String outputFile = G.basePath+G.pathSep+G.barcodesFolder+G.pathSep+MyDate.getAnsiDate(dates[i])+"-coverpage.pdf";
	   	PdfWriter pdfw=null;
	   	try
	   	{
	   		pdfw = PdfWriter.getInstance(doc, new FileOutputStream(outputFile));
	   	}
	   	catch(Exception e)
	   	{
	   		e.printStackTrace();
	   		System.exit(1);
	   	}
	   	doc.open();
	   	PdfContentByte pcb = pdfw.getDirectContent();
			
	   	PdfTomrGenerator pcg = new PdfTomrGenerator();
			//////////////////////// For each record, generate one cover page
			for(j=0;j<sz; j++)
			{
				String arr[] = rv.get(j);
				String ht = arr[0]; // roll no
				String sc = arr[1]; // sub code
				String stn = arr[2]; // student name
				String bn = arr[3]; // branch name
				String sbn = arr[4]; // subject name;
				String en = arr[5]; // exam name
				String emy= arr[6]; // Exam Month and year
				
				if(!sbh.addHtno(ht))
				{
					//System.out.println("Can not add HT: " + ht);
					//continue;
				}
				int slno = sbh.getSlno(ht);
				int barcode= sbh.getBarcode(ht);
				if(slno==0 || barcode == 0)
				{
					System.out.println("Can not find slno and barcode for: " + ht);
					continue;
				}
				pcg.addPage(pcb, slno, barcode, ht, stn, en, emy, bn, sc, sbn, dates[i], ceImage);
				try
				{
					doc.newPage();
				}
				catch (DocumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Date: " + dates[i] + " Complete...");
			doc.close();
			sbh.saveSBH(sbhFileName);
		}
      System.out.println("All Files Completed. Exiting program...");
 	}
}
