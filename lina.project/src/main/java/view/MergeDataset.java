package view;

import datasets.generic.GenericTableModel;
import datasets.generic.HandleGenericDataset;
import datasets.generic.GenericRowBean;
import java.util.LinkedList;
import constants.ConstDataset;
import constants.ConstGeneral;
import util.FileUtil;
import util.Messages;
import util.Validation;

/**
 *
 * @author Wesllen Sousa
 */
public class MergeDataset extends javax.swing.JDialog {

    private final Messages messages = new Messages();

    private LinkedList<GenericRowBean> data1, data2;
    private String source1, source2;

    public MergeDataset(java.awt.Frame parent, boolean modal, String source1, String source2) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);

        this.source1 = source1;
        this.source2 = source2;

        Thread thread = new Thread(this::merge);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_mergedData = new javax.swing.JTable();
        sx_busy = new org.jdesktop.swingx.JXBusyLabel();
        jLabel9 = new javax.swing.JLabel();
        tf_dellColumn = new javax.swing.JTextField();
        bt_dell = new javax.swing.JButton();
        lb_rowsMerged = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        lb_dataset1 = new javax.swing.JLabel();
        bt_view1 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        lb_dataset2 = new javax.swing.JLabel();
        bt_view2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mi_csv = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Merge dataset");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Merged dataset"));

        tb_mergedData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tb_mergedData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tb_mergedData);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Delete column:");

        bt_dell.setText("dell");
        bt_dell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_dellActionPerformed(evt);
            }
        });

        lb_rowsMerged.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_rowsMerged.setText("Merged Dataset, 0 rows");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_dellColumn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_dell)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sx_busy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(lb_rowsMerged, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sx_busy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tf_dellColumn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bt_dell)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_rowsMerged, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder("Dataset 1"));

        lb_dataset1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_dataset1.setText("Dataset 1, 10 rows");

        bt_view1.setText("View");
        bt_view1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_view1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_dataset1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_view1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addComponent(lb_dataset1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_view1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder("Dataset 2"));

        lb_dataset2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_dataset2.setText("Dataset 1, 10 rows");

        bt_view2.setText("View");
        bt_view2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_view2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_dataset2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_view2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addComponent(lb_dataset2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_view2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("File");

        mi_csv.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        mi_csv.setText("Save");
        mi_csv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_csvActionPerformed(evt);
            }
        });
        jMenu1.add(mi_csv);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Option");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Settings");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_view1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_view1ActionPerformed
        EditDataset view = new EditDataset(null, true, source1, data1);
        view.setVisible(true);
    }//GEN-LAST:event_bt_view1ActionPerformed

    private void bt_view2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_view2ActionPerformed
        EditDataset view = new EditDataset(null, true, source2, data2);
        view.setVisible(true);
    }//GEN-LAST:event_bt_view2ActionPerformed

    private void bt_dellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_dellActionPerformed
        dellColumnTable();
    }//GEN-LAST:event_bt_dellActionPerformed

    private void mi_csvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_csvActionPerformed
        Thread thread = new Thread(this::saveFileCSV);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }//GEN-LAST:event_mi_csvActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Options options = new Options(null, true);
        options.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_dell;
    private javax.swing.JButton bt_view1;
    private javax.swing.JButton bt_view2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_dataset1;
    private javax.swing.JLabel lb_dataset2;
    private javax.swing.JLabel lb_rowsMerged;
    private javax.swing.JMenuItem mi_csv;
    private org.jdesktop.swingx.JXBusyLabel sx_busy;
    private javax.swing.JTable tb_mergedData;
    private javax.swing.JTextField tf_dellColumn;
    // End of variables declaration//GEN-END:variables

    private void merge() {
        sx_busy.setBusy(true);

        data1 = HandleGenericDataset.bufferFileInMemory(ConstDataset.SEPARATOR, source1);
        HandleGenericDataset.setColumnClass(data1);
        data2 = HandleGenericDataset.bufferFileInMemory(ConstDataset.SEPARATOR, source2);
        HandleGenericDataset.setColumnClass(data2);

        LinkedList<GenericRowBean> mergedData = HandleGenericDataset.merge(data1, data2);
        populaTabela(mergedData);

        lb_rowsMerged.setText(mergedData.size() + " rows");
        lb_dataset1.setText(FileUtil.extractNameFile(source1) + ", " + data1.size() + " rows");
        lb_dataset2.setText(FileUtil.extractNameFile(source2) + ", " + data2.size() + " rows");

        sx_busy.setBusy(false);
    }

    public void populaTabela(LinkedList<GenericRowBean> data) {
        GenericTableModel model = new GenericTableModel(data);
        model.setColunas(HandleGenericDataset.extractNamesColumnFromBuffer(data));
        tb_mergedData.setModel(model);
    }

    private void dellColumnTable() {
        LinkedList<GenericRowBean> data = ((GenericTableModel) tb_mergedData.getModel()).getLinhas();
        try {
            HandleGenericDataset.removeColumnFromBuffer(data, Integer.parseInt(tf_dellColumn.getText()));
        } catch (NumberFormatException ex) {
            messages.aviso("Invalid number column!");
        }
        populaTabela(data);
    }

    private void saveFileCSV() {
        saveCSV(ConstDataset.DS_RAW, messages.inserirDados("Insert file name"));
    }

    private void saveCSV(String dir, String name) {
        sx_busy.setBusy(true);
        if (!Validation.isEmptyString(name)) {
            LinkedList<GenericRowBean> dataTable = ((GenericTableModel) tb_mergedData.getModel()).getLinhas();
            if (HandleGenericDataset.saveBufferToCSV(dir, name, dataTable, ConstDataset.SEPARATOR)) {
                ConstGeneral.TELA_PRINCIPAL.updateHandleTabedPane();
                messages.sucesso("File saved!");
            }
        } else {
            messages.aviso("Empty name!");
        }
        sx_busy.setBusy(false);
    }

}
