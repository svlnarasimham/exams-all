package common;
import javax.swing.*;

import java.sql.ResultSetMetaData;

import java.sql.DatabaseMetaData;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
public class Test extends JFrame
{
    public static void main(String args[]) throws Exception
    {
    	String   catalog           = null;
    	String   schemaPattern     = null;
    	String   tableNamePattern  = "courses";
    	String   columnNamePattern = null;
    	G.initialize();
    	Connection con = G.dbcon.getConnection();
    	DatabaseMetaData dbmd = con.getMetaData();
    	ResultSet result =  dbmd.getColumns(catalog, schemaPattern,  tableNamePattern, columnNamePattern);
    	while(result.next()){
    		System.out.println(result.getString(4) + "--" + result.getString(6) + ".." + result.getString(16));
    		//int n = result.getMetaData().getColumnCount();
    		//for(int i=1;i<n;i++)
    			//System.out.print(result.getString(i) + ",");
    		//System.out.println();
    	}    	
    }
}