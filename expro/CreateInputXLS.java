package expro;
import java.io.*;
import java.util.*;
import utility.*;

public class CreateInputXLS
{
	public static int getSubNumber(String scodes[], String subCode)
	{
		int i, n;
		n = scodes.length;
		for(i=0;i<n;i++)
		{
			if(scodes[i].equalsIgnoreCase(subCode))
			{
				break;
			}
		}
		return ((i<n) ? i : -1);
	}
	
	public static void main(String[] args) throws Exception
	{
		String basePath = "C:/workspace/expro/A10R10";
		String fnm = "em-2012.xls";
		int i, j, k;
		
		String scodes[] =	
			{  "1G111",
				"1G112",
				"1G113",
				"1G114",
				"1G311",
				"1G312",
				"1G411",
				"1G511",
				"1G513",
				"1GC11",
				"1GC12",
				"1GC13",
				"1GC14",
				"1GC15",
				"1GC16",
				"1GC17"};
		// Delete old files if any
		for(i=0;i<scodes.length;i++)
		{
			String cmFile = basePath+"/" + scodes[i] + "-cm.txt";
			String chFile = basePath+"/" + scodes[i] + "-ch.txt";
			File f = new File(cmFile);
			f.delete();
			f = new File(chFile);
			if (f.exists())
			{
				f.delete();
			}
		}

		String inFile = basePath + "/" + fnm;
		int nsheets = ExcelTools.getSheetCount(inFile);
		
		for(i=0;i<nsheets;i++) // For each sheet
		{
			String s[][] = ExcelTools.ExcelToArray(inFile, i);
			
			for(j=1; j<s[0].length; j++) //for each column, 0th is htno
			{
				String sc = s[0][j];
				String cmFile = basePath+"/" + scodes[i] + "-cm.txt";
				String chFile = basePath+"/" + scodes[i] + "-ch.txt";
				FileWriter fch = new FileWriter(chFile, true);
				FileWriter fcm = new FileWriter(cmFile, true);
				System.out.println("" + (i*5+j) + ". Code: " + ((i+1)*1000) + "  Sub: " + sc );
				for(k=1;k<s.length; k++) // For each row, starting from 1st row, 0th is header
				{
					String code = "" + ((i+1)*1000 + k);
					fch.write(code + "\t" + s[k][j] + "\n"); // marks
					fcm.write(code + "\t" + s[k][0] + "\n"); // htno
				}
				fcm.close();
				fch.close();
			}
		}
	}

}
