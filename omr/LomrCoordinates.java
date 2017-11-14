package omr;
import java.util.Enumeration;
import java.util.Properties;

import common.Coordinates;
import common.G;

public class LomrCoordinates extends Coordinates
{
	// All coordinates are in CM
	// Header table Exam/course/branch details etc.

	public float course1X = 2.1f; 
	public float course1Y = 1.70f; 
	public float exam1Y   = 2.07f;
	public float mandy1Y  = 2.49f;
	public float branch1Y = 2.88f;
	public float doe1Y    = 3.75f;
	public float scode1Y  = 3.79f;
	
	public float course2X = 8.8f; 
	public float course2Y = 1.68f; 
	public float exam2Y   = 2.28f;
	public float mandy2Y  = 2.46f;
	public float branch2Y = 2.98f;
	public float doe2Y    = 3.75f;
	public float scode2Y  = 3.86f;
	
	public float leftHT1X = 0.9f;
	public float leftHT1Y = 6.1f;
	public float leftHT2Y = 10.1f;
	public float leftHT3Y = 14.8f;
	public float leftHT4Y = 19.5f;
	public float vspace   = 0.85f;
	
	public float rightHT1X = 8.5f;
	public float rightHT1Y = 5.05f;
	public float rightHT2Y = 9.85f;
	public float rightHT3Y = 14.55f;
	public float rightHT4Y = 19.35f;
	public float hspace    = 2.1f;
	
	public  float slnoX = 1.5f;
	public  float slnoY = 26.0f;
	
	public  float barcode1X = 13.5f;
	public  float barcode1Y = 26.5f;
	public  float barcode1W = 4.0f;
	public  float barcode1H = 2.0f;
	
	public boolean dataLoaded = false;
	
	public  LomrCoordinates()
	{
		//loadCoordinates();
	}

	/**
	 *  Load coordinates from cover-coordinates.ini file (in config folder) 
	 */
	public void loadCoordinates()
	{
		G.initialize(); // try to load global data if not already loaded for safety.
		//String fnm = G.basePath+G.pathSep+G.configFolder + G.pathSep+"lomr-coordinates.ini";
		String fnm = "/home/qis/workspace/qis/lomr-coordinates.ini";
		Properties p = utility.Utilities.loadProperties(fnm);
		if(p.getProperty("ERROR") != null) // Error property set, hence can not continue
		{
			return;
		}
		// get all keys into an enumeration
		Enumeration<Object> keys = p.keys();

		// Get each key and assign it to the corresponding variable
		float val;
		String key;
		keys = p.keys();
		while(keys.hasMoreElements())
		{
			key  = (String) keys.nextElement(); 
			val = utility.Utilities.parseFloat(p.getProperty(key));
			
			// Set variables according to the keys //////////////
			if(key.equalsIgnoreCase("pageHeight")) {
				pageHeight = val;
			}
			else if(key.equalsIgnoreCase("pageWidth")){
				pageWidth = val;
			} 
			else if(key.equalsIgnoreCase("offsetX")){
				offsetX = val;
			} 
			else if(key.equalsIgnoreCase("offsetY")){
				offsetY = val;
			} 
			else if(key.equalsIgnoreCase("course1X")){
				course1X = val;
			}
			else if(key.equalsIgnoreCase("course1Y")){
				course1Y = val;
			}
			else if(key.equalsIgnoreCase("exam1Y")){
				exam1Y = val;
			}
			else if(key.equalsIgnoreCase("mandy1Y")){
				mandy1Y = val;
			} 
			else if(key.equalsIgnoreCase("branch1Y")){
				branch1Y = val;
			}
			else if(key.equalsIgnoreCase("doe1Y")){
				doe1Y = val;
			} 
			else if(key.equalsIgnoreCase("scode1Y")){
				scode1Y = val;
			} 
			else if(key.equalsIgnoreCase("course2X")){
				course2X = val;
			}
			else if(key.equalsIgnoreCase("course2Y")){
				course2Y = val;
			}
			else if(key.equalsIgnoreCase("exam2Y")){
				exam2Y = val;
			}
			else if(key.equalsIgnoreCase("mandy2Y")){
				mandy2Y = val;
			} 
			else if(key.equalsIgnoreCase("branch2Y")){
				branch2Y = val;
			}
			else if(key.equalsIgnoreCase("doe2Y")){
				doe2Y = val;
			} 
			else if(key.equalsIgnoreCase("scode2Y")){
				scode2Y = val;
			} 
			else if(key.equalsIgnoreCase("leftHT1X")){
				leftHT1X = val;
			} 
			else if(key.equalsIgnoreCase("leftHT1Y")){
				leftHT1Y = val;
			} 
			else if(key.equalsIgnoreCase("leftHT2Y")){
				leftHT2Y = val;
			} 
			else if(key.equalsIgnoreCase("leftHT3Y")){
				leftHT3Y = val;
			} 
			else if(key.equalsIgnoreCase("leftHT4Y")){
				leftHT4Y = val;
			} 
			else if(key.equalsIgnoreCase("rightHT1X")){
				rightHT1X = val;
			} 
			else if(key.equalsIgnoreCase("rightHT1Y")){
				rightHT1Y = val;
			} 
			else if(key.equalsIgnoreCase("rightHT2Y")){
				rightHT2Y = val;
			} 
			else if(key.equalsIgnoreCase("rightHT3Y")){
				rightHT3Y = val;
			} 
			else if(key.equalsIgnoreCase("rightHT4Y")){
				rightHT4Y = val;
			} 
			else if(key.equalsIgnoreCase("barcode1X")){
				barcode1X = val;
			} 
			else if(key.equalsIgnoreCase("barcode1Y")){
				barcode1Y = val;
			} 
			else if(key.equalsIgnoreCase("barcode1W")){
				barcode1W = val;
			} 
			else if(key.equalsIgnoreCase("barcode1H")){
				barcode1H = val;
			} 
			else if(key.equalsIgnoreCase("slnoX")){
				slnoX = val;
			} 
			else if(key.equalsIgnoreCase("slnoY")){
				slnoY = val;
			} 
			else if(key.equalsIgnoreCase("vspace")){
				vspace = val;
			} 
			else if(key.equalsIgnoreCase("hspace")){
				hspace = val;
			} 
		}
		dataLoaded = true;
	}
	
	public void printData()
	{
		System.out.println("course1X: " + course1X);
		System.out.println("course1Y: " + course1Y);
		System.out.println("exam1Y: " + exam1Y);
		System.out.println("mandy1Y: " + mandy1Y);
		System.out.println("branch1Y: " + branch1Y);
		System.out.println("doe1Y: " + doe1Y);
		System.out.println("scode1Y: " + scode1Y);
		System.out.println("course2X: " + course2X);
		System.out.println("course2Y: " + course2Y);
		System.out.println("exam2Y: " + exam2Y);
		System.out.println("mandy2Y: " + mandy2Y);
		System.out.println("branch2Y: " + branch2Y);
		System.out.println("doe2Y: " + doe2Y);
		System.out.println("scode2Y: " + scode2Y);
		System.out.println("leftHT1X: " + leftHT1X);
		System.out.println("leftHT1Y: " + leftHT1Y);
		System.out.println("leftHT2Y: " + leftHT2Y);
		System.out.println("leftHT3Y: " + leftHT3Y);
		System.out.println("leftHT4Y: " + leftHT4Y);
		System.out.println("vspace: " + vspace);
		System.out.println("rightHT1X: " + rightHT1X);
		System.out.println("rightHT1Y: " + rightHT1Y);
		System.out.println("rightHT2Y: " + rightHT2Y);
		System.out.println("rightHT3Y: " + rightHT3Y);
		System.out.println("rightHT4Y: " + rightHT4Y);
		System.out.println("hspace: " + hspace);
		System.out.println("slnoX: " + slnoX);
		System.out.println("slnoY: " + slnoY);
		System.out.println("barcode1X: " + barcode1X);
		System.out.println("barcode1Y: " + barcode1Y);
		System.out.println("barcode1W: " + barcode1W);
		System.out.println("barcode1H: " + barcode1H);

	}
	public static void main(String[] args)
	{
		LomrCoordinates l = new LomrCoordinates();
		l.loadCoordinates();
		l.printData();
	}
}
