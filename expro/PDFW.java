package expro;

import java.io.*;

//import java.util.*;
import javax.swing.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import common.Subject;

public abstract class PDFW //extends JFrame implements ActionListener
{
	public final static int LEFT =   0;
	public final static int RIGHT =  1;
	public final static int CENTER = 2;
	
	//public HeaderStrings hs = null;
	//public SubjectStrings ss = null;
	
	//protected Vector <Question> sqv;
	//protected Vector <Question> fqv;
	
	protected Globals g;
	
	private boolean twoParts;

	////////////// PDF Related Variables
	int leftMargin; // unit=1/72"
	int rightMargin;
	int topMargin;
	int bottomMargin;
	int borderWidth;
	int scale;   // Percent Reduction/Enlargement
	protected float widths[];	
	public void setWidths(float[] widths)
	{
		this.widths = widths;
	}

	Font curFont;
	int alignment;

   Chunk newLine;
   
	
	
	
	public void setTwoParts(boolean twoParts)
	{
		this.twoParts = twoParts;
	}

	//Subjects subjects=null;
	protected String subCode;
	protected String subTitle;

	protected String msg;
	protected String partaMarks="";
	protected String partbMarks="";
	
	public PDFW(Globals g)
	{
		this.g = g;
		
		//hs  = new HeaderStrings(g);
		//ss  = new SubjectStrings(g);
		
		//sqv = new Vector<Question>();
		//fqv = new Vector<Question>();

		leftMargin = 36; // unit=1/72"
		rightMargin = 36;
		topMargin = 36;
		bottomMargin = 36;
		borderWidth=0;
		scale=50;
		newLine = new Chunk("\n");
		widths = new float[]{0.05f, 0.95f};

	}

	public void genPDF(String qfname)
	{
		//System.out.println("File Name from call to genPDF:"+qfname);
		boolean res = loadQg(qfname);
		if(!res)
		{
			msg = "(PDFW:GenPDF) Can not load questions from: "+qfname;
			JOptionPane.showMessageDialog(null, msg);
			return;
		}
	}
	
	public boolean loadQg(String fname)
	{
		boolean res = true;
		subCode ="";
		try
		{
//			if(sqv.size() > 0) // For 2 part question paper, short questions exist
//			{
//				sqv.removeAllElements();
//			}
//			fqv.removeAllElements();
//			
//			hs.load(fname);
//			ss.load();
			
			FileReader fr = new FileReader(fname); 
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line=br.readLine()) != null)
			{
				if(line.trim().startsWith("#")) 
				{
					continue; // Skip Comments
				}
				String arr[] = line.split(":");
				if(arr.length != 7) // Skip all header string lines 
				{
					continue;
				}
//				Question q = new Question();
//				q.scode = arr[1]; // subcode
//				q.unit = Utilities.parseInt(arr[2]); // Unit
//				q.group = Utilities.parseInt(arr[3]); // Group
//				q.level = arr[4].toLowerCase().trim(); //Level
//				q.marks = Utilities.parseInt(arr[5]); // Marks
//				q.slno = Utilities.parseInt(arr[6]); // Question Slno
				
//				if(subCode.equals("")){subCode=q.scode;} // Set subject Code only once
				
				if(arr[0].equalsIgnoreCase("S"))
				{
					if(twoParts)
					{
	//					sqv.add(q); // Add to short question vector
					}
					else
					{
//						fqv.add(q); // Add to full questions vector
					}
				}
				else if (arr[0].equalsIgnoreCase("F"))
				{
//					fqv.add(q);
				}
			}
			fr.close();
		}
		catch(Exception e)
		{
			msg = "(PDFW:loadQG) "+e.getMessage();
			showMessage(msg);
			res=false;
		}
		return res;
	}

	
	protected Document getDocument(Rectangle pageSize)
	{
		return new Document(pageSize, leftMargin, rightMargin, topMargin, bottomMargin);
	}
	
	protected PdfWriter getPdfWriter(Document doc, String foutName)
	{
		PdfWriter pw = null;
		FileOutputStream fout;
		try
		{
			fout = new FileOutputStream(foutName);
			pw =  PdfWriter.getInstance(doc, fout);
			pw.setPageEvent(new MyPageAdaptor());
		}
		catch (Exception e){showMessage("Eror in PdfWriter Creation: " + e.getMessage());}
		
		return pw;
	}
	
	protected BaseFont getBaseFont()
	{
		BaseFont hfnt=null;
      try
      {
      	hfnt= BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, false);
      }
      catch(Exception e){showMessage("Eror in Base Font Creation: " + e.getMessage());}
      return hfnt;
	}
	
	public void showMessage(String msg)
	{
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public void setFont(Font f)
	{
		curFont = f;
	}
	
	public void setAlignment(int align)
	{
		alignment = align;
	}
	
	public void setHeaderFooter(Document doc)
	{
		HeaderFooter header = new HeaderFooter(new Phrase("Code: ", curFont),false);		   
	   header.setAlignment(alignment);
	   header.setBorder(Rectangle.NO_BORDER);
	   doc.setHeader(header);
	}

	public Paragraph getParagraph(String msg, boolean isNewLine)
	{
		Paragraph p=new Paragraph();
		p.setAlignment(alignment);
		Chunk c=new Chunk(msg, curFont);
		p.add(c);
		if(isNewLine)
		{
			p.add(newLine);
		}
		return p;
	}
	public void addQuestion(PdfPTable qpTable, String qno, String q)
	{
		// Q. No.
		Paragraph p1 = new Paragraph(qno, curFont);
		PdfPCell cel1 = new PdfPCell(p1);
		cel1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cel1.setPadding(0);
		cel1.setBorderWidth(borderWidth);
		qpTable.addCell(cel1);
		
		// Question Image
		String imgFile = Globals.basePath;
		Image img=null;
		try
		{
			img = Image.getInstance(imgFile);
		}
		catch (Exception e)
		{
			showMessage("(PDFW:getQuestion: " + e.getMessage());
			return;
		}
		img.scalePercent(scale);
		Paragraph p2 = new Paragraph("");
		p2.add(new Chunk(img,2,2)); // offset is 2 pixels
		p2.add(newLine);
		PdfPCell cel2 = new PdfPCell(p2);
		cel2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cel2.setBorderWidth(borderWidth);
		qpTable.addCell(cel2);
		
	}
	
	public abstract void generatePDF(String ofname);
	protected String getReplicatedLine(int n, String ch)
	{
		String s="";
		for(int i=0;i<n;i++) 
		{ 
			s += ch; 
		}
		return s;
	}
	
	protected String getFileName(String basePath, String q)
	{
		String fnm = 
				basePath+ Globals.pathSep + 
				"qbank"+ Globals.pathSep + 
//				q.scode+ g.pathSep + 
//				"jpg"+ g.pathSep + 
//				q.scode+
//				String.format("%02d", q.unit)+
//				String.format("%02d", q.group)+ 
//				q.level.toLowerCase()+
//				String.format("%02d", q.marks)+	
//				String.format("%02d", q.slno)+ 
				"-q.jpg";
		return fnm;
	}
	
		public void convertQpt2Pdf()
		{
			File inf; 
			PPDFilter myFF = new PPDFilter(".qpt");
			String fnames[]; 
			int i;
			String subTitle, subCode;
			Subject sbj;
			
			// Check if question papers directory exists and create if necessary
			File qpdir = new File(Globals.basePath+Globals.pathSep);
			if(!qpdir.exists())
			{
				qpdir = null;
				inf = new File(Globals.basePath);
			}
			else
			{
				inf = new File(Globals.basePath+Globals.pathSep);
			}
			
			fnames = inf.list(myFF);
			
			for(i=0;i<fnames.length;i++)
			{
				if(inf != null)
				{
					genPDF(Globals.basePath+Globals.pathSep+Globals.pathSep+fnames[i]);
				}
				else
				{
					genPDF(Globals.basePath+Globals.pathSep+fnames[i]);
				}
				subCode = fnames[i].substring(0,Globals.subCodeLen);
				
				sbj = Globals.subjects.getSubject(subCode);
				subTitle = sbj.title;
				subTitle.replace(' ', '_');
				String y;
				if(qpdir==null)
				{
					y = Globals.basePath+Globals.pathSep+fnames[i].substring(0, fnames[i].length()- 4) +"-"+ subTitle+".pdf";
				}
				else
				{
					y = Globals.basePath+Globals.pathSep+Globals.pathSep+fnames[i].substring(0, fnames[i].length()- 4) +"-"+ subTitle+".pdf";
				}
				generatePDF(y);
			}
			showMessage("Number of PDF Files Created: "+ i);
		}
		
	public void writeEndText(PdfWriter pw, Document doc)
	{
		PdfTemplate pdft;
		BaseFont fnt;
		
		pdft = pw.getDirectContent().createTemplate(50, 50);
		pdft.setBoundingBox(new Rectangle(-20,-20,50,50));
		try
		{
			fnt = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		}
		catch (Exception e) 
		{ 
			throw new ExceptionConverter(e);
		}
		
		PdfContentByte pcb = pw.getDirectContent();
		pcb.saveState();
		float tw = fnt.getWidthPoint("00",10);
		String msg="- xxx -";
		//float ypos = doc.bottom() - 30;
		float ypos = pw.getVerticalPosition(false)- 10;
		float textWidth = fnt.getWidthPoint(msg, 12);
		pcb.beginText();
		pcb.setFontAndSize(fnt, 10);
		pcb.setTextMatrix((doc.right()-textWidth-tw)/2, ypos);
		pcb.showText(msg);
		pcb.endText();
		pcb.addTemplate(pdft, doc.right()-tw, ypos);
		pcb.restoreState();
		
		return;
	}

	class PPDFilter implements FilenameFilter 
	{
		String re;
		public PPDFilter(String re) 
		{
			this.re = re;
		}
		public boolean accept(File dir, String name) 
		{
			return name.endsWith(re);
		}
	}
	
	public class MyPageAdaptor extends PdfPageEventHelper
	{
		protected PdfTemplate pdft;
		BaseFont fnt;
		
		public void onOpenDocument(PdfWriter pw, Document doc)
		{
			pdft = pw.getDirectContent().createTemplate(100, 100);
			pdft.setBoundingBox(new Rectangle(-20,-20,100,100));
			try
			{
				fnt = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			}
			catch (Exception e) { throw new ExceptionConverter(e);}
		}
		
		public void onEndPage(PdfWriter pw, Document doc)
		{
			PdfContentByte pcb = pw.getDirectContent();
			pcb.saveState();
			float tw = fnt.getWidthPoint("00",12);
			String msg="Page: " + pw.getPageNumber()+ " of ";
			float ypos = doc.bottom() - 30;
			float textWidth = fnt.getWidthPoint(msg, 12);
			pcb.beginText();
			pcb.setFontAndSize(fnt, 12);
			pcb.setTextMatrix(doc.right()-textWidth-tw, ypos);
			pcb.showText(msg);
			pcb.endText();
			pcb.addTemplate(pdft, doc.right()-tw, ypos);
			pcb.restoreState();
		}
		
		public void onCloseDocument(PdfWriter pw, Document doc)
		{
			pdft.beginText();
			pdft.setFontAndSize(fnt, 12);
			pdft.setTextMatrix(0,0);
			pdft.showText(String.valueOf(pw.getPageNumber() - 1));
			pdft.endText();
		}
	}

	/********************
	public static void main(String args[])
	{
		Globals g = new Globals("./config.dat");
		
		// Load Courses
		g.courses = new Courses(g);
		g.courses.loadCourses();
		
		// Load Branches
		g.branches = new Branches(g);
		g.branches.loadBranches();

		// Load subjects
		g.subjects = new Subjects(g);
		g.subjects.loadSubjects();
		
		if(g.subjects == null)
		{
			System.out.println("Can not load subjects");
			System.exit(1);
		}
		System.out.println("Number of SUbjects: "+ g.subjects.getSubjectCount());
		g.questions = new Questions(g);
		g.questions.loadQuestions("C0208");
		
		System.out.println("No. Of Questions:" + g.questions.getQuestionCount(0,0,'*',0));
		//QuestionGenerator qg = new QuestionGenerator("C0302");
		//qg.generateQuestionPattern();
		PDFW pdw = new PDFW(g);
		pdw.doTest();
	}
	************************/

}

