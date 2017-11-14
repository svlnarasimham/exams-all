package gradesheet;
import java.awt.Color;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class PdfGenerator 
{
	/// Font sizes
	public static final int HUGE = 18;
	public static final int LARGE =16; 
	public static final int BIG = 14;
	public static final int NORMAL = 12;
	public static final int SMALL = 10;
	public static final int TINY = 8;
	
	/// Colors
	public static final Color BLACK    = Color.black;
	public static final Color BLUE     = Color.blue;
	public static final Color CYAN     = Color.cyan;
	public static final Color DARKGRAY = Color.darkGray;
	public static final Color GRAY     = Color.gray;
	public static final Color GREEN    = Color.green;
	public static final Color MAGENTA  = Color.magenta;
	public static final Color ORANGE   = Color.orange;
	public static final Color PINK     = Color.pink;
	public static final Color RED      = Color.red;
	public static final Color WHITE    = Color.white;
	public static final Color YELLOW   = Color.yellow;

	public static final float A4WIDTH  = 8.27f;
	public static final float A4HEIGHT = 11.69f;
	
	float offsetX = 0.0f;
	float offsetY = 0.0f;
	float pageHeight = A4HEIGHT;
	float pageWidth  = A4WIDTH;
	
	//////////// ContentByte based Write Methods for various Data types ////////////
	/**
	 * Puts an Image object at a given location
	 * @param cb PdfContentByte object
	 * @param img Image object to be inserted
	 * @param x x coordinate in inches
	 * @param y y coordinate in inches
	 * @param w width of image in inches
	 * @param h height of image in inches
	 */
	protected void putImage(PdfContentByte cb, Image img, float x, float y, float w, float h)
	{
		try
		{
			cb.saveState();
			img.scaleToFit(w * 72, h * 72);
			img.setAbsolutePosition((x + offsetX) * 72,
			      (pageHeight - (y + offsetY)) * 72);
			cb.addImage(img);
			cb.restoreState();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void putTable(PdfContentByte cb, PdfPTable pt, float x, float y)
	{
		//cb.saveState();
		pt.writeSelectedRows(0, -1, (x + offsetX) *72, (pageHeight - (y + offsetY))* 72, cb);
		//cb.restoreState();
	}
	
//	private void putNumber(PdfContentByte cb, int num, int ndigits, float x,float y)
//	{
//		putNumber(cb, num, ndigits, x, y, NORMAL, BLACK);
//	}
	
	protected void putNumber(PdfContentByte cb, int num, int ndigits, float x, float y, Color c)
	{
		putNumber(cb, num, ndigits, x, y, NORMAL, c);
	}
	
	protected void putNumber(PdfContentByte cb, int num, int ndigits, float x, float y, int fontSize)
	{
		putNumber(cb, num, ndigits, x, y, fontSize, BLACK);
	}
	
	protected void putNumber(PdfContentByte cb, int num, int ndigits, float x, float y, int fontSize, Color c)
	{
		String tmp = "" + num;
		int len = tmp.length();
		for (int i = 0; i < ndigits - len; i++)
		{
			tmp = " " + tmp;
		}

		try
		{
			BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI,BaseFont.NOT_EMBEDDED);

			cb.saveState();
			cb.beginText();
			cb.setColorFill(c);
			cb.moveText((x + offsetX) * 72, (pageHeight - (y + offsetY)) * 72);
			cb.setFontAndSize(bf, fontSize);
			cb.showText("" + tmp);
			cb.endText();
			cb.restoreState();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void putText(
			PdfContentByte cb, 
			String text, 
			float x, 
			float y,
			int fontSize, 
			Color fontColor, 
			BaseFont bf)
	{
		try
		{
			cb.saveState();
			cb.beginText();
			cb.setColorFill(fontColor); // Set Font color
			cb.moveText((x + offsetX) * 72, (pageHeight - (y + offsetY)) * 72);
			cb.setFontAndSize(bf, fontSize);
			cb.showText(text);
			cb.endText();
			cb.restoreState();
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	protected void putNumber(PdfContentByte cb, int num, int ndigits, float x,float y)
	{
		putNumber(cb, num, ndigits, x, y, NORMAL, BLACK);
	}
	
	protected void putText(PdfContentByte cb, String text, float x, float y)
	{
		putText(cb,text,x,y,NORMAL, BLACK);
	}
	
	protected void putText(PdfContentByte cb, String text, float x, float y,Color c)
	{
		putText(cb,text,x,y,NORMAL, c);
	}
	
	protected void putText(PdfContentByte cb, String text, float x, float y, int fontSize)
	{
		putText(cb,text,x,y,fontSize, Color.black);
	}
	
	protected void putText(PdfContentByte cb, String text, float x, float y,int fontSize, BaseFont bf)
	{
		putText(cb, text, x, y, fontSize, BLACK, bf);
	}
	protected void putText(PdfContentByte cb, String text, float x, float y,int fontSize, Color fontColor)
	{
			BaseFont bf=null;
         try
         {
	         bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI,BaseFont.NOT_EMBEDDED);
	         putText(cb, text, x, y, fontSize, fontColor, bf);
         }
         catch (Exception e)
         {
	         System.out.println(e.getMessage());
         }
	}


}

