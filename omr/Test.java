package omr;
import javax.swing.JOptionPane;

import java.io.*;

public class Test
{
	public static void main(String[] args) throws IOException
	{
		String arr[] = utility.Utilities.getFiles("/home/qis/EXAMS/btech", ".....");
		for(int i=0;i<arr.length; i++) System.out.println(".." + arr[i]);
	}
	
}
