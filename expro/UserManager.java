package expro;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class UserManager extends JDialog implements ActionListener {
	
	
	private static final long serialVersionUID = 1L;
	public static final int NEW_USER=0;
	public static final int DELETE_USER=1;
	public static final int EDIT_USER=2;
	
	public boolean editFlag = false;
	JComboBox<String> jcuid;
	JTextField tuname;
	JPasswordField tupwd;
	JComboBox<String> jcrole;
	JCheckBox jcbdisabled;
	String aRoles[] = {"guest", "staff", "admin"};
	public User tu = null;
	int mode=0;
	
	public UserManager(JFrame parent, int entryMode)
	{
		super(parent, true);
		this.mode = entryMode;
		
		if(mode == UserManager.NEW_USER)
		{
			this.setTitle("New User Entry");
		}
		else if (mode == UserManager.DELETE_USER)
		{
			this.setTitle("Delete User");
		}
		else
		{
			this.setTitle("Modify User Details");
		}
		Container container =this.getContentPane();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		String uids[] = Globals.users.getUserIds();
		jcuid = new JComboBox<String>();

		
		if(mode == UserManager.NEW_USER)
		{
			jcuid.setEditable(true);
		}
		else // Load only editable uids for that user
		{
			for(int i=0;i<uids.length;i++)
			{
				if((Globals.curUser.getUrole().equals("admin")|| Globals.curUser.getUid().equals(uids[i])))
				{
					jcuid.addItem(uids[i]);
				}
			}
		}
		jcuid.setActionCommand("Uid");
		jcuid.addActionListener(this);

		tuname = new JTextField("");
		tupwd = new JPasswordField("");
		jcrole = new JComboBox<String>(aRoles);
		jcbdisabled = new JCheckBox();
		
		if(mode != UserManager.NEW_USER) // load details
		{
			loadDetails((String)jcuid.getSelectedItem());
		}
		
		JLabel luid = new JLabel("User id : ");
		JLabel luname = new JLabel("User name : ");
		JLabel lupwd = new JLabel("Password : ");
		JLabel lrole = new JLabel("role : ");
		JLabel ldisabled=new JLabel("Disable");

		JPanel jp1,jp2;
		JButton jbOk = new JButton("Ok");
		jbOk.setActionCommand("Ok");
		jbOk.addActionListener(this);

		JButton jbCancel = new JButton("Cancel");
		jbCancel.setActionCommand("Cancel");
		jbCancel.addActionListener(this);

		container.setLayout(new GridLayout(2,1));
		jp1=new JPanel();
		jp2=new JPanel();
		jp1.setLayout(new GridLayout(6,2));
		jp2.setLayout(new FlowLayout());
		jp1.add(luid);	    	jp1.add(jcuid);
		jp1.add(luname);		jp1.add(tuname);
		jp1.add(lupwd);     	jp1.add(tupwd);
		jp1.add(lrole);		jp1.add(jcrole);
		jp1.add(ldisabled); 	jp1.add(jcbdisabled); 
		
		container.add(jp1);
		
		//add.setSize(250, 250);
		jp2.add(jbOk);
		jp2.add(jbCancel);
		
		container.add(jp1);
		container.add(jp2);
		
		int w = 350;
		int h = 300;
		setSize(w,h);
		setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().width/2-w,
				(int)Toolkit.getDefaultToolkit().getScreenSize().height/2-h);
		setVisible(true);

	}
	
	public void loadDetails(String uid)
	{
		User tu = Globals.users.getUser(uid);
		if(tu != null)
		{
			//System.out.println("User: "+ Globals.curUser.getUid() + " Role: "+ Globals.curUser.getUrole());
			//System.out.println("chage User: "+ tu.getUid() + " Role: "+ tu.getUrole());
			tuname.setText(tu.getUname()); // Fill name field
			tupwd.setText(tu.getUpwd());   // Fill password field
			String tmps = tu.getUrole();   // fill role 
			if(tmps.equals("admin"))
			{
				jcrole.setSelectedItem("admin");
			}
			else if(tmps.equals("staff"))
			{
				jcrole.setSelectedItem("staff");
			}
			else
			{
				jcrole.setSelectedItem("guest");
			}
			tmps = tu.getDisabled();   // Filled Disabled field
			if(tmps.equals("true"))
			{
				jcbdisabled.setSelected(true);
			}
			else
			{
				jcbdisabled.setSelected(false);
			}
			
			//////////////// Check various conditions for modification/deletion
			
			if(uid.equals("admin")) // For admin user...
			{
				jcbdisabled.setEnabled(false); // can not disable admin
				jcrole.setEnabled(false);  // can not change role
				tuname.setEnabled(false);  // can not change user name
				tupwd.setEnabled(true);    // can change password
			}
			else // Other User
			{
				jcbdisabled.setEnabled(true); // can disable 
				jcrole.setEnabled(true);  // can change role 
				tuname.setEnabled(true);  // can change name
				tupwd.setEnabled(true);    // can change password
			}
			
			if(!Globals.curUser.getUrole().equals("admin")) // Not admin
			{
				jcbdisabled.setEnabled(false); // can not disable 
				jcrole.setEnabled(false);  // can not change role 
			}
			
			if(mode == UserManager.DELETE_USER)
			{
				// Only uid can be selected ... Others are unnecessary
				jcuid.setEditable(false);
				jcbdisabled.setEnabled(false);
				jcrole.setEnabled(false);
				tuname.setEnabled(false);
				tupwd.setEnabled(false);
			}
			else if(mode == UserManager.EDIT_USER)
			{
				jcuid.setEditable(false);
			}
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		String cmd = ae.getActionCommand();
		
		if(cmd.equals("Uid")) // User ID changed 
		{
			if(mode != UserManager.NEW_USER)
			{
				loadDetails((String)jcuid.getSelectedItem());
			}
		}
		else if(cmd.equals("Ok"))
		{
			String x1=(String)jcuid.getSelectedItem();
			String x2=tuname.getText().trim().toLowerCase();
			String x3=(new String(tupwd.getPassword())).trim().toLowerCase();
			String x4=aRoles[jcrole.getSelectedIndex()];
			String x6 = (jcbdisabled.isSelected())?"true" : "false";
			if(x1.equals("")||x2.equals("")||x3.equals("")||x4.equals("")||x6.equals(""))
			{
				JOptionPane.showMessageDialog(this,"Please enter all perticulars ");
			}
			else
			{
				boolean result=false;
				tu = new User(x1,x2,x3,x4,x6);
				//System.out.println("Mode: "+ mode + "  User: "+ tu.toString());
				switch(mode)
				{
					case UserManager.NEW_USER: 
						result=Globals.users.addUser(tu);
						if(!result)
						{
							JOptionPane.showMessageDialog(this, "Error in adding User");
						}
						break;
					case UserManager.DELETE_USER:
						result=Globals.users.delUser(tu.getUid());
						if(!result)
						{
							JOptionPane.showMessageDialog(this, "Error in deleting User");
						}
						break;
					case UserManager.EDIT_USER:
						result=Globals.users.editUser(tu);
						if(!result)
						{
							JOptionPane.showMessageDialog(this, "Error in editing User");
						}
						break;
					default:JOptionPane.showMessageDialog(this, "Wrong operation"); break;
				}
				//JOptionPane.showMessageDialog(this,	"Mode: "+mode+" Result: "+ result);
				this.dispose();
			}
		}
		else if (cmd.equals("Cancel"))
		{
			tu=null;
			this.dispose();
		}
	}
	
	/*********************************************
	public static void main(String args[])
	{
		
//		JFrame j = new JFrame("Test");
//		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		j.setSize(500,500);
//		j.setLocation(100,100);
//		j.setVisible(false);
		Globals g = new Globals("./config.dat");
		Globals.users = new Users(g);
		Globals.users.loadUsers();
		
		User tu = new User();
		tu.setUid("admin");
		tu.setUname("admin");
		tu.setUpwd("admin");
		tu.setDisabled("false");
		tu.setUrole("admin");
		Globals.curUser = tu;
		
		//UserEntry ue = new UserEntry(j,UserEntry.NEW_USER, g);
//		UserEntry ue = new UserEntry(j,UserEntry.DELETE_USER, g);
		//UserEntry ue = new UserEntry(j,UserEntry.EDIT_USER, g);
		
//		j.setVisible(true);
//		Container c = j.getContentPane();
//		c.setLayout(new FlowLayout());
//		c.add(new JLabel("Value: "));
//		if(ue.tu != null)
//			c.add(new JLabel(ue.tu.toString()));
//		else
//			c.add(new JLabel("null"));
//		
		printUsers(Globals.users);
		
	}
	***************************************/
	
	/// Debug
	public static void printUsers(Users users)
	{
		String x[] = users.getUserIds();
		for(int i=0;i<x.length;i++)
		{
			User tu = users.getUser(x[i]);
			System.out.println(tu);
		}
	}
	

}

