package omr;
import com.google.zxing.*;
import com.google.zxing.common.*;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import java.io.*;
import java.util.*;

import javax.imageio.*;

import java.awt.image.*;

public class BarcodeConverter
{
	/**
	 * Reads a Bar Code (3 of 9 format)  
	 * @param img Image object that contains bar code 
	 * @return Text read from the bar code or empty string if barcode is not readable.
	 */
	public static String readBarcode(BufferedImage img)
	{
	    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
	    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	    BufferedImageLuminanceSource bils = new BufferedImageLuminanceSource(img);
	    BinaryBitmap bMap = new BinaryBitmap(new HybridBinarizer(bils));
	    String value = "XXXXXX";
	    try
	    {
	   	 Result res = new MultiFormatReader().decode(bMap, hintMap);
	   	 value = res.getText();
	    }
	    catch(Exception e)
	    {
	   	 value = "";
	    }
	    
		 return value;
	}
	
	public static void main(String[] args) throws Exception
	{
	    BufferedImage bi = ImageIO.read(new FileInputStream("MyBarcode1.jpg"));
	    String x = readBarcode(bi);
	    
		    System.out.println("Value: " + x);
		}
	
}
