package common;
import javax.swing.*;
import javax.swing.border.*;

import utility.MyConsole;
import utility.Utilities;

import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame
{
	public static final long	serialVersionUID	= 1L;
	int	wh, ww, wx, wy; // Window Height, Width, location x,y
	String winTitle; // Window Title
	boolean isWinCentered; // is Window Centered
	public JTextArea consoleTextArea;
	public JScrollPane jsp;
	
	MenuActionListener mal;
	JMenu mAdmin;
	
	JMenu mPreExam;
	JMenuItem miGenHt;
	JMenuItem miGenSlVsBarcode;
	JMenuItem miGenThOMR;
	JMenuItem miGenLabOMR;
	JMenuItem miPreOther;
	
	JMenu mPostExam;
	JMenu mReports;
	JMenu mOther;
	JMenuItem mExit;
	
	JMenu mUsr;
	JMenuItem miAddUsr;
	JMenuItem miDelUsr;
	JMenuItem miDisUsr;
	JMenuItem miEditUsr;

	JMenu mCrs;
	JMenuItem miAddCrs;
	JMenuItem miDelCrs;
	JMenuItem miEditCrs;
	
	JMenu mBrn;
	JMenuItem miAddBrn;
	JMenuItem miDelBrn;
	JMenuItem miEditBrn;
	
	JMenu mExm;
	JMenuItem miAddExm;
	JMenuItem miDelExm;
	JMenuItem miEditExm;
	
	JMenu mLog;
	JMenuItem miViewLog;
	JMenuItem miClrLog;
	JMenuItem miBkupLog;
	
	JMenu mImport;
	JMenuItem miIm;
	JMenuItem miEm;
	JMenuItem miNr;
	JMenuItem miSb;
	JMenuItem miProcess;
	JMenuItem miGenMm;
	JMenuItem miGenCmm;
	JMenuItem miRecount;
	JMenuItem miReval;
	JMenuItem miExport;
	JMenuItem miStDetails;
	JMenuItem miBrRanks;
	JMenuItem miColRanks;
	JMenuItem miTask1;
	JMenuItem miTask2;
	JMenuItem miTask3;

	////////////////// Constructor ////////////////////
	public MainMenu()
	{
		///////// Set Window Properties //////////////////////////////////
		setTitle("QIS Exam Processing System"); // Set Window Title
		setLayout(new BorderLayout());
		mal =  new MenuActionListener(this);
		// Set Height and Width to the screen default size
		wh = 800;
		ww = 800;
		int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight =(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		wx = (screenWidth-ww)/2;
		wy = (screenHeight-wh)/2;
		setPreferredSize(new Dimension(ww, wh));
		setLocation(wx, wy); // Set starting point of window
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
			G.exit(1);
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new Terminate());
		/////////////////// End of Windows Properties Setting //////////////////
		
		// Get Container handle
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		// Create JMenuBar object
		JMenuBar menubar = new JMenuBar();
		menubar.setBorder(new BevelBorder(BevelBorder.RAISED));
		menubar.setBorderPainted(true);
		
		consoleTextArea = new JTextArea();
		jsp = new JScrollPane(consoleTextArea, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// Add this MenuBar to the container
		container.add(menubar, BorderLayout.NORTH);
		container.add(menubar, BorderLayout.NORTH);
		container.add(new JLabel(" "), BorderLayout.EAST);
		container.add(new JLabel(" "), BorderLayout.WEST);
		container.add(new JLabel(" "), BorderLayout.SOUTH);
		container.add(jsp, BorderLayout.CENTER);

		///// Menubar Items ////////////////
		// Admin
		mAdmin = new JMenu("Admin");
		menubar.add(mAdmin);
		mAdmin.setMnemonic('A');
		// Pre Examination
		mPreExam = new JMenu("PreExam");
		menubar.add(mPreExam);
		mPreExam.setMnemonic('P');
		// Post Examination
		mPostExam = new JMenu("Exam");
		menubar.add(mPostExam);
		mPostExam.setMnemonic('E');
		// Reports
		mReports = new JMenu("Reports");
		menubar.add(mReports);
		mReports.setMnemonic('R');
		// Other Tasks
		mOther = new JMenu("OtherTasks");
		menubar.add(mOther);
		mOther.setMnemonic('O');
		// Exit
		mExit = new JMenuItem("Exit");
		menubar.add(mExit);
		mExit.setMnemonic('E');
		mExit.setActionCommand("mExit");
		mExit.addActionListener(mal);

		//////////// Items in Admin Menu
		// admin:User
		mUsr = new JMenu("User");
		mAdmin.add(mUsr);
		mUsr.setMnemonic('U');
		// admin:user:Add/Remove/Disable/Modify user
		addMenuItem(mUsr, "Add", "miAddUsr", 'A', mal);
		addMenuItem(mUsr, "Remove", "miDelUsr", 'R', mal);
		addMenuItem(mUsr, "Disable", "miDisUsr", 'D', mal);
		addMenuItem(mUsr, "Modify", "miEditUsr", 'M', mal);
		// admin:course submenu
		mCrs = new JMenu("Course");
		mAdmin.add(mCrs);
		mCrs.setMnemonic('C');
		
		// admin:course:add/remove/modify course
		addMenuItem(mCrs, "Add",    "miAddCrs",  'A', mal);
		addMenuItem(mCrs, "Remove", "miDelCrs",  'R', mal);
		addMenuItem(mCrs, "Modify", "miEditCrs", 'M', mal);
		// admin:Branch submenu
		mBrn = new JMenu("Branch");
		mAdmin.add(mBrn);
		mBrn.setMnemonic('B');
		// admin:branch:add/remove/modify branch
		addMenuItem(mBrn, "Add",    "miAddBrn",  'A', mal);
		addMenuItem(mBrn, "Remove", "miDelBrn",  'R', mal);
		addMenuItem(mBrn, "Modify", "miEditBrn", 'M', mal);
		// admin:Examination add/remove/modify Exam
		mExm = new JMenu("Exam");
		mAdmin.add(mExm);
		mExm.setMnemonic('E');
		addMenuItem(mExm, "Add",    "miAddExm",  'A', mal);
		addMenuItem(mExm, "Remove", "miDelExm",  'R', mal);
		addMenuItem(mExm, "Modify", "miEditExm", 'M', mal);
		// admin:Logs submenu
		mLog = new JMenu("Logs");
		mAdmin.add(mLog);
		mLog.setMnemonic('L');
		// admin:logs:view log
		addMenuItem(mLog, "View",    "miViewLog",  'V', mal);
		addMenuItem(mLog, "Clear",   "miClrLog",   'C', mal);
		addMenuItem(mLog, "Backup",  "miBkupLog",  'C', mal);
		///////////// Pre Examination Menu
		addMenuItem(mPreExam, "Gen Hall Ticket",    "miGenHt",  'H', mal);// PreExam:Generate Hall Tickets
		addMenuItem(mPreExam, "Gen Slno Vs Barcode",    "miGenslVsBarcode",  'S', mal);// PreExam:Generate SLNO vs Barcodes 
		addMenuItem(mPreExam, "Gen Theory OMR",    "miGenThOMR",  'T', mal);// PreExam:Generate Theory Answer book Cover Page OMR sheets 
		addMenuItem(mPreExam, "Gen Lab OMR",    "miGenLabOMR",  'L', mal);// PreExam:Generate Lab Marks OMR 
		addMenuItem(mPreExam, "Import NR",    "miNR",  'N', mal);// PreExam:Generate Lab Marks OMR 
		addMenuItem(mPreExam, "Import Subjects",    "miSb",  'S', mal);// PreExam:Generate Lab Marks OMR 
		
		///////////// Post Examination Menu
		// PostExam:import marks submenu
		mImport = new JMenu("Import");
		mPostExam.add(mImport);
		mImport.setMnemonic('I');
		addMenuItem(mImport, "Int. Marks",   "miIm",  'I', mal);// PostExam:import:internal marks
		addMenuItem(mImport, "Ext. Marks",   "miEm",  'I', mal);// PostExam:import:external marks
		addMenuItem(mPostExam, "Process",    "miProcess",  'P', mal);// PostExam:Process submemu
		addMenuItem(mPostExam, "Gen Memos",  "miGenMm",  'M', mal);// PostExam:Generate Memos
		addMenuItem(mPostExam, "Gen CMM/PC", "miGenCmm", 'C', mal);// PostExam:Generate CMM/PC
		addMenuItem(mPostExam, "Recounting", "miRecount",'R', mal);// PostExam:Recounting
		addMenuItem(mPostExam, "Revaluation","miReval",  'V', mal);// PostExam:Revaluation
		addMenuItem(mPostExam, "Export Web Data","miExport",  'X', mal);// PostExam:Export
		// Reports
		// Reports:Student details 
		miStDetails = new JMenuItem("Student Details");
		mReports.add(miStDetails);
		miStDetails.setMnemonic('S');
		miStDetails.setActionCommand("miStDetails");
		miStDetails.addActionListener(mal);
		// Reports:Branch Ranks
		miBrRanks = new JMenuItem("Branch Ranks");
		mReports.add(miBrRanks);
		miBrRanks.setMnemonic('B');
		miBrRanks.setActionCommand("miBrRanks");
		miBrRanks.addActionListener(mal);
		// Reports:College Ranks
		miColRanks = new JMenuItem("College Ranks");
		mReports.add(miColRanks);
		miColRanks.setMnemonic('C');
		miColRanks.setActionCommand("miColRanks");
		miColRanks.addActionListener(mal);
		// Reports:Task 1
		addMenuItem(mOther, "Task 1", "miTask1", '1', mal);
		// Reports:Task 2
		addMenuItem(mOther, "Task 3", "miTask3", '3', mal);
		// Reports:task 3
		addMenuItem(mOther, "Task 3", "miTask3", '3', mal);
		setPreferredSize(new Dimension(800, 600));
		pack();
		setVisible(true);
		G.jfParent = this;
        G.out = new MyConsole(500, 300);
        G.out.setLocation(400, 400);
        //G.out.openConsole();
		G.println("Start of Program");
		G.println("-----------------");
	}

	public void addMenuItem(JMenu parent, String label, String actionCommand, char mnemonic, ActionListener ae)
	{
		JMenuItem jmi = new JMenuItem(label);
		jmi.setMnemonic(mnemonic);
		jmi.setActionCommand(actionCommand);
		jmi.addActionListener(ae);
		parent.add(jmi);
	}

	// Terminates program when the user closes the Frame object.
	class Terminate extends WindowAdapter
	{
		public void windowClosing (WindowEvent e)
		{
			G.exit(0);
		}// end windowClosing()

	}// end class Terminate
	
	
	public static void main (String args[])
	{
		G.initialize();
		int nrec = G.regulations.loadRegulations();
		if(nrec <=0)
		{
			Utilities.showMessage(G.jfParent, "Can not load regulations from database");
			G.exit(1);
		}
		System.out.println("Regulations Loade:" + nrec);
		nrec = G.courses.loadCourses();
		if(nrec <= 0)
		{
			Utilities.showMessage(G.jfParent, "Can not load courses from database");
			G.exit(1);
		}
		
		// Load Branches 
		nrec = G.branches.loadBranches();
		if(nrec <= 0)
		{
			Utilities.showMessage(G.jfParent, "Can not find Branches from database");
			G.exit(1);
		}
		
		new MainMenu();
		//System.out.println("Menu Started....");
	}

}

