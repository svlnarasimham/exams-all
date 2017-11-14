package omr;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import utility.MyDate;

import java.io.*;
import java.util.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import common.G;

public class LomrGenerator extends JFrame implements ActionListener
{
	public static final int HTLEN = 10;
	JTextField jtfcrs, jtfexm, jtfmy, jtfbrn, jtfdoe, jtfsco, jtfscn, jtfsln;
	JTextArea jta;
	JPanel jp1, jp2, jp3, jp4;
	JButton jbok, jbcancel;
	JScrollPane jsp1;
	
	private static final long	serialVersionUID	= 1L;
	public String crs="", exm="", my="", brn="", doe="", sco="", snm="", sln="";
	public String htn[];
	
	public void getData()
	{
		Container c = getContentPane();
		c.setLayout(null);
		// Debug code
		//String t = ""; // for normal operation
		String t = 	"15491A0101\n"+
						"15491A0101\n"+
						"15491A0102\n"+
						"15491A0103\n"+
						"15491A0104\n"+
						"15491A0105\n"+
						"15491A0106\n"+
						"15491A0107\n"+
						"15491A0108\n"+
						"15491A0109\n"+
						"15491A0110\n"+
						"15491A0111\n"+
						"15491A0112\n"+
						"15491A0113\n"+
						"15491A0114\n"+
						"15491A0115\n"+
						"15491A0116\n"+
						"15491A0117\n"+
						"15491A0118\n"+
						"15491A0119\n"+
						"15491A0120\n"+
						"15491A0101\n"+
						"15491A0101\n"+
						"15491A0101\n"+
						"15491A0101\n"+
						"15491A0101\n"+
						"15491A0101\n"+
						"15491A0101\n"+
						"15491A0101\n"+
						"15491A0101\n"+
						"15491A0101\n"+
						"15491A0101\n";

		// end of debug code		
		jtfcrs = new JTextField("M.C.A");
		jtfexm = new JTextField("I Sem Supplementary(R15)"); 
		jtfmy = new JTextField("June 2017"); 
		jtfbrn = new JTextField("M.C.A"); 
		jtfdoe = new JTextField(""); 
		jtfsco = new JTextField("E1593"); 
		jtfscn = new JTextField("Major Project & Comprehensive Viva");
		jtfsln = new JTextField("");
		jta = new JTextArea(40,20);
		jta.setText(t);
		jsp1 = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JLabel j1,j2,j3,j4,j5,j6,j7,j8,j9;
		
		int lx = 5; // left x
		int rx = 175; // right x
		
		int ch = 25;  // component height
		int rh = 30;  // row height
		int cury= 20; // current Y
		int cw  = 300;// component width
		int lw  = 150;// label width
		
		j1 = new JLabel("Course:", JLabel.RIGHT);
		j1.setBounds(lx, cury, lw, ch);
		c.add(j1);
		jtfcrs.setBounds(rx,cury,cw,ch);
		c.add(jtfcrs);
		cury += rh;
		
		j2 = new JLabel("Exam Name:", JLabel.RIGHT);
		j2.setBounds(lx, cury, lw,ch);
		c.add(j2);
		jtfexm.setBounds(rx,cury,cw,ch);
		c.add(jtfexm);
		cury += rh;
		
		j3 = new JLabel("Month & Year:", JLabel.RIGHT);
		j3.setBounds(lx, cury, lw,ch);
		c.add(j3);
		jtfmy.setBounds(rx,cury,cw,ch);
		c.add(jtfmy);
		cury += rh;
		
		j4 = new JLabel("Branch:", JLabel.RIGHT);
		j4.setBounds(lx, cury, lw,ch);
		c.add(j4);
		jtfbrn.setBounds(rx,cury,cw,ch);
		c.add(jtfbrn);
		cury += rh;
		
		j5 = new JLabel("Date of Exam:", JLabel.RIGHT);
		j5.setBounds(lx, cury, lw,ch);
		c.add(j5);
		jtfdoe.setBounds(rx,cury,cw,ch);
		c.add(jtfdoe);
		cury += rh;
		
		j6 = new JLabel("Subject Code:", JLabel.RIGHT);
		j6.setBounds(lx, cury, lw,ch);
		c.add(j6);
		jtfsco.setBounds(rx,cury,cw,ch);
		c.add(jtfsco);
		cury += rh;
		
		j7 = new JLabel("Subject Name:", JLabel.RIGHT);
		j7.setBounds(lx, cury, lw,ch);
		c.add(j7);
		jtfscn.setBounds(rx,cury,cw,ch);
		c.add(jtfscn);
		cury += rh;
		
		j8 = new JLabel("Start Serial No:", JLabel.RIGHT);
		j8.setBounds(lx, cury, lw,ch);
		c.add(j8);
		jtfsln.setBounds(rx,cury,cw,ch);
		c.add(jtfsln);
		cury += rh;
		
		j9 = new JLabel("Paste HTNos Here (one per line):", JLabel.CENTER);
		j9.setBounds(lx, cury, 400,ch);
		c.add(j9);
		cury += rh;
		
		jsp1.setBounds(100, cury, 300, 300);
		c.add(jsp1);
		cury += 310;
		
		int offsetx = 100;
		jbcancel = new JButton("Cancel");
		jbcancel.setActionCommand("Cancel");
		jbcancel.addActionListener(this);
		jbcancel.setBounds(offsetx+lx, cury, lw, ch);
		
		jbok = new JButton("OK");
		jbok.setActionCommand("Ok");
		jbok.addActionListener(this);
		jbok.setBounds(offsetx+rx, cury, lw, ch);
		c.add(jbok);
		c.add(jbcancel);
		
		// set size and show window
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500,700);
		setLocation(300, 100);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		String ac = ae.getActionCommand();
		if(ac.equals("Cancel"))
		{
			dispose();
		}
		else if(ac.equalsIgnoreCase("Ok"))
		{
			crs=jtfcrs.getText().trim(); 
			exm=jtfexm.getText().trim(); 
			my=jtfmy.getText().trim(); 
			brn=jtfbrn.getText().trim(); 
			doe=jtfdoe.getText().trim(); 
			sco=jtfsco.getText().trim(); 
			snm=jtfscn.getText().trim(); 
			sln=jtfsln.getText().trim();
			String ts = jta.getText().trim();
			htn = ts.split("[\n,\\s]"); // split on comma or whitespace or newline
			generateLabOMR();
			dispose();
		}
	}
	
	public void generateLabOMR()
	{
		// Preprocess data
		if(sln.equals("")|| htn.length == 0)
		{
			return;
		}
		int startSlno = utility.Utilities.parseInt(sln);
		if(startSlno == 0)
		{
			return;
		}
		Vector<String> v = new Vector<String>();
		for(int i=0; i<htn.length; i++)
		{
			if(htn[i].trim().length() != HTLEN)
			{
				if(htn[i].trim().length() > 0)
				{
					System.out.println("HTNO length not " + HTLEN + " for:"+htn[i]);
				}
				continue;
			}
			v.add(htn[i].trim());
		}
		String htarr[] = new String[v.size()];
		v.toArray(htarr);
		int n = htarr.length;
		// Open Document
		String ansidoe = MyDate.getAnsiDate(doe);
		String pdfFname = G.basePath+G.pathSep+G.barcodesFolder+G.pathSep+ansidoe+"-"+sco+"OMR.pdf";
		String txtFname = G.basePath+G.pathSep+G.barcodesFolder+G.pathSep+ansidoe+"-"+sco+"OMR.txt";
		Document doc;

		// Open Document 
   	doc = new Document(PageSize.A4);
   	doc.setMargins(0, 0, 0, 0);// left, right, top and bottom
   	PdfWriter pdfw=null;
   	FileWriter fw=null;
   	try
   	{
   		pdfw = PdfWriter.getInstance(doc, new FileOutputStream(pdfFname));
   		fw = new FileWriter(txtFname);
   	}
   	catch(Exception e)
   	{
   		System.out.println("Can not open document...\n" + e.getMessage());
   		e.printStackTrace();
   		System.exit(1);
   	}
   	doc.open();
   	PdfContentByte pcb = pdfw.getDirectContent();
   	PdfLomrGenerator pdflg = new PdfLomrGenerator();
   	
		// Add Pages
   	String arr[];
   	boolean res;
   	int i, j;
   	for(i = 0; i <= n/20; i++)
   	{
			int sz = Math.min(n-i*20, 20);
			if(sz == 0) // n is divisible by 
			{
				break;
			}
			arr = new String[sz];
			for(j=0;j<sz; j++)
			{
				arr[j] = htarr[j+i*20];
			}
			res = pdflg.addPage(pcb, startSlno+i, crs, exm, my, brn, doe, sco, snm, arr);
			if(res)
			{
				try
				{
					doc.newPage();
				}
				catch (DocumentException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try
				{
					for(j=0; j<sz;j++)
					{
						fw.write(sco+"\t" +(startSlno+i)+"\t"+ (j+1) + "\t" + arr[j] +"\n");
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
   	}
   	
   	try
   	{
   		doc.close();
   		fw.flush();
   		fw.close();
   	}
   	catch(Exception e)
   	{
   		e.printStackTrace();
   	}
   	finally
   	{
   		if(doc.isOpen()){doc.close();}
   	}
   	System.out.println("Document created..");
	}
	
	public static void main(String args[])
	{
		LomrGenerator lg = new LomrGenerator();
		lg.getData();
 	}
}
