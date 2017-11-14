package gradesheet;
import java.io.*;
import java.util.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class MarksTable
{
	public static boolean	halfWidthTable	= false;
	
	public static float		widths[]			= null;
	public static float		tblWidth			= 0;
	
	public static int			LEFT			= PdfPCell.ALIGN_LEFT;
	public static int			CENTER			= PdfPCell.ALIGN_CENTER;
	public static int			RIGHT			= PdfPCell.ALIGN_RIGHT;
	public static int			TOP				= PdfPCell.ALIGN_TOP;
	public static int			MIDDLE			= PdfPCell.ALIGN_MIDDLE;
	public static int			BOTTOM			= PdfPCell.ALIGN_BOTTOM;
	
	// slno, subcode, subtitle, grade, credits, grade points, result
	// public static float mwidths[] = {3f,6f,40f,3f,3f,3f,3f};
	// public static float widths2[] = {3f,6f,40f,3f,3f,3f,3f};
	
	// public static float mtblWidth = 7.25f * 72f; // For full width table
	// public static float tblWidth2 = 3.625f * 72f; // For half width table
	
	// public static float dataWidths[] = {1.0f, 1.0f, 4.25f, 1.0f};
	// public static float ftrWidths[] = {1.25f, 2.0f, 2.0f, 2.0f};
	
	/**
	 * Initializes Column widths and total table width
	 * 
	 * @param colWidths
	 *           Array of float values which represent the relative sizes of
	 *           columns
	 * @param tableWidth
	 *           Total table width in inches
	 */
	private static void init(float colWidths[], float tableWidth)
	{
		if(widths == null)
		{
			widths = colWidths;
			tblWidth = tableWidth * 72;
		}
	}
	
	/**
	 * Gets a paragraph with a given number of lines
	 * @param rows Number of rows required
	 * @param f Font to be used
	 * @return Paragraph with rows lines
	 */
	public static Paragraph getVerticalSpacePara(int rows, Font f)
	{
		String s = " ";
		for(int i = 0; i < rows; i++)
		{
			s += " \n";
					
		}
		Paragraph p = new Paragraph(s, f);
		p.setSpacingBefore(0);
		p.setSpacingAfter(0);
		p.setLeading(12f);
		p.setAlignment(Element.ALIGN_CENTER);
		return p;
	}
	
	/**
	 * Creates top data table with student details
	 * 
	 * @param colWidths
	 *           - Float array with relative table column widths<br>
	 *           This takes 2 columns where:<br>
	 *           First column is for Text data like (Name, Father Name, Roll No.
	 *           etc.)<br>
	 *           and the second column is for the photo
	 * @param mainTableWidth
	 *           Total table width in Inches
	 * @param keyValuePairs
	 *           The array containing key and values separated by equals sign(=)
	 *           <br>
	 *           The Keys can be from the following list:<br>
	 *           <UL>
	 *           <li>sheetTitle <i>(Grade Sheet | CMM | PC | ...) </i></li>
	 *           <li>sheetType <i>(Original | Duplicate | Transcript) </i></li>
	 *           <li>slnoLabel <i>(SLNO | MMNO | ...) </i></li>
	 *           <li>slnoValue <i>(Ex: A000001) </i></li>
	 *           <li>examTitle <i>(Ex: I Year I Semester (Regular)) </i></li>
	 *           <li>photoFile <i>(Ex: c:\exams\2016\D\16031D02501.jpg) </i></li>
	 *           <li>crsLabel <i>(Ex: Course/Semester) </i></li>
	 *           <li>crsValue <i>(Ex: M.Tech. I Semester) </i></li>
	 *           <li>brnLabel <i>(Ex: Branch) </i></li>
	 *           <li>brnValue <i>(Ex: Computer Science) </i></li>
	 *           <li>rlnoLabel <i>(Ex: RollNo) </i></li>
	 *           <li>rlnoValue <i>(Ex: 16031D02501) </i></li>
	 *           <li>nameLabel <i>(Ex: Name) </i></li>
	 *           <li>nameValue <i>(Ex: SVL Narasimham) </i></li>
	 *           <li>fnameLabel <i>(Ex: Father Name) </i></li>
	 *           <li>fnameValue <i>(Ex: SS Sarma) </i></li>
	 *           <li>mnameLabel <i>(Ex: Mother Name) </i></li>
	 *           <li>mnameValue <i>(Ex: S Hymavathi) </i></li>
	 *           <li>myLabel <i>(Ex: Month and Year of Exam); </i></li>
	 *           <li>myValue <i>(Ex: June, 2016) </i></li>
	 *           <li>defaultPhotoFile <i>(Ex: c:\exams\defaults\default-photo.jpg</i></li>
	 *           </UL>
	 * @return
	 */
	public static PdfPTable getDataTable(float colWidths[], float mainTableWidth, String keyValuePairs[])
	{
		PdfPTable mainTable;
		PdfPTable innerTable;
		PdfPCell pc;
		String tmp, tmp1;
		Hashtable<String, String> htData = new Hashtable<String, String>();
		
		// Initialize fonts if not available
		if(!MyFonts.initialized)
		{
			MyFonts.initFonts();
		}
		// Check if initialized now
		if(!MyFonts.initialized)
		{
			System.out.println("Can not continue without fonts...");
			return null;
		}
		// Initialize Hash table with key value pairs
		fillHashTable(htData, keyValuePairs);
		
		mainTable = new PdfPTable(colWidths);
		mainTable.setTotalWidth(mainTableWidth * 72); // convert total width to
																		// points
		mainTable.setSpacingBefore(3f);
		mainTable.setSpacingAfter(3f);
		
		// Row 1: Slno
		tmp = getValue(htData, "SlnoLabel", "   ");
		tmp1 = getValue(htData, "slnoValue", "   ");
		Phrase p = new Phrase();
		p.add(new Chunk(tmp + ": ", MyFonts.fontN12));
		p.add(new Chunk(tmp1, MyFonts.fontB12));
		pc = createTextCell(p, "", 0.1f, LEFT, MIDDLE);
		pc.setColspan(2);
		mainTable.addCell(pc);
		
		// Row 2: Sheet Heading (like Grade Sheet, CMM, PC ...)
		tmp = getValue(htData, "sheetTitle", "   ");
		if(tmp.trim().length() > 0) // Add row only if value is valid
		{
			pc = createTextCell(new Phrase(tmp, MyFonts.fontB14), "", 0f, CENTER, MIDDLE);
			pc.setColspan(2);
			pc.setPaddingBottom(10f);
			mainTable.addCell(pc);
		}
		
		// Row 3: Grade Sheet Type (Original, Duplicate, Transcript etc..)
		tmp = getValue(htData, "sheetType", "   ");
		// Add row only if value is valid
		if(tmp.trim().length() > 0 && !tmp.toLowerCase().startsWith("original"))
		{
			pc = createTextCell(new Phrase(tmp, MyFonts.fontB12), "", 0f, CENTER, MIDDLE);
			pc.setColspan(2);
			pc.setPaddingBottom(10f);
			mainTable.addCell(pc);
		}
		
		// CREATE INNER TABLE FOR DATA
		float w1, w2, w3;
		float innerTableWidth = mainTableWidth * colWidths[0] / (colWidths[0] + colWidths[1]);
		w1 = 1.0f; // 1 inch
		w2 = 0.5f; // 1 inch for extra first column space
		w3 = colWidths[0] - (w1 + w2);
		
		innerTable = new PdfPTable(new float[] { w1, w2, w3 });
		innerTable.setTotalWidth(innerTableWidth * 72);
		innerTable.setSpacingBefore(0);
		innerTable.setSpacingAfter(0);
		
		// Inner Table Row 1: Col 1,2 & 3 Exam Title (entire row)
		tmp = getValue(htData, "examTitle", "   ");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontB10), "", 0.1f, CENTER, MIDDLE);
		pc.setColspan(3);
		pc.setPaddingBottom(5); // Extra padding (default = 2)
		innerTable.addCell(pc);
		
		// Inner Table Row 2: col 1: Name
		tmp = getValue(htData, "RlnoLabel", "   ");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontN09), "", 0.1f, LEFT, MIDDLE);
		innerTable.addCell(pc);
		
		// Inner Table Row 2: col 2 & 3:
		tmp = getValue(htData, "RlnoValue", "   ");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontB10), "", 0.1f, LEFT, MIDDLE);
		pc.setColspan(2);
		innerTable.addCell(pc);
		
		// Inner Table Row 3: col 1: Name
		tmp = getValue(htData, "NameLabel", "   ");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontN09), "", 0.1f, LEFT, MIDDLE);
		innerTable.addCell(pc);
		
		// Inner Table Row 3: col 2 & 3:
		tmp = getValue(htData, "NameValue", "   ");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontB10), "", 0.1f, LEFT, MIDDLE);
		pc.setColspan(2);
		innerTable.addCell(pc);
		
		// Inner Table Row 4: col 1: Father Name
		// tmp = getValue(htData, "FNameLabel", " ");
		// pc = createTextCell(new Phrase(tmp, MyFonts.fontN09),"",0.1f, LEFT,
		// MIDDLE);
		// innerTable.addCell(pc);
		
		// Inner Table Row 4: col 2 & 3:
		// tmp = getValue(htData, "FNameValue", " ");
		// pc = createTextCell(new Phrase(tmp, MyFonts.fontB10),"",0.1f, LEFT,
		// MIDDLE);
		// pc.setColspan(2);
		// innerTable.addCell(pc);
		
		// Inner Table Row 5: col 1: Name
		// tmp = getValue(htData, "MNameLabel", " ");
		// pc = createTextCell(new Phrase(tmp, MyFonts.fontN09),"",0.1f, LEFT,
		// MIDDLE);
		// innerTable.addCell(pc);
		
		// Inner Table Row 5: col 2 & 3:
		// tmp = getValue(htData, "MNameValue", " ");
		// pc = createTextCell(new Phrase(tmp, MyFonts.fontB10),"",0.1f, LEFT,
		// MIDDLE);
		// pc.setColspan(2);
		// innerTable.addCell(pc);
		
		// Inner Table Row 6: col 1: Name
		//tmp = getValue(htData, "crsLabel", "   ");
		//pc = createTextCell(new Phrase(tmp, MyFonts.fontN09), "", 0.1f, LEFT, MIDDLE);
		//innerTable.addCell(pc);
		
		// Inner Table Row 6: col 2 & 3:
		//tmp = getValue(htData, "crsValue", "   ");
		//pc = createTextCell(new Phrase(tmp, MyFonts.fontB10), "", 0.1f, LEFT, MIDDLE);
		//pc.setColspan(2);
		//innerTable.addCell(pc);
		
		// Inner Table Row 7: col 1: Name
		tmp = getValue(htData, "brnLabel", "   ");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontN09), "", 0.1f, LEFT, MIDDLE);
		innerTable.addCell(pc);
		
		// Inner Table Row 7: col 2 & 3:
		tmp = getValue(htData, "brnValue", "   ");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontB10), "", 0.1f, LEFT, MIDDLE);
		pc.setColspan(2);
		innerTable.addCell(pc);
		
		// Inner Table Row 8: col 1: Name
		tmp = getValue(htData, "myLabel", "   ");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontN09), "", 0.1f, LEFT, MIDDLE);
		innerTable.addCell(pc);
		
		// Inner Table Row 8: col 2 & 3:
		tmp = getValue(htData, "myValue", "   ");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontB10), "", 0.1f, LEFT, MIDDLE);
		pc.setColspan(2);
		innerTable.addCell(pc);
		
		// Main Table Row 4: Column 1 (Data Column)
		PdfPCell pc1 = new PdfPCell(innerTable);
		pc1.setBorderWidthLeft(0.1f);
		pc1.setBorderWidthRight(0f);
		pc1.setBorderWidthTop(0.1f);
		pc1.setBorderWidthBottom(0.1f);
		mainTable.addCell(pc1);
		// Main Table Row 4: Column 2 (Photo Column)
		
		tmp = getValue(htData, "photoFile", "");
		String tmpdfp = getValue(htData, "defaultPhotofile", "");
		if(tmpdfp.equals(""))
		{
			tmpdfp = "c:/qis-exams/defaults/default-photo.jpg";
		}
	pc = createImageCell(tmp, 0.75f, 0.85f, 0f, 0.1f, 0.1f, 0.1f, RIGHT, TOP, tmpdfp);
		
		// pc = createTextCell(new Phrase("XXXX",MyFonts.fontB10),"TBR",0.1f,CENTER, MIDDLE);
		mainTable.addCell(pc);
		return mainTable;
	}
	
	/**
	 * Gets Marks Table Header with labels like slno, sub code, sub title grade etc.
	 * @param colWidths Column widths array 
	 * @param tableWidth Table total width in inches
	 * @param hdrStrings Header Strings array
	 * @return Marks  header table
	 */
	public static PdfPTable getMarksTableHeader(float colWidths[], float tableWidth, String hdrStrings[])
	{
		// Initialize fonts if not available
		if(!MyFonts.initialized)
		{
			MyFonts.initFonts();
		}
		// Check if initialized now
		if(!MyFonts.initialized)
		{
			System.out.println("Can not continue without fonts...");
			return null;
		}
		// Initialize half and full table widths
		MarksTable.init(colWidths, tableWidth);
		
		PdfPTable pt1 = new PdfPTable(MarksTable.widths);
		pt1.setTotalWidth(tblWidth);
		pt1.setSpacingAfter(3f);
		pt1.setSpacingBefore(3f);
		
		PdfPCell pc1 = new PdfPCell(new Paragraph(hdrStrings[0], MyFonts.fontB10)); // Slno
		PdfPCell pc2 = new PdfPCell(new Paragraph(hdrStrings[1], MyFonts.fontB10)); // Subcode
		PdfPCell pc3 = new PdfPCell(new Paragraph(hdrStrings[2], MyFonts.fontB10)); // subname
		PdfPCell pc4 = new PdfPCell(new Paragraph(hdrStrings[3], MyFonts.fontB10)); // grade
		PdfPCell pc5 = new PdfPCell(new Paragraph(hdrStrings[4], MyFonts.fontB10)); // gradePts
		PdfPCell pc6 = new PdfPCell(new Paragraph(hdrStrings[5], MyFonts.fontB10)); // credits
		pc1.setPaddingBottom(4f);		
		pc2.setPaddingBottom(4f);
		pc3.setPaddingBottom(4f);
		pc4.setPaddingTop(4f);
		pc5.setPaddingBottom(4f);
		pc6.setPaddingBottom(4f);
		// PdfPCell pc7 = new PdfPCell(new Paragraph(hdrStrings[6],
		// MyFonts.fontB10)); // Result
		
		// Slno
		//pc1.setRotation(90);
		pc1.setHorizontalAlignment(Element.ALIGN_CENTER);
		pc1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// Sub Code
		// pc2.setRotation(90);
		// pc2.setHorizontalAlignment(Element.ALIGN_LEFT);
		// pc2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pc2.setHorizontalAlignment(Element.ALIGN_CENTER);
		pc2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// Sub Title
		pc3.setHorizontalAlignment(Element.ALIGN_CENTER);
		//pc3.setHorizontalAlignment(Element.ALIGN_LEFT);
		pc3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// Grade
		//pc4.setRotation(90);
		pc4.setHorizontalAlignment(Element.ALIGN_CENTER);
		pc4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// Grade Points
	//	pc5.setRotation(90);
		pc5.setHorizontalAlignment(Element.ALIGN_CENTER);
		pc5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// Credits
	//	pc6.setRotation(90);
		pc6.setHorizontalAlignment(Element.ALIGN_CENTER);
		pc6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// Result
		// pc7.setRotation(90);
		// pc7.setHorizontalAlignment(Element.ALIGN_LEFT);
		// pc7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		// row 1 of inner table
		pt1.addCell(pc1);
		pt1.addCell(pc2);
		pt1.addCell(pc3);
		pt1.addCell(pc4);
		pt1.addCell(pc5);
		pt1.addCell(pc6);
		// pt1.addCell(pc7);
		return pt1;
	}
	
	/**
	 * Creates a marks table with given data
	 * 
	 * @param colWidths
	 *           Table Column relative widths as an array of floats
	 * @param tableWidth
	 *           Total table width in inches
	 * @param tableCaption
	 *           Table caption that appears centered on top of a table
	 * @param tableFooter
	 *           Text that will be shown as last row of the table
	 * @param mks
	 *           Marks array
	 * @param minRows
	 *           Minimum number of rows to be added to the table
	 * @return Table
	 */
	public static PdfPTable getMarksTable(float colWidths[], float tableWidth, String tableCaption, String tableFooter, String mks[][],
			int minRows)
	{
		// Initialize fonts if not available
		if(!MyFonts.initialized)
		{
			MyFonts.initFonts();
		}
		// Check if initialized now
		if(!MyFonts.initialized)
		{
			System.out.println("Can not continue without fonts...");
			return null;
		}
		// Initialize half and full table widths if necessary
		MarksTable.init(colWidths, tableWidth);
		
		PdfPTable pt = new PdfPTable(MarksTable.widths);
		pt.setTotalWidth(MarksTable.tblWidth);
		PdfPCell pc;
		// pt.setSpacingBefore(6.0f);
		// pt.setSpacingAfter(6.0f);
		
		int nrows = mks.length; // one for the footer if present
		if(tableFooter.trim().length() > 0)
		{
			// nrows += 1;
			minRows += 1;
		}
		int ncols = mks[0].length;
		if(nrows < minRows)
		{
			nrows = minRows;
		}
		int i, j;
		///// Caption Row
		if(tableCaption.trim().length() > 0)
		{
			pc = new PdfPCell();
			pc.setPaddingRight(4f);
			pc.setColspan(ncols);
			pc.disableBorderSide(PdfPCell.LEFT);
			pc.disableBorderSide(PdfPCell.RIGHT);
			pc.disableBorderSide(PdfPCell.TOP);
			pc.disableBorderSide(PdfPCell.BOTTOM);
			pc.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			pc.setPhrase(new Phrase(tableCaption, MyFonts.fontN10));
			pt.addCell(pc);
		}
		for(i = 0; i < nrows; i++)
		{
			for(j = 0; j < ncols; j++)
			{
				pc = new PdfPCell();
				pc.setPaddingRight(4f);
				pc.setNoWrap(true);
				pc.setFixedHeight(18.0f);
				pc.disableBorderSide(PdfPCell.TOP);
				pc.disableBorderSide(PdfPCell.BOTTOM);
				if(i == 0) // first row, hence top is enabled
				{
					pc.enableBorderSide(PdfPCell.TOP);
				}
				if(i == nrows - 1)
				{
					pc.enableBorderSide(PdfPCell.BOTTOM);
				}
				if(i > 0 && i < nrows - 1)
				{
					// do nothing for now
				}
				String tmpstr = (i < mks.length) ? mks[i][j] : " ";
				if(halfWidthTable == false)
				{
					if(j==1 || j == 2)
					{
						pc.setPhrase(new Phrase(tmpstr, MyFonts.fontN10));
					}
					else
					{
						pc.setPhrase(new Phrase(tmpstr, MyFonts.fontN12));
					}
				}
				else
				{
					pc.setPhrase(new Phrase(tmpstr, MyFonts.fontN12));
				}
				pc.setLeading(0.5f, 0.7f);
				if(i == mks.length && j == 2 && tableFooter.length() > 0)
				{
					pc.setPhrase(new Phrase(tableFooter, new Font(MyFonts.bfItalic, MyFonts.FONT_08)));
				}
				if(j==0 || j == 4 || j == 5)
				{
					pc.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//PREVIOUS RIGHT
				}
				else if (j==3)
				{
					pc.setPaddingLeft(4f);
					pc.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				}
				pt.addCell(pc);
			} // Inner for loop columns
		} // rows
		return pt;
	}
	
	/**
	 * Creates Footer table with GPA, CGPA, Signatures etc..
	 * 
	 * @param colWidths
	 *           - Float array with relative table column widths<br>
	 *           This takes 4 columns where:<br>
	 *           First column is for labels like (GPA, CGPA, Date etc.)<br>
	 *           Second column is for the values of GPA, CGPA etc.<br>
	 *           Third and 4th columns are for the signatures of
	 *           Principal/Director and Controller of Exams
	 * @param tableWidth
	 *           Total table width in Inches
	 * @param keyValuePairs
	 *           The array containing key and values separated by equals sign(=)
	 *           <br>
	 *           The Keys can be from the following list:<br>
	 *           <UL>
	 *           <li>gpaLabel <i>(Ex: GPA ) </i></li>
	 *           <li>gpaValue <i>(Ex: 8.7645) </i></li>
	 *           <li>cgpaLabel <i>(Ex: CGPA) </i></li>
	 *           <li>cgpaValue <i>(Ex: 7.9834) </i></li>
	 *           <li>dateLabel <i>(Ex: Date) </i></li>
	 *           <li>dateValue <i>(Ex: 06-09-2016) </i></li>
	 *           <li>prsigLabel <i>(Ex: Principal) </i></li>
	 *           <li>cesigLabel <i>(Ex: Controller of Exams| Director) </i></li>
	 *           <li>prsigFile <i>(Ex: c:/images/prsign.jpg) </i></li>
	 *           <li>cesigFile <i>(Ex: c:/images/cesign.jpg) </i></li>
	 *           <li>footer1 <i>(Ex: Note: GPA=Grade Point Average)</i></li>
	 *           <li>footer2 <i>(Ex: Note: CGPA=Cumulative Grade Point Average) </i></li>
	 *           <li>footer3 <i>(Ex: See over leaf for information) </i></li>
	 *           </UL>
	 * @return
	 */
	public static PdfPTable getFooterTable(float colWidths[], float tableWidth, String keyValuePairs[], int type)
	{
		PdfPTable mainTable;
		PdfPCell pc;
		String tmp;
		Hashtable<String, String> htData = new Hashtable<String, String>();
		
		// Initialize fonts if not available
		if(!MyFonts.initialized)
		{
			MyFonts.initFonts();
		}
		// Check if initialized now
		if(!MyFonts.initialized)
		{
			System.out.println("Can not continue without fonts...");
			return null;
		}
		// Initialize Hash table with key value pairs
		fillHashTable(htData, keyValuePairs);
		
		mainTable = new PdfPTable(colWidths);
		mainTable.setTotalWidth(tableWidth * 72); // convert total width to points
		mainTable.setSpacingBefore(3f);
		mainTable.setSpacingAfter(3f);
		
		// Row 1 Col 1: GPA
		tmp = getValue(htData, "gpaLabel", "   ");
		if(type == GradeSheetGenerator.ESSENTIAL)
		{
			tmp = "  ";
		}
		pc = createTextCell(new Phrase(tmp, MyFonts.fontN10), "LT", 0.1f, RIGHT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 1: col 2:
		tmp = getValue(htData, "gpaValue", "   ") + "  "; // some extra space after label
		if(type == GradeSheetGenerator.ESSENTIAL)
		{
			tmp = "  ";
		}
		pc = createTextCell(new Phrase(tmp, MyFonts.fontB10), "T", 0.1f, LEFT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 1: col 3: Dummy
		pc = createTextCell(new Phrase("  ", MyFonts.fontN09), "T", 0.1f, LEFT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 1: col 4: Dummy
		pc = createTextCell(new Phrase("  ", MyFonts.fontN09), "TR", 0.1f, LEFT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 2 Col 1: GPA
		tmp = getValue(htData, "cgpaLabel", "  ") + "  "; // some extra space after label
		if(type == GradeSheetGenerator.ESSENTIAL)
		{
			tmp = "  ";
		}
		pc = createTextCell(new Phrase(tmp, MyFonts.fontN10), "L", 0.1f, RIGHT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 2: col 2:
		tmp = getValue(htData, "cgpaValue", "  ");
		if(type == GradeSheetGenerator.ESSENTIAL)
		{
			tmp = "  ";
		}
		pc = createTextCell(new Phrase(tmp, MyFonts.fontB10), "", 0.1f, LEFT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 2: col 3: Dummy
		pc = createTextCell(new Phrase(" ", MyFonts.fontN09), "", 0.1f, LEFT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 2: col 4: Dummy
		pc = createTextCell(new Phrase(" ", MyFonts.fontN09), "R", 0.1f, LEFT, MIDDLE);
		mainTable.addCell(pc);

		// Row 3: Dummy Row
		pc = createTextCell(new Phrase(" ", MyFonts.fontN09), "LR", 0.1f, LEFT, MIDDLE);
		pc.setColspan(4);
		mainTable.addCell(pc);
		
		// Row 4: Dummy Row
		// pc = createTextCell(new Phrase(" ", MyFonts.fontN09),"",0.1f, LEFT,
		// MIDDLE);
		// pc.setColspan(4);
		// mainTable.addCell(pc);
		
		// Row 5 Col 1: dummy
		pc = createTextCell(new Phrase("  ", MyFonts.fontN09), "L", 0.1f, LEFT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 5 Col 2: dummy
		pc = createTextCell(new Phrase("  ", MyFonts.fontN09), "", 0.1f, LEFT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 5 Col 3: Principal Signature Image
		tmp = getValue(htData, "prsigFile", "");
		if(!tmp.equals(""))
		{
			pc = createImageCell(tmp, 1.0f, 0.5f, 0f, 0f, 0f, 0f, CENTER, MIDDLE, "");
		}
		else
		{
			pc = createTextCell(new Phrase("  ", MyFonts.fontN09), "", 0.1f, LEFT, MIDDLE);
		}
		mainTable.addCell(pc);
		
		// Row 5 Col 4: Controller of Examination Signature Image
		tmp = getValue(htData, "cesigFile", "");
		pc = createImageCell(tmp, 1.7f, 0.6f, 0f, 0f, 0.1f, 0f, CENTER, MIDDLE, "");
		//pc = createImageCell(tmp, 1.0f, 0.6f, 0f, 0f, 0.1f, 0f, CENTER, MIDDLE, "");
		
		mainTable.addCell(pc);
		
		// Row 6: col 1: Date Label
		tmp = getValue(htData, "dateLabel", "  ") + "  "; // extra space after label
		pc = createTextCell(new Phrase(tmp, MyFonts.fontN10), "L", 0.1f, RIGHT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 6: col 2: Date Label
		tmp = getValue(htData, "dateValue", "--");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontN10), "", 0.1f, LEFT, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 6: col 3: Principal Signature Label
		//tmp = getValue(htData, "PrsigLabel", "  ");
		tmp = "Verified By:"; // 
		pc = createTextCell(new Phrase(tmp, MyFonts.fontN10), "", 0.1f, CENTER, MIDDLE);
		
		mainTable.addCell(pc);
		
		// Row 6: col 4: CE Signature Label
		tmp = getValue(htData, "CesigLabel", "  ");
		pc = createTextCell(new Phrase(tmp, MyFonts.fontN10), "R", 0.1f, CENTER, MIDDLE);
		mainTable.addCell(pc);
		
		// Row 7: Dummy Row
		pc = createTextCell(new Phrase("  ", MyFonts.fontN09), "LBR", 0.1f, LEFT, MIDDLE);
		pc.setColspan(4);
		mainTable.addCell(pc);
		
		// Row 8: footer 1
		tmp = getValue(htData, "footer1", "  ");
		if(type == GradeSheetGenerator.ESSENTIAL)
		{
			tmp = "  ";
		}
		pc = createTextCell(new Phrase(tmp, MyFonts.fontI08), "", 0.1f, LEFT, MIDDLE);
		pc.setColspan(4);
		mainTable.addCell(pc);
		
		// Row 9: footer 2
		tmp = getValue(htData, "footer2", "  ");
		if(type == GradeSheetGenerator.ESSENTIAL)
		{
			tmp = "  ";
		}
		pc = createTextCell(new Phrase(tmp, MyFonts.fontI08), "", 0.1f, LEFT, MIDDLE);
		pc.setColspan(4);
		mainTable.addCell(pc);
		
		// Row 10: footer 3
		tmp = getValue(htData, "footer3", " ");
		if(type == GradeSheetGenerator.ESSENTIAL)
		{
			tmp = "  ";
		}
		pc = createTextCell(new Phrase(tmp, MyFonts.fontI08), "", 0.1f, LEFT, MIDDLE);
		pc.setColspan(4);
		mainTable.addCell(pc);
		
		return mainTable;
	}
	
	/**
	 * Constructs a cell with image
	 * 
	 * @param fileName
	 *           Image file Name
	 * @param width
	 *           New Width of image in points
	 * @param height
	 *           New height of image in points
	 * @param borderLeft
	 *           left border width (0= no border, 1 = 1 point border ...)
	 * @param borderTop
	 *           top border width
	 * @param borderRight
	 *           right border width
	 * @param borderBottom
	 *           bottom border width
	 * @param halign
	 *           Horizontal alignment (Image.ALIGN_LEFT| Image.ALIGN_RIGHT|
	 *           Image.ALIGN_CENTER)
	 * @param valign
	 *           Vertical alignment (Image.ALIGN_TOP| Image.ALIGN_BOTTOM|
	 *           Image.ALIGN_MIDDLE)
	 * @param defaultImageFile
	 *           Default image file to be used when image is not found
	 * @return New cell with either the image or blank
	 */
	public static PdfPCell createImageCell(String fileName, float width, float height, float borderLeft, float borderTop,
			float borderRight, float borderBottom, int halign, int valign, String defaultImageFile)
	{
		PdfPCell pc = null;
		if(fileName == null || fileName.equals(""))
		{
			pc = new PdfPCell(new Phrase("  "));
		}
		else
		{
			File fcheck = new File(fileName);
			if(!fcheck.exists()) // If photo not found, use dummy
			{
				fileName = defaultImageFile;
			}
			Image img = null;
			try
			{
				img = Image.getInstance(fileName);
				img.scaleToFit(width * 72, height * 72); // in points
				img.setAlignment(halign | valign);
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				img = null;
			}
			
			if(img != null)
			{
				pc = new PdfPCell(img);
				//System.out.println("Image Cell created...");
			}
			else
			{
				pc = new PdfPCell(new Phrase("  "));
				System.out.println("Text Cell created....");
			}
		}
		pc.setBorderWidth(0);
		pc.setBorderWidthLeft(borderLeft);
		pc.setBorderWidthTop(borderTop);
		pc.setBorderWidthRight(borderRight);
		pc.setBorderWidthBottom(borderTop);
		pc.setPaddingRight(5f);
		pc.setPaddingTop(5f);
		
		int val, hal;
		switch (halign)
		{
			case Image.ALIGN_LEFT:
				hal = PdfPCell.ALIGN_LEFT;
				break;
			case Image.ALIGN_RIGHT:
				hal = PdfPCell.ALIGN_RIGHT;
				break;
			case Image.ALIGN_CENTER:
				hal = PdfPCell.ALIGN_CENTER;
				break;
			default:
				hal = PdfPCell.ALIGN_LEFT;
		}
		switch (valign)
		{
			case Image.ALIGN_TOP:
				val = PdfPCell.ALIGN_TOP;
				break;
			case Image.ALIGN_BOTTOM:
				val = PdfPCell.ALIGN_BOTTOM;
				break;
			case Image.ALIGN_MIDDLE:
				val = PdfPCell.ALIGN_MIDDLE;
				break;
			default:
				val = PdfPCell.ALIGN_MIDDLE;
		}
		
		pc.setHorizontalAlignment(hal);
		pc.setVerticalAlignment(val);
		return pc;
	}
	
	/**
	 * Creates a cell with given border
	 * 
	 * @param phrase
	 *           Text value to be filled as a Phrase object
	 * @param borders
	 *           String that contains L/T/R/B characters where a letter
	 *           represents the border<br>
	 *           For example, if Top and Left borders are required, the string
	 *           will be "TL" or "LT"
	 * @param borderWidth
	 *           Width of border
	 * @param halign
	 *           Horizontal alignment (PdfPCell.LEFT or RIGHT or CENTER)
	 * @param valign
	 *           Vertical Alignment (PdfPCell.TOP or BOTTOM or MIDDLE)
	 * @return Cell
	 */
	public static PdfPCell createTextCell(Phrase phrase, String borders, float borderWidth, int halign, int valign)
	{
		borders = borders.toUpperCase();
		float l = borders.contains("L") ? borderWidth : 0f;
		float t = borders.contains("T") ? borderWidth : 0f;
		float r = borders.contains("R") ? borderWidth : 0f;
		float b = borders.contains("B") ? borderWidth : 0f;
		return createTextCell(phrase, l, t, r, b, halign, valign);
	}
	
	/**
	 * Creates a cell with given border widths and alignment
	 * 
	 * @param phrase
	 *           Text to be filled as a Phrase object
	 * @param left
	 *           Left border width
	 * @param top
	 *           Top border width
	 * @param right
	 *           Right border width
	 * @param bottom
	 *           Bottom border width
	 * @param halign
	 *           Horizontal alignment (PdfPCell.LEFT or RIGHT or CENTER)
	 * @param valign
	 *           Vertical Alignment (PdfPCell.TOP or BOTTOM or MIDDLE)
	 * @return PDF Cell
	 */
	public static PdfPCell createTextCell(Phrase phrase, float left, float top, float right, float bottom, int halign, int valign)
	{
		PdfPCell pc = new PdfPCell(phrase);
		pc.disableBorderSide(PdfPCell.LEFT);
		pc.disableBorderSide(PdfPCell.RIGHT);
		pc.disableBorderSide(PdfPCell.TOP);
		pc.disableBorderSide(PdfPCell.BOTTOM);
		
		pc.setBorderWidth(0);
		pc.setBorderWidthLeft(left);
		pc.setBorderWidthTop(top);
		pc.setBorderWidthRight(right);
		pc.setBorderWidthBottom(bottom);
		pc.setPaddingLeft(2);
		pc.setPaddingTop(2);
		pc.setPaddingRight(2);
		pc.setPaddingBottom(2);
		pc.setHorizontalAlignment(halign);
		pc.setVerticalAlignment(valign);
		return pc;
	}
	
	/**
	 * Creates an array of strings with dummy data for header data table
	 * 
	 * @return created hash table
	 */
	public static String[] createDummyDataArray()
	{
		String os = System.getProperty("os.name").toUpperCase();
		ArrayList<String> ar = new ArrayList<String>();
		ar.add("sheetTitle=Grade Sheet");
		ar.add("sheetType=Transcript");
		ar.add("slnoLabel=Code:");
		ar.add("slnoValue=12345678");
		ar.add("examTitle=B.Tech. I Year I Semester (Regular)");
		if(os.contains("WINDOWS"))
		{
			ar.add("photoFile=c:/qis-exams/photos/15491A0101.jpg");
			ar.add("defaultPhotoFile=c:/qis-exams/defaults/default-photo.jpg");
		}
		else
		{
			ar.add("photoFile=/home/qisexams/qis-exams/photos/154910101.jpg");
			ar.add("defaultPhotoFile=/home/qisexams/qis-exams/defaults/default-photo.jpg");
		}
		ar.add("crsLabel=Semester");
		ar.add("crsValue=I Semester");
		ar.add("brnLabel=Branch");
		ar.add("brnValue=Computer Networks and Information Security");
		ar.add("rlnoLabel=RollNo");
		ar.add("rlnoValue=15491A0101");
		ar.add("nameLabel=Name");
		ar.add("nameValue=Ayyannagari Naga Venkata Satyanarayana");
		ar.add("fnameLabel=Father Name");
		ar.add("fnameValue=Ayyannavari Lakshmi Narayana Swamy");
		ar.add("mnameLabel=Mother Name");
		ar.add("mnameValue=Ayyannavari Vijaya Lakshmi");
		ar.add("myLabel=Month and Year of Exam");
		ar.add("myValue=June, 2016");
		String x[] = new String[ar.size()];
		return ar.toArray(x);
	}
	
	/**
	 * Creates dummy data for footer table
	 */
	public static String[] createDummyFooterArray()
	{
		ArrayList<String> ar = new ArrayList<String>();
		ar.add("gpaLabel=SGPA");
		ar.add("gpaValue=7.3654");
		ar.add("cgpaLabel=CGPA");
		ar.add("cgpaValue=8.0345");
		ar.add("prsigLabel=Examinations In-charge");
		ar.add("cesigLabel=DIRECTOR");
		ar.add("prsigFile=" + "c:/qis-exams/defaults/pr-signature.jpg");
		ar.add("cesigFile=" + "c:/qis-exams/defaults/ce-signature.jpg");
		ar.add("dateLabel=Date");
		ar.add("dateValue=06-09-2016");
		ar.add("footer1=Note: GPA = Grade Point Average,  CGPA = Cumulative Grade Point Average");
		ar.add("footer3=See over leaf for information");
		String x[] = new String[ar.size()];
		return ar.toArray(x);
	}
	
	/**
	 * Fills the Hash table with given key value pairs separated by '='
	 * 
	 * @param data
	 *           Hash table which will be filled with given data
	 * @param keyValuePairs
	 *           Equals (=) separated array of key-value pair strings
	 */
	private static void fillHashTable(Hashtable<String, String> data, String keyValuePairs[])
	{
		int i;
		String key;
		String val;
		String arr[];
		for(i = 0; i < keyValuePairs.length; i++)
		{
			arr = keyValuePairs[i].split("=");
			if(arr.length < 2)
			{
				continue;
			}
			key = arr[0].trim().toUpperCase();
			val = arr[1];
			for(int j = 2; j < arr.length; j++)
			{
				val += ("=" + arr[j]);
			}
			data.put(key, val);
		}
	}
	
	/**
	 * Gets a value from hash table for a given key or default value
	 * 
	 * @param data
	 *           Hash table from which key should be searched
	 * @param key
	 *           Key for which value is required
	 * @param defaultValue
	 *           Default value if key not found
	 * @return Value for the given key if found or default value otherwise.
	 */
	public static String getValue(Hashtable<String, String> data, String key, String defaultValue)
	{
		String tmp = data.get(key.toUpperCase());
		if(tmp == null)
		{
			tmp = defaultValue;
		}
		return tmp;
	}
	
	public static void main(String[] args) throws Exception
	{
		String mksTblHdr[] = { "S.No.", "Subject Code", "Subject Title", "Grade", "Grade Points", "Credits"};

		String mksTbl[][] = { 
				{ "1", "A0001", "MATHEMATICAL FOUNDATIONS OF COMPUTER SCIENCE", "A", "8", "4"}, 
				{ "2", "A0002", "COMPUTER PROGRAMMING", "A+", "9", "4"}, 
				{ "3", "A0003", "PROBABILITY AND STATISTICS", "O", "10", "4"}, 
				{ "4", "A0004", "ACCOUNTING AND FINANCIAL MANAGEMENT", "F", "0", "0"}, 
				{ "5", "A0005", "INFORMATION & COMMUNICATION TECHNOLOGY", "B", "6", "4"}, 
				{ "6", "A0006", "TECHNICAL COMMUNICATION & COMPUTER ETHICS", "B+", "7", "4"}, 
				{ "7", "A0007", "COMPUTER PROGRAMMING LAB", "A-", "6", "4"}, 
				{ "8", "A0008", "INFORMATION & COMMUNICATION TECHNOLOGY LAB", "A", "8", "4"},
				{ "9", "A0009", "ENGLISH COMMUNICATION SKILLS LAB", "A+", "9", "4"} 
				};
		
		float widths1[] = {3f, 6f, 40f, 3f, 3f, 3f};
		float tblWidth1 = 7.25f; // For full width table
		// float widths2[] = {3f,6f,40f,3f,3f,3f,3f};
		// float tblWidth2 = 3.625f; // For half width table
		
		// float dataWidths[] = {1.0f, 1.0f, 4.25f, 1.0f};
		// float ftrWidths[] = {1.25f, 2.0f, 2.0f, 2.0f};
		
		Document doc = null;
		doc = new Document(PageSize.A4, 0, 0, 0, 0); // Create PDF document
		PdfWriter pw = PdfWriter.getInstance(doc, new FileOutputStream("test.pdf")); // Get
																												// PdfWriter
																												// object
		doc.open(); // Open Document
		
		PdfContentByte pcb = pw.getDirectContent();
		
		float yoffset = 1.5f;
		
		// Student and Exam Data Table
		String[] dd = createDummyDataArray();
		PdfPTable pth = getDataTable(new float[] { 6.5f, 1f }, 7.25f, dd);
		pth.writeSelectedRows(0, -1, 36f, 72 * (11.68f - yoffset), pcb);
		System.out.println("Data Table Height: " + (pth.getTotalHeight() / 72f));
		
		yoffset += (0.25f + pth.getTotalHeight() / 72f);
		// Marks Table Header
		PdfPTable pt1 = MarksTable.getMarksTableHeader(widths1, tblWidth1, mksTblHdr);
		pt1.writeSelectedRows(0, -1, 36f, 72f * (11.68f - yoffset), pcb);
		System.out.println("Marks Table Header Height: " + (pt1.getTotalHeight() / 72f));
		
		yoffset += (0.05+ pt1.getTotalHeight() / 72f);
		// Marks Table
		PdfPTable pt2 = MarksTable.getMarksTable(widths1, tblWidth1, "", "* Notice A", mksTbl, 10);
		pt2.writeSelectedRows(0, -1, 36f, 72f * (11.68f - yoffset), pcb);
		System.out.println("Marks Table height: " + (pt2.getTotalHeight() / 72f));
		
		yoffset += (0.5f + pt2.getTotalHeight() / 72f);
		// Footer table
		dd = createDummyFooterArray();
		PdfPTable ptf = getFooterTable(new float[] { 1.0f, 3.25f, 1.5f, 1.5f }, 7.25f, dd, GradeSheetGenerator.GRADESHEET);
		ptf.writeSelectedRows(0, -1, 36f, 72f * (11.68f - yoffset), pcb);
		
		// float tw = pt2.getTotalWidth();
		// float th = pt2.getTotalHeight();
		// System.out.println("TW, TH: " + tw + ", " + th);
		doc.close();
		System.out.println("Document Created...");
	}
	
}
