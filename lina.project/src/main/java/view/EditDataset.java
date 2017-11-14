package view;

import datasets.generic.GenericTableModel;
import datasets.generic.HandleGenericDataset;
import datasets.generic.GenericRowBean;
import java.util.LinkedList;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import util.FileUtil;
import util.Messages;

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
        populaComboboxColumn();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_data = new javax.swing.JTable();
        lb_rows = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cb_column = new javax.swing.JComboBox<>();
        bt_dellColumn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tf_word = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tf_newWord = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tf_search = new javax.swing.JTextField();
        bt_replace = new javax.swing.JButton();
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

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Handle Dataset"));

        jLabel1.setText("Column:");

        cb_column.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        bt_dellColumn.setText("Delete");
        bt_dellColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_dellColumnActionPerformed(evt);
            }
        });

        jLabel2.setText("Replace:");

        jLabel3.setText("for");

        jLabel4.setText("Search:");

        tf_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_searchKeyReleased(evt);
            }
        });

        bt_replace.setText("Replace");
        bt_replace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_replaceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cb_column, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_dellColumn, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sx_busy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(tf_word, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_newWord)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_replace, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tf_search))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(cb_column, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bt_dellColumn))
                    .addComponent(sx_busy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tf_word, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_newWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(bt_replace))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tf_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
            .addComponent(lb_rows, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
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
        Integer removeColumn = Integer.parseInt(cb_column.getSelectedItem().toString());
        dellColumnTable(removeColumn);
        populaComboboxColumn();
    }//GEN-LAST:event_bt_dellColumnActionPerformed

    private void bt_replaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_replaceActionPerformed
        Integer column = Integer.parseInt(cb_column.getSelectedItem().toString());
        replace(column);
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
        if (dataTable.getFirst().getTupla().size() <= 2) {
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
        populaComboboxColumn();
        sx_busy.setBusy(false);
    }//GEN-LAST:event_mi_convVerticalActionPerformed

    private void tf_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_searchKeyReleased
        Thread thread2 = new Thread(this::searchTableByColumn);
        thread2.setPriority(Thread.MAX_PRIORITY);
        thread2.start();
    }//GEN-LAST:event_tf_searchKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_dellColumn;
    private javax.swing.JButton bt_replace;
    private javax.swing.JComboBox<String> cb_column;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_rows;
    private javax.swing.JMenuItem mi_convHorizontal;
    private javax.swing.JMenuItem mi_convVertical;
    private javax.swing.JMenuItem mi_save;
    private javax.swing.JMenuItem mi_settings;
    private org.jdesktop.swingx.JXBusyLabel sx_busy;
    private javax.swing.JTable tb_data;
    private javax.swing.JTextField tf_newWord;
    private javax.swing.JTextField tf_search;
    private javax.swing.JTextField tf_word;
    // End of variables declaration//GEN-END:variables

    private void populaTabela(LinkedList<GenericRowBean> data) {
        GenericTableModel model = new GenericTableModel(data);
        if (!data.isEmpty()) {
            model.setColunas(HandleGenericDataset.extractNamesColumnFromBuffer(data));
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
            tb_data.setRowSorter(sorter);
            tb_data.setModel(model);
            tb_data.updateUI();
            tb_data.clearSelection();
            updateRowNumber();
        } else {
            messages.aviso("Empty data result!");
        }
    }

    private void populaComboboxColumn() {
        cb_column.removeAllItems();
        LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_data.getModel()).getLinhas();
        int i = 0;
        for (; i < dataTable.getFirst().getTupla().size(); i++) {
            cb_column.addItem((i + 1) + "");
        }
        cb_column.addItem((i + 1) + "");
    }

    private void searchTableByColumn() {
        sx_busy.setBusy(true);

        int col = Integer.parseInt(cb_column.getSelectedItem().toString()) - 1;
        String chave = tf_search.getText();

        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tb_data.getRowSorter();
        RowFilter<TableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(chave, col);
        } catch (java.util.regex.PatternSyntaxException ex) {
            messages.bug(ex.getMessage());
        }
        sorter.setRowFilter(rf);

        sx_busy.setBusy(false);
    }

    private void replace(int column) {
        sx_busy.setBusy(true);
        LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_data.getModel()).getLinhas();
        HandleGenericDataset.replaceInBuffer(dataTable, tf_word.getText(), tf_newWord.getText(), column - 1);
        populaTabela(dataTable);
        sx_busy.setBusy(false);
    }

    private void dellColumnTable(int removeColumn) {
        sx_busy.setBusy(true);
        LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_data.getModel()).getLinhas();
        if (removeColumn > dataTable.iterator().next().getTupla().size()) {
            messages.aviso("Column class can not be removed or invalid column!");
        } else {
            HandleGenericDataset.removeColumnFromBuffer(dataTable, removeColumn);
            populaTabela(dataTable);
        }
        sx_busy.setBusy(false);
    }

    private void updateRowNumber() {
        LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_data.getModel()).getLinhas();
        lb_rows.setText(dataTable.size() + " rows and " + dataTable.getFirst().getTupla().size() + " columns");
    }

}
