package gradesheet;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Watermark;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
 
    public static final String DEST = "colored_letters.pdf";
 
    public static void main(String[] args) throws IOException,
            DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(DEST));
        document.open();
        MyFonts.initFonts();
        Paragraph p = new Paragraph("SVL Narasimham", new Font(MyFonts.bfNormal, MyFonts.FONT_14, Font.BOLD, Color.RED));
        String s = "SVL Narasimham";
        Chunk c = new Chunk(s, MyFonts.fontN08);
        document.add(c);
        document.add(p);
        document.close();
    }
}