package expro;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
public class ExamSelector
{
	public static String getExamCodeGUI(String path)
	{
		ArrayList <String> ecodes = new ArrayList<String>();

		// Get all files from the given path
		File f = new File(path);
		String fileNames[] = f.list();
		
		
		// filter required files that matches the ecode pattern
		for(String t:fileNames)
		{
			if(!t.matches(Globals.ecodePattern))
			{
				continue;
			}
			File tf = new File(t);
			if(tf.isDirectory())
			{
				ecodes.add(t);
			}
		}
		
		// Create an array of strings from the filtered list
		fileNames = new String[ecodes.size()];
		ecodes.toArray(fileNames);
		Arrays.sort(fileNames); // sort the file names
		
		if(fileNames==null || fileNames.length == 0)
		{
			return "";
		}
		String input = (String) JOptionPane.showInputDialog(
				null, // Parent Frame 
				"Choose Exam Code", // Message
		      "Exam Code Chooser", // Window Title 
		      JOptionPane.QUESTION_MESSAGE, // Input Type
		      null, // Use default icon
		      fileNames, // Array of choices
		      fileNames[0]); // Initial choice 
		return (input == null)?"":input;
		//return "A151511R151100";
	}
	
	public static String getExamCode(String path)
	{
		path = path.replace('\\', '/');
		int idx = path.lastIndexOf('/');
		String ecode="";
		if(idx < 0)
		{
			ecode = JOptionPane.showInputDialog("Ecode: ");
			if(ecode==null)
			{
				ecode = "";
			}
		}
		else
		{
			ecode = path.substring(idx+1);
		}
		return (ecode.matches(Globals.ecodePattern)?ecode:"");
	}
}