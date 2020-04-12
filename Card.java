import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.io.IOException;

public class Card extends JPanel {

    public final String suit;
    public final int value;
    private BufferedImage front;
    private BufferedImage back;
    private boolean faceDown;
    private boolean isBlack;

    // Constructor
    public Card(String suit, int value) {
        // Construct a card
        this.value = value;
        this.suit = suit;
        // Start the card out facing up
        this.faceDown = false;

        try {
            // Load the images for the front and back of the card
            // Have to use getClass().getResource() because otherwise it won't work out of a
            // .jar file.
            URL path = getClass().getResource("./cards/" + this.toString() + ".png");
            front = ImageIO.read(path);

            URL backPath = getClass().getResource("./cards/back.png");
            back = ImageIO.read(backPath);
            // Sets the size of the card to the size of the .png
            setBounds(0, 0, front.getWidth(), front.getHeight());
        } catch (IOException e) {
            // Do nothing
            e.printStackTrace();
        }
        setSize(100, 145);
        setOpaque(false);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage showCard = front;
        if (faceDown) {
            showCard = back;
        }

        g.drawImage(showCard, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public String getSuit() {
        return this.suit;
    }

    public String getValAsString() {
        // Turns the value of the card into a string
        if (this.value == 14) {
            return "Ace";
        } else if (this.value == 13) {
            return "King";
        } else if (this.value == 12) {
            return "Queen";
        } else if (this.value == 11) {
            return "Jack";
        } else {
            return Integer.toString(this.value);
        }
    }

    public int getValAsInt() {
        // Returns the integer value of the card
        return this.value;
    }

    public String toString() {
        return this.getValAsString() + " of " + this.suit;
    }
}