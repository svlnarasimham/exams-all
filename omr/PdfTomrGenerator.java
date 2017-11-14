package omr;

import java.awt.Color;
import java.io.*;
import java.util.Random;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import common.G;
import common.PdfLibrary;

public class PdfTomrGenerator extends PdfLibrary
{
	// Marks Required 
	float bw = 0f; // border width
	Font f1, f2;
	TomrCoordinates cc;
	float mf = TomrCoordinates.CM2PT; // multiplyFactor
	
	public PdfTomrGenerator()
	{
		cc = new TomrCoordinates();
		cc.loadCoordinates();
		f1 = new Font(Font.TIMES_ROMAN,14,Font.BOLD);
		f2 = new Font(Font.TIMES_ROMAN, 8, Font.BOLD);
	}
	
	/**
	 * Generates one Answer book Cover Page 
	 * @param pcb PDF Content Byte object
	 * @param slno Serial Number of the booklet
	 * @param barcode Bar code Number assigned to this book
	 * @param htno Hall Ticket number
	 * @param name Name of the candidate
	 * @param exam Examination Name
	 * @param mandy Month and Year of Exam
	 * @param branch Branch of the Candidate
	 * @param code Subject Code
	 * @param name Subject Name
	 * @param doe Date of Examination
	 * @param cesig Controller's Signature Image
	 * @return True if page is added successfully or false otherwise
	 */
	public boolean addPage(
			PdfContentByte pcb, 
			int slno,
			int barcode,
			String htno,
			String name,
			String exam,
			String mandy,
			String branch,
			String scode,
			String sname,
			String doe,
			Image cesig)
	{
		boolean res = true;
		try
		{
			// Serial Number
			putNumber(pcb, slno, 7, cc.slnoX, cc.slnoY, f1);
			putImage(pcb, cesig, cc.cesigX, cc.cesigY, cc.cesigW, cc.cesigH, 0);
			// Get Barcode Image
			Image barcodeImage = BarcodeGenerator.getBarcode(
					pcb, 
					""+barcode, 
					false, 
					cc.barcode1W, 
					cc.barcode1H);
			
			// Get Photo Image
			String photoFile = G.basePath+G.pathSep+G.photosFolder+G.pathSep+htno.toUpperCase()+".jpg";
			File f = new File(photoFile);
			if(!f.exists())
			{
				photoFile = G.basePath+G.pathSep+G.defaultsFolder+G.pathSep+G.defaultPhotoFile;
			}
			Image photoImage = Image.getInstance(photoFile);
			photoImage.scaleToFit(cc.photoW*mf, cc.photoH*mf);
			photoImage.setBorderWidth(0);
			
			// Put barcode 1
			putImage(pcb, barcodeImage, cc.barcode1X, cc.barcode1Y, cc.barcode1W, cc.barcode1H);
			putImage(pcb, cesig, cc.cesigX, cc.cesigY, cc.cesigW, cc.cesigH);// CE Signature
			putImage(pcb, photoImage, cc.photoX,cc.photoY, cc.photoW, cc.photoH);
			
			putText(pcb, htno, cc.htnoX, cc.htnoY,f2);// HTNO
			putText(pcb, name, cc.htnoX, cc.nameY, f2);// Candidate name
			putText(pcb, exam, cc.htnoX, cc.exam1Y, 0f, f2); // Exam Name
			putText(pcb, mandy, cc.htnoX, cc.mandy1Y, 0f, f2); // Month and year of exam
			putText(pcb, branch, cc.htnoX, cc.branch1Y, 0f, f2);// Branch name
			putText(pcb, scode, cc.htnoX, cc.scode1Y, 0f, f2);// Subject code
			putText(pcb, sname, cc.htnoX, cc.sname1Y, 0f, f2);// Subject name
			putText(pcb, doe, cc.htnoX, cc.doe1Y, 0f, f2); // date of exam
			
			putImage(pcb, barcodeImage, cc.barcode1X, cc.barcode2Y, cc.barcode2W, cc.barcode2H);
			putText(pcb, exam, cc.exam2X, cc.exam2Y, 0f, f2); // Exam Name
			putText(pcb, branch, cc.exam2X, cc.branch2Y, 0f, f2);// Branch name
			putText(pcb, scode, cc.exam2X, cc.scode2Y, 0f, f2);// Subject code
			putText(pcb, sname, cc.exam2X, cc.sname2Y, 0f, f2);// Subject name

			putImage(pcb, barcodeImage, cc.barcode1X, cc.barcode3Y, cc.barcode3W, cc.barcode3H);
			putText(pcb, exam, cc.exam2X, cc.exam3Y, 0f, f2); // Exam Name
			putText(pcb, branch, cc.exam2X, cc.branch3Y, 0f, f2);// Branch name
			putText(pcb, scode, cc.exam2X, cc.scode3Y, 0f, f2);// Subject code
			putText(pcb, sname, cc.exam2X, cc.sname3Y, 0f, f2);// Subject name
//			putText(pcb, " ", cc.exam2X, cc.sname3Y+1.0f, f2); // dummy end statement
			
         putImage(pcb, barcodeImage, cc.barcode4X, cc.barcode4Y, cc.barcode4W, cc.barcode4H,90f);
			putText(pcb, "Exam: "     + exam,  cc.exam4X,  cc.exam4Y,  90f, f2); // Exam Name
			putText(pcb, "Sub.Code: " + scode, cc.scode4X, cc.scode4Y, 90f, f2);// Subject code
			putText(pcb, "Sub.Name: " + sname, cc.sname4X, cc.sname4Y, 90f, f2);// Subject name
			
			// Perforation Marks
			if(G.rnd == null)
			{
				G.rnd = new Random();
			}
			// Draw vertical line across perforations for matching
			// Line is randomly drawn between 4 and 19cm with a resolution of 0.25cm
			float x = 4 + (((float)G.rnd.nextInt(60))/4.0f);
			pcb.saveState();
			float llen = 0.5f;
			pcb.setLineWidth(1f);
			pcb.setColorStroke(Color.yellow);
			pcb.moveTo(x*mf, (cc.pageHeight - (cc.perf1Y-llen))*mf);
			pcb.lineTo(x*mf, (cc.pageHeight - (cc.perf1Y+llen))*mf);
			pcb.moveTo(x*mf, (cc.pageHeight - (cc.perf2Y-llen))*mf);
			pcb.lineTo(x*mf, (cc.pageHeight - (cc.perf2Y+llen))*mf);
			pcb.stroke();
			pcb.restoreState();
			//putText(pcb, "||", x, cc.perf1Y+0.2f, f1);
			//putText(pcb, "||", x, cc.perf2Y+0.2f, f1);
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
			writer = PdfWriter.getInstance(document, new FileOutputStream("coverpage.pdf"));
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (DocumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      // step 3
      document.open();
      // step 4
      PdfContentByte cb = writer.getDirectContent();
      PdfTomrGenerator pcg = new PdfTomrGenerator();
		Image img = null;
		try
		{
			img = Image.getInstance("/home/qis/workspace/qis/src/qis-ce-signature.jpg");// Photo
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
      pcg.addPage(
      		cb, 
      		1234567, 
      		98482, 
      		"15491A0110", 
      		"SALAGRAMA VEERA VENKATA SATYANARAYANA RAO", 
      		"I YEAR I SEMESTER REGULAR EXAMINATION", 
      		"NOVEMBER 2015 R15", 
      		"ELECTRONICS AND COMMUNICATIONS ENGINEERING", 
      		"A0001", 
      		"DATA STRUCTURES THROUGH C PROGRAMMING", 
      		"18-11-2015", 
      		img);
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
