package common;

import java.util.ArrayList;
import java.util.Enumeration;

import utility.Utilities;

public class ExamConstants
{
	// Organization details
	public String   orgName          = "Jawaharlal Nehru Technological University Hyderabad";
	public String	instituteName    = "QIS College of Engineering and Technology";
	public String	instituteAddress = "Vegamukkapalem, Ongole-523272, Prakasam District, AP";
	public String   institutePrefix  = "qis";
	
	// Exam code pattern
	public String ecodePattern = "^[ABCDEF][0-9]{10}[RS][0-9]{2}$"; // CC+YOE+MOE+BAT+REG+YR+SEM+RS+SLNO

	// Base Path and path separator
	public String	pathSep          = "/";
	public String	basePath         = "/home" + "/" + institutePrefix + "exams";
	
	// Standard Folders
	public String	examFolder     = "exams";
	public String	trashFolder    = "trash";
	public String	configFolder   = "conf";
	public String	logFolder      = "logs";
	public String	backupFolder   = "backup";
	public String	defaultsFolder = "defaults";
	public String   outputFolder   = "output";
	public String	pdfFolder      = "pdf";
	public String   htFolder       = "ht";
	public String	photosFolder   = "photos";
	public String   barcodesFolder = "barcodes";

	// Standard Files
	public String    usersFile      = "users.txt";
	public String    courseFile     = "courses.txt";
	public String    branchesFile   = "branches.txt";
	public String    subjectsFile   = "subjects.txt";
	
	public String	logoFile         = "qislogo.jpg";
	public String	configFile       = "qis.ini";
	public String	cesigFile        = "default-ce-signature.jpg";
	public String	prsigFile        = "default-pr-signature.jpg";
	public String	logFile          = "log.txt";
	public String	defaultCesigFile = "default-ce-signature.jpg";
	public String	defaultPrsigFile = "default-pr-signature.jpg";
	public String	defaultPhotoFile = "default-photo.jpg";
	public String   defaultRulerFile = "default-ruler.jpg";

	// Barcode related Constants
	public String intOmrCoordsFile = "int-omr1.txt";
	public String extOmrCoordsFile = "ext-omr1.txt";

	public final String INTERNAL = "I";
	public final String EXTERNAL = "E";
	
	public void loadConstants()
	{
		String sql = "select fldname, fldvalue from constants;";
		ArrayList<String[]> al = G.dbcon.executeSQL(sql);
		if(al.size() == 0) 
		{
			return;
		}
		
		for(String arr[]: al)
		{
			String key = arr[0];
			String val = arr[1];
			// Assign value to appropriate variable
			// Organization Name
			if     (equals(key, "OrganizationName"))    {orgName=val;}
			else if(equals(key, "InstituteName"))    {instituteName = val;}
			else if(equals(key, "InstituteAddress")) {instituteAddress = val;}
			else if(equals(key, "InstituteLogoFile")){logoFile = val;}
			else if(equals(key, "InstitutePrefix"))  { institutePrefix = val;}
			// Path and path separator
			else if(equals(key, "basepath"))	{basePath = val;}
			else if(equals(key, "PathSep")) 	{pathSep=val;}
			// Program folders
			else if(equals(key, "ConfigFolder ")) {configFolder =val;}
			else if(equals(key, "ExamFolder ")) {examFolder =val;}
			else if(equals(key, "BackupFolder")){backupFolder=val;}
			else if(equals(key, "TrashFolder")) {trashFolder=val;}
			else if(equals(key, "LogFolder")) 	{logFolder=val;}
			else if(equals(key, "defaultsFolder")){defaultsFolder = val;}
			else if(equals(key, "PdfFolder")){pdfFolder = val;}
			else if(equals(key, "PhotosFolder")){photosFolder = val;}
			else if(equals(key, "HallTicketsFolder")){htFolder = val;}
			else if(equals(key, "BarcodesFolder")){barcodesFolder = val;}
			// Program file names
			else if(equals(key, "LogFile "))	{logFile =val;}
			else if(equals(key, "PrincipalSignatureFile")){prsigFile = val;}
			else if(equals(key, "CESignatureFile")){cesigFile = val;}
			else if(equals(key, "configFile")) { configFile = val;}
			else if(equals(key, "defaultPhotoFile")) {defaultPhotoFile = val;}
			else if(equals(key, "defaultRulerFile")) {defaultRulerFile = val;}
			// Barcode related Constants
			else if(equals(key, "IntOmrCoordsFile")) {intOmrCoordsFile = val;}
			else if(equals(key, "ExtOmrCoordsFile")) {extOmrCoordsFile = val;}
		}
	}

	private static boolean equals(String s1, String s2)
	{
		s1 = s1.replaceAll(" ", "").replaceAll("_", "");
		s2 = s2.replaceAll(" ", "").replaceAll("_", "");
		return s1.trim().equalsIgnoreCase(s2.trim());
	}


}
