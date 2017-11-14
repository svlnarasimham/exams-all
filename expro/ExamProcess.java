package expro;

import java.io.*;
import java.util.*;

import common.Branch;
import common.Branches;
import common.Course;
import common.Courses;
import common.G;
import common.Subject;
import utility.*;

//import javax.swing.JOptionPane;
//import com.mysql.jdbc.Util;
public class ExamProcess
{
	// Define some global variables
	public static boolean debug = true;
	public static boolean res = false;
	public static boolean overwrite = false;
	public static String statusFile="";
	public static int status=-1;
	public static int nsub = 0;
	public static int nnr = 0; 
	public static Vector<String> ev = null;
	
	/**
	 * Initialize Globals to default values
	 */
	public static void initGlobals()
	{
		// Initialize Database and Globals
		if(!Globals.isInitialized())
		{
			Globals.initialize();
		}
	}
	
	/**
	 * Initializes database using Global object's values
	 */
	public static void initDatabase()
	{
		if(!DbUtils.isInitialized())
		{
			DbUtils.initConnection(Globals.dbDriver, Globals.dbUrl,Globals.dbUser, Globals.dbPassword);
		}
	}

	/**
	 * Selects the base folder where all marks, nr and subject file reside.<br>
	 * @return Selected folder or current folder (./) after converting backslashes to slashes. 
	 */
	public static String getExamFolder()
	{
		String examFolder = utility.Utilities.chooseFile(Globals.basePath, Utilities.OPEN, Utilities.DIRS, "..*", "Exam folder");
		if(examFolder == null)
		{
			examFolder = "./";
		}
		examFolder = examFolder.replace('\\','/');
		return examFolder;
	}
	
	/**
	 * Creates Subjects file from Database for given ecode.<br>
	 * It also checks if it is already imported and prompts the user to skip or overwrite.<br>
	 * Then it loads the Subjects from the imported file.<br>
	 * If no subjects are loaded, it terminates the process.
	 */
	public static void loadSubjects()
	{
		status = utility.Utilities.getStatus(statusFile);
		if(status >= 1)
		{
			overwrite = Utilities.confirm("Subjects Already Imported from DB.. Do you want to re-import?");
		}
		if(status < 1 || overwrite)
		{
			res = Globals.subjects.createSubjectFileFromDb(Globals.ecode, Globals.subjectsFile);
			if(res)
			{
				status = 1;
				Utilities.saveStatus(statusFile, status);
			}
			else
			{
				System.out.println("Can not import subjects from Database...");
				System.exit(1);
			}
		}
		// Finally load subjects file
		nsub = Globals.subjects.loadSubjects(Globals.subjectsFile);
		if(nsub <= 0) // Check if subjects are loaded
		{
			System.out.println("Can not load any subjects from file: "+ Globals.subjectsFile);
			System.exit(1);
		}
		System.out.println("Subjects Loaded: " + nsub);
	}
	
	/**
	 * Creates Nominal Rolls file from Database.<br>
	 * It also checks if it is already imported and prompts the user to skip or overwrite.<br>
	 * Then it loads the Nominal Rolls from the imported NR file.<br>
	 * If no data is loaded, it terminates the process.
	 */
	public static void loadNominalRolls()
	{
		overwrite=false;
		if(status >= 2)
		{
			overwrite = Utilities.confirm("NR Already Imported .. Do you want to re-import?");
		}
		if(status < 2 || overwrite)
		{
			res = Globals.nr.createNrFileFromDb(Globals.ecode, Globals.nrf);
			if(res)
			{
				status = 2;
				Utilities.saveStatus(statusFile, status);
			}
			else
			{
				System.out.println("NR Importing from database failed...");
				System.exit(1);
			}
		}
		// Finally load NR from nr text file
		nnr = Globals.nr.loadNrolls(Globals.nrf);
		if(nnr <= 0)
		{
			System.out.println("Can not load any NR records from file: " + Globals.nrf);
			System.exit(1);
		}
		System.out.println("NR Loaded: "+nnr);
	}
	
	/**
	 * Creates subject-wise internal marks files from given XLS file.<br>
	 * It also checks if data is already imported and prompts the user to skip or overwrite.<br>
	 * If failed to load data, it terminates the process.
	 */
	public static void importInternalMarks()
	{
		overwrite=false;
		if(status >= 3)
		{
			overwrite = Utilities.confirm("IM Already Imported .. Do you want to re-import?");
		}
		if(status  < 3 || overwrite)  
		{
			String xlsFile = Globals.imxf;
			String targetFolder = Globals.basePath;
			res = ImportTools.importInternals(xlsFile, targetFolder);
			if(res)
			{
				status = 3;
				Utilities.saveStatus(statusFile, status);
			}
			else
			{
				System.exit(1);
			}
		}
	}
	
	/**
	 * Imports External Marks from XLS files.<br>
	 * If OMR scanning is used, this is not required. 
	 * For now, it always returns true.
	 */
	public static void importExternalMarks()
	{
		overwrite=false;
		if(status >= 4)
		{
			overwrite = Utilities.confirm("EM Already Imported .. Do you want to re-import?");
		}
		if(status  < 4 || overwrite) 
		{
			res = new Importer().importEm(); // dummy statement which always returns true. May be used in future
			if(res)
			{
				status = 4;
				Utilities.saveStatus(statusFile, status);
			}
			else
			{
				System.exit(1);// will never be executed as of now
			}
		}
	}
	
	/**
	 * Checks if all necessary files are available or not<br>
	 * If any error is detected, the process will be terminated.
	 */
	public static void checkData()
	{
		ev = new Vector<String>();
		
		///// Check Subject file
		if(!Utilities.checkFile(Globals.subjectsFile))
		{
			ev.add("Subjects File Not Found (" + Globals.subjectsFile + ") ...");
		}
		
		///// Check NR file
		if(!Utilities.checkFile(Globals.nrf))
		{
			ev.add("NR File not Found (" + Globals.nrf + ") ...");
		}
		
		////// For each subject, check IM and EM files
		HashSet<String> completed = new HashSet<String>();
		Subject s[] = Globals.subjects.getSubjects();
		if(s == null)
		{
			ev.add("No subjects loaded from " + Globals.subjectsFile);
		}
		else
		{
			int i;
			String tsc;
			for(i=0;i<s.length;i++)
			{
				tsc = s[i].scode.trim().toUpperCase();
				if(completed.contains(tsc))
				{
					continue;
				}
				completed.add(tsc);
				String imfile, emfile;
				imfile = Globals.basePath + "/" + tsc + "-im.txt";
				emfile = Globals.basePath + "/" + tsc + "-hm.txt";
				
				if(!Utilities.checkFile(imfile)) // Check IM file if internal marks exist
				{
					if(s[i].mxe < s[i].mxt)
					ev.add("IM File Not Found: " + imfile);
				}
				
				if(!Utilities.checkFile(emfile)) // Check EM file if external marks exist
				{
					if(s[i].mxe > 0)
					{
						ev.add("EM File Not Found: " + emfile);
					}
				}
			} // End of for subject loop
		}// end of else
		
		if(ev.size() > 0)
		{
			String errmsg = "Errors Present....\n";
			for(int i=0; i<ev.size(); i++)
			{
				errmsg += (ev.get(i)+"\n");
			}
			Utilities.showMessage(errmsg);
			System.exit(1); // Don't continue;
		}
		else
		{
			Utilities.showMessage("Data Validation OK. Posting Marks into records .....");
		}
	}
	
	/**
	 * Posts internal marks and missing marks into the NR structure
	 * @param sb Subjects Array
	 */
	public static void postInternalAndMissingMarks(Subject sb[])
	{
		int i;
		HashSet<String> cs = new HashSet<String>(); // Hashset for completed subjects 
		overwrite = false;
		cs.clear(); // clear hash set
		if(status >= 5)
		{
			overwrite = Utilities.confirm("IM already loaded... Do you want to load again?");
		}
		
		if(status  < 5 || overwrite) 
		{
			for(i=0;i<nsub;i++) // for each subject
			{
				if(cs.contains(sb[i].scode)) // subject already processed
				{
					continue;
				}
				String fileName = Globals.basePath+"/"+sb[i].scode + "-im.txt";
				ev = postMarks(fileName, sb[i].scode, Nroll.IM);
				cs.add(sb[i].scode);
			}
			if(ev.size()> 0)
			{
				Utilities.showMessage("Errors Present in IM");
				Utilities.saveVector(Globals.errf, ev);
				System.exit(1);
			}

			/////////// Post Missing Internal marks
			ev = postMissingMarks();
			if(ev.size()> 0)
			{
				Utilities.showMessage("Errors in Missing Marks (See errors.txt file).. Ignoring some data");
				Utilities.saveVector(Globals.errf, ev);
			}
			
			ev = checkIM();
			if(ev.size() > 0)
			{
				Utilities.showMessage("Errors in Internal Marks .. Clear them first...");
				Utilities.saveVector(Globals.errf, ev);
				System.exit(1);
			}
			
			Globals.nr.saveNr(Globals.nrwmf);
			status = 5;
			Utilities.saveStatus(Globals.stf, status);
			Globals.nr.saveNr(Globals.nrwmf);
			Utilities.showMessage("Internal Marks loaded successfully...");
		}
	}
	
	/**
	 * Posts external marks into NR structure
	 * @param sb Subjects array
	 */
	public static void postExternalMarks(Subject sb[])
	{
		int i;
		HashSet<String> cs = new HashSet<String>(); // Hash set for marking completed subjects
		overwrite = false;
		cs.clear(); // clear hash set
		if(status >= 6)
		{
			overwrite = Utilities.confirm("EM already loaded... Do you want to load again?");
		}
		
		if(status  < 6 || overwrite) 
		{
			for(i=0;i<nsub;i++)
			{
				if(cs.contains(sb[i].scode)) // subject already processed
				{
					continue;
				}
				String fileName = Globals.basePath+"/"+sb[i].scode + "-hm.txt";
				ev = postMarks(fileName, sb[i].scode, Nroll.EM);
				cs.add(sb[i].scode);
			}
			
			if(ev.size()> 0)
			{
				Utilities.showMessage("Errors Present in EM");
				Utilities.saveVector(Globals.errf, ev);
				System.exit(1);
			}
			status = 6;
			Utilities.saveStatus(Globals.stf, status);
			Globals.nr.saveNr(Globals.nrwmf);
			Utilities.showMessage("External Marks loaded successfully...");
		}
	}
	
	public static void computeModeration(Subject sb[])
	{
		int i,j;
		HashSet<String> cs = new HashSet<String>();
		overwrite = false;
		cs.clear(); // clear hash set
		if(status >= 7)
		{
			overwrite = Utilities.confirm("Moderation already done... Do you want to redo?");
		}
		
		if(status  < 7 || overwrite) 
		{
			/******************* Initializing Subjects ******************/
			for(i= 0; i < sb.length; i++) // initialize subject counts
			{
				sb[i].appeared = 0;
				sb[i].passed = 0;
				sb[i].moderation = 0;
			}
			
			Subject sub;
			Nroll tnr;
			Marks tmk;
			int passCount;
			
			/******************** SUBJECT MODERATION SECTION **************/
			for(i=0;i< nsub;i++)
			{
				sub = sb[i];
				sub.passed = 0;
				if(cs.contains(sb[i].scode)) // subject already processed
				{
					for(j=0;j<i;j++) // Find sub code where the data already available
					{
						if(sb[j].scode.equalsIgnoreCase(sub.scode)) break;
					}
					
					sub.appeared = sb[j].appeared;
					sub.passed = sb[j].passed;
					sub.moderation = sb[j].moderation;
					continue;
				}
				
				cs.add(sub.scode); // Add this sub code to completed subjects list
				sub.appeared = Globals.nr.getNrollCount(sub.scode); // Set appeared 
				//System.out.println("Appeared: " + sub.scode + "  " + sub.appeared);
				
				passCount=0;
				for(j=0;j<nnr;j++)
				{
					tnr = Globals.nr.nrl.get(j);
					tmk = tnr.getMarks(sub.scode);
					if(tmk == null) 
					{ 
						continue;
					}
					if(tmk.em >= sub.mne) 
					{
						passCount++;
					}
				}
				sub.passed = passCount; // Set passed
				if(sub.appeared == 0) // No students appeared, hence moderation is 0 
				{
					sub.moderation=0;
					continue;
				}

				int passPercent =(int) ((((double)sub.passed)/((double)sub.appeared))*100.0);
				
				if(sub.tl.equals("T")) // Moderation only for theory subjects
				{
					sub.moderation = getSubModeration(passPercent, sub.mxe);
					//System.out.println("Moderation for subject: " + sub.scode + " : " + sub.moderation);
				}
				else // It is a lab, so don't compute moderation
				{
					sub.moderation = 0;
				}
			}
			
			//System.out.println("Passed in : " + Globals.subjects.sub.get(0).scode + "   " + Globals.subjects.sub.get(0).passed );
			Globals.subjects.save(Globals.subjectsFile); // Save subjects with Moderation
			Globals.subjects.loadSubjects(Globals.subjectsFile);
			printModTable(sb, Globals.basePath+"/"+"Modtable-01.txt", "Moderation Table: 1");
			status = 7;
			Utilities.saveStatus(Globals.stf, status);
			Utilities.showMessage("Moderation computed successfully...");
		}
	}
	
	/**
	 * Processes Data and computes results for each candidate
	 */
	public static void processData(Subject sb[])
	{
		int i;
		overwrite = false;
		Hashtable<String, Integer> spmht = loadSpecialModeration(Globals.basePath+"/"+ "spmod.txt");
		
		if(status >= 8)
		{
			overwrite = Utilities.confirm("Already Process... Do you want to reprocess?");
		}
		
		if(status  < 8 || overwrite) 
		{
			Nroll nra[] = Globals.nr.getNrolls();
			for(i=0;i<nra.length;i++)
			{
				process(nra[i],spmht);
			}
			Globals.nr.saveNr(Globals.nrwmf);
			Globals.subjects.save(Globals.basePath+"/"+ Globals.ecode + "-sub2.txt");
			printModTable(sb, Globals.basePath+"/"+"Modtable-02.txt", "Moderation Table: 2 (After Moderation)");
			
			Globals.nr.exportNR(Globals.basePath+"/"+Globals.ecode+"-mks.txt");
			status = 8;
			Utilities.saveStatus(Globals.stf, status);
			Utilities.showMessage("Results processed successfully...");
		}
	}
	
	/**
	 * Generates result sheet from the processed data
	 * @param sb Subjects array
	 */
	public static void generateResult(Subject sb[])
	{
		int i,j,k;
		if(status >= 9)
		{
			overwrite = Utilities.confirm("Result Sheet Generated... Do you want to regenerate?");
		}
		
		if(status  < 9 || overwrite) 
		{
			Courses crs = new Courses();
			crs.loadCourses();
			String courseCode = Globals.ecode.substring(0,1).toUpperCase();
			System.out.println("Course Code: " + courseCode);
			Course xx = crs.getCourse(courseCode);
			String crsName = "XXX XXX XXX";
			if(xx != null)
			{
				crsName = xx.sname;
			}
			
			Branches brn = new Branches();
			brn.loadBranches(courseCode);
			Branch brns[] = brn.getBranches(courseCode);
			if(brns == null)
			{
				Utilities.showMessage("Can not load Branches for course: " + courseCode);
				System.exit(1);
			}
	
			int year = sb[0].year;
			int sem = sb[0].sem;
			
			Hashtable<Integer, Integer> apht = loadAllPassValues(Globals.apvf);
			if(apht == null)
			{
				apht = new Hashtable<Integer, Integer>();
			}
			
			Nroll tnr;
			String psubs[];
			String brsubCodes[];
			int allpass;
			
			Hashtable<String, Vector<String>> htsubs = new Hashtable<String, Vector<String>>();
			
			for(i=0; i<brns.length; i++) // For each Branch
			{
				String brName = brns[i].bname;
				Nroll nrs[] = G.nr.getNrolls(courseCode, brns[i].bcode);
				Subject subs[] = G.subjects.getSubjects(courseCode, brns[i].bcode, year, sem);
				htsubs.clear();
				htsubs.put("ALL", new Vector<String>()); // Add All pass vector first
				
				brsubCodes = null;
				brsubCodes = new String[subs.length]; // to Store branch Subject Codes only
				for(j=0;j<subs.length; j++) 
				{
					brsubCodes[j]= new String(subs[j].scode); // copy sub codes into an array
					htsubs.put(subs[j].scode, new Vector<String>()); // Create a vector for each subcode
				}
				
				allpass = apht.get(brns[i].bcode);
				
				for(j=0;j<nrs.length; j++)
				{
					tnr = nrs[j];
					psubs = tnr.getPassSubjects();
					if(psubs.length == allpass) // All pass
					{
						htsubs.get("ALL").add(tnr.rollno);
						continue;
					}
					for(k=0;k<psubs.length; k++)
					{
						htsubs.get(psubs[k]).add(tnr.rollno);
					}
				}
				boolean ovrw = ((i==0) ? false : true);
				printResults(crsName, brName, htsubs, subs, Globals.resf, ovrw);
			}
			status = 9;
			Utilities.saveStatus(Globals.stf, 9);
			Utilities.showMessage("Result sheet Generated ..." + Globals.resf);
		}

	}

	public static void printResults(String crs, String brn, Hashtable<String,Vector<String>> hts, Subject s[], String fname, boolean overwrite)
	{
		int i, j;
		FileWriter fw;
		Vector <String> tv;
		try
		{
			fw = new FileWriter(fname, overwrite);
			fw.write("*****************************************************\n");
			fw.write("* Course: " + crs + " : Branch : " + brn + "\n");   
			fw.write("*****************************************************\n");
			
			/// Print All Pass first
			fw.write("\nPassed in all subjects\n");
			fw.write("================================================\n");
			
			tv = hts.get("ALL");
			for(i=0;i< tv.size(); i++)
			{
				if(i> 0 && i%8 == 0)
				{
					fw.write("\n");
				}
				fw.write(tv.get(i)+"    ");
			}
			fw.write("\n\n");
			
			for(i=0;i<s.length; i++) // For rest of subjects
			{
				fw.write("Passed in subject: " + s[i].scode + ".." + s[i].title + "\n");
				fw.write("..........................................................\n");
				
				tv = hts.get(s[i].scode);
				for(j=0;j < tv.size(); j++)
				{
					if(j> 0 && j%8 == 0)
					{
						fw.write("\n");
					}
					fw.write(tv.get(j)+"    ");
				}
				fw.write("\n\n");
			}
			fw.close();
		}
		catch(Exception e){
			Utilities.showMessage("Error in writing results.." + e.getMessage());
		}
	}
	
	//////////////////////////// Process one Student Record ///////////////////
	/**
	 * Processes one student's record
	 * @param nrl Nominal Roll Structure of the student
	 * @param spmht Special Moderation Hash table
	 */
	public static void process(Nroll nrl, Hashtable<String, Integer> spmht)
	{
		Subject sub;
		int graceMarks = 0;
		int i;
		int im, em, sm, gm, spm, smLimit;
		int t1, t2;
		//******** Initializing Section **********
		graceMarks = getGraceMarks(); // 5 marks at present
		spm = 0; // For now special moderation is not implemented ....
		
		Marks mk;
	   for(i=0;i<nrl.nregd;i++) // For each subject
		{
	   	mk = nrl.mks.get(i);
	   	sub = Globals.subjects.getSubject(mk.scode); // get subject for that code
	   	if(sub == null)
	   	{
	   		Utilities.showMessage("Can not find subject : " + mk.scode + " for student :" + nrl.rollno);
	   		System.exit(1);
	   	}
	   	spm = getSpecialModeration(spmht, mk.scode);
	   	smLimit = getSubModLimit(sub.mxe); // get max general moderation that can be given
	   	
	   	int imz = mk.im;
	   	im = Math.max(0, imz); // set IM to at least 0
	   	em = mk.em;
	   	if(sub.mxe <= 0)
	   	{
	   		em = 0;
	   	}
	   	
	   	if(em < 0) // Absent case and hence don't consider
	   	{
	   		mk.res = "F";
	   		mk.cr = 0;
	   		mk.tm = mk.getTotal();
	   		continue;
	   	}

	   	// External marks present and hence continue
   		sm = Math.min(sub.moderation, (sub.mxe - em)); // Find minimum sub moderation
   		spm = Math.min(spm, sub.mxe-(em+sm));
   		t1 = sub.mne - (em+sm+spm); // extra marks required to pass external
   		t2 = sub.mnt - (im+em+sm+spm); // extra marks required to pass total
   		
   		if(t1 <= 0  && t2 <= 0) // Directly passed
   		{
   			mk.im = imz;
   			mk.sm = sm;
   			mk.gm = 0;
   			mk.spm = spm;
   			mk.tem = mk.getTotalEm();
   			mk.tm = mk.getTotal();
   			mk.cr = sub.credits;
   			mk.res = "P";
   		}
   		else // Check if possible to pass with moderation and grace marks
   		{
   			if(!sub.tl.toUpperCase().equals("T")) // If not theory fail and continue
   			{
   		   		mk.res = "F";
   		   		mk.cr = 0;
   		   		mk.tem = mk.getTotalEm();
   		   		mk.tm = mk.getTotal();
   		   		continue;
   			}
   			t1 = Math.max((sub.mne - (em + sm + spm)), (sub.mnt - (im + em + sm + spm))); // find required marks
   			if(t1 <= graceMarks && t1 <= smLimit ) // check if grace marks are sufficient to pass
   			{
   				gm = t1;
   				graceMarks -= gm;
   				mk.im = imz;
   				mk.sm = sm;
   				mk.gm = gm;
   				mk.spm =spm;
   				mk.tem = mk.getTotalEm();
   				mk.tm = mk.getTotal();
   				mk.cr = sub.credits;
   				mk.res = "P";
   				sub.passed++; // passed because of moderation
   				//System.out.println("Passed with Moderation .. " + nrl.htno + " Sub: " + mk.scode);
   			}
   			else // failed even after moderation and grace marks
   			{
   				mk.im = imz;
   				mk.sm = 0;
   				mk.gm = 0;
   				mk.spm = 0;
   				mk.tem = mk.getTotalEm();
   				mk.tm = mk.getTotal();
   				mk.cr = 0;
   				mk.res = "F";
   			} // end of failed even after moderation block
   		} // end of pass block
		} // End of for subject loop
		return;		
	}
	
	/**
	 * Prints Moderation Table
	 * @param sb Subjects array
	 * @param fname File name where result should be stored
	 * @param comment Any Comment that should be printed.
	 */
	public static void printModTable(Subject sb[], String fname, String comment)
	{
		try
		{
			FileWriter fw = new FileWriter(fname);
			String str;
			String line = "------------------------------------------------------------------";
			String header = String.format("%6s %8s %6s %6s %4s %s", "Code", "Appeared", "Passed", "%", "Mod","Subject");
			Subject s;
			//String d = "\t";
			double passPercent;
			fw.write(line+"\n");
			fw.write(header+"\n");
			fw.write(line+"\n");
			for(int i=0;i<sb.length;i++)
			{
				s = sb[i];
				if(s.appeared> 0)
				{
					passPercent = (s.passed*100.0)/s.appeared;
				}
				else
				{
					passPercent = 0.00;
				}
				str = String.format("%6s %8d %6d %6.2f %4d %s", s.scode, s.appeared, s.passed, passPercent, s.moderation,s.title);
				fw.write(str+"\n");
			}
			fw.write(line+"\n");
			if(comment != null && !comment.equals(""))
			{
				fw.write("**" + comment);
			}
			fw.close();
		}
		catch(Exception e)
		{
			Utilities.showMessage("Can not write Moderation table ...");
		}
	}

	/**
	 * Checks if all internal marks are present. <br>
	 * If any marks are missing, they are stored in a string vector and returned.
	 * @return Vector of string, each containing one missing mark.
	 */
	public static Vector <String> checkIM()
	{
		Vector <String> ev = new Vector<String>();
		int nnr = Globals.nr.getNrollCount();
		Nroll tnr;
		Marks tmk;
		Subject tsub;
		for(int i=0;i<nnr;i++)
		{
			tnr = Globals.nr.nrl.get(i);
			tnr.resetRegdCount(); // Recount registered subjects
			for(int j=0;j<tnr.nregd;j++)
			{
				tmk = tnr.mks.get(j);
				tsub = Globals.subjects.getSubject(tmk.scode);
				if(tsub.mxe == tsub.mxt) // No internal marks for this subject
				{
					tmk.im = Marks.NOMARKS;
				}
				else
				{
					if(tmk.im < -2) // -1:ab, -2:wh
					{
						ev.add("IM Not found for: " + tnr.rollno + "\t" + tmk.scode+ "\t");
					}
				}
			}
		}
		return ev;
	}
	/**
	 * Read marks from file and post it to NR structure
	 * @param filename Marks file name where data is in ROLLNO TAB Marks format per line
	 * @param subCode Subject Code
	 * @param mksType Type of Marks (EM:Theory and IM:Practicals are valid values)  
	 * @return Errors Vector which contains error messages if any.
	 */
	
	public static Vector <String> postMarks(String filename, String subCode, int mksType)
	{
		Vector <String> ev = new Vector<String>();
		String s;
		int lc = 0, tmpmks, res;
		Subject tmps = Globals.subjects.getSubject(subCode);
		// Check if marks exist for the subject
		// If max external marks = 0 and type = External, there are no external marks
		// If Max external marks = max total marks and type = Internal, there are no internal marks
		if((tmps.mxe == 0 && mksType == Nroll.EM) || 
			(tmps.mxe == tmps.mxt && mksType == Nroll.IM))
		{
			return ev; // simply return error vector without any elements
		}
		
		try
		{
			// Open file for reading
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			while((s = br.readLine()) != null) // for each line
			{
				lc++;
				if(s.trim().startsWith("#")) continue; // Comment lines
				if(s.trim().length() < 12) continue; // HT length + tab + at least one digit for marks
				String arr[] = s.split("\t");
				if(arr.length < 2)
				{
					ev.add("Error in field count at line: " + lc + " in file: " + filename);
					continue;
				}
				tmpmks = parseMarks(arr[1].trim());
				res = Globals.nr.addMarks(arr[0], subCode, tmpmks, mksType);
				switch(res)
				{
					case -3:
						ev.add("HTNO " + arr[0] + " Not found in " + Globals.nrf); 
						break;
					case -2:
						ev.add(arr[0] + " Not Registered Subject: " + subCode + " but Int. marks present");
						break;
					case -1:
						ev.add("HTNO: " + arr[0] + " Subject: " + subCode + " duplicate internal marks with different values");
						break;
					case 0:
					case 1:
						break;
				}
			}
			br.close();
			fr.close();
		}
		catch (Exception e)
		{
			Utilities.showMessage("Error in importing Marks for " + subCode + ": " + e.getMessage());
			ev.add("Error in importing " + ((mksType==Nroll.EM)?"External " : "Internal ") + "Marks of subject: " + subCode + "at line: " + lc);
		}
		return ev;
	}
	
	public static Vector <String> postMissingMarks()
	{
		Vector <String> ev = new Vector<String>();
		String s;
		String subCode="";
		int lc = 0, tmpmks, res;
		if(!Utilities.checkFile(Globals.misf))
		{ 
			return ev; // No missing marks file, hence, return 
		}
		
		try
		{
			// Open file
			FileReader fr = new FileReader(Globals.misf);
			BufferedReader br = new BufferedReader(fr);
			while((s = br.readLine()) != null) // for each line
			{
				lc++;
				if(s.trim().startsWith("#")) continue; // Comment lines
				if(s.trim().length() < 15) continue; // HT length + tab + subcode + tab + marks
				String arr[] = s.split("\t");
				if(arr.length != 3)
				{
					ev.add("Error in field count (should be 3 {htno<tab>subcode<tab>mks}) at line: " + lc + " in file: " + Globals.misf);
					continue;
				}
				subCode = arr[1].trim().toUpperCase();
				tmpmks = parseMarks(arr[2]);
				res = Globals.nr.addMarks(arr[0], subCode, tmpmks, Nroll.IM);
				if(res == -1)
				{
						ev.add("HTNO: " + arr[0] + " Subject: " + subCode + " duplicate internal marks with different values.. Skipping");
				}
			}
			br.close();
			fr.close();
		}
		catch (Exception e)
		{
			Utilities.showMessage("Error in importing IM from: " + Globals.misf + ": " + e.getMessage());
			ev.add("Error in importing Internal Marks from: " + Globals.misf + "at line: " + lc);
		}
		return ev;
	}
	
	//////////Get Subject Moderation /////////////
	static int getSubModeration(int pp, int maxemks)
	{
		int mod=0;
		/************************/
		if(pp < 50 )
		{
			if(pp >= 30) // between 50% and 30%
			{
				mod = (int)(maxemks * 0.05 + 0.5);
			}
			else
			{
				mod = (int)(maxemks*0.1 + 0.5);
			}
		}
		/************************/
		return mod;
	}
	
	///////// Grace marks allowed (Institutional grace marks) 
	public static int getGraceMarks()
	{
		int grm = 5;
		return grm;
	}
	
	//////////Get special moderation given to this subject (if any) //////
	// As of now, there is no provision
	public static int getSpecialModeration(Hashtable<String, Integer>ht, String sc)
	{
		int t;
		if(ht == null)
		{
			t = 0;
		}
		else
		{
			Integer x = ht.get(sc);
			t = (x == null) ? 0 : x;
		}
		return t;
	}
	
	/**
	 * Loads Special moderation from file (subcode \t moderation marks format)
	 * @param spmfName Text file name which contains special moderation values
	 * @return special moderation hash table.
	 */
	public static Hashtable <String, Integer> loadSpecialModeration(String spmfName) 
	{
	  	Hashtable<String, Integer> hts =  new Hashtable<String, Integer>();
   	String skey;
   	int val;
   	String line;
   	
		File f = new File(spmfName);
		if(f.exists())
		{
			try
			{
				FileReader fr = new FileReader(spmfName);
				BufferedReader br = new BufferedReader(fr);
				while((line = br.readLine())!= null)
				{
					String arr[] = line.split("\t");
					if(arr.length == 2)
					{
						val = Utilities.parseInt(arr[1]);
						skey = arr[0].trim().toUpperCase();
						hts.put(skey, val);
					}
				}
				br.close();
				fr.close();
			}
			catch(Exception e)
			{
				// do nothing
			}
		}
   	return hts;
	}

	/**
	 * Gets all pass value for the given branch
	 * @param ht Hash table containing branch vs all pass values
	 * @param branchCode Branch code for which all pass value is required
	 * @return all pass value or zero
	 */
	public static int getAllPassValue(Hashtable<String, Integer>ht, int branchCode)
	{
		int t;
		if(ht == null)
		{
			t = 0;
		}
		else
		{
			Integer x = ht.get(branchCode);
			t = (x == null) ? 0 : x;
		}
		return t;
	}
	
	/**
	 * Gets branch code wise all pass value
	 * @param apvfName All pass values file name (each line contains bcode\tvalue format)
	 * @return Hashtable that contains bcode as key and allpass value as value
	 */
	public static Hashtable <Integer, Integer> loadAllPassValues(String apvfName)
	{
	  	Hashtable<Integer, Integer> hti =  new Hashtable<Integer, Integer>();
   	Integer ikey;
   	int val;
   	String line;
   	
		File f = new File(apvfName);
		if(f.exists())
		{
			try
			{
				FileReader fr = new FileReader(apvfName);
				BufferedReader br = new BufferedReader(fr);
				while((line = br.readLine())!= null)
				{
					String arr[] = line.split("\t");
					if(arr.length == 2)
					{
						ikey = Utilities.parseInt(arr[0]);
						val = Utilities.parseInt(arr[1]);
						hti.put(ikey, val);
					}
				}
				br.close();
				fr.close();
			}
			catch(Exception e)
			{
				// do nothing
			}
		}
   	return hti;
	}
	


	/////////// Find Maximum marks that can be added to a subject ///
	/**
	 * Computes 4% of max external marks which is the limit for subject moderation
	 * @param maxExtMks
	 * @return
	 */
	static int getSubModLimit(int maxExtMks)
	{
		int m = (int) (maxExtMks*0.04 + 0.5);
		return m;
	//return 2;
	}

	/**
	 * Parses marks field
	 * @param mks Marks String 
	 * @return parsed number or -1 if Absent, -2 if Withheld, -3 if Parse Error 
	 */
	static int parseMarks(String mks)
	{
		int n = Marks.DEFAULT;
		mks = mks.trim().toUpperCase();
		if(mks.startsWith("A") || mks.startsWith("-"))
		{
			n = Marks.ABSENT;// -1: Absent
		}
		else if(mks.startsWith("W"))
		{
			n = Marks.WITHHELD; // -2: Withheld
		}
		else
		{
			try
			{
				n = Utilities.parseInt(mks);
			}
			catch (NumberFormatException nfe)
			{
				System.out.println("Can not parse marks '"+ mks + "'");
			}
		}
		return n;
	}
	
	/**
	 * This is the actual processing program which executes the steps in correct sequence.
	 */
	public static void doProcess()
	{
		// ///////////////// Step 1: Initialize globals
		initGlobals();
		// ///////////////// Step 2: Initialize Database
		initDatabase();
		
		// ///////////////// Step 3: Select Exam Folder
		String examFolder = getExamFolder();
		
		// ///////////////// Step 4: Get Exam Code from folder name's last part
		String ecode = ExamSelector.getExamCode(examFolder); 
		if(ecode == null || ecode.equals(""))// Validate code
		{
			String msg = "Folder name must end with Examcode. "
					+ "Can not continue without Exam Code...";
			Utilities.showMessage(msg);
			return;
		}
		// ///////////////// Step 5: Initialize all default exam names like subjects file, nr file, output files etc.
		Globals.setExamFileNames(examFolder, ecode);
		statusFile = Globals.stf;
		Utilities.showMessage("Using Base Folder: " + examFolder);
		
		// ///////////////// Step 6: Import subjects if necessary 
		// create subjects file from DB / load subjects from text file
		// if subjects file can not be created, the process is terminated.
		loadSubjects();
		
		// ///////////////// Step 7: Import NR file from xls
		// If necessary create NR file from database and then load data.
		// If it can not load any data, it terminates the process.
		loadNominalRolls();
		
		// ///////////////// Step 8: Import IM file from xls into individual text files
		importInternalMarks();

		// ///////////////// Step 9: Import EM files from xls
		// // This stage is not required if OMR validation is used, 
		// // since the em files are generated there
		importExternalMarks();
		
		// Processing steps are executed irrespective of status after 4
		// //////////////// Step 10: Process the examination data 
		// Step 10A. check data
		checkData();
		
		// Step 10B. Get Subjects into an array
		Subject sb[] = Globals.subjects.getSubjects(); // get subject array

		///// Step 5: Post IM to NR
		postInternalAndMissingMarks(sb);
		
		// Step 6: Post EM
		postExternalMarks(sb);
		
		// Step 7: Find Moderation
		computeModeration(sb);
		
		// Step 8: Process Data
		processData(sb);
		
		////////////////// Step 9: Generate Results Sheet
		generateResult(sb);
		
		// Step 10: Marks Memos Generated
		
		
		// Step 11: Recounting Update
		
		// Step 12: Marks Exported to DB and Process Frozen
		
	} //////////// End of Main Processing section
	
	public static void main(String args[])
	{
		doProcess();
	}
}
