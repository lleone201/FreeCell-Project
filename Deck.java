import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    // Deck wil need cards obviously
    ArrayList<Card> cards;

    public int getDeckSize() {
        // Returns the current number of cards left in the deck
        return cards.size();
    }

    public void shuffleDeck() {
        // Uses the Collections.shuffle method to shuffle up the deck and get the cards
        // in randomized order
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        // Draws a card from the deck and then removes it from the cards in the deck.
        Card drawn = cards.get(0);
        cards.remove(0);

        return drawn;
    }

    // Deck constructor
    public Deck() {
        // Create 52 cards for the deck and shuffle them
        cards = new ArrayList<Card>();
        String suit = "";
        for (int i = 0; i < 4; ++i) {
            if (i == 0) {
                suit = "hearts";
            } else if (i == 1) {
                suit = "clubs";
            } else if (i == 2) {
                suit = "spades";
            } else if (i == 3) {
                suit = "diamonds";
            }
            for (int j = 2; j <= 14; ++j) {
                cards.add(new Card(suit, j));
            }
        }
        shuffleDeck();
    }

}