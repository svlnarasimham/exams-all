package common;

public class Branch implements Comparable<Branch>
{
	public String ccode; // Course Code
	public String bcode; // branch code
	public String babbr; // branch short name
	public String bname; // branch full name
	public String dcode; // Faculty (department)
	/**
	 * Default constructor which initializes all fields to blanks.
	 */
	public Branch()
	{
		this("","","","","");
	}
	
	/**
	 * Constructor
	 * @param courseCode Course Code ("A","B" etc.)
	 * @param branchCode Branch Code (1,2,3, etc.)
	 * @param shortName Short Branch Name (MECH, R&AC, PS etc.)
	 * @param name Branch Full Name
	 */
	public Branch(String courseCode, int branchCode, String shortName, String name)
	{
		this(courseCode,String.format("%02d", branchCode),shortName,name,"");
	}

	/**
	 * Constructor
	 * @param courseCode Course Code ("A","B" etc.)
	 * @param branchCode Branch Code (1,2,3, etc.)
	 * @param shortName Short Branch Name (MECH, R&AC, PS etc.)
	 * @param name Branch Full Name
	 */
	public Branch(String courseCode, String branchCode, String shortName, String name)
	{
		this(courseCode,branchCode,shortName,name,"");
	}
	
	/**
	 * Constructor
	 * @param courseCode Course Code ("A","B" etc.)
	 * @param branchCode Branch Code (1,2,3, etc.)
	 * @param shortName Short Branch Name (MECH, R&AC, PS etc.)
	 * @param name Branch Full Name
	 * @param faculty Faculty under which this is offered (Civil, Mechanical etc.)
	 * @param pcString PC String that contains variables to be printed in PC.
	 */
	public Branch(
			String courseCode, 
			int branchCode, 
			String shortName, 
			String name, 
			String dcode)
	{
		this(courseCode,String.format("%02s",branchCode),shortName,name,dcode);
	}

	/**
	 * Constructor
	 * @param courseCode Course Code ("A","B" etc.)
	 * @param branchCode Branch Code ("01","02","03", etc.)
	 * @param shortName Short Branch Name (MECH, R&AC, PS etc.)
	 * @param name Branch Full Name
	 * @param decod Faculty under which this is offered (0 for B.Tech. or Branch Code for M.Tech.)
	 */
	public Branch(
			String courseCode, 
			String branchCode, 
			String shortName, 
			String name, 
			String dcode)
	{
		this.ccode = courseCode;
		this.bcode = branchCode;
		this.babbr = shortName;
		this.bname  = name;
		this.dcode = dcode;
	}
	
	/**
	 * Converts the branch as a string with tab separated fields
	 */
	public String toString()
	{
		String sep = "\t";
		return ccode+sep+bcode+sep+babbr+sep+bname;
	}
	
	public String toJSON()
	{
		String q = "\"";
		StringBuffer sb = new StringBuffer();
		sb.append("{")
			.append(q).append("ccode").append(q).append(":").append(q).append(ccode).append(q).append(",")
			.append(q).append("bcode").append(q).append(":").append(q).append(bcode).append(q).append(",")
			.append(q).append("babbr").append(q).append(":").append(q).append(babbr).append(q).append(",")
			.append(q).append("name") .append(q).append(":").append(q).append(bname) .append(q).append(",")
			.append(q).append("dcode").append(q).append(":").append(q).append(dcode).append(q)
			.append("}"); 
		return sb.toString();
	}
	
	public int compareTo(Branch o)
	{
		return (ccode+bcode).compareTo(o.ccode+o.bcode);
	}
	
	public static void main(String[] args)
	{
		Branch b = new Branch("A", "01", "CE", "Civil Engineering", "00");
		System.out.println(b.toJSON());
	}
}
