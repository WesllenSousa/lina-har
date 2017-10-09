package main;

import com.jtattoo.plaf.mint.MintLookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import util.Messages;
import view.Principal;

/**
 * Created by kevin on 13/09/2016.
 */
public class Run {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new MintLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            Messages msg = new Messages();
            msg.bug(ex.toString());
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Principal handleData = new Principal();
                handleData.setVisible(true);
            }
        });

    }
}
