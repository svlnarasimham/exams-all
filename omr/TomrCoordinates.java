package omr;
import java.util.Enumeration;
import java.util.Properties;

import common.Coordinates;
import common.G;

public class TomrCoordinates extends Coordinates
{
	// All coordinates are in CM
	// Header table contains student details, photo, course/branch details etc.
	public float	cesigX		= 15.6f;
	public float	cesigY		= 4.5f;
	public float	cesigW		= 4.0f;
	public float	cesigH		= 0.8f;
	
	public float	barcode1X	= 4.2f;
	public float	barcode1Y	= 3.2f;
	public float	barcode1W	= 4.0f;
	public float	barcode1H	= 1.5f;
	
	public float	barcode2X	= 4.2f;
	public float	barcode2Y	= 13.6f;
	public float	barcode2W	= 4.0f;
	public float	barcode2H	= 1.5f;
	
	public float	barcode3X	= 4.4f;
	public float	barcode3Y	= 24.0f;
	public float	barcode3W	= 4.0f;
	public float	barcode3H	= 1.5f;
	
	public float	barcode4X	= 2.05f;
	public float	barcode4Y	= 17.5f;
	public float	barcode4W	= 4.0f;
	public float	barcode4H	= 1.5f;
	
	public float	slnoX			= 5.6f;
	public float	slnoY			= 1.1f;
	
	public float	htnoX			= 6.2f;
	public float	htnoY			= 3.635f;
	public float	nameX			= 7.0f;
	public float	nameY			= 4.23f;
	public float	exam1X		= 7.0f;
	public float	exam1Y		= 4.765f;
	public float	mandy1X		= 7.0f;
	public float	mandy1Y		= 5.31f;
	public float	branch1X		= 7.0f;
	public float	branch1Y		= 5.88f;
	public float	scode1X		= 7.0f;
	public float	scode1Y		= 6.48f;
	public float	sname1X		= 7.0f;
	public float	sname1Y		= 7.08f;
	public float	doe1X			= 7.0f;
	public float	doe1Y			= 7.68f;
	
	public float	exam2X		= 5.9f;
	public float	exam2Y		= 14.16f;
	public float	branch2X		= 6.2f;
	public float	branch2Y		= 14.9f;
	public float	scode2X		= 6.2f;
	public float	scode2Y		= 15.5f;
	public float	sname2X		= 6.2f;
	public float	sname2Y		= 16.1f;
	
	public float	exam3X		= 6.2f;
	public float	exam3Y		= 24.9f;
	public float	branch3X		= 6.2f;
	public float	branch3Y		= 25.46f;
	public float	scode3X		= 6.2f;
	public float	scode3Y		= 26.06f;
	public float	sname3X		= 6.2f;
	public float	sname3Y		= 26.66f;
	
	public float	exam4X		= 1.8f;
	public float	exam4Y		= 12.05f;
	public float	scode4X		= 2.2f;
	public float	scode4Y		= 11.95f;
	public float	sname4X		= 2.6f;
	public float	sname4Y		= 12.05f;
	
	public float	photoX		= 13.5f;
	public float	photoY		= 8.5f;
	public float	photoW		= 2.3f;
	public float	photoH		= 2.3f;
	
	public float	perf1Y		= 9.1f;
	public float	perf2Y		= 19.4f;
	
	public TomrCoordinates()
	{
		// loadCoordinates();
	}
	
	/**
	 * Load coordinates from cover-coordinates.ini file (in config folder)
	 */
	public void loadCoordinates()
	{
		G.initialize(); // try to load global data if not already loaded for safety.
		String fnm = G.basePath + G.pathSep + G.configFolder + G.pathSep
				+G.EXTERNAL_OMR_COORD_FILE;
	
					System.out.println("File: " + fnm);
		Properties p = utility.Utilities.loadProperties(fnm);
		if(p.getProperty("ERROR") != null) // Error property set, hence can not
														// continue
		{
			System.out.println("Can not load values from:" + fnm);
			return;
		}
		// get all keys into an enumeration
		Enumeration<Object> keys = p.keys();
		
		// Get each key and assign it to the corresponding variable
		float val;
		String key;
		keys = p.keys();
		while (keys.hasMoreElements())
		{
			key = (String) keys.nextElement();
			val = utility.Utilities.parseFloat(p.getProperty(key));
			
			// Set variables according to the keys //////////////
			if(key.equalsIgnoreCase("pageHeight"))
			{
				pageHeight = val;
			}
			else if(key.equalsIgnoreCase("pageWidth"))
			{
				pageWidth = val;
			}
			else if(key.equalsIgnoreCase("offsetX"))
			{
				offsetX = val;
			}
			else if(key.equalsIgnoreCase("offsetY"))
			{
				offsetY = val;
			}
			else if(key.equalsIgnoreCase("cesigX"))
			{
				cesigX = val;
			}
			else if(key.equalsIgnoreCase("cesigY"))
			{
				cesigY = val;
			}
			else if(key.equalsIgnoreCase("cesigW"))
			{
				cesigW = val;
			}
			else if(key.equalsIgnoreCase("cesigH"))
			{
				cesigH = val;
			}
			else if(key.equalsIgnoreCase("barcode1X"))
			{
				barcode1X = val;
			}
			else if(key.equalsIgnoreCase("barcode1Y"))
			{
				barcode1Y = val;
			}
			else if(key.equalsIgnoreCase("barcode1W"))
			{
				barcode1W = val;
			}
			else if(key.equalsIgnoreCase("barcode1H"))
			{
				barcode1H = val;
			}
			else if(key.equalsIgnoreCase("barcode2X"))
			{
				barcode2X = val;
			}
			else if(key.equalsIgnoreCase("barcode2Y"))
			{
				barcode2Y = val;
			}
			else if(key.equalsIgnoreCase("barcode2W"))
			{
				barcode2W = val;
			}
			else if(key.equalsIgnoreCase("barcode2H"))
			{
				barcode2H = val;
			}
			else if(key.equalsIgnoreCase("barcode3X"))
			{
				barcode3X = val;
			}
			else if(key.equalsIgnoreCase("barcode3Y"))
			{
				barcode3Y = val;
			}
			else if(key.equalsIgnoreCase("barcode3W"))
			{
				barcode3W = val;
			}
			else if(key.equalsIgnoreCase("barcode3H"))
			{
				barcode3H = val;
			}
			else if(key.equalsIgnoreCase("barcode4X"))
			{
				barcode4X = val;
			}
			else if(key.equalsIgnoreCase("barcode4Y"))
			{
				barcode4Y = val;
			}
			else if(key.equalsIgnoreCase("barcode4W"))
			{
				barcode4W = val;
			}
			else if(key.equalsIgnoreCase("barcode4H"))
			{
				barcode4H = val;
			}
			else if(key.equalsIgnoreCase("slnoX"))
			{
				slnoX = val;
			}
			else if(key.equalsIgnoreCase("slnoY"))
			{
				slnoY = val;
			}
			else if(key.equalsIgnoreCase("htnoX"))
			{
				htnoX = val;
			}
			else if(key.equalsIgnoreCase("htnoY"))
			{
				htnoY = val;
			}
			else if(key.equalsIgnoreCase("nameX"))
			{
				nameX = val;
			}
			else if(key.equalsIgnoreCase("nameY"))
			{
				nameY = val;
			}
			else if(key.equalsIgnoreCase("exam1X"))
			{
				exam1X = val;
			}
			else if(key.equalsIgnoreCase("exam1Y"))
			{
				exam1Y = val;
			}
			else if(key.equalsIgnoreCase("mandy1X"))
			{
				mandy1X = val;
			}
			else if(key.equalsIgnoreCase("mandy1Y"))
			{
				mandy1Y = val;
			}
			else if(key.equalsIgnoreCase("branch1X"))
			{
				branch1X = val;
			}
			else if(key.equalsIgnoreCase("branch1Y"))
			{
				branch1Y = val;
			}
			else if(key.equalsIgnoreCase("scode1X"))
			{
				scode1X = val;
			}
			else if(key.equalsIgnoreCase("scode1Y"))
			{
				scode1Y = val;
			}
			else if(key.equalsIgnoreCase("sname1X"))
			{
				sname1X = val;
			}
			else if(key.equalsIgnoreCase("sname1Y"))
			{
				sname1Y = val;
			}
			else if(key.equalsIgnoreCase("doe1X"))
			{
				doe1X = val;
			}
			else if(key.equalsIgnoreCase("doe1Y"))
			{
				doe1Y = val;
			}
			else if(key.equalsIgnoreCase("exam2X"))
			{
				exam2X = val;
			}
			else if(key.equalsIgnoreCase("exam2Y"))
			{
				exam2Y = val;
			}
			else if(key.equalsIgnoreCase("branch2X"))
			{
				branch2X = val;
			}
			else if(key.equalsIgnoreCase("branch2Y"))
			{
				branch2Y = val;
			}
			else if(key.equalsIgnoreCase("scode2X"))
			{
				scode2X = val;
			}
			else if(key.equalsIgnoreCase("scode2Y"))
			{
				scode2Y = val;
			}
			else if(key.equalsIgnoreCase("sname2X"))
			{
				sname2X = val;
			}
			else if(key.equalsIgnoreCase("sname2Y"))
			{
				sname2Y = val;
			}
			else if(key.equalsIgnoreCase("exam3X"))
			{
				exam3X = val;
			}
			else if(key.equalsIgnoreCase("exam3Y"))
			{
				exam3Y = val;
			}
			else if(key.equalsIgnoreCase("branch3X"))
			{
				branch3X = val;
			}
			else if(key.equalsIgnoreCase("branch3Y"))
			{
				branch3Y = val;
			}
			else if(key.equalsIgnoreCase("scode3X"))
			{
				scode3X = val;
			}
			else if(key.equalsIgnoreCase("scode3Y"))
			{
				scode3Y = val;
			}
			else if(key.equalsIgnoreCase("sname3X"))
			{
				sname3X = val;
			}
			else if(key.equalsIgnoreCase("sname3Y"))
			{
				sname3Y = val;
			}
			else if(key.equalsIgnoreCase("exam4X"))
			{
				exam4X = val;
			}
			else if(key.equalsIgnoreCase("exam4Y"))
			{
				exam4Y = val;
			}
			else if(key.equalsIgnoreCase("scode4X"))
			{
				scode4X = val;
			}
			else if(key.equalsIgnoreCase("scode4Y"))
			{
				scode4Y = val;
			}
			else if(key.equalsIgnoreCase("sname4X"))
			{
				sname4X = val;
			}
			else if(key.equalsIgnoreCase("sname4Y"))
			{
				sname4Y = val;
			}
			else if(key.equalsIgnoreCase("photoX"))
			{
				photoX = val;
			}
			else if(key.equalsIgnoreCase("photoY"))
			{
				photoY = val;
			}
			else if(key.equalsIgnoreCase("photoW"))
			{
				photoW = val;
			}
			else if(key.equalsIgnoreCase("photoH"))
			{
				photoH = val;
			}
			else if(key.equalsIgnoreCase("perf1Y"))
			{
				perf1Y = val;
			}
			else if(key.equalsIgnoreCase("perf2Y"))
			{
				perf2Y = val;
			}
		}
	}
	public static void main(String[] args)
	{
		TomrCoordinates l = new TomrCoordinates();
		l.loadCoordinates();
		System.out.println(l.photoH);
	}
}
