package expro;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
//import java.io.*;

@SuppressWarnings("serial")
public class MyLogin extends JDialog implements ActionListener
{	
	JLabel jlbName, jlbPwd;
	JTextField jtfName;
	JPasswordField jtfPwd;
	JButton jbLogin;
	JPanel jp1,jp2;

	public MyLogin(JFrame j)
	{
		super(j, "Login page", true);
		
	}
	public void doLogin()
	{

		jlbName=new JLabel("Name : ");
		jtfName=new JTextField("",10);
		jlbPwd=new JLabel("Password : ");
		jtfPwd=new JPasswordField("",10);
		jbLogin=new JButton("login");
		Container c=getContentPane();

		c.setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jp1=new JPanel();
		jp1.setLayout(new GridLayout(2,2));
		jp2=new JPanel();
		jp2.setLayout(new FlowLayout());
		jp1.add(jlbName);
		jp1.add(jtfName);
		jp1.add(jlbPwd);
		jp1.add(jtfPwd);
		//tpass.setEchoChar('*');
		jp2.add(jbLogin);
		c.add(jp1);
		c.add(jp2);
		jbLogin.addActionListener(this);
		///// Create window and center it to the screen
		int winw=300;
		int winh=200;
		this.setSize(winw,winh);
		this.setLocation(
				(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-winw)/2, 
				(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-winh)/2);
		this.setVisible(true);
	}
	

	public void actionPerformed(ActionEvent ae)
	{
		Users us=new Users();
		us.loadUsers();
		String msg = "";
		User tu; // Temp user variable
		tu=us.getUser(jtfName.getText()); // get the user object
		if(tu!=null)
		{
			if (tu.getDisabled().equalsIgnoreCase("true"))
			{
				Globals.curUser = null;
				JOptionPane.showMessageDialog(this, "The User account disabled\n Contact Administrator...");
			}
			else if (tu.getUpwd().equals(new String(jtfPwd.getPassword()))) 
			{
		    	 Log l=Globals.log;
		    	 msg=l.createMsg(tu.getUrole(), tu.getUid(), " user logged in ", "login");
		    	 l.writeLog(msg);
		    	 Globals.curUser=tu;
		    	 dispose();
		    	 //MainMenu s=new MainMenu(g);
			}
			else
			{
				Globals.curUser = null;
				jtfName.setText("");
				jtfPwd.setText("");
				JOptionPane.showMessageDialog(this, "The Username or password incorrect...\n Try again...");
			}
		}
		else
		{
			Globals.curUser = null;
			jtfName.setText("");
			jtfPwd.setText("");
			JOptionPane.showMessageDialog(this, "The Username or password incorrect...\n Try again...");
		}
		return;
	}
}
/**********
public class Login
{
	public static void main(String args[])
	{
			MyLogin l=new MyLogin(new Globals("config.dat")); 
	}
}
*****************/