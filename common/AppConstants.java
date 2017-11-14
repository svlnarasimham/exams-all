package common;

import java.util.ArrayList;
import java.util.HashMap;

public class AppConstants
{
	public HashMap<String, String> hmc = null;
	public AppConstants()
	{
		hmc = new HashMap<>();
	}
	
	public void loadConstants()
	{
		// Load Default Values
		hmc.put("institutename", "VNR College of Engineering and Technology");
		hmc.put("instituteaddress", "Bachupally, Hyderabad");
		hmc.put("instituteprefix", "vnr");
		hmc.put("ecodepattern", "^[a-fA-F][0-9]{6}[rsRS][0-9]{6}$");
		hmc.put("pathsep", "/");
		hmc.put("examfolder", "exams");
		hmc.put("trashfolder", "trash");
		hmc.put("configfolder", "conf");
		hmc.put("logfolder", "logs");
		hmc.put("backupfolder", "backup");
		hmc.put("defaultsfolder", "defaults");
		hmc.put("outputfolder", "output");
		hmc.put("pdffolder", "pdf");
		hmc.put("htfolder", "ht");
		hmc.put("photosfolder", "photos");
		hmc.put("barcodesfolder", "barcodes");
		hmc.put("memosfolder", "mm");
		hmc.put("cmmpcfolder", "cmmpc");
		hmc.put("logofile", "logo.jpg");
		hmc.put("configfile", "qis.ini");
		hmc.put("cesigfile", "cesig.jpg");
		hmc.put("prsigfile", "prsig.jpg");
		hmc.put("logfile", "log.txt");
		hmc.put("extomrcoordfile", "theory-omr-coordinates.txt");
		hmc.put("labomrcoordfile", "lab-omr-coordinates.txt");
		hmc.put("defaultphotofile", "default-photo.jpg");
		hmc.put("instable", "institute");
		hmc.put("deptable", "departments");
		hmc.put("rgltable", "regulations");
		hmc.put("crstable", "courses");
		hmc.put("brntable", "branches");
		hmc.put("yrstable", "yearsem");
		hmc.put("subtable", "subjects");
		hmc.put("seqtable", "subequ");
		hmc.put("coltable", "colleges");
		hmc.put("stutable", "students");
		hmc.put("pastable", "passedout");
		hmc.put("exmtable", "exams");
		hmc.put("mkstable", "marks");
		hmc.put("mpstable", "mp");
		hmc.put("scntable", "subcounts");
		hmc.put("crttable", "criteria");
		hmc.put("lnotable", "lastnos");
		hmc.put("grdtable", "grades");
		hmc.put("clstable", "classes");
		hmc.put("usrtable", "users");
		hmc.put("logtable", "logs");
		hmc.put("mnutable", "menu");
		hmc.put("roltable", "roles");
		hmc.put("barcodesfolder", "barcode");
		// Load from Database
		ArrayList<String[]> al = G.dbcon.executeSQL("select name, value from constants;");
		if(al.size() > 0)
		{
			for(String arr[]: al)
			{
				hmc.put(arr[0].toLowerCase(), arr[1]);
			}
		}
	}
	
	public String getValue(String key)
	{
		String val = hmc.get(key.toLowerCase());
		return ((val == null)? "": val);
	}

	public void setValue(String key, String val)
	{
		String sql = "replace into constants (name, value) values ('"+key+"','"+val+"');";
		G.dbcon.executeUpdate(sql);
	}
}
