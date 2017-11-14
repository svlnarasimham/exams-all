package common;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import omr.PdfHTGenerator;
import utility.*;

public class HTGenerator
{
	public void domain()
	{
		G.setDebugLevel(0);
		// Initialize Global Data
		G.initialize();
		G.out.clear();// clear console
		// Select Exam Code
		G.ecode = new EcodeGetterDialog().getECode();
		if (G.ecode.equals(""))
		{
			return; // no Exam code selected, so quit
		}

		// Load Subjects
		Subjects subjects = new Subjects();
		int nrec = subjects.loadSubjectsFromDb(G.ecode);
		if (nrec <= 0)
		{
			Utilities.showMessage("Can not load Subjects from database");
			return;
		}
		if (G.debugLevel > 0)
		{
			Utilities.showMessage("Number of Subjects: " + nrec);
		}

		// Create PDF HT Generator Object
		PdfHTGenerator phtg = new PdfHTGenerator();
		// Fixed data part //////////////////
		// Logo Image
		String logoFile = G.basePath + G.pathSep + G.defaultsFolder + G.pathSep + G.logoFile;
		if(!PdfHTGenerator.checkFile(logoFile))
		{
			G.println("Can not find logo file at: " + logoFile);
			return;
		}
		Image logoImage = null;
		try
		{
			logoImage = Image.getInstance(logoFile);
			logoImage.scaleToFit(50, 50);
			logoImage.setBorderWidth(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		// Get CE Signature
		// Logo Image
		String cesigFile = G.basePath + G.pathSep + G.defaultsFolder + G.pathSep + G.cesigFile;
		File file = new File(cesigFile);
		if (!file.exists())
		{
			utility.Utilities.showMessage("Can not find file:" + cesigFile + "  Using default file...");
			cesigFile = G.basePath + G.pathSep + G.defaultsFolder + G.pathSep + G.defaultCesigFile;
		}

		PdfHTGenerator.checkFile(cesigFile);
		Image ceImage = null;
		try
		{
			ceImage = Image.getInstance(cesigFile);
			ceImage.scaleToFit(100, 50);// approx. 1.5 x 0.6 inches
			ceImage.setBorderWidth(0);
			logoImage.setAlignment(Image.MIDDLE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		// Exam Name and Month & Year of Exam
		String sql = "select emy, ename from exams where ecode = '" + G.ecode + "';";
		Vector<String[]> vr = G.dbcon.executeSQL(sql);
		if (vr.size() != 1)
		{
			utility.Utilities.showMessage("Can not load exam details for code:" + G.ecode);
			return;
		}
		String emy = vr.get(0)[0];
		String examTitle = vr.get(0)[1];

		// Output File (pdfFolder/G.ecode+"-ht.pdf")
		String outputFile = G.basePath + G.pathSep + G.pdfFolder + G.pathSep + G.ecode + "-ht.pdf";

		String photoFile;
		String dummy = " ";

		// Get Roll Numbers
		int cnt = 0;
		sql = "select distinct a.srollno, b.sname, b.ccode, b.bcode " + "from "
				+ "registration a, students b " + "where " + "a.srollno = b.srollno and ecode  = '"
				+ G.ecode + "' " + "order by b.bcode, a.srollno;";

		Vector<String[]> htVector = G.dbcon.executeSQL(sql);
		int sz = htVector.size();
		if (sz == 0)
		{
			utility.Utilities.showMessage("No records for this Exam: " + G.ecode);
			return;
		}

		Vector<String> sv = new Vector<String>();
		// For each Roll Number, get data and generate Hall Ticket
		String arr[];
		String rno = "", snm = "", cc = "", bc = "", crsName = "", brName = "", oldcc = "",
				oldbc = "";
		sql = "select scode from registration where srollno=? and ecode = ?;";
		PreparedStatement ps = null;
		try
		{
			ps = G.dbcon.getConnection().prepareStatement(sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		Document doc;

		// Open Document
		doc = new Document(PageSize.A4);
		doc.setMargins(48, 48, 24, 0);// left, right, top and bottom
		try
		{
			PdfWriter.getInstance(doc, new FileOutputStream(outputFile));
		}
		catch (Exception e)
		{
			G.println(e.getMessage());
			//e.printStackTrace();
			return;
			//G.exit(1);
		}
		doc.open();

		for (int i = 0; i < sz; i++)
		{
			// if(i>1) break; // debug code
			arr = htVector.get(i);
			rno = arr[0].trim().toUpperCase(); // Roll No
			snm = arr[1].trim().toUpperCase(); // Name
			cc = arr[2].trim().toUpperCase(); // Course Code
			bc = arr[3].trim().toUpperCase(); // Branch Code
			if (!oldcc.equals(cc)) // load course name only if necessary
			{
				Course c = G.courses.getCourse(cc);
				if (c != null)
				{
					crsName = c.cname;
				}
				else
				{
					utility.Utilities.showMessage("Can not find course: " + cc);
					crsName = "XXX";
				}
			}

			if (!oldbc.equals(bc)) // load branch name only if necessary
			{
				Branch b = G.branches.getBranch(cc, bc);
				if (b != null)
				{
					brName = b.bname;
				}
				else
				{
					utility.Utilities.showMessage("Can not find Branch: " + cc + ":" + bc);
					brName = "XXX";
				}
			}
			sv.removeAllElements(); // empty subject codes vector
			try
			{
				ps.setString(1, rno);
				ps.setString(2, G.ecode);
				ResultSet rs = ps.executeQuery();
				while (rs.next())
				{
					sv.add(rs.getString(1));
				}
				rs.close();
			}
			catch (Exception e)
			{
				G.println(e.getMessage());
				//e.printStackTrace();
				return;
			}
			String tscodes[] = new String[sv.size()];
			sv.toArray(tscodes);
			String scodes[] = subjects.sortSubCodes(cc, bc, tscodes);
			ArrayList<String> alTheory = new ArrayList<String>();
			ArrayList<String> alLab = new ArrayList<String>();
			for (int ti = 0; ti < scodes.length; ti++)
			{
				Subject tsub = subjects.getSubject(scodes[ti]);
				if (tsub == null)
				{
					Utilities.showMessage(G.jfParent, "Can not find subject for code: " + scodes[ti]);
					doc.close();
					return;
					//G.exit(1);
				}
				if (tsub.type.equals("T"))
				{
					alTheory.add(scodes[ti]);
				}
				else
				{
					alLab.add(scodes[ti]);
				}
			}
			int nthe = alTheory.size();
			int nlab = alLab.size();
			alTheory.addAll(alLab); // Append all lab records to alTheory
			String arrScodes[] = new String[alTheory.size()];
			alTheory.toArray(arrScodes);
			String sNames[] = subjects.getSubjectNames(cc, bc, arrScodes);

			photoFile = G.basePath + G.pathSep + G.photosFolder + G.pathSep + rno + ".jpg";
			// Everything loaded and ready for HT Generation
			try
			{
				PdfPTable pt1 = phtg.getHeaderTable(logoImage, photoFile, examTitle, emy, crsName);
				doc.add(pt1);

				PdfPTable pt2 = phtg.getGeneralsDetailsTable(rno, snm, brName);
				doc.add(pt2);

				PdfPTable pt3 = phtg.getSubjectsTable(sNames, nthe, nlab);
				doc.add(pt3);

				PdfPTable pt4 = phtg.getFooterTable(ceImage, dummy, dummy);
				doc.add(pt4);

				if (i % 2 == 0)
				{
					PdfPTable pt5 = phtg.getRulerTable();
					doc.add(pt5);
				}
				else
				{
					doc.newPage();
				}
			}
			catch (Exception e)
			{
				G.println(e.getMessage());
				//e.printStackTrace();
				return;
			}
			cnt++;
		}
		doc.close();
		Utilities.showMessage("File: " + outputFile + " Created with " + cnt + " records");
	}

	public static void main(String[] args)
	{
		G.initialize();
		G.courses.loadCourses();
		G.branches.loadBranches();
		HTGenerator htg = new HTGenerator();
		htg.domain();
	}
}
