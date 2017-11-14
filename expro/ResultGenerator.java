package expro;

import java.io.*;
import java.util.Vector;

import javax.swing.JOptionPane;

import common.Subject;
import common.Subjects;
import utility.Utilities;

/**
 * This class reads the results file and generates the <br>
 * final results file to be displayed to students
 * 
 * @author svl narasimham
 *
 */
// Steps involved
// 1. read NRWM results file
// 2. Load SUB File
// 3. Add grades to the marks
// 4. Add subject names to subcodes
// 5. For each branch, print result


//15491A0228

public class ResultGenerator
{
	public static final int OPEN = 0; // 
	public static final int CLOSE = 1; // 
	
	static String nrwmFile = null;
	static String subf = null;
	static String finalResultsFile = "final-results.txt";
	static String finalDbFile = "final-db.txt";
	static String basePath = ".";
	static String ccode = "A"; 

	static FileReader fr = null;
	static BufferedReader br = null;
	static FileWriter fw = null;
	static FileWriter fd = null;
	
	static Subjects subs = null;
	static boolean debug = false;
	
	public static void main(String args[])
	{

		// 1. Get NR with Marks file ("*-nrwithmarks.txt")
		if(debug)
		{
			ccode = "E"; // set course to E
			nrwmFile = "E151501R151200/test-nrwithmarks.txt"; // Set nrwm file name
		}
		else
		{
			// Get Course Code (to prepend to results file)
			String[] choices = { "A: B.Tech.", "D: M.Tech.", "E: MBA", "F: MCA" };
		   String input = (String) JOptionPane.showInputDialog(
		   		null, // Parent window
		   		"Choose Course", // Prompt message
		   		"Choose Course", // Window Title
		   		JOptionPane.QUESTION_MESSAGE, // Message type
		   		null,// Icon null means use default
		   		choices, // Array of choices
		   		choices[0]); // Initial choice
		   if(input==null || input.trim().equals(""))
		   {
				System.out.println("Course code can not be blank...");
		   	return; // nothing can be done
		   }
			ccode = input.split(":")[0];
			// Get NR With Marks File Name
			nrwmFile = Utilities.chooseFile("./", Utilities.OPEN,Utilities.FILES,"..*nrwithmarks.txt","Choose NR with Marks file (*-nrwithmarks.txt)");
			if(nrwmFile == null)
			{
				System.out.println("No NR with Marks file selected ...");
				return;
			}
			nrwmFile = nrwmFile.replace('\\', '/');
			//System.out.println("NR File: "+ nrwmFile);
		}
		// Get Base Path from NRWM file path
		basePath = nrwmFile.substring(0,nrwmFile.lastIndexOf("/"));
		
		//2. Load subjects file
		subs = new Subjects();
		if(debug)
		{
			subf = "E151501R151200/E151501R151200-sub.txt";// Debug Code
		}
		else
		{
			subf = Utilities.chooseFile(basePath, Utilities.OPEN,Utilities.FILES,"..*-sub.txt","Choose subjects file...");
			if(subf == null)
			{
				System.out.println("No subjects file selected ...");
				return;
			}
		}
		subs.loadSubjects(subf);
		if(subs.getSubjectCount() == 0)
		{
			System.out.println("Can not load subjects from file: "+ Globals.subjectsFile);
			return;
		}
		// 3. Read NR with marks file and save the results in "final-result.txt" file
		openFiles();
		String line;
		String arr[];
		String htno="", name="", branch="";
		String firstLine;
		Vector<String> marks = new Vector<String>();
		int cgp;
		int ccr;
		int tgp;
		int tcr;
		int nsub;
		int slno =0;
		int nrec = 0;
		while((line=getLine(br)) != null)
		{
			if(line.startsWith("#") || line.trim().length() == 0)
			{
				continue;
			}
			arr = line.split("\t");
			if(arr.length < 7)
			{
				System.out.println("Field Count Error at line: " + line);
				continue;
			}
			if(arr.length == 7) // First Line
			{
				htno = arr[0];
				name = arr[1];
				if(!branch.equals(arr[3].trim()))
				{
					slno = 0;
					branch = arr[3];
				}
				nsub = Utilities.parseInt(arr[6]);
				ccr = 0;
				cgp = 0;
				marks.removeAllElements();
				//System.out.println("Line: " + htno);
				for(int i=0;i<nsub;i++)
				{
					line = getLine(br);
					arr = line.split("\t");
					if(arr.length < 10)
					{
						System.out.println("Field Count error for HTNO: " + htno + " " + line);
						System.exit(1);
					}
					String subcode = arr[0];
					Subject sb = subs.getSubject(subcode);
					if(sb == null)
					{
						System.out.println("Can not find subject: " + subcode);
						continue;
					}
					int mm = sb.mxt;
					String subName = sb.title;
					boolean isTheory = sb.tl.equals("T");
					int im = Utilities.parseInt(arr[1]);
					int em = Utilities.parseInt(arr[6]);
					tcr = Utilities.parseInt(arr[8]);
					String res = arr[9];
					//if(htno.endsWith("0363") && subcode.equals("A0081"))
					//{
					//	System.out.println("Debug " + im + ", " + em);
					//}
					
					String grade = Grades.getGrade(im, em, mm, res, (sb.tl.toUpperCase().equals("E")?true: isTheory));
					tgp = Grades.getGradePoints(grade);
					if(sb.tl.equalsIgnoreCase("T")|| sb.tl.equalsIgnoreCase("L")) {ccr += sb.credits;}
					if(sb.tl.equalsIgnoreCase("T")|| sb.tl.equalsIgnoreCase("L")) {cgp += (tgp*tcr);}
					marks.add(subcode + "\t"+String.format("%-50.50s",subName)+"\t"+ tcr+"\t"+ grade + "\t"+ tgp);
				}
				float sgpa = (float)cgp/(float)ccr;
				firstLine = htno+"\t"+name+"\t"+"SGPA: (" + cgp + "/" + ccr + ") = " + String.format("%.2f", sgpa) + "\tBranch: "+branch;
				writeRecord((slno+1), firstLine, marks);
				slno++;
				nrec++;
			}
		}
		
		closeFiles();
		JOptionPane.showMessageDialog(null, nrec+" Records Written to file...");
		//System.out.println("NR with marks file to "+ finalResultsFile+" conversion done...");
		System.out.println("Final result is saved in " + basePath+"/"+ccode+"-"+finalResultsFile +" ...");
	}
	
	public static void writeRecord(int slno, String firstLine, Vector<String> marks)
	{
		//System.out.println("Writing: " + firstLine);
		try
		{
				fw.write(slno+".\t" + firstLine + "\n");
				for(int i=0;i<marks.size(); i++)
				{
					fw.write("\t"+(i+1) + ".\t" + marks.get(i)+"\n");
				}
				fw.write("\n\n");
				
				// Write to DB compatible text file
				for(int i=0;i<marks.size(); i++)
				{
					String htno = firstLine.split("\t")[0].trim();
					String arr[] = marks.get(i).split("\t");
					fd.write(htno + "\t" + arr[0] + "\t" + arr[2] + "\t" + arr[3] + "\t" + arr[4] +"\n");
				}
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	public static void openFiles()
	{
		try
		{
				fr = new FileReader(nrwmFile);
				br = new BufferedReader(fr);
				fw = new FileWriter(basePath+"/"+ccode+"-"+finalResultsFile);
				
				fd = new FileWriter(basePath+"/"+ccode+"-"+finalDbFile);
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
			return;
		}
		
	}
	
	public static void closeFiles()
	{
		try
		{
				br.close();
				fr.close();
				fw.close();
				fd.close();
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
			return;
		}
		
	}
	public static String getLine(BufferedReader br)
	{
		try
		{
			return br.readLine();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
