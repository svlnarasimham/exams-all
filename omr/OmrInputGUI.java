package omr;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;

import javax.swing.*;

import common.G;
import utility.Utilities;

/* **************************** Input GUI Class ****************** */
	public class OmrInputGUI extends JDialog implements ActionListener
	{
		String dSubCode="A0008";
		String dBasePath = "/home/qis/EXAMS";
		String dEntryType = "E";
		boolean debug = false;
		
		private static final long serialVersionUID = 1L;
		public String ecode, scode, omrdir, entryType, outdir;
		public JTextField jtfEcode, jtfSubCode, jtfBaseFolder, jtfOutFolder, jtfType;
		public JComboBox<String> jcType, jcEcode, jcSubcode;
		private JButton jbok, jbcancel, jbOmrFolder;
		public OmrInputGUI()
		{
			super((JFrame)null, true);
			setTitle("Input Data Screen for OMR Reader");
		}
		
		public void getData()
		{
			Container c = getContentPane();
			c.setLayout(new GridLayout(7,1));
			setSize(600,400);
			setLocation(300, 300);
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			setVisible(false);
			
			jtfEcode = new JTextField("XXXX");
			jtfEcode.setEditable(false);
			jtfEcode.setPreferredSize(new Dimension(200,25));
			
			jtfSubCode = new JTextField("");
			jtfSubCode.setPreferredSize(new Dimension(150,25));
			if(debug)
			{
				jtfSubCode.setText(dSubCode);
			}
			
			jtfBaseFolder = new JTextField("");
			jtfBaseFolder.setPreferredSize(new Dimension(250,25));
			if(debug)
			{
				jtfBaseFolder.setText(dBasePath);
			}
			
			jtfOutFolder = new JTextField("");
			jtfOutFolder.setPreferredSize(new Dimension(250,25));
			jtfOutFolder.setEditable(false);
			
			jtfType = new JTextField(" ");
			jtfType.setPreferredSize(new Dimension(25,25));
			jtfType.setHorizontalAlignment(JTextField.CENTER);
			if(debug)
			{
				jtfType.setText(dEntryType);
			}
			
			jbok = new JButton("OK");
			jbok.setActionCommand("Ok");
			jbcancel = new JButton("Cancel");
			jbcancel.setActionCommand("Cancel");
			
			jcEcode = new JComboBox<String>();
			jcEcode.setPreferredSize(new Dimension(150,25));
			jcEcode.setEnabled(false);
			
			jbOmrFolder = new JButton("...");
			jbOmrFolder.setActionCommand("OmrFolder");
			
			jcSubcode = new JComboBox<String>();
			jcSubcode.setPreferredSize(new Dimension(150,25));
			jcSubcode.setActionCommand("SubCode");
			jcSubcode.setEnabled(true);
			
			jcType = new JComboBox<String>(new String[] {"Select", "Internal", "External"});
			jcType.setActionCommand("IntExt");

			JPanel jp0, jp1, jp2, jp3, jp4, jp5, jp6;
			jp0 = new JPanel(new FlowLayout(FlowLayout.CENTER));
			jp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jp3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jp4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jp5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jp6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
			
			// Row 0 (Heading row)
			jp0.add(new JLabel("Enter OMR Details"));
			c.add(jp0);
			// row 1 - Exam code
			jp1.add(new JLabel("Exam Code: ", JLabel.RIGHT), JPanel.LEFT_ALIGNMENT);
			jp1.add(jtfEcode, JPanel.LEFT_ALIGNMENT);
			jp1.add(jcEcode, JPanel.LEFT_ALIGNMENT);
			c.add(jp1);
			//row 2 - Base Folder
			jp2.add(new JLabel("OMR Images Base Folder: ", JLabel.RIGHT), JPanel.LEFT_ALIGNMENT);
			jp2.add(jtfBaseFolder, JPanel.LEFT_ALIGNMENT);
			jbOmrFolder.addActionListener(this);
			jp2.add(jbOmrFolder, JPanel.LEFT_ALIGNMENT);
			c.add(jp2);
			//row 3 - Subject Code
			jp3.add(new JLabel("Subject Code: ", JLabel.RIGHT), JPanel.LEFT_ALIGNMENT);
			jp3.add(jtfSubCode, JPanel.LEFT_ALIGNMENT);
			jp3.add(jcSubcode, JPanel.RIGHT_ALIGNMENT);
			c.add(jp3);
			//row 4 - Output Folder (Automatically filled)
			jp4.add(new JLabel("Output Folder: ", JLabel.RIGHT), JPanel.LEFT_ALIGNMENT);
			jp4.add(jtfOutFolder, JPanel.LEFT_ALIGNMENT);
			c.add(jp4);
			//row 5 - Type Selection
			jp5.add(new JLabel("Internal/External (I/E) Exam: ", JLabel.RIGHT), JPanel.LEFT_ALIGNMENT);
			jp5.add(jtfType, JPanel.LEFT_ALIGNMENT);
			jcType.addActionListener(this);
			jp5.add(jcType, JPanel.LEFT_ALIGNMENT);
			c.add(jp5);
			//row 6 - Buttons 
			jbok.addActionListener(this);
			jbcancel.addActionListener(this);
			jp6.add(jbcancel);
			jp6.add(jbok);
			c.add(jp6);
			setVisible(true);
		}
		
		@Override
		public void actionPerformed(ActionEvent ae)
		{
			String cmd = ae.getActionCommand();
			if(cmd.equalsIgnoreCase("OK"))
			{
				if(jtfBaseFolder.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(this, "Images Base Folder can not be empty");
				}
				else if(jtfSubCode.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(this, "Subject Code can not be empty");
				}
				else if(jtfType.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(this, "Select Internal/External Marks");
				}
				else
				{
					ecode= jtfEcode.getText().trim();
					scode= jtfSubCode.getText().trim();
					omrdir=jtfBaseFolder.getText().trim().replace('\\', '/');
					entryType=jtfType.getText().trim().toUpperCase();
					this.setVisible(false);
				}
			}
			else if(cmd.equalsIgnoreCase("Cancel"))
			{
				ecode="";
				scode="";
				omrdir="";
				entryType="";
				jtfSubCode.setText("");
				jtfBaseFolder.setText("");
				jtfOutFolder.setText("");
				this.setVisible(false);
			}
			else if(cmd.equalsIgnoreCase("IntExt"))
			{
				String res = " ";
				String x = (String)jcType.getSelectedItem();
				if(x.startsWith(G.INTERNAL) || x.startsWith(G.EXTERNAL))
				{
					res = x.substring(0, 1).toUpperCase();
				}
				jtfType.setText(res);
			}
			else if(cmd.equalsIgnoreCase("OmrFolder"))
			{
				String x = utility.Utilities.chooseFile("/home/qis/EXAMS/BTECH-II-SEM", Utilities.OPEN, Utilities.DIRS, "..*", "Choose Images Directory");
				if(x != null)
				{
					jtfBaseFolder.setText(x.replace('\\', '/'));
					loadSubjects(x);
				}
			}
			else if(cmd.equalsIgnoreCase("SubCode"))
			{
				String x = (String)jcSubcode.getSelectedItem();
				if(x.startsWith("Select"))
				{
					x = "";
				}
				jtfSubCode.setText(x);
			}
		}
		
		private void loadSubjects(String path)
		{
			String files[] = utility.Utilities.getFiles(path, ".....");
			System.out.println("Path:" + path);
			System.out.println("Files: " + files.length);
			Arrays.sort(files);
			jcSubcode.removeActionListener(this);
			jcSubcode.removeAllItems();
			jcSubcode.addItem("Select");
			for(int i=0;i<files.length; i++)
			{
				if((new File(path+"/" + files[i])).isDirectory())
				{
					jcSubcode.addItem(files[i]);
				}
			}
			jcSubcode.setSelectedIndex(0);
			jcSubcode.addActionListener(this);
		}
		
		public void close()
		{
			dispose();
		}
		
		public static void main(String args[])
		{
			OmrInputGUI og = new OmrInputGUI();
			og.getData();
			og.close();
			System.out.println("Ecode:" + og.ecode);
			System.out.println("Sub code:" + og.scode);
			System.out.println("omr folder:" + og.omrdir);
			System.out.println("Type: " + og.entryType);
		}
	}
	
