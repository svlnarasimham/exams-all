package common;

import utility.Utilities;

public class Mark
{
	public static final int ABSENT   = -1;
	public static final int NOMARKS  = -2;
	public static final int WITHHELD = -3; // Uninitialized
	public static final int DEFAULT  = -4; // Uninitialized
	public static final int UNKNOWN  = -4; // Uninitialized
	
	public String scode;
	public String sname;
	public int im; // internal marks
	public int em; // external marks
	public int sm; // subject moderation
	public int gm; // grace marks
	public int spm; // special moderation
	public int tem; // Total External Marks
	public int tm; // total marks
	public int cr; // Credits
	public String grade; // Grade
	public int gradePoints; // Grade Points for this grade
	public String res; // result (P/F)
	public int slno;
	public int scr;
	
	/**
	 * Default constructor
	 */
	public Mark()
	{
		scode="";
		sname="";
		res= "F";
		grade = "F";
		im = DEFAULT;
		em = tm = tem = DEFAULT;
		cr = sm = gm = spm = slno = scr = gradePoints = 0;
	}

	/**
	 * Constructor
	 * @param scode Subject Code
	 * @param im Internal Marks
	 * @param em External Marks
	 * @param sm Subject moderation
	 * @param gm Grace marks
	 * @param spm Special Moderation if any
	 * @param tem Total external marks
	 * @param tm Total marks
	 * @param cr Credits
	 * @param res Result
	 * @param grade Grade obtained
	 * @param slno Serial Number of the subject in the required print order
	 */
	public Mark(String scode, int im, int em, int sm, int gm, 
			int spm, int tem, int tm, int cr, String res, String grade, int slno)
	{
		this.scode = scode.trim().toUpperCase();
		this.sname = "";
		this.im = im;
		this.em = em;
		this.sm = sm;
		this.gm = gm;
		this.spm = spm;
		this.tem = tem;
		this.tm = tm;
		this.cr = cr;
		this.res = res;
		this.grade = grade;
		this.gradePoints = 0;
		this.slno = slno;
		this.scr = 0;
	}

	public Mark(String scode, int im, int em, int sm, int gm, 
			int spm, int tem, int tm, int cr, String res, String grade)
	{
		this(scode, im, em, sm, gm, spm, tem, tm, cr, res, grade, 0);
	}
	
	/**
	 * Constructor
	 * @param scode Subject Code
	 * @param im Internal Marks
	 * @param em External Marks
	 * @param sm Subject moderation
	 * @param gm Grace marks
	 * @param spm Special Moderation if any
	 * @param tem Total external marks
	 * @param tm Total marks
	 * @param cr Credits
	 * @param res Result
	 */
	public Mark(String scode, int im, int em, int sm, int gm, 
			int spm, int tem, int tm, int cr, String res)
	{
		this(scode, im, em, sm, gm, spm, tem, tm, cr, res, "F", 0);
	}
	
	public Mark(String scode)
	{
		this(scode, DEFAULT, DEFAULT, 0, 0, 0, DEFAULT, DEFAULT, 0, "F", "F", 0);
	}
	
	public Mark(String scode, int im, int em)
	{
		this(scode, im, em, 0, 0, 0, -1, -1, 0, "F", "F", 0);
	}
	
	public int getTotalEm()
	{
		int sum = 0;
		if(em > 0) sum += em;
		if(sm > 0) sum += sm;
		if(gm > 0) sum += gm;
		if(spm>0) sum += spm;
		return sum;
	}
	
	public int getTotal()
	{
		int sum = 0;
		if(im > 0) sum += im;
		sum += getTotalEm();
		return sum;
	}
	
	public String toString()
	{
		String d = "\t";
		return scode + d + im + d + em + d + sm + d + gm + d + spm + d + tem + d + tm + d + cr + d + res;
	}
	
	public String toGradeString()
	{
		String d = "\t";
		return (toString() + d + grade + d + gradePoints);
	}
	
	public String toMemoString()
	{
		String d = "\t";
		return scode + d + sname + d + cr + d + res + d + grade + d + gradePoints;
	}
	/**
	 * Converts a tab separated data string into a Mark object<br>
	 * The String contains exactly 11 fields as follows:<br>
	 * Field 1: Subject Code <br>
	 * Field 2: Internal Marks  <br>
	 * Field 3: External Marks  <br>
	 * Field 4: Subject Moderation  <br>
	 * Field 5: Grace Marks  <br>
	 * Field 6: Special Moderation  <br>
	 * Field 7: Total External Marks  <br>
	 * Field 8: Total Marks  <br>
	 * Field 9: Credits  <br>
	 * Field 10: Result  <br>
	 * Field 11: Grade  <br>
	 * 
	 * @param s String containing the data
	 * @return Marks object or null
	 */
	public static Mark createMarks(String s)
	{
		String arr[] = s.split("\t");
		if(arr.length < 11)
		{
			return null;
		}
		String scode = arr[0].trim().toUpperCase();
		int im = Utilities.parseInt(arr[1]);
		int em = Utilities.parseInt(arr[2]);
		int sm = Utilities.parseInt(arr[3]);
		int gm = Utilities.parseInt(arr[4]);
		int spm = Utilities.parseInt(arr[5]);
		int tem = Utilities.parseInt(arr[6]);
		int tm = Utilities.parseInt(arr[7]);
		int cr = Utilities.parseInt(arr[8]);
		String res = arr[9];
		String grade = arr[10];
		int gp = 0; // grade points
		if(arr.length == 12)
		{
			gp = Utilities.parseInt(arr[11]);
		}
		return new Mark(scode, im, em, sm, gm, spm, tem, tm, cr, res, grade, gp);
	}
	
}
