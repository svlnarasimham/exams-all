package omr;
import java.awt.image.BufferedImage;


public class ImageRotator 
{
	public static void main(String[] args) 
	{
		// Set OMR sheet's Paths
		// 3. Load image file names
		String basePath = "/home/qis/EXAMS/btech";
		String folder = utility.Utilities.chooseFile(basePath, utility.Utilities.OPEN, utility.Utilities.DIRS, "..*", "Select JPG Folder");
		
		String imageFilesList[] = utility.Utilities.getFiles(folder, "..*[Jj][Pp][Gg]");
		if(imageFilesList.length == 0)// no images found 
		{
			System.out.println("No images files found in " + folder);
			return;
		}
		
		
		for(int i=0; i< imageFilesList.length; i++)
		{
			String fname = folder+"/"+imageFilesList[i];
			System.out.println("" + i + " of "+ imageFilesList.length + ": " + fname);
			BufferedImage bi = ImageTools.getImage(fname);
			bi = ImageTools.rotate(bi, 90);
			ImageTools.saveImage(bi, fname, true);
			System.gc();
		}
	}
}
