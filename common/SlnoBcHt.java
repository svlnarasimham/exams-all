package common;
import java.io.*;
import java.util.*;

import utility.Utilities;

public class SlnoBcHt
{
	private Vector<SBHData> v=null;
	private Hashtable<String, Integer> idx;
	SlnoBcHt()
	{
		v = new Vector<SlnoBcHt.SBHData>();
		idx = new Hashtable<String, Integer>();
	}

	/**
	 * Gets an element's index where the HTNO is blank
	 * @return empty element's index or -1 if none is available
	 */
	private int getEmptyElement()
	{
		int sz = v.size();
		for(int i=0;i<sz; i++)
		{
			if(v.get(i).htno.equals(""))
			{
				return i;
			}
		}
		return -1;
	}
	
	public int getSlno(String htno)
	{
		Integer n = idx.get(htno);
		if(n == null)
		{
			return 0;
		}
		return v.get(n).slno;
	}
	
	public int getBarcode(String htno)
	{
		Integer n = idx.get(htno);
		if(n == null)
		{
			return 0;
		}
		return v.get(n).barcode;
	}
	
	
	public void add(int slno, int barcode, String htno)
	{
		int tmp = v.size();
		v.add(new SBHData(slno, barcode, htno));
		if(!htno.trim().equals(""))
		{
			idx.put(htno, tmp);
		}
	}
	
	public void reindex()
	{
		String tmp;
		idx = new Hashtable<String, Integer>();
		int sz = v.size();
		for(int i=0;i<sz;i++)
		{
			tmp = v.get(i).htno.trim();
			if(tmp.equals(""))
			{
				continue;
			}
			else
			{
				idx.put(tmp, i);
			}
		}
	}
	
	public boolean addHtno(String htno)
	{
		if(idx.get(htno) != null) // element already exists..
		{
			return false;
		}
		int tmp = getEmptyElement();
		if(tmp == -1)  // No element is free...
		{
			return false;
		}
		v.get(tmp).htno = htno;
		idx.put(htno, tmp);
		return true;
	}
	
	public void saveSBH(String fileName)
	{
		File f = new File(fileName);
		if(f.exists())
		{
			File tf = new File(fileName+".old.txt");
			if(tf.exists())
			{
				tf.delete();
			}
			f.renameTo(new File(fileName+".old.txt"));
		}
		f = new File(fileName);
		try
		{
			FileWriter fw = new FileWriter(f);
			int sz = v.size();
			for(int i=0;i<sz;i++)
			{
				fw.write(v.get(i).toString()+"\n");
			}
			fw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int loadSBH(String fileName)
	{
		File f = new File(fileName);
		if(!f.exists())
		{
			return 0;
		}
		int cnt =0;
		String tsl, tbc, tht, line;
		try
		{
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String arr[];
			while((line = br.readLine())!= null)
			{
				line = line.trim();
				if(line.startsWith("#")) continue; // skip comments
				if(line.length() < 3) continue; // minimum length 
				arr = line.split("\t");
				if(arr.length < 2 ) 
				{ 
					System.out.println("Field Count error at : " + line);
					continue;
				}
				
				tsl = arr[0];
				tbc = arr[1];
				if(arr.length ==3)
				{
					tht = arr[2];
				}
				else
				{
					tht = "";
				}
				add(Utilities.parseInt(tsl), Utilities.parseInt(tbc), tht);
				cnt++;
			}
			br.close();
			fr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return cnt;
	}
	
	public class SBHData
	{
		int slno;
		int barcode;
		String htno;
		public SBHData(int slno, int barcode, String htno)
		{
			this.slno = slno;
			this.barcode = barcode;
			this.htno = htno;
		}
		
		public String toString()
		{
			return slno+"\t"+barcode+"\t"+htno;
		}
	}
	
	public static void main(String[] args)
	{
		SlnoBcHt sbh = new SlnoBcHt();
//		sbh.add(1, 10, "");
//		sbh.add(2, 11, "");
//		sbh.add(3, 12, "");
//		sbh.addHtno("ABCD");
//		sbh.addHtno("DEFG");
//		sbh.addHtno("GHIJ");
//		sbh.add(4, 14, "");
		sbh.loadSBH("testsbh.txt");
		System.out.println("SLNO for DEFG: " + sbh.getSlno("DEFG") + " barcode: " + sbh.getBarcode("DEFG"));
		for(int i = 0;i<sbh.v.size(); i++)
		{
			System.out.println(sbh.v.get(i));
		}
		sbh.add(6, 16, "KLMM");
		sbh.saveSBH("testsbh.txt");

	}
}
