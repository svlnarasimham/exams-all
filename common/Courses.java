package common;
import java.util.*;
import utility.*;

public class Courses
{
	public ArrayList<Course> crs;
	public Courses ()
	{
		crs = new ArrayList <>();
		G.initialize();
	}
	
	/**
	 * Loads courses from Database (courses table)
	 * @return
	 */
	public int loadCourses()
	{
		CourseDao cs = new CourseDao();
		crs = cs.getAll();
		if(crs.size() ==0)
		{
			Utilities.showMessage(null, "Can not connect to Database. Loading defaults.");
			crs = cs.getDefaultCourses();
		}
		return crs.size();
	}
	
	// Return Course short/long Name for a code. 
	// If shortName is true, short name is returned 
	public Course getCourse(String code)
	{
		Iterator<Course> itr = crs.iterator();
		while (itr.hasNext())
		{
			Course c = (Course) itr.next();
			if (c.ccode.equalsIgnoreCase(code))
			{
				return c;
			}
		}
		return null;
	}
	
	public int getCourseCount()
	{
		return crs.size();
	}

	public Course[] getCourses()
	{
		int n = crs.size();
		if (n==0) return null;
		Course x[] = new Course[n];
		return crs.toArray(x);
	}
	/* */
	public static void main(String args[])
	{
		G.initialize();
		int nrec = G.courses.loadCourses();
		//c.loadCourses("courses.dat");
		System.out.println("Courses: " + nrec);
		Course c[] = G.courses.getCourses();
		for(Course cr:c)
			System.out.println("Course: " + cr.ccode + ": " + cr.cname + "(" + cr.cabbr + ")");
		G.closeAll();
	}
	/* */
}
