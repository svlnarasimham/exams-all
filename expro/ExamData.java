package expro;

public class ExamData
{
	public String examFolder;
	public String examCode;
	public String courseCode;
	public String examTitle;
	public String course;
	public String batch;
	public String regulation;
	public String year;
	public String semester;
	public String moe;
	public String yoe;
	public String examMY;
	public String rs; // Regular or Supplementary
	public String ddr; // Date of declaration of result
	public String nrfName; // NR File name
	public String subfName; // Subjects File name
	public String mksfName; // Marks file name
	public String photofName; // photos folder
	public String cefName; // CE Signature file
	public String prfName; // Principal Signature file name
	public String outfName; // Output pdf file name

	public boolean ok;
	
	public ExamData()
	{
		this.reset();
	}
	
	public void reset()
	{
		examTitle = "";
		examMY = "";
		course = "";
		year = "";
		semester = "";
		rs = "";
		batch = "";
		regulation = "";
		moe = "";
		yoe = "";
		examCode = "";
		examFolder ="";
		ok = false;
	}
	
	public void getExamDetails()
	{
		new ExamSetupGUI(this);
	}
	/* ***************/
	public static void main(String[] args)
	{
		ExamData ed = new ExamData();
		ed.getExamDetails();
		
		System.out.println("Exam Title:" + ed.examTitle);
		System.out.println("Exam Year and Sem:" + ed.year + ", " + ed.semester);
		System.out.println("Month of exam:" + ed.moe);
	}
}


