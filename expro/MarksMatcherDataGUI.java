package expro;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import utility.Store;
import utility.Utilities;
public class MarksMatcherDataGUI extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	String baseFolder;
	String cmFile, chFile, entryType;
	public JTextField jtfCmf, jtfChf, jtfOutFolder, jtfType;
	private JButton jbok, jbcancel, jbcmf, jbchf;
	//private JComboBox<String> jcType;
	private ButtonGroup bg;
	private JRadioButton jrt, jrl; // theory and lab radio buttons
	private String initialPath = "./";
	public MarksMatcherDataGUI()
	{
		super((JFrame)null, true);
		setTitle("Input Data Screen");
	}
	
	public void getData()
	{
		this.getData("./");
	}
	
	public void getData(String initialPath)
	{
		if(!Store.isInitialized())
		{
			Store.initialize();
		}
		Container c = getContentPane();
		c.setLayout(new GridLayout(5,1));
		setSize(500,400);
		setLocation(300, 300);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setVisible(false);
		
		jtfCmf = new JTextField("");
		jtfCmf.setEditable(false);
		jtfCmf.setPreferredSize(new Dimension(250,25));
		
		jtfChf = new JTextField("");
		jtfChf.setEditable(false);
		jtfChf.setPreferredSize(new Dimension(250,25));
		
		jtfOutFolder = new JTextField("");
		jtfOutFolder.setEditable(false);
		jtfOutFolder.setPreferredSize(new Dimension(250,25));
		jtfOutFolder.setEditable(false);
		
		jtfType = new JTextField(" ");
		jtfType.setPreferredSize(new Dimension(25,25));
		jtfType.setHorizontalAlignment(JTextField.CENTER);

		jbok = new JButton("OK");
		jbok.setActionCommand("Ok");
		jbok.addActionListener(this);
		
		jbcancel = new JButton("Cancel");
		jbcancel.setActionCommand("Cancel");
		jbcancel.addActionListener(this);
		
		jbcmf = new JButton("...");
		jbcmf.setActionCommand("CMF");
		jbcmf.addActionListener(this);
		
		jbchf = new JButton("...");
		jbchf.setActionCommand("CHF");
		jbchf.addActionListener(this);
		
		//jcType = new JComboBox<String>(new String[] {"Theory", "Lab"});
		//jcType.setActionCommand("TL");

		jrt = new JRadioButton("Theory");
		jrt.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				jtfType.setText("T");
			}
		});
		
		jrl = new JRadioButton("Lab");
		jrl.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				jtfType.setText("L");
			}
		});
		
		bg = new ButtonGroup();
		bg.add(jrt);
		bg.add(jrl);
		
		JPanel jp0, jp1, jp2, jp3,jp4;
		jp0 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		jp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jp3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jp4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Row 0 (Heading row)
		jp0.add(new JLabel("Choose Files to Match"));
		c.add(jp0);
		
		// row 1 - Code vs Marks File
		jp1.add(new JLabel("Code Vs Marks File: ", JLabel.RIGHT), JPanel.LEFT_ALIGNMENT);
		jp1.add(jtfCmf, JPanel.LEFT_ALIGNMENT);
		jp1.add(jbcmf, JPanel.LEFT_ALIGNMENT);
		c.add(jp1);
		
		//row 2 - Base Folder
		jp2.add(new JLabel("Code Vs Roll No. File: ", JLabel.RIGHT), JPanel.LEFT_ALIGNMENT);
		jp2.add(jtfChf, JPanel.LEFT_ALIGNMENT);
		jp2.add(jbchf, JPanel.LEFT_ALIGNMENT);
		c.add(jp2);
		
		//row 3 - Subject Code
		//jp3.add(new JLabel("Theory/Lab (T/L) Exam: ", JLabel.RIGHT), JPanel.LEFT_ALIGNMENT);
		//jp3.add(jtfType, JPanel.LEFT_ALIGNMENT);
		//jp3.add(jcType, JPanel.LEFT_ALIGNMENT);
		jp3.add(new JLabel("Select Theory/Lab:", JLabel.RIGHT),JPanel.LEFT_ALIGNMENT);
		jp3.add(jtfType, JPanel.LEFT_ALIGNMENT);
		jp3.add(jrt, JPanel.LEFT_ALIGNMENT);
		jp3.add(jrl, JPanel.LEFT_ALIGNMENT);
		c.add(jp3, JPanel.LEFT_ALIGNMENT);

		//row 4 - Buttons 
		jp4.add(jbcancel);
		jp4.add(jbok);
		c.add(jp4);
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		String cmd = ae.getActionCommand();
		if(cmd.equalsIgnoreCase("OK"))
		{
			if(jtfCmf.getText().trim().length() == 0)
			{
				JOptionPane.showMessageDialog(this, "Code Vs Marks file name can not be empty");
			}
			else if(jtfChf.getText().trim().length() == 0)
			{
				JOptionPane.showMessageDialog(this, "Code Vs Rollnos file name can not be empty");
			}
			else if(jtfType.getText().trim().length() == 0)
			{
				JOptionPane.showMessageDialog(this, "Select Theory/Lab");
			}
			else
			{
				cmFile= jtfCmf.getText().trim();
				chFile= jtfChf.getText().trim();
				entryType=jtfType.getText().trim().toUpperCase();
				String basepathcm=new File(cmFile).getParentFile().getAbsolutePath();
				String basepathch=new File(chFile).getParentFile().getAbsolutePath();
				Store.put("MarksMatcher.cm", basepathcm);
				Store.put("MarksMatcher.ch", basepathch);
				Store.save();
				dispose();
			}
		}
		else if(cmd.equalsIgnoreCase("Cancel"))
		{
			cmFile="";
			chFile="";
			entryType="";
			jtfChf.setText("");
			jtfCmf.setText("");
			jtfType.setText("");
			jrt.setSelected(false);
			jrl.setSelected(false);
			dispose();
		}
		else if(cmd.equalsIgnoreCase("CHF"))
		{
			String path = Store.getValue("MarksMatcher.ch");
			if(path.equals(""))
			{
				path = initialPath;
			}
			String x = utility.Utilities.chooseFile(path, Utilities.OPEN, Utilities.FILES, "..*", "Choose Code Vs Rollno file");
			if(x != null)
			{
				jtfChf.setText(x.replace('\\', '/'));
			}
		}
		else if(cmd.equalsIgnoreCase("CMF"))
		{
			String path = Store.getValue("MarksMatcher.cm");
			if(path.equals(""))
			{
				path = initialPath;
			}
			String x = utility.Utilities.chooseFile(path, Utilities.OPEN, Utilities.FILES, "..*", "Choose Code Vs Marks file");
			if(x != null)
			{
				jtfCmf.setText(x.replace('\\', '/'));
			}
		}
	}
	
	public void close()
	{
		dispose();
	}
	
	public static void main(String args[])
	{
		MarksMatcherDataGUI og = new MarksMatcherDataGUI();
		og.getData();
		System.out.println("CHF:" + og.chFile);
		System.out.println("CMF:" + og.cmFile);
		System.out.println("Type: " + og.entryType);
	}
	

}
