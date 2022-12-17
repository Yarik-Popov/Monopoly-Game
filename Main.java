// imports
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Yarik Popov & Kirill Dariy <br>
 * ICS4U1, Mr. Naccarato <br>
 * Jan 16, 2022 <br>
 * This class creates a window to take in the input for the number of players in the 
 * game. Then it creates the game and plays it. 
 */
class Main
{
	//Class variable used to check the number of player playing to pass into game constructor
    public static int userSelection = -1;

    //Creates menu and game frames
    public static void main(String[] args)
    {
//        Create menu and x position and width
        JFrame menu = new JFrame();
        int xpos = 200;
        int xwid = 200;

//        Text explaining number of players
		JLabel standardText = new JLabel("How many players do you want to participate in this game?");
		standardText.setBounds(150,20,380,30);

//		
        JButton player2 = new JButton("2 player");
        player2.setBounds(xpos,140,xwid,30);

		JButton player3 = new JButton("3 player");
        player3.setBounds(xpos,190,xwid,30);

		JButton player4 = new JButton("4 player");
        player4.setBounds(xpos,240,xwid,30);

		JButton exit = new JButton("Exit the game");
        exit.setBounds(xpos,290,xwid,30);

		menu.add(standardText);
        menu.add(player2);
		menu.add(player3);
		menu.add(player4);
        menu.add(exit); // this button does not show, however, when you swap it with player 2 for ex. then it appears
		
        
		menu.setVisible(true);

        

        ////////////////////////
        // Creating the frame //
        ////////////////////////

        
        menu.setSize(600,400);
        menu.setLocationRelativeTo(null);
        menu.setLayout(null);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //////////////////////////
        // All Action Listeners //
        //////////////////////////

        player2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) 
            { 
                userSelection = 2;
            } 
        });

        player3.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) 
            { 
                userSelection = 3;
            } 
        });

        player4.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) 
            { 
                userSelection = 4;
            } 
        });

        exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) 
            { 
                menu.dispose();
            } 
        });

        // Check if the player has launched the game
        while(true)
        {
            System.out.print(""); 
            if (userSelection >= 2 && userSelection <= 4)
            {
                // Close the menu window
                menu.dispose();
                // Make the game object and play
                Game game = new Game(userSelection);
                
                game.play();
				break;
                
            }
		}
       
    }

}