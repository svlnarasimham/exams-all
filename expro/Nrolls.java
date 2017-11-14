package expro;
import java.io.*;
import java.util.*;

import common.G;
import common.Mark;
import utility.Utilities;

public class Nrolls
{
	public Vector <Nroll> nrl;
	public Hashtable<String, Integer> hst;
	
	public Nrolls ()
	{
		if(!G.isInitialized())
		{
			G.initialize();
		}
		nrl = new Vector<Nroll>();
		hst = new Hashtable<String, Integer>();
	}
	
	public boolean createNrFileFromDb(String examcode, String outputFile)
	{
		if(!G.dbcon.isInitialized())
		{
			G.dbcon.initConnection(G.dbDriver, G.dbUrl, G.dbUser, G.dbPassword);
			if(!G.dbcon.isConnected())
			{
				System.out.println("Can not connect to DB...");
				return false;
			}
		}
		String nrsql = "select distinct a.srollno, a.sname, a.bcode, b.scode from students a, registration b where a.srollno = b.srollno and b.ecode='"+examcode+"' order by a.bcode,a.srollno, b.scode;";
		Vector<String[]> v = G.dbcon.executeSQL(nrsql);
		String cc = examcode.substring(0,1);
		String yr = examcode.substring(5,6);
		String sm = examcode.substring(6,7);
		StringBuffer res = new StringBuffer("");
		
		int sz = v.size();
		int nrec=0;
		//System.out.println("SQL: " + nrsql);
		String htn="";
		String snm="";
		String bc ="";
		Vector<String>vsc = new Vector<String>();
		String arr[];
		if(sz == 0)
		{
			G.out.println("SQL: " + nrsql);
			G.out.println("Can not get NR records from DB..");
			return false;
		}
		FileWriter fw=null;
		try
		{
			fw = new FileWriter(outputFile);
		}
		catch(Exception e)
		{
			System.out.println("File Name: " + outputFile);
			e.printStackTrace();
			return false;
		}
		int start=0;
		// arr order = "srollno, sname, bcode, scode";
		while(start<sz)
		{
			arr = v.get(start);
			vsc.removeAllElements();
			htn = arr[0];
			snm = arr[1];
			bc = arr[2];
			vsc.add(arr[3]);
			res.delete(0, res.length());
			res.append(arr[0]+"\t"+snm+"\t"+cc+"\t"+bc+"\t"+yr+"\t"+sm);
			start++;
			while(start<sz)
			{
				arr = v.get(start);
				if(htn.equals(arr[0]))
				{
					vsc.add(arr[3]);
					start++;
				}
				else
				{
					break;
				}
			}
			// Write the record
			res.append("\t"+vsc.size());
			for(int j=0; j<vsc.size();j++)
			{
				res.append("\t"+vsc.get(j));
			}
//			System.out.println(""+nrec+"." + res.toString());
//			nrec++;
			try
			{
				fw.write(res.toString()+"\n");
				nrec++;
			}
			catch(Exception e)
			{ 
				e.printStackTrace();
			}
		}
		try
		{
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		Utilities.showMessage(nrec + "NR Records Written: " + " to file: " + outputFile);
		return true;
	}
	
	public Nroll getNroll(String htn)
	{
		Integer res = hst.get(htn.trim().toUpperCase());
		if(res != null) 
			return nrl.get(res); 
		else 
			return null;
	}
	
	/**
	 * Returns the Nroll object for the given sequence number
	 * 
	 * @param idx
	 *           Sequence number in the internal vector
	 * @return Nroll Object at idx location in the vector or null if out of range
	 */
	public Nroll getNroll(int idx)
	{
		if(idx >= 0 && idx < nrl.size()) // Check for valid range
		{
			return nrl.get(idx);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * @param htn -> Roll Number
	 * @param subCode -> Subject Code
	 * @param marks -> Marks
	 * @param mksType -> Internal (0) / External Marks(1) 
	 * @return -> Result Code
	 * 	 1: successful
	 * 	 0: Duplicate Same Value
	 * 	-1: Duplicate with Different Value 
	 * 	-2: Sub Not Registered  
	 * 	-3: NR Not Found  
	 */
	
	public int addMarks(String htn, String subCode, int marks, int mksType)
	{
		int res = -3; // NR not found (initial guess)
		Nroll tnr = getNroll(htn);
		if(tnr != null)
		{
			res = tnr.addMarks(subCode, marks, mksType);
		}
		return res;
	}

	public int loadNrolls(String fname)
	{
		int cnt=0;
		
		try
		{
			FileReader fr = new FileReader(fname);
			BufferedReader br = new BufferedReader(fr);
			String line;
			Nroll tnr;
			int nregd = 0;
			int lineCount=0;
			String arr[];
			String lines[];
			while((line=br.readLine()) != null)
			{
				lineCount++;
				if (line.trim().equals("")) continue; // blank lines
				if (line.startsWith("#")) continue; // comment line
				arr = line.split("\t");
				if(arr.length < 7)
				{
					Utilities.showMessage("Field Count Error at line: " + lineCount + " in file: " + fname + "(" + line + ")");
					continue;
				}
				
				nregd = Utilities.parseInt(arr[6].trim());
				if(arr.length == 7 && nregd > 0) // First line doesn't contain Sub Codes 
				{
					lines = new String[nregd+1];
					lines[0] = line;
					for(int i=0;i<nregd;i++)
					{
						lines[i+1] = br.readLine().trim();
						lineCount++;
					}
					tnr = Nroll.createNroll(lines); // Create NR with Marks
				}
				else
				{
					tnr = Nroll.createNroll(line); // Create NR without Marks
				}
				
				if(tnr == null)
				{
					Utilities.showMessage("NR error around line: " + lineCount + " in file: " + fname + "(" + line + ")");
					continue;
				}
				nrl.add(tnr);
				hst.put(tnr.rollno, cnt);
				cnt++;
			}
			br.close();
			fr.close();
		}
		catch (Exception e){e.printStackTrace();}
		return cnt;
	}

	public Nroll[] getNrolls(String cc, int bc)
	{
		Vector <Nroll> x = new Vector <Nroll>();
		int n=nrl.size();
		Nroll temp;
		for(int i=0;i<n;i++)
		{
			temp = nrl.get(i);
			//System.out.println("Comapring: ("+bc+","+y+","+s+") with " + temp.bcode +","+temp.year+","+temp.sem + "--> " + temp.scode);
			if(temp.ccode.equalsIgnoreCase(cc) && 
					temp.bcode == bc)
			{
				x.add(temp);
			}
		}
	   Nroll nr[] = new Nroll [x.size()];
		for(int i=0;i<x.size();i++)
		{
			nr[i]=x.get(i);
		}
		x=null;
		return nr;
	}
	
	public Nroll[] getNrolls()
	{
	   int n = nrl.size();
	   if (n==0) 
	   {
	   	return null;
	   }
		
	   Nroll x[] = new Nroll [n];
		for(int i=0;i<n;i++)
		{
			x[i]=nrl.get(i);
		}
		return x;
	}

	public String[] getHtnos()
	{
	   int n = nrl.size();
	   if (n==0) 
	   {
	   	return null;
	   }
		
	   String x[] = new String [n];
		for(int i=0;i<n;i++)
		{
			x[i]=nrl.get(i).rollno;
		}
		return x;
	}
	
	public int getNrollCount()
	{
		return nrl.size();
	}
	
	public int getNrollCount(String scode)
	{
		int cnt = 0;
		for(int i=0;i<nrl.size(); i++)
		{
			if(nrl.get(i).isRegistered(scode)) cnt++;
		}
		return cnt;
	}
	
	public boolean saveNr(String fname)
	{
		boolean res = true;
		
		try
      {
	      FileWriter fw = new FileWriter(fname);
	      for(int i=0;i<nrl.size();i++)
	      {
	      	fw.write(nrl.get(i).toString());
	      }
	      fw.close();
      }
      catch (IOException e)
      {
	      Utilities.showMessage(e.getMessage());
	      res = false;
      }
		
		return res;
	}
	
	public boolean exportNR(String outfile)
	{
		boolean res = true;
		String ts1, ts2;
		Nroll tnr;
		Mark tm;
		String d = "\t";
		try
		{
			FileWriter fw = new FileWriter(outfile);
			for(int i=0;i<nrl.size(); i++)
			{
				 tnr = nrl.get(i);
				 tnr.resetRegdCount();
				 ts1 = tnr.rollno+
						 d+tnr.ccode+
						 d+tnr.bcode+
						 d+tnr.year+
						 d+tnr.sem;
				 for(int j= 0; j<tnr.nregd; j++)
				 {
					 tm = tnr.mks.get(j);
					 ts2 = tm.toString();
					 fw.write(ts1+d+ts2+"\n");
				 }
			}
			fw.close();
		}
		catch (Exception e)
		{
			Utilities.showMessage("Error in exporting NR: " + e.getMessage());
			res = false;
		}
		return res;
	}
	
	/*********/
	public static void main(String[] args)
	{
		Nrolls nr = new Nrolls();
		nr.createNrFileFromDb("F151501R151200", "xxx.txt");
//		int cnt = b.loadNrolls(basepath+"/A151511R151100-nr.txt");
//		System.out.println(cnt + " Nrolls loaded...");
//		Nroll x = b.getNroll("15491A0256");
//		System.out.println(x);
//		int n = b.getNrollCount("A0001");
//		System.out.println("A0501 NR Count: " + n);
	}
   /***************/
}
