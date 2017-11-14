package common;

import java.util.ArrayList;

public class RegulationDao 
{
	public static final int CCODE = 0; 
	public static final int REGL  = 1; 

	public String q = "'";
	
	public RegulationDao()
	{
		if(!G.isInitialized())
		{
			G.initialize();
		}
	}
	
	public ArrayList<Regulation> getAll()
	{
		String sql = "select ccode, rcode from regulations order by ccode;";
		ArrayList<Regulation> al = new ArrayList<>();
		ArrayList<String[]> alr = G.dbcon.executeSQL(sql);
		for(String[] arr : alr)
		{
			Regulation tbr = new Regulation(arr[CCODE], arr[REGL]);
			al.add(tbr);
		}
		return al;
	}
	
	public ArrayList<Regulation> getSome(String ccode)
	{
		String sql = "select ccode, rcode from " + 
				G.dbName + "." + G.rglTable + " " + 
				"where ccode=" + q+ccode+q;
		ArrayList<String[]> al = G.dbcon.executeSQL(sql);
		ArrayList<Regulation> rgal = new ArrayList<>();
		if(al.size() > 0)
		{
			for(String arr[]: al)
			{
				rgal.add(new Regulation(arr[CCODE], arr[REGL]));
			}
		}
		return rgal;
	}
	
	public int insert(Regulation rgl)
	{
		String sql = "insert into regulations (ccode, rcode) values (" +
				q+rgl.ccode+q    + ", " +
				q+rgl.rcode+q +
				");"; 
		int res = G.dbcon.executeUpdate(sql);
		return res;
	}
	
	public int delete(Regulation rgl)
	{
		String sql = "delete from regulations where " +
				"ccode = " + q+rgl.ccode+q + " and " +
				"regulation = " + q+rgl.rcode+q + ";";
		int res = G.dbcon.executeUpdate(sql);
		return res;
	}
}
