package common;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;


public class DbTableViewer extends JDialog
{
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane1;
	private JTable jTable1;

	public DbTableViewer(JFrame parent, String dbName, String tableName)
	{
		super(parent, "Table Viewer", true);
		initComponents(dbName, tableName);
	}

	public void retrieve(String dbName, String tableName)
	{
		DefaultTableModel dm = getData(dbName, tableName);
		jTable1.setModel(dm);
	}

	private void initComponents(String dbName, String tableName)
	{
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(new Dimension(500, 200));
		
		jScrollPane1 = new JScrollPane();
		jTable1 = new JTable();
		jTable1.setModel(new DefaultTableModel(new Object[][] {}, new String[] {}));

		jScrollPane1.setViewportView(jTable1);
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		c.add(jScrollPane1, BorderLayout.CENTER);
		setTitle("Table Viewer");
		pack();
		retrieve(dbName, tableName);
		setVisible(true);
	}// </editor-fold>

	public DefaultTableModel getData(String dbName, String tableName)
	{
		DefaultTableModel dm = new DefaultTableModel();
		
		ArrayList<String[]>alm = G.dbcon.getTableMetaData(dbName, tableName);
		if(alm.size() == 0)
		{
			// no table metadata found
			dm.addColumn("");
			dm.addColumn("");
			dm.addColumn("");
			
		}
		else
		{
			for(int i=0; i<alm.size(); i++)
			{
				String arr[] = alm.get(i);
				dm.addColumn(arr[0]+"("+arr[1].charAt(0)+")");
			}
		}
		
		String sql = "select * from " + dbName + "." + tableName + ";";
		ArrayList<String[]> al = G.dbcon.executeSQL(sql);

		if(al.size() > 0)
		{
			for (String[] c : al)
			{
				dm.addRow(c);
			}
			
		}
			return dm;

	}

	public static void main(String[] args)
	{
		G.initialize();
		
		DbTableViewer dtv = new DbTableViewer(null, "vnrmasters", "branches");
		if(!dtv.isVisible())
		{
			System.out.println("Closing db...");
			G.closeAll();
		}
		
	}
}