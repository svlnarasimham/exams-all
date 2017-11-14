package common;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class EcodeGetterDialog extends JDialog implements ActionListener
{
	private static final long	serialVersionUID	= 1L;
	String ecode="";
	JComboBox<String> jcbEcode;
	JTextField jtfEcode;
	JButton jbcancel, jbok;
	public EcodeGetterDialog()
	{
		super(G.jfParent, "Input Details for HT Generation", true);
		int ww = 400;
		int wh = 200;
		int sw = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		setSize(ww, wh);
		setLocation((sw-ww)/2, 100); 
		jcbEcode = new JComboBox<String>();
		jcbEcode.addActionListener(this);
		jcbEcode.setPreferredSize(new Dimension(150,50));
		populateComboBox(jcbEcode, "select ecode from exams where eprocessed='N';");
		jcbEcode.setEditable(true);

		
		jtfEcode = new JTextField("");
		
		jbok=new JButton("OK");
		jbok.addActionListener(this);
		jbok.setActionCommand("OK");
		
		jbcancel=new JButton("Cancel");
		jbcancel.addActionListener(this);
		jbcancel.setActionCommand("CANCEL");
		
		Container c = getContentPane();
		c.setLayout(new GridLayout(3, 2));
		// 1st row
		c.add(new JLabel("Exam Code", JLabel.RIGHT));
		c.add(jcbEcode);
		// 2nd row
		c.add(new JLabel("ECode", JLabel.RIGHT));
		c.add(jtfEcode);
		
		// 3rd row
		c.add(jbcancel);
		c.add(jbok);
	}
	
	public String getECode()
	{
		setVisible(true);
		return ecode;
	}
	
	public void populateComboBox(JComboBox<String> jcb, String sql)
	{
		ActionListener ax[] = jcb.getActionListeners();
		for(int i=0;i<ax.length; i++)
		{
			jcb.removeActionListener(ax[i]);
		}
		jcb.removeAllItems();
		jcbEcode.addItem("---Select Exam Code---");
		ArrayList<String[]> vs = G.dbcon.executeSQL(sql);
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
			dispose();
		}
		else if(ac.equals("CANCEL"))
		{
			ecode = "";
			dispose();
		}
		else
		{
			String x = (String)jcbEcode.getSelectedItem();
			jtfEcode.setText(x.startsWith("-")?"":x);
		}
	}
	
	public static void main(String[] args)
	{
		G.initialize();
		String x = new EcodeGetterDialog().getECode();
		System.out.println("ECODE :" +x);
	}
}
