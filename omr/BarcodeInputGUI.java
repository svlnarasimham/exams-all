package omr;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Displays an Image in a scrollable Panel and interactively gets field values manually 
 * Wherever an error is detected in bubbling.
 * @author SVL Narasimham
 *
 */
public class BarcodeInputGUI extends JDialog implements ActionListener
{
	public static final long serialVersionUID = 1L;
	ImageIcon icon;
	JLabel jlImage;	// label to show image as its icon
	JPanel bottomPane; 			// main panel for controls and fields
	JButton jbok;	// button for closing window
	JTextField jtfBarcode;		// to represent fileds in params.txt file
	JLabel jlmsg; // Message label
	public String barcode="";
	
	/**
	 * constructor
	 * @param title Window Title
	 * @param w Width of window in pixels
	 * @param h Height of window in pixels
	 */
	public BarcodeInputGUI(String title, BufferedImage bi)	
	{
		super((JFrame)null, true); // Call Parent's constructor
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		icon = new ImageIcon(bi);
		jlImage = new JLabel(icon); // loading image to jlImage
		c.add(jlImage, BorderLayout.CENTER);
		
		bottomPane = new JPanel();  // Bottom Interface panel
		bottomPane.setLayout(new FlowLayout());
		
		jbok=new JButton("OK");
		jbok.setActionCommand("OK");
		jbok.addActionListener(this);

		jlImage.setHorizontalAlignment(SwingConstants.LEFT);
		jlImage.setVerticalAlignment(SwingConstants.TOP);
		
		// displaying offset as label & textfield for recording co-ordinates 
		jtfBarcode= new JTextField();
		jtfBarcode.setPreferredSize(new Dimension(150,20));
		//jtfxy.setEditable(false);
		jlmsg = new JLabel("Enter barcode value:", JLabel.RIGHT);
		bottomPane.add(jlmsg);
		bottomPane.add(jtfBarcode);
		bottomPane.add(jbok);
		c.add(bottomPane,BorderLayout.SOUTH);
		this.setSize(new Dimension(400,300));
		int x = ((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-400)/2;
		int y = ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-300)/2;
		this.setLocation(x, y);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae)
	{	
		barcode = jtfBarcode.getText();
		dispose();
	}
	
	public static void main(String args[]) throws IOException
	{
		BufferedImage bi = ImageIO.read(new File("MyBarcode3.jpg"));
		BarcodeInputGUI bg = new BarcodeInputGUI("Barcode", bi);
		System.out.println(bg.barcode);
	}
}
