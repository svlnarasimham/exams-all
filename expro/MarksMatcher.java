package expro;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class MarksMatcher
{
	Hashtable<String, String> htcm; // Code vs Marks for theory and Lab (key = barcode+"-"+slno)
	Hashtable<String, SlnoHtno> htcht; // theory barcode vs (slno, htno)
	Hashtable<String, String> htchl;
	
	public void matchMarks()
	{
		int nrec = 0; 
		String chfile, cmfile, type, outfile;
		// 1. Get Paths and file names
		MarksMatcherDataGUI mmdg = new MarksMatcherDataGUI();
		mmdg.getData();
		chfile = mmdg.chFile;
		cmfile = mmdg.cmFile;
		type   = mmdg.entryType;
		if(chfile.equals("") || cmfile.equals("") || type.equals(""))
		{
			return; // empty data, hence return
		}
		String basePath="./";
		if(cmfile.indexOf('/') != -1)
		{
			basePath = cmfile.substring(0,cmfile.lastIndexOf('/')); // get absolute base path
		}
		
		// Create HM file name
		String x[] = cmfile.substring(cmfile.lastIndexOf('/')+1).split("-"); 
		outfile = basePath+"/" + x[0] + "-hm.txt";
		
		// 2. Load Code vs Marks and Code Vs HTNO files into Hash tables
		boolean isTheory = (type.equalsIgnoreCase("T")?true:false);
		
		// Load Code vs Marks file
		int count = loadCM(cmfile, isTheory);
		System.out.println("Code Vs Marks loaded: " + count);

		// Load Code vs HTNO file
		count = loadCH(chfile, isTheory);
		System.out.println("Code vs HTNO Loaded: " + count);
		
		// 4. Open Output file
		FileWriter fw = null;
		try
		{
			fw = new FileWriter(outfile);
		}
		catch(Exception e){e.printStackTrace();}
		
		// Match the appropriate Values based on barcode(theory) or barcode+slno(lab)
		Enumeration<String> keys = htcm.keys(); // get all keys from code vs marks
		String res;
		String val;
		SlnoHtno slht;
		StringBuffer sberr = new StringBuffer("");
		
		while(keys.hasMoreElements()) // for each key
		{
			boolean error=false;
			val = null;
			slht = null;
			String key = keys.nextElement();
			res = "";
			
			if(isTheory) // Theory subject, hence cv and cm files should be matched on barcode
			{
				slht = htcht.get(key);
				if(slht != null)
				{
					res = slht.htno+"\t"+ htcm.get(key)+"\t"+key+"\t"+slht.slno;
				}
				else
				{
					error = true;
				}
			}
			else
			{
				val = htchl.get(key);
				if(val != null)
				{
					res = val+"\t"+ htcm.get(key)+"\t"+key;
				}
				else
				{
					error = true;
				}
			}
			if(error)
			{
				sberr.append("HTNO not found for key: " + key +"\n");
			}
			else
			{
				try
				{
					fw.write(res + "\n");
					nrec++;
				}
				catch(Exception e){e.printStackTrace();}
			}
		}
		if(sberr.length() != 0)
		{
			System.out.println(".. Missing Data in files: " + cmfile + ", " + chfile);
			JOptionPane.showMessageDialog(null, sberr);
			System.out.println(sberr.toString());
		}
		try
		{
			fw.close();
		}
		catch(Exception e){e.printStackTrace();}
		JOptionPane.showMessageDialog(null, "Records Processed: " + nrec);

	}
	
	public int loadCM(String filename, boolean isTheory)
	{
		htcm = new Hashtable<String, String>();
		StringBuffer sberr = new StringBuffer("");
		DataLoader dl = new DataLoader();
		dl.loadData(filename, "\t", (isTheory ? 2: 3));
		String arr[];
		String key, oldVal;
		
		while( (arr = dl.getNext())!= null)
		{
			if(isTheory)
			{
				key = arr[0];
				oldVal = htcm.put(key, arr[1]);
			}
			else
			{
				key = arr[0]+"-"+(utility.Utilities.parseInt(arr[1])+1);
				oldVal = htcm.put(key, arr[2]);
			}
			if(oldVal != null)
			{
				sberr.append("Duplicate Key: " + arr[0]+"\n");
			}
		}
		dl = null;
		if(sberr.length() != 0)
		{
			System.out.println(".. Duplicates Present in file " + filename);
			JOptionPane.showMessageDialog(null, sberr);
			System.out.println(sberr.toString());
		}
		return htcm.size();
		
	}
	
	public int loadCH(String filename, boolean isTheory)
	{
		if(isTheory)
		{
			htcht = new Hashtable<String, SlnoHtno>();
		}
		else
		{
			htchl = new Hashtable<String,String>();
		}
		String key;
		String oldVall;
		SlnoHtno oldValt;

		StringBuffer sberr = new StringBuffer("");
		DataLoader dl = new DataLoader();
		dl.loadData(filename, "\t", 3);
		String arr[];
		while( (arr = dl.getNext())!= null)
		{
			oldValt = null;
			oldVall = null;
			if(isTheory)
			{
				key = arr[0];
				oldValt = htcht.put(arr[1], new SlnoHtno(arr[0], arr[2]));
			}
			else
			{
				key = arr[1]+"-"+arr[2];
				oldVall = htchl.put(key, arr[3]);
			}
			if(oldValt != null || oldVall != null)
			{
				sberr.append("Duplicate Key: " + key + "\n");
			}
		}
		dl = null;
		if(sberr.length() != 0)
		{
			System.out.println(".. Duplicates Present in file " + filename);
			JOptionPane.showMessageDialog(null, sberr);
			System.out.println(sberr.toString());
		}
		if(isTheory)
		{
			return htcht.size();
		}
		else
		{ 
			return htchl.size();
		}
	}

	/**
	 * Code vs HTNO or Code vs Marks data will be loaded into this object
	 * @author svl
	 *
	 */
	public class DataLoader
	{
		public Vector<String[]> vs= null;
		int curIndex = 0;
		
		/**
		 * Loads data from a given file
		 * @param fileName File from which data is to be loaded
		 * @param delim Delimiter for fields within a record
		 * @param fieldCount Number of valid number of fields. <br>
		 *        If field count is not fieldCount, it ignores that record (line).
		 */
		public void loadData(String fileName, String delim, int fieldCount)
		{
			vs = new Vector<String[]>();
			File ftest = new File(fileName);
			
			if(!ftest.exists()) // Create a new file if doesn't exist
			{
				JOptionPane.showMessageDialog(null, fileName + " doesn't Exist.." );
			}
			
			try
			{
				FileReader fr = new FileReader(fileName);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line=br.readLine()) != null)
				{
					if (line.trim().equals("")) continue; // blank lines
					if (line.startsWith("#")) continue; // comment line
					String arr[] = line.split(delim);
					if (arr.length < fieldCount)
					{
						//System.out.println("Field Count Error at Line: "+lineCount);
						continue;
					}
					vs.add(arr);
				}
				br.close();
				fr.close();
			}
			catch (Exception e){e.printStackTrace();}
			System.out.println(vs.size() + " records loaded from " + fileName);
		}
		
		/**
		 * Returns One row as an array of Fields and increments index
		 * @return
		 */
		public String[] getNext()
		{
			String res[] = null;
			if(curIndex < vs.size())
			{
				res = vs.get(curIndex);
				curIndex++;
			}
			return res;
		}
	}
	
	/**
	 * Storage class for slno vs htno data
	 * @author svl
	 *
	 */
	public class SlnoHtno
	{
		String slno;
		String htno;
		public SlnoHtno(String slno, String htno)
		{
			this.slno = slno;
			this.htno = htno;
		}
	}
	
	public static void main(String args[])
	{
		MarksMatcher mm = new MarksMatcher();
		mm.matchMarks();
	}
}
