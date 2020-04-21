import java.util.ArrayList;
import javax.swing.JLayeredPane;

public class CardStack extends JLayeredPane
{
	Card bottom;
	ArrayList<Card> pile;
	int stackType;			//If 0, a regular stack; if 1, a free cell(top left stack); if 2, a foundation (top right stack)
	
	public CardStack(int type)
	{
		bottom = new Card("blank", 0);
		
	}
}