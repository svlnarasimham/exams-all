package expro;

public class User
{
	public String uid;   // Unique ID
	public String uname; // User Name
	public String upwd;  // Password
	public String urole; // admin, staff ,user guest
	public String udisabled;  // "true", "false"
	
	public User()
	{
		this.uid       = ""; 
		this.uname     = "";
		this.upwd      = "";
		this.urole     = "";
		this.udisabled = "";
	}
	public User(String uid, String upwd)
	{
		this.uid       = uid.toLowerCase(); 
		this.uname     = "";
		this.upwd      = upwd.toLowerCase();
		this.urole     = "";
		this.udisabled = "";
	}
	public User(String uid,String uname, String upwd, String urole, String disabled)
	{
		this.uid       = uid.toLowerCase(); 
		this.uname     = uname.toLowerCase();
		this.upwd      = upwd.toLowerCase();
		this.urole     = urole.toLowerCase();
		this.udisabled = disabled.toLowerCase();
	}

	public String getUid()
   {
   	return uid;
   }

	public void setUid(String uid)
   {
   	this.uid = uid.toLowerCase();
   }

	public String getUname()
   {
   	return uname;
   }

	public void setUname(String uname)
   {
   	this.uname = uname.toLowerCase();
   }

	public String getUpwd()
   {
   	return upwd;
   }

	public void setUpwd(String upwd)
   {
   	this.upwd = upwd.toLowerCase();
   }

	public String getUrole()
   {
   	return urole;
   }

	public void setUrole(String urole)
   {
   	this.urole = urole.toLowerCase();
   }

	public String getDisabled()
   {
   	return udisabled;
   }

	public void setDisabled(String disabled)
   {
   	this.udisabled = disabled.toLowerCase();
   }
	
	public String toString()
	{
		String line = uid.toLowerCase()+":" +
							uname.toLowerCase()+":" +
							upwd.toLowerCase() + ":" +
							urole.toLowerCase() + ":" +
							udisabled.toLowerCase();
		return line;
	}
	
	public boolean equals (Object u)
	{
		User uu = (User)u;
		if(uid.trim().equalsIgnoreCase(uu.getUid().trim()))
		{
			return true;
		}
		return false;
	}
	
	// Parses an encrypted string loads the details
	public boolean loadFromEncryptedString(String enStr)
	{
		boolean res=false;
		String ts = utility.Utilities.decrypt(enStr);
		String ta[]= ts.split(":");
		if(ta.length == 5)
		{
			res = true;
			this.uid=ta[0];
			this.uname=ta[1];
			this.upwd=ta[2];
			this.urole=ta[3];
			this.udisabled=ta[4];
		}
		return res;
	}
	
	public String toEncryptedString()
	{
		String res; 
		res = uid+":"+uname+":"+upwd+":"+urole+":"+udisabled;
		res = utility.Utilities.encrypt(res);
		return res;
	}
}
