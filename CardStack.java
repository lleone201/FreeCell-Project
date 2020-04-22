import java.util.ArrayList;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLayeredPane;

public class CardStack extends JLayeredPane
{
	ArrayList<Card> pile;
	int stackType;			//If 0, a regular stack; if 1, a free cell(top left stack); if 2, a home cell (top right stack)
	public int width, height;	//The horizontal and vertical lengths of the pane when added to the game
	private int offset;			//The height between the top of a card and the top of the card above it
	private int capacity;		//The capacity is 1 for free cells, 13 for home cells(to hold a full suit of cards), and effectively 52 for regular piles (only as a failsafe)
	public CardStack old;		//If a card that was dragged failed to be added, it will return to the stack it was formerly attached to
	
	public CardStack(int t, int w)
	{
		pile = new ArrayList<Card>();
		offset = 20;				//The first non-blank card in a pile is directly on top of the blank card
		stackType = t;
		width = w;
		if(stackType == 1)
			capacity = 1;
		else if(stackType == 2)
			capacity = 13;
		else
			capacity = 52;
	}
	
	public void addCard(Card c)
	{
		c.setLocation(0, offset * pile.size());
		pile.add(c);
		this.add(c, 1, 0);
/* 		if(pile.size() == 2 && stackType == 0)
			offset = 15;	//After the first non-blank card is added, the offset is updated */
		resize();
	}
	
	public void removeCard(Card c)
	{
		pile.remove(c);
		resize();
	}
	
	public void resize()
	{
		height = 145;
		if(pile.size() != 0)
			height += offset * (pile.size());
		this.setPreferredSize(new Dimension(width, height));
/* 		for(int i = 0; i < pile.size(); i++)
			putLayer(pile.get(i), DEFAULT_LAYER);
		putLayer(pile.get(pile.size() - 1), DRAG_LAYER); */
	}
	
	public boolean isEmpty()
	{
		return pile.size() == 0;
	}
	
 	public CardStack splitStack(Card first)
	{
		CardStack temp = new CardStack(stackType, 100);
		for(int i = pile.indexOf(first); i < pile.size(); i++)
		{
			temp.addCard(pile.get(i));
			removeCard(pile.get(i));
		}
		
		temp.old = this;
		return temp;
	}
	
	public void uniteStacks(CardStack p)
	{
		for(int i = 0; i < p.pile.size(); i++)
		{
			addCard(p.pile.get(i));
		}
		resize();
	}
	
	public Card peekTop()
	{
		for(int i = 0; i < pile.size(); i++)
		{
			if(getLayer(pile.get(i)) == DRAG_LAYER)
				return pile.get(i);
		}
		return null;
	}
	
	public boolean acceptableMove(Card c)
	{	
		Card topCard;
		
		switch(stackType)
		{
			case 0:			//If the card stack is empty, the card can be added; if not, the card must be 1 higher and the alternate color
				if(isEmpty())
					return true;
				else
				{
					topCard = peekTop();
					if(topCard.getColor() != c.getColor() && topCard.value + 1 == c.value)
						return true;
					else
						return false;
				}
			case 1:			//If the free cell is empty, the card can be added
				if(pile.size() == capacity)
					return false;
				else
					return true;
			case 2:				//The card can be added if the home cell is empty and it is an ace; if not, the card must be 1 higher and the same suit
				if(isEmpty() && c.value == 14)
					return true;
				if(pile.size() == c.value)
				{
					if(pile.get(1).suit.equals(c.suit))
						return true;
					else
						return false;
				}
					
		}
		return false;
	}
	
	@Override
	public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
	    return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
	}
	
	@Override
	public int getBaseline(int width, int height) {
	    return 0;
	}
	
	public String toString()
	{
		String result = new String();
		for(int i = 0; i < pile.size(); i++)
		{
			result += pile.get(i).toString() + " ";
		}
		
		result += "\n";
		return result;
	}
	
}