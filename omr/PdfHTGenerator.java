package omr;
import java.io.*;
import javax.swing.JOptionPane;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import common.G;


public class PdfHTGenerator
{
	Font fnt1 = null;
	Font fnt2 = null;
	Font fnt3 = null;
	Font fnt4 = null;
	Font fnt5 = null;
	//Font fnt6 = null;
	//Font fnt7 = null;
	//Font fnt8 = null;
	
	// Constructor
	public PdfHTGenerator()
	{
		try
		{
			String bf1 = BaseFont.TIMES_BOLD;
			String bf2 = BaseFont.TIMES_ROMAN;
			String enc = BaseFont.WINANSI;
			boolean embed = BaseFont.NOT_EMBEDDED;
			fnt1 = new Font(BaseFont.createFont(bf1, enc, embed), 10,Font.BOLD);
			fnt2 = new Font(BaseFont.createFont(bf2, enc, embed), 10, Font.BOLD);
			fnt3 = new Font(BaseFont.createFont(bf2, enc, embed), 10, Font.BOLD);
			fnt4 = new Font(BaseFont.createFont(bf2, enc, embed), 10, Font.BOLD|Font.ITALIC);
			fnt5 = new Font(BaseFont.createFont(bf2, enc, embed), 10, Font.ITALIC);
			//fnt6 = new Font(BaseFont.createFont(bf2, enc, embed), 9,Font.BOLD);
			//fnt7 = new Font(BaseFont.createFont(bf2, enc, embed), 8, Font.BOLD);
			//fnt8 = new Font(BaseFont.createFont(bf2, enc, embed), 6, Font.BOLD);
		}
		catch(Exception e)
		{
			G.println(e.getMessage());
		}
	}
	
	/**
	 * Generates Header Table
	 * @param logoImage Logo Image object
	 * @param photoFile Student's photo file with full path
	 * @param examTitle Examination Name (Ex: I Year II Semester REgular)
	 * @param examMandY Examination Month and Year (Ex: June 2015)
	 * @param crsName Course Name (Ex: Bachelar of Technology)
	 * @return Pdf Table  
	 * @throws Exception
	 */
	public PdfPTable getHeaderTable(
			Image logoImage, 
			String photoFile,
			String examTitle,
			String examMandY,
			String crsName) throws Exception
	{
		PdfPTable ptbl = null;
		//fnt1.setColor(255, 0, 0);
      Chunk newLine = new Chunk ("\n");
      
      float widths[] = new float[] {1.0f, 4.0f, 1.0f};
      
      ptbl = new PdfPTable(widths); 
      ptbl.setWidthPercentage(100);
      
      ptbl.getDefaultCell().setBorderWidth(0);
      ptbl.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
 
     // ptbl.getDefaultCell().setBorderWidthBottom(3);
      
      ptbl.setSpacingAfter(10f); 
      
      File f = new File(photoFile);
      if(!f.exists())
      {
    	  G.println("File " + photoFile + "not found ...");
    	  photoFile = G.basePath+G.pathSep+G.defaultsFolder+G.pathSep+"default-photo.jpg";
      }
      Image image2 = Image.getInstance(photoFile);
      image2.scaleToFit(75, 75);
      image2.setBorderWidth(0);
      
      Paragraph p1 = new Paragraph();
      p1.setAlignment(Paragraph.ALIGN_CENTER);
      Chunk c = new Chunk(G.instituteName, fnt2);
      p1.add(c);
      p1.add(newLine);
      p1.add(new Chunk(G.instituteAddress, fnt3));
      p1.add(newLine);
      p1.add(newLine);
      p1.add(new Chunk("HALL TICKET", fnt2));
      p1.add(newLine);
      p1.add(new Chunk(examTitle, fnt2));
      p1.add(newLine);
      p1.add(new Chunk(examMandY, fnt2));
      p1.add(newLine);
      p1.add(new Chunk(crsName, fnt2));
      p1.add(newLine);
      p1.add(newLine);
      
      PdfPCell cell1 = new PdfPCell(logoImage, false);
      cell1.setVerticalAlignment(PdfPCell.ALIGN_TOP);
      cell1.setBorder(0);
      cell1.setBorderWidthBottom(1);
      
      PdfPCell cell2 = new PdfPCell(p1);
      cell2.setBorder(0);
      cell2.setBorderWidthBottom(1);
      cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      cell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      
      PdfPCell cell3 = new PdfPCell(image2, false);
      cell3.setBorder(0);
      cell3.setBorderWidthBottom(1);
      cell3.setVerticalAlignment(PdfPCell.ALIGN_TOP);
      
      ptbl.addCell(cell1);
      ptbl.addCell(cell2);
      ptbl.addCell(cell3);
      
      return ptbl;
	}
	
	/**
	 * Generates Student Details table
	 * @param htno Roll No of the candidate
	 * @param name Name of the candidate
	 * @param brn Branch name
	 * @return Generated Pdf Table
	 * @throws Exception
	 */
	public PdfPTable getGeneralsDetailsTable(String htno, String name, String brn) throws Exception
	{
		PdfPTable ptbl = null;
      
      float widths[] = new float[] {1.0f};
	      
      ptbl = new PdfPTable(widths); 
      ptbl.setWidthPercentage(100);
      
      ptbl.getDefaultCell().setBorderWidth(0);
      ptbl.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
 
      ptbl.setSpacingAfter(10f); 
      
      Paragraph p;
      Chunk c;
      
      // Roll number
      p = new Paragraph();
      p.setAlignment(Paragraph.ALIGN_LEFT);
      c = new Chunk("Hall Ticket No: ", fnt3);
      p.add(c);
      c = new Chunk(String.format("%-30s", htno), fnt4); 
      p.add(c);
      ptbl.addCell(p);
	      
      // Name
      p = new Paragraph();
      c = new Chunk("Name: ", fnt3);
      p.add(c);
      c = new Chunk(name, fnt4); 
      p.add(c);
      ptbl.addCell(p);
      
      // Branch
      p = new Paragraph();
      p.setAlignment(Paragraph.ALIGN_LEFT);
      c = new Chunk("Branch:  ", fnt3);
      p.add(c);
      c = new Chunk(brn, fnt4); 
      p.add(c);
      ptbl.addCell(p);
      return ptbl;
	}
		
	public PdfPTable getSubjectsTable(String subNames[], int nleft, int nright) throws Exception
	{
		int maxrows = 8;
		String arr[] = reorder(subNames, nleft, nright, maxrows);
		
		PdfPTable ptbl = null;
      // Create table
      float widths[] = new float[] {0.4f, 3.0f, 0.4f, 3.0f};
      ptbl = new PdfPTable(widths); 
      ptbl.setWidthPercentage(100);
      
      ptbl.getDefaultCell().setBorderWidth(1);
      ptbl.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
      
      ptbl.setSpacingAfter(10f); 
	      
      Paragraph p;
      Chunk c, c1;
      PdfPCell tCell;
      int i;
	   
      // Table header (2 columns hence, 2 times the same header
      tCell = new PdfPCell(new Paragraph(new Chunk("Sl\nNo", fnt1)));
      tCell.setBorder(PdfPCell.BOX);
      ptbl.addCell(tCell);
	      
      tCell = new PdfPCell(new Paragraph(new Chunk("Subject (Theory)", fnt1)));
      tCell.setBorder(PdfPCell.BOX);
      ptbl.addCell(tCell);
	      
      tCell = new PdfPCell(new Paragraph(new Chunk("Sl\nNo", fnt1)));
      tCell.setBorder(PdfPCell.BOX);
      ptbl.addCell(tCell);
	   
      tCell = new PdfPCell(new Paragraph(new Chunk("Subject (Practical)", fnt1)));
      tCell.setBorder(PdfPCell.BOX);
      ptbl.addCell(tCell);
      int lc=0, rc=0, slno=0; // left and right counts
      // Print subjects
      boolean dataOk = false;
      int sz = maxrows*2;
      for(i=0;i<sz;i++) // for each subject
      {
      	p = new Paragraph();
      	p.setAlignment(Paragraph.ALIGN_LEFT);
      	dataOk = (arr[i].trim().equals(""))?false:true;
      	if(i%2 == 0) // left column 
      	{ 
      		slno = lc;
      		lc++;
      	}
      	else
      	{
      		slno = rc;
      		rc++;
      	}
      	if(dataOk) // subject exists, hence, print subject name
      	{
	      	c = new Chunk(String.format("%2d", (slno+1)), fnt3);
	      	c1= new Chunk(arr[i], fnt5);
      	}
      	else // subjects exhausted, hence print blank 
      	{
      		c = new Chunk(String.format("%2s", " "), fnt3);
      		c1 = new Chunk(" ", fnt5);
      	}
      	p.add(c);
      	tCell = new PdfPCell(p);
      	tCell.setBorder(PdfPCell.LEFT|PdfPCell.RIGHT); // set left and right borders
      	if(i >=sz-2) // set bottom border also for the last row 
      	{
      		tCell.setBorder(PdfPCell.LEFT|PdfPCell.RIGHT|PdfPCell.BOTTOM);
      	}
      	ptbl.addCell(tCell);

      	p = new Paragraph();
      	p.setAlignment(Paragraph.ALIGN_LEFT);
      	p.add(c1);
      	tCell = new PdfPCell(p);
      	tCell.setBorder(PdfPCell.LEFT|PdfPCell.RIGHT);
      	if(i >=sz-2)
      	{
      		tCell.setBorder(PdfPCell.LEFT|PdfPCell.RIGHT|PdfPCell.BOTTOM);
      	}
     	ptbl.addCell(tCell);
      }
      return ptbl;
	}
	
	/**
	 * Returns Footer table which contains ce signature
	 * @param ceSigImage Controller of Examination's Signature image object
	 * @param info1 Any information that should be printed
	 * @param info2 Any information that should be printed
	 * @return Footer pdf table
	 * @throws Exception
	 */
	public PdfPTable getFooterTable(Image ceSigImage, String info1, String info2) throws Exception
	{
		PdfPTable ptbl = null;
		int lineWidth = 0;
		int border = PdfPCell.NO_BORDER;
      
		// Create table with 3 columns
      float widths[] = new float[] {0.5f, 0.25f, 0.25f};
      ptbl = new PdfPTable(widths); 
      ptbl.setWidthPercentage(100);
      
      ptbl.getDefaultCell().setBorderWidth(lineWidth);
      ptbl.getDefaultCell().setBorder(border);
      
      ptbl.setSpacingAfter(10f); 
      
      // CE Image
      PdfPCell cell1 = new PdfPCell(ceSigImage, false); // ce signature, don't auto scale
      cell1.setVerticalAlignment(PdfPCell.ALIGN_TOP);
      cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      cell1.setBorder(border);
      ptbl.addCell(cell1);
      
      // Information 1 
      PdfPCell cell2 = new PdfPCell(new Paragraph(info1));
      cell2.setBorder(border);
      cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      cell2.setVerticalAlignment(PdfPCell.ALIGN_TOP);
      ptbl.addCell(cell2);
      
      // Another Dummy field, which may be used for any information
      // (If principal's signature is required, it may be added here)
      PdfPCell cell3 = new PdfPCell(new Paragraph(info2));
      cell3.setBorder(border);
      cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      cell3.setVerticalAlignment(PdfPCell.ALIGN_TOP);
      ptbl.addCell(cell3);
      
      ptbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      ptbl.addCell(new Paragraph("Controller of Examinations"));
      //ptbl.addCell(new Paragraph("  "));
      
      // Add Extra Lines
      //line 1
      ptbl.addCell(new Paragraph(" "));
      ptbl.addCell(new Paragraph(" "));
      ptbl.addCell(new Paragraph(" "));
      // line 2
      ptbl.addCell(new Paragraph(" "));
      ptbl.addCell(new Paragraph(" "));
      ptbl.addCell(new Paragraph(" "));
      return ptbl;
	}
		
	public PdfPTable getRulerTable() throws Exception
	{
		PdfPTable ptbl = null;
		
      float widths[] = new float[] {1.0f};
      ptbl = new PdfPTable(widths); 
      ptbl.setWidthPercentage(100);
      
      ptbl.getDefaultCell().setBorderWidth(0);
      ptbl.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
      
      ptbl.setSpacingAfter(10f); 
      
      Image img = Image.getInstance(G.basePath+G.pathSep+G.defaultsFolder+G.pathSep+G.defaultRulerFile);
      PdfPCell pc = new PdfPCell(img, true); // true indicates auto scale
      pc.setBorder(PdfPCell.NO_BORDER);
      ptbl.addCell(pc);
      ptbl.addCell(new Paragraph("\n"));
      return ptbl;
	}
	
	public String[] reorder(String arr[], int nleft, int nright, int nrows)
	{
		int sz = nrows*2;
		String tarr[] = new String[sz];
		int i,j, e;
		e = nleft;
		j=0;
		for(i=0;i<sz;i+=2) // fill left values 
		{
			tarr[i] = (j<e)?arr[j]:" ";
			j++;
		}
		e = nright;
		j=0;
		for(i=1;i<sz;i+=2)
		{
			tarr[i] = (j<e) ? arr[nleft+j]:" ";
			j++;
		}
		return tarr;
	}
	
	/**
	 * Checks if a file exists and exits if file not found
	 * @param fileName File Name
	 */
	public static boolean checkFile(String fileName)
	{
		File f = new File(fileName);
		if(!f.exists())
		{
			JOptionPane.showMessageDialog(null, "Can not find file: " + fileName);
			return false;
			//System.exit(1);
		}
		return true;
	}
	
	public static void main(String[] args)
	{
		PdfHTGenerator pht = new PdfHTGenerator();
		String arr[] = new String[] {"1","2","3","4", "5", "6", "7", "8", "9"};
		String ta [] = pht.reorder(arr, 6, 3, 9);
		for(int i=0;i<18; i++)
		{
			System.out.println("ta["+i+"] = '" + ta[i]);
		}
	}
}
