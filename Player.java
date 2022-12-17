import java.util.ArrayList;
import javax.swing.*;

/**
 * Yarik Popov & Kirill Dariy <br>
 * ICS4U1, Mr. Naccarato <br>
 * Jan 22, 2022 <br>
 * This is the player class for the monopoly game. It is the main class of the program.
 */
public class Player
{
	//Class constants related to image
	public static final String BORDER = "border";
	public static final String[] IMAGES_NAMES = {"car", "cat", "dog", "wagon"};
	public static final String IMAGE_PATH = ".//Images//";
	public static final String IMAGE_EXTENSION = "Transparent ";
	public static final String IMAGE_FILE_TYPE = ".png";

	//Class constants related to drawing off info
	public static final int SQUARE_SIZE = 300;
	public static final int CORNER_SIZE = 10;
	public static final int[] X_DISPLAY = {1000, 1400};
	public static final int[] Y_DISPLAY = {50, 400};
	public static final int DISPLAY_CENTER_X = 130;
	public static final int DISPLAY_CHANGE_Y = 25;
	public static final int STARTING_IMAGE_LOCATION = 800;
	public static final int X_TEXT_LENGTH = 100;
	public static final int Y_TEXT_HEIGHT = 25;
	public static final int DISPLAY_IMAGE_MOVE = 20;
	public static final int IMAGE_MOVE = 76;
	public static final int[] IMAGE_OFFSETS = {0, 30};
	
	//Button constants
	public static final int NUM_BUTTONS = 3; //When adding buttons to player increase this constant
	public static final int BUTTON_OWNED_PROPERTIES = 0;
	public static final int BUTTON_BUYING = 1;
	public static final int BUTTON_PASS = 2;
	public static final String BUTTON_NAME_OWNED_PROPERTIES = "Your properties";
	public static final String BUTTON_NAME_BUYING = "Buy property";
	public static final String BUTTON_NAME_PASS = "Pass your turn";
	
//	Constants related to displaying property info
	public static final int PROPERTIES_INFO_WIDTH = 200;
	public static final int PROPERTIES_INFO_HEIGHT = 30;
	public static final int PROPERTIES_INFO_STEP = 50;
	public static final int PROPERTIES_X_NAME = 100;
	public static final int PROPERTIES_X_BUTTON = 300;
	
	//Class constants related to starting or default values
	public static final int STARTING_MONEY = 1000;
	public static final int GO_AMOUNT = 200;
	public static final int NO_JAIL = 0;
	public static final int STARTING_LOCATION = 0;
	public static final String DEFAULT_NAME = "Default Player";
	public static final int DEFAULT_JAIL_TIME = 3;
	
	//Other class constants
	public static final String[] NAMES = {"Player 1", "Player 2", "Player 3", "Player 4"};
	public static final String TEXT_JAIL_TIME = "Jail time of ";
	public static final String TEXT_MONEY = "Has $";
	public static final String TEXT_LOCATION = "Location at:";
	public static final String TEXT_NUM_JAIL_FREES = " jail frees cards";
	
	//Instance variables
	private int money;
	private int jailTime;
	private int location;
	private String name;
	private ArrayList<Property> properties;
	private int numJailFrees;
	private int rollDouble;
	
//	Instance variables related to x and y of the image, imageOffSet & Display
	private int xImage;
	private int yImage;
	private int xImageOffSet;
	private int yImageOffSet;
	private int xDisplay;
	private int yDisplay;
	
//	Instance variables related to the content displayed on the screen 
	private ImageIcon image;
	private JLabel imageLabel;
	private JLabel moneyLabel;
	private JLabel indicatorLabel;
	private JLabel indicatorLabelLost;
	private JLabel jailTimeLabel;
	private JLabel locationLabel;
	private JLabel numJailFreesLabel;
	
	//Instance variable buttons
	private JButton buyingButton;
	private JButton passButton;
	private JButton ownedPropertiesButton;

	//Constructor
	
	/**
	 * The constructor creates an object based on the index in NAMES string array.
	 * It also sets the coordinates of the display, imageOffSet and image based on the
	 * index.
	 * @param index
	 */
	public Player(int index)
	{	
//		Makes sure the index is valid
		if(index >=0 && index < NAMES.length)
		{
			name = NAMES[index];
			xDisplay = X_DISPLAY[index % 2];
			yDisplay = Y_DISPLAY[(int) (index / 2)];
			xImageOffSet = IMAGE_OFFSETS[index % 2];
			yImageOffSet = IMAGE_OFFSETS[(int) (index / 2)]; 
			String path = getImageFilePath(index);
			
//			Gets the image path making sure it exists and creates the image and imagelabel off of it
			if(path != null)
			{
				image = new ImageIcon(path);
				imageLabel = new JLabel(image);
			}
			placeImage();
				
		}
//		Invalid index inputed
		else
		{
			name = DEFAULT_NAME;
		}
			
//		Initialize other variables
		money = STARTING_MONEY;
		jailTime = NO_JAIL;
		location = STARTING_LOCATION;
		properties = new ArrayList<Property>();
		rollDouble = 0;
		numJailFrees = 0;
		
		//Initializes buttons
		buyingButton = new JButton(BUTTON_NAME_BUYING);
		ownedPropertiesButton = new JButton(BUTTON_NAME_OWNED_PROPERTIES);
		passButton = new JButton(BUTTON_NAME_PASS);
		
//		Initialize labels
		moneyLabel = new JLabel(TEXT_MONEY+money);
		jailTimeLabel = new JLabel(TEXT_JAIL_TIME+jailTime);
		indicatorLabel = new JLabel(new ImageIcon(IMAGE_PATH+"Green indicator.png"));
		indicatorLabelLost = new JLabel(new ImageIcon(IMAGE_PATH+"Red indicator.png"));
		locationLabel = new JLabel(Tile.locationToString(location));
		numJailFreesLabel = new JLabel(numJailFrees+TEXT_NUM_JAIL_FREES);
	}
	
	//Methods
	
	/**
	 * This is a static method which dependent on the index gets the image file path
	 * and returns it or if the index is invalid when it returns null.
	 * @param index
	 * @return String. Path or null.
	 */
	public static String getImageFilePath(int index)
	{
		if(index >=0 && index < IMAGES_NAMES.length)
			return IMAGE_PATH+IMAGE_EXTENSION+IMAGES_NAMES[index]+IMAGE_FILE_TYPE;
		else
			return null;
	}
	
	/**
	 * This method checks if the player can continue play by calling hasMoney() and 
	 * canMortgageProperties() if both are false than the player can't play. <br> <br>
	 * 
	 * Info from hasMoney(): <br>
	 * The player's money is greater than 0. <br> <br>
	 * 
	 * Info from canMortgageProperties(): <br>
	 * This method checks if the player can mortgage existing properties by calling getMortgageTotal() and 
	 * checking if the amount is greater than 0. <br> <br>
	 * 
	 * Info from getMortgageTotal(): <br>
	 * This method gets the total possible mortgage amount at the given moment checking if the
	 * properties can be mortgaged or not. Uses Property.canPay() {Instance method} and 
	 * Property.getMortgagePrice() {Instance method}. <br> <br>
	 * 
	 * Info from Property.canPay() {Instance method}: <br>
	 * returns canPay {Instance variable}. <br> <br>
	 * 
	 * Info from Property.getMortgagePrice() {Instance method}: <br>
	 * return rentPrice/2 {Instance variable}.
	 * @return boolean
	 */
	public boolean canPlay()
	{
		return hasMoney() || canMortgageProperties();
	}
	
	//Image methods
	
	/**
	 * This method places the image on the board based on the location, imageOffSets
	 * and if the player is in jail or not.
	 */
	public void placeImage()
    {
    //Places image in jail
		if(isInJail())
		{
			xImage = STARTING_IMAGE_LOCATION-(IMAGE_MOVE*10)+xImageOffSet;
			yImage = STARTING_IMAGE_LOCATION+yImageOffSet;
		}
		//Places image on bottom row of tiles
		else if(location >=0 && location <= 9)
		{
			xImage = STARTING_IMAGE_LOCATION-(IMAGE_MOVE*location)+xImageOffSet;
			yImage = STARTING_IMAGE_LOCATION+yImageOffSet;
			
		}
		//Visiting jail
		else if(location == 10)
		{
			if(yImageOffSet > 0)
			{
				xImage = STARTING_IMAGE_LOCATION-(IMAGE_MOVE*11)+70;
				yImage = STARTING_IMAGE_LOCATION+yImageOffSet;
			}
			else
			{
				xImage = STARTING_IMAGE_LOCATION-(IMAGE_MOVE*11)+xImageOffSet+70;
				yImage = STARTING_IMAGE_LOCATION+70;
			}
		}
		//Places image on left side
		else if(location >= 11 && location <= 20)
		{
			xImage = STARTING_IMAGE_LOCATION-(IMAGE_MOVE*10)+xImageOffSet;
			yImage = STARTING_IMAGE_LOCATION-(IMAGE_MOVE*(location-10))+yImageOffSet;
		}
		//Places image on top row of tiles
		else if(location >= 21 && location <= 30)
		{
			xImage = STARTING_IMAGE_LOCATION-(IMAGE_MOVE*30)+(IMAGE_MOVE*location)+xImageOffSet;
			yImage = STARTING_IMAGE_LOCATION-(IMAGE_MOVE*10)+yImageOffSet;
		}
		//Places image on right row of tiles
		else if(location >= 31)
		{
			xImage = STARTING_IMAGE_LOCATION+xImageOffSet;
			yImage = STARTING_IMAGE_LOCATION-(IMAGE_MOVE*40)+(IMAGE_MOVE*location)+yImageOffSet;
		}
		drawImage();
	}
	
	/**
	 * This method draws the image on the imageLabel based on the x and y of the image
	 */
	public void drawImage()
	{
		imageLabel.setBounds(xImage, yImage, image.getIconWidth(), image.getIconHeight());
	}
	
	/**
	 * This method draws the info of the player in the display, places the image of the
	 * character on board and sets up the location of the buttons.
	 * @param frame
	 */
	public void drawInfo(JFrame frame)
	{
//		Creates display border and places it
		ImageIcon borderImage = new ImageIcon(IMAGE_PATH+IMAGE_EXTENSION+BORDER+IMAGE_FILE_TYPE);
		JLabel borderLabel = new JLabel(borderImage);
		borderLabel.setBounds(xDisplay, yDisplay, borderImage.getIconWidth(), borderImage.getIconHeight());
		frame.add(borderLabel);

//		Places playing indicator
		ImageIcon playerIndicator = (ImageIcon) indicatorLabel.getIcon();
		indicatorLabel.setBounds(xDisplay+250, yDisplay+20, playerIndicator.getIconWidth(), playerIndicator.getIconHeight());
		frame.add(indicatorLabel);
		indicatorLabel.setVisible(false); // we can activate this with the roll button to help the user know whos turn it is
		
//		Places indicator lost
		ImageIcon playerIndicatorLost = (ImageIcon) indicatorLabelLost.getIcon();
		indicatorLabelLost.setBounds(xDisplay+250, yDisplay+20, playerIndicatorLost.getIconWidth(), playerIndicatorLost.getIconHeight());
		frame.add(indicatorLabelLost);
		indicatorLabelLost.setVisible(false); // we can activate this with the roll button to help the user know whos turn it is
		
		//Adds the image to the info display 
		JLabel imageLabel1 = new JLabel(image);
		imageLabel1.setBounds(xDisplay+DISPLAY_IMAGE_MOVE, yDisplay+DISPLAY_IMAGE_MOVE, image.getIconWidth(), image.getIconHeight());
		frame.add(imageLabel1);
		
		//Adds the image to the board so it can be moved around
		drawImage();
		frame.add(imageLabel);
		
		//Initializes x and y used in display
		int x = xDisplay+DISPLAY_CENTER_X;
		int y = yDisplay+DISPLAY_CHANGE_Y;
		
		//Info related to name
		JLabel nameLabel = new JLabel(name);
		nameLabel.setBounds(x, y, X_TEXT_LENGTH, Y_TEXT_HEIGHT);
		frame.add(nameLabel);
		
		//Info related to money
		y += DISPLAY_CHANGE_Y;
		moneyLabel.setBounds(x, y, X_TEXT_LENGTH, Y_TEXT_HEIGHT);
		frame.add(moneyLabel);
		
		//Info related to jail time
		y += DISPLAY_CHANGE_Y;
		jailTimeLabel.setBounds(x, y, X_TEXT_LENGTH, Y_TEXT_HEIGHT);
		frame.add(jailTimeLabel);
		
//		Info related to num jail frees
		y += DISPLAY_CHANGE_Y;
		numJailFreesLabel.setBounds(x, y, X_TEXT_LENGTH, Y_TEXT_HEIGHT);
		frame.add(numJailFreesLabel);
		
//		Info related to location
		y += DISPLAY_CHANGE_Y;
		JLabel locationHelpLabel = new JLabel(TEXT_LOCATION);
		locationHelpLabel.setBounds(x, y, X_TEXT_LENGTH, Y_TEXT_HEIGHT);
		frame.add(locationHelpLabel);
		
//		Info related to location
		y += DISPLAY_CHANGE_Y;
		locationLabel.setBounds(x, y, X_TEXT_LENGTH*2, Y_TEXT_HEIGHT);
		frame.add(locationLabel);
		
		//Sets up the owned Properties button
		x -= 25;
		y += DISPLAY_CHANGE_Y+5;
		ownedPropertiesButton.setBounds(x, y, 125, Y_TEXT_HEIGHT);
		frame.add(ownedPropertiesButton);
		ownedPropertiesButton.setVisible(false);
	
		//Sets up the buy property button
		y += DISPLAY_CHANGE_Y;
		buyingButton.setBounds(x, y+5, 125, Y_TEXT_HEIGHT);
		frame.add(buyingButton);
		buyingButton.setVisible(false);

		//Sets up the pass button
		y += DISPLAY_CHANGE_Y;
		passButton.setBounds(x, y+20, 125, Y_TEXT_HEIGHT);
		frame.add(passButton);
		passButton.setVisible(false);
	}
	
	/**
	 * This method updates the money and jail time info of the player in the
	 * display.
	 */
	public void updateInfo()
	{
//		Sets the money, jail time and num jail frees to the correct text
		moneyLabel.setText(TEXT_MONEY+money);
		jailTimeLabel.setText(TEXT_JAIL_TIME+jailTime);
		numJailFreesLabel.setText(numJailFrees+TEXT_NUM_JAIL_FREES);
		
//		Edge case for location label where the player is visiting the player
		if(!isInJail() && location == Tile.LOCATION_JAIL)
			locationLabel.setText("Visiting Jail");
		else
			locationLabel.setText(Tile.locationToString(location));
	}
	
	//Button methods
	
	/**
	 * This method turns the button of typeButton based on the appropriate constant to
	 * on or off. <br> <br>
	 * 
	 * This method is used by setButtonsVisible(boolean)
	 * to set all the buttons to on or off.
	 * @param onOff
	 * @param typeButton
	 */
	public void setButtonsVisible(boolean onOff, int typeButton)
	{
		if(typeButton == BUTTON_OWNED_PROPERTIES)
			ownedPropertiesButton.setVisible(onOff);
		else if(typeButton == BUTTON_BUYING)
			buyingButton.setVisible(onOff);
		else if(typeButton == BUTTON_PASS)
			passButton.setVisible(onOff);
	}
	
	/**
	 * This method sets all the buttons to on or off based on the parameter by calling
	 * setButtonsVisible(boolean, int) passing in i for the int parameter. <br> <br>
	 * 
	 * Info from setButtonsVisible(boolean, int): <br>
	 * This method turns the button of typeButton based on the appropriate constant to
	 * on or off. 
	 * @param onOff
	 */
	public void setButtonsVisible(boolean onOff)
	{
		for(int i=0; i<NUM_BUTTONS; i++)
			setButtonsVisible(onOff, i);
	}
	
	/**
	 * This method returns the button based on the type according to the appropriate constant.
	 * If the type parameter is not equal to any of the button constants then it returns null.
	 * @param type
	 * @return button or null.
	 */
	public JButton getButton(int type)
	{
		if(type == BUTTON_BUYING)
			return buyingButton;
		else if(type == BUTTON_PASS)
			return passButton;
		else if(type == BUTTON_OWNED_PROPERTIES)
			return ownedPropertiesButton;
		
		return null;
	}
	
	/**
	 * This method sets the indicator label to visible or invisible based on the
	 * parameter.
	 * @param onOff
	 */
	public void setIndicatorLabelVisible(boolean onOff)
	{
		indicatorLabel.setVisible(onOff);
	}
	
	/**
	 * This method adds the action listeners to the buttons of the player.
	 * @param game
	 */
	public void addActionListeners(Game game)
	{
//		Shows all the owned properties
		ownedPropertiesButton.addActionListener(e -> 
		{
			displayProperties();
		});
		
//		Buys the property
		buyingButton.addActionListener(e ->  
		{
//			Gets the tile and property at current location
			Tile tile = game.getTiles().get(location);
			Property property = tile.getProperty();
			
//			Check to make sure property exists
			if(tile.hasProperty())
			{
				property.displayInfo(this);
			}
//			Shows message that you can't buy this because it is not a property
			else
			{
				JOptionPane.showMessageDialog(null, "You can't buy this because it isn't a property");
			}
		});

//		Passes the turn onto the next player
		passButton.addActionListener(e -> 
		{
			//Clear the player buttons and sets the roll button to visible
			setButtonsVisible(false);
			game.getRoll().setVisible(true);
			
//			Goes to the next player and gets the next player
			game.goToNextPlayer();
			Player nextPlayer = game.getPlayers().get(game.getCurrentPlayer());
			
//			Sets the next player's indicator label to visible and this player's one to invisible
			nextPlayer.setIndicatorLabelVisible(true);
			setIndicatorLabelVisible(false);
		});
		
	}
	
	/**
	 * This method creates a frame to display the property names and a button to show info. 
	 * It uses getPropertyNames() & addPropertiesToFrame(JFrame, ArrayList<<String>String>, int). <br> <br>
	 * 
	 * Info from getPropertyNames(): <br>
	 * This method gets all the properties names of this player and returns it inside of an
	 * array list. <br> <br>
	 * 
	 * Info from addPropertiesToFrame(JFrame, ArrayList<<String>String>, int) <br>
	 * This is a helper method to add in the names of the properties and the show info button
	 * to the frame.
	 */
	public void displayProperties()
	{
//		Initializes frame and property names arraylist
		JFrame frame = new JFrame("List of Properties");
		ArrayList<String> propertyNames = getPropertyNames();
		
		int height = getPropertyNames().size()*PROPERTIES_INFO_STEP+(PROPERTIES_INFO_STEP*3);
		frame.setSize(600, height);
		
//		Gets the image and adds to frame setting bounds
		JLabel copiedImage = new JLabel(image);
		copiedImage.setBounds(10, 10, image.getIconWidth(), image.getIconHeight());
		frame.add(copiedImage);
		
		int propertyNamesSize = propertyNames.size();
//		Adds the properties into the frame
		for(int i=0; i< propertyNamesSize; i++)
			addPropertiesToFrame(frame, propertyNames, i);
		
//		The player has no properties so create a label stating this
		if(propertyNamesSize == 0)
		{
			JLabel noProperties = new JLabel("You have no properties");
			noProperties.setBounds(PROPERTIES_X_NAME*2, PROPERTIES_INFO_STEP/2, PROPERTIES_INFO_WIDTH, PROPERTIES_INFO_HEIGHT);
			frame.add(noProperties);
		}
		
//		Sets layout to null, visible and default close operation
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * This is a helper method to add in the names of the properties and the show info button
	 * to the frame used in displayProperties().
	 * @param frame
	 * @param names
	 * @param index
	 */
	private void addPropertiesToFrame(JFrame frame, ArrayList<String> names, int index)
	{
//		Creates property name label and y
		JLabel propertyName = new JLabel(names.get(index));
		int y = PROPERTIES_INFO_STEP*(index+1);
		
//		Sets bounds of property name label and adds to frame
		propertyName.setBounds(PROPERTIES_X_NAME, y, PROPERTIES_INFO_WIDTH, PROPERTIES_INFO_HEIGHT);
		frame.add(propertyName);
		
//		Creates show info button and sets bounds 
		JButton showInfo = new JButton("Show info");
		showInfo.setBounds(PROPERTIES_X_BUTTON, y, PROPERTIES_INFO_WIDTH, PROPERTIES_INFO_HEIGHT);
		
//		Adds show info button to frame and creates action listener for button which shows info related to a property
		frame.add(showInfo);
		showInfo.addActionListener(e -> {
			properties.get(index).displayInfo(this);
		});
	}
	
	/**
	 * This method sets the buying button to visible if the player is at a
	 * location where the property can be purchased. Otherwise it would make this
	 * button invisible. You can only buy properties where one exists.
	 * @param game
	 */
	public void showBuyingButton(Game game)
	{
		Property property = game.getTiles().get(location).getProperty();
		if(canBuyProperty(property))
			setButtonsVisible(true, BUTTON_BUYING);
		else
			setButtonsVisible(false, BUTTON_BUYING);
	}
	
	/**
	 * This method moves the player by the amount. 
	 * If also makes sure to loop around if the location is greater than
	 * the number of tiles or less than 0. If passing go adds money to player. Only adds
	 * money if the amount is positive, can't go backwards and get go money.
	 * @param amount
	 */
	public void move(int amount)
	{
		if(!isInJail())
		{
//			Sets useful variables and increases location
			int tileListLength = Tile.LIST_OF_TILES.length;
			boolean wentAround = false;
			location += amount;
			
//			Makes sure the player's location isn't over the length of the tiles
			while(location >= tileListLength)
			{
				location -= Tile.LIST_OF_TILES.length;
				wentAround = true;
			}
			
//			Makes sure the player's location isn't negative
			while(location < 0)
			{
				location += tileListLength;
			}
				
//			Adds money to player passing go
			if(wentAround)
				money += GO_AMOUNT;
		}
		placeImage();
	}
	
	//Money methods
	
	/**
	 * This method adds money to the player.
	 * It makes sure the amount is greater than 0.
	 * @param amount
	 */
	public void addMoney(int amount)
	{
		if(amount > 0)
			money += amount;
	}
	
	/**
	 * This method takes away money from the player. It makes sure the player
	 * can pay this amount by calling canPay(int). <br> <br>
	 * 
	 * Info from canPay(int): <br>
	 * This method makes sure the player can pay the amount inputed by checking
	 * if the amount is greater than 0 and that the player's money is greater or equal
	 * to the amount.
	 * @param amount
	 */
	public void takeMoney(int amount)
	{
		if(canPay(amount))
			money -= amount;
	}
	
	/**
	 * This method makes sure the player can pay the amount inputed by checking
	 * if the amount is greater than 0 and that the player's money is greater or equal
	 * to the amount.
	 * @param amount
	 * @return boolean
	 */
	public boolean canPay(int amount)
	{
		return amount >= 0 && money >= amount;
	}
	
	/**
	 * The player's money is greater than 0. Used by canPlay().
	 * @return
	 */
	public boolean hasMoney()
	{
		return money > 0;
	}
	
	//Jail Methods
	
	/**
	 * This method reduces the jail time of the player by making sure first that
	 * the player is in jail by calling isInJail(). <br> <br>
	 * 
	 * Info from isInJail(): <br>
	 * This method checks if the jail time of the player is greater than the no jail 
	 * constant or 0.
	 */
	public void reduceJailTime()
	{
		if(isInJail())
			jailTime--;
	}
	
	/**
	 * This method sets the jail time of the player based to the default jail time constant.
	 * By calling setJailTime(int) and passing in the DEFAULT_JAIL_TIME constant as
	 * the parameter. See that method for more info. <br> <br>
	 * 
	 * Info from setJailTime(int): <br>
	 * It also sets the location and image of character to the jail location, turns off all buttons
	 * except for passing and sets the roll double count to 0.
	 */
	public void setJailTime()
	{
		setJailTime(DEFAULT_JAIL_TIME);
	}
	
	/**
	 * This method checks if the jail time of the player is greater than the no jail 
	 * constant or 0. 
	 * @return boolean
	 */
	public boolean isInJail()
	{
		return jailTime > NO_JAIL;
	}
	
	/**
	 * This method checks if the player can escape jail or that the num of jail frees
	 * is greater than 0.
	 * @return boolean
	 */
	public boolean canEscapeJail()
	{
		return numJailFrees > NO_JAIL;
	}
	
	/**
	 * This method frees the player from jail by check if the player can escape jail and
	 * if they are in jail by calling canEscapeJail() and isInJail().
	 * It sets the jail time to 0 and reduces the number of jail frees by 1. <br> <br>
	 * 
	 * Info from canEscape():<br> 
	 * This method checks if the player can escape jail or that the number of jail frees
	 * is greater than 0. <br> <br>
	 * 
	 * Info from isInJail(): <br>
	 * This method checks if the jail time of the player is greater than the no jail 
	 * constant or 0. 
	 */
	public void escapeJail()
	{
		if(canEscapeJail() && isInJail())
		{
			jailTime = 0;
			numJailFrees--;
		}
	}
	
	/**
	 * This method adds jail frees to the player by check to make sure the amount inputed
	 * is greater than 0.
	 * @param num
	 */
	public void addNumJailFrees(int num)
	{
		if(num > 0)
			numJailFrees += num;
	}
	
	/**
	 * This method increases the number of jail frees by 1 by calling addNumJailFrees(int) 
	 * and passing in 1 as the parameter. <br> <br>
	 * 
	 * Info from addNumJailFrees(int): <br>
	 * This method adds jail frees to the player by check to make sure the amount inputed
	 * is greater than 0.
	 */
	public void addNumJailFrees()
	{
		addNumJailFrees(1);
	}
	
	/**
	 * This method check to see if 2 players are equal by making sure the other player
	 * isn't a null. It would return false. It would check to see if the players names are
	 * equal and if they are it would return true.
	 * @param other
	 * @return boolean
	 */
	public boolean equals(Player other)
	{
		if(other == null)
			return false;
		else
			return name.equals(other.getName());
	}
	
	//Mortgage methods
	
	/**
	 * This method mortgages the players properties based on the card inputed passing
	 * the cost of the card into mortgageProperties(int).
	 * @param card
	 */
	public void mortgageProperties(Card card)
	{
//		Checks if card exists and gets cost as positive
		if(card != null)
		{
			int cost = -card.getCost();
			
//			Edge case for card
			if(card.isMakeGeneralRepair())
				mortgageProperties(cost*properties.size());
			
//			Mortgages properties based on cost
			else
				mortgageProperties(cost);
		}
			
	}
	
	/**
	 * This method mortgages the properties of the player without asking for permission
	 * until the player has enough money
	 * @param amount
	 */
	public void mortgageProperties(int amount)
	{
		if(!canPay(amount))
		{
//			Initializes useful variables
			int neededAmount = amount;
			
//			Loops through each property and checks if can pay and amount is greater than 0
			for(Property p: properties)
				if(p.canPay() && amount > 0)
				{
//					Displays message of mortgaging property and decreases amount 
					JOptionPane.showMessageDialog(null, p.getName()+" was mortgaged to pay for debts");
					amount -= p.getMortgagePrice();
					
//					Increase money of player and sets the property can pay rent to false
					money += p.getMortgagePrice();
					p.setCanPay(false);
				}
			
//Reduces money if money is greater than neededAmount
			if(money > neededAmount)
				money -= neededAmount;
			else
				money = 0;
		}
//		If the amount is greater than 0 and can pay the amount and updates info
		else if(amount > 0)
			money -= amount;
		updateInfo();
	}
	
	/**
	 * This method mortgages the properties of the player based on rent price of the property inputed. 
	 * It is used in the Property.payRent(int) {Instance method}. <br> <br>
	 * 
	 * Info Property.payRent(int) {Instance method}: <br>
	 * This method pays the rent to the owner by taking the players money or if they
	 * don't have enough money, the players properties are mortgaged to pay. It also 
	 * creates message boxes to indicate what is happening.
	 * @param property
	 */
	public void mortgageProperties(Property property)
	{
//		Makes sure the property exists
		if(property != null)
		{
//			Gets rent price and checks if the property can pay and player can't pay rent
			int rentPrice = property.getRentPrice();
			if(property.canPay() && !canPay(rentPrice))
			{
				
//			Initialize important variables
				int neededAmount = rentPrice - transferRemainingMoney(property);
				int amount = 0;
				
//				Loops through the properties of this player checking if the property can pay and if amount 
				for(Property p: properties)
					if(p.canPay() && amount < neededAmount)
					{
//						Show message message, add mortgage price to amount and set property to can't pay
						JOptionPane.showMessageDialog(null, p.getName()+" was mortgaged to pay for debts");
						amount += p.getMortgagePrice();
						p.setCanPay(false);
					}
				
//				Get owner and remaining money to it
				Player owner = property.getOwner();
				if(amount > neededAmount)
				{
					owner.addMoney(neededAmount);
					money = amount - neededAmount;
				}
				
//				Add the amount of money which is left and updates info
				else
					owner.addMoney(amount);
				updateInfo();
			}
		}
	}
	
	/**
     * This is a method used by the mortgageProperties(Property) and Property.payRent(Player) {Instance method} methods
     * to transfer the remaining money to the owner of the property. It returns the remaining amount of money left so it 
     * can be used to reduce from mortgageProperties(int) method. 
     * @param player
     * @return remainingAmount.
     */
    public int transferRemainingMoney(Property property)
    {
    	int remainingAmount = money;
		money = 0;
		property.getOwner().addMoney(remainingAmount);
		return remainingAmount;
    }
	
	/**
	 * This method checks if the player can mortgage existing properties by calling getMortgageTotal() and 
	 * checking if the amount is greater than 0. <br> <br>
	 * 
	 * Info from getMortgageTotal(): <br>
	 * This method gets the total possible mortgage amount at the given moment checking if the
	 * properties can be mortgaged or not. Uses Property.canPay() {Instance method} and 
	 * Property.getMortgagePrice() {Instance method}. <br> <br>
	 * 
	 * Info from Property.canPay() {Instance method}: <br>
	 * returns canPay {Instance variable}. <br> <br>
	 * 
	 * Info from Property.getMortgagePrice() {Instance method}: <br>
	 * return rentPrice/2 {Instance variable}.
	 * @return boolean
	 */
	public boolean canMortgageProperties()
	{
		return getMortgageTotal() > 0;
	}
	
	/**
	 * This method gets the total possible mortgage amount at the given moment checking if the
	 * properties can be mortgaged or not. Uses Property.canPay() {Instance method} and 
	 * Property.getMortgagePrice() {Instance method}. <br> <br>
	 * 
	 * Info from Property.canPay() {Instance method}: <br>
	 * returns canPay {Instance variable}. <br> <br>
	 * 
	 * Info from Property.getMortgagePrice() {Instance method}: <br>
	 * return rentPrice/2 {Instance variable}.
	 * @return total
	 */
	public int getMortgageTotal()
	{
		int total = 0;
		for(Property p: properties)
			if(p.canPay())
				total += p.getMortgagePrice();
		return total;
	}
	
//	Property methods
	
	/**
	 * This method gets all the properties names of this player and returns it inside of an
	 * array list. 
	 * @return output
	 */
	public ArrayList<String> getPropertyNames()
	{
		ArrayList<String> output = new ArrayList<String>();

//		Loops over each property gets the name and adds to outputs string array list
		for(Property p: properties)
			output.add(p.getName());
		
		return output;
	}
	
	/**
	 * This method buys the property by checking to see if the player can buy it by calling
	 * canBuyProperty(Property). If the player can buy this property,
	 * it is added to the properties array list and money is subtracted. <br> <br>
	 * 
	 * Info from canBuyProperty(Property): <br>
	 * This method checks the following conditions on the property: <br>
	 * 1. Isn't null <br>
	 * 2. Isn't purchased calls Property.isPurchased() {Instance method} <br>
	 * 3. Player doesn't have it  calls hasProperty(Property) <br>
	 * 4. Player has enough money to buy it calls canPay(int) and passes in the purchase price
	 * as the parameter <br>
	 * If all these are true then this method returns true
	 * @param property
	 */
	public void buyProperty(Property property)
	{
		if(canBuyProperty(property))
		{
			addProperty(property);
			takeMoney(property.getPurchasePrice());
		}
	}
	
	/**
	 * This method checks the following conditions on the property: <br>
	 * 1. Isn't null <br>
	 * 2. Isn't purchased calls Property.isPurchased() {Instance method} <br>
	 * 3. Player doesn't have it  calls hasProperty(Property) <br>
	 * 4. Player has enough money to buy it calls canPay(int) and passes in the purchase price
	 * as the parameter <br>
	 * If all these are true then this method returns true
	 * @param property
	 * @return boolean
	 */
	public boolean canBuyProperty(Property property)
	{
		return property != null && !property.isPurchased() && !hasProperty(property) && canPay(property.getPurchasePrice());
	}
	
	/**
	 * This method makes sure the property isn't null then it would run through and check if the
	 * property inputed exists in the arraylist of properties. It is does it would return true.
	 * @param property
	 * @return
	 */
	public boolean hasProperty(Property property)
	{
		if(property != null)
			for(Property p: properties)
				if(p.equals(property))
					return true;
		return false;
	}
	
	//Adds property to properties array and sets the owner of property to this player
	/**
	 * This method adds the property to the properties arraylist and sets the property's owner
	 * to this player. This method also makes sure the property isn't null and the player doesn't
	 * have this property by calling hasProperty(Property). <br> <br>
	 * 
	 * Info from hasProperty(Property): <br>
	 * This method makes sure the property isn't null then it would run through and check if the
	 * property inputed exists in the arraylist of properties. It is does it would return true.
	 * @param property
	 */
	public void addProperty(Property property)
	{
		if(property != null && !hasProperty(property))
		{
			properties.add(property);
			property.setOwner(this);
		}
	}
	
	/**
	 * This method counts the number of properties in the properties arraylist
	 * which have the same type and returns this number. 
	 * @param type
	 * @return count
	 */
	public int getNumPropertiesOfType(int type)
	{
		int count = 0;
		for(Property p: properties)
			if(p.getType() == type)
				count++;
		return count;
	}
	
	/**
	 * This method checks to see if the player has all the properties of the inputed type
	 * by calling getNumPropertiesOfType(int) and Property.getNumProperties(int). <br> <br>
	 * 
	 * Info from getNumPropertiesOfType(int): <br>
	 * This method counts the number of properties in the properties arraylist
	 * which have the same type and returns this number. 
	 * @param type
	 * @return
	 */
	public boolean hasAllPropertiesOfType(int type)
	{
		return getNumPropertiesOfType(type) == Property.getNumProperties(type);
	}
	
	/**
	 * This method increases the roll double count by 1
	 */
	public void increaseRollDouble()
	{
		rollDouble++;
	}
	
	//Getters

	public JLabel getIndicatorLabelLost()
	{
		return indicatorLabelLost;
	}
	
	public JButton getPassButton()
	{
		return passButton;
	}
	
	public JButton getBuyingButton()
	{
		return buyingButton;
	}
	
	public JButton getOwnedPropertiesButton()
	{
		return ownedPropertiesButton;
	}
	
	public int getMoney()
	{
		return money;
	}
	
	public int getJailTime()
	{
		return jailTime;
	}
	
	public int getLocation()
	{
		return location;
	}
	
	public int getXImage() {
		return xImage;
	}
	
	public int getYImage() {
		return yImage;
	}
	
	public int getXDisplay() {
		return xDisplay;
	}
	
	public int getYDisplay() {
		return yDisplay;
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ArrayList<Property> getProperties()
	{
		return properties;
	}
	
	public int getXImageOffSet()
	{
		return xImageOffSet;
	}
	
	public int getYImageOffSet()
	{
		return yImageOffSet;
	}
	
	public int getNumJailFrees() {
		return numJailFrees;
	}   
	
	public int getRollDouble() {
		return rollDouble;
	}

	public JLabel getMoneyLabel() {
		return moneyLabel;
	}
	
	public JLabel getJailTimeLabel() {
		return jailTimeLabel;
	}
	
	public JLabel getIndicatorLabel()
	{
		return indicatorLabel;
	}
	
	//Setters
	
	public JLabel getLocationLabel() {
		return locationLabel;
	}

	public JLabel getNumJailFreesLabel() {
		return numJailFreesLabel;
	}

	public void setNumJailFreesLabel(JLabel numJailFreesLabel) {
		this.numJailFreesLabel = numJailFreesLabel;
	}

	public void setLocationLabel(JLabel locationLabel) {
		this.locationLabel = locationLabel;
	}

	public void setIndicatorLabelLost(JLabel indicatorLabelLost)
	{
		this.indicatorLabelLost = indicatorLabelLost;
	}
	
	public void setPassButton(JButton passButton)
	{
		this.passButton = passButton;
	}
	
	public void setBuyingButton(JButton buyingButton)
	{
		this.buyingButton = buyingButton;
	}
	
	public void setOwnedPropertiesButton(JButton ownedPropertiesButton)
	{
		this.ownedPropertiesButton = ownedPropertiesButton;
	}
	
	public void setNumJailFrees(int numJailFrees) {
		this.numJailFrees = numJailFrees;
	}
	
	public void setMoneyLabel(JLabel moneyLabel) {
		this.moneyLabel = moneyLabel;
	}

	public void setJailTimeLabel(JLabel jailTimeLabel) {
		this.jailTimeLabel = jailTimeLabel;
	}

	public void setRollDouble(int rollDouble) {
		this.rollDouble = rollDouble;
	}

	public void setMoney(int money)
	{
		this.money = money;
	}
	
	/**
	 * This method sets the jail time of the player based. It also sets the
	 * location and image of character to the jail location, turns off all buttons
	 * except for passing and sets the roll double count to 0.
	 * @param jailTime
	 */
	public void setJailTime(int jailTime)
	{
		this.jailTime = jailTime;
		location = Tile.LOCATION_JAIL;
		placeImage();
		setButtonsVisible(false, Player.BUTTON_BUYING);
		setButtonsVisible(false, Player.BUTTON_OWNED_PROPERTIES);
		setButtonsVisible(true, Player.BUTTON_PASS);
		setRollDouble(0);
	}
	
	/**
	 * This method sets the location of the player. It checks the make sure the 
	 * location is valid and adds money if passing go.
	 * @param location
	 */
	public void setLocation(int location)
	{
		if(location >=0 && location < Tile.LIST_OF_TILES.length)
		{
			int previous = this.location;
			this.location = location;
			
//			The player passed go
			if(this.location < previous)
				money += GO_AMOUNT;
		}
	}
	
	public void setXImage(int xImage) {
		this.xImage = xImage;
	}
	
	public void setYImage(int yImage) {
		this.yImage = yImage;
	}
	
	public void setXDisplay(int xDisplay) {
		this.xDisplay = xDisplay;
	}
	
	public void setYDisplay(int yDisplay) {
		this.yDisplay = yDisplay;
	}
	
	public void setImage(ImageIcon image) {
		this.image = image;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setProperties(ArrayList<Property> properties)
	{
		this.properties = properties;
	}
	
	public void setXImageOffSet(int xImageOffSet)
	{
		this.xImageOffSet = xImageOffSet;
	}
	
	public void setYImageOffSet(int yImageOffSet)
	{
		this.yImageOffSet = yImageOffSet;
	}
	
	public void setIndicatorLabel(JLabel indicatorLabel)
	{	
		this.indicatorLabel = indicatorLabel;
	}
	
	//ToString
	public String toString()
	{
		return name+" with $"+money+" at "+location+" index, jail time of "+jailTime+", double roll value of "+rollDouble+", "+numJailFrees+" num jail frees with properties "+properties.toString();
	}

}