package omr;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class BarcodeGenerator {
	public static final float CM2PT = 28.3465f;
	public static Barcode39 code39=null;
	
	/**
	 * Generates a bar code image without the human readable text under it.
	 * @param cb PDF Content Byte object
	 * @param text Text to be printed.
	 * @return Image Object with default 4cm x 1cm size.
	 */
	public static com.lowagie.text.Image getBarcode(PdfContentByte cb, String text)
	{
		return getBarcode(cb, text, false, 4.0f,1.0f);
	}
	
	/**
	 * Creates a barcode (code 39 format) from the given text
	 * @param cb PDF Content Byte
	 * @param text Text to be converted to a barcode
	 * @param printTextAlso If true, it prints the content at the bottom of barcode
	 * @param imageWidth Width in cm
	 * @param imageHeight Height in cm
	 * @return Barcode Image object.
	 */
	public static com.lowagie.text.Image getBarcode(
			PdfContentByte cb, 
			String text, 
			boolean printTextAlso,
			float imageWidth,
			float imageHeight)
	{
		if(code39 == null)
		{
			code39 = new Barcode39();
		}
      code39.setCode(text);
      if(printTextAlso)
      {
      	code39.setAltText(text);
      }
      else
      {
      	code39.setAltText("");
      }
      code39.setBarHeight(12);
      com.lowagie.text.Image img = code39.createImageWithBarcode(cb, Color.black, Color.black);
      img.scaleAbsolute(imageWidth*CM2PT, imageHeight*CM2PT);
      return img;
	}
	
    public static void main(String[] args) throws IOException,
            DocumentException {
        new BarcodeGenerator().createPdf("barcode.pdf");
    }
 
    /**
     * Creates a PDF document.
     * @param Filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document(PageSize.A4);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfContentByte cb = writer.getDirectContent();
 
        // CODE 39
        document.add(new Paragraph("Barcode 3 of 9"));
        document.add(getBarcode(cb, "15491A0101", true, 4f,1f));
        document.add(getBarcode(cb, "SVLN", false, 5f, 0.75f));
        document.add(getBarcode(cb, "TSR", true, 7.5f,1.25f));
        document.add(getBarcode(cb, "12345", true, 2.0f, 1.0f));
        document.add(getBarcode(cb, "67890", false, 3.0f,0.5f));
               
        
        // step 5
        document.close();
        System.out.println("Document created....");
    }
}
 