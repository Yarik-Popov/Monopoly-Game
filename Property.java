import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;
/**
 * Jan 22, 2022 <br>
 * This class holds the info related to a property and all the methods. When the 
 * constructor is called with a valid int parameter it creates a property parsing the 
 * file based on the NAMES_LIST string array.
 */
public class Property 
{
	//Class constants info for creating properties from index
	public static final String PROPERTY_PATH = ".//Property Info//";
	public static final String[] NAMES_LIST = {"Mediterranean Avenue", "Baltic Avenue", "Reading Railroad", "Oriental Avenue", "Vermont Avenue", "Connecticut Avenue", "St. Charles Place", "Electric Company", "States Avenue", "Virginia Avenue", "Pennsylvania Railroad", "St. James Place", "Tennessee Avenue", "New York Avenue", "Kentucky Avenue", "Indiana Avenue", "Illinois Avenue", "B. & O. Railroad", "Atlantic Avenue", "Ventnor Avenue", "Water Works", "Marvin Gardens", "Pacific Avenue", "North Carolina Avenue", "Pennsylvania Avenue", "Short Line", "Park Place", "Boardwalk"};
	public static final String PROPERTY_FILE_TYPE = ".txt";
	
	//Class constants
	public static final String[] TYPES = {"Brown", "Cyan", "Magenta", "Orange", "Red", "Yellow", "Green", "Blue", "Railroad", "Utilities"};
	public static final boolean DEFAULT_CAN_PAY = false;
	public static final String CAN_PAY_RENT_STRING = "Can it accept rent?:    ";
	
	//Class constants related to types
	public static final String INVALID_TYPE = "Invalid type";
	public static final int TYPE_RAILROAD = 8;
	public static final int TYPE_UTILITY = 9;
	
	//Instance variables
	private String name;
	private int rentPrice;
	private int purchasePrice;
	private int type;
	private boolean canPay;
	private Player owner;
	
	/**
	 * The constructor taking in an index from NAMES_LIST and locates a file of the name
	 * PROPERTY_PATH+NAMES_LIST[index]+PROPERTY_FILE_TYPE. <br> <br>
	 * The constructor parses the information from the text file in the following order. <br>
	 * 1. rentPrice <br>
	 * 2. purchasePrice <br>
	 * 3. type in String[] TYPES <br>
	 * @param index
	 */
	public Property(int index)
	{
//		Gets the property file path and makes sure it isn't null
		String path = getPropertyFilePath(index);
		if(path != null)
		{
//			Sets up scanner variable and attempts to get the file
			Scanner scan = null;
			try
			{
				scan = new Scanner(new File(path));
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			
//			Sets the name based on the names list and default can pay
			name = NAMES_LIST[index];
			canPay = DEFAULT_CAN_PAY;
			
//			Attempts to parse the information from the file
			try
			{
				rentPrice = scan.nextInt();
				purchasePrice = scan.nextInt();
				type = scan.nextInt();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	//Methods
	
	/**
	 * This methods gets the property file path based on the index in the NAMES_LIST string
	 * array.
	 * @param index
	 * @return String. Path or null.
	 */
	public static String getPropertyFilePath(int index)
	{
		if(index >=0 && index < NAMES_LIST.length)
			return PROPERTY_PATH+NAMES_LIST[index]+PROPERTY_FILE_TYPE;
		else
			return null;
	}
	
	//Static method to check if type is valid
	public static boolean isValidType(int type)
	{
		return type >=0 && type < TYPES.length;
	}
	
	/**
	 * This method gets the number of properties based on the type. If the
	 * type is invalid it returns -1.
	 * @param type
	 * @return int
	 */
	public static int getNumProperties(int type)
	{
//		The property is of type Brown, Blue or a Utility
		if(type == 0 || type == 7 || type == TYPE_UTILITY)
			return 2;
		else if(type == TYPE_RAILROAD)
			return 4;
		else if(type >0 && type < TYPE_UTILITY)
			return 3;
		
		return -1;
	}
	
 /**
	 * This method pays the rent to the owner by taking the players money or if they
	 * don't have enough money, the players properties are mortgaged to pay. It also 
	 * creates message boxes to indicate what is happening.
	 * @param player
	 */
    public void payRent(Player player)
	{
		if(canPay() && isPurchased() && player != null && !player.equals(owner))
		{
//			Get owner name and show message related to transfer of funds
			String ownerName = owner.getName();
			JOptionPane.showMessageDialog(null, player.getName()+" landed on "+name+" owned by "+ownerName);

//			The player has enough money to pay for the rent
			if(player.canPay(rentPrice))
			{
				JOptionPane.showMessageDialog(null, ownerName+" took "+rentPrice+" to pay for rent");
				player.takeMoney(rentPrice);
				owner.addMoney(rentPrice);
			}
//				The player doesn't have enough money to pay for the rent
			else if(player.canMortgageProperties())
			{
//					Player transfers remaining money to owner and mortgages properties
				JOptionPane.showMessageDialog(null, ownerName+" took your remaining money to pay for the rent");
				JOptionPane.showMessageDialog(null, ownerName+" also mortgaged your properties to pay for the remaining rent");
				player.mortgageProperties(this);
			}
//				The player doesn't have properties they can mortgage so it is the end
			else
			{
				player.transferRemainingMoney(this);
				JOptionPane.showMessageDialog(null, "You don't have enough money to pay the full rent and can't mortgage a property so it is game over");
			}
			
		}
	}
	
	/**
	 * This method displays the info of the property and allows the user to buy it if it can be
	 * bought or mortgage/unmortgage the property.
	 * @param player
	 */
	public void displayInfo(Player player)
	{
//		Initialize variable and create frame
		JFrame frame = new JFrame(name+" Info");
		int y = 50;
		int x = 50;
		
//		Create and set bounds for nameLabel and increase y
		JLabel nameLabel = new JLabel("Name:    " + name);
		nameLabel.setBounds(x,y,300,30);
		y += 20;

//		Create and set bounds for purchasePriceLabel and increase y
		JLabel purchasePriceLabel = new JLabel("Price:    " + purchasePrice);
		purchasePriceLabel.setBounds(x,y,300,30);
		y += 20;

//		Create and set bounds for rentPriceLabel and increase y
		JLabel rentPriceLabel = new JLabel("Rent price:    " + rentPrice);
		rentPriceLabel.setBounds(x,y,300,30);
		y += 20;
		
//		Create and set bounds for mortgagePriceLabel and increase y
		JLabel mortgagePriceLabel = new JLabel("Mortgage price:    "+getMortgagePrice());
		mortgagePriceLabel.setBounds(x,y,300,30);
		y += 20;
			
//		Create and set bounds for typeLabel and increase y
		JLabel typeLabel = new JLabel("Colour Set:    " + TYPES[type]);
		typeLabel.setBounds(x,y,300,30);
		y += 20;

//		Create and set bounds for canPayLabel and increase y
		JLabel canPayLabel = new JLabel(CAN_PAY_RENT_STRING + canPay);
		canPayLabel.setBounds(x,y,300,30);
		y += 80;

//		Create mortgage button, setting the bounds and visibility
		JButton mortgageButton = new JButton("Mortgage your property");
		mortgageButton.setBounds(x,y,300,30);
		mortgageButton.setVisible(false);

//		Create unmortgage button, setting the bounds and visibility
		JButton unmortgageButton = new JButton("Unmortgage your property");
		unmortgageButton.setBounds(x,y,300,30);
		unmortgageButton.setVisible(false);

//		Create pay for property button, setting the bounds and visibility
		JButton payForProperty = new JButton("Buy property");
		payForProperty.setBounds(x,y,300,30);
		payForProperty.setVisible(false);
		
//		Sets the different buttons to visible		
		if(!isPurchased())
			payForProperty.setVisible(true);
		else if(canPay())
			mortgageButton.setVisible(true);
		else
			unmortgageButton.setVisible(true);
		
//		Action listener for mortgaging property
		mortgageButton.addActionListener(e ->
		{
//			Perform mortgage and show message
			JOptionPane.showMessageDialog(null, "You get "+getMortgagePrice()+", but your proprety can't pay rent");
			player.addMoney(getMortgagePrice());
			canPay = false;
			updatePlayerAndPropertyLabelInfo(player, canPayLabel);
			
//			Allows the player to buy this property if they unmortgaged enough money
			if(player.canPay(rentPrice))
				player.setButtonsVisible(true, Player.BUTTON_BUYING);
			
//			Set mortgage button to invisible and unmortgage to visible
			mortgageButton.setVisible(false);
			unmortgageButton.setVisible(true);
		});

//		Action listener for unmortgaging property
		unmortgageButton.addActionListener(e ->
		{
			// The player can unmortgage the property
			if(player.canPay(getMortgagePrice()))
			{
				JOptionPane.showMessageDialog(null, "You lose "+getMortgagePrice()+", but your proprety can pay rent");
				player.takeMoney(getMortgagePrice());
				canPay = true;
				updatePlayerAndPropertyLabelInfo(player, canPayLabel);
				
//				Set mortgage button to visible and unmortgage to invisible
				unmortgageButton.setVisible(false);
				mortgageButton.setVisible(true);
			}
			else
				JOptionPane.showMessageDialog(null, "You don't have enough money to unmortgage the property");
		});

//		Action listener for paying or buying property
		payForProperty.addActionListener(e ->
		{
//			Buys the property
			if(player.canBuyProperty(this))
			{
				JOptionPane.showMessageDialog(null, "Congratulations you now own "+name);
				player.buyProperty(this);
				updatePlayerAndPropertyLabelInfo(player, canPayLabel);
				
				payForProperty.setVisible(false);
				mortgageButton.setVisible(true);
			}
//			Player can't buy the property
			else
				JOptionPane.showMessageDialog(null, "You can't buy this property");
		});
		
//		Adds the labels to the frame
		frame.add(mortgagePriceLabel);
		frame.add(mortgageButton);
		frame.add(unmortgageButton);
		frame.add(payForProperty);
		frame.add(canPayLabel);
		frame.add(typeLabel);
		frame.add(purchasePriceLabel);
		frame.add(nameLabel);
		frame.add(rentPriceLabel);

//		Sets the size, location, layout and visibility of frame
		frame.setSize(420,420);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setVisible(true);
	}
	
	//This helper method updates the player info and property can pay label
	private void updatePlayerAndPropertyLabelInfo(Player player, JLabel canPayLabel)
	{
		player.updateInfo();
		canPayLabel.setText(CAN_PAY_RENT_STRING + canPay);
	}
	
	/**
	 * This method checks if the properties have the same name
	 * and type.
	 * @param other
	 * @return boolean
	 */
	public boolean equals(Property other)
	{
		if(other == null)
		{
			return false;
		}
		return name.equals(other.getName()) && type == other.getType();
	}
	
//	Property is a railroad
	public boolean isRailroad()
	{
		return type == TYPE_RAILROAD;
	}
	
//	Property is a utility
	public boolean isUtility()
	{
		return type == TYPE_UTILITY;
	}
	
	//Helper method to convert the type to a string to be used in toString()
	private String typeToString()
	{
		if(isValidType(type))
			return TYPES[type];
		else
			return INVALID_TYPE;
	}
	
	//Getters
	
	public String getName()
	{
		return name;
	}
	
	public int getRentPrice()
	{
		return rentPrice;
	}
	
	public int getPurchasePrice()
	{
		return purchasePrice;
	}
	
//	The mortgage price is always half the purchase price in monopoly
	public int getMortgagePrice()
	{
		return purchasePrice/2;
	}
	
	public int getType()
	{
		return type;
	}
	
//	The property can only be purchased if the owner exists
	public boolean isPurchased()
	{
		return owner != null;
	}
	
	public boolean canPay()
	{
		return canPay;
	}
	
	public Player getOwner()
	{
		return owner;
	}
	
	//Setters
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setRentPrice(int price)
	{
		this.rentPrice = price;
	}
	
	public void setPurchasePrice(int price)
	{
		this.purchasePrice = price;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public void setCanPay(boolean canPay)
	{
		this.canPay = canPay;
	}
	
	/**
	 * This method sets the owner of this property and sets the canPay to true.
	 * Use Player.addProperty(Property) {Instance method} to add this property to the owner, 
	 * that method contains checking and is advisable to use. That method also calls this
	 * method when it is executed.
	 * @param owner
	 */
	public void setOwner(Player owner)
	{
		this.owner = owner;
		canPay = true;
	}
	
	//ToString	
	public String toString()
	{
		return "Property: "+name+" of type "+typeToString()+", with rent of $"+rentPrice+", a purchase price of $"+purchasePrice+" and a mortgage price of $"+getMortgagePrice()+", is purchased "+isPurchased()+" and can pay rent "+canPay;
	}
}
