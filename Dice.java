import java.util.Random;
import javax.swing.*;
/**
 * Jan 20, 2022 <br>
 * This is the dice class used in the monopoly game. It is used as the randomizer
 * for moving the player on the board.
 */
public class Dice 
{
	//Class constant
	public static final int INITIAL_VALUE = 1;
	public static final int NUM_SIDES = 6;
	public static final String DICE_FILE_PATH = ".//Images//Dice//";
	public static final String DICE_FILE_NAME = "diceface";
	public static final String DICE_FILE_TYPE = ".png";
	public static final int[] X_IMAGE = {1050, 1200};
	public static final int Y_IMAGE = 780;
		
	//Instance variables
	private int value;
	private int xPosition;
	private int yPosition;
	private JLabel label;
	private Random random;
	
	//Creates a new dice
	public Dice(int index)
	{
		random = new Random();
		label = new JLabel();
		
		if(index >=0 && index < X_IMAGE.length)
			xPosition = X_IMAGE[index];
		else
			xPosition = X_IMAGE[0];
		
		yPosition = Y_IMAGE;
		
		getNewValue();
	}
	
	//Creates a new value and returns it
	public int getNewValue()
	{
		value = random.nextInt(NUM_SIDES) + INITIAL_VALUE;
		ImageIcon image = new ImageIcon(DICE_FILE_PATH+DICE_FILE_NAME+value+DICE_FILE_TYPE);
		label.setIcon(image);
		label.setBounds(xPosition, yPosition, image.getIconWidth(), image.getIconHeight());
		return value;
	}
	
//	Adds the dice to the frame
	public void addToFrame(JFrame frame)
	{
		frame.add(label);
	}
	
	//Getters
	//Gets the value
	public int getValue()
	{
		return value;
	}
	
	public int getXPosition()
	{
		return xPosition;
	}
	
	public int getYPosition()
	{
		return yPosition;
	}
	
	public void setVisible(boolean type)
	{
		label.setVisible(type);
	}
	
	//Gets the string of the icon
	public JLabel getLabel()
	{
		return label;
	}
	
	//Setters
	
	public void setXPosition(int xPosition)
	{
		this.xPosition = xPosition;
	}
	
	public void setYPosition(int yPosition)
	{
		this.yPosition = yPosition;
	}
	
	public void setLabel(JLabel label)
	{
		this.label = label;
	}
	
	//Returns a string representation of the object
	public String toString()
	{
		return "Dice with value of "+value;
	}
}
