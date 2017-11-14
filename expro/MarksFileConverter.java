package expro;
import java.io.FileWriter;

public class MarksFileConverter
{
	public static void main(String[] args) throws Exception
	{
		String infName = "marks-2012.xls";
		int i, j,k;
		for(i=0;i<5;i++)
		{
			String x[][] = utility.ExcelTools.ExcelToArray(infName, i);
			int r = x.length;
			int c = x[0].length;
			for(j=2;j<c;j+=2) // for each column from 2nd onwards, internal , external 
			{
				String code = x[0][j].trim().toLowerCase();
				if(code.length() == 0)
				{
					System.out.println("Blank Code at: Page: " + i + "  Column: " + j);
					continue;
				}
				System.out.println(i+"." + j+ ". Processing File: "+ x[0][j] + "-im.txt");
				// Internal
				FileWriter fwi = new FileWriter(x[0][j]+"-im.txt", true);
				FileWriter fwe = new FileWriter(x[0][j]+"-em.txt", true);
				for(k=1;k<r;k++)
				{
					if(x[k][0].trim().length() < 10) 
					{ 
						System.out.println("skipping .. Page(" + (i+1) + ") Row(" + (k+1)+ ")");
						continue;
					}
					fwi.write(x[k][0] + "\t" + x[k][j]+ "\n"); //Htno \t Internal marks
					fwe.write(x[k][0] + "\t" + x[k][j+1] + "\n"); // External Marks
				}
				fwi.close();
				fwe.close();
			}
			
		}
		
		
		FileWriter f = new FileWriter("abc", true); 

	}

}
