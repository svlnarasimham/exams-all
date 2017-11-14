package expro;

import java.util.Vector;

import javax.swing.JOptionPane;

import common.G;
import common.Subject;
import common.Subjects;

public class CheckFiles
{
	Subjects sub;
	Vector <String> ev; // Error Vector
	
	public CheckFiles()
	{
		if(!G.isInitialized())
		{
			G.initialize();
		}
		ev = new Vector<String>();
	}
	
	public boolean checkAll()
	{
		sub = new Subjects();
		String basePath = G.ed.examFolder+"/";
		int res = sub.loadSubjects(G.ed.examFolder+"/subjets.txt");
		String errfile = basePath + "errors.txt";
		if(res <= 0)
		{
			JOptionPane.showMessageDialog(G.jfParent, "Can not load subjects from : " + G.ed.examFolder + "/subjects");
			return false;
		}
		Subject s[] = sub.getSubjects();
		int i;

		//Check NR File:
		String fnr = basePath + G.ed.examCode + "-nr.txt";
		if(!utility.Utilities.checkFile(fnr))
		{
			ev.add("NR file not found: " + fnr);
		}
		
		
		//Check Internal and external Marks Files..
		for(i=0; i<s.length;i++)
		{
			String fim = basePath + s[i].scode + "-im.txt";
			String fem = basePath + s[i].scode + "-cm.txt";
			if(!utility.Utilities.checkFile(fim))
			{
				ev.add("Internal Marks file not found for subject: " + s[i].scode);
			}
			if(!utility.Utilities.checkFile(fem))
			{
				ev.add("External Marks not imported for subject: " + s[i].scode);
			}
		}
		if(ev.size() > 0)
		{
			utility.Utilities.saveVector(errfile, ev);
			JOptionPane.showMessageDialog(null, "Errors Present in matching.. See file: " + errfile + " for errors");
			return false;
		}
		JOptionPane.showMessageDialog(G.jfParent, "Check for Files Successful");
		return true;
	}
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
