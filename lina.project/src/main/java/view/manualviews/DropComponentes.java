package view.manualviews;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 * @author Wesllen Sousa Lima
 */
public class DropComponentes extends MouseAdapter implements MouseMotionListener {

    private int baseX = -1;
    private int baseY = -1;

    private int x;
    private int y;

    private JPanel painel;

    public DropComponentes() {
    }

    public DropComponentes(JPanel painel) {
        this.painel = painel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Component b = e.getComponent();
        if ((this.baseX != -1) && (this.baseY != -1)) {
            x = b.getX() + e.getX() - this.baseX;
            y = b.getY() + e.getY() - this.baseY;
            b.setLocation(x, y);
            b.getParent().repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.baseX = e.getX();
        this.baseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (painel != null && painel instanceof PainelBox) {
        }
        this.baseX = -1;
        this.baseY = -1;
    }

}
