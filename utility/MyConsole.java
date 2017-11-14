package utility;

import java.awt.*;
import javax.swing.*;

public class MyConsole extends JPanel
{
	private static final long serialVersionUID = 1L;
	public JTextArea jta;
	int width, height;
	int curLen = 0;
	String text;
	/**
	 * Constructor
	 * @param width Width of window
	 * @param height Height of window
	 */
	public MyConsole(int width, int height)
	{
		jta = new JTextArea();
		this.width = width;
		this.height = height;
		this.text = "";
		setPreferredSize(new Dimension(width, height));
		JScrollPane jsp = new JScrollPane(jta, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setLayout(new BorderLayout());
		add(jsp,BorderLayout.CENTER);
	}
	
	public boolean println(String text)
	{
		boolean res = false;
		this.text = text;
		res = this.println();
		return res;
	}

	/**
	 * Append a line of text to the console object
	 * @param text Text to be printed
	 */
	public boolean println()
	{
		boolean res = false;
		jta.append(text+"\n");
		curLen += (text.length()+1);
		jta.setCaretPosition(curLen);
		jta.update(jta.getGraphics());
		res = true;
		return res;
	}

	/**
	 * Clears the text area
	 */
	public boolean clear()
	{
		boolean res = false;
		jta.setText("");
		curLen = 0;
		jta.update(jta.getGraphics());
		return res;
	}

	public static void main(String[] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(800, 400);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MyConsole c = new MyConsole(300,300);
		jf.add(c, BorderLayout.CENTER);
		jf.setVisible(true);
		for (int i = 0; i < 100; i++)
		{
			c.println("This is line: " + (i + 1));
			try
			{
				Thread.sleep(25);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		System.out.println("This line should be shown in real console...");
	}
}
