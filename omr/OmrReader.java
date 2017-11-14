package omr;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.tree.VariableHeightLayoutCache;

import common.G;
public class OmrReader
{
	public static void main(String[] args)
	{
		// 1. create OMR sheet object
		OmrSheet os = new OmrSheet();
		os.openConsole();
		
		// 2. Get data like subject code, folder etc., from GUI 
		//    and set paths and file names
		OmrInputGUI ig = new OmrInputGUI();
		ig.getData();
		// Check if data is entered properly
		if(ig.scode.trim().equals("") || ig.omrdir.trim().equals("") || ig.entryType.trim().equals(""))
		{
			os.println("SubCode or Path or Type not given....");
			return;
		}
		
		// Transfer values from GUI object to local variables
		//String ecode = ig.ecode.trim(); // not used as of now
		String basePath = ig.omrdir.trim();
		String subcode = ig.scode.trim();
		String entryType = ig.entryType.trim();
		ig.close();// Close GUI object
		
		// Set OMR sheet's Paths
		os.basePath = basePath+"/" + subcode;
		os.barcodeImageFolder = basePath + "/" + subcode + "/" + G.BARCODE_FOLDER;
		os.outputFolder = basePath + "/" + G.OUTPUT_FOLDER;
		os.subCode = ig.scode;
		// Set output and error file names
		String errorFileName  = os.outputFolder+"/" + subcode+"-err.txt";
		String outputFileName = os.outputFolder+"/"+subcode +"-out.txt";
		String ext = (entryType.startsWith(G.INTERNAL)?"-bsm.txt" : "-cm.txt");
		String marksFileName  = os.outputFolder+"/"+subcode+ext;
		
		// 3. Load image file names
		String imageFilesList[] = utility.Utilities.getFiles(basePath+"/"+subcode, "..*[Jj][Pp][Gg]");
		if(imageFilesList.length == 0)// no images found 
		{
			os.println("No images files found in " + basePath);
			return;
		}
		Arrays.sort(imageFilesList);
		
		// 4. Create required folders
		// create output folder if doesn't exist
		File f = new File(os.outputFolder);
		if(!f.exists())
		{
			boolean res = f.mkdirs();
			if(!res)
			{
				os.println("Can not create Folder: " + f.getAbsolutePath());
				return;
			}
		}
		// create barcode images folder if doesn't exist
		f = new File(os.barcodeImageFolder);
		if(!f.exists())
		{
			boolean res = f.mkdirs();
			if(!res)
			{
				os.println("Can not create Folder: " + f.getAbsolutePath()); 
				return;
			}
		}
		
		// 5. Load Configuration of OMR Sheet
		String omrConfigFile;
		if(entryType.equals("I"))
		{
			omrConfigFile = G.INT_CONFIG_FILE;
		}
		else if(entryType.equals("E"))
		{
			omrConfigFile = G.EXT_CONFIG_FILE;
		}
		else
		{
			os.println("OMR Type must be I or E only");
			return;
		}
		
		os.loadSheetDetails(omrConfigFile);
		//os.showData();

		// 6. For each image file
		// a. read omr data and validate data
		// b. write validated data to output file
		// c. Convert data to marks and write to marks file
		FileWriter fout=null, ferr=null, fmks=null;
		boolean append = true;
		try
		{
			if((new File(outputFileName)).exists())
			{
				int res = JOptionPane.showConfirmDialog(null,"Overwrite existing output file?", "Output File Exists", JOptionPane.YES_NO_OPTION);
				if(res == 0)
				{
					append = false;
				}
			}
			fout = new FileWriter(outputFileName, append);
			ferr = new FileWriter(errorFileName);
			fmks = new FileWriter(marksFileName, append);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int errCnt = 0;
		OmrRecord omrRecord;
		Validator validator = new Validator(null);
		BufferedImage bi;
		for(int i=0; i< imageFilesList.length; i++)
		{
			String res = os.readSheet(imageFilesList[i]);
			try
			{
				if(res.trim().length() == 0)
				{
					ferr.write(imageFilesList[i]+": Can not read data....\n");
					errCnt++;
					continue;
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			String imgFile = os.basePath+"/"+imageFilesList[i];
			bi = ImageTools.getImage(imgFile);
			omrRecord = new OmrRecord(res);
			String title = "Validating " + imgFile + " [" + (i+1) +" of " + imageFilesList.length + "]";
			validator.validateSheet(title,bi, os, omrRecord, entryType);
			os.println(imageFilesList[i] + ": " + res);
			try
			{
				fout.write(omrRecord.toString()+"\n");// Write original data to output
				
				// Convert data to marks and write to file
				if(entryType.equals(G.EXTERNAL))
				{
					String marks = (omrRecord.fieldValues[1]+omrRecord.fieldValues[2]).replace('.', ' ').replace('?', ' ').trim();
					fmks.write(omrRecord.barcode + "\t" + utility.Utilities.parseInt(marks) + "\n");
				}
				else if(entryType.equals(G.INTERNAL))
				{
					for(int ti = 0; ti<20; ti++)
					{
						int idx = ti*4;
						String marks = (omrRecord.fieldValues[idx]+
								omrRecord.fieldValues[idx+1]+
								omrRecord.fieldValues[idx+2]+
								omrRecord.fieldValues[idx+3]).replace('?', ' ').replace('.', ' ').trim();
						if(marks.startsWith("A"))
						{
							marks = "0";
						}
						fmks.write(omrRecord.barcode+"\t"+ti+"\t"+utility.Utilities.parseInt(marks)+"\n");
					}
				}
				else
				{
					System.out.println("Entry should be " + G.INTERNAL +" or " + G.EXTERNAL + " only");
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.gc();
		}
		// Close output Files 
		try
		{
			fout.close();
			ferr.close();
			fmks.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		// Delete error file if no errors found
		if(errCnt == 0)
		{
			File fe = new File(errorFileName);
			try
			{
				fe.delete();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		os.println("Everything finished...  Close the window...");
		os.closeConsole();
		validator.close();
		utility.Utilities.showMessage("Validation Completed.. Results are written to Files.");
		while(validator.imgdsp.isVisible())
		{
			try
			{
				Thread.sleep(500);
			}
			catch(Exception e){}
		}
		//System.exit(0);
	}
}
