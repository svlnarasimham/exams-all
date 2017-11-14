/**
 * This program adds subject names to the marks file for easy lookup
 * This is not used by the programs, but used for manual verification/use.
 * It takes subjects file and marks file and add subject title to marks file and writes the new data in a new file
 */
package expro;
import java.io.*;

import common.Subject;
import common.Subjects;
import utility.Utilities;

public class SubNameAdderForMarksFile
{
	public Subjects sub;

	public SubNameAdderForMarksFile()
	{
		sub = new Subjects(); // 
	}
	
	public void doAddSubjectNames()
	{
		String mksFname = Utilities.chooseFile("./", Utilities.OPEN, Utilities.FILES, "..*nrwm.txt");
		if(mksFname == null || mksFname.equals("")) return;
		
		String subFname = Utilities.chooseFile("./", Utilities.OPEN, Utilities.FILES, "..*sub.txt");
		if(subFname == null || subFname.equals("")) return;
		String outFname = mksFname.substring(0,mksFname.length()-4)+"-with-sub-names.txt";
		int res = addSubjectNames(mksFname, subFname, outFname);
		Utilities.showMessage(null, outFname + " file created with " + res + " records..");
	}
	
	public int addSubjectNames(String marksFname, String subFname, String outFname)
	{
		final int FIELD_COUNT=10;
		final int SUB_CODE=0;
		final int INTERNAL_MARKS=1;
		final int EXTERNAL_MARKS=2;
		final int TOTAL_MARKS=7;
		final int CREDITS=8;
		final int RESULT=9;

		int cnt=0;
		int scnt = sub.loadSubjects(subFname);
		if(scnt <= 0)
		{
			Utilities.showMessage("Can not load subjects from file " + subFname);
			return 0;
		}
		try
		{
			FileReader fr = new FileReader(marksFname);
			BufferedReader br = new BufferedReader(fr);
			
			FileWriter fw = new FileWriter(outFname);
			
			String line;
			String tsc; // temporary subject code

			// ///////////// Possible data formats ///////////////////////////////
			// NR File with Marks (*-nrwm.txt) Row 1 - Candidate details 
			// htno + name + crscode + brcode + year + sem + batch
			// Next n rows where n is the number of subjects registered, contain marks data
			// subCode + intMks + extMks + subModrtion + graceMks + spclMod + totExtMks + totMks + crdts + result;
			//
			// Data file for Database Update, which contains same fields for all rows.
			//Fields: Rollno, course, branch, year, sem, subcode, im, em, sm, gm, spm, tem, tot, cr, res
			//          0        1       2      3    4      5      6   7   8    9   10   11   12  13   14
			// /////////////////////////////////////////////////////////////////
			while((line=br.readLine()) != null)
			{
				if (line.trim().equals("") || line.startsWith("#")) // blank lines or comment lines 
				{
					fw.write(line+"\n");
					fw.flush();
					continue; 
				}
				
				String arr[] = line.split("\t"); // split fields into an array
				if (arr.length != FIELD_COUNT) // if field count is not matching 
				{
					fw.write("\n" + line + "\n"); // write line as it is
					fw.flush();
					continue;
				}

				tsc = arr[SUB_CODE];
				String snm = "";
				Subject tsub = sub.getSubject(tsc);
				if(tsub == null || tsub.title.equals(""))
				{
					snm = "xxxxxxxxxxxx";
				}
				else
				{
					snm = tsub.title;
				}
				
				fw.write(
						arr[SUB_CODE] + "\t"+
						snm + "\t"+ 
						arr[INTERNAL_MARKS] + "\t"+	
						((arr[EXTERNAL_MARKS].equals("-1"))?"AB":arr[EXTERNAL_MARKS]) + "\t"+	
						arr[TOTAL_MARKS] + "\t"+	
						arr[CREDITS] + "\t"+	
						arr[RESULT] + "\n");	
				fw.flush();
				cnt++;
			}
			
			br.close();
			fr.close();
			fw.close();
		}
		catch (Exception e)
		{
			Utilities.showMessage("Error in Loading Subjects: " + e.getMessage());
		}
		return cnt;
	}

	/*********/
	public static void main(String[] args)
	{
		new SubNameAdderForMarksFile().doAddSubjectNames();
	}
	/***************/
}
