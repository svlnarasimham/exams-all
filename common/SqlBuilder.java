package common;

import java.util.HashMap;
import java.util.Set;

public class SqlBuilder
{
	static String SPACE = " ";
	
	private static String getPreparedString(String base, String argsAndTypes[][])
	{
		if(argsAndTypes == null || argsAndTypes.length == 0)
		{
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int qc = 0; // question mark count;
		for(int i=0; i<base.length();i++)
		{
			int ch = base.charAt(i);
			if(ch != '?')
			{
				sb.append((char)ch);
			}
			else
			{
				boolean intval = false;
				if(argsAndTypes[qc][1].toLowerCase().startsWith("n"))
				{
					intval = true;
				}
				if(!intval) 
				{
					sb.append("'");
				}
				sb.append(argsAndTypes[qc][0]);
				if(!intval) 
				{
					sb.append("'");
				}
				qc++;
			}
		}
		return sb.toString();
	}
	
	private static String getCSArray(String arr[])
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< arr.length; i++)
		{
			if(i>0) {sb.append(",");}
			sb.append(arr[i]);
		}
		return sb.toString();

	}
	/**
	 * Builds SQL with the given parameters
	 * @param tableName Table Name to be used (Ex: vnrexams.courses)
	 * @param fields Fields to be retrieved as an array (Ex: new String[]{"ccode", "cname"} or new String[]{"ccode"})
	 * @param whereClause Prepared Statement like where clause (Ex: "where ccode = ? and cname like ?")
	 * @param whereArgsAndTypes Array of values for the ? marks as strings and their data types<br>
	 * (Ex: new String[][]{{"A","s"}, {"B%", "s"}})<br>
	 * where Type "n" = numeric (no quotes) or "s" = string (quotes required)  
	 * @param orderBy Array of names of fields on which data should be ordered
	 * @return Generated query
	 */
	public static String buildSelectSql(
			String tableName,
			String fields[],
			String whereClause,
			String whereArgsAndTypes[][],
			String orderBy[]
			)
	{
		StringBuilder sb = new StringBuilder();
		
		// Select statement
		sb.append("select");
		sb.append(SPACE);
		if(fields==null)
		{
			return "";
		}
		// Fields list with comma separator
		sb.append(getCSArray(fields));
		sb.append(SPACE);
		// Table Name
		sb.append("from");
		sb.append(SPACE);
		sb.append(tableName);
		sb.append(SPACE);
		
		// Where clause
		if(whereClause!=null && !whereClause.equals(""))
		{
			sb.append("where");
			sb.append(SPACE);
			sb.append(getPreparedString(whereClause, whereArgsAndTypes));
			sb.append(SPACE);
		}
		if(orderBy!=null && orderBy.length > 0)
		{
			sb.append("order by");
			sb.append(SPACE);
			sb.append(getCSArray(orderBy));
			sb.append(SPACE);
			
		}
		return sb.toString();
	}
	
	/**
	 * Inserts data into a table
	 * @param tableName Table Name (Ex: vnrexams.marks)
	 * @param data Hashmap with field name as key, and value and data type as value array<br>
	 * where "n" - numeric which doesn't require quotes or "s" to quote the values
	 * @return SQL String
	 */
	public static String buildInsertSql(String tableName, HashMap<String, String[]> data)
	{
		StringBuilder sb = new StringBuilder();
		
		// Insert into statement
		sb.append("insert into");
		sb.append(SPACE);
		// Table name
		sb.append(tableName);
		sb.append(SPACE);
		
		// Fields list
		if(data == null || data.size() == 0)
		{
			return "";
		}
		String keys[] = new String[data.size()];
		StringBuilder tsb = new StringBuilder();
		tsb.append("(");
		Set<String> hs = data.keySet();
		int i = 0;
		for(String s:hs)
		{
			if(i>0)
			{
				tsb.append(",");
			}
			tsb.append(s);
			keys[i] = s;
			i++;
		}
		tsb.append(")");
		sb.append(tsb);
		sb.append(SPACE);
		
		sb.append("values");
		sb.append(SPACE);
		sb.append("(");
		for(i=0;i<keys.length; i++)
		{
			String arr[] = data.get(keys[i]);
			boolean intdata = false;
			if(arr[1].toLowerCase().startsWith("n"))
			{
				intdata = true;
			}
			if(i>0)
			{
				sb.append(",");
			}
			if(!intdata) sb.append("'");
			sb.append(arr[0]);
			if(!intdata) sb.append("'");
		}
		sb.append(")");
		
		return sb.toString();
	}
	
	public static String buildUpdateSql(String tableName, 
			HashMap<String, String[]> data,
			String whereClause,
			String whereArgsAndTypes[][])
	{
		StringBuilder sb = new StringBuilder();
		
		// update statement
		sb.append("update");
		sb.append(SPACE);
		// Table name
		sb.append(tableName);
		sb.append(SPACE);
		sb.append("set");
		
		// Fields list
		if(data == null || data.size() == 0)
		{
			return "";
		}
		sb.append(SPACE);
		Set<String> hs = data.keySet();
		int i = 0;
		for(String s:hs)
		{
			if(i>0)
			{
				sb.append(",");
			}
			sb.append(s); // Key
			sb.append("=");
			String arr[] = data.get(s);
			boolean intdata = false;
			if(arr[1].toLowerCase().startsWith("n"))
			{
				intdata = true;
			}
			if(!intdata) sb.append("'");
			sb.append(arr[0]);
			if(!intdata) sb.append("'");
			i++;
		}
		// Where clause
		if(whereClause.length() > 0)
		{
			sb.append(SPACE);
			sb.append("where");
			sb.append(SPACE);
		
			sb.append(getPreparedString(whereClause, whereArgsAndTypes));
			sb.append(SPACE);
		}
		return sb.toString();
	}
	
	public static String buildDeleteSql(String tableName, String whereClause)
	{
		StringBuilder sb = new StringBuilder();
		
		return sb.toString();
	}
	
	
	
	
	
	public static void main(String[] args)
	{
		String fields[] = {"age", "name", "number"};
		String table = "vnrmasters.courses";
		String whereClause = "(age = ? and name = ?) or number = ?";
		String whereArgsAndTypes[][] = {{"10","n"}, {"svl", "s"},{"20","n"}};
		String orderBy[] = {"name","age","number"};
		HashMap<String, String[]> hm = new HashMap<>();
		for(int i=0;i<fields.length; i++)
		{
			hm.put(fields[i], whereArgsAndTypes[i]);
		}
		String x = buildSelectSql(table, fields, whereClause, whereArgsAndTypes, orderBy); 
		System.out.println(x);
		String y = buildInsertSql(table, hm);
		System.out.println(y);
		String z = buildUpdateSql(table, hm, whereClause, whereArgsAndTypes);
		System.out.println(z);
	}
}
