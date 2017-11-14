package omr;
public class OmrRecord
{
	String fileName;
	String barcode;
	int refX;
	int refY;
	float angle;
	int fieldCount;
	String fieldValues[];
	
	/**
	 * Extracts data from omr data (colon separated string)
	 * @param omrSheetData Colon separated Data string obtained from OmrSheet.
	 */
	public OmrRecord(String omrSheetData)
	{
		String arr[] = omrSheetData.split(":");
		int len = arr.length;
		if(len<6)
		{
			System.out.println("Line Values Count Not Correct...");
			fileName = "";
			barcode = "";
			refX=0;
			refY=0;
			angle = 0.0f;
			fieldCount=0;
		}
		else
		{
			fileName = arr[0].trim();
			barcode = arr[1].trim();
			refX = utility.Utilities.parseInt(arr[2].trim());
			refY = utility.Utilities.parseInt(arr[3].trim());
			angle = utility.Utilities.parseFloat(arr[4].trim());
			fieldCount = utility.Utilities.parseInt(arr[5].trim());
			if(fieldCount>0)
			{
				fieldValues = new String[fieldCount];
				for(int i=0;i<fieldCount; i++)
				{
					fieldValues[i] = arr[6+i].trim();
				}
			}
		}
	}
	
	public String toString()
	{
		String delim = ":";
		StringBuffer sb = new StringBuffer(fileName+delim+barcode+delim+refX+delim+refY+delim+angle+delim+fieldCount);
		for(int i=0;i<fieldCount;i++)
		{
			sb.append(delim+fieldValues[i]); 
		}
		return sb.toString();
	}
	
	public static void main(String args[])
	{
		String x = "lomr-0002.jpg:789012:72:278:0.2497:80:A:.:4:8:.:.:1:9:.:.:4:0:.:1:9:.:.:1:8:0:.:.:1:8:.:.:9:1:.:.:2:6:.:.:8:4:.:.:6:?:A:.:.:.:.:.:?:9:.:.:6:4:.:.:6:3:.:.:1:8:A:.:5:3:.:.:1:8:.:.:7:9:.:.:8:9:.:.:9:0";
		OmrRecord or = new OmrRecord(x);
		System.out.println(or);

	}
}

