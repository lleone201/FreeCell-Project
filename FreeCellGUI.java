import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Desktop;
import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class FreeCellGUI extends JFrame implements MouseListener, MouseMotionListener
{
	JButton[] options;			//Holds the menu of options the player can select
	Deck deck;				
	JPanel menuBar, topRow, columns;
	ArrayList<CardStack> freeCells, foundations, playingField;		//These correspond to the top left free cells, the top right completed sets of cards, and the main 8 columns where the game is played
	
	public FreeCellGUI(Deck d)
	{
		//Adds the bottom menu bar for getting help on playing the game and restarting the game
		String[] theOptions = new String[]{"Help", "New Game"} 
		super("Free Cell Solitaire");
		options = new JButton[2];
		for(int i = 0; i < 3; i++)
		{
			options[i] = new JButton(theOptions[i]);
		}
		
		options[0].addActionListener(new ActionListener(){	//The button for asking how to play
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int findHelp = JOptionPane.showConfirmDialog(null, "Access the Internet to learn how to play free cell solitaire?", "Help", YES_NO_OPTION);
				if(findHelp == JOptionPane.YES_OPTION)
					Desktop.getDesktop().browse(new URL("https://en.wikipedia.org/wiki/FreeCell#Rules").toURI());
				else
					JOptionPane.getRootFrame().dispose();
			}
		}
		
		options[1].addActionListener(new ActionListener(){	//The button for restarting the game
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int reset = JOptionPane.showConfirmDialog(null, "Would you like to start a new game from the beginning?", "New Game", YES_NO_OPTION);
				if(reset == JOptionPane.YES_OPTION)
				{
					gameReset();
				}
				else
				{
					JOptionPane.getRootFrame().dispose();
				}
			}
		}
		
		menuBar = new JPanel();
		menuBar.setLayout(new GridLayout(1, 2, getWidth() / 4, 0));
		menuBar.add(options[0]);
		menuBar.add(options[1]);
		add(menuBar, BorderLayout.SOUTH);
		
		//Adds the free cells and foundations to the game board
		topRow = new JPanel;
		topRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(topRow, BorderLayout.NORTH);
		
		//Adds the main game area, where the cards are laid out into 8 columns
		columns = new JPanel();
		columns.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(columns, BorderLayout.CENTER);
		
		//Other changes to the JFrame, such as the background color, are done here
		setVisible(true);
		deck = d;
		setMinimumSize(new Dimension(600, 900));
		setBackground(30, 80, 25);
		gameStart();
	}
	
	public void gameReset()	//Called to reset the game
	{
		deck = new Deck();
		
		initialize();
		repaint();
	}
	
	public void gameStart() //Called to start the game, whether for the first time or after a reset
	{
		topRow.removeAll();
		columns.removeAll();
		
		for(Card i : deck.cards)
		{
			c.addMouseListener(this);
			c.addMouseMotionListener(this);
		}
	}
	
}