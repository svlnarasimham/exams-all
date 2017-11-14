package omr;
import java.awt.image.*;
import javax.swing.*;

import common.G;
import utility.Utilities;

public class Validator
{
	public JFrame parent;
	public ImageDisplayer imgdsp;
	public OmrSheet os;
	public String type;
	public String inputData;
	//OmrRecord omrRecord;
	
	/**
	 * Constructor
	 * @param parent Parent JFrame
	 */
	public Validator(JFrame parent)
	{
		this.parent = parent;
		imgdsp = new ImageDisplayer("OMR Data Validation", 700, 600);
		os = null;
		type = ""; // default
		inputData = "";
	}
	
	/**
	 * Validates a given sheet
	 * @param bi Buffered Image that should be displayed when errors present
	 * @param os OmrSheet Object that contains reference point and coordinates for each field
	 * @param inputData Data object that contains the scanned and recognized field values
	 * @param type Type of sheet (INTERNAL | EXTERNAL) for now.
	 * @return Corrected OmrRecord
	 */
	public void validateSheet(
			String imageFileName,
			BufferedImage bi, 
			OmrSheet os,
			OmrRecord or,
			String type
			)
	{
		imgdsp.setTitle("OMR Data Validation:" + imageFileName);
		imgdsp.setOmrSheet(os); // Set OmrSheet Object
	   ImageTools.rotateImage(bi, os.tiltAngle, os.refX, os.refY);
		imgdsp.setIcon(bi);  // Set Image
		
		if(type.equals(G.EXTERNAL))
		{
			doExternalValidation(or);
		}
		else if(type.equals(G.INTERNAL))
		{
			doInternalValidation(or);
		}
		else
		{
			os.println("Type should be " + G.EXTERNAL + " or " + G.INTERNAL + " only...");
			// do nothing for now..
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private void doExternalValidation(OmrRecord omrRecord)
	{
		// Entry Number
		String x;
		if(!omrRecord.fieldValues[0].equals("1") && 
				!omrRecord.fieldValues[0].equals("2")) // Entry number can be 1 or 2 only
		{
			x = imgdsp.getField("Enter Entry Number:", 0, "1");
			omrRecord.fieldValues[0] = x;
		}
		
		// Marks (leading zero is OK but trailing blank is not allowed)
		// No ? symbol in both digits
		x = (omrRecord.fieldValues[1]+omrRecord.fieldValues[2]).replace('.', ' ');
		x = Utilities.ltrim(x);
		if(x.indexOf(' ')!= -1 || x.indexOf('?') != -1 || x.length() == 0)
		{
			x = (imgdsp.getFieldGroup("Enter Marks:", new int[]{1,2}, x)).trim();
			for(int j = 0; j < 2-x.length(); j++)
			{
				x = "."+x;
			}
			omrRecord.fieldValues[1] = x.substring(0, 1);
			omrRecord.fieldValues[2] = x.substring(1);
		}
	}
	
	private OmrRecord doInternalValidation(OmrRecord omrRecord)
	{
		int i, idx;
		String x;
		boolean error;
		for(i=0;i<20;i++)
		{
			idx = i*4;
			x = (omrRecord.fieldValues[idx] +
					omrRecord.fieldValues[idx+1] +
					omrRecord.fieldValues[idx+2] +
					omrRecord.fieldValues[idx+3]).replace('.', ' ').toUpperCase();
			if(x.startsWith("A   "))
			{
				System.out.println("String: '" + x + "'");
			}
			error = false;
			// Case 1 - Absent but marks present
			if(x.startsWith("A")) 
			{
				if(!(x.trim().equals("A") || x.equals("A000"))) // not A followed by blanks or zeros
				{
					error = true;
				}
			}
			else
			{
				x = Utilities.ltrim(x);
				// Case 2 - Any digit is ?
				if(x.indexOf(' ') != -1 || x.indexOf('?') != -1)
				{
					error = true;
				}
			}
			
			// Marks (leading zero is OK but trailing blank is not allowed)
			if(error)
			{
				x = (imgdsp.getFieldGroup("Enter Marks:", new int[]{idx, idx+1, idx+2, idx+3}, x)).trim();
				if(x.toUpperCase().startsWith("A"))
				{
					x = "A...";
				}
				else // Left pad string
				{
					for(int j = 0; j < 4-x.length(); j++)
					{
						x = "."+x;
					}
				}
				omrRecord.fieldValues[idx+0] = x.substring(0, 1);
				omrRecord.fieldValues[idx+1] = x.substring(1, 2);
				omrRecord.fieldValues[idx+2] = x.substring(2, 3);
				omrRecord.fieldValues[idx+3] = x.substring(3);
			}
		}
		return omrRecord;
	}
	
	public void close()
	{
		imgdsp.close();
	}
}
