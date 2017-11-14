package common;

public class Course
{
	public String ccode;
	public String cabbr;
	public String cname;
	
	public Course()
	{
		ccode="";
		cabbr="";
		cname = "";
	}
	
	public Course(String courseCode, String shortName, String courseName)
	{
		ccode=courseCode;
		cabbr=shortName;
		cname = courseName;
	}
	
	public String toJSON()
	{
		String q = "\"";
		return "{"+ 
				q+"ccode" + q + ":" + q+ ccode+q + "," +
				q+"cabbr" + q + ":" + q+ cabbr+q + "," +
				q+"cname" + q + ":" + q+ cname + q + 
				"}";
	}
	
	public String toString()
	{
		return ccode + "\t" + cabbr+ "\t" + cname;
	}
	
	public static String toJSON(Course c[])
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i=0; i<c.length; i++)
		{
			if(i>0)
			{
				sb.append(",");
			}
			sb.append(c[i].toJSON());
		}
		sb.append("]");
		return sb.toString();
	}

	public String getCode()
	{
		return ccode;
	}

	public void setCode(String code)
	{
		this.ccode = code;
	}

	public String getAbbr()
	{
		return cabbr;
	}

	public void setAbbr(String abbr)
	{
		this.cabbr = abbr;
	}

	public String getName()
	{
		return cname;
	}

	public void setName(String name)
	{
		this.cname = name;
	}
	
	/* */
	public static void main(String[] args)
	{
		Course ca[] = new Course[3];
		for(int i=0;i<3;i++)
		{
			ca[i] = new Course("A", "B.Tech.", "Bachelor of Technology");
		}
		System.out.println(Course.toJSON(ca));
		System.out.println(ca[0].toJSON());
	}
	/* */
}
