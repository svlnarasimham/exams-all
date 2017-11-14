package expro;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import utility.MyDate;

public class ExamSetupGUI extends JDialog implements ActionListener
{
   private static final long serialVersionUID = 1L;
   private ExamData ed;
   
   private JTextField jtfbatch, jtfReg, jtfMandY, jtfExamTitle;
   private JComboBox<String> jcCourseCode, jcYr, jcSem, jcRs, jcYear, jcMonth;
   private JButton jbOk, jbCancel;

   String courseCodes[] = {"A: B.Tech.", "D: M.Tech", "E: M.B.A.", "F: M.C.A."};
   String years[] = {"0", "1", "2", "3", "4"}; // For PG, Year = 0
   String sems[] = {"0", "1", "2", "3", "4", "5", "6"}; // for MCA 6 semesters
   String regsup[]={"Regular", "Suplementary"};
   
   public ExamSetupGUI(ExamData ed)
   {
   	super((JFrame)Globals.jfParent, true);
   	this.ed = ed;
   	
		setLayout(new GridLayout(11,2));
		setTitle("Examination Setup");
		setSize(350,350);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JLabel jl1 = new JLabel ("Course Code", JLabel.RIGHT);
		JLabel jl2 = new JLabel ("Year", JLabel.RIGHT);
		JLabel jl3 = new JLabel ("Semester", JLabel.RIGHT);
		JLabel jl4 = new JLabel ("Reg/Sup", JLabel.RIGHT);
		JLabel jlb = new JLabel ("Batch Year(YY)", JLabel.RIGHT);
		JLabel jl5 = new JLabel ("Regulation Year (YY)", JLabel.RIGHT);
		JLabel jl6 = new JLabel ("Year of Exam", JLabel.RIGHT);
		JLabel jl7 = new JLabel ("Month of Exam", JLabel.RIGHT);
		JLabel jl8 = new JLabel ("Month & Year Text", JLabel.RIGHT);
		JLabel jl9 = new JLabel ("Exam Title", JLabel.RIGHT);
		
		jcCourseCode = new JComboBox<String>(courseCodes);
		jcYr = new JComboBox<String>(years);
		jcSem = new JComboBox<String>(sems);
		jcRs = new JComboBox<String>(regsup);
		//jc5 = new JComboBox(md.getMonthsArray());
		jcYear = new JComboBox<String>();
		int temp = MyDate.getYear();
		jcYear.addItem("" + (temp-1));
		jcYear.addItem("" + temp);
		
		jtfMandY = new JTextField();
		jtfExamTitle = new JTextField();
		
		jcMonth = new JComboBox<String>(MyDate.getMonthsArray());
		
		jtfbatch = new JTextField();
		jtfReg = new JTextField();
		
		jbOk = new JButton("OK");
		jbOk.setActionCommand("ok");
		jbOk.addActionListener(this);
		
		jbCancel = new JButton("Cancel");
		jbCancel.setActionCommand("cancel");
		jbCancel.addActionListener(this);
		
		// Course
		this.add(jl1); 
		this.add(jcCourseCode);
		
		// Year
		this.add(jl2);
		this.add(jcYr);
		
		// Sem
		this.add(jl3);
		this.add(jcSem);
		
		// Reg/sub		
		this.add(jl4);
		this.add(jcRs);
		
		// Batch
		this.add(jlb);
		this.add(jtfbatch);
		
		//Regulation
		this.add(jl5);
		this.add(jtfReg);
		
		// Year of Exam
		this.add(jl6);
		this.add(jcYear);
		
		// Month of Exam
		this.add(jl7);
		this.add(jcMonth);
		
		// Month and Year String
		this.add(jl8);
		this.add(jtfMandY);
		
		// Examination Title
		this.add(jl9);
		this.add(jtfExamTitle);
		
		// OK and Cancel buttons
		this.add(jbOk);
		this.add(jbCancel);
		
		pack();
		setLocationRelativeTo(Globals.jfParent);
		setVisible(true);
   }
   
	public void actionPerformed(ActionEvent ae)
	{
		String action = ae.getActionCommand();
		if(action.equalsIgnoreCase("ok"))
		{
			String crs, yr, sem, rs, bat, reg, y, m;
			String temp = (String)jcCourseCode.getSelectedItem();
			crs = temp.split(":")[0];
			
			yr = (String) jcYr.getSelectedItem();
			sem = (String) jcSem.getSelectedItem();
			rs = ""+((String) jcRs.getSelectedItem()).charAt(0);
			bat = jtfbatch.getText();
			if(bat.length() != 2)
			{
				jtfbatch.requestFocus();
				JOptionPane.showMessageDialog(this, "Batch must be 2 digits");
				return;
			}
			reg = jtfReg.getText();
			if(reg.length() != 2)
			{
				jtfReg.requestFocus();
				JOptionPane.showMessageDialog(this, "Regulation must be 2 digits");
				return;
			}
			int tempy = 2000 + utility.Utilities.parseInt(reg);
			if(tempy > MyDate.getYear())
			{
				jtfReg.requestFocus();
				JOptionPane.showMessageDialog(this, "Regulation can not be in Future");
				return;
			}
			if(tempy < (MyDate.getYear() - 10))
			{
				jtfReg.requestFocus();
				JOptionPane.showMessageDialog(this, "Regulation Can not be older than 10 years");
				return;
			}
			y = (String)jcYear.getSelectedItem();
			
			int tm = MyDate.getMonth((String)jcMonth.getSelectedItem());
			m = ((tm < 10) ? ("0"+tm) : (""+tm));
			
			///////  Set Values 
			ed.course = crs.charAt(0);
			ed.examMY = jtfMandY.getText().trim();
			ed.examTitle = jtfExamTitle.getText().trim();
			ed.rs = rs.toUpperCase().charAt(0);
			ed.year = utility.Utilities.parseInt(yr);
			ed.semester = utility.Utilities.parseInt(sem);
			ed.regulation = reg.trim();
			ed.monthOfExam = utility.Utilities.parseInt(m);
			ed.yearOfExam = utility.Utilities.parseInt(y);

			// Check if details are filled properly and then set exam code and exam folder
			if( (ed.course != 'X')&&(!ed.examTitle.equals(""))&&(!ed.regulation.equals("")))
			{
				ed.examCode =crs+yr+sem+rs+reg+"-"+m+"-"+y;

				// check if absolute path and construct folder name accordingly 
				if(Globals.examFolder.startsWith("/") || 
					Globals.examFolder.indexOf(':') != -1)
				{
					ed.examFolder = Globals.examFolder + Globals.pathSep + ed.examCode;
				}
				else
				{
					ed.examFolder = Globals.basePath + Globals.pathSep + Globals.examFolder + Globals.pathSep + ed.examCode;
				}

				// Check if Folder already exists
				File f = new File(ed.examFolder);
				if (f.exists())
				{
					ed.reset();
					JOptionPane.showConfirmDialog(this, "Folder Already Exists..");
					jtfExamTitle.requestFocus();
					return;
				}
				ed.ok = true;
				dispose();
			}
 		}
		else if (action.equalsIgnoreCase("cancel"))
		{
			ed.reset();
			dispose();
			//System.out.println(examCode);
		}
		
	}
	
	/* */
   public static void main(String args[])
	
	{
   	ExamData ed = new ExamData();
		ExamSetupGUI ex = new ExamSetupGUI(ed);
		System.out.println(ex.jtfExamTitle.getText());
	}
	/* */
	
}
