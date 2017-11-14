package expro;

import java.io.FileReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class MyProperties 
{
	private Hashtable< String, String> ht= null; 
	public MyProperties(String fileName)
	{
		ht = new Hashtable<String, String>();
		Properties p = new Properties();
		try
		{
			FileReader fr = new FileReader(fileName);
			p.load(fr);
			fr.close();
		}
		catch (Exception e){ht.put("Error", "Can Not Load Values");}
		Enumeration <Object> enm = p.keys();
		while(enm.hasMoreElements())
		{
			String x = (String)enm.nextElement();
			//System.out.println("Adding: " + x + " -> " + p.getProperty(x));
			ht.put(x.toLowerCase().trim(), p.getProperty(x));
		}
		enm=null;
		p=null;
	}
	
	public String getProperty(String key)
	{
		if(key == null)return ""; // Check for null key value
		
		String x = ht.get(key.toLowerCase().trim());
		if(x == null) x=""; // convert null answer to blank string.
		return x;
	}
}
