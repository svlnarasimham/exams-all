package expro;

//import java.io.FileWriter;
import java.util.Vector;

import common.Mark;
import utility.Utilities;

public class Nroll
{
	public static final int IM = 0;
	public static final int EM = 1;
	
	public String rollno;
	public String name;
	public String fname;
	public String mname;
	public String ccode;
	public int bcode;
	public String year;
	public String sem;
	public int nregd;
	public Vector<Mark> mks;
	
	public Nroll()
	{
		rollno="";
		name = "";
		fname = "";
		mname = "";
		ccode="";
		bcode=0;
		year = "";
		sem = "";
		nregd=0;
		
		mks = new Vector<>();
	}
	
	public Nroll(String htno, String name, String ccode, String bcode, String year, String sem, Vector<String> scodes)
	{
		this(htno,name,ccode, Utilities.parseInt(bcode),year,sem, scodes);
	}
	
	public Nroll(String htno, String name, String ccode, int bcode, String year, String sem, Vector<String> scodes)
	{
		this.rollno  = htno;
		this.name  = name;
		this.ccode = ccode;
		this.bcode = bcode;
		this.year = year;
		this.sem = sem;
		mks = new Vector<>();
		for(int i=0;i<scodes.size();i++)
		{
			mks.add(new Mark(scodes.get(i)));
		}
		this.nregd = scodes.size();
	}
	
	public Nroll(String htno, String name, String ccode, String bcode, String year, String sem)
	{
		this(htno,name,ccode,Utilities.parseInt(bcode), year, sem);
	}
	
	public Nroll(String htno, String name, String ccode, int bcode, String year, String sem)
	{
		this.rollno  = htno;
		this.name  = name;
		this.ccode = ccode;
		this.bcode = bcode;
		this.year = year;
		this.sem = sem;
		mks = new Vector<>();
		this.nregd = 0;
	}
	
	/**
	 * Counts the number of marks records and sets this to the internal registered count.
	 */
	public void resetRegdCount()
	{
		this.nregd = this.mks.size();
	}
	
	public Mark getMarks(String scode)
	{
		int i;
		for(i=0;i<mks.size(); i++)
		{
			if(mks.get(i).scode.equalsIgnoreCase(scode))
			{
				return mks.get(i);
			}
		}
		return null;
	}
	
	
	public String[] getPassSubjects()
	{
		int i = 0;
		Vector <String> pcv = new Vector<String>();
		for(i=0;i<mks.size();i++)
		{
			if(mks.get(i).res.equalsIgnoreCase("P"))
			{
				pcv.add(mks.get(i).scode);
			}
		}
		String x[] = new String[pcv.size()];
		for(i=0; i < x.length; i++)
		{
			x[i] = new String(pcv.get(i));
		}
		return x;
	}
	
	public boolean isRegistered(String scode)
	{
		boolean res = false;
		for(int i=0; i< mks.size();i++)
		{
			if(mks.get(i).scode.trim().toUpperCase().equalsIgnoreCase(scode)) 
			{
				res = true;
				break;
			}
		}
		return res;
	}
	
	/**
	 * @param subCode
	 * 	-> Subject Code
	 * @param marks -> Marks
	 * @param mksType -> Marks Type ( Internal (IM = 0) / External (EM = 1))  
	 * @return -> result code
	 * 	1:successful, 
	 * 	0:duplicate  
	 * 	-1:duplicate with different marks 
	 * 	-2:sub code not found
	 */
	public int addMarks(String subCode, int marks, int mksType)
	{
		int res = -2; // Set to not found initially
		Mark m=null;
		int i, tmk;
		for(i=0;i<nregd; i++)
		{
			m = mks.get(i);
			if(m.scode.equalsIgnoreCase(subCode)) // Subject Found
			{
				break;
			}
		}
		if(i<nregd) // Subject Found, so try to add marks 
		{
			tmk = ((mksType == IM) ? m.im : m.em);
			
			if(tmk != Mark.DEFAULT) // marks already posted
			{
				if(tmk == marks) 
				{
					res = 0; // Duplicate
				}
				else
				{
					res = -1; // Duplicate entry with different marks ..
				}
			}
			else // Everything OK, so post marks to appropriate field
			{
				res = 1;
				if(mksType == IM)
				{
					m.im = marks;
				}
				else
				{
					m.em = marks;
				}
			}
		}
		return res;
	}
	
	public String toNrString()
	{
		String d = "\t";
		String ts = rollno + d + name + d + ccode + d + bcode + d + year + d + sem + d + nregd;
		String ts2="";
		String tsc;
		for(int i=0;i<mks.size(); i++)
		{
			tsc = mks.get(i).scode;
			ts2 = ("\t" +  tsc);
			ts += ts2;
		}
		return ts;
	}
	public String toString()
	{
		String d = "\t";
		String ts = rollno + d + name + d + ccode + d + bcode + d + year + d + sem + d + nregd + "\n";
		String ts2="";
		Mark m;
		for(int i=0;i<mks.size(); i++)
		{
			m = mks.get(i);
			ts2 = m.toString() + "\n";
			ts += ts2;
		}
		return ts;
	}
	
	public static Nroll createNroll(String line)
	{
		String ht,nm,cc, bc, yr, sm, nr, sc;
		int nregd=0;
		Vector <String> v ;
		Nroll tnr;
		String arr[] = line.split("\t");
		if (arr.length < 7) // Field Count error
		{
			return null;
		}
		
		// Field 0: HTNO
		ht = arr[0].trim().toUpperCase();
		
		// Field 1: Name
		nm = arr[1].trim().toUpperCase();
		
		// Field 2: Course Code
		cc = arr[2].trim().toUpperCase();
		
		// Field 3: Branch Code
		bc = arr[3].trim();
		
		// Field 4: Year
		yr = arr[4].trim();
		
		// Field 5: Sem
		sm = arr[5].trim();
		
		// Field 6: nRegistered (Not used in program and hence skip)
		nr = arr[6].trim(); // Read and skip if subCodes present
		nregd = Utilities.parseInt(nr);
		
		// Field 7 to end are subject codes, read them to a vector
		
		v =  new Vector<String>();
		for(int i=7;i<arr.length;i++)
		{
			sc = arr[i].trim().toUpperCase();
			if(sc.length() > 4) // Check for minimum code length
			{
				v.add(sc);
			}
		}
		if(v.size() > 0)
		{
			tnr = new Nroll(ht,nm,cc,bc,yr,sm, v);
		}
		else
		{
			tnr = new Nroll(ht,nm,cc,bc,yr,sm);
			tnr.nregd = nregd;
		}
		return tnr;
	}
	
	public static Nroll createNroll(String line[])
	{
		//String ht,nm,cc, bc, yr, sm, nr, sc;
		Nroll tnr;
		int i = 0;
		
		tnr = Nroll.createNroll(line[0]);
		if (tnr == null) 
		{
			return null;
		}
		
		if(tnr.nregd != line.length-1)
		{
			return null;
		}
		// Load Subjects and marks from remaining lines
		for(i=1;i<line.length;i++)
		{
			Mark m = Mark.createMarks(line[i]);
			if(m == null) // Check for minimum code length
			{
				return null;
			}
			tnr.mks.add(m);
		}
		tnr.resetRegdCount();
		return tnr;
	}

}
