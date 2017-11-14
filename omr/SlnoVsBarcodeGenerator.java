package omr;
import java.io.*;
import java.util.*;
import javax.swing.*;

import utility.MyDate;

public class SlnoVsBarcodeGenerator
{
	public SlnoVsBarcodeGenerator()
	{
	}
	/**
	 * Generates barcode vs serial number file, where<br>
	 * barcode is sequence number and serial numbers are shuffled.
	 * @param examCode Examination Code
	 * @param date  Date in dd-mm-yyyy format
	 * @param targetFolder Target Folder in which the file is created. (Empty means current folder)
	 * @param startSlno Start Serial Number (Max 7 digits)
	 * @param startBarcode Start Barcode Number (Max 6 digits)
	 * @param nCodes Number of codes to be generated (number of students registered for this subject)
	 */
	public void generateCodesFile(
			String examCode, 
			String date, 
			String targetFolder, 
			int startSlno, 
			int startBarcode, 
			int nCodes)
	{
		int i, x, tmp;
		int asl[] = new int[nCodes];
		int abc[] = new int[nCodes];
		String ansiDate = MyDate.getAnsiDate(date);
		int seed = utility.Utilities.parseInt(ansiDate);
		Random	rnd	= new Random(seed); // Random seed
		for(i=0;i<nCodes; i++)
		{
			asl[i] = startSlno+i;
			abc[i] = startBarcode + i;
		}
		for(i=0;i<nCodes; i++) // shuffle barcodes
		{
			x = rnd.nextInt(nCodes);
			tmp = abc[i];
			abc[i] = abc[x];
			abc[x]= tmp;
		}
		if(targetFolder.trim().equals("")) // if folder name is empty,
		{
			targetFolder = "."; // folder name is set to current folder (.)
		}
		String fileName = targetFolder + "/" + ansiDate + ".txt";
		try
		{
			FileWriter fw = new FileWriter(fileName);
			for(i=0;i<nCodes;i++)
			{
				fw.write(""+asl[i] + "\t" + abc[i] + "\n");
			}
			fw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Data Saved in File: " + fileName);
		
	}
	
	public static void main(String[] args)
	{
		SlnoVsBarcodeDialog sbd = new SlnoVsBarcodeDialog();
		SlnoVsBarcodeGenerator sbg = new SlnoVsBarcodeGenerator();
		sbd.getData();
		String ecode = sbd.getEcode();
		String date = sbd.getDate();
		String folder = sbd.getTargetFolder();
		int slno = sbd.getStartSlno();
		int barcode = sbd.getStartBarcode();
		int ncodes = sbd.getnCodes();
		if(!ecode.equals(""))
		{
			sbg.generateCodesFile(ecode, date, folder, slno, barcode, ncodes);
		}
		sbd.dispose();
	}
}
