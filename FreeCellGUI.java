import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Desktop;
import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class FreeCellGUI extends JFrame implements MouseListener, MouseMotionListener
{
	JButton[] options;			//Holds the menu of options the player can select
	Deck deck;				
	JPanel menuBar;
	JPanel topRow;
	JPanel columns;
	ArrayList<CardStack> freeCells;
	ArrayList<CardStack> homeCells;
	ArrayList<CardStack> playingField;
								//The above correspond to the top left free cells, the top right completed sets of cards, and the main 8 columns where the game is played
	CardStack dragged;			//When a pile is dragged around, it is added to this
	Point offset;				//Helps properly align where the card will be dragged to
	
	public FreeCellGUI()
	{
		super("Free Cell Solitaire");
		//Adds the bottom menu bar for getting help on playing the game and restarting the game
		String[] theOptions = new String[]{"Help", "New Game"};
		options = new JButton[2];
		for(int i = 0; i < 3; i++)
		{
			options[i] = new JButton(theOptions[i]);
		}
		
		options[0].addActionListener(new ActionListener()	//The button for asking how to play
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int findHelp = JOptionPane.showConfirmDialog(null, "Access the Internet to learn how to play free cell solitaire?", "Help", JOptionPane.YES_NO_OPTION);
				if(findHelp == JOptionPane.YES_OPTION)
				{
					try
					{
						Desktop.getDesktop().browse(new URL("http://www.solitairecity.com/FreeCell.shtml").toURI());
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				else
					JOptionPane.getRootFrame().dispose();
			}
		});
		
		options[1].addActionListener(new ActionListener()	//The button for restarting the game
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int reset = JOptionPane.showConfirmDialog(null, "Would you like to start a new game from the beginning?", "New Game", JOptionPane.YES_NO_OPTION);
				if(reset == JOptionPane.YES_OPTION)
				{
					gameReset();
				}
				else
				{
					JOptionPane.getRootFrame().dispose();
				}
			}
		});
		
		menuBar = new JPanel();
		menuBar.setLayout(new GridLayout(1, 2, getWidth() / 4, 0));
		menuBar.add(options[0]);
		menuBar.add(options[1]);
		add(menuBar, BorderLayout.SOUTH);
		
		//Adds the free cells and foundations to the game board
		topRow = new JPanel();
		topRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(topRow, BorderLayout.NORTH);
		
		//Adds the main game area, where the cards are laid out into 8 columns
		columns = new JPanel();
		columns.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(columns, BorderLayout.CENTER);
		
		//Other changes to the JFrame, such as the background color, are done here
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		deck = new Deck();
		setMinimumSize(new Dimension(600, 900));
		setBackground(new Color(30, 80, 25));
		gameStart();
	}
	
	public void gameReset()	//Called to reset the game
	{
		deck = new Deck();
		
		gameStart();
		repaint();
	}
	
	public void gameStart() //Called to start the game, whether for the first time or after a reset
	{
		int j;
		CardStack s;
		topRow.removeAll();
		columns.removeAll();
		
		for(Card i : deck.cards)
		{
			i.addMouseListener(this);
			i.addMouseMotionListener(this);
		}
		
		for(int i = 0; i < 8; i++)
		{
			s = new CardStack(0, 120);
			if(i < 4)
				j = 7;
			else
				j = 6;
			for(; j > 0; j--)
			{
				Card card = deck.drawCard();
				s.add(card);
			}
			playingField.add(s);
			columns.add(s);
		}
		
		for(int i = 0; i < 8; i++)
		{
			if(i < 3)
				s = new CardStack(1, 120);	
			else if(i == 3)
				s = new CardStack(1, 240);	//The last of the free cells is wider to create space between them and the home cells
			else
				s = new CardStack(2, 120);
			
			if(i < 4)
				freeCells.add(s);
			else
				homeCells.add(s);
			
			topRow.add(s);
		}
		
		validate();
	}
	
	public boolean winCondition()
	{
		for(CardStack s : homeCells)
		{
			if(s.pile.size() != 14)
				return false;
		}
		
		return true;
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(dragged != null)
		{
			Point pos = getLocationOnScreen();
			offset = e.getPoint();
			pos.x = e.getLocationOnScreen().x - pos.x - offset.x;
			pos.y = e.getLocationOnScreen().y - pos.y - offset.y;
		}
		repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{

	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if(e.getComponent() instanceof CardStack)
		{
			int idx = playingField.indexOf((CardStack)e.getComponent());
			if(e.getComponent() instanceof Card)
			{
				Card c = (Card)e.getComponent();
				if(playingField.get(idx).getLayer(c) != JLayeredPane.DRAG_LAYER)
					return;
				
				CardStack s = (CardStack)c.getParent();
				if(s.isEmpty() || s.stackType == 2)
					return;
				
				dragged = new CardStack(s.stackType, s.width);
				dragged = s.splitStack(c);
				
				Point pos = getLocationOnScreen();
				offset = e.getPoint();
				pos.x = e.getLocationOnScreen().x - pos.x - offset.x;
				pos.y = e.getLocationOnScreen().y - pos.y - offset.y;
				
				dragged.setLocation(pos);
				
				repaint();
			}
		}
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(dragged != null)
		{
			boolean cardDrop = false;
			Point mPos = e.getLocationOnScreen();
			ArrayList<CardStack> temp = new ArrayList<CardStack>();
			for(int i = 0; i < 8; i++)
			{
				temp.add(playingField.get(i));
				if(i < 4)
				{
					temp.add(freeCells.get(i));
					temp.add(homeCells.get(i));
				}
			}
			
			for(CardStack s : temp)
			{
				Point sPos = s.getLocationOnScreen();
				Rectangle r = s.getBounds();
				r.x = sPos.x;
				r.y = sPos.y;
				
				if(r.contains(mPos) && s.acceptableMove(dragged.pile.get(1)))
				{
					s.uniteStacks(dragged);
					cardDrop = true;
					break;
				}
			}
			
			if(cardDrop == false)
				dragged.old.uniteStacks(dragged);
			
			dragged = null;
			repaint();
			
			if(winCondition())
				JOptionPane.showMessageDialog(this, "Congratulations!");
		}
	}
	
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	

}