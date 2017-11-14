package expro;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;


public class SubjectFileCreator
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String codes [][] = {
				{"EEE", "2", "1GC11", "1GC12", "1GC13", "1GC14", "1G311", "1G112", "1G513", "1G312", "1G114", "1GC16", "1GC17", "1G411"},
				{"ME", "3", "1GC11", "1GC12", "1GC13", "1GC14", "1G112", "1G511", "1G512", "1GC16", "1GC17", "1G114", "1G411"},
				{"ECE", "4", "1GC11", "1GC12", "1GC13", "1GC14", "1G311", "1G112", "1G513", "1G312", "1GC16", "1GC17", "1G114", "1G411"},
				{"CSE", "5", "1GC11", "1GC12", "1GC13", "1GC14", "1GC15", "1G111", "1G513", "1GC16", "1GC17", "1G113", "1G411"},
				{"IT", "12", "1GC11", "1GC12", "1GC13", "1GC14", "1GC15", "1G111", "1G513", "1GC16", "1GC17", "1G113", "1G411"}
				};
		Hashtable<String, String> hsc = new Hashtable<String, String>();
		java.util.HashSet<String> hsl = new HashSet<String>();
		hsl.add("1G114");
		hsl.add("1G114");
		hsl.add("1G312");
		hsl.add("1GC16");
		hsl.add("1GC17");
		hsl.add("1G113");
		hsl.add("1G411");

		hsc.put("1GC11", "ENGINEERING MATHEMATICS - I");
		hsc.put("1GC12", "PHYSICS - I");
		hsc.put("1GC13", "CHEMISTRY - I");
		hsc.put("1G111", "STRENGTH OF MATERIALS");
		hsc.put("1G112", "ENVIRONMENTAL SCIENCE");
		hsc.put("1G311", "ENGINEERING MECHANICS - I");
		hsc.put("1G511", "C PROGRAMMING AND DATA STRUCTURES");
		hsc.put("1G512", "PRINCIPLES OF PROGRAMMING LANGUAGES");
		hsc.put("1G513", "BASIC ELECTRONICS");
		hsc.put("1GC14", "MECHANICS OF SOLIDS - I");
		hsc.put("1GC15", "ENGINEERING DRAWING");
		hsc.put("1G114", "CHEMISTRY LAB");
		hsc.put("1G312", "ENGINEERING MECHANICS LAB");
		hsc.put("1GC16", "CP & DS LAB");
		hsc.put("1GC17", "STRENGTH OF MATERIALS LAB");
		hsc.put("1G113", "ENGINEERING GRAPHICS LAB");
		hsc.put("1G411", "BASIC ELECTRONICS LAB");
		
		if(hsl.contains("1GC17"))
		{
			System.out.println("It is a lab");
		}
		else
		{
			System.out.println("It is not a lab");
		}

		int mxe, mxt, mne, mnt, cr;
		
		int yr = 1, sem = 0;
		String d = "\t";
		for(int i=0;i<codes.length; i++)
		{
			System.out.println("Subjects for:  " + codes[i][0]);
			for(int j=0;j<codes[i].length-2;j++)
			{
				boolean isLab = (hsl.contains(codes[i][j+2]))? true: false;
				if(isLab)
				{
					mxe = 60;
					mxt = 100;
					mne = 21;
					mnt = 40;
					cr = 2;
				}
				else
				{
					mxe = 70;
					mxt = 100;
					mne = 25;
					mnt = 40;
					cr = 4;
				}
				
				System.out.println(
						codes[i][j+2]+ d +
						hsc.get(codes[i][j+2])+ d +
						codes[i][1]+ d +
						yr + d + sem + d + mxe + d + mxt + d + mne + d + mnt + d + cr + d + ((isLab)?"1" : "0"));	
			}
		}
	}

}
