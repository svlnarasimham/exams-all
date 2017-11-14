package common;
import java.util.*;
import javax.swing.*;

import expro.ExamData;
import expro.Nrolls;
import utility.*;

public class G
{
	static
	{
		//System.setProperty("javax.net.debug", "all");
		//System.setProperty("javax.net.ssl.keyStore","/home/svl/ssl/keystore");
		//System.setProperty("javax.net.ssl.keyStorePassword","svl123");
		//System.setProperty("javax.net.ssl.trustStore","/home/svl/ssl/truststore");
		//System.setProperty("javax.net.ssl.trustStorePassword","svl123");
	}
	// Organization details -- 
	public static String	instituteName    = "XXX College of Engineering and Technology";
	public static String	instituteAddress = "XXX YYY ZZZ";
	public static String    institutePrefix  = "vnr";
	public static String    serverIP         = "localhost";
	// Exam code pattern
	public static String 	ecodePattern = "^[ABCDEF][0-9]{6}[RS][0-9]{6}$";

	// Base Path and path separator
	public static String	pathSep          = "/";
	public static String	basePath         = "/home" + pathSep + institutePrefix+ pathSep+institutePrefix + "exams";
	
	// Standard Folders
	public static String	examFolder     = "exams";
	public static String	trashFolder    = "trash";
	public static String	configFolder   = "conf";
	public static String	logFolder      = "logs";
	public static String	backupFolder   = "backup";
	public static String	defaultsFolder = "defaults";
	public static String    outputFolder   = "output";
	public static String	pdfFolder      = "pdf";
	public static String    htFolder       = "ht";
	public static String	photosFolder   = "photos";
	public static String    barcodesFolder = "barcodes";
	public static String    memosFolder    = "mm";
	public static String    cmmPcFolder    = "cmmpc";

	public static String	logoFile         = "qislogo.jpg";
	public static String	configFile       = "qis.ini";
	public static String	cesigFile        = "default-ce-signature.jpg";
	public static String	prsigFile        = "default-pr-signature.jpg";
	public static String	logFile          = "log.txt";
	public static String	thyOmrCoordFile  = "theory-omr-coordinates.txt";
	public static String	labOmrCoordFile  = "lab-omr-coordinates.txt";
	public static String    defaultPhotoFile = "default-photo.jpg";
	
	// Master Table Names
	public static String insTable = "institute";
	public static String depTable = "departments";
	public static String rglTable = "regulations";
	public static String crsTable = "courses";
	public static String brnTable = "branches";
	public static String yrsTable = "yearsem";
	public static String subTable = "subjects";
	public static String seqTable = "subequ";
	public static String colTable = "colleges";
	public static String stuTable = "students";
	public static String pasTable = "passedout";
	public static String exmTable = "exams";
	public static String mksTable = "marks";
	public static String mpsTable = "mp";
	public static String scnTable = "subcounts";
	public static String crtTable = "criteria";
	public static String lnoTable = "lastnos";
	public static String grdTable = "grades";
	public static String clsTable = "classes";
	public static String usrTable = "users";
	public static String logTable = "logs";
	public static String mnuTable = "menu";
	public static String rolTable = "roles";

	// Barcode related Constants
	public static final String INTERNAL_OMR_COORD_FILE = "int-omr1.txt";
	public static final String EXTERNAL_OMR_COORD_FILE = "ext-omr1.txt";
	public static final String BARCODE_FOLDER = "barcode";
	public static final String OUTPUT_FOLDER = "output";
	public static final String INTERNAL = "I";
	public static final String EXTERNAL = "E";
	
	// Database Connection Parameters
	public static String dbName     = institutePrefix + "masters";
	public static String dbDriver   = "com.mysql.jdbc.Driver";
	public static String dbUrl      = "jdbc:mysql://" + serverIP + "/" + dbName;
	public static String dbUser     = "root";
	public static String dbPassword = "svl123"; // Change to qis123 for actual database
	// GUI
	public static JFrame jfParent = null; // Parent Window for Dialogs
	
	// Global objects for processing ......
	public static Regulations  regulations = null;
	public static Courses      courses = null;
	public static Branches     branches= null;
	public static Subjects     subjects= null;
	public static Random       rnd     = null;
	public static ExamData     ed      = null;
	public static Nrolls       nr      = null;
	public static DbUtils      dbcon   = null; // Main Connection
	public static DbUtils      dbcon2  = null; // Second connection
	public static DbUtils      dbcon3  = null; // Third connection
	public static MyConsole    out     = null; // console output panel

	public static String ecode  = ""; // Exam Code
	
	// Exam Details
	public static String examTitle     =""; 
	
	// General Constants
	public static boolean dataLoaded   = false;
	public static int	subCodeLen       = 5;
	//public static Errors err = null;
	public static String errorMsg ="";
	private static boolean initialized = false;
	public static int debugLevel = 0;  // Debug Level
	
	public static void initialize()
	{
		if(!initialized)
		{
			initialize(G.dbDriver, G.dbUrl, G.dbUser, G.dbPassword);
		}
	}
	
	public static void initialize(String dbDriver, String dbUrl, String usr, String pwd)
	{
		if(initialized)
		{
			return;
		}
		initialized = true; // see that it is not going into infinite loop
		// Create Singleton Objects
		regulations = new Regulations();
		courses  = new Courses();
		branches = new Branches();
		subjects = new Subjects();
		ed       = new ExamData();
		nr       = new Nrolls();
		dbcon    = new DbUtils();
		rnd      = new Random();

		// Initialize DB Object with driver, url, user and password
		//System.out.println("Driver: " + G.dbDriver);
		//System.out.println("URL: " + G.dbUrl);
		//System.out.println("User: " + G.dbUser);
		
		dbcon.initConnection(G.dbDriver, G.dbUrl, G.dbUser, G.dbPassword);
		if(dbcon.getConnection() == null)
		{
			utility.Utilities.showMessage("Can not connect to Database..");
			System.exit(1);
		}
		
		ArrayList<String[]> arl = dbcon.executeSQL("select name, value from " + dbName + ".constants");
		for(String arr[]: arl)
		{
			// Path and path separator
			// Program folders
			// Program file names
			String key = arr[0].trim();
			String val = arr[1].trim();
			if     (equals(key, "InstName"))    {instituteName = val;}
			else if(equals(key, "InstAddr"))    {instituteAddress = val;}
			else if(equals(key, "InstPrefix"))  {institutePrefix = val;}
			else if(equals(key, "LogoFile"))    {logoFile = val;}
			else if(equals(key, "basepath"))	{basePath = val;}
			else if(equals(key, "PathSep")) 	{pathSep=val;}
			else if(equals(key, "ConfigFolder")){configFolder =val;}
			else if(equals(key, "ExamFolder ")) {examFolder =val;}
			else if(equals(key, "BarcodesFolder ")){barcodesFolder =val;}
			else if(equals(key, "EcodePattern ")){ecodePattern =val;}
			else if(equals(key, "BackupFolder")){backupFolder=val;}
			else if(equals(key, "TrashFolder")) {trashFolder=val;}
			else if(equals(key, "LogFolder")) 	{logFolder=val;}
			else if(equals(key, "defaultsFolder")){defaultsFolder = val;}
			else if(equals(key, "PhotosFolder")){photosFolder = val;}
			else if(equals(key, "HtFolder"))    {barcodesFolder = val;}
			else if(equals(key, "MmFolder"))	{memosFolder =val;}
			else if(equals(key, "CmmFolder"))	{cmmPcFolder =val;}
			else if(equals(key, "ConfigFile"))  {configFile =val;}
			else if(equals(key, "PrSigFile"))   {prsigFile = val;}
			else if(equals(key, "CeSigFile"))   {cesigFile = val;}
			else if(equals(key, "DefaultPhotoFile")){defaultPhotoFile = val;}
			else if(equals(key, "OutputFolder")){outputFolder = val;}
			else if(equals(key, "ThyOmrCoordFile")){thyOmrCoordFile = val;}
			else if(equals(key, "LabOmrCoordFile")){labOmrCoordFile = val;}
			else if(equals(key, "CeSigFile"))   {cesigFile = val;}
			else if(equals(key, "insTable")) {insTable = val;}
			else if(equals(key, "depTable")) {depTable = val;}
			else if(equals(key, "rglTable")) {rglTable = val;}
			else if(equals(key, "crsTable")) {crsTable = val;}
			else if(equals(key, "brnTable")) {brnTable = val;}
			else if(equals(key, "yrsTable")) {yrsTable = val;}
			else if(equals(key, "subTable")) {subTable = val;}
			else if(equals(key, "seqTable")) {seqTable = val;}
			else if(equals(key, "colTable")) {colTable = val;}
			else if(equals(key, "stuTable")) {stuTable = val;}
			else if(equals(key, "pasTable")) {pasTable = val;}
			else if(equals(key, "exmTable")) {exmTable = val;}
			else if(equals(key, "mksTable")) {mksTable = val;}
			else if(equals(key, "mpsTable")) {mpsTable = val;}
			else if(equals(key, "mnuTable")) {mnuTable = val;}
			else if(equals(key, "rolTable")) {rolTable = val;}
			else if(equals(key, "scnTable")) {scnTable = val;}
			else if(equals(key, "crtTable")) {crtTable = val;}
			else if(equals(key, "lnoTable")) {lnoTable = val;}
			else if(equals(key, "grdTable")) {grdTable = val;}
			else if(equals(key, "clsTable")) {clsTable = val;}
			else if(equals(key, "usrTable")) {usrTable = val;}
			else if(equals(key, "logTable")) {logTable = val;}
			else 
			{
				System.out.println(key + ": Not found in the list");
			}
		}
		initialized = true;
	}

	public static void setDebugLevel(int n)
	{
		debugLevel = n;
	}
	
	private static boolean equals(String s1, String s2)
	{
		return s1.trim().equalsIgnoreCase(s2.trim());
	}
	
	public static boolean isInitialized()
	{
		return initialized;
	}
	
	
	public static void printAllValues()
	{
		System.out.println("------------------------------");
		System.out.println(	"instituteName = "+ instituteName);
		System.out.println("instituteAddress = " + instituteAddress);
		System.out.println("institutePrefix = " + institutePrefix);
		System.out.println("serverIP = " + serverIP);
		System.out.println("ecodePattern = " + ecodePattern);
		System.out.println("pathSep = " + pathSep);
		System.out.println("basePath = " + basePath);
		System.out.println("examFolder = " + examFolder);
		System.out.println("trashFolder = " + trashFolder);
		System.out.println("configFolder = " + configFolder);
		System.out.println("logFolder = " + logFolder);
		System.out.println("backupFolder = " + backupFolder);
		System.out.println("defaultsFolder = " + defaultsFolder);
		System.out.println("outputFolder = " + outputFolder);
		System.out.println("pdfFolder = " + pdfFolder);
		System.out.println("htFolder = " + htFolder);
		System.out.println("photosFolder = " + photosFolder);
		System.out.println("barcodesFolder = " + barcodesFolder);
		System.out.println("memosFolder = " + memosFolder);
		System.out.println("cmmPcFolder = " + cmmPcFolder);
		System.out.println("logoFile = " + logoFile);
		System.out.println("configFile = " + configFile);
		System.out.println("cesigFile = " + cesigFile);
		System.out.println("prsigFile = " + prsigFile);
		System.out.println("logFile = " + logFile);
		System.out.println("thyOmrCoordFile = " + thyOmrCoordFile);
		System.out.println("labOmrCoordFile = " + labOmrCoordFile);
		System.out.println("defaultPhotoFile = " + defaultPhotoFile);

		System.out.println("dbName = " + dbName);
		System.out.println("dbDriver = " + dbDriver);
		System.out.println("dbUrl = " + dbUrl);
		System.out.println("dbUser = " + dbUser);
		System.out.println("dbPassword = "+dbPassword);		
		// Set various standard file names
		System.out.println("------------------------------");
	}      

	public static void closeAll()
	{
		if(dbcon != null)
		{
			dbcon.closeConnection();
			dbcon = null;
		}
		if(out != null)
		{
			try
			{
				out.clear();
			}
			catch (Exception e){}
			out=null;
		}
		initialized = false;
	}

	public static void exit(int code)
	{
		closeAll();
		System.exit(code);
	}
	
	public static void println(String s)
	{
		if(out != null)
		{
			out.println(s);
			Utilities.sleep(3);
		}
	}
	
	/***************/
	public static void main(String args[])
	{
		G.initialize();
		G.printAllValues();
		G.closeAll();
	}
	/**********************/

}
