package expro;

import javax.swing.JOptionPane;

public class ImportData
{
	public ImportData()
	{
	}
	
	public void importData()
	{
		boolean res;
		res = importNrolls();
		if(!res)
		{
			JOptionPane.showConfirmDialog(Globals.jfParent, "Nominal Rolls File Not loaded ..");
		}
		res = importSubjects();
		if(!res)
		{
			JOptionPane.showConfirmDialog(Globals.jfParent, "Subjects File Not loaded ..");
		}
		res = importIm();
		if(!res)
		{
			JOptionPane.showConfirmDialog(Globals.jfParent, "Internal Marks not  loaded properly ..");
		}
		res = importEm();
		if(!res)
		{
			JOptionPane.showConfirmDialog(Globals.jfParent, "External marks Not loaded properly ..");
		}
	}

	public boolean importNrolls()
	{
		boolean res=false;
		JOptionPane.showMessageDialog(Globals.jfParent, "Select NRolls File..");
		String fn = utility.Utilities.chooseFile("/", utility.Utilities.OPEN, utility.Utilities.FILES, "*-nr.xls");
		if(fn != null && fn.length() > 5)
		{
			res = utility.Utilities.copyfile(fn, Globals.basePath + "/" + Globals.ed.examFolder+"/" + Globals.ed.examCode + "-nr.xls");
		}
		
		return res;
	}
	
	public boolean importSubjects()
	{
		boolean res=false;
		//String fn = utility.Utilities.chooseFile("/", utility.Utilities.OPEN, utility.Utilities.FILES, "*-sub.xls");
		return res;
	}
	
	public boolean importIm()
	{
		boolean res=false;
		return res;
	}
	
	public boolean importEm()
	{
		boolean res=false;
		return res;
	}

	public static void main(String[] args)
	{
	}

}
