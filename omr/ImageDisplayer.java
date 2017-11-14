package omr;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Displays an Image in a scrollable Panel and interactively gets field values manually 
 * Wherever an error is detected in bubbling.
 * @author SVL Narasimham
 *
 */
public class ImageDisplayer extends JFrame implements ActionListener
{
	public static final long serialVersionUID = 1L;
	public static final String WRONG_ANSWER = "?";
	OmrSheet os;
	ImageIcon icon;
	String imageName=null; //image file name
	int ih=0, iw=0; //image height and width in pixels
	JScrollPane jsp1 = null;    // scrollbar for image 
	JLabel jlImage;	// label to show image as its icon
	JPanel bottomPane; 			// main panel for controls and fields
	BufferedImage image=null;	// for loading image file 
	JButton jbNext;	// button for closing window
	JTextField jtfxy, jtfclr;		// to represent fileds in params.txt file
	JLabel jlmsg; // Message label
	Graphics2D g2d;
	int oboxx=0, oboxy=0, oboxw=0, oboxh=0; // old box values
	int cboxx, cboxy, cboxw, cboxh; // cur box value
	int jsww, jswh;
	boolean ready = false; // flag for calling object for reading value
	
	/**
	 * constructor
	 * @param title Window Title
	 * @param w Width of window in pixels
	 * @param h Height of window in pixels
	 */
	public ImageDisplayer(String title, int w, int h)	
	{
		super(title); // Call Parent's constructor
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gd = image.createGraphics();
		gd.setColor(Color.white);
		gd.fillRect(0, 0, w, h);
		icon = new ImageIcon(image);
		jlImage = new JLabel(icon); // loading image to jlImage
		
		bottomPane = new JPanel();  // Bottom Interface panel
		bottomPane.setLayout(new FlowLayout());
		
		jbNext=new JButton("Next");
		jbNext.setActionCommand("Next");
		jbNext.addActionListener(this);

		jlImage.setHorizontalAlignment(SwingConstants.LEFT);
		jlImage.setVerticalAlignment(SwingConstants.TOP);
		
		jsp1 = new JScrollPane(jlImage,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp1.getVerticalScrollBar().setMaximum(100);
		jsp1.getHorizontalScrollBar().setMaximum(100);

		// displaying offset as label & textfield for recording co-ordinates 
		jtfxy= new JTextField();
		jtfxy.setPreferredSize(new Dimension(50,20));
		//jtfxy.setEditable(false);
		jlmsg = new JLabel("Enter Value for the Field (Green Box):", JLabel.RIGHT);
		bottomPane.add(jlmsg);
		bottomPane.add(jtfxy);
		bottomPane.add(jbNext);
		c.add(jsp1,BorderLayout.CENTER);
		c.add(bottomPane,BorderLayout.SOUTH);
		this.setSize(new Dimension(w,h));
		this.setVisible(true);
		jsww = jsp1.getViewport().getWidth();
		jswh = jsp1.getViewport().getHeight();
		//System.out.println("Jscrollpane Width and Height: " + jsww + ", " + jswh);
	}
	
	public void actionPerformed(ActionEvent ae)
	{	
		String ac = ae.getActionCommand();
		if(ac.equals("Next"))
		{
			ready = true;
		}
	}
	
	// Control Functions 
	/**
	 * Finds if the result is ready for reading
	 * @return true if result is ready or false otherwise
	 */
	public boolean isReady()
	{
		return ready;
	}
	
	/**
	 * Resets result to false.
	 */
	public void resetReady()
	{
		ready = false;
	}
	
	/**
	 * Reads an image from file and shows it 
	 * @param path File Full path and name
	 */
	public void setIcon(String path)
	{
		try
		{
			image = ImageIO.read(new File(path));
			setIcon(image);
		}
		catch(Exception e){}
	}
	
	/**
	 * Sets image to be displayed
	 * @param img image object to be shown
	 */
	public void setIcon(BufferedImage img)
	{
		image = img;
		ih = image.getHeight();
		iw = image.getWidth();
		g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(Color.green);
		g2d.setXORMode(Color.white);
		g2d.setStroke(new BasicStroke(2));
		icon = new ImageIcon(image);
		jlImage.setIcon(icon);
	}
	
	/**
	 * Sets the current OmrSheet Object
	 * @param os OmrSheet Object to be used for field coordinates
	 */
	public void setOmrSheet(OmrSheet os)
	{
		this.os = os;
	}
	
	/**
	 * Gets the value of the user input text field
	 * @return Text Field's Value
	 */
	public String getValue()
	{
		return jtfxy.getText().trim().toUpperCase();
	}
	
	/**
	 * Closes and disposes the display window.
	 */
	public void close()
	{
		dispose();
	}
	
	/**
	 * Draws a rectangle around the given field
	 * @param fieldNumber Field number in the current sheet
	 */
	private void showField(int fieldNumber)
	{
		resetReady();
		OmrSheet.OmrField of = os.fields[fieldNumber];
		int minx=100000, maxx=-1, miny=100000, maxy=-1; // initialize to impossible values
		int n = of.vbbl.size();
		int i,x,y,w,h;
		// get min and max x and y coordinates for the bubbles of the given field
		for(i=0;i<n;i++)
		{
			OmrSheet.Bubble b = of.vbbl.get(i);
			x = os.columnOffsets[b.column];
			y = os.getTrackY(b.track);
			if(x<minx) minx = x;
			if(x>maxx) maxx = x;
			if(y<miny) miny = y;
			if(y>maxy) maxy = y;
		}
		// get Rectangle Size
		x = os.refX + minx -os.bubbleWidth/2;
		y = os.refY + miny - os.bubbleHeight/2;
		w = maxx - minx + os.bubbleWidth;
		h = maxy - miny + os.bubbleHeight;
		//System.out.println("Box: " + x+","+y+","+ w+","+h);
		drawRect(x,y,w,h);
		
		// If box height or width is out of visible area, scroll content
		JScrollBar jsh = jsp1.getHorizontalScrollBar();
		JScrollBar jsv = jsp1.getVerticalScrollBar();
		
		// Set HScroll and VScroll values
		jswh = jsp1.getViewport().getHeight();
		jsww = jsp1.getViewport().getWidth();
		int nh = (y+h)/jswh;
		int nw = (x+w)/jsww;
		
		jsh.setValue(Math.min(nw*jsww, x-w));
		jsv.setValue(Math.min(nh*jswh, y-h));
		jlImage.repaint();
	}
	
	/**
	 * Draws a rectangle around the given field set
	 * @param fieldNumbers Field numbers Array in the current sheet
	 */
	private void showFieldsGroup(int fieldNumbers[])
	{
		resetReady();
		
		int minx=100000, maxx=-1, miny=100000, maxy=-1; // initialize to impossible values
		int i,j,x,y,w,h, sz;
		sz = fieldNumbers.length;
		for(j=0;j<sz; j++)
		{
			OmrSheet.OmrField of = os.fields[fieldNumbers[j]];
			int n = of.vbbl.size();
			// get min and max x and y coordinates for the bubbles of the given field
			for(i=0;i<n;i++)
			{
				OmrSheet.Bubble b = of.vbbl.get(i);
				x = os.columnOffsets[b.column];
				y = os.getTrackY(b.track);
				if(x<minx) minx = x;
				if(x>maxx) maxx = x;
				if(y<miny) miny = y;
				if(y>maxy) maxy = y;
			}
		}
		// get Rectangle Size
		x = os.refX + minx -os.bubbleWidth/2;
		y = os.refY + miny - os.bubbleHeight/2;
		w = maxx - minx + os.bubbleWidth;
		h = maxy - miny + os.bubbleHeight;
		//System.out.println("Box: " + x+","+y+","+ w+","+h);
		drawRect(x,y,w,h);
		
		// If box height or width is out of visible area, scroll content
		JScrollBar jsh = jsp1.getHorizontalScrollBar();
		JScrollBar jsv = jsp1.getVerticalScrollBar();
		
		// Set HScroll and VScroll values
		jswh = jsp1.getViewport().getHeight();
		jsww = jsp1.getViewport().getWidth();
		int nh = (y+h)/jswh;
		int nw = (x+w)/jsww;
		
		jsh.setValue(Math.min(nw*jsww, x-w));
		jsv.setValue(Math.min(nh*jswh, y-h));
		jlImage.repaint();
	}
	
	/**
	 * Draws a rectangle and removes the old rectangle if any.
	 * @param x Rectangle start x
	 * @param y Rectangle start y
	 * @param w Rectangle width
	 * @param h Rectangle height
	 */
	private void drawRect(int x, int y, int w, int h)
	{
		if(x>iw || y > ih)
		{
			return;
		}
		// remove old box
		g2d.drawRect(oboxx, oboxy, oboxw, oboxh);
		// draw new box
		g2d.drawRect(x, y, w, h); // Horizontal line 

		oboxx = x;
		oboxy = y;
		oboxw = w;
		oboxh = h;
		image.flush();
		//jlImage.setIcon(icon);
		jlImage.repaint();
	}
   
	/**
	 * Gets the manual response for a given field. It also marks the field with green box.
	 * @param message Prompt to be shown before the input text box 
	 * @param fieldNumber Field Number which requires manual input
	 * @param defaultValue Initial value of the input text box.
	 * @return Response for the given field
	 */
	public String getField(String message, int fieldNumber, String defaultValue)
	{
		jlmsg.setText(message);
		//System.out.println("Getting field: " + fieldNumber);
		showField(fieldNumber);
		jtfxy.setText(defaultValue);
		jtfxy.setSelectionStart(0);
		jtfxy.setSelectionEnd(defaultValue.length());
		jtfxy.requestFocus();
		while(isVisible() && !isReady())
		{
			try{
				Thread.sleep(300);//wait 100 ms
			}
			catch(Exception e){}
		}
		if(isReady())
		{ 
			return getValue();
		}
		else 
		{
			return defaultValue; // returned due to window closing
		}
	}

	/**
	 * Gets the manual response for a given field. It also marks the field with green box.
	 * @param message Prompt to be shown before the input text box 
	 * @param fieldNumbers Field Numbers as an integer array which requires manual input
	 * @param defaultValue Initial value of the input text box.
	 * @return Response for the given field
	 */
	public String getFieldGroup(String message, int fieldNumbers[], String defaultValue)
	{
		jlmsg.setText(message);
		//System.out.println("Getting field: " + fieldNumber);
		showFieldsGroup(fieldNumbers);
		jtfxy.setText(defaultValue);
		jtfxy.setSelectionStart(0);
		jtfxy.setSelectionEnd(defaultValue.length());
		jtfxy.requestFocus();
		while(isVisible() && !isReady())
		{
			try{
				Thread.sleep(300);//wait 100 ms
			}
			catch(Exception e){}
		}
		if(isReady())
		{ 
			return getValue().trim();
		}
		else 
		{
			return defaultValue; // returned due to window closing
		}
	}

	public static void main(String[] args)
	{
		ImageDisplayer dsp = new ImageDisplayer("Image Displayer", 900,800);
		dsp.setIcon("A0009/lomr-0001.jpg");
		OmrSheet os = new OmrSheet();
		os.loadSheetDetails("int-omr.txt");
		//os.readSheet("lomr-0001.jpg");68, 272
		os.refX = 68;
		os.refY = 272;
		dsp.setOmrSheet(os);
		int x[] = { 0,1,2,3};
		System.out.println("Value 40: " + dsp.getFieldGroup("Enter value for field 40", x,"x"));
		System.out.println("Value 27: " + dsp.getField("Value for field 27", 27, "0"));
		dsp.close();
	}
}
