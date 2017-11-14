package common;
import java.util.*;
public class Students
{
	Vector <Student> nv;
	Hashtable <String, Integer> idx;
	public Students()
	{
		nv = new Vector<Student>();
		idx = new Hashtable<String, Integer>();
	}
	
	public void addStudent(Student student)
	{
		int indx = nv.size();
		nv.add(student);
		idx.put(student.rollno.trim().toUpperCase(), indx);
	}
	
	public Student getNR(String htno)
	{
		Integer res = idx.get(htno.trim().toUpperCase());
		if(res != null	)
		{
			return nv.get(res);
		}
		else
		{
			return null;
		}
	}
}
