import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TestCard {

    public static void main(String[] args) {
        JFrame frame = new JFrame("TestCard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Card jPanel = new Card("hearts", 2);
        jPanel.flipToBack();
        frame.add(jPanel);
        frame.setBackground(Color.WHITE);
        frame.pack();
        frame.setSize(100, 145);
        frame.setVisible(true);
    }

}