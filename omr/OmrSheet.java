package omr;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class OmrSheet
{
	public static final int UP = -1;
	public static final int DOWN = 1;
	public static final int LEFT = -1;
	public static final int RIGHT = 1;
	public static final int MIN_REF_BOX_SIDE = 10;
	public static int THRESHOLD = 140;
	
	public String barcodeImageFolder = "barcode";
	public String outputFolder = "output";
	public String basePath = "";
	public String subCode = "";
	
	int dpi = 150;
	int blackThreshold = 140;
	
	int barcodeStartX = 0;
	int barcodeStartY = 0;
	int barcodeWidth  = 100;
	int barcodeHeight = 100;
	
	int barcodeTextAngle = 0;
	int barcodeTextX = 0;
	int barcodeTextY = 0;
	
	int refBlockWidth = 5;
	int refBlockHeight= 5;
	int refSearchX    = 100;
	int refSearchY    = 150;
	
	int bubbleWidth   = 10;
	int bubbleHeight  = 10;
	int thresholdLow  = 20;
	int thresholdHigh = 35;
	
	int trackLength   = 100;
	int totalTracks     = 10;
	float trackPitch    = 10.0f;

	int totalFields     = 1;
	int totalColumns    = 1;
	
	int refX;
	int refY;
	float tiltAngle;
	
	OmrField fields[];
	int columnOffsets[];
	String barcode;
	private utility.MyConsole console;
	private boolean consoleOpen;
	int data[][]; // Global Data Array
	/**
	 * Constructor
	 */
	public OmrSheet()
	{
		console = new utility.MyConsole();
		consoleOpen = false;
	}
	
	/**
	 * Opens a console
	 */
	public void openConsole()
	{
		if(!consoleOpen)
		{ 
			console.open();
			consoleOpen = true;
		}
	}
	
	/**
	 * Enables the close button of the console window, if console is opened.
	 */
	public void closeConsole()
	{
		if(consoleOpen)
		{
			console.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			consoleOpen = false;
		}
	}
	
	/**
	 * Appends a line of text to the opened console
	 * @param str Input String to be appended.
	 */
	public void println(String str)
	{
		if(consoleOpen)
		{
			console.println(str);
		}
	}
	
	/**
	 * Load coordinates from cover-coordinates.ini file (in config folder)
	 */
	public void loadSheetDetails(String filename)
	{
		Properties p = new Properties();
		FileReader fr = null;
		//String fnm = G.basePath + G.pathSep + G.configFolder + G.pathSep + filename;
		try
		{
			fr = new FileReader(filename);
			p.load(fr);
			fr.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if(p.getProperty("ERROR") != null) // Error property set, hence can not continue
		{
			return;
		}
		// get all keys into an enumeration
		Enumeration<Object> keys = p.keys();
		
		// Get each key and assign it to the corresponding variable
		String key, v;
		int ncol, nfld;
		keys = p.keys();
		
		// First, Get Column and field counts. Then only proceed to other values
		v = p.getProperty("omr.TotalColumns");
		if(v == null)
		{
			System.out.println("Can not find field 'TotalColumns'");
			System.out.println("This field is mandatory");
			return;
		}
		ncol = val(v);
		if(ncol == 0)
		{
			System.out.println("TotalColumns can not be zero");
			return;
		}
		totalColumns = ncol;
		columnOffsets = new int[ncol];
		
		v = p.getProperty("omr.TotalFields");
		if(v == null)
		{
			println("Can not find field 'TotalFields'");
			println("This field is mandatory");
			return;
		}
		nfld = val(v);
		if(nfld == 0)
		{
			println("TotalFields can not be zero");
			return;
		}
		totalFields = nfld;
		fields = new OmrField[nfld];
		int fldcnt = 0;
		// Field and column counts are set hence, proceed with other variables
		while (keys.hasMoreElements())
		{
			key = (String) keys.nextElement();
			v = p.getProperty(key);
			
			// Set variables according to the keys //////////////
			if(key.equalsIgnoreCase("omr.DPI"))
			{
				dpi = val(v);
			}
			else if(key.equalsIgnoreCase("omr.BlackThreshold"))
			{
				blackThreshold = val(v);
			}
			else if(key.equalsIgnoreCase("omr.BarcodeStartX"))
			{
				barcodeStartX = val(v);
			}
			else if(key.equalsIgnoreCase("omr.BarcodeStartY"))
			{
				barcodeStartY = val(v);
			}
			else if(key.equalsIgnoreCase("omr.BarcodeWidth"))
			{
				barcodeWidth = val(v);
			}
			else if(key.equalsIgnoreCase("omr.BarcodeHeight"))
			{
				barcodeHeight = val(v);
			}
			else if(key.equalsIgnoreCase("omr.BarcodeTextAngle"))
			{
				barcodeTextAngle = val(v);
			}
			else if(key.equalsIgnoreCase("omr.BarcodeTextX"))
			{
				barcodeTextX = val(v);
			}
			else if(key.equalsIgnoreCase("omr.BarcodeTextY"))
			{
				barcodeTextY = val(v);
			}
			else if(key.equalsIgnoreCase("omr.RefBlockWidth"))
			{
				refBlockWidth = val(v);
			}
			else if(key.equalsIgnoreCase("omr.RefBlockHeight"))
			{
				refBlockHeight = val(v);
			}
			else if(key.equalsIgnoreCase("omr.RefSearchX"))
			{
				refSearchX = val(v);
			}
			else if(key.equalsIgnoreCase("omr.RefSearchY"))
			{
				refSearchY = val(v);
			}
			else if(key.equalsIgnoreCase("omr.BubbleWidth"))
			{
				bubbleWidth = val(v);
			}
			else if(key.equalsIgnoreCase("omr.BubbleHeight"))
			{
				bubbleHeight = val(v);
			}
			else if(key.equalsIgnoreCase("omr.ThresholdLow"))
			{
				thresholdLow = val(v);
			}
			else if(key.equalsIgnoreCase("omr.ThresholdHigh"))
			{
				thresholdHigh = val(v);
			}
			else if(key.equalsIgnoreCase("omr.TotalTracks"))
			{
				totalTracks = (int)val(v);
			}
			else if(key.equalsIgnoreCase("omr.TotalTrackLength"))
			{
				trackLength = val(v);
			}
			else if(key.equalsIgnoreCase("omr.TotalFields"))
			{
				// Already used before, hence nothing need to be done
			}
			else if(key.toLowerCase().startsWith("omr.field_"))
			{
				String arr[] = key.split("_");
				int idx = (int) val(arr[1]);
				fields[idx] = new OmrField();
				arr = v.split(":");
				for(int i=0;i<arr.length; i++)
				{
					String arr1[] = arr[i].split(",");
					int track = (int)val(arr1[0]);
					int column= (int)val(arr1[1]);
					String value = arr1[2].trim();
					fields[idx].add(new Bubble(track, column, value));
				}
				fldcnt++;
			}
			else if(key.equalsIgnoreCase("omr.TotalColumns"))
			{
				// Already used before, hence nothing need to be done
			}
			else if(key.equalsIgnoreCase("omr.ColumnOffsets"))
			{
				String arr[] = v.split(",");
				if(arr.length != totalColumns)
				{
					System.out.println("TotalColumns and columns defined are not matching");
					System.out.println("Expected: " + totalColumns + " Obtained: " + arr.length);
					System.exit(1);
				}
				for(int i=0; i<arr.length; i++)
				{
					columnOffsets[i] = val(arr[i]);
				}
			}
			else
			{
				println("Unknown Key: " + key);
			}
		}
		if(fldcnt != totalFields)
		{
			System.out.println("TotalFields and fields defined are not matching");
			System.out.println("Expected: " + totalFields + " Obtained: " + fldcnt);
			System.exit(1);
		}
		if(totalTracks > 0)
		{
			trackPitch = ((float)trackLength)/totalTracks;
		}
	}
	/**
	 * Converts a string into an integer
	 * @param x value string
	 * @return value of the string
	 */
	public int val(String x)
	{
		int val = 0;
		try
		{
			val = Integer.parseInt(x.trim()); 
		}
		catch(Exception e) {}
		return val;
	}
	
	/**
	 * Computes the track y coordinate in pixels from reference block
	 * @param track Track number (zero based)
	 * @return track y coordinate in pixels from the reference block
	 */
	public int getTrackY(int track)
	{
		int y = Math.round((refBlockHeight/2.0f) + (trackPitch*track));
		return y;
	}
	
	/**
	 * Computes black pixel percentage 
	 * @param data Binary data array of the image
	 * @param x Bubble center's X coordinate in pixels
	 * @param y Bubble center's Y coordinate in pixels
	 * @param bubbleWidth Bubble width in pixels
	 * @param bubbleHeight Bubble height in pixels
	 * @return percentage of black bubbles in the rectangle
	 */
	private int getBubbleBlackPixelPercentage(int data[][], int x, int y, int bubbleWidth, int bubbleHeight)
	{
		//System.out.println("Bubble: " + x + ", " + y + ", " + bubbleWidth + "," + bubbleHeight);
		int cnt=0;
		int startx = x - bubbleWidth/2;
		int starty = y - bubbleHeight/2;
		int endx = x + bubbleWidth/2;
		int endy = y + bubbleHeight/2;
		int totalPixels = (endx-startx+1)*(endy-starty+1);
		for(int i = startx; i<endx; i++)
		{
			for(int j= starty; j< endy; j++)
			{
				if(data[j][i] == 0)cnt++;
			}
		}
		int percent = Math.round(cnt*100f/totalPixels);
		return percent;
	}
	
	/**
	 * Prints a point x,y coordinates along with a given message
	 * @param message Message to be prefixed
	 * @param pt Point object
	 */
	public void printPoint(String message, Point pt)
	{
		System.out.println(message + ": (" + ((int)pt.getX()) + ", " + ((int)pt.getY()) + ")");
	}

	/**
	 * Finds the corner of the reference box
	 * @param data Integer pixel array of the image
	 * @param searchStartX X coordinate where the search begins (give sufficient extra value)
	 * @param searchStartY Y coordinate where the search begins (give sufficient extra value)
	 * @param width Width of search area (searches from searchStartX to searchStartX &plusmn; width
	 * @param height Height of search area (searches from searchStartY to searchStartY &plusmn; width
	 * @param hdir Horizontal direction of search (RIGHT (1) or LEFT (-1))
	 * @param vdir Vertical Direction of search (UP (-1) or DOWN (1))
	 * @param minBlackWidth Minimum black width of the reference box (generally MIN_REF_BOX_SIDE)
	 * @param minBlackHeight Minimum black height of the reference box (generally MIN_REF_BOX_SIDE)
	 * @return
	 */
	public Point findCorner(int data[][], int searchStartX, int searchStartY, int width, int height, int hdir, int vdir, int minBlackWidth, int minBlackHeight)
	{
		int ystart = searchStartY;
		int xstart = searchStartX;
		int dy = height-minBlackHeight-1;
		int dx = width-minBlackWidth-1;
		boolean found;
		int ix, iy, pty = -1, ptx = -1, cornerX = -1, cornerY = -1;
		// Scan horizontally for each line and find any black line of min length
		// The first row that contain black line is Y Coordinate

		// Find X on which the block starts
		found = false;
		for(iy = 0; iy < dy; iy++) // for dy rows 
		{
			if(found)
			{
				break;
			}
			pty = ystart + iy * vdir;
			for(ix=0; ix < dx; ix++) // for delta width Cols
			{
				if(found)
				{
					cornerX = ptx;
					cornerY = pty;
					break;
				}
				ptx = xstart + ix * hdir;
				found = isCorner(data, ptx, pty, hdir, vdir, MIN_REF_BOX_SIDE, MIN_REF_BOX_SIDE);
				// check for horizontal black line
			} // for each column (j)
		} // for each row (i)
		return new Point(cornerX, cornerY);
	}
	
	/**
	 * Checks if a point is a corner by checking horizontally and vertically for black pixels
	 * @param data Image pixel data
	 * @param x Point x coordinate
	 * @param y Point y coordinate
	 * @param hdir Horizontal direction
	 * @param vdir Vertical direction
	 * @param xlen width of black pixels required
	 * @param ylen height of black pixels required 
	 * @return true if it is an edge or false otherwise
	 */
	public boolean isCorner(int data[][], int x, int y, int hdir, int vdir, int xlen, int ylen)
	{
		int k;
		for(k=0;k<xlen; k++)
		{
			if(data[y][x+k*hdir] == 1) return false;
		}
		for(k=0;k<ylen;k++)
		{
			if(data[y+k*vdir][x] == 1) return false;
		}
		return true;
	}
	/**
	 * Gets Reference point Coordinates<br>
	 * It also finds the angle of rotation of image<br>
	 * If angle of rotation is greater than acceptable limits, it rotates the image<br>
	 * If image is rotated, it saves the image to the given file.
	 * @param data Pixel data array
	 * @param bi BufferedImage object
	 * @return Point of the Top left corner or (-1,-1)
	 */
	public Point getReferencePoint(BufferedImage bi)
	{
		int srchW,srchH;
		//srchW = Math.max(refBlockWidth, refSearchX-2*refBlockWidth); // up to almost left edge
		srchW = Math.min(dpi, refSearchX-2*refBlockWidth); // up to almost left edge
		srchH = Math.min(refSearchY-2*refBlockHeight, dpi); // search up to 1 inch vertically
		ImageTools.setBlackThreshold(blackThreshold); // set Color Threshold
		// Get Left top corner point
		Point tlp = findCorner(data, refSearchX, refSearchY, srchW, srchH, LEFT, DOWN, MIN_REF_BOX_SIDE, MIN_REF_BOX_SIDE);
		//printPoint("Top Left Corner", tlp);
		if(tlp.x == -1 || tlp.y == -1) // top left point not found
		{
			println("Can not find top left Reference Box");
			data = null;
			return new Point(-1, -1);
		}
		// Get bottom left point
		Point blp = findCorner(data, refSearchX, Math.min((tlp.y+trackLength+dpi/4), bi.getHeight()-5), srchW, srchH+dpi/4 , LEFT, UP, MIN_REF_BOX_SIDE, MIN_REF_BOX_SIDE);
		//printPoint("Bottom Left Corner", blp);
		if(blp.x == -1 || blp.y == -1) // bottom left point not found
		{
			println("Can not find Bottom Left Reference Box");
			data = null;
			return new Point(-1,-1);
		}
		// Find tilt angle
		double theta =(Math.atan((double)(blp.x-tlp.x)/(double)(blp.y-tlp.y)))*(180/Math.PI);
		tiltAngle = (float)theta; // Set object's tilt angle to theta
		//System.out.println("Angle:" + String.format("%6.4f", theta));
		if(Math.abs(theta)>0.1) // if tilt is more than tolerable
		{ 
			ImageTools.rotateImage(bi, theta, tlp.x, tlp.y); // Rotate image
			data = null;
			data = ImageTools.getPixelArray(bi, ImageTools.BINARY, ImageTools.THRESHOLD, Color.red);
			blp = findCorner(data, refSearchX, Math.min((tlp.y+trackLength+dpi/4), bi.getHeight()-5), srchW, srchH+dpi/4 , LEFT, UP, MIN_REF_BOX_SIDE, MIN_REF_BOX_SIDE);
			//printPoint("New Bottom Left Corner", blp);
			if(blp.x == -1 || blp.y == -1)
			{
				println("Can not find Bottom Left Reference Box");
				data = null;
				return new Point(-1, -1);
			}
		}
		return tlp;
	}
	
	/**
	 * Loads a jpg omr sheet and calculates its values
	 * @param fileName File Name of the OMR sheet (.jpg file)
	 * @return Field values as a string with colon as delimiter<br>
	 * The fields are as follows<br>
	 * 0. File Name:<br>
	 * 1. barcode:<br>
	 * 2. reference point x coordinate in pixels:<br>
	 * 3. reference point y coordinate in pixels:<br>
	 * 4. Tilt Angle in degrees<br>
	 * 4. number of fields in this sheet:<br>
	 * 5. each field value separated with a colon (:)
	 */
	public String readSheet(String fileName)
	{
		String output="";
		String fnm =  basePath + "/" + fileName;
		System.out.println("Processing file: " + fnm);
		BufferedImage bi ;
		// Step 1: load image
		bi = ImageTools.getImage(fnm); // load image from rotated image
		if(bi == null)
		{
			println("Can not load image: " + fnm);
			return "";
		}

		// Step 2: Get image binary pixel values into an array 
		data = ImageTools.getPixelArray(bi, ImageTools.BINARY, ImageTools.THRESHOLD, Color.red);
		ImageTools.writeImagePixels("MyData.txt", data);
		// Step 3: Get Top left Point of the reference point
		Point tlp = getReferencePoint(bi); // file to write rotated image
		if(tlp.x == -1 || tlp.y == -1) // left corner not found
		{
			println("*** Can not find top left Reference Box from file: " + fnm +" ***");
			data = null;
			return "";
		}
		// store reference point x and y
		refX = tlp.x;  
		refY = tlp.y;
		
		// Step 4: Barcode reading part
		barcode = getBarcode(bi);
		//System.out.println("Barcode: " + barcode);
		
		if(barcode.length()>0)
		{
			String targetFile = barcodeImageFolder + "/" + fileName.substring(0, fileName.length()-4) + "-" + barcode + ".jpg";
			System.out.println("Barcode Added File: '" + targetFile + "'");
			// Uncomment below line to insert barcode in the image
			ImageTools.addTextToImage(bi, targetFile, barcode, barcodeTextX, barcodeTextY, 14, Color.red, barcodeTextAngle, false);
		}
		else
		{
			println("*** Can not read barcode from file: " + fileName + " ***");
			return "";
		}
		output += (fileName+":"); // Add File Name
		output += (barcode+":"); // Add barcode 
		output += (refX+":"); // Add reference point X
		output += (refY+":"); // Add reference point Y
		output += (String.format("%6.4f", tiltAngle) + ":"); // Add tilt angle
		
		// Compute black percentage of each bubble
		computeBubbleMarksPercentages(data);
		// Add Field Values to the output string
		output += (fields.length + ":"); // Add field count
		
		for(int i=0;i<fields.length; i++)
		{
			output += (fields[i].getValue()); // add each field
			if(i <fields.length-1)
			{
				output += ":";
			}
		}
		return output; // return output string
	}
	
	/**
	 * Computes each bubble's black percentage area with in its bounding rectangle
	 * @param data Image pixel array
	 * @param tlp Top left corner
	 */
	public void computeBubbleMarksPercentages(int data[][])
	{
		// Get bubble Mark percentages
		int i, j;
		for(i=0; i<fields.length; i++)
		{
			Vector<Bubble> v = fields[i].vbbl;
			for(j=0;j<v.size(); j++)
			{
				Bubble b = v.get(j);
				int y = Math.round(refY+refBlockHeight/2+(b.track*trackPitch));
				int x = refX+columnOffsets[b.column];
				b.percent = getBubbleBlackPixelPercentage(data, x, y, bubbleWidth, bubbleHeight);
				//System.out.println("Bubble: "+i+","+j+","+x+"," + y + "," + b.percent);
			}
		}
		return;
	}
	
	/**
	 * Gets a barcode from pixel data array
	 * @param iGrayPixelData Pixel data array
	 * @return Barcode of the image represented by the pixel array or blank.
	 */
	public String getBarcode(BufferedImage bi)
	{
		float contrast = 1.2f; // 25% extra contrast
		float brightness = 15f; // 20% extra brightness
		BufferedImage tbi = bi.getSubimage(barcodeStartX, barcodeStartY, barcodeWidth, barcodeHeight);
		//ImageTools.saveImage(tbi, "MyBarcode1.jpg", true);
		if(barcodeWidth< barcodeHeight)
		{
			tbi = ImageTools.rotate(tbi, -90);
		}
		//ImageTools.saveImage(tbi, "MyBarcode2.jpg", true);
		RescaleOp rescaleOp = new RescaleOp(contrast, brightness, null);
		rescaleOp.filter(tbi, tbi);  // Source and destination are the same.		
		String barcode = BarcodeConverter.readBarcode(tbi);
		boolean valid = true;
		for(int i=0;i<barcode.length(); i++)
		{
			if(!Character.isDigit(barcode.charAt(i)))
			{
				valid = false;
			}
		}
		
		if(barcode.equals("") || !valid) // try one more time with more contrast
		{
			//System.out.println("Checking second time...");
			tbi = bi.getSubimage(barcodeStartX, barcodeStartY, barcodeWidth, barcodeHeight);
			//ImageTools.saveImage(tbi, "MyBarcode1.jpg", true);
			if(barcodeWidth< barcodeHeight)
			{
				tbi = ImageTools.rotate(tbi, -90);
			}
			//ImageTools.saveImage(tbi, "MyBarcode2.jpg", true);
			rescaleOp = new RescaleOp(1.5f, 10f, null);
			rescaleOp.filter(tbi, tbi);  // Source and destination are the same.		
			barcode = BarcodeConverter.readBarcode(tbi);
		}
		// Try Manually if failed to read barcode..
		valid = true;
		for(int i=0;i<barcode.length(); i++)
		{
			if(!Character.isDigit(barcode.charAt(i)))
			{
				valid = false;
			}
		}
		
		if(barcode.equals("") || !valid)
		{
			BarcodeInputGUI big = new BarcodeInputGUI("Barcode Manual Entry", tbi);
			barcode = big.barcode;
		}
		valid = true;
		for(int i=0;i<barcode.length(); i++)
		{
			if(!Character.isDigit(barcode.charAt(i)))
			{
				valid = false;
			}
		}
		return (valid)? barcode : "";
	}
	
	/**
	 * Prints the data for debug
	 */
	public void showData()
	{
		System.out.println("DPI = " + dpi);
		System.out.println("Black Threshold = " + blackThreshold);
		System.out.println("barcodeStartX = " + barcodeStartX);
		System.out.println("barcodeStarty = " + barcodeStartY);
		System.out.println("barcodeWidth = " + barcodeWidth);
		System.out.println("barcodeHeight = " + barcodeHeight);
		System.out.println("barcode Text angle = " + barcodeTextAngle);
		System.out.println("barcode text x = " + barcodeTextX);
		System.out.println("barcode text y = " + barcodeTextY);
		System.out.println("refBlockWidth = " + refBlockWidth);
		System.out.println("refBlockHeight = " + refBlockHeight);
		System.out.println("refSearchX = " + refSearchX);
		System.out.println("refSearchY = " + refSearchY);
		System.out.println("bubbleWidth = " + bubbleWidth);
		System.out.println("bubbleHeight = " + bubbleHeight);
		System.out.println("thresholdLow = " + thresholdLow);
		System.out.println("thresholdHigh = " + thresholdHigh);
		System.out.println("trackPitch = " + trackPitch);
		System.out.println("trackLength = " + trackLength);
		System.out.println("totalTracks = " + totalTracks);
		System.out.println("totalColumns = " + totalColumns);
		System.out.print("[");
		for(int i=0; i< totalColumns; i++)
		{
			System.out.print(columnOffsets[i] + ", ");
		}
		System.out.println("]");
		
		System.out.println("totalFields = " + totalFields);
		for(int i=0; i<fields.length; i++)
		{
			System.out.print("Field[" + i + "] = ");
			Vector<Bubble> v = fields[i].vbbl;
			for(int j=0;j<v.size(); j++)
			{
				Bubble b = v.get(j);
				System.out.print("(" + b.track + "," + b.column + "," + b.value + "), ");
			}
			System.out.println("...");
		}
	}

	/* ********************************** OMR Field Class *************************** */
	/**
	 * OMR Field data structure, which stores data related to one field.<br>
	 * The field consists of one or more bubbles, where only one need to be marked.
	 * @author SVL Narasimham
	 *
	 */
	public class OmrField
	{
		Vector<Bubble> vbbl;
		public static final String BLANK = ".";
		public static final String ERROR = "?";
		public OmrField()
		{
			vbbl = new Vector<OmrSheet.Bubble>();
		}

		/**
		 * Gives the number of bubbles associated with this field
		 * @return Bubble Count for this field
		 */
		public int getBubbleCount()
		{
			return vbbl.size();
		}
		
		/**
		 * Get a Bubble from the field
		 * @param i index of bubble in the field
		 * @return Bubble at i<sup>th</sup> location
		 */
		public Bubble getBubble(int i)
		{
			return vbbl.get(i);
		}
		
		/**
		 * Adds a bubble to this field
		 * @param bbl Bubble to be added
		 */
		public void add(Bubble bbl)
		{
			vbbl.add(bbl);
		}
		
		/**
		 * Checks all points and returns the status of this field
		 * @return " " if not marked, marked value if marked and "?" if multiple marks are present
		 */
		public String getValue()
		{
			int cnt = 0, ambcnt = 0;
			String mrkval = new String("");
			Bubble b;
			boolean mrk1, mrk2, ok;
			for(int i=0; i<vbbl.size(); i++)
			{
				b = vbbl.get(i);
				mrk1 = (b.percent >= thresholdHigh) ? true : false;
				mrk2 = (b.percent >= thresholdLow) ? true : false;
				if(mrk1 != mrk2) ambcnt++;
				// For now, if second value is ok, consider it as marked
				ok = ((mrk1 == mrk2) ||(!mrk1 && mrk2) ) ? true : false;
				if(ok && mrk2)
				{
					cnt++;
					mrkval = vbbl.get(i).value;
				}
			}
			if(cnt == 0) // nothing marked (blank)
			{
				return BLANK;
			}
			else if(cnt == 1)// Only one value marked
			{
				// Check if ambiguity prevails
				if(ambcnt == 0)
				{
					return mrkval;
				}
				else
				{
					//return ERROR; // if low is marked and high is not marked
					return mrkval; // for now, even if only low is marked, return value 
				}
			}
			else // more than one bubble marked (hence error)
			{
				return ERROR;
			}
		}
	}
	
	/* ********************************* Bubble Data Class ********************** */
	/**
	 * Bubble data class
	 * @author SVL Narasimham
	 *
	 */
	public class Bubble
	{
		int track;  // Y distance from reference
		int column; // X distance from reference 
		String value; // Value of the bubble if marked
		int percent;
		
		public Bubble(int track, int column, String value)
		{
			this.track  = track;
			this.column = column;
			this.value  = value;
			percent = 0;
		}
	}
	
	/* ******************************* Main Function *************************** */
	public static void main(String args[])
	{
	}
	
}
