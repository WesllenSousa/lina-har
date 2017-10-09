package view.viewControler;

import datasets.generic.GenericRowBean;
import datasets.generic.HandleGenericDataset;
import java.io.File;
import java.util.LinkedHashSet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.filechooser.FileFilter;
import constants.ConstDataset;
import constants.ConstGeneral;
import util.FileUtil;
import util.Messages;
import util.SwingUtil;
import util.Validation;
import view.EditDataset;

/**
 *
 * @author Wesllen Sousa
 */
public class ComponentView {

    public String openFileChooser(JFrame frame, FileFilter filter) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File(ConstDataset.CURRENT_DIR));
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(true);
        Integer retorno = fileChooser.showDialog(frame, "Confirm");
        String currentFile = null;
        if (retorno == JFileChooser.OPEN_DIALOG) {
            ConstDataset.CURRENT_DIR = fileChooser.getSelectedFile().getParent() + File.separator;
            currentFile = fileChooser.getSelectedFile().getAbsolutePath();
        }
        return currentFile;
    }

    public void openEditView(JList list, String source) {
        LinkedHashSet<GenericRowBean> data = HandleGenericDataset.bufferFileInMemory(ConstDataset.SEPARATOR, source);
        HandleGenericDataset.setColumnClass(data);
        EditDataset view = new EditDataset(null, false, source, data);
        view.setVisible(true);
    }

    public void deleteDataset(JList list, String dir, String name) {
        Messages messages = new Messages();
        if (messages.confirmacao("Are you sure delete this file?")) {
            FileUtil.deleteFile(new File(dir + name));
            SwingUtil.listFilesList(list, dir);
        }
    }

    public void editDeleteDataset(JList list, String dir, int type) {
        if (list.isFocusable() && list.getSelectedIndex() != -1) {
            if (type == ConstGeneral.PP_EDIT) {
                openEditView(list, dir + list.getSelectedValue());
            } else if (type == ConstGeneral.PP_DELETE) {
                deleteDataset(list, dir, list.getSelectedValue().toString());
            }
        }
    }

    public void renameDataset(JList list, String dir) {
        if (list.isFocusable()) {
            Messages messages = new Messages();
            String newName = messages.inserirDados("Insert new name!");
            if (!Validation.isEmptyString(newName)) {
                FileUtil.renameFile(dir + list.getSelectedValue(), newName);
                SwingUtil.listFilesList(list, dir);
            }
        }
    }

}
