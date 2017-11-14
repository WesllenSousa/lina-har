package view.manualviews;

import datasets.generic.GenericRowBean;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import constants.ConstGeneral;
import constants.ConstImagens;
import util.ImagemUtil;

/**
 *
 * @author Wesllen Sousa Lima
 */
public class PainelBox extends JPanel implements MouseListener {

    private JPopupMenu popup;
    private BufferedImage bufferedImage;

    private LinkedList<GenericRowBean> data;
    private LinkedList<GenericRowBean> dataFeatures = new LinkedList<>();
    private LinkedList<String> classes = new LinkedList<>();

    private String nameDataset;
    private List<String> signalSelection, filters, dataFusion, principalFeatures, timeFeatures, frequencyFeatures;

    public PainelBox(JPopupMenu popup, String nameDataset) {
        ConstGeneral.CURRENT_BOX = this;
        this.popup = popup;

        this.nameDataset = nameDataset;

        this.addMouseListener(this);
        this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        corNormal();

        bufferedImage = ImagemUtil.ler(getClass().getResourceAsStream(ConstImagens.box));
        repaint();
    }

    public void configData(LinkedList<GenericRowBean> data) {
        this.data = data;
    }

    public void updateConfigLists(List<String> signalSelection, List<String> filters, List<String> dataFusion,
            List<String> principalFeatures, List<String> timeFeatures, List<String> frequencyFeatures) {
        this.signalSelection = signalSelection;
        this.filters = filters;
        this.dataFusion = dataFusion;
        this.principalFeatures = principalFeatures;
        this.timeFeatures = timeFeatures;
        this.frequencyFeatures = frequencyFeatures;
    }

    public void cleanLists() {
        data.clear();
        dataFeatures.clear();
        classes.clear();
        data = null;
        dataFeatures = null;
        classes = null;
        System.gc();
    }

    private void corNormal() {
        setBackground(new Color(204, 204, 204)); //cinza claro
    }

    private void corInativo() {
        setBackground(new Color(255, 102, 102));//vermelho claro
    }

    private void corLigado() {
        setBackground(new Color(116, 211, 135)); //verde claro
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ConstGeneral.CURRENT_BOX = this;
        ConstGeneral.TELA_PRINCIPAL.updateConfigList(nameDataset, signalSelection,
                filters, dataFusion, principalFeatures, timeFeatures, frequencyFeatures);
        if (e.getButton() == MouseEvent.BUTTON3) {
            popup.show(e.getComponent(), e.getX(), e.getY());
        } else if (e.getButton() == MouseEvent.BUTTON1) {
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBorder(new BevelBorder(BevelBorder.RAISED));
        corInativo();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        corNormal();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bufferedImage != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(bufferedImage, 5, 5, bufferedImage.getWidth(), bufferedImage.getHeight(), this);
        }
    }

    /*
        Getters e Setters
     */
    public LinkedList<GenericRowBean> getData() {
        return data;
    }

    public void setData(LinkedList<GenericRowBean> data) {
        this.data = data;
    }

    public LinkedList<GenericRowBean> getDataFeatures() {
        return dataFeatures;
    }

    public void setDataFeatures(LinkedList<GenericRowBean> dataFeatures) {
        this.dataFeatures = dataFeatures;
    }

    public String getNameDataset() {
        return nameDataset;
    }

    public void setNameDataset(String nameDataset) {
        this.nameDataset = nameDataset;
    }

    public LinkedList<String> getClasses() {
        return classes;
    }

    public void setClasses(LinkedList<String> classes) {
        this.classes = classes;
    }

}
