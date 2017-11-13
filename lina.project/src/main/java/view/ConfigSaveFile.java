package view;

import constants.ConstDataset;
import constants.ConstGeneral;
import datasets.generic.GenericRowBean;
import datasets.generic.HandleGenericDataset;
import java.util.LinkedList;
import util.Messages;
import util.Validation;

/**
 *
 * @author Wesllen Sousa
 */
public class ConfigSaveFile extends javax.swing.JDialog {

    private final Messages messages = new Messages();
    private LinkedList<GenericRowBean> data;

    public ConfigSaveFile(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
    }

    public ConfigSaveFile(java.awt.Frame parent, boolean modal, LinkedList<GenericRowBean> data) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cb_where = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        tf_name = new javax.swing.JTextField();
        bt_save = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cb_format = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Save File");

        jLabel1.setText("Where:");

        cb_where.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Raw", "Train", "Test", "Stream" }));

        jLabel2.setText("Name:");

        bt_save.setText("Save");
        bt_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_saveActionPerformed(evt);
            }
        });

        jLabel3.setText("Format: ");

        cb_format.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "csv", "arff" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bt_save, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cb_where, 0, 174, Short.MAX_VALUE)
                            .addComponent(tf_name)
                            .addComponent(cb_format, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cb_format, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cb_where, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tf_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_save)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_saveActionPerformed
        String nameFile = tf_name.getText();
        if (!Validation.isEmptyString(nameFile)) {
            String format = (String) cb_format.getSelectedItem();
            String where = (String) cb_where.getSelectedItem();
            switch (where) {
                case "Raw":
                    save(ConstDataset.DS_RAW, nameFile, format);
                    break;
                case "Train":
                    save(ConstDataset.DS_TRAIN, nameFile, format);
                    break;
                case "Test":
                    save(ConstDataset.DS_TEST, nameFile, format);
                    break;
                case "Stream":
                    save(ConstDataset.DS_STREAM, nameFile, format);
                    break;
                default:
                    break;
            }
        } else {
            messages.aviso("Empty name!");
        }
    }//GEN-LAST:event_bt_saveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_save;
    private javax.swing.JComboBox<String> cb_format;
    private javax.swing.JComboBox<String> cb_where;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField tf_name;
    // End of variables declaration//GEN-END:variables

    private void save(String dir, String nameFile, String format) {
        if (format.equals("csv")) {
            saveCSV(dir, nameFile);
        } else if (format.equals("arff")) {
            saveARFF(dir, nameFile);
        }
    }

    private void saveCSV(String dir, String nameFile) {
        if (HandleGenericDataset.saveBufferToCSV(dir, nameFile, data, ConstDataset.SEPARATOR)) {
            ConstGeneral.TELA_PRINCIPAL.updateHandleTabedPane();
            messages.sucesso("File saved!");
            dispose();
        }
    }

    private void saveARFF(String dir, String nameFile) {
        if (HandleGenericDataset.saveBufferToARFF(dir, nameFile, data)) {
            ConstGeneral.TELA_PRINCIPAL.updateHandleTabedPane();
            messages.sucesso("File saved!");
            dispose();
        }
    }

}
