package common;

import com.sun.webkit.Utilities;

public class Subject implements Comparable<Subject>
{
	public String code;    // Subject Code
	public String name;    // Subject Name
	public String ccode;    // Course Code
	public String bcode;    // Branch Code
	public int batch;    	// Batch
	public int regl;      	// Regulation Year
	public int year;        // Year
	public int sem;         // Semester
	public String rs; 		// Regular/Suppl
	public int mxe;   		// Maximum marks
	public int mxt;   		// Maximum Total
	public int mne;   		// Minimum external marks to pass
	public int mnt;   		// minimum total marks to pass
	public int cr; 			// Credits
	public String type; 		// Theory (T) or Lab(L) or drawing(D) ...
	public String cat; 		// Core(C)/Elective(E)/Open Elective (O) ...
	public String ecode; 	// Exam Code
	public int appeared;    // number of appeared candidates
	public int passed;      // number of passed candidates;
	public int moderation;  // moderation given
	public int slno; 			// Serial number for order

	public Subject()
	{
		this("","","","",0,0,0,0,"",0,0,0,0,0,"","","",-1,-1,-1,0);
	}
	
	/**
	 * Constructor 
	 * @param ccode Subject Code
	 * @param bname Subject Name
	 * @param ccode Course Code (A/D/E/F ..)
	 * @param bcode Branch Code (01, 02, ...)
	 * @param batch Batch year (YY)
	 * @param regl Regulation Year
	 * @param year Year (0/1/2/3/4)
	 * @param sem Semester (0/1/2)
	 * @param rs Regular/Suppl
	 * @param mxe Maximum External marks
	 * @param mxt Maximum Total
	 * @param mne Minimum external marks to pass
	 * @param mnt Minimum total marks to pass
	 * @param cr Credits
	 * @param type Type [Theory (T) or Lab(L) or drawing(D) ...]
	 * @param cat Catetory [Core(C)/Elective(E)/Open Elective (O) ...]
	 * @param ecode Exam Code 
	 * @param appeared number of candidates appeared 
	 * @param passed number of candidates passed
	 * @param moderation moderation given
	 * @param slno Serial number for order
	 */
	public Subject(
			String scode,
			String sname,
			String ccode,
			String bcode,
			int batch,
			int regl,
			int year,
			int sem,
			String rs,
			int mxe,
			int mxt,
			int mne,
			int mnt,
			int cr,
			String type, 
			String cat, 
			String ecode,
			int appeared,
			int passed,
			int moderation,
			int slno)
	{
		this.code = scode;
		this.name = sname;
		this.ccode = ccode;
		this.bcode = bcode;
		this.batch = batch;
		this.regl = regl;
		this.year = year;
		this.sem = sem;
		this.rs = rs;
		this.mxe = mxe;
		this.mxt = mxt;
		this.mne = mne;
		this.mnt = mnt;
		this.cr = cr;
		this.type = type;
		this.cat = cat;
		this.ecode = ecode;
		this.appeared = appeared;
		this.passed = passed;
		this.moderation = moderation;
		this.slno = slno;
	}
	/**
	 * Returns subject as a tab separated string
	 */
	public String toString()
	{
		String temp = code+"\t"+
				name+"\t"+
				ccode +"\t"+
				bcode+"\t"+
				batch+"\t"+
				regl+"\t"+
				year+"\t"+
				sem+"\t"+
				rs +"\t"+
				mxe +"\t"+
				mxt +"\t"+
				mne +"\t"+
				mnt +"\t"+
				cr +"\t"+
				type+"\t"+
				cat +"\t"+
				ecode +"\t"+
				appeared +"\t"+
				passed +"\t"+
				moderation +"\t"+
				slno;
		return temp;
	}
	
	public int compareTo(Subject other)
	{
		return slno - other.slno;
	}
	
	public Subject(
			String scode, 
			String title, 
			String ccode,
			int bcode, 
			String reg, 
			int year, 
			int sem,
			int maxExtMks,
			int maxTotMks,
			int minExtMks,
			int minTotMks,
			int credits,
			String tl,
			int appeared,
			int passed,
			int moderation)
	{
		this(scode, title,ccode,""+bcode, 
				0, utility.Utilities.parseInt(reg), year,sem, 
				"R",
				maxExtMks,maxTotMks,minExtMks,minTotMks,credits,
				tl, "X", "",
				appeared,passed,moderation, 0);
	}
	
	public Subject(
			int slno,
			String scode, 
			String title, 
			String ccode,
			int bcode, 
			String reg, 
			int year, 
			int sem,
			int maxExtMks,
			int maxTotMks,
			int minExtMks,
			int minTotMks,
			int credits,
			String tl,
			int appeared,
			int passed,
			int moderation)
	{
		this(scode, title,ccode,""+bcode, 
				0, utility.Utilities.parseInt(reg), year,sem, 
				"R",
				maxExtMks,maxTotMks,minExtMks,minTotMks,credits,
				tl, "", "",
				appeared,passed,moderation, slno);
	}
	
}
