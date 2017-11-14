package common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class MenuActionListener implements ActionListener
{
	MainMenu mm;
	public MenuActionListener(MainMenu mainMenuObject)
	{
		mm = mainMenuObject;
	}
	public void actionPerformed (ActionEvent ae)
	{
		System.out.println("You Selected: " + (String) ae.getActionCommand());
		if (ae.getActionCommand().equalsIgnoreCase("mAdmin"))
		{
		}
		else if (ae.getActionCommand().equalsIgnoreCase("mPostExam"))
		{
		}
		else if (ae.getActionCommand().equalsIgnoreCase("mReports"))
		{
		}
		else if (ae.getActionCommand().equalsIgnoreCase("mOther"))
		{
		}
		else if (ae.getActionCommand().equalsIgnoreCase("mExit"))
		{
			G.closeAll();
			exit();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("mUsr"))
		{
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miAddUsr"))
		{
			addUser();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miDelUsr"))
		{
			deleteUser();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miDisUsr"))
		{
			disableUser();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miEditUsr"))
		{
			editUser();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("mCrs"))
		{
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miAddCrs"))
		{
			addCourse();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miDelCrs"))
		{
			deleteCourse();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miEditCrs"))
		{
			editCourse();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("mBrn"))
		{
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miAddBrn"))
		{
			addBranch();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miDelBrn"))
		{
			deleteBranch();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miEditBrn"))
		{
			editBranch();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("mExm"))
		{
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miAddExm"))
		{
			addExam();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miDelExm"))
		{
			deleteExam();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miEditExm"))
		{
			editExam();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("mLog"))
		{
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miViewLog"))
		{
			viewLog();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miClrLog"))
		{
			clearLog();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miBkupLog"))
		{
			backupLog();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miGenHt"))
		{
			genHt();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miGenSlVsBarcode"))
		{
			genSlVsBarcode();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miGenThOMR"))
		{
			genThOmr();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miGenLabOMR"))
		{
			genLabOmr();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miNr"))
		{
			importNR();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miSb"))
		{
			importSub();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miIm"))
		{
			importIM();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miEm"))
		{
			importEM();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miProcess"))
		{
			processExam();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miGenMm"))
		{
			genMemos();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miGenCmm"))
		{
			genCmm();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miRecount"))
		{
			recounting();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miReval"))
		{
			revaluation();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miExport"))
		{
			exportWebData();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miStDetails"))
		{
			studentDetails();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miBrRanks"))
		{
			brnRankReport();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miColRanks"))
		{
			colRankReport();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miTask1"))
		{
			dummy1();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miTask2"))
		{
			dummy2();
		}
		else if (ae.getActionCommand().equalsIgnoreCase("miTask3"))
		{
			dummy3();
		}
		else		{
			System.out.println("Error in Action Command: " + ae.getActionCommand());
 			System.exit(1);
		}
	}

	public void exit()
	{
		// Write the actual code here
		System.exit(0);
	}

	public void addUser()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: addUser");
	}

	public void deleteUser()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: deleteUser");
	}

	public void disableUser()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: disableUser");
	}

	public void editUser()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: editUser");
	}

	public void addCourse()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: addCourse");
	}

	public void deleteCourse()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: deleteCourse");
	}

	public void editCourse()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: editCourse");
	}

	public void addBranch()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: addBranch");
	}

	public void deleteBranch()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: deleteBranch");
	}

	public void editBranch()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: editBranch");
	}

	public void addExam()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: addExam");
	}

	public void deleteExam()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: deleteExam");
	}

	public void editExam()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: editExam");
	}

	public void viewLog()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: viewLog");
	}

	public void clearLog()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: clearLog");
	}

	public void backupLog()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: backupLog");
	}

	/**
	 * Generate Hall ticket PDF file
	 */
	public void genHt()
	{
		//JOptionPane.showMessageDialog(mm,"Code not Implemented: genHT");
		HTGenerator htg = new HTGenerator();
		htg.domain();
	}

	public void genSlVsBarcode()
	{
		JOptionPane.showMessageDialog(mm, "Code not implemented: genSlVsBarcode");
	}
	
	public void genLabOmr()
	{
		JOptionPane.showMessageDialog(mm, "Code not implemented: genLabOmr");
	}
	
	public void genThOmr()
	{
		JOptionPane.showMessageDialog(mm, "Code not implemented: genThOmr");
	}
	
	public void importNR()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: importNR");
	}

	public void importSub()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: importSub");
	}

	public void importIM()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: importIM");
	}

	public void importEM()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: importEM");
	}

	public void processExam()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: processExam");
	}

	public void genMemos()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: genMemos");
	}

	public void genCmm()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: genCmm");
	}

	public void recounting()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: recounting");
	}

	public void revaluation()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: revaluation");
	}

	public void exportWebData()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: exportWebData");
	}

	public void studentDetails()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: studentDetails");
	}

	public void brnRankReport()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: brnRankReport");
	}

	public void colRankReport()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: colRankReport");
	}

	public void dummy1()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: dummy1");
	}

	public void dummy2()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: dummy2");
	}

	public void dummy3()
	{
		// Write the actual code here
		JOptionPane.showMessageDialog(mm,"Code not Implemented: dummy3");
	}

}
