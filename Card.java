import java.util.Random;
import javax.swing.*;

/**
 * Yarik Popov & Kirill Dairy <br>
 * ICS4U1, Mr. Naccarato <br>
 * Jan 22, 2022 <br>
 * This is the Card class which handles the chance and community
 * cards in the game on monopoly.
 */
public class Card 
{
	//Class constants related to getting of image of file
	public static final String CARD_FILE_PATH = ".//Images//";
	public static final String CHANCE_EXTENSION = "chance//";
	public static final String COMMUNITY_EXTENSION = "community//";
	public static final String CARD_FILE_TYPE = ".png";
	
	//Chance
	public static final String[] CHANCE_NAMES = {"Advance To Boardwalk", "Go Back 3 Spaces", "Advance To Illinois", "Advance To Nearest Railroad", "Advance To Nearest Utility", "Advance To St. Charles", "Bank Pays You 50", "Building and Loan Matures", "Get Out of Jail Free", "Advance To Go", "Go To Jail", "Make General Repairs", "Poor Tax"};
	public static final int[] CHANCE_MONEY = {0, 0, 0, 0, 0, 0, 50, 150, 0, 200, 0, -25, -15};
	//Edge cases
	public static final int CHANCE_BOARDWALK = 0;
	public static final int CHANCE_GO_BACK3 = 1;
	public static final int CHANCE_ILLINOIS = 2;
	public static final int CHANCE_RAILROAD = 3;
	public static final int CHANCE_UTILITY = 4;
	public static final int CHANCE_ST_CHARLES = 5;
	public static final int CHANCE_ESCAPE_JAIL = 8;
	public static final int CHANCE_GO_TO_START = 9;
	public static final int CHANCE_GO_TO_JAIL = 10;
	public static final int CHANCE_MAKE_GENERAL_REPAIRS = 11;
	
	//Community
	public static final String[] COMMUNITY_NAMES = {"Advance To Go", "Bank Error", "Beauty Contest", "Doctor's Fee", "From Sale of Stock", "Get Out of Jail Free", "Go To Jail", "Income Tax Refund", "Inheritance", "Life Insurance Matures", "Pay Hospital", "Pay School Tax", "Receive for Services", "XMas Fund Matures"};
	public static final int[] COMMUNITY_MONEY = {200, 200, 10, -50, 45, 0, 0, 20, 100, 100, -100, -150, 25, 100};
	//Edge cases
	public static final int COMMUNITY_GO = 0;
	public static final int COMMUNITY_ESCAPE_JAIL = 5;
	public static final int COMMUNITY_GO_TO_JAIL = 6;
	
	//Constants related to setting x and y positions
	public static final int DEFAULT_POSITION = 200;
	
	//Instance variables
	private boolean isChance;
	private int type;
	private int xPosition;
	private int yPosition;
	private JLabel label;
	private Random random;
	
	public Card(boolean isChance)
	{
		this.isChance = isChance;
		random = new Random();
		label = new JLabel();
		xPosition = DEFAULT_POSITION;
		yPosition = DEFAULT_POSITION;
		getNewType();
	}

//	Methods
	
	public int getNewType()
	{
		//Creates the type and image for chance card
		if(isChance)
		{
			type = random.nextInt(CHANCE_NAMES.length);
		}
		//Creates the type and image for community card
		else
		{
			type = random.nextInt(COMMUNITY_NAMES.length);
		}
		drawImage();
		return type;
	}
	
//	This method draws the image based on the type and chance or community.
	public void drawImage()
	{
		System.out.println("drawImage()");
		System.out.println(toString());
		ImageIcon image = null;
		
		if(isChance)
			image = new ImageIcon(CARD_FILE_PATH+CHANCE_EXTENSION+CHANCE_NAMES[type]+CARD_FILE_TYPE);
		else
			image = new ImageIcon(CARD_FILE_PATH+COMMUNITY_EXTENSION+COMMUNITY_NAMES[type]+CARD_FILE_TYPE);

		label.setIcon(image);
		label.setBounds(xPosition, yPosition, image.getIconWidth(), image.getIconHeight());
	}
	
	/**
	 * This method checks to make sure the card can perform the event on the
	 * player. As in the player has enough money to pay for the fee associated with
	 * type if there is one.
	 * @param player
	 * @return
	 */
	public boolean canDoEvent(Player player)
	{
//		The card is a chance
		if(isChance)
		{
			int amount = CHANCE_MONEY[type];
			
//			Money being added
			if(amount >=0)
				return true;
//			Special case where the fee depends on the players number of properties
			if(type == CHANCE_MAKE_GENERAL_REPAIRS)
			{
				return player.canPay(amount*player.getProperties().size());
			}
//			Money being taken away
			return player.canPay(-amount);
		}
//		The card is a community
		else
		{
			int amount = COMMUNITY_MONEY[type];
//			Money being added
			if(amount >=0)
				return true;
//			Money being taken away
			return player.canPay(-amount);
		}
	}
	
	/**
	 * This method does the event based on the type and whether the card is a chance
	 * or community card on the player. It also makes sure the event can be done on the
	 * player by calling canDoEvent(Player) see this method for more info.
	 * @param player
	 */
	public void doEvent(Player player)
	{
//		Makes sure the card can do the event on the player
		if(canDoEvent(player))
		{
//			Does the event if the card is a chance card
			if(isChance)
			{
				if(type == CHANCE_BOARDWALK)
					player.setLocation(Tile.LOCATION_BROADWALK);
				else if(type == CHANCE_GO_BACK3)
					player.move(-3);
				else if(type == CHANCE_ILLINOIS)
					player.setLocation(Tile.LOCATION_ILLINOIS);
				else if(type == CHANCE_RAILROAD)
					setLocationRailRoadUtility(player, Tile.LOCATION_RAILROADS); 
				else if(type == CHANCE_UTILITY)
					setLocationRailRoadUtility(player, Tile.LOCATION_UTILITIES);
				else if(type == CHANCE_ST_CHARLES)
					player.setLocation(Tile.LOCATION_ST_CHARLES);
				else if(type == CHANCE_ESCAPE_JAIL)
					player.addNumJailFrees();
				else if(type == CHANCE_GO_TO_START)
					player.setLocation(Player.STARTING_LOCATION);
				else if(type == CHANCE_GO_TO_JAIL)
					player.setJailTime();
				else if(type == CHANCE_MAKE_GENERAL_REPAIRS)
					player.takeMoney(CHANCE_MONEY[type]*player.getProperties().size());
				else
				{
					int amount = CHANCE_MONEY[type];
					if(amount > 0)
						player.addMoney(amount);
					else
						player.takeMoney(-amount);
				}
			}
//			Does the event if the card is a community card
			else
			{
				if(type == COMMUNITY_GO)
					player.setLocation(Player.STARTING_LOCATION);
				else if(type == COMMUNITY_GO_TO_JAIL)
					player.setJailTime();
				else if(type == COMMUNITY_ESCAPE_JAIL)
					player.addNumJailFrees();
				else
				{
					int amount = COMMUNITY_MONEY[type];
					if(amount >= 0)
						player.addMoney(amount);
					else
						player.takeMoney(-amount);
				}
			}
		}
		else
			player.mortgageProperties(this);
	}
	
	/**
	 * Returns if the card is a chance card of type make general repairs
	 * @return
	 */
	public boolean isMakeGeneralRepair()
	{
		return isChance && type == CHANCE_MAKE_GENERAL_REPAIRS;
	}
	
	/**
	 * Gets the cost of the card on the player
	 * @return
	 */
	public int getCost()
	{
		if(isChance)
			return CHANCE_MONEY[type];
		else
			return COMMUNITY_MONEY[type];
	}
	
	/**
	 * This helper method sets the location of the player to the nearest railroad or utility
	 * before the current position.
	 * @param player
	 * @param list
	 */
	private void setLocationRailRoadUtility(Player player, int[] list)
	{
		int location = -1;
		for(int position: list)
			if(position >= player.getLocation())
			{
				location = position;
				player.setLocation(location);
				break;
			}
		if(location == -1)
			player.setLocation(Tile.LOCATION_RAILROADS[0]);
	}
	
	//Adds the card to the frame
	public void addToFrame(JFrame frame)
	{
		frame.add(label);
	}
	
	public String typeToString()
	{
		if(isChance)
			return CHANCE_NAMES[type];
		else
			return COMMUNITY_NAMES[type];
	}
	
	public String isChanceToString()
	{
		if(isChance)
			return "Chance";
		else
			return "Community";
	}
	
//	Getters
	
	public boolean isChance() {
		return isChance;
	}

	public int getType() {
		return type;
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	public int getXPosition()
	{
		return xPosition;
	}
	
	public int getYPosition()
	{
		return yPosition;
	}
	
//	Setters
	
	public void setChance(boolean isChance) {
		this.isChance = isChance;
	}

	public void setType(int type) {
		this.type = type;
		drawImage();
	}
	
	public void setLabel(JLabel label) {
		this.label = label;
	}
	
	public void setXPosition(int xPosition)
	{
		this.xPosition = xPosition;
	}
	
	public void setYPosition(int yPosition)
	{
		this.yPosition = yPosition;
	}
	
	// ToString
	
	public String toString()
	{
		return isChanceToString()+" Card of "+typeToString();
	}
}
