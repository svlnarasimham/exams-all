package gradesheet;


public class Grades
{
	/**
	 * Returns Grade for a given percentage as per UGC Recommendation
	 * <br/>
	 * marks >= 90%: <b>O   </b>(Outstanding)<br> 
	 * marks >= 80%: <b>A+  </b>(Excellent)<br>
	 * marks >= 70%: <b>A   </b>(Very Good)<br>
	 * marks >= 60%: <b>B+  </b>(Good) <br>
	 * marks >= 50%: <b>B   </b>(Above Average)<br>
	 * marks >= 45%: <b>C/F </b>(Average for theory and Fail for lab)<br>
	 * marks >= 40%: <b>P/F </b>(Pass for theory and Fail for Lab)<br>
	 * marks <  40%: <b>F   </b>(Fail)<br>
	 * marks <    0: <b>AB  </b>(Absent)<br>

	 * @param percentage Marks obtained<br>
	 * @return Grade String
	 */
	public static String getGrade(int internalMarks, int externalMarks, int maxMarks, String result, boolean isTheory)
	{
		if(externalMarks < 0) 
		{
			return "AB";
		}
		if(result.equals("F"))
		{
			return "F";
		}
		
		if(internalMarks < 0) internalMarks = 0;
		//if(externalMarks < 0) externalMarks = 0;
		
		float percentage = (float)(internalMarks+externalMarks)*100f/maxMarks;
		if(percentage >= 90.0f)
		{
			return "O"; // Outstanding (Grade Points: 10)
		}
		if(percentage >= 80.0f)
		{
			return "A+"; // Excellent (9)
		}
		if(percentage >= 70.0f)
		{
			return "A"; // Very Good (8)
		}
		if(percentage >= 60.0f) 
		{
			return "B+"; // Good  (7)
		}
		if(percentage >= 50.0f)
		{
			return "B"; // Above Average (6)
		}
		if(percentage >= 45.0f && isTheory)
		{
			return "C"; // Average (5)
		}
		if(percentage >= 40.0f && isTheory)
		{
			return "P"; // Pass (4)
		}
		return "F";
	}

	public static int getGradePoints(String grade)
	{
		if(grade.equalsIgnoreCase("O"))
		{
			return 10; //  (Grade Points: 10)
		}
		if(grade.equalsIgnoreCase("A+"))
		{
			return 9; //  (Grade Points: 9)
		}
		if(grade.equalsIgnoreCase("A"))
		{
			return 8; //  (Grade Points: 8)
		}
		if(grade.equalsIgnoreCase("B+"))
		{
			return 7; //  (Grade Points: 7)
		}
		if(grade.equalsIgnoreCase("B"))
		{
			return 6; //  (Grade Points: 6)
		}
		if(grade.equalsIgnoreCase("C"))
		{
			return 5; //  (Grade Points: 5)
		}
		if(grade.equalsIgnoreCase("P"))
		{
			return 4; //  (Grade Points: 4)
		}
		return 0;
	}
}
