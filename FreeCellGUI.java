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
import java.awt.Component;
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
import javax.swing.BoxLayout;


public class FreeCellGUI extends JFrame implements MouseListener, MouseMotionListener
{
	JButton[] options;			//Holds the menu of options the player can select
	Deck deck;				
	JPanel menuBar;
	JPanel gameArea;
	JPanel topRow;
	JPanel columns;
	ArrayList<CardStack> freeCells;
	ArrayList<CardStack> homeCells;
	ArrayList<CardStack> playingField;
								//The above correspond to the top left free cells, the top right completed sets of cards, and the main 8 columns where the game is played
	CardStack dragged;			//When a pile is dragged around, it is added to this
	JLayeredPane jlp;
	Point offset;				//Helps properly align where the card will be dragged to
	Card tCard;
	
	public FreeCellGUI()
	{
 		super("Free Cell Solitaire");
		setLayout(new BorderLayout());
		//Adds the bottom menu bar for getting help on playing the game and restarting the game
		String[] theOptions = new String[]{"Help", "New Game"};
		options = new JButton[2];
		for(int i = 0; i < 2; i++)
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
		
		gameArea = new JPanel();
		gameArea.setOpaque(false);
		gameArea.setLayout(new BoxLayout(gameArea, BoxLayout.PAGE_AXIS));
		
		menuBar = new JPanel(new GridLayout(0,2));
		menuBar.add(options[0]);
		menuBar.add(options[1]);
		add(menuBar, BorderLayout.SOUTH);
		
		//Adds the free cells and foundations to the game board
		FlowLayout tFlow = new FlowLayout(FlowLayout.CENTER);
		tFlow.setAlignOnBaseline(true);
		topRow = new JPanel(tFlow);
		add(topRow, FlowLayout.LEFT);
		topRow.setOpaque(true);
		topRow.setVisible(true);
		topRow.setSize(new Dimension(2160, 150));
		topRow.setBackground(Color.red);
		gameArea.add(topRow);
		//topRow.setBackground(new Color(30, 80, 25));
		
		//Adds the main game area, where the cards are laid out into 8 columns
		FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
		flow.setAlignOnBaseline(true);
		columns = new JPanel(flow);
		columns.setOpaque(true);
		columns.setMinimumSize(new Dimension(200, 900));
		columns.setBackground(Color.blue);
		gameArea.add(columns);
		columns.setVisible(true);
		//columns.setBackground(new Color(30, 80, 25));
		
		//Other changes to the JFrame, such as the background color, are done here
		jlp = getLayeredPane();
		add(gameArea);
		playingField = new ArrayList<CardStack>();
		freeCells = new ArrayList<CardStack>();
		homeCells = new ArrayList<CardStack>();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		deck = new Deck();
		setMinimumSize(new Dimension(1080, 720));
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
		int temp;
		CardStack[] s = new CardStack[8];
		Card card;
		topRow.removeAll();
		columns.removeAll();
		
		for(int i = 0; i < 8; i++)
		{
			s[i] = new CardStack(0, 120);
			s[i].addCard(new Card("blank", 0));
			if(i < 4)
				temp = 7;
			else
				temp = 6;
			
			for(int j = 0; j < temp; j++)
			{
				card = deck.drawCard();
				card.addMouseListener(this);
				card.addMouseMotionListener(this);
				s[i].addCard(card);
			}
			playingField.add(s[i]);
			columns.add(s[i]);
		}
		
 		for(int i = 0; i < 8; i++)
		{
			if(i < 4)
				s[i] = new CardStack(1, 120);	
			else
				s[i] = new CardStack(2, 120);
			
			s[i].addCard(new Card("blank", 0));
			
			if(i < 4)
				freeCells.add(s[i]);
			else
				homeCells.add(s[i]);
			
			topRow.add(s[i]);
		}
		
/* 		for(int i = 0; i < 8; i++)
			topRow.add(new Card("blank", 0)); */
		
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
			dragged.setLocation(pos);
			System.out.print("d");
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
		if(e.getComponent() instanceof Card)
		{
			Card c = (Card)e.getComponent();
			offset = e.getPoint();
			
			if(getComponentAt(offset) == columns)
			{
				if(playingField.get(playingField.indexOf(c.getParent())).getLayer(c) != JLayeredPane.DRAG_LAYER)
					return;
				CardStack s = (CardStack)c.getParent();
				if(s.isEmpty() || s.stackType == 2)
					return;
				
				dragged = new CardStack(s.stackType, s.width);
				dragged.addCard(c);
				s.removeCard(c);
				dragged.old = s;
				playingField.add(dragged);
				
				jlp.add(dragged, JLayeredPane.DRAG_LAYER);
				
				Point pos = getLocationOnScreen();
				pos.x = e.getLocationOnScreen().x - pos.x - offset.x;
				pos.y = e.getLocationOnScreen().y - pos.y - offset.y;
				dragged.setLocation(pos);
				
				repaint();
				System.out.print("p");
			}
			else
			{
				Component comp = topRow.getComponent(0);
				if (comp instanceof CardStack)
				{
					if(((CardStack)comp).stackType == 1)
					{
						CardStack s = (CardStack)c.getParent();
						if(s.isEmpty() || s.stackType == 2)
							return;
						
						dragged = new CardStack(s.stackType, s.width);
						dragged.addCard(c);
						s.removeCard(c);
						dragged.old = s;
						playingField.add(dragged);
						
						jlp.add(dragged, JLayeredPane.DRAG_LAYER);
						
						Point pos = getLocationOnScreen();
						pos.x = e.getLocationOnScreen().x - pos.x - offset.x;
						pos.y = e.getLocationOnScreen().y - pos.y - offset.y;
						dragged.setLocation(pos);
						
						repaint();
						System.out.print("p");
					}
				}
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
				
				if(r.contains(mPos) && s.acceptableMove(dragged.pile.get(0)))
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
			
			System.out.print("r");
		}
	}
	
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	

}