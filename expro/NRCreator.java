package expro;

import java.io.FileWriter;

public class NRCreator
{
	public static void main(String[] args) throws Exception
	{
		String infName = "marks-2012.xls";
		int i, j,k;
		String outfName = "nr.txt";
		FileWriter fw = new FileWriter(outfName);
		
		String regd = "";
		for(i=0;i<5;i++)
		{
			String x[][] = utility.ExcelTools.ExcelToArray(infName, i);
			
			regd = "";
			int r = x.length;
			int c = x[0].length;
			for(j=2;j<c; j+=2)
			{
				regd += ("\t" + x[0][j]);
			}
			
			for(k=1;k<r;k++)
			{
				if(x[k][0].trim().length() < 10) 
				{ 
					System.out.println("skipping .. Page(" + (i+1) + ") Row(" + (k+1)+ ")");
					continue;
					
				}
				String tmp = x[k][0] + "\t" +x[k][1] + "\t"+ "A\t0"+ (i+2) +  regd + "\n";
				fw.write(tmp);
			}
		}
		fw.close();
	}

}
