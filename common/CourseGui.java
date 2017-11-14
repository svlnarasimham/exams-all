package common;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.table.DefaultTableModel;

public class CourseGui extends JFrame implements ActionListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	// Variables declaration - do not modify
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JScrollPane jScrollPane1;
	private JTable jTable1;
	private JTextField nameTxt;
	private JTextField abbrTxt;
	private JTextField codeTxt;
	private JButton cBtn;
	private JButton rBtn;
	private JButton uBtn;
	private JButton dBtn;
	private JButton clearBtn;
	// End of variables declaration

	public CourseGui()
	{
		initComponents();
	}

	private void retrieve()
	{
		DefaultTableModel dm = getData();
		jTable1.setModel(dm);
	}

	@SuppressWarnings("unchecked")
	private void initComponents()
	{
		Container c = getContentPane();
		c.setLayout(new GridLayout(0, 2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(500, 200));
		jPanel1 = new JPanel();
		jPanel2 = new JPanel();
		jScrollPane1 = new JScrollPane();
		jTable1 = new JTable();
		jLabel1 = new JLabel("Code");
		jLabel2 = new JLabel("Abbr");
		jLabel3 = new JLabel("Name");
		cBtn = new JButton("New");
		rBtn = new JButton("Retrieve");
		uBtn = new JButton("Modify");
		dBtn = new JButton("Delete");
		clearBtn = new JButton("Clear");
		codeTxt = new JTextField();
		abbrTxt = new JTextField();
		nameTxt = new JTextField();
		jTable1.setModel(new DefaultTableModel(new Object[][] {}, new String[] {}));
		jTable1.addMouseListener(this);

		jScrollPane1.setViewportView(jTable1);
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		rBtn.addActionListener(this);
		cBtn.addActionListener(this);
		uBtn.addActionListener(this);
		dBtn.addActionListener(this);
		clearBtn.addActionListener(this);

		jPanel2.setLayout(new GridLayout(0, 2));
		jPanel2.add(jLabel1);
		jPanel2.add(codeTxt);
		jPanel2.add(jLabel2);
		jPanel2.add(abbrTxt);
		jPanel2.add(jLabel3);
		jPanel2.add(nameTxt);
		jPanel2.add(cBtn);
		jPanel2.add(rBtn);
		jPanel2.add(uBtn);
		jPanel2.add(dBtn);
		jPanel2.add(clearBtn);
		jPanel2.add(new JLabel(" "));

		jPanel1.setLayout(new BorderLayout());
		jPanel1.add(jScrollPane1, BorderLayout.CENTER);
		c.add(jPanel1);
		c.add(jPanel2);

		setTitle("Course CRUD");
		pack();
		setVisible(true);
	}// </editor-fold>

	public DefaultTableModel getData()
	{
		// ADD COLUMNS TO TABLE MODEL
		DefaultTableModel dm = new DefaultTableModel();
		dm.addColumn("Code");
		dm.addColumn("Abbr");
		dm.addColumn("Name");

		CourseDao cs = new CourseDao();
		ArrayList<Course> al = cs.getAll();
		for (Course c : al)
		{
			dm.addRow(new String[] { c.ccode, c.cabbr, c.cname });
		}
		return dm;

	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource() == jTable1)
		{
			System.out.println("JTable Row Selected");
			String code = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
			String abbr = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
			String name = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString();
			nameTxt.setText(name);
			abbrTxt.setText(abbr);
			codeTxt.setText(code);
		}
		else
		{
			System.out.println("JTable Row not selected");
		}
	}

	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		CourseDao cs = new CourseDao();
		if (src == cBtn)
		{
			int res = cs.add(new Course(codeTxt.getText(), abbrTxt.getText(), nameTxt.getText()));
			if (res == 1)
			{
				retrieve();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Not Saved");
			}
		}
		else if (src == uBtn)
		{
			int res =
					cs.update(new Course(codeTxt.getText(), abbrTxt.getText(), nameTxt.getText()));
			if (res == 1)
			{
				JOptionPane.showMessageDialog(null, "Successfully Updated");
				// CLEAR TXT
				nameTxt.setText("");
				abbrTxt.setText("");
				codeTxt.setText("");
				retrieve();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Not Updated");
			}

		}
		else if (src == rBtn)
		{
			retrieve();
		}
		else if (src == dBtn)
		{
			String[] options = { "Yes", "No" };
			int answ = JOptionPane.showOptionDialog(null, "Sure To Delete??", "Delete Confirm",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
					options[1]);
			if (answ == 0)
			{
				int res = cs.delete(
						new Course(codeTxt.getText(), abbrTxt.getText(), nameTxt.getText()));
				if (res == 1)
				{
					JOptionPane.showMessageDialog(null, "Deleted successfully");
					// CLEAR TXT
					nameTxt.setText("");
					abbrTxt.setText("");
					codeTxt.setText("");
					retrieve();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Not Deleted");
				}
			}
		}
		else if (src == clearBtn)
		{
			jTable1.setModel(new DefaultTableModel());
		}
	}

	public static void main(String[] args)
	{
		G.initialize();
		new CourseGui();
	}
}