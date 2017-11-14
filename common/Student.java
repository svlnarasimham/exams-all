package common;
public class Student implements Comparable<Student>
{
	String rollno="";
	String name="";
	String fname="";
	String mname="";
	String gender="";
	String ccode="";
	String bcode="";
	int curyear=0;
	int cursem=0;
	public Student(String rollno, String ccode, String bcode)
	{
		this.rollno = rollno;
		this.ccode = ccode;
		this.bcode = bcode;
	}
	public Student( 
			String rollno,
			String name, 
			String fname, 
			String mname, 
			String gender, 
			String ccode, 
			String bcode, 
			int curyear, 
			int cursem)
	{
		this.rollno  = rollno ;
		this.name    = name   ;
		this.fname   = fname  ;
		this.mname   = mname  ;
		this.gender  = gender ;
		this.ccode   = ccode  ;
		this.bcode   = bcode  ;
		this.curyear = curyear;
		this.cursem  = cursem ;
	}

	public int compareTo(Student obj)
	{
		return rollno.trim().toUpperCase().compareTo(obj.rollno.trim().toUpperCase());
	}
	public boolean equals(Student obj)
	{
		return rollno.equalsIgnoreCase(obj.rollno);
	}
}
