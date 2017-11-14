package common;

import java.util.ArrayList;

public class CourseDao
{
	public static final int CCODE = 0; 
	public static final int CABBR = 1; 
	public static final int CNAME = 2; 
	String q = "'";
	
	public CourseDao()
	{
		if(!G.isInitialized())
		{
			G.initialize();
		}
	}
	
	public ArrayList<Course> getAll()
	{
		String sql = "select ccode, cabbr, cname from courses order by ccode ";
		ArrayList<Course> al = new ArrayList<>();
		ArrayList<String[]> alr = G.dbcon.executeSQL(sql);
		for(String[] arr : alr)
		{
			Course tbr = new Course(arr[CCODE], arr[CABBR], arr[CNAME]);
			al.add(tbr);
		}
		
		return al;
	}
	
	public Course getOne(String ccode)
	{
		String sql = "select ccode, cabbr, cname " + 
				"from courses " + 
				"where ccode=" + q+ccode+q;
		ArrayList<String[]> al = G.dbcon.executeSQL(sql);
		Course b = null;
		if(al.size() > 0)
		{
			String arr[] = al.get(0);
			b = new Course(arr[CCODE], arr[CABBR], arr[CNAME]);
		}
		return b;
	}
	
	public int add(Course c)
	{
		String sql = "insert into courses (ccode, cabbr, cname) values (" +
				q+c.ccode+q    + ", " +
				q+c.cabbr+q    + ", " +
				q+c.cname+q    +
				");"; 
		int res = G.dbcon.executeUpdate(sql);
		return res;
	}
	
	public int delete(Course c)
	{
		String sql = "delete from courses where " +
				"ccode = " + q+c.ccode+q;
		int res = G.dbcon.executeUpdate(sql);
		return res;
	}
	
	public int update(Course c)
	{
		String sql = "update courses set " +
			"cabbr = " + q+c.cabbr+q    + ", " +
			"cname = " + q+c.cname+q + 
			" where ccode = " + q+c.ccode+q; 
		System.out.println("Update query: " + sql);
		int res = G.dbcon.executeUpdate(sql);
		return res;
	}
	
	/**
	 * Load Coursees form pre-defined list
	 * @param ccode Course code ("*" represents all courses)
	 * @return number of Coursees loaded
	 */
	public ArrayList<Course> getDefaultCourses()
	{
		ArrayList<Course> al = new ArrayList<>();
		al.add(new Course("A","B.Tech.","Bachelor of Technology"));
		al.add(new Course("D","M.Tech.","Master of Technology"));
		al.add(new Course("E","MBA","Master of Business Administration"));
		al.add(new Course("F","MCA","Master of Computer Applications"));
		return al;
	}
}
