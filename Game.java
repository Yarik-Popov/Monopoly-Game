// Imports
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * Jan 22, 2022 <br>
 * This is the game class. It holds the arraylist of players and tiles and variables of 
 * dice. This is the class where the users play monopoly inside of.
 */
public class Game
{
    //Class Constants
	public static final ImageIcon BOARD = new ImageIcon(Player.IMAGE_PATH+"900xBoard"+Player.IMAGE_FILE_TYPE);
	public static final int X_BOARD = 10;
	public static final int Y_BOARD = 10;
	public static final String SONGFILE_STRING = ".//Best Song in The World.wav";
	private static Clip clip;
	
	//Instance variables
	private ArrayList<Player> players;
	private ArrayList<Tile> tiles; 
	private int currentPlayer;
	private Dice diceLeft;
	private Dice diceRight;
	private JButton roll;
	private JButton mute;
	private int recursionDepth;
	private JFrame frame;
	
	/**
	 * The constructor creates a game object based on the numPlayers parameter.
	 * @param numPlayers
	 */
	public Game(int numPlayers)
	{
		//Initialize players, tiles and currentPlayer index
		players = new ArrayList<Player>();
		tiles = new ArrayList<Tile>();
		currentPlayer = 0;
		
		//Creates players and tiles adding them into their appropriated arraylist
		for(int i=0; i < numPlayers; i++)
			players.add(new Player(i));
		for(int i=0; i < Tile.LIST_OF_TILES.length; i++)
			tiles.add(new Tile(Tile.LIST_OF_TILES[i]));
		
		//Creates the left and right dice and the roll button
		diceLeft = new Dice(0);
		diceRight = new Dice(1);
		roll = new JButton("Roll");
		mute = new JButton("Mute");
		recursionDepth = 1;
		frame = new JFrame("Monopoly");
	}
	
	//Methods
	
	/**
	 * Plays the music file based on the music location parameter. This procedure is used
	 * by the play() method.
	 * @param musicLocation
	 */
  	public static void playMusic(String musicLocation)
  	{
//  		Tries to get the music location
  		try
  		{
  			File musicPath = new File(musicLocation);

//  			If the music file exists so play the song
  			if(musicPath.exists())
  			{
  				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
  				clip = AudioSystem.getClip();
  				clip.open(audioInput);
  				clip.start();
  			}
  		}
//  		The music file doesn't exist
  		catch(Exception e)
  		{
  			System.out.println("No music found");
  		}
  			
  	}
	
	/**
	 * This method goes to the next player. If the next player is the first
	 * player in the list then it goes through each player and reduces their jail time.
	 * It also checks if there is only 1 player left using recursion to see who is the winner.
	 */
	public void goToNextPlayer()
	{
		currentPlayer++;
		if(currentPlayer >= players.size())
		{
			currentPlayer = 0;
			for(Player p: players)
				p.reduceJailTime();
		}
		
//		Checks if only 1 player in list of players can play (Exit condition)
		if(recursionDepth >= players.size())
		{
			frame.dispose();
			
//			Finds the last player
			for(Player p: players)
				if(p.canPlay())
					JOptionPane.showMessageDialog(null, p.getName()+" won");
		}
		
//		Gets the next player 
		Player player = players.get(currentPlayer);
		
//		Using recursion run through the players until the exit condition is meet
		if(!player.canPlay())
		{
			recursionDepth++;
			player.getIndicatorLabelLost().setVisible(true);
			goToNextPlayer();
		}
		recursionDepth = 1;
	}
	
	/**
	 * This method is the main one of for this class it plays the game and calls all the
	 * other methods.
	 */
	public void play()
	{
		//Initialize frame
		frame.setLayout(null);
        // frame.setSize(1900,1000);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        //Start music
        playMusic(SONGFILE_STRING);
        
        //Draws the info and adds action listeners to button for each player
        for(Player p: players)
        {
        	p.drawInfo(frame);
        	p.addActionListeners(this);
        }
        
        //Board image
        JLabel boardLabel = new JLabel(BOARD);
        boardLabel.setBounds(10, 10, 900, 900);
        frame.add(boardLabel);
        
		//Sets the bounds of the roll and mute button
		roll.setBounds(960,780,60,112);
		mute.setBounds(1600,860,100,50);

		//Adds Action Listeners to roll
		roll.addActionListener(e -> 
		{
//			Gets the player based on the current player variable, updates its info
			Player player = players.get(currentPlayer);
			player.updateInfo();
			player.setIndicatorLabelVisible(true);
			diceLeft.getNewValue();
            diceRight.getNewValue();
			
			// If the player rolls a double
			if(diceLeft.getValue() == diceRight.getValue()) 
			{
				player.increaseRollDouble();
//				Player rolls 3 doubles and therefore goes to jail
				if(player.getRollDouble() == 3)
				{
					JOptionPane.showMessageDialog(null, "You rolled 3 doubles, so you go to jail");
					roll.setVisible(false);
					player.setJailTime();
				}
//				The player rolls a double and gets to roll again
				else
				{
					player.move(diceLeft.getValue()+diceRight.getValue());
					player.showBuyingButton(this);
					player.setButtonsVisible(true, Player.BUTTON_OWNED_PROPERTIES);
				}
			}
			// If the player rolls something other than a double
			else 
			{
//				Move the player and turn of rolling
				player.move(diceLeft.getValue()+diceRight.getValue());
				roll.setVisible(false);
				
//				Displays the buttons
				player.setButtonsVisible(true);
				player.showBuyingButton(this);
			}
			
//			Gets the current location of the player, tile, creates window and gets new type.
			int currentLocation = player.getLocation();
			Tile tile = tiles.get(currentLocation);
			tile.createWindow(player);
			
//			Checks if the player move and does this the tile event
			if(currentLocation != player.getLocation())
			{
				Tile anotherTile = tiles.get(player.getLocation());
				anotherTile.createWindow(player);
				player.showBuyingButton(this);
			}
			
//			Updates all the players info
			updateAllPlayerInfo();
		});
		
//		Adds action listener to mute button
		mute.addActionListener(e -> {
//		Messages asking the player if they want to mute the music
			JOptionPane.showMessageDialog(null, "Are you sure you want to end this magnificent music?");
			JOptionPane.showMessageDialog(null, "Really?????????");
			JOptionPane.showMessageDialog(null, "OK FINE. Just click ok here");
			clip.close();
		});

		//Adds the dice and roll to the frame
		diceLeft.addToFrame(frame);
		diceRight.addToFrame(frame);
		frame.add(roll);
		frame.add(mute);
		
		//Sets default close and visible of frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
//	Updates all the player info
	public void updateAllPlayerInfo()
	{
		for(Player p: players)
			p.updateInfo();
	}
	
    //Getters
    
    public ArrayList<Player> getPlayers()
    {
    	return players;
    }
    
    public ArrayList<Tile> getTiles()
    {
    	return tiles;
    }
    
    public int getCurrentPlayer() {
		return currentPlayer;
	}
    
    public JButton getRoll()
    {
    	return roll;
    }
    
  //Setters

	public int getRecursionDepth() {
		return recursionDepth;
	}

	public void setRecursionDepth(int recursionDepth) {
		this.recursionDepth = recursionDepth;
	}

	public void setPlayers(ArrayList<Player> players)
    {
    	this.players = players;
    }
    
    public void setTiles(ArrayList<Tile> tiles)
    {
    	this.tiles = tiles;
    }
    
    public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
    
    public void setRoll(JButton roll)
    {
    	this.roll = roll;
    }
    
    //toString
    public String toString()
    {
    	return "Players: "+players.toString();//+". Tiles: "+tiles.toString();
    }
    
 
    
}