package common;

import java.util.ArrayList;
import java.util.List;

public class Regulation 
{
	String q = "\"";
	String ccode;
	String rcode;

	public Regulation()
	{
		ccode="";
		rcode = "";
	}
	public Regulation(String ccode, String regulation)
	{
		this.ccode = ccode;
		this.rcode = regulation;
	}
	public String getCcode() {
		return ccode;
	}
	
	public void setCcode(String ccode) {
		this.ccode = ccode;
	}
	
	public String getRcode() {
		return rcode;
	}
	
	public void setRcode(String regulation) {
		this.rcode = regulation;
	}

	public String toJSON()
	{
		return "{" + q+"ccode"+q + ":"+ q+ccode+q + ","+ q+"rcode"+q + ":" +q+rcode+q +"}";
	}
	
	public static String toJSON(List<Regulation> regl)
	{
		Regulation ra[] = new Regulation[regl.size()];
		regl.toArray(ra);
		return toJSON(ra);
	}
	
	public static String toJSON(Regulation regl[])
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean first = true;
		for(Regulation r: regl)
		{
			if(!first)
			{
				sb.append(",");
			}
			first=false;
			sb.append(r.toJSON());
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Regulation r1 = new Regulation("A", "15");
		Regulation r2 = new Regulation("A", "15");
		Regulation arr[] = {r1,r2};
		ArrayList<Regulation> al = new ArrayList<>();
		al.add(r1); al.add(r2);
		System.out.println(Regulation.toJSON(al));
	}

}
