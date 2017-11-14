package util;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 *
 * @author Wesllen Sousa Lima
 */
public class SwingUtil {

    public static void limparCamposJTextField(JPanel jPanel) {
        Component[] components = jPanel.getComponents();
        JTextField jTextField = null;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JTextField) {
                jTextField = (JTextField) components[i];
                jTextField.setText("");
            }
        }
    }

    public static void limparCamposJTextArea(JPanel jPanel) {
        Component[] components = jPanel.getComponents();
        JTextArea jTextArea = null;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JTextArea) {
                jTextArea = (JTextArea) components[i];
                jTextArea.setText("");
            }
        }
    }

    public static void desabilitarCamposJTextField(JPanel jPanel) {
        Component[] components = jPanel.getComponents();
        JTextField jTextField = null;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JTextField) {
                jTextField = (JTextField) components[i];
                jTextField.setEditable(false);
            }
        }
    }

    public static void abilitarCamposJTextField(JPanel jPanel) {
        Component[] components = jPanel.getComponents();
        JTextField jTextField = null;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JTextField) {
                jTextField = (JTextField) components[i];
                jTextField.setEditable(true);
            }
        }
    }

    public static void selectTextInJTextField(JTextField textField) {
        textField.setSelectionStart(0);
        textField.setSelectionEnd(textField.getText().length());
    }

    public static void fechaTabbedPanePeloTitulo(JTabbedPane tabbedPane, String titulo) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String t = tabbedPane.getTitleAt(i);
            if (titulo.equals(t)) {
                tabbedPane.remove(i);
            }
        }
    }

    public static void desabilitarSelecaoToggleButton(JPanel panel) {
        Component[] components = panel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JToggleButton) {
                JToggleButton toggleButton = (JToggleButton) components[i];
                toggleButton.setSelected(false);
            }
        }
    }

    public static String changePickListLeftRight(JList left, JList right) {
        String value = null;
        if (!left.isSelectionEmpty()) {
            value = left.getSelectedValue().toString();
            Integer indice = left.getSelectedIndex();
            ((DefaultListModel) right.getModel()).addElement(left.getSelectedValue());
            ((DefaultListModel) left.getModel()).removeElementAt(indice);
            left.clearSelection();
        }
        return value;
    }

    public static String changePickListRightLeft(JList right, JList left) {
        String value = null;
        if (!right.isSelectionEmpty()) {
            value = right.getSelectedValue().toString();
            Integer indice = right.getSelectedIndex();
            ((DefaultListModel) left.getModel()).addElement(right.getSelectedValue());
            ((DefaultListModel) right.getModel()).removeElementAt(indice);
            right.clearSelection();
        }
        return value;
    }

    public static void changePickListLeftRightAll(JList left, JList right) {
        int size = left.getModel().getSize();
        for (int i = 0; i < size; i++) {
            ((DefaultListModel) right.getModel()).addElement(left.getModel().getElementAt(i));
        }
        for (int i = 0; i < size; i++) {
            ((DefaultListModel) left.getModel()).removeElementAt(0);
        }
        left.clearSelection();
    }

    public static void changePickListRightLeftAll(JList right, JList left) {
        int size = right.getModel().getSize();
        for (int i = 0; i < size; i++) {
            ((DefaultListModel) left.getModel()).addElement(right.getModel().getElementAt(i));
        }
        for (int i = 0; i < size; i++) {
            ((DefaultListModel) right.getModel()).removeElementAt(0);
        }
        right.clearSelection();
    }

    public static void fillPickListRight(JList listRight, JList listLeft, List<String> values) {
        DefaultListModel defaultListModel = new DefaultListModel<>();
        for (String s : values) {
            defaultListModel.addElement(s);
            ((DefaultListModel) listLeft.getModel()).removeElement(s);
            listRight.clearSelection();
        }
        listRight.setModel(defaultListModel);
    }

    public static void listFilesList(JList list, String dir) {
        HashSet<String> files = FileUtil.listFiles(new File(dir));
        DefaultListModel defaultListModel = new DefaultListModel<>();
        for (String file : files) {
            defaultListModel.addElement(file);
        }
        list.setModel(defaultListModel);
    }

    public static void fillList(JList<String> list, LinkedList<String> columns) {
        DefaultListModel defaultListModel = new DefaultListModel<>();
        for (String s : columns) {
            defaultListModel.addElement(s);
        }
        list.setModel(defaultListModel);
    }

    public static List<String> getElementsList(JList list) {
        ArrayList<String> values = new ArrayList<>();
        Enumeration enumeration = ((DefaultListModel) list.getModel()).elements();
        while (enumeration.hasMoreElements()) {
            values.add(enumeration.nextElement().toString());
        }
        return values;
    }
    
    public static Comparator getComparadorString() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                String valor1 = null;
                String valor2 = null;
                try {
                    valor1 = (String) o1;
                    valor2 = (String) o2;
                } catch (Exception e) {
                    System.out.println(e);
                }
                return valor1.compareTo(valor2);
            }
        };
        return comparator;
    }

}
