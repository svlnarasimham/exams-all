package omr;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import javax.swing.*;

import common.G;
import utility.*;

public class SlnoVsBarcodeDialog extends JDialog implements ActionListener
{
	private static final long	serialVersionUID	= 1L;

	String ecode="", date="",targetFolder = "";
	int startSlno=0, startBarcode=0, nCodes=0, selEcode=0, selDate=0;
	JComboBox<String> jcbEcode;
	JComboBox<String> jcbDate;
	JTextField jtfStartSlno, jtfStartBarcode, jtfNumberOfCodes, jtfTargetFolder;

	JButton jbcancel, jbok;
	public SlnoVsBarcodeDialog()
	{
		super(G.jfParent, "Input Details for SLNO vs Barcode Generation", true);
		int ww = 500;
		int wh = 200;
		int sw = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		setSize(ww, wh);
		setLocation((sw-ww)/2, 100); 
		loadInitValues();
		
		jcbEcode = new JComboBox<String>();
		jcbEcode.addActionListener(this);
		jcbEcode.setPreferredSize(new Dimension(150,50));
		String sqlec = "select ecode from exams where eprocessed='F' order by ecode;";
		populateComboBox(jcbEcode, sqlec ,"---Select Exam Code---");
		jcbEcode.setSelectedIndex(selEcode);
		
		jcbDate = new JComboBox<String>();
		jcbDate.addActionListener(this);
		jcbDate.setPreferredSize(new Dimension(150,50));
		String sqldt = "select distinct(sdate) from schedule s, exams e where s.ecode = e.ecode and e.eprocessed='F' order by s.slno;";
		populateComboBox(jcbDate, sqldt, "--- Select Date ---");
		jcbDate.setSelectedIndex(selDate);
		
		jtfStartSlno    = new JTextField(""+ startSlno);
		jtfStartBarcode = new JTextField(""+ startBarcode);
		jtfNumberOfCodes= new JTextField(""+ nCodes);
		jtfTargetFolder = new JTextField(targetFolder);
		
		jbok=new JButton("OK");
		jbok.addActionListener(this);
		jbok.setActionCommand("OK");
		
		jbcancel=new JButton("Cancel");
		jbcancel.addActionListener(this);
		jbcancel.setActionCommand("CANCEL");
		
		Container c = getContentPane();
		c.setLayout(new GridLayout(7, 2));
		// 1st row
		c.add(new JLabel("Exam Code", JLabel.RIGHT));
		c.add(jcbEcode);
		// 2nd row
		c.add(new JLabel("Date", JLabel.RIGHT));
		c.add(jcbDate);
		// 3rd row
		c.add(new JLabel("Start Serial Number", JLabel.RIGHT));
		c.add(jtfStartSlno);
		// 4th row
		c.add(new JLabel("Start Barcode", JLabel.RIGHT));
		c.add(jtfStartBarcode);
		// 5th row
		c.add(new JLabel("Number of Codes", JLabel.RIGHT));
		c.add(jtfNumberOfCodes);
		// 6th row
		c.add(new JLabel("Destination Folder", JLabel.RIGHT));
		c.add(jtfTargetFolder);
		// 7th row
		c.add(jbcancel);
		c.add(jbok);
	}
	
	public void getData()
	{
		setVisible(true);
	}
	
	public String getEcode()
	{
		return ecode;
	}

	public String getDate()
	{
		return date;
	}

	public String getTargetFolder()
	{
		return targetFolder;
	}

	public int getStartSlno()
	{
		return startSlno;
	}

	public int getStartBarcode()
	{
		return startBarcode;
	}

	public int getnCodes()
	{
		return nCodes;
	}
	/**
	 * Populates a menu with given sql
	 * @param jcb Combobox object
	 * @param sql SQL to be executed
	 * @param zeroItem First item in the menu. If empty string, it will be ignored.
	 */
	public void populateComboBox(JComboBox<String> jcb, String sql, String firstItem)
	{
		ActionListener ax[] = jcb.getActionListeners();
		for(int i=0;i<ax.length; i++)
		{
			jcb.removeActionListener(ax[i]);
		}
		jcb.removeAllItems();
		if(firstItem.length() != 0)
		jcb.addItem(firstItem);
		if(G.dbcon.getConnection() == null)
		{
			G.dbcon.initConnection(G.dbDriver, G.dbUrl, G.dbUser, G.dbPassword);
		}
		Vector<String[]> vs = G.dbcon.executeSQL(sql);
		if(vs != null)
		{
			int sz = vs.size();
			for(int i=0;i<sz;i++)
			{
				jcb.addItem(vs.get(i)[0]);
			}
		}
		for(int i=0;i<ax.length; i++)
		{
			jcb.addActionListener(ax[i]);
		}
	}

	public void actionPerformed(ActionEvent ae)
	{
		String ac = ae.getActionCommand();
		if(ac.equals("OK"))
		{
			String x = (String)jcbEcode.getSelectedItem();
			if(x.startsWith("-"))
			{
				x = "";
			}
			ecode = x;
			x = (String)jcbDate.getSelectedItem();
			if(x.startsWith("-"))
			{
				x = "";
			}
			date = x;
			
			targetFolder = (String) jtfTargetFolder.getText().trim();
			startSlno = Utilities.parseInt(jtfStartSlno.getText().trim());
			startBarcode = Utilities.parseInt(jtfStartBarcode.getText().trim());
			nCodes = Utilities.parseInt(jtfNumberOfCodes.getText().trim());
			setVisible(false);
			saveInitValues();
		}
		else if(ac.equals("CANCEL"))
		{
			ecode = "";
			date ="";
			targetFolder = "";
			startSlno = 0;
			startBarcode=0;
			nCodes = 0;
			setVisible(false);
		}
	}
	
	public void loadInitValues()
	{
		File f = new File("./bcodes.ini");
		if(!f.exists()) return;
		try
		{
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null)
			{
				if(line.trim().length() < 10) continue;
				if(line.trim().startsWith("#")) continue;
				String arr[] = line.split("=");
				if(arr.length != 2) continue;
				String tmp0 = arr[0].trim().toUpperCase();
				String tmp1 = arr[1].trim();
				
				if(tmp0.equals("EXAMCODESELECTION"))
				{
					selEcode = Utilities.parseInt(tmp1);
				}
				else if(tmp0.equals("DATESELECTION"))
				{
					selDate = Utilities.parseInt(tmp1);
				}
				else if(tmp0.equals("FOLDER"))
				{
					targetFolder = tmp1;
				}
				else if(tmp0.equals("STARTSLNO"))
				{
					startSlno = Utilities.parseInt(tmp1);
				}
				else if(tmp0.equals("STARTBARCODE"))
				{
					startBarcode = Utilities.parseInt(tmp1);
				}
				else if(tmp0.equals("NUMBEROFCODES"))
				{
					nCodes = Utilities.parseInt(tmp1);
				}
			}
			br.close();
			fr.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void saveInitValues()
	{
		File f = new File("./bcodes.ini");
		try
		{
			FileWriter fw = new FileWriter(f);
			fw.write("ExamCodeSelection = " + jcbEcode.getSelectedIndex() +"\n");
			fw.write("DateSelection = " + jcbDate.getSelectedIndex() +"\n");
			fw.write("Folder = " + targetFolder + "\n");
			fw.write("StartSlno = " + startSlno + "\n");
			fw.write("StartBarcode = " + startBarcode + "\n");
			fw.write("NumberOfCodes = " + nCodes + "\n");
			fw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		SlnoVsBarcodeDialog sbd = new SlnoVsBarcodeDialog();
		sbd.getData();
		String x = sbd.getEcode();
		String y = sbd.getDate();
		String z = sbd.getTargetFolder();
		int a = sbd.getStartSlno();
		int b = sbd.getStartBarcode();
		int c = sbd.getnCodes();
		
		System.out.println("ECODE:" + x);
		System.out.println("Date:" + y);
		System.out.println("Folder:" + z);
		System.out.println("Start Slno: " + a);
		System.out.println("Start Barcode: " + b);
		System.out.println("Start ncodes: " + c);
		sbd.dispose();
	}
}
