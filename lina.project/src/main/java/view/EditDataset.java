package view;

import datasets.generic.GenericTableModel;
import datasets.generic.HandleGenericDataset;
import datasets.generic.GenericRowBean;
import java.util.LinkedList;
import util.FileUtil;
import util.Messages;
import util.Validation;

/**
 *
 * @author Wesllen Sousa
 */
public class EditDataset extends javax.swing.JDialog {

    private final Messages messages = new Messages();

    public EditDataset(java.awt.Frame parent, boolean modal, String nameDataset, LinkedList<GenericRowBean> data) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);

        this.setTitle("View Dataset - " + FileUtil.extractNameFile(nameDataset));

        populaTabela(data);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_data = new javax.swing.JTable();
        lb_rows = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tf_word = new javax.swing.JTextField();
        tf_newWord = new javax.swing.JTextField();
        bt_replace = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        tf_numberColumn = new javax.swing.JTextField();
        bt_dellColumn = new javax.swing.JButton();
        sx_busy = new org.jdesktop.swingx.JXBusyLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mi_save = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mi_convHorizontal = new javax.swing.JMenuItem();
        mi_convVertical = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mi_settings = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("View Dataset");

        tb_data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tb_data.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tb_data);

        lb_rows.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_rows.setText("0 rows");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Replace"));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("word:");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("New word:");

        bt_replace.setText("Replace");
        bt_replace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_replaceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tf_newWord)
                    .addComponent(tf_word)))
            .addComponent(bt_replace, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_word, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_newWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bt_replace))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Delete column"));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Number:");

        bt_dellColumn.setText("Delete");
        bt_dellColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_dellColumnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_numberColumn))
            .addComponent(bt_dellColumn, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_numberColumn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_dellColumn))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(lb_rows, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(199, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sx_busy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sx_busy, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_rows, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenu1.setText("File");

        mi_save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        mi_save.setText("Save");
        mi_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_saveActionPerformed(evt);
            }
        });
        jMenu1.add(mi_save);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Edit");

        mi_convHorizontal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        mi_convHorizontal.setText("Convert to horizontal format");
        mi_convHorizontal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_convHorizontalActionPerformed(evt);
            }
        });
        jMenu3.add(mi_convHorizontal);

        mi_convVertical.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_MASK));
        mi_convVertical.setText("Converto to vertical format");
        mi_convVertical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_convVerticalActionPerformed(evt);
            }
        });
        jMenu3.add(mi_convVertical);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Option");

        mi_settings.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        mi_settings.setText("Settings");
        mi_settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_settingsActionPerformed(evt);
            }
        });
        jMenu2.add(mi_settings);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

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

        setBounds(0, 0, 637, 680);
    }// </editor-fold>//GEN-END:initComponents

    private void bt_dellColumnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_dellColumnActionPerformed
        dellColumnTable();
    }//GEN-LAST:event_bt_dellColumnActionPerformed

    private void bt_replaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_replaceActionPerformed
        replace();
    }//GEN-LAST:event_bt_replaceActionPerformed

    private void mi_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_saveActionPerformed
        LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_data.getModel()).getLinhas();
        ConfigSaveFile dialog = new ConfigSaveFile(null, true, dataTable);
        dialog.setVisible(true);
    }//GEN-LAST:event_mi_saveActionPerformed

    private void mi_settingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_settingsActionPerformed
        Options options = new Options(null, true);
        options.setVisible(true);
    }//GEN-LAST:event_mi_settingsActionPerformed

    private void mi_convHorizontalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_convHorizontalActionPerformed
        sx_busy.setBusy(true);
        LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_data.getModel()).getLinhas();
        if (dataTable.get(0).getTupla().size() <= 2) {
            LinkedList<GenericRowBean> dataConverted = HandleGenericDataset.convertToHorizontalFormat(dataTable);
            populaTabela(dataConverted);
        } else {
            messages.aviso("Data should be one number column and the class column!");
        }
        sx_busy.setBusy(false);
    }//GEN-LAST:event_mi_convHorizontalActionPerformed

    private void mi_convVerticalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_convVerticalActionPerformed
        sx_busy.setBusy(true);
        LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_data.getModel()).getLinhas();
        LinkedList<GenericRowBean> dataConverted = HandleGenericDataset.convertToVerticalFormat(dataTable);
        populaTabela(dataConverted);
        sx_busy.setBusy(false);
    }//GEN-LAST:event_mi_convVerticalActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_dellColumn;
    private javax.swing.JButton bt_replace;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_rows;
    private javax.swing.JMenuItem mi_convHorizontal;
    private javax.swing.JMenuItem mi_convVertical;
    private javax.swing.JMenuItem mi_save;
    private javax.swing.JMenuItem mi_settings;
    private org.jdesktop.swingx.JXBusyLabel sx_busy;
    private javax.swing.JTable tb_data;
    private javax.swing.JTextField tf_newWord;
    private javax.swing.JTextField tf_numberColumn;
    private javax.swing.JTextField tf_word;
    // End of variables declaration//GEN-END:variables

    private void populaTabela(LinkedList<GenericRowBean> data) {
        GenericTableModel model = new GenericTableModel(data);
        if (!data.isEmpty()) {
            model.setColunas(HandleGenericDataset.extractNamesColumnFromBuffer(data));
            tb_data.setModel(model);
            tb_data.updateUI();
            tb_data.clearSelection();
            updateRowNumber();
        } else {
            messages.aviso("Empty data result!");
        }
    }

    private void replace() {
        sx_busy.setBusy(true);
        LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_data.getModel()).getLinhas();
        HandleGenericDataset.replaceInBuffer(dataTable, tf_word.getText(), tf_newWord.getText());
        populaTabela(dataTable);
        sx_busy.setBusy(false);
    }

    private void dellColumnTable() {
        sx_busy.setBusy(true);
        Integer removeColumn = -1;
        if (Validation.isInteger(tf_numberColumn.getText())) {
            removeColumn = Integer.parseInt(tf_numberColumn.getText());
            LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_data.getModel()).getLinhas();
            if (removeColumn > dataTable.iterator().next().getTupla().size()) {
                messages.aviso("Column class can not be removed or invalid column!");
            } else {
                HandleGenericDataset.removeColumnFromBuffer(dataTable, removeColumn);
                populaTabela(dataTable);
            }
        } else {
            messages.aviso("Invalid number column!");
        }
        sx_busy.setBusy(false);
    }

    private void updateRowNumber() {
        LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_data.getModel()).getLinhas();
        lb_rows.setText(dataTable.size() + " rows and " + dataTable.getFirst().getTupla().size() + " columns");
    }

}
