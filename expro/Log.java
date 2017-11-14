package expro;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

public class Log
{

	String logFileName;
	String basePath;

	public Log()
	{
		basePath=Globals.basePath;
		logFileName = Globals.logFile;
	}
	
	public void setLogFileName (String logFileName)
	{
		this.logFileName = logFileName;
	}

	public void setBasePath (String basePath)
	{
		this.basePath = basePath;
	}

	public boolean writeLog(String msg)
	{
		boolean res=true;
		FileWriter fw;
		String fnm = basePath + Globals.pathSep + Globals.logFolder + Globals.pathSep + logFileName;
		
		// Open file in append mode, write msg and then close file
		try
		{
			fw = new FileWriter(fnm, true);
			fw.write(utility.Utilities.encrypt(msg)+"\n"); // Write message
			fw.close();
		}
		catch(Exception e)
		{
			res = false;
			JOptionPane.showMessageDialog(null, "Can not open Logfile: " + fnm);
		}
		return res;
	}
	
	/*****
	Creates a message with : separated data with fields in the following order
	Type of Action : User ID : Message : Action : Time 
	*******/ 
	public String createMsg(String type, String uid, String msg, String action)
	{
		//System.out.println("createmsg");
		String res="";
		res = type+ " : " + uid + " : " + msg + " : " + action + " : " + utility.Utilities.getTimeString();
		//System.out.println("createmsg");
		return res;
	}

	/*************** Other Log Utilities ****************/
	
	public void viewLog(JFrame parent)
	{
		if(!Globals.curUser.getUrole().equals("admin"))
		{
			JOptionPane.showMessageDialog(parent, "Only Admin can do this");
			return;
		}
		String fnm = basePath + Globals.pathSep + Globals.logFolder + Globals.pathSep + logFileName;
		new MyTextEditor(parent, "View Log", fnm, false);
	}
	
	public void clearLog(JFrame parent)
	{
		if(!Globals.curUser.getUrole().equals("admin"))
		{
			JOptionPane.showMessageDialog(parent, "Only Admin can do this");
			return;
		}
		String fnm = basePath + Globals.pathSep + Globals.logFolder + Globals.pathSep + logFileName;
		try
		{
			File f = new File(fnm);
			boolean res = f.delete();
			if (!res)
			{
				JOptionPane.showMessageDialog(parent, "File Deletion Error");
			}
			else
			{
				FileWriter fw = new FileWriter(fnm);
				fw.close();
			}
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(parent, "File Truncation Error");
			return;
		}
		JOptionPane.showMessageDialog(parent, "Log File Cleared");
	}
	
	public void backupLog(JFrame parent)
	{
		if(!Globals.curUser.getUrole().equals("admin"))
		{
			JOptionPane.showMessageDialog(parent, "Only Admin can do this");
			return;
		}
		String fnm = basePath + Globals.pathSep + Globals.logFolder + Globals.pathSep + "logBackup-"
							+ utility.Utilities.getTimeString() + ".txt";
		try
		{
			FileWriter fw = new FileWriter(fnm,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(utility.Utilities.encrypt("Backup on: "+utility.Utilities.getTimeString())+"\n");
			//bw.newLine();
			FileReader fr = new FileReader(basePath + Globals.pathSep + Globals.logFolder + Globals.pathSep
														+ logFileName);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line=br.readLine())!= null)
			{
				bw.write(utility.Utilities.encrypt(line) + "\n");
			}
			bw.close();
			fw.close();
			br.close();
			fr.close();
			String msg=createMsg(Globals.curUser.getUrole(), Globals.curUser.getUid(), " backup is taken for the log up to date ", " logbackup ");
			writeLog(msg);
			JOptionPane.showMessageDialog(parent, "Backup Stored in : " + fnm);
		}
		catch(IOException ioe)
		{
			JOptionPane.showMessageDialog(parent, "Backup Failed...");
		}
	}
	
}
