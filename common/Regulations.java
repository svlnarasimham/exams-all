package common;
import java.util.*;
import utility.*;

public class Regulations
{
	public ArrayList<Regulation> rgal;
	public Regulations ()
	{
		rgal = new ArrayList <>();
		G.initialize();
	}
	
	/**
	 * Loads Regulations from Database (regulations table)
	 * @return count of regulations loaded
	 */
	public int loadRegulations()
	{
		RegulationDao rgd = new RegulationDao();
		rgal = rgd.getAll();
		if(rgal.size() ==0)
		{
			Utilities.showMessage(null, "Can not load regulations....");
			System.exit(1);
		}
		return rgal.size();
	}
	
	public String[] getRegulationsOnly(String ccode)
	{
		ArrayList<Regulation> al = getRegulations(ccode);
		if(al.size() == 0)
		{
			return null;
		}
		String arr[] = new String[al.size()];
		int i=0;
		for(Regulation r:al) 
		{
			arr[i++] = r.getRcode();
		}
		return arr;
	}
	// Return regulations for a code. 
	public ArrayList<Regulation> getRegulations(String ccode)
	{
		ArrayList<Regulation> alr = new ArrayList<>();
		for(Regulation r: rgal)
		{
			if (r.ccode.equalsIgnoreCase(ccode))
			{
				alr.add(r);
			}
		}
		return alr;
	}
	
	public int getRegulationsCount()
	{
		return rgal.size();
	}

	public ArrayList<Regulation> getRegulations()
	{
		return new ArrayList<Regulation>(rgal);
	}
	
	/* */
	public static void main(String args[])
	{
		G.initialize();
		int nrec = G.regulations.loadRegulations();
		//c.loadCourses("courses.dat");
		System.out.println("Regulations: " + nrec);
		ArrayList<Regulation> c = G.regulations.getRegulations();
		System.out.println("Regulations: " + Regulation.toJSON(c));
		c = G.regulations.getRegulations("D");
		for(Regulation r:c)
		{
			System.out.println(r.ccode + ": " + r.rcode);
		}
		G.closeAll();
	}
	/* */
}
