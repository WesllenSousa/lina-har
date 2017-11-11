package view.manualviews;

import datasets.generic.GenericRowBean;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import constants.ConstGeneral;
import view.viewControler.DesktopView;

/**
 *
 * @author Wesllen Sousa Lima
 */
public class InternalFrame extends JInternalFrame implements ActionListener, InternalFrameListener, ComponentListener {

    private DesktopView desktopView;

    private JMenuItem viewDataset;
    private JCheckBoxMenuItem checkBox;
    private JPanel graphic;

    private LinkedList<GenericRowBean> data;
    private LinkedList<GenericRowBean> dataFeatures = new LinkedList<>();
    private LinkedList<String> classes = new LinkedList<>();

    private String nameDataset;
    private List<String> signalSelection, filters, principalFeatures, timeFeatures, frequencyFeatures;

    public InternalFrame(DesktopView desktopView, String nameDataset) {
        ConstGeneral.CURRENT_INTERNAL_FRAME = this;
        this.desktopView = desktopView;
        this.nameDataset = nameDataset;
        iniciar();
    }

    public void configData(LinkedList<GenericRowBean> data) {
        this.data = data;
        addData();
    }

    private void addData() {
        ((LineGraphic) graphic).prepareStream(data);
        int row = 0;
        for (GenericRowBean bean : data) {
            if (row == 0) {
                row++;
                continue;
            } else {
                ((LineGraphic) graphic).addData(bean);
            }
            row++;
        }
    }

    private void addDataFeatures() {
        ((LineGraphic) graphic).prepareStream(dataFeatures);
        int row = 0;
        for (GenericRowBean bean : dataFeatures) {
            if (row == 0) {
                row++;
                continue;
            } else {
                ((LineGraphic) graphic).addData(bean);
            }
            row++;
        }
    }

    public void updateGraphic() {
        if (graphic != null) {
            if (dataFeatures.isEmpty()) {
                addData();
            } else {
                addDataFeatures();
            }
        }
    }

    private void iniciar() {
        this.getContentPane().setLayout(new BorderLayout());
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setResizable(true);
        this.setAutoscrolls(true);
        this.setVisible(true);

        setTitle(nameDataset);

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_A);

        viewDataset = new JMenuItem("View dataset");
        viewDataset.addActionListener(this);
        viewDataset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        file.add(viewDataset);

        JMenu options = new JMenu("Options");
        options.setMnemonic(KeyEvent.VK_B);

        checkBox = new JCheckBoxMenuItem("Check Box");
        checkBox.addActionListener(this);
        checkBox.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
        options.add(checkBox);

        JMenuBar barraMenu = new JMenuBar();
        barraMenu.add(file);
        barraMenu.add(options);
        setJMenuBar(barraMenu);

        this.addComponentListener(this);
        this.addInternalFrameListener(this);

        graphic = new LineGraphic(nameDataset, true);
        getContentPane().add(graphic, BorderLayout.CENTER);

        this.pack();
    }

    public void updateConfigLists(List<String> signalSelection, List<String> filters, List<String> principalFeatures,
            List<String> timeFeatures, List<String> frequencyFeatures) {
        this.signalSelection = signalSelection;
        this.filters = filters;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewDataset) {
            desktopView.editData();
        } else if (e.getSource() == checkBox) {

        }
        updateUI();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (graphic != null) {
            graphic.setPreferredSize(this.getSize());
            graphic.updateUI();
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        cleanLists();
        ConstGeneral.CURRENT_INTERNAL_FRAME = null;
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        ConstGeneral.CURRENT_INTERNAL_FRAME = this;
        ConstGeneral.TELA_PRINCIPAL.updateConfigList(nameDataset, signalSelection,
                filters, principalFeatures, timeFeatures, frequencyFeatures);
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
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

    public void setClasseFeatures(LinkedList<String> classes) {
        this.classes = classes;
    }

}
