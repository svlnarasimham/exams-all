package utility;
import java.awt.*;
import javax.swing.*;

public class MessagePopup extends JFrame
{
   private static final long serialVersionUID = 1L;
   private JLabel jl;

   public MessagePopup(JFrame parentFrame)
	{
   	int w = 600,h = 125;
   	int x=100, y=100;
   	int pw, ph;
   	Dimension scrSize;
   	if(parentFrame == null)
   	{
   		scrSize = Toolkit.getDefaultToolkit().getScreenSize();
   		x = 0;
   		y = 0;
   	}
   	else
   	{
   		scrSize = parentFrame.getSize();
   		x = parentFrame.getX();
   		y = parentFrame.getY();
   	}
   	pw = (int) scrSize.getWidth();
   	ph = (int) scrSize.getHeight();
   	x = Math.max(x , x + (pw-w)/2);
   	y = Math.max(y, y + (ph-h)/2);

      setLocation(x, y);

   	jl = new JLabel("", JLabel.CENTER);
   	jl.setForeground(Color.yellow);
     	Container c = getContentPane();
     	c.setBackground(Color.blue);
   	c.setLayout(new BorderLayout());
   	setUndecorated(true);
		setSize(500,125);
		setLocation(100,100);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		c.add(jl);
		//setVisible(false);
	}
   
   public void open()
   {
   	setVisible(true);
   }

   public void showMessage(String msg, long delay)
   {
		jl.setText(msg);
		try
		{
			Thread.sleep(delay);
		}
		catch(InterruptedException e) {};
   }

   public void close()
   {
   	setVisible(false);
   	dispose();
   }

   public static void main(String args[])
	{
//		JFrame jf = new JFrame();
//		jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		jf.setSize(400,400);
//		jf.setLocation(100,100);
//		jf.setVisible(true);
		MessagePopup mp = new MessagePopup(null);
		mp.open();
		mp.showMessage("Test message", 1000); 
		mp.showMessage("Message 2 is on the way .. ", 1000);
		mp.close();
	}

}
