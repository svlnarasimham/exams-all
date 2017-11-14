package expro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Users
{
	private Vector<User> uVector;
	
	public Users()
	{
		uVector = new Vector<User>();
	}

	public int loadUsers()
	{
		User tu=null;
		String fname = getUsersFileName(); //adds path to users.dat
		File ftest = new File(fname);
		if(!ftest.exists()) // Create a new file if doesn't exist
		{
			int res = JOptionPane.showConfirmDialog(null, 
						"Create New User File ?" + fname, 
						"Confirmation", 
						JOptionPane.YES_NO_OPTION);
			if(res != 0) // Yes=0, No=1
			{
				JOptionPane.showMessageDialog(null,"Terminating the Program");
			}
			createUserFile();
		}
		
		try
		{
			FileReader fr = new FileReader(new File(fname));
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			while((line=br.readLine()) != null)
			{
				if(line.trim().length() < 40) continue; // short/empty/comment lines

				tu = new User();
				boolean res = tu.loadFromEncryptedString(line);
				if(!res)
				{
					continue;
				}
				uVector.add(tu);
			}
			br.close();
			fr.close();
		}
		catch(Exception e)
		{
			System.out.println("Can not open Passwords File....");
			System.exit(1);
		}
		return uVector.size();
	}
	
	public String[] getUserIds()
	{
		String a[] = new String[uVector.size()];
		for(int i=0;i<uVector.size();i++)
		{
			a[i]=uVector.get(i).getUid();
		}
		return a;
	}
	
	// Returns user object only when name and password are ok
	public User checkUser(User u)
	{
		int i=0;
		User tu=null;
		int userCount = uVector.size();
		for(i=0;i<userCount;i++)
		{
			tu=uVector.get(i);
			if(tu.getUid().trim().toLowerCase().equals(u.getUid().trim().toLowerCase())&&
				tu.getUpwd().trim().toLowerCase().equals(u.getUpwd().trim().toLowerCase()))
			{
				break;
			}
		}
		if(i == userCount) // not found
		{
			tu=null;
		}
		return tu;
	}

	public boolean addUser(User u)
	{
		boolean res = false;
		if (!Globals.curUser.urole.equalsIgnoreCase("admin"))
		{
			JOptionPane.showMessageDialog(null,"Only Admin can do this");
		}
		else if (u != null)
		{
			User tu;
			tu = getUser(u.getUid());
			if (tu != null) 
			{
				JOptionPane.showMessageDialog(null, "User " + u.getUid()
						+ " already exists");
				return false;
			}

			uVector.add(u);
			saveUsers();
			res = true;
		}
		return res;
	}
	
	public boolean delUser(String uid)
	{
		boolean res=false;
		if (!Globals.curUser.urole.equals("admin"))
		{
			JOptionPane.showMessageDialog(null,"Only Admin can do this");
		}
		else
		{
			User tu;
			tu = getUser(uid);
			//System.out.println(tu);
			if(tu == null)
			{
				JOptionPane.showMessageDialog(null,"User " + uid + " doesn't exist");
				return false;
			}
			if(tu.getUid().equals("admin"))
			{
				JOptionPane.showMessageDialog(null, "Can not remove admin");
			}
			else
			{
				uVector.remove(tu);
				saveUsers();
			}
			res = true;
		}
		return res;
	}
	
	
	// Sets the attributes of user u and saves it.
	public boolean editUser(User u)
	{
		boolean res = false;
		User tu=null;
		if(Globals.curUser.equals(u) || Globals.curUser.urole.equals("admin"))
		{
			for(int i=0;i<uVector.size();i++)
			{
				tu = uVector.get(i);
				if(uVector.get(i).equals(u))
				{
					tu.setUname(u.getUname());
					tu.setUpwd(u.getUpwd());
					tu.setUrole(u.getUrole());
					tu.setDisabled(u.getDisabled());
					saveUsers();
					res = true;
					break;
				}
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Only Admin can do that");
		}
		return res;
	}

	public void saveUsers()
	{
		int i;
		User tu;
		String fname = getUsersFileName();
		try
		{
			FileWriter fw = new FileWriter(new File(fname));
			String line;
			for(i=0;i<uVector.size();i++)
			{
				tu = uVector.get(i);
				line = tu.toEncryptedString();
				fw.write(line+"\n");
			}
			fw.close();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error Writing File");
		}
	}

	private String getUsersFileName()
	{
		String fname = Globals.basePath + Globals.pathSep + Globals.configFolder + Globals.pathSep + Globals.usersFile;
		return fname;
	}
	
	// Returns the user object for the given user ID
	public User getUser(String uid)
	{
		int i=0;
		User tu=null;
		int userCount = uVector.size();
		for(i=0;i<userCount;i++)
		{
			if((uVector.get(i)).getUid().equalsIgnoreCase(uid))
			{
				tu = uVector.get(i);
			}
		}
		return tu;
	}
	
	public void createUserFile()
	{
		User tu;
		String x;
		String fname = getUsersFileName(); //adds path to users.dat
		File ftest = new File(fname);
		try
		{
			FileWriter fin = new FileWriter(ftest);
			
			// add user with UID, Name, PWD, Role, Disable=true/false)
			tu = new User("admin","Administrator","admin","admin","false");
			x = tu.toEncryptedString();
			fin.write(x+"\n");
			
			tu = new User("guest","Guest User","guest","guest","false");
			x = tu.toEncryptedString();
			fin.write(x+"\n");
			
			fin.close();
		}
		catch(Exception e)
		{ 
			JOptionPane.showMessageDialog(null, "Error: " +e.getMessage()); 
		}
	}
	
}
