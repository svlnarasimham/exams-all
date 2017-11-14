package common;
import java.awt.Color;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class PdfLibrary 
{
	/// Font sizes
	public static final int HUGE = 18;
	public static final int LARGE =16; 
	public static final int BIG = 14;
	public static final int NORMAL = 12;
	public static final int SMALL = 10;
	public static final int TINY = 8;
	
	/// Colors
	public static final Color BLACK = Color.black;
	public static final Color BLUE = Color.blue;
	public static final Color CYAN = Color.cyan;
	public static final Color DARKGRAY = Color.darkGray;
	public static final Color GRAY = Color.gray;
	public static final Color GREEN = Color.green;
	public static final Color MAGENTA = Color.magenta;
	public static final Color ORANGE = Color.orange;
	public static final Color PINK = Color.pink;
	public static final Color RED = Color.red;
	public static final Color WHITE = Color.white;
	public static final Color YELLOW = Color.yellow;

	public PdfContentByte pcb;
	
	public static final float POINTS_PER_CM = 28.3465f;
	
	public float pageHeight;
	public PdfLibrary()
	{
		 pageHeight = 29.7f; // A4 page
	}
	
	public void setPageHeight(float pageHeight)
	{
		this.pageHeight = pageHeight;
	}
	
	//////////// ContentByte based Write Methods for various Data types ////////////
	/**
	 * Puts an Image object at a given location
	 * @param cb PdfContentByte object
	 * @param img Image object to be inserted
	 * @param x x coordinate in cm
	 * @param y y coordinate in cm
	 * @param w width of image in cm
	 * @param h height of image in cm
	 */
	protected void putImage(PdfContentByte cb, Image img, float x, float y, float w, float h)
	{
		putImage(cb,img, x, y, w, h, 0.0f);
	}
	
	/**
	 * Puts an Image object at a given location
	 * @param cb PdfContentByte object
	 * @param img Image object to be inserted
	 * @param x x coordinate in cm
	 * @param y y coordinate in cm
	 * @param w width of image in cm
	 * @param h height of image in cm
	 * @param angle Anti-clock-wise angle in degrees
	 */
	protected void putImage(PdfContentByte cb, Image img, float x, float y, float w, float h, float angle)
	{
		try
		{
			cb.saveState();
			img.scaleToFit(w * POINTS_PER_CM, h * POINTS_PER_CM);
			img.setAbsolutePosition(x * POINTS_PER_CM,(pageHeight - y)* POINTS_PER_CM);
			img.setRotationDegrees(angle);
			cb.addImage(img);
			cb.restoreState();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a table to the current page
	 * @param cb PDF content byte
	 * @param pt Pdf Table Object 
	 * @param x  X coordinate in cm
	 * @param y  Y coordinate in cm
	 */
	protected void putTable(PdfContentByte cb, PdfPTable pt, float x, float y)
	{
		//cb.saveState();
		pt.writeSelectedRows(0, -1, x *POINTS_PER_CM, (pageHeight - y)* POINTS_PER_CM, cb);
		//cb.restoreState();
	}
	
//	private void putNumber(PdfContentByte cb, int num, int ndigits, float x,float y)
//	{
//		putNumber(cb, num, ndigits, x, y, NORMAL, BLACK);
//	}
	
	/**
	 * Puts a number at given location on the current page
	 * @param cb PDF Content byte object
	 * @param num Number to be printed
	 * @param ndigits  Width of number 
	 * @param x X Coordinate in cm
	 * @param y Y Coordinate in cm
	 * @param color Color to be used
	 */
	protected void putNumber(PdfContentByte cb, int num, int ndigits, float x, float y, Color color)
	{
		putNumber(cb, num, ndigits, x, y, NORMAL, color);
	}
	
	/**
	 * Puts a number at given location on the current page
	 * @param cb PDF Content byte object
	 * @param num Number to be printed
	 * @param ndigits  Width of number 
	 * @param x X Coordinate in cm
	 * @param y Y Coordinate in cm
	 * @param fontSize Font Size in points
	 */
	protected void putNumber(PdfContentByte cb, int num, int ndigits, float x, float y, int fontSize)
	{
		putNumber(cb, num, ndigits, x, y, fontSize, BLACK);
	}
	
	/**
	 * Puts a number at given location on the current page
	 * @param cb PDF Content byte object
	 * @param num Number to be printed
	 * @param ndigits  Width of number 
	 * @param x X Coordinate in cm
	 * @param y Y Coordinate in cm
	 */
	protected void putNumber(PdfContentByte cb, int num, int ndigits, float x,float y)
	{
		putNumber(cb, num, ndigits, x, y, NORMAL, BLACK);
	}
	
	/**
	 * Puts a number at given location on the current page
	 * @param cb PDF Content byte object
	 * @param num Number to be printed
	 * @param ndigits  Width of number 
	 * @param x X Coordinate in cm
	 * @param y Y Coordinate in cm
	 * @param fontSize Font Size in points
	 * @param color Color to be used
	 */
	protected void putNumber(PdfContentByte cb, int num, int ndigits, float x, float y, int fontSize, Color color)
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
			cb.setColorFill(color);
			cb.moveText( x * POINTS_PER_CM, (pageHeight - y) * POINTS_PER_CM);
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

	/**
	 * Puts a number at given location on the current page
	 * @param cb PDF Content byte object
	 * @param num Number to be printed
	 * @param ndigits  Width of number 
	 * @param x X Coordinate in cm
	 * @param y Y Coordinate in cm
	 * @param fontSize Font Size in points
	 * @param color Color to be used
	 */
	protected void putNumber(PdfContentByte cb, int num, int ndigits, float x, float y, Font f)
	{
		String tmp = "" + num;
		int len = tmp.length();
		for (int i = 0; i < ndigits - len; i++)
		{
			tmp = " " + tmp;
		}

		try
		{
			BaseFont bf = f.getCalculatedBaseFont(false);
			Color color = f.color();
			if(color == null)
			{
				color = Color.black;
			}
			float fontSize = f.getCalculatedSize();
			if(fontSize < 6f)
			{
				fontSize = 6f;
			}
			else if (fontSize > 18f)
			{
				fontSize = 18f;
			}
			cb.saveState();
			cb.beginText();
			cb.setColorFill(color);
			cb.moveText( x * POINTS_PER_CM, (pageHeight - y) * POINTS_PER_CM);
			cb.setFontAndSize(bf, fontSize);
			cb.showText(tmp);
			cb.endText();
			cb.restoreState();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Puts a given text string at given location
	 * @param cb PDF content Byte object
	 * @param text Text to be printed
	 * @param x  X coordinate in cm from left
	 * @param y  Y Coordinate in cm from top
	 */
	protected void putText(PdfContentByte cb, String text, float x, float y)
	{
		putText(cb,text,x,y,NORMAL, BLACK);
	}
	
	/**
	 * Puts a given text string at given location
	 * @param cb PDF content Byte object
	 * @param text Text to be printed
	 * @param x  X coordinate in cm from left
	 * @param y  Y Coordinate in cm from top
	 * @param fontColor Font color
	 */
	protected void putText(PdfContentByte cb, String text, float x, float y,Color fontColor)
	{
		putText(cb,text,x,y,NORMAL, fontColor);
	}

	/**
	 * Puts a given text string at given location
	 * @param cb PDF content Byte object
	 * @param text Text to be printed
	 * @param x  X coordinate in cm from left
	 * @param y  Y Coordinate in cm from top
	 * @param fontSize Font Size in points
	 */
	protected void putText(PdfContentByte cb, String text, float x, float y, int fontSize)
	{
		putText(cb,text,x,y,fontSize, Color.black);
	}
	
	/**
	 * Puts a given text string at given location
	 * @param cb PDF content Byte object
	 * @param text Text to be printed
	 * @param x  X coordinate in cm from left
	 * @param y  Y Coordinate in cm from top
	 * @param fontSize Font Size in points
	 * @param bf Baseline Font object to be used for fonts.
	 */
	protected void putText(PdfContentByte cb, String text, float x, float y,int fontSize, BaseFont bf)
	{
		putText(cb, text, x, y, fontSize, BLACK, bf);
	}
	
	/**
	 * Puts a given text string at given location
	 * @param cb PDF content Byte object
	 * @param text Text to be printed
	 * @param x  X coordinate in cm from left
	 * @param y  Y Coordinate in cm from top
	 * @param fontSize Font Size in points
	 * @param fontColor Font color
	 */
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

	/**
	 * Puts a given text string at given location
	 * @param cb PDF content Byte object
	 * @param text Text to be printed
	 * @param x  X coordinate in cm from left
	 * @param y  Y Coordinate in cm from top
	 * @param fontSize Font Size in points
	 * @param fontColor Font color
	 * @param bf Baseline Font object to be used for fonts.
	 */
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
			cb.moveText(x * POINTS_PER_CM, (pageHeight - y) * POINTS_PER_CM);
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
	
	/**
	 * Puts a given text string at given location
	 * @param cb PDF content Byte object
	 * @param msg Text to be printed
	 * @param x  X coordinate in cm from left
	 * @param y  Y Coordinate in cm from top
	 * @param fnt Font object to be used 
	 */
	public void putText(PdfContentByte cb, String msg, float x, float y, Font fnt)
	{
		ColumnText ct = new ColumnText(cb);
		Phrase myText = new Phrase(msg, fnt);
		BaseFont bf = fnt.getCalculatedBaseFont(false);
		x = x*POINTS_PER_CM;
		y = (pageHeight-y)*POINTS_PER_CM;
		float fntSz = fnt.getCalculatedSize();
		float w = bf.getWidthPoint(msg, fntSz) + 3;
		float asc = bf.getAscentPoint(msg, fntSz);
		float dsc = bf.getDescentPoint(msg, fntSz);

		float h = fntSz + asc + Math.abs(dsc) +3;
		//System.out.println("Asc, Dsc, fntSize, Height = " + asc + "," + dsc + ", "+ fntSz + ", " + h);
		ct.setSimpleColumn(myText, x, y, x+w, y+h, h, Element.ALIGN_LEFT);
		try
      {
	      ct.go();
      }
      catch (DocumentException e)
      {
	      e.printStackTrace();
      }
	}
	/**
	 * Prints a String with given font and angle at x, y coordinates
	 * @param cb Content Byte
	 * @param msg Message text to print
	 * @param x X coordinate
	 * @param y Y Coordinate
	 * @param fnt Font to be used
	 * @param ang Angle in degrees (counter clock-wise)
	 */
	public void putText(PdfContentByte cb, String msg, float x, float y, float ang, Font fnt)
	{

      BaseFont bf = fnt.getCalculatedBaseFont(false);
      float size = fnt.getCalculatedSize();
      cb.beginText();
      cb.setFontAndSize(bf, size);
      cb.showTextAligned(PdfContentByte.ALIGN_LEFT, msg, x*POINTS_PER_CM, (pageHeight-y)*POINTS_PER_CM, ang);
      cb.endText();
	}
}
