package gradesheet;
import java.awt.Color;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

public class MyFonts
{
	public static final int FONT_08 = 8;
	public static final int FONT_09 = 9;
	public static final int FONT_10 = 10;
	public static final int FONT_12 = 12;
	public static final int FONT_14 = 14;
	
	public static final int LEFT = 0;
	public static final int TOP = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM=3;

	public static BaseFont bfNormal = null;
	public static BaseFont bfItalic = null; 
	public static BaseFont bfBold = null;
	public static BaseFont bfBoldItalic = null;
	
	// Normal Fonts
	public static Font fontN08 = null;
	public static Font fontN09 = null;
	public static Font fontN10 = null;
	public static Font fontN12 = null;
	public static Font fontN14 = null;
	// Bold Fonts
	public static Font fontB08 = null;
	public static Font fontB09 = null;
	public static Font fontB10 = null;
	public static Font fontB12 = null;
	public static Font fontB14 = null;
	// Italic Fonts
	public static Font fontI08 = null;
	public static Font fontI09 = null;
	public static Font fontI10 = null;
	public static Font fontI12 = null;
	public static Font fontI14 = null;
	// Bold Italic Fonts
	public static Font fontBI08 = null;
	public static Font fontBI09 = null;
	public static Font fontBI10 = null;
	public static Font fontBI12 = null;
	public static Font fontBI14 = null;
	
	public static boolean initialized = false;
	
	public static void initFonts()
	{
		if(!initialized)
		{
			try
			{
				bfNormal = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.EMBEDDED);
				bfItalic = BaseFont.createFont(BaseFont.TIMES_ITALIC, BaseFont.CP1252, BaseFont.EMBEDDED);
				bfBold   = BaseFont.createFont(BaseFont.TIMES_BOLD,  BaseFont.CP1252, BaseFont.EMBEDDED);
				bfBoldItalic = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC,  BaseFont.CP1252, BaseFont.EMBEDDED); 
				
				// Initialize fonts
				fontN08 = new Font(bfNormal, FONT_08,Font.NORMAL,Color.BLUE);
				fontN09 = new Font(bfNormal, FONT_09);
				fontN10 = new Font(bfNormal, FONT_10);
				fontN12 = new Font(bfNormal, FONT_12);
				fontN14 = new Font(bfNormal, FONT_14);
			
				fontB08 = new Font(bfBold,FONT_08);
				fontB09 = new Font(bfBold,FONT_09);
				fontB10 = new Font(bfBold,FONT_10);
				fontB12 = new Font(bfBold,FONT_12);
				fontB14 = new Font(bfBold,FONT_14);

				fontI08 = new Font(bfItalic, FONT_08);
				fontI09 = new Font(bfItalic, FONT_09);
				fontI10 = new Font(bfItalic, FONT_10);
				fontI12 = new Font(bfItalic, FONT_12);
				fontI14 = new Font(bfItalic, FONT_14); 
				
				fontBI08 = new Font(bfBoldItalic, FONT_08);
				fontBI09 = new Font(bfBoldItalic, FONT_09);
				fontBI10 = new Font(bfBoldItalic, FONT_10);
				fontBI12 = new Font(bfBoldItalic, FONT_12);
				fontBI14 = new Font(bfBoldItalic, FONT_14);
			}
			catch (Exception e)
			{
				System.out.println("Can not initialize fonts: " + e.getMessage());
			}
			initialized = true;
		}
	}
}
