package common;
public class Coordinates
{
	public static final float CM2PT   = 28.3465f;
	public static final float PT2CM   = 0.03528f;
	public static final float CM2INCH = 0.3937f;
	public static final float INCH2CM = 2.54f;
	public static final float INCH2PT = 72.0f;
	public static final float PT2INCH = 0.0139f;

	// All coordinates are in CM
	public  float pageHeight = 29.7f; // page height 
	public  float pageWidth  = 21.0f;   // page width
	
	public  float offsetX = 0.0f;     // offset from right
	public  float offsetY = 0.0f;     // offset from top

}
