package expro;
import java.io.*;
import java.util.*;

import common.Subject;
import utility.Utilities;

public class ImportTools
{
	public static final int IM = 0;
	public static final int EMT = 1;
	public static final int EML = 2;
	
	static boolean globalOk=false;
	
	//Import internals from an xls file
	/**
	 * Import internal marks from xls file and writes them to individual subject-wise <br>
	 * rollno-vs-marks files. Each file name will be &lt;<i>SubjectCode</i>&gt;+"-im.txt".
	 * @param xlsFile XLS File (not xlsx file) that contain internal marks.<br> 
	 * @param targetFolder Folder into which the output files should be written
	 * @return True if successful or false otherwise.
	 */
	public static boolean importInternals(String xlsFile, String targetFolder)
	{
		int i, j,k;
		boolean res = false;
		int scount = utility.ExcelTools.getSheetCount(xlsFile);
		HashSet<String> subCodes = new HashSet<String>();
		FileWriter fwi;
		//System.out.println("Processing: " + xlsFile );
		try
		{
			for(i=1;i<scount;i++) // (one per branch, 1st sheet is instructions)
			{
				String x[][];
	         x = utility.ExcelTools.ExcelToArray(xlsFile,i);
				int r = x.length;
				int c = x[0].length;
				if(c < 3 || r < 2) // Check for empty sheet
				{
					continue;
				}
				for(j=2;j<c;j++) // for each column from 1st onwards (skip 0&1 column, htno,name)
				{
					String code = x[0][j].trim().toUpperCase();
					if(code.length() == 0)
					{
						//System.out.println("Blank Code at: Page: " + i + "  Column: " + j);
						continue;
					}
					//System.out.println(i+"." + j+ ". Processing File: "+ x[0][j] + "-im.txt");
					
					// Internal
					String imfName = targetFolder + "/" + x[0][j]+"-im.txt";
					if(subCodes.contains(x[0][j])) // if already processed once, append to it
					{
						fwi = new FileWriter(imfName, true);
					}
					else // otherwise, overwrite existing file with fresh data
					{
						subCodes.add(x[0][j]);
						//System.out.println("Overwriting " + imfName);
						fwi = new FileWriter(imfName, false); // Overwrite when it is fresh code
						
					}
					for(k=1;k<r;k++)
					{
						if(x[k][0].trim().length() < 3) // Minimum length for HTNO
						{ 
							continue;
						}
						fwi.write(x[k][0] + "\t" + x[k][j]+ "\n"); //Htno \t Internal marks
					}
					fwi.close();
				}
         }
			res = true;
		}
		catch (Exception e)
		{
			Utilities.showMessage("Error in Importing Internals: " + e.getMessage());
			res = false;
		}
		return res;
	}
	
	/**
	 * Imports theory external marks from code-vs-marks and code-vs-htno files.<br/>
	 * @param cmfile Code vs Marks File that contains tab separated Code and Marks
	 * @param chfile Code vs HTNO file that contains tab separated Code and HTNOs
	 * @param emfile External Marks file where the HTNO vs Marks should be written
	 * @param chmfile File name into which Code vs HTNO vs Marks are written
	 * @param errfile Errors file where any error messages are written
	 * @param subCode Subject Code
	 * @return True if successful or false otherwise.
	 */
	public static boolean importTheoryExternals(
			String cmfile, 
			String chfile, 
			String emfile, 
			String chmfile, 
			String errfile,
			String subCode)
	{
		boolean res=true;
		
		if(!Utilities.checkFile(chfile) || !Utilities.checkFile(cmfile))
		{
			Utilities.showMessage("File: " + chfile + " (OR) " + cmfile + " not found..");
			return false;
		}
		Hashtable<String, String>hch = new Hashtable <String, String>();
		Hashtable<String, String>hcm = new Hashtable <String, String>();
		String sch[][] = utility.ExcelTools.ExcelToArray(chfile, 0);
		String scm[][] = utility.ExcelTools.ExcelToArray(cmfile, 0);
		Vector<String>errorMsgs = new Vector<String>();

		// Check Length
		int i;
		int n;
		int blankLines=0;
		// Load ch 
		n = sch.length;
		for(i=0;i<n; i++)
		{
			if(sch[i][0].trim().length() == 0) {blankLines++; continue;}
			if((hch.put(sch[i][0], sch[i][1])) != null)
			{
				errorMsgs.add("Duplicate code in file:" + chfile + ":: " + sch[i][0]);
			}
		}
		
		if(n != (hch.size()+blankLines))
		{
			Utilities.showMessage((n-hch.size()) + " Duplicates present in " + chfile + "\n" +
							"Errors are saved in file: " + errfile);
			saveErrors(errfile, errorMsgs);
			return false;
		}
		// load cm
		n = scm.length;
		blankLines = 0;
		for(i=0;i<n; i++)
		{
			if(scm[i][0].trim().length() == 0)
			{
				blankLines++;
				continue;
			}
			if((hcm.put(scm[i][0], scm[i][1])) != null)
			{
				errorMsgs.add("Duplicate code in file:" + cmfile + ":: " + scm[i][0]);
			}
		}

		if(n != (hcm.size()+blankLines))
		{
			Utilities.showMessage((n-hcm.size()) + " Duplicates present in " + cmfile+ "\n" +
					"Errors are saved in file: " + errfile);
			saveErrors(errfile, errorMsgs);
			return false;
		}
		// Check size of both
		if (hch.size()!= hcm.size())
		{
			Utilities.showMessage("Size not Same Error, File:" + chfile + ": contains " + 
					hch.size() + "records and File:" + cmfile + ": contains " + hcm.size() + "records");
			return false;
		}
		
		
		//check if all codes in sch have matches in scm
		n = sch.length;
		for (i=0;i<n;i++)
      {
			if(sch[i][0].trim().length() == 0)
			{
				blankLines++;
				continue;
			}
			if(hcm.get(sch[i][0]) == null)
			{
				errorMsgs.add(sch[i][0] +" in file " + chfile + " has no match in file " + cmfile );
			}
      }
		
		// Check if all codes in scm have matches in sch
		n = scm.length;
		for(i=0;i<n; i++)
		{
			if(scm[i][0].trim().length() == 0)
			{
				blankLines++;
				continue;
			}
			if(hch.get(scm[i][0]) == null)
			{
				errorMsgs.add(scm[i][0] +" in file " + cmfile + " has no match in file " + chfile );
			}
		}
		
		if(errorMsgs.size() > 0)
		{
			saveErrors(errfile, errorMsgs);
			Utilities.showMessage("Errors Present in matching.. See file: " + errfile + " for errors");
			return false;
		}
		
		// All matched .. So write them to Files chm and em
		try
		{
			FileWriter fem = new FileWriter(emfile);
			FileWriter fchm = new FileWriter(chmfile);
			String mks;
			for(i=0;i<n;i++)
			{
				if(sch[i][0].trim().length() == 0)
				{
					blankLines++;
					continue;
				}

				mks = hcm.get(sch[i][0]);
				fem.write(sch[i][1] + "\t" + mks + "\n");
				fchm.write(sch[i][0] + "\t" + sch[i][1] + "\t" + mks + "\n");
			}
			fem.close();
			fchm.close();
			res = true;
		}
		catch (Exception e)
		{
			Utilities.showMessage("Error in Importing: " + chmfile +", "+emfile + ": " + e.getMessage());
			e.printStackTrace();
			res = false;
		}
		//Utilities.showMessage("Subject: " + subCode + " Processed successfully");
		System.out.println("Subject: " + subCode + " Processed successfully");
		return res;
	}
	
	/**
	 * Import Practical Exam Marks from XLS file
	 * @param xlsFile XLS file (not XLSX file) that contain HTNO VS MARKS data
	 * @param targetFile HTNO VS MARKS output text file
	 * @return true if successful or false otherwise.
	 */
	public static boolean importLabExternals(String xlsFile, String targetFile)
	{
		int i;
		String x[][];

		try
      {
			x = utility.ExcelTools.ExcelToArray(xlsFile,0);
			if(x == null)
			{
				return false;
			}
			int r = x.length;
			int c = x[0].length;
			if(c!= 2)
			{
				Utilities.showMessage("External Marks File should contain only two fields\n (" 
						+ xlsFile + ") contains " + c + " fields ...");
				return false;
			}
			FileWriter fwe = new FileWriter(targetFile);
			for(i=1;i<r;i++)
			{
				if(x[i][0].trim().length() < 10) 
				{ 
					//System.out.println("skipping .. Page(" + (i+1) + ") Row(" + (k+1)+ ")");
					continue;
				}
				fwe.write(x[i][0] + "\t" + x[i][1]+ "\n"); //Htno \t Internal marks
			}
			fwe.close();
      }
      catch (Exception e)
      {
      	Utilities.showMessage("Error in Importing Lab Externals from: " + xlsFile +": " + e.getMessage());
			e.printStackTrace();
			return false;
      }
      return true;
	}

	/**
	 * Imports subjects from xls file
	 * @param xlsFname Subjects XLS file (not XLSX file)
	 * @param outFile Output subject file that contains tab separated data
	 * @return true if successful or false otherwise.
	 */
	public static boolean importSubjects(String xlsFname, String outFile)
	{
		final int FC=13;
		String arr[][] = utility.ExcelTools.ExcelToArray(xlsFname, 0);

		String rg,sc,st,tl, cc,bc;
		int y,s,mxe, mxt, mne, mnt, cr, app, pas, mod;
		int i;

		FileWriter subf=null;
		boolean res = true;
		try
		{
			subf = new FileWriter(outFile);
		}
		catch(Exception e)
		{
			Utilities.showMessage("Error in opening file: " + outFile + "(" + e.getMessage() + ")");
			return false;
		}
		for(i=1;i<arr.length;i++)
		{
			if(arr[i][0].trim().equals("")) continue; // blank lines
			if(arr[i].length == 0) continue;
			if(arr[i].length != FC)
			{
				Utilities.showMessage(
						"Subjects:loadSubjects: Field count error at around Line " + (i+1) + " "+arr[i][0]);
				continue;
			}

			// Field 0: Subject Code
			sc = arr[i][0].trim().toUpperCase();
			
			// Field 1: Subject Title
			st = arr[i][1].trim().toUpperCase();
			
			// Field 2: Course Code
			cc = arr[i][2].trim().toUpperCase();
			
			// Field 3: Branch Code
			bc = arr[i][3].trim();
			int bcn = utility.Utilities.parseInt(bc);
			if(bcn< 0)
			{
				Utilities.showMessage("Branch Code Parsing Error at line: " + (i+1));
				continue;
			}
			
			// Field 4: Regulation
			rg = arr[i][4].trim();
			
			// Field 5: Year
			y  = utility.Utilities.parseInt(arr[i][5].trim());
			if(y < 0)
			{
				String msg = "Year parsing error in line: "+(i+1);
				Utilities.showMessage(msg);
				continue;
			}
			
			// Field 6: Semester
			s  = utility.Utilities.parseInt(arr[i][6].trim());
			if(s < 0)
			{
				String msg = "Semester parsing error in line: "+ (i+1);
				Utilities.showMessage(msg);
				continue;
			}
			
			// Field 7: Maximum External Marks
			mxe = utility.Utilities.parseInt(arr[i][7].trim());
			if(mxe < 0)
			{
				String msg = "Max. Ext Marks parsing error in line: "+(i+1);
				Utilities.showMessage(msg);
				continue;
			}
			
			// Field 8: Maximum Total Marks
			mxt = utility.Utilities.parseInt(arr[i][8].trim());
			if(mxt < 0)
			{
				String msg = "Max. Total Marks parsing error in line: "+(i+1);
				Utilities.showMessage(msg);
				continue;
			}
			
			// Field 9: Minimum External Marks for pass
			mne = utility.Utilities.parseInt(arr[i][9].trim());
			if(mne < 0)
			{
				String msg = "Min. Ext Marks parsing error in line: "+(i+1);
				Utilities.showMessage(msg);
				continue;
			}
			
			// Field 10: Minimum Total marks for pass
			mnt = utility.Utilities.parseInt(arr[i][10].trim());
			if(mnt < 0)
			{
				String msg = "Min. Total Marks parsing error in line: "+(i+1);
				Utilities.showMessage(msg);
				continue;
			}
			
			// Field 11: Credits
			cr = utility.Utilities.parseInt(arr[i][11].trim());
			if(mnt < 0)
			{
				String msg = "Credits parsing error in line: "+(i+1);
				Utilities.showMessage(msg);
				continue;
			}
			
			// Field 12: Theory / Lab
			tl = arr[i][12].trim().toUpperCase();
			
			// Check if it is already processed .. If so, load the values
			if(arr.length ==16)
			{
				// Field 13: Appeared
				app = utility.Utilities.parseInt(arr[i][13].trim());
			
				// Field 14: Passed
				pas = utility.Utilities.parseInt(arr[i][14].trim());
			
				// Field 15: Moderation
				mod = utility.Utilities.parseInt(arr[i][15].trim());
			}
			else
			{
				app = -1;
				pas = -1;
				mod = -1;
			}

			Subject sub = new Subject(sc, st, cc, bcn, rg, y, s, mxe, mxt, mne, mnt, cr, tl, app,pas,mod);
			try
			{
				subf.write(sub.toString() + "\n");
			}
			catch (Exception e)
			{
				Utilities.showMessage("Error in Writing subject: (" + e.getMessage() +")");
				res = false;
				break;
			}
		}
		
		try
		{
			subf.close();
		}
		catch(Exception e)
		{
			Utilities.showMessage("Error in Writing subject: (" + e.getMessage() +")");
			res = false;
		}
		return res;
	}
	
	/**
	 * Imports subjects from xls file
	 * @param xlsNrFile Nominal Rolls XLS file (not XLSX file)
	 * @param outFile Output subject file that contains tab separated data
	 * @return true if successful or false otherwise.
	 */
	public static boolean importNrolls(String xlsNrFile, String outFile)
	{
		String x[][] = utility.ExcelTools.ExcelToArray(xlsNrFile, 0);
		int i, j;
		String ht="",nm="",cc="", bc="", sc="", yr="", sm="";
		Vector <String> sv;
		
		FileWriter nrf=null;
		boolean res = true;
		try
		{
			nrf = new FileWriter(outFile);
		}
		catch(Exception e)
		{
			System.out.println("Error in opening file: " + outFile + "(" + e.getMessage() + ")");
			return false;
		}

		
		for(i=1;i<x.length; i++) // for each row (skip row 0)
		{
			if(x[i].length == 0)
			{
				continue; // skip blank lines
			}
			
			if(x[i].length <= 6)
			{
				Utilities.showMessage("NRolls:loadNR(): Error in Field Count at line: " +(i+1)+ " (Skipping)");
				continue;
			}
			sv = new Vector<String>();
			for(j=0;j<x[i].length; j++)
			{
				switch(j)
				{
					case 0: ht = x[i][j].trim().toUpperCase(); break;
					case 1: nm = x[i][j].trim().toUpperCase(); break;
					case 2: cc = x[i][j].trim().toUpperCase(); break;
					case 3: bc = x[i][j].trim().toUpperCase(); break;
					case 4: yr = x[i][j].trim().toUpperCase(); break;
					case 5: sm = x[i][j].trim().toUpperCase(); break;
					default: 
						sc = x[i][j].trim().toUpperCase(); 
						if(sc.length() > 4)
						{
							sv.add(sc);
						}
						break;
				}
			}
			if(ht.length() > 5) 
			{
				Nroll nrl  = new Nroll(ht,nm,cc,bc,yr, sm, sv);
				try
				{
					nrf.write(nrl.toNrString()+"\n"); // Write only NR but not marks lines
				}
				catch(Exception e)
				{
					String msg = "Error in Writing NR: " + e.getMessage();
					Utilities.showMessage(msg);
					res = false;
					break;
				}
			}
		}
		try
		{
			nrf.close();
		}
		catch(Exception e)
		{
			String msg = "Error in Writing NR: " + e.getMessage();
			Utilities.showMessage(msg);
			res = false;
		}
		return res;
	}
	
	/**
	 * Save errors vector data in a file
	 * @param file File name into which errors must be written
	 * @param ev Error Messages Vector
	 */
	public static void saveErrors(String file, Vector<String> ev)
	{
		utility.Utilities.saveVector(file, ev);
	}
	
	public static void main(String args[])
	{
		//ImportTools.importInternals("c:/workspace/expro/A10R10/im-2012.xls", "c:/workspace/expro/A10R10");
		//ImportTools.importLabExternals("c:/workspace/expro/A10R10/1G335-hm.xml", "c:/workspace/expro/A10R10/1G335-em.txt");
		ImportTools.importTheoryExternals(
				"c:/workspace/expro/A21R111211/1G334-cm.xls", 
				"c:/workspace/expro/A21R111211/1G334-ch.xls", 
				"c:/workspace/expro/A21R111211/1G334-em.txt",
				"c:/workspace/expro/A21R111211/1G334-chm.txt",
				"c:/workspace/expro/A21R111211/errors.txt",
				"1G237");
		//ImportTools.importSubjects("c:/workspace/expro/A10R10/sub-2012.xls", "c:/workspace/expro/A10R10/sub-2012.txt");
		//ImportTools.importNrolls("c:/workspace/expro/A10R10/nr-2012.xls", "c:/workspace/expro/A10R10/nr-2012.txt");
	}
}
