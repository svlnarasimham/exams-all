package expro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
public class TxtToXls
{
	public static void main(String[] args) throws Exception
	{
		String basePath = "C:/workspace/expro/exams/A10R12-08-2012";
		File fd = new File(basePath);
		
		String fnames[] = fd.list();	

		for(int i=0;i<fnames.length;i++)
		{
			if(!fnames[i].endsWith(".txt")) continue;
			String inFile = basePath+"/"+fnames[i];
			int idx = fnames[i].lastIndexOf(".txt");
			String outFile = basePath+"/" + fnames[i].substring(0, idx) + ".xls";
			System.out.println((i+1) +". Infile: " + inFile);
			String s[][] = loadFile(inFile);
			System.out.println((i+1) +". Infile: " + outFile);
			utility.ExcelTools.ArrayToExcel(s, outFile, "Sheet1");
			//String s[][] = loadFile(inFile);
		}
	}

	public static String [][] loadFile(String f)
	{
		Vector <String> v = new Vector<String>();
		String s[][];
		int res;
		try
		{
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			int count =0;
			String line;
			while((line = br.readLine()) != null)
			{
				if(line.trim().length() <  5){System.out.println("Skipping.." + count); continue;}
				String ts[] = line.trim().split("\t");
				if(ts.length != 2) {System.out.println("Skipping 2... " + count);continue; }
				v.add(line);
				count++;
			}
			br.close();
			fr.close();
			s = new String [v.size()][2];
			for(int i=0;i<v.size(); i++)
			{
				String x[] = v.get(i).split("\t");
				s[i][0] = x[0];
				s[i][1] = x[1];
			}
		}
		catch(Exception e){e.printStackTrace(); return null;}
		v=null;
		return s;
	}
}
