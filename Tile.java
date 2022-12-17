import javax.swing.*;

/**
 * Jan 22, 2022 <br>
 * This class holds the info related to each tile in the game.
 */
public class Tile 
{
	//Class constants related to special tiles
	public static final int EVENT_STARTING_POSITION = -1; //No Event
	public static final int EVENT_COMMUNITY_CHEST = -2;
	public static final int EVENT_INCOME_TAX = -3; //Finished
	public static final int EVENT_CHANCE = -4;
	public static final int EVENT_JAIL = -5; //No Event
	public static final int EVENT_FREE_PARKING = -6; //No Event
	public static final int EVENT_GO_TO_JAIL = -7; //Finished
	public static final int EVENT_LUXURY_TAX = -8; //Finished
	
	//Constants related to tax
	public static final int INCOME_TAX_AMOUNT = 200;
	public static final int LUXURY_TAX_AMOUNT = 100;
	
	//Class constants
	public static final int MIN_PROPERTY_VALUE = 0;
	public static final int[] LIST_OF_TILES =  {EVENT_STARTING_POSITION, 0, EVENT_COMMUNITY_CHEST, 1, EVENT_INCOME_TAX, 2, 3, EVENT_CHANCE, 4, 5, EVENT_JAIL, 6, 7, 8, 9, 10, 11, EVENT_COMMUNITY_CHEST, 12, 13, EVENT_FREE_PARKING, 14, EVENT_CHANCE,  15, 16, 17, 18, 19, 20, 21, EVENT_GO_TO_JAIL, 22, 23, EVENT_COMMUNITY_CHEST, 24, 25, EVENT_CHANCE, 26, EVENT_LUXURY_TAX, 27};
	public static final String[] NAMES = {"Property", "Starting Position", "Community Chest", "Income Tax", "Chance", "Jail", "Free Parking", "Go to jail", "Luxury Tax"};
	
	//Class constants related to specific locations
	public static final int[] LOCATION_RAILROADS = {5, 15, 25, 35};
	public static final int[] LOCATION_UTILITIES = {12, 28};
	public static final int LOCATION_JAIL = 10;
	public static final int LOCATION_ST_CHARLES = 11;
	public static final int LOCATION_ILLINOIS = 24;
	public static final int LOCATION_BROADWALK = 39;
	
//	Window constants
	public static final int JLABEL_TEXT_LENGTH = 400;
	
	//Instance variables
	private int type;
	private Property property;
	private Card card;
	
	//Constructor
	
	/**
	 * The constructor takes in an integer for the type and creates a tile off of it. <br>
	 * If the type is greater than 0 and less than Property.NAMES_LIST.length, it creates
	 * a tile with a property. <br>
	 * If the type is inputed is equal to the constants EVENT_CHANCE or EVENT_COMMUNITY_CHEST
	 * than a chance card or a community card is created respectively. <br>
	 * Else the type inputed becomes a type matching the event constants.
	 * @param type
	 */
	public Tile(int type)
	{
		this.type = type;
		
//		Creates the property if the type is a property
		if(type >= MIN_PROPERTY_VALUE && type < Property.NAMES_LIST.length)
			property = new Property(type);
		
//		Creates the chance card if correct type
		if(type == EVENT_CHANCE)
			card = new Card(true);
		
//		Creates community card if correct type
		else if(type == EVENT_COMMUNITY_CHEST)
			card = new Card(false);
	}
	
	//Methods
	
	/**
	 * This method gets the location of a tile as a string from the index inputed based on the
	 * LIST_OF_TILES string array constant. If the tile at that index is a property it would
	 * get the name of that property instead of simply type property.
	 * @param index
	 * @return location or ""
	 */
	public static String locationToString(int index)
	{
		if(index >=0 && index < LIST_OF_TILES.length)
		{
			int type = LIST_OF_TILES[index];
			if(type >= MIN_PROPERTY_VALUE && type < Property.NAMES_LIST.length)
				return Property.NAMES_LIST[type];
			else
				return NAMES[-type];
		}
		return "";
	}
	
	/**
	 * This method creates the window where the info of the tile and whether the
	 * event was successfully completed is displayed. It also does the event.
	 * @param player
	 */
	public void createWindow(Player player)
	{
//		Gets new type of card
		getNewTypeCard();
		
//		Pays the rent to the owner
		if(hasProperty())
			property.payRent(player);
		
//		Does the event
		if(hasEvent())
		{
//			Creates the frame, set size, layout and location
			JFrame frame = new JFrame("Tile Event");
			frame.setLayout(null);
			frame.setLocationRelativeTo(null);
			
//			Create labels and add them to frame
//			Text label
			JLabel textLabel = new JLabel("You landed on "+typeToString());
			frame.add(textLabel);
			
//			Sets up exit button
			JButton exitButton = new JButton("Ok");
			exitButton.addActionListener(e -> {
				frame.dispose();
			});
			frame.add(exitButton);
			exitButton.setVisible(true);
			
//			Sets the size based on the image
			
			addCardToFrame(frame);
			if(hasCard())
			{
//				Sets the bounds of the labels and frame size
				frame.setSize(600, 600);
				textLabel.setBounds(150, 50, JLABEL_TEXT_LENGTH, 30);
				exitButton.setBounds(200, 400, 200, 50);
			}
			else
			{
//				Sets the bounds of the labels and frame size
				frame.setSize(500, 500);
				textLabel.setBounds(100, 50, JLABEL_TEXT_LENGTH, 30);
				exitButton.setBounds(100, 300, 200, 50);
			}
			
			doEvent(player);
			
//			Sets frame to visible and dispose on close
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}
	
	/**
	 * This method does the event on the player based on the type and whether it can
	 * do the event.
	 * @param player
	 */
	public void doEvent(Player player)
	{
		if(canDoEvent(player))
		{
			//Performs the event
			if(type == EVENT_INCOME_TAX)
				player.takeMoney(INCOME_TAX_AMOUNT);
			else if(type == EVENT_LUXURY_TAX)
				player.takeMoney(LUXURY_TAX_AMOUNT);
			else if(type == EVENT_GO_TO_JAIL)
				player.setJailTime();
			else if(hasCard())
				card.doEvent(player);
			player.placeImage();
		}
//		Mortgages properties of players to pay for event
		else if(hasCard())
			player.mortgageProperties(card);
		else if(type == EVENT_INCOME_TAX)
			player.mortgageProperties(INCOME_TAX_AMOUNT);
		else if(type == EVENT_LUXURY_TAX)
			player.mortgageProperties(LUXURY_TAX_AMOUNT);
		else if(hasProperty())
			property.payRent(player);
	}
	
	/**
	 * This method makes sure that the event can be done on the player based on the fee
	 * associated with the type.
	 * @param player
	 * @return boolean
	 */
	public boolean canDoEvent(Player player)
	{
		if(type == EVENT_INCOME_TAX)
			return player.canPay(INCOME_TAX_AMOUNT);
		else if(type == EVENT_LUXURY_TAX)
			return player.canPay(LUXURY_TAX_AMOUNT);
		else if(hasCard())
			return card.canDoEvent(player);
		return true;
	}
	
	//This tile has a card
	public boolean hasCard()
	{
		return card != null;
	}
	
	//This tile has a property
	public boolean hasProperty()
	{
		return property != null;
	}
	
//	This tile has an event
	public boolean hasEvent()
	{
		return type < MIN_PROPERTY_VALUE && type != EVENT_STARTING_POSITION && type != EVENT_FREE_PARKING && type != EVENT_JAIL;
	}
	
	//Prints out the type as a string
	public String typeToString()
	{
		if(hasCard())
			return card.toString();
		else if(hasProperty())
			return property.getName();		
		else if(type < MIN_PROPERTY_VALUE && -type < NAMES.length)
			return NAMES[-type];
		return "";
	}
	
	//Adds the card to the frame if this tile has a card
	public void addCardToFrame(JFrame frame)
	{
		if(hasCard())
			card.addToFrame(frame);
	}
	
//	Gets a new type of card if the tile has one
	public void getNewTypeCard()
	{
		if(hasCard())
			card.getNewType();
	}
	
	//Getters
	
	public int getType()
	{
		return type;
	}
	
	public Property getProperty()
	{
		return property;
	}
	
	public Card getCard() {
		return card;
	}
	
	//Setters

	public void setCard(Card card) {
		this.card = card;
	}

	//Needs to create property if correct type?
	public void setType(int type)
	{
		this.type = type;
	}
	
	public void setProperty(Property property)
	{
		this.property = property;
	}
	
	//ToString
	public String toString()
	{
		return "Tile of type: "+typeToString(); //+propertyToString();
	}
}
