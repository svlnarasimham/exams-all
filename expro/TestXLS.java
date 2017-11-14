package expro;
import java.io.*;

import jxl.*;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class TestXLS
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
	  String s[][] = null;
	  Workbook w;
	  int rows, cols;
	  String inputFile = "marks-2012.xls";
	  System.out.println("Sheet No of ece: " + utility.ExcelTools.getSheetNumber(inputFile, "ECE"));
	  System.out.println("Sheet Name of 3: " + utility.ExcelTools.getSheetName(inputFile, 3));
	}

	  
	  public static void ArrayToExcel(String s[][], String outputFile, String sheetName) throws IOException  
	  {
		  WritableWorkbook wwb;
		  int rows, cols;
		  int i, j;
		  if(!outputFile.endsWith(".xls"))
		  {
			  outputFile += ".xls";
		  }
	    try 
	    {
	    	wwb = Workbook.createWorkbook(new File(outputFile));
	    	WritableSheet ws = wwb.createSheet(sheetName, 0);
	      rows = s.length;
	      cols = s[0].length;
		   
	      for (i = 0; i < rows; i++) 
	      {
	        for (j = 0; j < cols; j++) 
	        {
	          Label l = new Label(j,i,s[i][j]);
	          ws.addCell(l);
	        }
	      }
	      wwb.write();
	      wwb.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return;
	  }

	  public static void printArray(String s[][])
	  {
	    for(int i=0;i<s.length;i++)
	    {
	    	for(int j=0;j<s[0].length;j++)
	    	{
	    		System.out.print(s[i][j] + "     ");
	    	}
	    	System.out.println();
	    }
		  
	  }
	  

}
