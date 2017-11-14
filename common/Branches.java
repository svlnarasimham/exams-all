package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import javax.swing.JOptionPane;

import utility.*;

public class Branches
{

	public Hashtable<String, Branch> htb;
	public Branches ()
	{
		htb = new Hashtable<>();
	}
	
	/**
	 * Load all branches for all courses
	 * @return
	 */
	public int loadBranches()
	{
		return loadBranches("*");
	}
	
	/**
	 * Loads all branches from the database for a given code. If connection failed
	 * loads default branches.
	 * @param courseCode Course code for which branches are to be loaded (* for all branches)
	 * @return number of branches loaded
	 */
	public int loadBranches(String courseCode)
	{
		htb.clear();
		BranchDao bs = new BranchDao();
		ArrayList<Branch> al = null;
		if(courseCode.equals("*") || courseCode.equals(""))
		{
			al = bs.getAll();
		}
		else 
		{
			al = bs.getSome(courseCode);
		}
		if(al.size() == 0) // If no branch is loaded, load default branches
		{
			Utilities.showMessage(null, "Can not load branches from Database. Loading defaults.");
			al = bs.getDefaultBranches(courseCode);
		}
		for(Branch b: al)
		{
			htb.put(b.ccode+b.bcode, b);
		}
		return htb.size();
	}

	/**
	 * Imports branches from Text file into database which contain the following tab separated fields<br>
	 * Field 1. Course Code (A = BTech, D=MTech, E=MBA, F=MCA ...)<br>
	 * Field 2. Branch Code (01=Civil, 02=EEE ...)<br>
	 * Field 3. Short Name (Ex: CE, EEE, ME, ECE, CSE, ...)<br>
	 * Field 4. Branch Full Name 
	 * @param fname Branches file name 
	 * @return number of branches loaded (for all courses)
	 */
	public int importBranches(String fname)
	{
		int cnt;
		File ftest = new File(fname);
		BranchDao bs = new BranchDao();
		
		if(!ftest.exists()) // Create a new file if doesn't exist
		{
			JOptionPane.showMessageDialog(null, 
					"Branches File " + fname + " doesn't Exist.. Loading Defaults .." );
			return 0;
		}
		
		cnt=0;
		try
		{
			FileReader fr = new FileReader(fname);
			BufferedReader br = new BufferedReader(fr);
			String line;
			int lineCount=0;
			String cc=""; // Course Code
			String bc=""; // branch code
			String bsn=""; // Branch short name
			String bn=""; // branch Name
			String fac = ""; // Faculty
			
			while((line=br.readLine()) != null)
			{
				lineCount++;
				if (line.equals("")) continue; // blank lines
				if (line.startsWith("#")) continue; // comment line
				String arr[] = line.split("[:\t]");
				if (arr.length < 4)
				{
					System.out.println("Branches:loadBranches: Field Count Error at Line: "+lineCount);
					continue;
				}
				cc=arr[BranchDao.CCODE].trim().toUpperCase();
				bc=arr[BranchDao.BCODE].trim().toUpperCase();
				bsn = arr[BranchDao.BABBR].trim().toUpperCase();
				bn = arr[BranchDao.BNAME].trim().toUpperCase();
				fac = "";
				if(arr.length > 4)
				{
					fac = arr[BranchDao.DCODE].trim().toUpperCase();
				}
				int res = bs.add(new Branch(cc,bc, bsn, bn, fac));
				if(res == 1)
				{
					cnt++;
				}
			}
			br.close();
			fr.close();
		}
		catch (Exception e){e.printStackTrace();}
		return cnt;
	}

	/**
	 * Get All branches of all courses
	 * @return All branches as an array
	 */
	public Branch[] getBranches()
	{
		Enumeration<Branch> vals = htb.elements();
		Vector<Branch> tv = new Vector<Branch>();
		Branch ba[];
		while(vals.hasMoreElements())
		{
			tv.add(vals.nextElement());
		}
		ba = new Branch[tv.size()];
		Arrays.sort(tv.toArray(ba));
		return ba;
	}

	/**
	 * Returns all branches for a given course code
	 * @param ccode Course Code
	 * @return Branches array
	 */
	public Branch[] getBranches(String ccode)
	{
		Enumeration<Branch> vals = htb.elements();
		Vector<Branch> tv = new Vector<Branch>();
		Branch ba[];
		Branch b;
		while(vals.hasMoreElements())
		{
			b = vals.nextElement();
			if(b.ccode.equalsIgnoreCase(ccode))
			{
				tv.add(b);
			}
		}
		ba = new Branch[tv.size()];
		Arrays.sort(tv.toArray(ba));
		return ba;
	}
	
	/**
	 * 
	 * @return Returns the number of branches in the container
	 */
	public int getBranchCount()
	{
		return htb.size();
	}

	/**
	 * Gets a branch for a given short name
	 * @param shortName Branch short name
	 * @return Branch object if found or null
	 */
	public Branch getBranch(String shortName)
	{
		Enumeration<Branch> vals = htb.elements();
		Branch tmpbr, fbrn = null;
		while(vals.hasMoreElements())
		{
			tmpbr = vals.nextElement();
			if(tmpbr.babbr.equalsIgnoreCase(shortName))
			{
				fbrn = tmpbr;
				break;
			}
		}
		return fbrn;
	}
	
	/**
	 * Get Branch object for the given course and branch
	 * @param ccode Course Code
	 * @param bcode Branch Code
	 * @return Branch object or blank object if none found. (Never null)
	 */
	public Branch getBranch(String ccode, String bcode)
	{
		Branch b = null;
		if(bcode.trim().length() == 1)
		{
			bcode = "0"+bcode.trim();
		}
		b = htb.get(ccode.trim().toUpperCase()+bcode.toUpperCase());
		if(b==null)
		{
			b = new Branch();
		}
		return b;
	}

	/**
	 * Gets a branch for a given course and branch codes
	 * @param ccode Course code
	 * @param bcode Branch code
	 * @return Branch object if found or null
	 */
	public Branch getBranch(String ccode, int bcode)
	{
		return getBranch(ccode, String.format("%02d",bcode));
	}

	/*****/
	public static void main(String args[])
	{
		G.initialize();
		G.branches.loadBranches();
		System.out.println("Branches: " + G.branches.getBranchCount());
		System.out.println("Branch for A, 3:" + G.branches.getBranch("A", "03").bname);
		Branch ba[] = G.branches.getBranches();
		for(int i=0;i<ba.length; i++)
		{
			System.out.println(ba[i].ccode+" " + ba[i].bcode + " " + ba[i].bname);
		}
		System.out.println("Branch: " + G.branches.getBranch("ECE").bname);
		System.out.println("BRanch: " + G.branches.getBranch("D", "04").bname);
	}
	
	/********/
}
