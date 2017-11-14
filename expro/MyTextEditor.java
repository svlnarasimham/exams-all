package expro;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MyTextEditor extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	String fnm;
	//boolean changed=false;
	String data="";
	JTextArea jta= null;
	boolean editable;
	boolean logfile=false;
	
	public MyTextEditor(JFrame parent, String title, String filename)
	{
		super(parent, title, true);
		this.editable = true;
		if(title.equalsIgnoreCase("view log"))
		{
			logfile = true;
		}
		doTextEditor(parent, title,filename);
	}
	public MyTextEditor(JFrame parent,String title, String filename, boolean editable)
	{
		super(parent, title, true);
		this.editable = editable;
		if(title.equalsIgnoreCase("view log"))
		{
			logfile = true;
		}
		doTextEditor(parent, title, filename);
	}
	public void doTextEditor(JFrame parent,String title, String filename)
	{	
		fnm = filename;
		setSize(700,400);
		setLocation(100,100);
		//setVisible(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JButton jbOk, jbCancel;
		jbOk = new JButton("Save");
		jbOk.setActionCommand("Save");
		jbOk.addActionListener(this);
		jbOk.setEnabled(editable);
		
		jbCancel = new JButton("Cancel");
		jbCancel.setActionCommand("Cancel");
		jbCancel.addActionListener(this);
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		jta = new JTextArea(20,80);
		JScrollPane jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel jp1; 

		jp1 = new JPanel();
		jp1.setLayout(new FlowLayout());
		jp1.add(jbOk);
		jp1.add(jbCancel);
		
		//c.add(jp1, BorderLayout.CENTER);
		c.add(jsp, BorderLayout.CENTER);
		c.add(jp1, BorderLayout.SOUTH);

		
		try
		{
			//int res;
			//char buf[] = new char[1024];
			String s;
			
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			while((s = br.readLine()) != null)
			{
				if(logfile)
				{
					data += (utility.Utilities.decrypt(s.trim()) + "\n");
				}
				else
				{
					data += (s.trim()+"\n");
				}
			}
			br.close();
			fr.close();
			jta.setText(data);
			//jta.setText("ABCD EFGH");
			jta.setFont(new Font("Courier",Font.PLAIN,14));
			jta.setForeground(Color.blue);
			jta.setCaretPosition(0);
			jsp.getVerticalScrollBar().setValue(0);
			jsp.getVerticalScrollBar().repaint();
			jsp.repaint();
			jsp.revalidate();
			//jta.repaint();
			jta.setEditable(editable);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		String cmd = ae.getActionCommand();
		boolean flag = false;
		if(cmd.equals("Save"))
		{
			flag = true;
			if(editable)
			{
				try
				{
					FileWriter fw = new FileWriter(new File(fnm));
					fw.write(jta.getText());
					fw.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(this, "File Saved");
			}
		}
		else if (cmd.equals("Cancel"))
		{
			flag = true;
			JOptionPane.showMessageDialog(this, "Operation Aborted");
		}
		if(flag) dispose(); // Dispose only when Cancel or Save are pressed..
	}

	/************
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		new MyTextEditor(null,"Editor","/home/svl/workspace/rgm/subjects.dat");
	}
	**************/
}
