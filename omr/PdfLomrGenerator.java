package omr;
import java.io.FileOutputStream;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import common.PdfLibrary;

public class PdfLomrGenerator extends PdfLibrary
{
	// Marks Required 
	float bw = 0f; // border width
	Font f1, f2, fbig;
	LomrCoordinates cc;
	float mf = LomrCoordinates.CM2PT; // multiplyFactor
	
	public PdfLomrGenerator()
	{
		cc = new LomrCoordinates();
		cc.loadCoordinates();
		f1 = new Font(Font.TIMES_ROMAN,8,Font.NORMAL);
		f2 = new Font(Font.TIMES_ROMAN, 8, Font.BOLD);
		fbig= new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
	}
	
	/**
	 * Generates one Lab Cover Page 
	 * @param pcb PDF Content Byte object
	 * @param barcode Barcode Number to be printed 
	 * @param course Course Abbreviated name (B.Tech., M.Tech. etc.)
	 * @param exam Examination Name
	 * @param mandy Month and Year of Exam
	 * @param branch Abbreviated Branch of the Candidate
	 * @param doe Date of Examination
	 * @param scode Subject Code
	 * @param sname Subject Name
	 * @param htno [] Array of up to 20 Hall Ticket Numbers to be printed
	 * @return True if page is added successfully or false otherwise
	 */
	public boolean addPage(
			PdfContentByte pcb, 
			int barcode,
			String course,
			String exam,
			String mandy,
			String branch,
			String doe,
			String scode,
			String sname,
			String htno[])
	{
		boolean res = true;
		int sz = Math.min(20, htno.length);
		try
		{
			
			// Serial Number (left side around bottom)
			putNumber(pcb, barcode, 5, cc.slnoX, cc.slnoY, fbig);
			
			// Left side 
			putText(pcb, course, cc.course1X, cc.course1Y,f1);// Course
			putText(pcb, exam, cc.course1X, cc.exam1Y, 0f, f1); // Exam Name
			putText(pcb, mandy, cc.course1X, cc.mandy1Y, 0f, f1); // Month and year of exam
			putText(pcb, branch, cc.course1X, cc.branch1Y, 0f, f1);// Branch name
			putText(pcb, doe, cc.course1X, cc.doe1Y, 0f, f1); // date of exam
			putText(pcb, scode, cc.course1X, cc.scode1Y, 0f, f1);// Subject code
			
			// Right side
			putText(pcb, course, cc.course2X, cc.course2Y, 0f, f2);// Course
			putText(pcb, exam, cc.course2X, cc.exam2Y, 0f, f2); // Exam Name
			putText(pcb, mandy, cc.course2X, cc.mandy2Y, 0f, f2); // Month and year of exam
			putText(pcb, branch, cc.course2X, cc.branch2Y, 0f, f2);// Branch name
			putText(pcb, doe, cc.course2X, cc.doe2Y, 0f, f2); // date of exam
			putText(pcb, scode+": "+sname, cc.course2X, cc.scode2Y, 0f, f2);// Subject code and name

			// Add Left HTNOs
			for(int i=0; i<sz; i++)
			{
				float y = 0.0f;
				int yy = i/5;
				switch(yy)
				{
					case 0: y = cc.leftHT1Y;break;
					case 1: y = cc.leftHT2Y;break;
					case 2: y = cc.leftHT3Y;break;
					case 3: y = cc.leftHT4Y;break;
				}
				putText(pcb, htno[i], cc.leftHT1X, y+(i%5)*cc.vspace, 0f, f1);
			}
			
			// Add Right HTNOs
			for(int i=0; i<sz; i++)
			{
				float x = cc.rightHT1X + (i%5) * cc.hspace;
				float y = 0.0f;
				int yy = i/5;
				switch(yy)
				{
					case 0: y = cc.rightHT1Y;break;
					case 1: y = cc.rightHT2Y;break;
					case 2: y = cc.rightHT3Y;break;
					case 3: y = cc.rightHT4Y;break;
				}
				putText(pcb, htno[i], x, y, 0f, f2);
			}
			
			// Get Barcode Image
			Image barcodeImage = BarcodeGenerator.getBarcode(
					pcb, 
					""+barcode, 
					true, 
					cc.barcode1W, 
					cc.barcode1H);
			
			// Add barcode 
			putImage(pcb, barcodeImage, cc.barcode1X, cc.barcode1Y, cc.barcode1W, cc.barcode1H);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	
	public static void main(String[] args)
	{
      // step 1
      Document document = new Document(PageSize.A4);
      // step 2
      PdfWriter writer=null;
		try
		{
			writer = PdfWriter.getInstance(document, new FileOutputStream("lomrpage.pdf"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
      // step 3
      document.open();
      // step 4
      PdfContentByte cb = writer.getDirectContent();
      PdfLomrGenerator pcg = new PdfLomrGenerator();
      String htnos[] = {
      		"15491A0101", "15491A0102", "15491A0103", "15491A0104", "15491A0105", 
      		"15491A0106", "15491A0107", "15491A0108", "15491A0109", "15491A0110", 
      		"15491A0111", "15491A0112", "15491A0113", "15491A0114", "15491A0115", 
      		"15491A0116", "15491A0117", "15491A0118", "15491A0119", "15491A0120"
      };
      pcg.addPage(
      		cb, 
      		123456, 
      		"B.Tech.", 
      		"B.Tech. I Year I Sem Regular", 
      		"November 2015", 
      		"ECE", 
      		"18-11-2015", 
      		"A0001", 
      		"DATA  STRUCTURES THROUGH C PROGRAMMING LAB", 
      		htnos);
      try
		{
			document.newPage();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      // step 5
      document.close();
      System.out.println("Document created....");
	}

}
