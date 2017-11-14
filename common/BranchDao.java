package common;

import java.util.ArrayList;

public class BranchDao
{
	public static final int CCODE = 0; 
	public static final int BCODE = 1; 
	public static final int BABBR = 2; 
	public static final int BNAME = 3; 
	public static final int DCODE = 4; 
	String q = "'";
	//String tableName = G.dbName+"."+G.brnTable;
	
	public BranchDao()
	{
		if(!G.isInitialized())
		{
			G.initialize();
		}
	}
	
	public ArrayList<Branch> getAll()
	{
		//String fieldsList[] = {"ccode", "bcode", "babbr", "bname", "dcode"};
		String sql = "select ccode, bcode, babbr, bname, dcode " + 
				"from " + G.dbName+"." + G.brnTable + " order by ccode,bcode;";
		ArrayList<Branch> al = new ArrayList<>();
		ArrayList<String[]> alr = G.dbcon.executeSQL(sql);
		for(String[] arr : alr)
		{
			Branch tbr = new Branch(arr[0], arr[1], arr[2], arr[3], arr[4]);
			al.add(tbr);
		}
		
		return al;
	}
	
	public Branch getOne(String ccode, String bcode)
	{
		String sql = "select ccode, bcode, babbr, bname, dcode from branches " + 
				"where ccode=" + q+ccode+q + " and bcode = " + q+bcode+q + ";";
		ArrayList<String[]> al = G.dbcon.executeSQL(sql);
		Branch b = null;
		if(al.size() > 0)
		{
			String arr[] = al.get(0);
			b = new Branch(arr[CCODE], arr[BCODE], arr[BABBR], arr[BNAME], arr[DCODE]);
		}
		return b;
	}
	
	public ArrayList<Branch> getSome(String ccode)
	{
		String sql = "select ccode, bcode, babbr, bname, dcode " +
				"from branches " +
				"where ccode = " + q+ccode+q + " " + 
				"order by ccode, bcode;";
		ArrayList<Branch> al = new ArrayList<>();
		ArrayList<String[]> alr = G.dbcon.executeSQL(sql);
		for(String[] arr : alr)
		{
			Branch tbr = new Branch(arr[CCODE], arr[BCODE], arr[BABBR], arr[BNAME], arr[DCODE]);
			al.add(tbr);
		}
		return al;
	}
	
	public int add(Branch b)
	{
		String sql = "insert into branches (ccode, bcode, babbr, bname, dcode) values (" +
				q+b.ccode+q + ", " +
				q+b.bcode+q + ", " +
				q+b.babbr+q + ", " +
				q+b.bname+q + ", " +
				q+b.dcode+q +
				");"; 
		int res = G.dbcon.executeUpdate(sql);
		return res;
	}
	
	public int delete(Branch b)
	{
		String sql = "delete from branches where " +
				"ccode = " + q+b.ccode+q + " and " +
				"bcode = " + q+b.bcode+q + ";";
		int res = G.dbcon.executeUpdate(sql);
		return res;
	}
	
	public int update(Branch b)
	{
		String sql = "ubdate branches set " +
			"babbr = " + q+b.babbr+q    + ", " +
			"bname = " + q+b.bname+q     + ", " +
			"dcode = " + q+b.dcode+q  + " " +
			"where ccode = " + q+b.ccode+q + " and bcode = " + q+b.bcode+q + ";"; 
		int res = G.dbcon.executeUpdate(sql);
		return res;
	}
	
	/**
	 * Load branches form pre-defined list
	 * @param ccode Course code ("*" represents all courses)
	 * @return number of branches loaded
	 */
	public ArrayList<Branch> getDefaultBranches(String ccode)
	{
		ArrayList<Branch> al = new ArrayList<>();
		ccode = ccode.toUpperCase();
		if(ccode.equals("A") || ccode.equals("*"))
		{
			al.add(new Branch("A","01","CE", "Civil Engineering"));
			al.add(new Branch("A","02","EEE","Electrical and Electronics Engineering"));
			al.add(new Branch("A","03","MEC","Mechanical Engineering"));
			al.add(new Branch("A","04","ECE","Electronics and Communication Engineering"));
			al.add(new Branch("A","05","CSE","Computer Science & Engineering"));
			al.add(new Branch("A","12","IT","Information Technology"));
		}
		if (ccode.equals("D") || ccode.equals("*"))
		{
			al.add(new Branch("D","04","CAD","CAD/CAM","01"));
			al.add(new Branch("D","05","CS","Computer Science & Engineering", "05"));
			al.add(new Branch("D","06","DSCE","Digital Systems and Computer Electronics", "04"));
			al.add(new Branch("D","07","EPS","Electrical Power Systems","02"));
			al.add(new Branch("D","15","MD","Machine Design", "03"));
			al.add(new Branch("D","25","OC","Software Engineering"));
			al.add(new Branch("D","38","DECS","DECS","04"));
			al.add(new Branch("D","43","PE","Power Electronics", "02"));
			al.add(new Branch("D","49","EPE","Electrical Power Engineering","02"));
			al.add(new Branch("D","55","EMB","Embedded Systems"));
			al.add(new Branch("D","57","VLSI","VLSI","04"));
			al.add(new Branch("D","58","CSE","Computer Science & Engineering","05"));
		}
		if (ccode.equals("E") || ccode.equals("*"))
		{
			al.add(new Branch("E","00","MBA","Master of Business Administration"));
		}
		if (ccode.equals("F") || ccode.equals("*"))
		{
			al.add(new Branch("F","00","MCA","Master of Computer Applications"));
		}
		return al;
	}
}
