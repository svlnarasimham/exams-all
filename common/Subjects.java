package common;

import java.io.*;
import java.util.*;
import utility.Utilities;

// subjects table structure
// +-------------+-----------------+
// | code | varchar(10) |
// | name | varchar(60) |
// | ccode | char(1) |
// | bcode | tinyint(4) |
// | sbatch | tinyint(4) |
// | eregulation | tinyint(4) |
// | syear | tinyint(4) |
// | ssem | tinyint(4) |
// | srs | char(1) |
// | smxe | smallint(6) |
// | smxt | smallint(6) |
// | smne | smallint(6) |
// | smnt | smallint(6) |
// | scredits | tinyint(4) |
// | stype | char(1) |
// | scat | char(1) |
// | ecode | char(14) |
// | sappeared | int(11) |
// | spassed | int(11) |
// | smod | tinyint(4) |
// | slno | smallint(6) |
// +-------------+-----------------+

public class Subjects
{
	public static final int SCODE = 0;
	public static final int SNAME = 1;
	public static final int CCODE = 2;
	public static final int BCODE = 3;
	public static final int BATCH = 4;
	public static final int REGL = 5;
	public static final int YEAR = 6;
	public static final int SEM = 7;
	public static final int RS = 8;
	public static final int MXE = 9;
	public static final int MXT = 10;
	public static final int MNE = 11;
	public static final int MNT = 12;
	public static final int CR = 13;
	public static final int TYPE = 14;
	public static final int CAT = 15;
	public static final int ECODE = 16;
	public static final int APP = 17;
	public static final int PASS = 18;
	public static final int MOD = 19;
	public static final int SLNO = 20;

	// public Vector <Subject> sub;
	public Vector<Subject> sbv;
	int nsub;

	public Subjects()
	{
		if (!G.isInitialized())
		{
			G.initialize();
		}
		nsub = 0;
		sbv = new Vector<>();
	}

	/**
	 * Fetches all subjects for an exam code and writes them to a file with proper format
	 * 
	 * @param ecode
	 *            14 character examination code
	 * @param outputFile
	 *            Output File (Ex: "subjects.dat")
	 * @return True if successful or false otherwise
	 */
	public boolean createSubjectFileFromDb(String ecode, String outputFile)
	{
		String subsql = "select scode,sname,ccode,bcode, eregulation,"
				+ "syear,ssem,smxe,smxt,smne,smnt,scredits,stype " 
				+ "from subjects "
				+ "where ecode = '" + ecode.toUpperCase() + "' " 
				+ "order by bcode,slno";
		// Output format:
		// 1.Subject Code
		// 2.Subject Title
		// 3.Course code
		// 4.Branch code
		// 5.Regulation YY
		// 6.Year
		// 7.Semester
		// 8.Max. External marks
		// 9.Max. Total marks
		// 10.Min. External marks to pass
		// 11.Min. Total marks to pass
		// 12.Credits
		// 13.Type [Theory/lab (T/L)]

		Vector<String[]> v = G.dbcon.executeSQL(subsql); // get data into array
		String res = "";
		int sz = v.size();
		int nrec = 0;
		// System.out.println("SQL: " + subsql);
		String arr[];
		if (sz == 0)
		{
			// System.out.println("SQL: " + subsql);
			System.out.println("Can not get Subject records from DB..");
			return false;
		}

		FileWriter fw = null;
		try
		{
			fw = new FileWriter(outputFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		for (int i = 0; i < v.size(); i++)
		{
			arr = v.get(i);
			res = arr[0];
			for (int j = 1; j < arr.length; j++)
			{
				res += ("\t" + arr[j]);
			}
			res += "\n";
			try
			{
				fw.write(res);
				nrec++;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				try
				{
					fw.close();
				}
				catch (Exception ee)
				{
					ee.printStackTrace();
				}
				return false;
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
		System.out.println(nrec + "Subject Records Written: " + " to file: " + outputFile);
		return true;
	}

	/**
	 * Loads Subjects from the subjects file<br/>
	 * Subject File Format (each record is a row with tab separated fields:<br/>
	 * Field 1.Subject Code <br/>
	 * Field 2.Subject Title <br/>
	 * Field 3.Course code <br/>
	 * Field 4.Branch code <br/>
	 * Field 5.Regulation YY <br/>
	 * Field 6.Year <br/>
	 * Field 7.Semester <br/>
	 * Field 8.Max. External marks <br/>
	 * Field 9.Max. Total marks <br/>
	 * Field 10.Min. External marks to pass<br/>
	 * Field 11.Min. Total marks to pass <br/>
	 * Field 12.Credits <br/>
	 * Field 13.Type [Theory/lab (T/L)] <br/>
	 * 
	 * 
	 * @param fname
	 *            Subject file name which contains the tab separated subjects list
	 * @return count of records loaded
	 */
	public int loadSubjectsFromFile(String fname)
	{
		int cnt = 0;
		try
		{
			FileReader fr = new FileReader(fname);
			BufferedReader br = new BufferedReader(fr);
			String line;
			int lineCount = 0;
			String rg, sc, st, tl, cc, bc;
			int y, s, mxe, mxt, mne, mnt, cr, app, pas, mod;
			while ((line = br.readLine()) != null)
			{
				lineCount++;
				if (line.trim().equals(""))
					continue; // blank lines
				if (line.startsWith("#"))
					continue; // comment line
				String arr[] = line.split("\t");
				if (arr.length < 13)
				{
					String msg = "Field Count Error in " + fname + " file at Line: " + lineCount;
					Utilities.showMessage(msg);
					continue;
				}

				// Field 0: Subject Code
				sc = arr[0].trim().toUpperCase();

				// Field 1: Subject Title
				st = arr[1].trim().toUpperCase();

				// Field 2: Course Code
				cc = arr[2].trim().toUpperCase();

				// Field 3: Branch Code
				bc = arr[3].trim();
				int bcVal = Utilities.parseInt(bc);
				if (bcVal < 0)
				{
					Utilities.showMessage("Branch Code Parsing Error at line: " + lineCount);
					continue;
				}

				// Field 4: Regulation
				rg = arr[4].trim();

				// Field 5: Year
				y = utility.Utilities.parseInt(arr[5].trim());
				if (y < 0)
				{
					String msg = "Year parsing error in line: " + lineCount;
					Utilities.showMessage(msg);
					continue;
				}

				// Field 6: Semester
				s = utility.Utilities.parseInt(arr[6].trim());
				if (s < 0)
				{
					String msg = "Semester parsing error in line: " + lineCount;
					Utilities.showMessage(msg);
					continue;
				}

				// Field 7: Maximum External Marks
				mxe = utility.Utilities.parseInt(arr[7].trim());
				if (mxe < 0)
				{
					String msg = "Max. Ext Marks parsing error in line: " + lineCount;
					Utilities.showMessage(msg);
					continue;
				}

				// Field 8: Maximum Total Marks
				mxt = utility.Utilities.parseInt(arr[8].trim());
				if (mxt < 0)
				{
					String msg = "Max. Total Marks parsing error in line: " + lineCount;
					Utilities.showMessage(msg);
					continue;
				}

				// Field 9: Minimum External Marks for pass
				mne = utility.Utilities.parseInt(arr[9].trim());
				if (mne < 0)
				{
					String msg = "Min. Ext Marks parsing error in line: " + lineCount;
					Utilities.showMessage(msg);
					continue;
				}

				// Field 10: Minimum Total Marks for pass
				mnt = utility.Utilities.parseInt(arr[10].trim());
				if (mnt < 0)
				{
					String msg = "Min. Total Marks parsing error in line: " + lineCount;
					Utilities.showMessage(msg);
					continue;
				}

				// Field 11: Credits
				cr = utility.Utilities.parseInt(arr[11].trim());
				if (mnt < 0)
				{
					String msg = "Credits parsing error in line: " + lineCount;
					Utilities.showMessage(msg);
					continue;
				}

				// Field 12: Theory / Lab
				tl = arr[12].trim().toUpperCase();

				// Check if it is already processed .. If so, load the values
				if (arr.length == 16)
				{
					// Field 13: Appeared
					app = utility.Utilities.parseInt(arr[13].trim());
					app = 0; // deliberately made zero

					// Field 14: Passed
					pas = utility.Utilities.parseInt(arr[14].trim());
					pas = 0;
					// Field 15: Moderation
					mod = utility.Utilities.parseInt(arr[15].trim());
				}
				else
				{
					app = 0;
					pas = 0;
					mod = 0;
				}
				sbv.add(new Subject(sc, st, cc, bcVal, rg, y, s, mxe, mxt, mne, mnt, cr, tl, app,
						pas, mod));
				cnt++;
			}
			br.close();
			fr.close();
		}
		catch (Exception e)
		{
			Utilities.showMessage("Error in Loading Subjects: " + e.getMessage());
		}
		return cnt;
	}

	public Subject[] getSubjects(String cc, int bc, int y, int s)
	{
		Vector<Subject> x = new Vector<Subject>();
		int n = sbv.size();
		Subject temp;
		for (int i = 0; i < n; i++)
		{
			temp = sbv.get(i);
			// System.out.println("Comapring: ("+bc+","+y+","+s+") with " + temp.bcode
			// +","+temp.year+","+temp.sem + "--> " + temp.scode);
			if (temp.ccode.equalsIgnoreCase(cc) && temp.bcode.equals(String.format("%02d", bc))
					&& temp.year == y && temp.sem == s)
			{
				x.add(temp);
			}
		}
		Subject subj[] = new Subject[x.size()];
		x.toArray(subj);
//		for (int i = 0; i < x.size(); i++)
//		{
//			subj[i] = x.get(i);
//		}
		//x = null;
		return subj;
	}

	public Subject[] getSubjects()
	{
		return getArray(sbv);
	}

	Subject[] getArray(Vector<Subject> subj)
	{
		if (subj.size() == 0)
		{
			return null;
		}

		Subject x[] = new Subject[subj.size()];
		subj.toArray(x);
		return x;
	}

	public String[] getSubjectCodes()
	{
		int n = sbv.size();
		if (n == 0)
		{
			return null;
		}

		String x[] = new String[n];
		for (int i = 0; i < n; i++)
		{
			x[i] = sbv.get(i).code;
		}
		return x;
	}

	public void save(String filename)
	{
		FileWriter fw = null;
		File f = new File(filename);
		if (f.exists())
		{
			f.delete();
		}
		try
		{
			fw = new FileWriter(filename);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < sbv.size(); i++)
			{
				Subject s = sbv.get(i);
				// System.out.println("Saving Subject: " + s);
				bw.write(s.toString());
				bw.write("\n");
				bw.flush();
			}
			bw.close();
			fw.close();
		}
		catch (Exception e)
		{
			Utilities.showMessage("Error in Writing subjects file: (" + e.getMessage() + ")");
		}
	}

	public int loadSubjectsFromDb(String ecode)
	{
		int cnt = 0;
		if (G.dbcon.getConnection() == null) // if still no db connection
		{
			Utilities.showMessage(null, "Can not connect to Database. Loading defaults.");
			Subject s = new Subject("XXXXX", "XXXXXX XXXXXXXXXX XXXXXXXX XXX", "X", "00", 0, 0, 0,
					0, "X", 0, 0, 0, 0, 0, "X", "X", "XXXXXXXXXXXX", -1, -1, 0, 0);
			sbv.add(s);
		}
		else
		{
			String bc, sc, sn, cc, rs, ty, ct, ec;
			int bt, er, sy, ss, xe, xt, ne, nt, cr, ap, pa, mo, sl;

			String sql = "select " + "scode, sname, ccode,bcode, sbatch, eregulation, "
					+ "syear, ssem, srs, smxe, smxt, smne, smnt, scredits, "
					+ "stype, scat, ecode, sappeared, spassed, smod, slno " + "from subjects "
					+ "where ecode = '" + ecode + "';";
			// System.out.println(sql);
			// System.out.flush();
			Vector<String[]> brVector = G.dbcon.executeSQL(sql);
			int sz = brVector.size();
			String arr[];
			if (brVector != null)
			{
				for (int i = 0; i < sz; i++)
				{
					arr = brVector.get(i);
					sc = arr[SCODE].trim().toUpperCase(); // Subject code
					sn = arr[SNAME].trim().toUpperCase(); // Subject Name
					cc = arr[CCODE].trim().toUpperCase(); // Course code
					String tmps = arr[BCODE].trim().toUpperCase();
					if (tmps.length() == 1)
					{
						tmps = "0" + tmps;
					}
					bc = tmps; // branch code
					bt = Utilities.parseInt(arr[BATCH].trim()); // batch
					er = Utilities.parseInt(arr[REGL].trim()); // Regulation
					sy = Utilities.parseInt(arr[YEAR].trim()); // Year
					ss = Utilities.parseInt(arr[SEM].trim()); // Semester
					rs = arr[RS].trim().toUpperCase(); // Regular/suppl.
					xe = Utilities.parseInt(arr[MXE].trim()); // Max External
					xt = Utilities.parseInt(arr[MXT].trim()); // Max Total
					ne = Utilities.parseInt(arr[MNE].trim()); // Min external
					nt = Utilities.parseInt(arr[MNT].trim()); // Min total
					cr = Utilities.parseInt(arr[CR].trim()); // credits
					ty = arr[TYPE].trim().toUpperCase();
					ct = arr[CAT].trim().toUpperCase();
					ec = arr[ECODE].trim().toUpperCase();
					ap = Utilities.parseInt(arr[APP].trim()); // appeared
					pa = Utilities.parseInt(arr[PASS].trim()); // passed
					mo = Utilities.parseInt(arr[MOD].trim()); // moderation
					sl = Utilities.parseInt(arr[SLNO].trim()); // serial number
					Subject s = new Subject(sc, sn, cc, bc, bt, er, sy, ss, rs, xe, xt, ne, nt, cr,
							ty, ct, ec, ap, pa, mo, sl);
					sbv.add(s);
					cnt++;
				}
			}
		}
		return cnt;
	}

	public Subject getSubject(String scode)
	{
		for(Subject sub: sbv)
		{
			if (sub.code.equalsIgnoreCase(scode))
			{
				return sub;
			}
		}
		return null;
	}

	public String getSubjectName(String scode)
	{
		Subject s = getSubject(scode);
		if (s != null)
		{
			return s.name;
		}
		return "";
	}

	public Subject getSubject(String ccode, String bcode, String scode)
	{
		ccode = ccode.toUpperCase().trim();
		bcode = bcode.trim().toUpperCase();
		if (bcode.length() == 1)
		{
			bcode = "0" + bcode; 
		}
		scode = scode.toUpperCase().trim();
		for(Subject sub: sbv)
		{
			if (sub.ccode.equals(ccode) && sub.bcode.equals(bcode) && sub.code.equalsIgnoreCase(scode))
			{
				return sub;
			}
		}
		return null;
	}

	public String getSubjectName(String ccode, String bcode, String scode)
	{
		Subject s = getSubject(ccode, bcode, scode);
		return (s == null) ? "" : s.name;
	}

	public String[] getSubjectNames(String ccode, String bcode, String scodes[])
	{
		String tn[] = new String[scodes.length];
		for (int i = 0; i < scodes.length; i++)
		{
			tn[i] = getSubjectName(ccode, bcode, scodes[i]);
		}
		return tn;
	}

	public int getSubjectCount()
	{
		return sbv.size();
	}

	public String[] sortSubCodes(String ccode, String bcode, String scodes[])
	{
		Subject tmpsub;
		int n = scodes.length;
		String sc[] = new String[n];
		for (int i = 0; i < n; i++)
		{
			String slno = "9999";
			tmpsub = getSubject(ccode, bcode, scodes[i]);
			if (tmpsub != null)
			{
				slno = String.format("%04d", tmpsub.slno);
			}
			sc[i] = slno + scodes[i];
		}
		Arrays.sort(sc);
		for (int i = 0; i < sc.length; i++)
		{
			sc[i] = sc[i].substring(4);
		}
		return sc;
	}

	/*
	 * ******** public static void main(String[] args) { String
	 * basepath="c:/workspace/qis-expro/A151511R151100"; Subjects b = new Subjects(); int cnt =
	 * b.loadSubjects(basepath+"/A151511R151100-sbv.txt"); System.out.println(cnt +
	 * " Subjects loaded..."); // Subject tmp; // String s=" : "; // for(int i=0;i<cnt;i++) // { //
	 * tmp = b.sbv.get(i); // System.out.println( // tmp.scode + s + // tmp.bcode + s + // tmp.year
	 * + s + // tmp.sem + s + // tmp.title + s + // tmp.maxmks); // } Subject x[] =
	 * b.getSubjects("A",1,1,1); for(int i=0;i<x.length;i++) { System.out.println(x[i]); } } /*
	 **************/
}
