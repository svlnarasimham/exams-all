package gradesheet;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import common.G;
import expro.ExamData;
import utility.Utilities;


public class FilesSelectorGUI extends JDialog implements ActionListener
{
   private static final long serialVersionUID = 1L;
   private ExamData ed;
   boolean prsigRequired = false; // true if principal signature is required
   
   private JTextField jtfMy, jtfSubf, jtfNrf, jtfMksf, jtfOutf, jtfDdr, jtfCe, jtfPhoto, jtfEcode;
   private JComboBox<String> jcCourseCode, jcYr, jcSem, jcRs;
   private JButton jbOk, jbCancel, jbSub, jbNr, jbOut, jbMks, jbCe, jbPhoto;
   
   private JTextField jtfPr = null;
   private JButton jbPr = null;
   
   String courseCodes[] = {"A: B.Tech.", "D: M.Tech.", "E: M.B.A.", "F: M.C.A."};
   String years[] = {" ", "I", "II", "III", "IV"}; // For PG, Year = 0
   String sems[] = {" ", "I", "II", "III", "IV", "V", "VI"}; // for MCA 6 semesters
   String regsup[]={"Regular", "Supplementary"};
	String basePath="/qisexams/process";
   
	boolean debug = false;
   public FilesSelectorGUI(ExamData ed)
   {
   	super((JFrame)null, true);
   	this.ed = ed;
   	int nrows = (prsigRequired) ? 14: 13;
		setLayout(new GridLayout(0,2));
		setTitle("Examination Details Selector");
		setSize(450,300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JLabel jl1 = new JLabel ("Course", JLabel.RIGHT);
		JLabel jl2 = new JLabel ("Year", JLabel.RIGHT);
		JLabel jl3 = new JLabel ("Semester", JLabel.RIGHT);
		JLabel jl4 = new JLabel ("Reg/Sup", JLabel.RIGHT);
		JLabel jl5 = new JLabel ("Month and Year of First Exam:", JLabel.RIGHT);
		JLabel jl6 = new JLabel ("Subjects File", JLabel.RIGHT);
		JLabel jl7 = new JLabel ("NR File", JLabel.RIGHT);
		JLabel jl8 = new JLabel ("Marks File", JLabel.RIGHT);
		JLabel jl9 = new JLabel ("Output File", JLabel.RIGHT);
		JLabel jl10= new JLabel ("Date of Result", JLabel.RIGHT);
		JLabel jl11= new JLabel ("Photos Folder", JLabel.RIGHT);
		JLabel jl12= new JLabel ("Controller Signature", JLabel.RIGHT);
		JLabel jl13= new JLabel ("Principal Signature", JLabel.RIGHT);
		JLabel jl14= new JLabel ("Exam Code", JLabel.RIGHT);
		
		// Comboboxes
		jcCourseCode = new JComboBox<String>(courseCodes);
		jcYr = new JComboBox<String>(years);
		jcSem = new JComboBox<String>(sems);
		jcRs = new JComboBox<String>(regsup);

		// Text fields
		jtfMy = new JTextField();
		jtfMy.setPreferredSize(new Dimension(250,25));
		jtfSubf = new JTextField();
		jtfSubf.setPreferredSize(new Dimension(250,25));
		jtfNrf = new JTextField();
		jtfNrf.setPreferredSize(new Dimension(250,25));
		jtfMksf = new JTextField();
		jtfMksf.setPreferredSize(new Dimension(250,25));
		jtfOutf = new JTextField("gradesheet-xx.pdf");
		jtfOutf.setPreferredSize(new Dimension(250,25));
		jtfDdr = new JTextField();
		jtfDdr.setPreferredSize(new Dimension(250,25));
		jtfCe = new JTextField();
		jtfCe.setPreferredSize(new Dimension(250,25));
		jtfCe.setText("/home/qis/EXAMS/defaults/qis-ce-signature.jpg"); // set default value
		jtfPhoto = new JTextField();
		jtfPhoto.setPreferredSize(new Dimension(250,25));
		jtfPhoto.setText("/home/qis/EXAMS/photos"); // set default value
		if(prsigRequired)
		{
			jtfPr = new JTextField();
			jtfPr.setPreferredSize(new Dimension(250,25));
			jtfCe.setText("/home/qis/EXAMS/defaults/qis-pr-signature.jpg"); // set default value
		}
		jtfEcode = new JTextField();
		jtfEcode.setPreferredSize(new Dimension(250,25));

		// Buttons
		jbOk = new JButton("OK");
		jbOk.setActionCommand("ok");
		jbOk.addActionListener(this);
		
		jbCancel = new JButton("Cancel");
		jbCancel.setActionCommand("cancel");
		jbCancel.addActionListener(this);
	
		jbSub = new JButton("...");
		jbSub.setPreferredSize(new Dimension(50, 25));
		jbSub.setActionCommand("Subject");
		jbSub.addActionListener(this);
		
		jbNr = new JButton("...");
		jbNr.setPreferredSize(new Dimension(50, 25));
		jbNr.setActionCommand("Nr");
		jbNr.addActionListener(this);
		
		jbMks = new JButton("...");
		jbMks.setPreferredSize(new Dimension(50, 25));
		jbMks.setActionCommand("Marks");
		jbMks.addActionListener(this);
		
		jbOut = new JButton("...");
		jbOut.setPreferredSize(new Dimension(50, 25));
		jbOut.setActionCommand("Out");
		jbOut.addActionListener(this);
		
		jbCe = new JButton("...");
		jbCe.setPreferredSize(new Dimension(50, 25));
		jbCe.setActionCommand("Controller");
		jbCe.addActionListener(this);
		
		jbPhoto = new JButton("...");
		jbPhoto.setPreferredSize(new Dimension(50, 25));
		jbPhoto.setActionCommand("Photo");
		jbPhoto.addActionListener(this);
		
		if(prsigRequired)
		{
			jbPr = new JButton("...");
			jbPr.setPreferredSize(new Dimension(50, 25));
			jbPr.setActionCommand("Principal");
			jbPr.addActionListener(this);
		}
		
		
		JPanel jp1 = new JPanel(new FlowLayout());
		JPanel jp2 = new JPanel(new FlowLayout());
		JPanel jp3 = new JPanel(new FlowLayout());
		JPanel jp4 = new JPanel(new FlowLayout());
		JPanel jp5 = new JPanel(new FlowLayout());
		JPanel jp6 = new JPanel(new FlowLayout());
		JPanel jp7 = new JPanel(new FlowLayout());
		
		// 1 Course
		this.add(jl1); 
		this.add(jcCourseCode);
		
		// 2 Year
		this.add(jl2);
		this.add(jcYr);
		
		// 3 Sem
		this.add(jl3);
		this.add(jcSem);
		
		// 4 Reg/sub		
		this.add(jl4);
		this.add(jcRs);
		
		// 5 month and year of exam
		this.add(jl5);
		this.add(jtfMy);
		
		// 6 Subjects file
		this.add(jl6);
		jp1.add(jtfSubf);
		jp1.add(jbSub);
		this.add(jp1);
		
		// 7 NR file
		this.add(jl7);
		jp2.add(jtfNrf);
		jp2.add(jbNr);
		this.add(jp2);
		
		// 8 Marks file
		this.add(jl8);
		jp3.add(jtfMksf);
		jp3.add(jbMks);
		this.add(jp3);
		
		// 9 Output file
		// gradesheet-xx.pdf
		this.add(jl9);
		jp4.add(jtfOutf);
		jp4.add(jbOut);
		this.add(jp4);
		
		// 10 Date of Declaration of Result
		this.add(jl10);
		this.add(jtfDdr);
		
		// 11. Photos Folder
		this.add(jl11);
		jp5.add(jtfPhoto);
		jp5.add(jbPhoto);
		this.add(jp5);
		
		// 12. Controller Signature
		this.add(jl12);
		jp6.add(jtfCe);
		jp6.add(jbCe);
		this.add(jp6);
		
		// 13. Principal signature if required
		if(prsigRequired)
		{
			this.add(jl13);
			jp7.add(jtfPr);
			jp7.add(jbPr);
			this.add(jp7);
		}
		this.add(jl14);
		this.add(jtfEcode);
		
		// 13 or 14. OK and Cancel buttons
		this.add(jbOk);
		this.add(jbCancel);
		
		pack();
		setLocationRelativeTo(G.jfParent);
		
		/* ************** Debug Code ******************** */
		if(debug)
		{
			jcCourseCode.setSelectedIndex(0);
			jcYr.setSelectedIndex(1);
			jcSem.setSelectedIndex(1);
			jcRs.setSelectedIndex(0);

			// Text fields
			jtfMy.setText("June 2016");;
			jtfSubf.setText("c:/workspace/qis-gradesheet/A151511R151100/A151511R151100-sub.txt");
			jtfNrf.setText("c:/workspace/qis-gradesheet/A151511R151100/A151511R151100-nr.txt");
			jtfMksf.setText("c:/workspace/qis-gradesheet/A151511R151100/A151511R151100-mks.txt");
			jtfOutf.setText("gradesheet-11R16.pdf");
			jtfDdr.setText("20-06-2016");
			//jtfCe.setText("c:/qis-exams/defaults/ce-signature.jpg");
			jtfPhoto.setText("c:/qis-exams/photos");
			if(prsigRequired)
			{
				//jtfPr.setText("c:/qis-exams/defaults/pr-signature.jpg");
			}
		}

		setVisible(true);
   }
   
	public void actionPerformed(ActionEvent ae)
	{
		String action = ae.getActionCommand();
		String cc, crs, yr, sem, rs, my, dt, ec;
		
		if(action.equalsIgnoreCase("ok")) /////////////////////////////////////////////////////////////
		{
			String tmp = (String)jcCourseCode.getSelectedItem();
			cc = (tmp.split(":")[0]).trim();
			crs = (tmp.split(":")[1]).trim();
			yr  = ((String) jcYr.getSelectedItem()).trim();
			sem = ((String) jcSem.getSelectedItem()).trim();
			rs  = ((String) jcRs.getSelectedItem()).trim();
			my  = jtfMy.getText().trim();
			dt  = jtfDdr.getText().trim();
			ec = jtfEcode.getText().trim().toUpperCase();
			
			// Check if Year and Semester are blank
			if(yr.length() == 0 && sem.length() == 0)
			{
				Utilities.showMessage(this, "Both Year and Semester can not be blank");
				ed.reset();
				return;
			}
			
			if(yr.length() > 0)
			{
				if(!sem.equals("I") && !sem.equals("") && !sem.equals("II"))
				{
					Utilities.showMessage(this, "Semester can not be more than II");
					ed.reset();
					return;
				}
			}
			if(ec.length() != 14)
			{
				Utilities.showMessage(this, "Exam Code should be 14 characters");
				ed.reset();
				return;
			}
			// /////  Set Values 
			// jtfMy, jtfSubf, jtfNrf, jtfMksf, jtfOutf, jtfDdr;
			// JComboBox<String> jcCourseCode, jcYr, jcSem, jcRs
			ed.courseCode = cc;
			ed.course = crs;
			ed.year = yr;
			ed.semester = sem;
			ed.rs = rs;
			ed.examMY = my;
			ed.ddr = dt;
			ed.examCode = ec;
			
			String et = crs + " ";
			if(yr.length()> 0)
			{
				et += (yr + " Year ");
			}
			
			if(sem.length() > 0)
			{
				et += (sem + " Semester ");
			}
			
			et += ("("+ rs + ")");
			ed.examTitle = et ;

			ed.subfName = jtfSubf.getText();
			if(ed.subfName.length() == 0)
			{
				Utilities.showMessage(this, "Subject file not selected...");
				ed.reset();
				return;
			}
			ed.nrfName = jtfNrf.getText();
			if(ed.nrfName.length() == 0)
			{
				Utilities.showMessage(this, "NR file not selected...");
				ed.reset();
				return;
			}
			ed.mksfName = jtfMksf.getText();
			if(ed.mksfName.length() == 0)
			{
				Utilities.showMessage(this, "Marks file not selected...");
				ed.reset();
				return;
			}
			ed.outfName = jtfOutf.getText();
			if(ed.outfName.length() == 0)
			{
				Utilities.showMessage(this, "Output file not selected...");
				ed.reset();
				return;
			}
			if(prsigRequired)
			{
				ed.prfName = jtfPr.getText();
			}
			ed.photofName = jtfPhoto.getText();
			if(ed.photofName.length() == 0)
			{
				Utilities.showMessage(this, "Photos Folder not selected...");
				ed.reset();
				return;
			}
			ed.cefName = jtfCe.getText();
			if(ed.cefName.length() == 0)
			{
				Utilities.showMessage(this, "Controller signature file not selected...");
				ed.reset();
				return;
			}
			if(prsigRequired && ed.prfName.length() == 0)
			{
				Utilities.showMessage(this, "Principal Signature file not selected...");
				ed.reset();
				return;
			}
			if(dt.length() == 0)
			{
				Utilities.showMessage(this, "Date of Result is empty");
				ed.reset();
				return;
			}
			
			ed.ok = true;
			dispose();
 		}
		else if (action.equalsIgnoreCase("cancel"))
		{
			ed.reset();
			dispose();
			//System.out.println(examCode);
		}
		else if(action.equalsIgnoreCase("subject")) ////////////////////////////////////////
		{
			setTextField(jtfSubf, Utilities.OPEN, Utilities.FILES, "..*-sub.txt", "Select Subject File");
			String tmps = jtfSubf.getText().trim();
			if(tmps.length() > 14)
			{
				int idx = tmps.lastIndexOf('/');
				jtfEcode.setText(tmps.substring(idx+1, idx+1+14));
			}
		}
		else if(action.equalsIgnoreCase("nr"))
		{
			setTextField(jtfNrf, Utilities.OPEN, Utilities.FILES, "..*-nr.txt", "Select NR File");
		}
		else if(action.equalsIgnoreCase("marks"))
		{
			setTextField(jtfMksf, Utilities.OPEN, Utilities.FILES, "..*-mks.txt", "Select Marks File");
		}
		else if(action.equalsIgnoreCase("out"))
		{
			setTextField(jtfOutf, Utilities.SAVE, Utilities.FILES, "..*pdf", "Select Output PDF File");
		}
		else if(action.equalsIgnoreCase("Photo"))
		{
			setTextField(jtfPhoto, Utilities.OPEN, Utilities.DIRS, "..*", "Select Photos Folder");
		}
		else if(action.equalsIgnoreCase("Controller"))
		{
			setTextField(jtfCe, Utilities.OPEN, Utilities.FILES, "..*jpg", "Select CE Signature JPG File");
		}
		else if(action.equalsIgnoreCase("Principal"))
		{
			setTextField(jtfPr, Utilities.OPEN, Utilities.FILES, "..*jpg", "Select Principal signature jpg File");
		}
	}

	/**
	 * 
	 * @param jtf Text Field to be filled
	 * @param openSaveMode use Utilites.OPEN or Utilities.SAVE mode
	 * @param fileOrDir Use Utilities.DIRS for directory or Utilities.FILES for file selection
	 * @param regExp  File name filter (Ex: ..*.txt for all .txt files)
	 * @param msg Message to be printed on the dialog title
	 * 
	 */
	public void setTextField(JTextField jtf, int openSaveMode, int fileOrDir, String regExp, String msg)
	{
		String path;
		path = Utilities.chooseFile(basePath, openSaveMode, fileOrDir, regExp, msg);
		if(path == null || path.equals(""))
		{
			jtf.setText("");
		}
		else
		{
			path = path.replace('\\', '/');
			jtf.setText(path);
			if(path.lastIndexOf('/') >= 0)
			{
				basePath = path.substring(0,path.lastIndexOf('/'));
			}
		}
	}
	
	/* */
   public static void main(String args[])
	
	{
   	ExamData ed = new ExamData();
		FilesSelectorGUI ex = new FilesSelectorGUI(ed);
		
		System.out.println(ex.jtfSubf.getText());
	}
	/* */
	
}
