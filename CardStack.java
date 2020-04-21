import java.util.ArrayList;
import java.awt.Dimension;
import javax.swing.JLayeredPane;

public class CardStack extends JLayeredPane
{
	Card bottom;			//The first card added to a stack will always be this one, which is blank
	ArrayList<Card> pile;
	int stackType;			//If 0, a regular stack; if 1, a free cell(top left stack); if 2, a home cell (top right stack)
	int width, height;		//The horizontal and vertical lengths of the pane when added to the game
	int offset;				//The height between the top of a card and the top of the card above it
	int capacity;			//The capacity is 1 for free cells, 13 for home cells(to hold a full suit of cards), and effectively 52 for regular piles (only as a failsafe)
	CardStack old;			//If a card that was dragged failed to be added, it will return to the stack it was formerly attached to
	
	public CardStack(int t, int w)
	{
		bottom = new Card("blank", 0);
		pile = new ArrayList<Card>();
		pile.add(bottom);
		offset = 0;			//The first non-blank card in a pile is directly on top of the blank card
		type = t;
		width = w;
		if(type == 1)
			capacity = 1;
		else if(type == 2)
			capacity == 13;
		else
			capacity == 52;
	}
	
	public void add(Card c)
	{
		c.setLocation(0, offset * pile.size());
		pile.add(c);
		this.add(c, 1, 0);
		if(pile.size() == 2 && type == 0)
			offset = 21;	//After the first non-blank card is added, the offset is updated
		resize();
	}
	
	public void remove(Card c)
	{
		pile.remove(c);
		this.remove(c);
		resize();
	}
	
	public void resize()
	{
		height = 145;
		if(cards.size() != 0)
			height += offset * (cards.size() - 2);
		this.setSize(width, height);
		for(int i = 0; i < pile.size(); i++)
			pile.putLayer(i, DEFAULT_LAYER);
		pile.putLayer(pile.size() - 1, DRAG_LAYER);
	}
	
	public boolean isEmpty()
	{
		return pile.size() - 1 == 0;
	}
	
 	public CardStack splitStack(Card first)
	{
		CardStack temp = new CardStack(type, 100);
		for(int i = pile.indexOf(first); i < cards.size();)
		{
			temp.add(pile.get(i));
			remove(pile.get(i));
			if(temp.size() == 1)
				temp.offset = 21;
		}
		
		temp.old = this;
		return temp;
	}
	
	public void uniteStacks(Pile p)
	{
		for(Card c : p.pile)
		{
			add(c);
		}
		resize();
	}
	
	public Card peekTop()
	{
		for(int i = 0; i < pile.size(); i++)
		{
			if(pile.get(i).getLayer() == DRAG_LAYER)
				return pile.get(i);
		}
	}
	
	public boolean acceptableMove(Card c)
	{
		if(this == p)
			return false;
		
		Card topCard;
		
		switch(type)
		{
			case 0:			//If the card stack is empty, the card can be added; if not, the card must be 1 higher and the alternate color
				if(size() == 1)
					return true;
				else
				{
					topCard = peekTop();
					if(topCard.isBlack != c.isBlack && topCard.value + 1 == c.value)
						return true;
					else
						return false;
				}
			case 1:			//If the free cell is empty, the card can be added
				if(size() == capacity)
					return false;
				else
					return true;
			case 2:			//The card can be added if the home cell is empty and it is an ace; if not, the card must be 1 higher and the same suit
				if(pile.size() == c.value)
				{
					if(pile.size() == 1)
						return true;
					else
					{
						if(pile.get(2).suit.equals(c.suit))
							return true;
						else
							return false;
					}
				}
					
		}
	}
	
}