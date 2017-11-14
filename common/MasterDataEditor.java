package common;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class MasterDataEditor extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	JComboBox<String> jcData, jcAction;
	JButton jbXls, jbOk, jbCancel;
	JTextField jfXls;
	
	String dataType="";
	String actionType="";
	String actionFile="";
	
	String dataItems[] = {"Select Data Type", "Courses", "Regulations", "Departments", 
			"Branches", "Subjects", "Students", "Exams"};
	String actionItems[] = {"Select Action", "Add New", "Modify", "Delete", "Show All"};
	
	
	public void getData()
	{
		Container c = getContentPane();
		c.setLayout(new GridLayout(0,2));
		setSize(500, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		jcData = new JComboBox<>(dataItems);
		jcAction = new JComboBox<>(actionItems);
		
		jfXls = new JTextField();
		jbXls = new JButton("Choose XLS File");
		jbXls.setActionCommand("XLS");
		jbXls.addActionListener(this);;
		jbOk = new JButton("OK");
		jbOk.setActionCommand("OK");
		jbOk.addActionListener(this);
		jbCancel = new JButton("Cancel");
		jbCancel.setActionCommand("CANCEL");
		jbCancel.addActionListener(this);;
		
		c.setLayout(new BorderLayout());
		c.add(new JLabel("<b><u>Master Tables Editor Form</u></b>"), BorderLayout.NORTH);
		JPanel jp1 = new JPanel();
		jp1.setLayout(new GridLayout(0, 2));
		jp1.add(new JLabel("Data Type"));
		jp1.add(jcData, JPanel.LEFT_ALIGNMENT);
		c.add(jp1, BorderLayout.CENTER);
		setVisible(true);
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	}

	public static void main(String[] args)
	{
		MasterDataEditor mde = new MasterDataEditor();
		mde.getData();
		

	}
}
