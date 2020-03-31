import javax.swing.*;
import java.awt.*;

public class Card extends JPanel {

    public final String suit;
    public final int value;
    private BufferedImage front;
    private BufferedImage back;
    private boolean faceDown;
    private boolean isBlack;

    public Card(String suit, int value) {
        // Construct a card
        this.value = value;
        this.suit = suit;
        faceDown = false;
    }

    public String getSuit() {
        return this.suit;
    }

    public String getValAsString() {
        // Turns the value of the card into a string
        switch (this.value) {
            case 14:
                return "Ace";
                break;
            case 13:
                return "King";
                break;
            case 12:
                return "Queen";
                break;
            case 11:
                return "Jack";
                break;
            default:
                return Integer.toString(this.value);
        }
    }

    public String toString() {
        return this.getValAsString() + " of " + this.suit;
    }
}