package omr;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

public class ImageTools
{
	public static final int COLOR = 0;
	public static final int GRAYSCALE = 1;
	public static final int BINARY = 2;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	public static int THRESHOLD = 127;
	
	/**
	 * Sets static threshold value of this class (default is 127) 
	 * @param th Threshold for Black (Generally 120 to 150)
	 */
	public static void setBlackThreshold(int th)
	{
		THRESHOLD = th;
	}

	/**
	 * Reads an image from a given file
	 * @param fileName Image file name
	 * @return BufferedImage object or null
	 * Supported extensions are: <br><b><i>jpg|bmp|gif|png|wbmp|jpeg</i></b>
	 */
	public static BufferedImage getImage(String fileName)
	{
		File file = new File(fileName);
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(file);
		}
		catch (Exception e)
		{
			System.out.println("Error in loading Image");
			e.printStackTrace();
		}
		// Enhance Contrast and brightness
		//RescaleOp rescaleOp = new RescaleOp(1.2f, 10, null);
		//rescaleOp.filter(image, image);  // Source and destination are the same.
		return image;
	}

	/**
	 * Saves a memory image to a file in jpeg format 
	 * @param bi BufferedImage Object
	 * @param fileName File name for storing this image
	 * @return true if successful or false otherwise.
	 */
	public static boolean saveImage(BufferedImage bi, String fileName, boolean overwrite)
	{
		return saveImage(bi, fileName, "jpg", overwrite);
	}

	/**
	 * Saves a memory image to a file 
	 * @param bi BufferedImage Object
	 * @param fileName File name for storing this image
	 * @param format One of the supported format strings<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Format string can be one of <b><i>(JPG|BMP|GIF|WBMP|PNG|JPEG)</i></b><br>
	 * <i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Format can be in all uper or all lower case</i>
	 * @param overwrite Overwrites a file if true and if file already exits.
	 * @return true if successful or false otherwise.
	 */
	public static boolean saveImage(BufferedImage bi, String fileName, String format, boolean overwrite)
	{
		boolean res = true;
		File file = new File(fileName);
		if(file.exists() && !overwrite)
		{
			return false;
		}
		try
		{
			ImageIO.write(bi, format, file);		
		}
		catch (Exception e)
		{
			System.out.println("Failed to write file: " + file);
			e.printStackTrace();
			res = false;
		}
		//System.out.println("File Saved to: " + file.getAbsolutePath());
		return res;
	}


	/**
	 * Converts an image into color pixel array, using default Threshold and filter colors
	 * @param fileName Image file name
	 * @return pixel array corresponding to the image
	 */
	public static int[][] getPixelArray(String fileName)
	{
		BufferedImage img = getImage(fileName);
		return getPixelArray(img, COLOR, THRESHOLD, Color.red);
	}
	
	/**
	 * Converts an image into color pixel array using default color
	 * @param fileName Image file name
	 * @param imageType Image Type (COLOR| GRAYSCALE | BINARY)
	 * @param threshold Threshold value between 0 and 255 (127 to 150 is ideal)
	 * @return pixel array corresponding to the image
	 */
	public static int[][] getPixelArray(String fileName, int imageType, int threshold)
	{
		BufferedImage img = getImage(fileName);
		return getPixelArray(img, imageType, threshold, Color.red);
	}
	
	/**
	 * Converts an image to pixel array
	 * @param img Source BufferedImage Object
	 * @param colorType Color type required (COLOR|GRAYSCALE|BINARY)
	 * @param threshold Value below which, a color is interpreted as black<br>(Generally, 100 to 150 is ok)
	 * @param filterColor Color to be filtered (Color.red/green/blue)
	 * @return pixel array
	 */
	public static int[][] getPixelArray(BufferedImage img, int colorType, int threshold, Color filterColor)
	{
		if(img == null)
		{
			return null;
		}
		int rows = img.getHeight();
		int cols = img.getWidth();
		double rf = 0.33, gf = 0.56, bf = 0.11;
		if(filterColor.equals(Color.red))
		{
			rf = 1.0; 
			gf = 0.0;
			bf = 0.0; 
		}
		else if (filterColor.equals(Color.green))
		{
			rf = 0.0; 
			gf = 1.0;
			bf = 0.0; 
		}
		else if (filterColor.equals(Color.blue))
		{
			rf = 0.0; 
			gf = 0.0;
			bf = 1.0; 
		}
		PixelGrabber pixelGrabber = null; // Extracts pixels of current image
		int iPixData[] = new int[rows * cols]; // to store pixels from pixelgrabber
		// initializing pixelGrabber
		pixelGrabber = new PixelGrabber(img, 0, 0, img.getWidth(), img.getHeight(), iPixData, 0, img.getWidth());
		// grab pixels
		try
		{
			pixelGrabber.grabPixels();
		}
		catch (Exception e)
		{
			System.out.println("Can not grab pixels from Image..");
			e.printStackTrace();
			iPixData = null;
		}
		// Convert single dimension array into two dimensional array
		int iGrayPixdata[][] = new int[rows][cols]; // to find offset from pixel data
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				int p;
				p = iPixData[i * cols + j];
				
				if(colorType == GRAYSCALE || colorType == BINARY)
				{
					int r = 0xff & (p >> 16);
					int g = 0xff & (p >> 8);
					int b = 0xff & (p);
					p = (int) (rf * r + gf * g + bf * b);
					if(colorType == BINARY)
					{
						p = (p < threshold) ? 0 : 1;
					}
				}
				iGrayPixdata[i][j] = p;
			}
		}
		iPixData = null;
		//System.gc();
		return iGrayPixdata;
	}

	/**
	 * Converts an sub image to pixel array
	 * @param img Source BufferedImage Object
	 * @param x Start X coordinate within the image
	 * @param y Start Y coordinate 
	 * @param w Width of sub image
	 * @param h Height of sub image
	 * @return pixel array
	 */
	public static int[][] getPixelArray(BufferedImage img, int x, int y, int w, int h)
	{
		if(img == null)
		{
			return null;
		}
		PixelGrabber pixelGrabber = null; // Extracts pixels of current image
		int tmpx[] = new int[w * h]; // to store pixels from pixelgrabber
		// initializing pixelGrabber
		pixelGrabber = new PixelGrabber(img, x, y, w, h, tmpx, 0, w);
		// grab pixels
		try
		{
			pixelGrabber.grabPixels();
		}
		catch (Exception e)
		{
			System.out.println("Can not grab pixels from Image..");
			e.printStackTrace();
			tmpx = null;
			return null;
		}
		// Convert single dimension array into two dimensional array
		int px[][] = new int[h][w]; // to find offset from pixel data
		for(int i = 0; i <h ; i++)
		{
			for(int j = 0; j < w; j++)
			{
				px[i][j] = tmpx[i*w + j];
			}
		}
		tmpx = null;
		//System.gc();
		return px;
	}

	/**
	 * Rotates a copy of an image on its center.
	 * @param img The image to be rotated 
	 * @param angle The angle in degrees
	 * @return The rotated image
	 */
	public static BufferedImage rotate(BufferedImage img, double angle)
	{
	    double sin = Math.abs(Math.sin(Math.toRadians(angle)));
	    double cos = Math.abs(Math.cos(Math.toRadians(angle)));

	    int w = img.getWidth(null);
	    int h = img.getHeight(null);

	    int neww = (int) Math.floor(w*cos + h*sin);
	    int newh = (int) Math.floor(h*cos + w*sin);

	    BufferedImage bimg =  new BufferedImage(neww, newh, img.getType());
	    Graphics2D g = bimg.createGraphics();

	    g.translate((neww-w)/2, (newh-h)/2);
	    g.rotate(Math.toRadians(angle), w/2, h/2);
	    g.drawRenderedImage(img, null);
	    g.dispose();
	    return bimg;
	}
	
	/**
	 * Rotates an image
	 * @param srcImage Image to be rotated
	 * @param angle Angle in degrees (positive means counter-clockwise)
	 */
	public static void rotateImage(BufferedImage srcImage, double angle, int x, int y)
	{
		if(srcImage != null)
		{
			angle = Math.toRadians(angle);
			// create empty temporary image
			BufferedImage tmpImage = new BufferedImage(
					srcImage.getWidth(), 
					srcImage.getHeight(), 
					BufferedImage.TYPE_INT_ARGB);
			// get Graphics of the source image
			Graphics2D ggd = srcImage.createGraphics(); 
			// Create Affine Transformer
			AffineTransform atx = new AffineTransform();
			atx.rotate(angle, x, y); 
			AffineTransformOp op = new AffineTransformOp(atx, AffineTransformOp.TYPE_BILINEAR);
			op.filter(srcImage, tmpImage); // rotate srcimage into tmpimage
			ggd.drawImage(tmpImage, 0, 0, null);// write the image back to original
			ggd.dispose();
		}
	}
	
	/**
	 * Writes image pixels to a file for later analysis
	 * @param fileName Output file name
	 * @param pixels Two dimensional integer array of pixels
	 */
	public static void writeImagePixels(String fileName, int pixels[][])
	{
		try
		{
			FileWriter fw= new FileWriter(fileName);
			int rows = pixels.length;
			int cols = pixels[0].length;
			for(int i=0;i<rows;i++)
			{
				for(int j=0;j<cols; j++)
				{
					fw.write(pixels[i][j]+"");
				}
				fw.write("\n");
			}
			fw.flush();
			fw.close();
		}
		catch(Exception e){}
	}
	
	/**
	 * Transposes a matrix 
	 * @param pixels Original two dimensional matrix
	 * @return resultant transposed two dimensional matrix
	 */
	public static int[][] transposeMatrix(int pixels[][])
	{
		int pxl[][] = new int[pixels[0].length][pixels.length];
		for(int i=0;i<pixels.length; i++)
		{
			for(int j=0;j<pixels[0].length; j++)
			{
				pxl[j][i] = pixels[i][j];
			}
		}
		return pxl;
	}
	
	/**
	 * Gets a sub matrix from a binary matrix with optional transpose and converts it to black and white
	 * @param pixels Original  matrix with binary values
	 * @param x start x coordinate
	 * @param y start y coordinate
	 * @param w width of sub matrix
	 * @param h height of sub matrix
	 * @param transpose true if transpose is required (90 degrees rotation of image)
	 * @return sub matrix
	 */
	public static int[][] getSubArray(int pixels[][], int x, int y, int w, int h, boolean transpose)
	{
		int px[][];
		int white = 255<<16|255<<8|255; // convert value 1 to 24bit white color value
		if(transpose)
		{ 
			px = new int [w][h];
		}
		else
		{
			px = new int[h][w];
		}
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w; j++)
			{
				if(transpose)
				{
					px[j][i] = pixels[y+i][x+j];
					if(px[j][i] > 0) px[j][i] = white;
				}
				else
				{
					px[i][j] = pixels[y+i][x+j];
					if(px[i][j] > 0) px[i][j] = white;
				}
				
			}
		}
		return px;
	}

	/**
	 * Converts an array of pixels into a BufferedImage
	 * @param pixels Pixel array of data
	 * @return BufferedImage object created from this data
	 */
	public static BufferedImage arrayToImage(int pixels[][])
	{
		int width = pixels[0].length;
		int height = pixels.length;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				bi.setRGB(j, i, pixels[i][j]);
			}
		}
		return bi;
	}
	
	/**
	 * Adds Text to the image with a given color and size <br>
	 * at given (x,y) coordinates with vertical or horizontal orientation
	 * @param sourceFile Source image file name
	 * @param targetFile target image file name
	 * @param text Text to be added
	 * @param x X coordinate where x is to be added
	 * @param y Y coordinate where y is to be added
	 * @param size Font Size in points
	 * @param color Color to be used 
	 * @param rotationAngle Rotation angle in degrees in counter-clockwise (0,90,180, 270(-90))
	 * 
	 * <b>Note:</b><p>
	 * For -90<sup>0</sup> or 270<sup>0</sup> text, coordinates are (-y, x) where x,y are standard coordinates<br>
	 * For 90<sup>0</sup> text, coordinates are (y, -x)<br>
	 * For 180<sup>0</sup> text, coordinates are (-x, -y)
	 */
	public static void addTextToImage(
			String sourceFile, 
			String targetFile, 
			String text, 
			int x, 
			int y, 
			int size, 
			Color color, 
			int rotationAngle,
			boolean overwrite)
	{
		Font f = new Font("Roman", Font.BOLD, size);
		BufferedImage bi = getImage(sourceFile);
		Graphics2D gd = (Graphics2D)bi.getGraphics();
		AffineTransform originalAft = gd.getTransform();
		
		AffineTransform tmp = new AffineTransform();
		tmp.rotate(Math.toRadians(rotationAngle));
		gd.setTransform(tmp);
		gd.setFont(f);
		gd.setColor(color);
		gd.drawString(text, x,y);
		gd.setTransform(originalAft);
		saveImage(bi, targetFile, overwrite);
		gd.dispose();
	}

	/**
	 * Adds Text to the image with a given color and size <br>
	 * at given (x,y) coordinates with vertical or horizontal orientation
	 * @param sourceImage Source image
	 * @param targetFile target image file name
	 * @param text Text to be added
	 * @param x X coordinate where x is to be added
	 * @param y Y coordinate where y is to be added
	 * @param size Font Size in points
	 * @param color Color to be used 
	 * @param rotationAngle Rotation angle in degrees in counter-clockwise (0,90,180, 270(-90))
	 * 
	 * <b>Note:</b><p>
	 * For -90<sup>0</sup> or 270<sup>0</sup> text, coordinates are (-y, x) where x,y are standard coordinates<br>
	 * For 90<sup>0</sup> text, coordinates are (y, -x)<br>
	 * For 180<sup>0</sup> text, coordinates are (-x, -y)
	 */
	public static void addTextToImage(
			BufferedImage sourceImage, 
			String targetFile, 
			String text, 
			int x, 
			int y, 
			int size, 
			Color color, 
			int rotationAngle,
			boolean overwrite)
	{
		Font f = new Font("Roman", Font.BOLD, size);
		Graphics2D gd = (Graphics2D)sourceImage.getGraphics();
		AffineTransform originalAft = gd.getTransform();
		
		AffineTransform tmp = new AffineTransform();
		tmp.rotate(Math.toRadians(rotationAngle));
		gd.setTransform(tmp);
		gd.setFont(f);
		gd.setColor(color);
		gd.drawString(text, x,y);
		gd.setTransform(originalAft);
		saveImage(sourceImage, targetFile, overwrite);
		gd.dispose();
	}

	public static void main(String[] args)
	{
		// Create a graphics image from an array of values
		int color, c1, c2, c3, c4;
		c1 = 255<<16;
		c2 = 255<<8;
		c3 = 255;
		c4 = 255<<8 | 255;
		int x[][] = new int[100][100];
		for(int i=0;i<100;i++)
		{
			for(int j=0; j<100; j++)
			{
				
				if(i<50)
				{
					color = (j<50)?c1:c2;
				}
				else 
				{
					color = (j<50)?c3:c4;
				}
				x[i][j] = color;
			}
		}
		
		BufferedImage bi = arrayToImage(x); // create image from int array
		saveImage(bi, "svltestfile.jpg", "jpg", true);
		
		//addTextToImage("qis-tomr-0.jpg", "addtext1.jpg", "SVLN", -150, -200, 14, Color.red, 180);
		//		String []x = ImageIO.getReaderFormatNames();
		//		System.out.println("Supported Formats:");
		//		for(int i=0; i< x.length;i++)
		//		{
		//			System.out.println(x[i]);
		//		}
		//		x= ImageIO.getReaderFileSuffixes();
		//		System.out.println("Supported Extensions:");
		//		for(int i=0; i< x.length;i++)
		//		{
		//			System.out.println(x[i]);
		//		}
		
	}
}
