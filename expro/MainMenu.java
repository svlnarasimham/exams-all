package expro;


import java.awt.*;
import java.awt.event.*;
//import java.io.*;
//import java.sql.*;

import javax.swing.*;

public class MainMenu extends JFrame implements ActionListener
{
	public static final long	serialVersionUID	= 1L;

	int winHeight, winWidth;	// Window Height and Width
	
	// Level 1 Menu
	JMenu	menu_01, // Masters
			menu_02, // Process
			menu_03, // Reports
			menu_04; // System
	
	////////////////////////////// Level 2 menus
	// Masters Sub menu
	JMenu	menu_01_01; // User
	JMenuItem menu_01_02; // Exam
	JMenu menu_01_03; // Log
	
	// Process Sub menu
	JMenuItem	menu_02_01, // Import Marks 
					menu_02_02, // Check Data
					menu_02_03, // Process
					menu_02_04, // Export Result
					menu_02_05; // Process Recounting
	
	// Reports Sub menu
	JMenuItem 	menu_03_01, // Generate Memos
					menu_03_02, // Generate Single
					menu_03_03, // Generate Statistics
					menu_03_04, // Student Record
					menu_03_05; // Misc
	
	// System sub menu
	JMenuItem 	menu_04_01, // Backup 
					menu_04_02, // Restore
					menu_04_03, // Misc.
					menu_04_04; // Exit;

	// Level 3
	JMenuItem 	menu_01_01_01, // Add User 
					menu_01_01_02,  // Remove User
					menu_01_01_03;  // Edit User
	
	JMenuItem 	menu_01_03_01, // View Log 
					menu_01_03_02,  // Clear Log
					menu_01_03_03;  // Backup Log

	String title = "Examination Processing Software";
	
	public MainMenu ()// Default Constructor
	{
	}
	
	public void setTitle(String ttl)
	{
		this.title = ttl;
	}
	
	public void doMainMenu ()//QPMain qpm ,
	{
		// /////// Set Window Properties //////////////////////////////////
		setTitle(title);// Set Window Title
		// Set Height and Width to the screen default size
		winHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		winWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		setPreferredSize(new Dimension(winWidth - 200, winHeight - 200));
		setLocation(100, 100); // Set starting point of window
		// setResizable(false); // Don't Allow Resizing Operations
		setBackground(Color.gray); // Set Background Color of Window

		try
		// Set Look and Feel to System Default
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// ///////////////// End of Windows Properties Setting //////////////////

		// Get Container handle
		Container container = this.getContentPane();

		// Create JMenuBar object
		JMenuBar menubar = new JMenuBar();
		menubar.setBorderPainted(true);

		// Add this MenuBar to the container
		container.add(menubar, BorderLayout.NORTH);
		Terminate t = new Terminate();
		t.setParent(this);
		this.addWindowListener(new Terminate());
		pack();
		setVisible(true);

		///// Masters
		menu_01 = new JMenu("Masters");
		menubar.add(menu_01);
		menu_01.setMnemonic('M');

		////// Process
		menu_02 = new JMenu("Process");
		menubar.add(menu_02);
		menu_02.setMnemonic('P');

		///// Paper Generation
		menu_03 = new JMenu("Reports");
		menubar.add(menu_03);
		menu_03.setMnemonic('R');

		///// Administration
		menu_04 = new JMenu("System");
		menubar.add(menu_04);
		menu_04.setMnemonic('S');

		///////////// Master Sub Menus

		/// User
		menu_01_01 = new JMenu("User");
		menu_01.add(menu_01_01);
		menu_01_01.setMnemonic('U');
		
		// User Add
		menu_01_01_01 = new JMenuItem("Add");
		menu_01_01.add(menu_01_01_01);
		menu_01_01_01.setMnemonic('A');
		menu_01_01_01.setActionCommand("menu_01_01_01");
		menu_01_01_01.addActionListener(this);

		// User remove
		menu_01_01_02 = new JMenuItem("Remove");
		menu_01_01.add(menu_01_01_02);
		menu_01_01_02.setMnemonic('R');
		menu_01_01_02.setActionCommand("menu_01_01_02");
		menu_01_01_02.addActionListener(this);

		// user edit
		menu_01_01_03 = new JMenuItem("Edit");
		menu_01_01.add(menu_01_01_03);
		menu_01_01_03.setMnemonic('E');
		menu_01_01_03.setActionCommand("menu_01_01_03");
		menu_01_01_03.addActionListener(this);

		/// Exam Setup
		menu_01_02 = new JMenuItem("Exam Setup");
		menu_01.add(menu_01_02);
		menu_01_02.setMnemonic('E');
		menu_01_02.setActionCommand("menu_01_02");
		menu_01_02.addActionListener(this);

		//// Log Submenu
		menu_01_03 = new JMenu("Logs");
		menu_01.add(menu_01_03);
		menu_01_03.setMnemonic('L');
		
		// Log view
		menu_01_03_01 = new JMenuItem("View");
		menu_01_03.add(menu_01_03_01);
		menu_01_03_01.setMnemonic('V');
		menu_01_03_01.setActionCommand("menu_01_03_01");
		menu_01_03_01.addActionListener(this);

		// Log clear
		menu_01_03_02 = new JMenuItem("Clear");
		menu_01_03.add(menu_01_03_02);
		menu_01_03_02.setMnemonic('C');
		menu_01_03_02.setActionCommand("menu_01_03_02");
		menu_01_03_02.addActionListener(this);

		// Log backup
		menu_01_03_03 = new JMenuItem("Backup");
		menu_01_03.add(menu_01_03_03);
		menu_01_03_03.setMnemonic('B');
		menu_01_03_03.setActionCommand("menu_01_03_03");
		menu_01_03_03.addActionListener(this);

		///////// Process Menu
		menu_02_01 = new JMenuItem("Import Data");
		menu_02_01.setMnemonic('I');
		menu_02_01.setActionCommand("menu_02_01");
		menu_02_01.addActionListener(this);

		menu_02_02 = new JMenuItem("Check Data");
		menu_02.add(menu_02_02);
		menu_02_02.setMnemonic('C');
		menu_02_02.setActionCommand("menu_02_02");
		menu_02_02.addActionListener(this);

		menu_02_03 = new JMenuItem("Process Data");
		menu_02.add(menu_02_03);
		menu_02_03.setMnemonic('P');
		menu_02_03.setActionCommand("menu_02_03");
		menu_02_03.addActionListener(this);

		menu_02_04 = new JMenuItem("Export Result");
		menu_02.add(menu_02_04);
		menu_02_04.setMnemonic('E');
		menu_02_04.setActionCommand("menu_02_04");
		menu_02_04.addActionListener(this);

		menu_02_05 = new JMenuItem("Recounting Process");
		menu_02.add(menu_02_05);
		menu_02_05.setMnemonic('R');
		menu_02_05.setActionCommand("menu_02_05");
		menu_02_05.addActionListener(this);

		
		menu_03_01 = new JMenuItem("Generate Memos");
		menu_03.add(menu_03_01);
		menu_03_01.setMnemonic('M');
		menu_03_01.setActionCommand("menu_03_01");
		menu_03_01.addActionListener(this);

		menu_03_02 = new JMenuItem("Generate Single Memo");
		menu_03.add(menu_03_02);
		menu_03_02.setMnemonic('S');
		menu_03_02.setActionCommand("menu_03_02");
		menu_03_02.addActionListener(this);

		menu_03_03 = new JMenuItem("Statistics");
		menu_03.add(menu_03_03);
		menu_03_03.setMnemonic('T');
		menu_03_03.setActionCommand("menu_03_03");
		menu_03_03.addActionListener(this);

		menu_03_04 = new JMenuItem("Student Record");
		menu_03.add(menu_03_04);
		menu_03_04.setMnemonic('R');
		menu_03_04.setActionCommand("menu_03_04");
		menu_03_04.addActionListener(this);

		menu_03_05 = new JMenuItem("Misc");
		menu_03.add(menu_03_05);
		menu_03_05.setMnemonic('M');
		menu_03_05.setActionCommand("menu_03_05");
		menu_03_05.addActionListener(this);

		menu_04_01 = new JMenuItem("Backup");
		menu_04.add(menu_04_01);
		menu_04_01.setMnemonic('B');
		menu_04_01.setActionCommand("menu_04_01");
		menu_04_01.addActionListener(this);

		menu_04_02 = new JMenuItem("Restore");
		menu_04.add(menu_04_02);
		menu_04_02.setMnemonic('R');
		menu_04_02.setActionCommand("menu_04_02");
		menu_04_02.addActionListener(this);

		menu_04_03 = new JMenuItem("Misc");
		menu_04.add(menu_04_03);
		menu_04_03.setMnemonic('M');
		menu_04_03.setActionCommand("menu_04_03");
		menu_04_03.addActionListener(this);

		menu_04_04 = new JMenuItem("Exit");
		menu_04.add(menu_04_04);
		menu_04_04.setMnemonic('E');
		menu_04_04.setActionCommand("menu_04_04");
		menu_04_04.addActionListener(this);
		setPermissions();
	}

	private void setPermissions()
	{
		String role = Globals.curUser.getUrole();
		int r=0;
		
		if(role.equals("admin")) r = 2;
		else if(role.equals("staff")) r = 1;
		else r = 0;
		
		//JOptionPane.showMessageDialog(null, ""+r);
		
		if( r< 2 ) // staff or guest
		{
			//////////// User 
			menu_01_01_01.setEnabled(false); // add
			menu_01_01_02.setEnabled(false); // remove
			
			//////////// Exam Creation
			menu_01_02.setEnabled(false); // add
		
			///////////// Log
			menu_01_03_01.setEnabled(false); // View
			menu_01_03_02.setEnabled(false); // Clean
			menu_01_03_03.setEnabled(false); // Backup

			
			//////////  System
			menu_04_01.setEnabled(false); // backup
			menu_04_02.setEnabled(false); // restore
		}
		if(r < 1) // Guest User
		{
			/////////// User 
			menu_01_01_03.setEnabled(false); // edit
			
			/////////// Process
			menu_02_01.setEnabled(false); // Import
			menu_02_03.setEnabled(false); // Process
			menu_02_04.setEnabled(false); // Export
			menu_02_05.setEnabled(false); // Recounting

			/////////// Reports
			menu_03_01.setEnabled(false); // Gen Memos
			menu_03_02.setEnabled(false); // Single Memo
			menu_03_03.setEnabled(false); // Statistics
			menu_03_04.setEnabled(false); // Student Record
			menu_03_05.setEnabled(false); // Student Record
		}
	}

	public void actionPerformed (ActionEvent ae)
	{
		//System.out.println("You Selected: " + (String) ae.getActionCommand());
		if (ae.getActionCommand().equals("menu_01_01_01"))
		{
			addUser();
		}
		else if (ae.getActionCommand().equals("menu_01_01_02"))
		{
			deleteUser();
		}
		else if (ae.getActionCommand().equals("menu_01_01_03"))
		{
			editUser();
		}
		else if (ae.getActionCommand().equals("menu_01_02"))
		{
			setupExam();
		}
		else if (ae.getActionCommand().equals("menu_01_03_01"))
		{
			viewLog();
		}
		else if (ae.getActionCommand().equals("menu_01_03_02"))
		{
			clearLog();
		}
		else if (ae.getActionCommand().equals("menu_01_03_03"))
		{
			backupLog();
		}
		else if (ae.getActionCommand().equals("menu_02_01"))
		{
			importData();
		}
		else if (ae.getActionCommand().equals("menu_02_02"))
		{
			checkData();
		}
		else if (ae.getActionCommand().equals("menu_02_03"))
		{
			processData();
		}
		else if (ae.getActionCommand().equals("menu_02_04"))
		{
			exportData();
		}
		else if (ae.getActionCommand().equals("menu_02_05"))
		{
			processRecounting();
		}
		else if (ae.getActionCommand().equals("menu_03_01"))
		{
			generateMemos();
		}
		else if (ae.getActionCommand().equals("menu_03_02"))
		{
			generateMemo();
		}
		else if (ae.getActionCommand().equals("menu_03_03"))
		{
			generateStatistics();
		}
		else if (ae.getActionCommand().equals("menu_03_04"))
		{
			generateStudentRecord();
		}
		else if (ae.getActionCommand().equals("menu_03_05"))
		{
			misc();
		}
		else if (ae.getActionCommand().equals("menu_04_01"))
		{
			backup();
		}
		else if (ae.getActionCommand().equals("menu_04_02"))
		{
			restore();
		}
		else if (ae.getActionCommand().equals("menu_04_03"))
		{
			misc();
		}
		else if (ae.getActionCommand().equals("menu_04_04"))
		{
			exitApp();
		}
		else
		{
			JOptionPane.showMessageDialog(this,"Action Not found: " + ae.getActionCommand());
		}
	}

	private void misc()
	{
		JOptionPane.showMessageDialog(this,"Not Implemented");
	}

	private void generateStudentRecord()
	{
		JOptionPane.showMessageDialog(this,"Not Implemented");
	}

	private void generateStatistics()
	{
		JOptionPane.showMessageDialog(this,"Not Implemented");
	}

	private void generateMemo()
	{
		JOptionPane.showMessageDialog(this,"Not Implemented");
	}

	private void generateMemos()
	{
		JOptionPane.showMessageDialog(this,"Not Implemented");
	}

	private void processRecounting()
	{
		JOptionPane.showMessageDialog(this,"Not Implemented");
	}

	private void exportData()
	{
		JOptionPane.showMessageDialog(this,"Not Implemented");
	}

	private void processData()
	{
		JOptionPane.showMessageDialog(this,"Not Implemented");
	}

	private void checkData()
	{
		boolean exc = setExamCode();
		if (exc)
		{
			CheckFiles cf = new CheckFiles();
			cf.checkAll();
		}
	}

	private void importData()
	{
		if(Globals.ed.examFolder.length() < 1)
		{
			JOptionPane.showConfirmDialog(this, "First Setup Examination");
		}
		ImportData id = new ImportData();
		id.importData();
	}
		
	private void setupExam()
	{
		if(!Globals.isUserOk())
		{
			JOptionPane.showMessageDialog(this, "Not authorized to use this");
			return;
		}

		String msg=Globals.log.createMsg(Globals.curUser.getUrole(), Globals.curUser.getUid(), "User "+Globals.curUser.uid +" Exam Setup", "Setup");
		Globals.log.writeLog(msg);
		
		//// Setup Exam Name and Create folder and Database for that Exam
		ExamData ed = new ExamData();
		ed.getExamDetails();

		Globals.ed = ed; // Store it to Globals
		
		/*
		//ExamDbCreator edc = new ExamDbCreator(g);
		int res = edc.createExam();
		if(res == -1) // If folder creation fials
		{
			JOptionPane.showMessageDialog(this, "Can not create Folder: " + Globals.ed.examFolder);
		}
		else if (res == -2)
		{
			JOptionPane.showMessageDialog(this, "DB Creation Error..: (" + url + ")");
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Database and Folders Created successfully");
		}
		*/
	}

	public void addUser ()
	{
		UserManager ue = new UserManager(this,UserManager.NEW_USER);
		if(ue.tu != null)
		{
			String msg=Globals.log.createMsg(Globals.curUser.getUrole(), Globals.curUser.getUid(), "new user "+ue.tu.uid +" is added", "adduser");
			Globals.log.writeLog(msg);
		}
	}

	public void deleteUser ()
	{
		UserManager ue = new UserManager(this,UserManager.DELETE_USER);
		if(ue.tu != null)
		{
			String msg=Globals.log.createMsg(Globals.curUser.getUrole(), Globals.curUser.getUid(), "new user "+ue.tu.uid +" is deleted", "deleteuser");
			Globals.log.writeLog(msg);
		}
	}

	public void editUser ()
	{
		UserManager ue = new UserManager(this,UserManager.EDIT_USER);
		if(ue.tu != null)
		{
			String msg=Globals.log.createMsg(Globals.curUser.getUrole(), Globals.curUser.getUid(), "User "+ue.tu.uid +" is edited", "editeuser");
			Globals.log.writeLog(msg);
		}
	}

	public void viewLog ()
	{
		Globals.log.viewLog(this);
	}

	public void clearLog ()
	{
		Globals.log.clearLog(this);
	}

	public void backupLog ()
	{
		Globals.log.backupLog(this);
		String msg=Globals.log.createMsg(Globals.curUser.getUrole(), Globals.curUser.getUid(), "User "+Globals.curUser.uid +" Log Backup Performed", "LogBackup");
		Globals.log.writeLog(msg);
	}

	public void backup ()
	{
		String msg=Globals.log.createMsg(Globals.curUser.getUrole(), Globals.curUser.getUid(), "User "+Globals.curUser.uid +" Backup", "Backup");
		Globals.log.writeLog(msg);
	}

	
	public void restore ()
	{
		String msg=Globals.log.createMsg(Globals.curUser.getUrole(), Globals.curUser.getUid(), "User "+Globals.curUser.uid +" Restore", "Restore");
		Globals.log.writeLog(msg);
	}
	
	public void exitApp ()
	{
		int res = JOptionPane.showConfirmDialog(this, "Do you really want to exit?");
		if(res != 0) return;
		//Log l=Globals.log;
  	 	//String msg=l.createMsg(Globals.curUser.getUrole(), Globals.curUser.getUid(), " user exited ", "exit");
  	 	//l.writeLog(msg);
		this.dispose();
		System.exit(0);
	}
	
	// Terminates program when the user closes the Frame object.
	class Terminate extends WindowAdapter
	{
		JFrame parent = null;
		public void setParent(JFrame p)
		{
			parent = p;
		}
		
		public void windowClosing (WindowEvent e)
		{
			((MainMenu)Globals.jfParent).exitApp();
		}// end windowClosing()

	}// end class Terminate
	
	public boolean setExamCode()
	{
		return true;
	}
	
	public static void main(String args[])
	{
		// Set Log File
		Globals.log = new Log();
		
		// Load Users
		Globals.users = new Users();
		Globals.users.loadUsers();
		
		
		// Create Main Menubar
		MainMenu mmb = new MainMenu();
		
		Globals.jfParent = mmb;

		// Get Login Details
		MyLogin ml = new MyLogin(mmb);
		ml.doLogin();
		if(Globals.curUser == null) // User not correct
		{
			JOptionPane.showMessageDialog(null, "You are not authorized to use this system");
			System.exit(1);
		}
		else if(Globals.curUser.udisabled.equalsIgnoreCase("true"))
		{
			JOptionPane.showMessageDialog(null, "User Disabled");
			System.exit(1);
		}
		
		// User is Ok. Load other details of the process.
		
		// Load Courses
		Globals.courses = new Courses();
		//Globals.courses.loadCourses();
		
		for(int i=0;i<Globals.courses.getCourseCount(); i++)
		{
			//System.out.println(Globals.courses.crs.get(i).sname);
		}
		// Load Branches
		Globals.branches = new Branches();
		//Globals.branches.loadBranches();
		
		for(int i=0; i<Globals.branches.getBranchCount(); i++)
		{
			//System.out.println(Globals.branches.brn.get(i).name);
		}
		
		// Run the menu
		mmb.setTitle("Examination Processing Software");
		mmb.doMainMenu();
	}
}
