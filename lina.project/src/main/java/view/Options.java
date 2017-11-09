package view;

import constants.ConstDataset;
import constants.ConstGeneral;
import constants.Parameters;
import util.Validation;

/**
 *
 * @author Wesllen Sousa
 */
public class Options extends javax.swing.JDialog {

    /**
     * Creates new form Options
     */
    public Options(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(ConstGeneral.TELA_PRINCIPAL);

        fillPreprocessingOptions();
        fillSymbolicOptions();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tf_dateFormat = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tf_separatorFile = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tf_columnClass = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tf_columnTimestamp = new javax.swing.JTextField();
        bt_preprocessing = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        ck_clearHist = new javax.swing.JCheckBox();
        bt_symbolic = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        tf_fallThPagehinkey = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tf_maxWinLegth = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tf_minWinLegth = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tf_maxSymbol = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tf_maxWordLegth = new javax.swing.JTextField();
        tf_minWordLength = new javax.swing.JTextField();

        jButton2.setText("Apply");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Settings");

        jLabel2.setText("Datetime format:");

        jLabel3.setText("Separator file:");

        jLabel4.setText("Column class:");

        jLabel5.setText("Column timestamp:");

        bt_preprocessing.setText("Apply");
        bt_preprocessing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_preprocessingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bt_preprocessing, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tf_separatorFile, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(tf_dateFormat)
                            .addComponent(tf_columnClass, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tf_columnTimestamp))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tf_dateFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tf_separatorFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tf_columnClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tf_columnTimestamp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bt_preprocessing)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Preprocessing", jPanel2);

        jLabel1.setText("Speed streaming:");

        jRadioButton1.setText("Slow");

        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Fast");

        jRadioButton3.setText("None");

        ck_clearHist.setSelected(true);
        ck_clearHist.setText("Clear histogram");

        bt_symbolic.setText("Apply");
        bt_symbolic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_symbolicActionPerformed(evt);
            }
        });

        jLabel6.setText("Threshold Page Hinkley:");

        jLabel7.setText("Max window length:");

        jLabel8.setText("Min window length:");

        jLabel9.setText("Max symbol:");

        jLabel10.setText("Max word length:");

        jLabel11.setText("Min word length:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(ck_clearHist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bt_symbolic, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tf_fallThPagehinkey)
                            .addComponent(tf_maxWinLegth)
                            .addComponent(tf_minWinLegth)
                            .addComponent(tf_maxSymbol)
                            .addComponent(tf_maxWordLegth)
                            .addComponent(tf_minWordLength, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ck_clearHist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tf_fallThPagehinkey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tf_maxWinLegth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tf_minWinLegth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(tf_maxSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(tf_maxWordLegth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(tf_minWordLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(bt_symbolic)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Symbolic", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_symbolicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_symbolicActionPerformed
        if (ck_clearHist.isSelected()) {
            ConstGeneral.CLEAR_HIST = true;
        } else {
            ConstGeneral.CLEAR_HIST = false;
        }
        if (tf_fallThPagehinkey.getText() != null && Validation.isDouble(tf_fallThPagehinkey.getText())) {
            ConstGeneral.PERCENT_QUEDA_THRESHOULD = Double.parseDouble(tf_fallThPagehinkey.getText());
        }
        if (tf_maxWinLegth.getText() != null && Validation.isInteger(tf_maxWinLegth.getText())) {
            Parameters.MAX_WINDOW_LENGTH = Integer.parseInt(tf_maxWinLegth.getText());
        }
        if (tf_minWinLegth.getText() != null && Validation.isInteger(tf_minWinLegth.getText())) {
            Parameters.MIN_WINDOW_LENGTH = Integer.parseInt(tf_minWinLegth.getText());
        }
        if (tf_maxSymbol.getText() != null && Validation.isInteger(tf_maxSymbol.getText())) {
            Parameters.MAX_SYMBOL = Integer.parseInt(tf_maxSymbol.getText());
        }
        if (tf_maxWordLegth.getText() != null && Validation.isInteger(tf_maxWordLegth.getText())) {
            Parameters.MAX_WORD_LENGTH = Integer.parseInt(tf_maxWordLegth.getText());
        }
        if (tf_minWordLength.getText() != null && Validation.isInteger(tf_minWordLength.getText())) {
            Parameters.MIN_WORD_LENGTH = Integer.parseInt(tf_minWordLength.getText());
        }
        dispose();
    }//GEN-LAST:event_bt_symbolicActionPerformed

    private void bt_preprocessingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_preprocessingActionPerformed
        if (!Validation.isEmptyString(tf_dateFormat.getText())) {
            ConstGeneral.DATE_FORMAT = tf_dateFormat.getText();
        }
        if (!Validation.isEmptyString(tf_separatorFile.getText())) {
            ConstDataset.SEPARATOR = tf_separatorFile.getText();
        }
        if (!Validation.isEmptyString(tf_columnClass.getText())) {
            ConstDataset.CLASS = tf_columnClass.getText();
        }
        if (!Validation.isEmptyString(tf_columnTimestamp.getText())) {
            ConstDataset.TIMESTAMP = tf_columnTimestamp.getText();
        }
        dispose();
    }//GEN-LAST:event_bt_preprocessingActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_preprocessing;
    private javax.swing.JButton bt_symbolic;
    private javax.swing.JCheckBox ck_clearHist;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField tf_columnClass;
    private javax.swing.JTextField tf_columnTimestamp;
    private javax.swing.JTextField tf_dateFormat;
    private javax.swing.JTextField tf_fallThPagehinkey;
    private javax.swing.JTextField tf_maxSymbol;
    private javax.swing.JTextField tf_maxWinLegth;
    private javax.swing.JTextField tf_maxWordLegth;
    private javax.swing.JTextField tf_minWinLegth;
    private javax.swing.JTextField tf_minWordLength;
    private javax.swing.JTextField tf_separatorFile;
    // End of variables declaration//GEN-END:variables

    private void fillPreprocessingOptions() {
        tf_dateFormat.setText(ConstGeneral.DATE_FORMAT);
        tf_separatorFile.setText(ConstDataset.SEPARATOR);
        tf_columnClass.setText(ConstDataset.CLASS);
        tf_columnTimestamp.setText(ConstDataset.TIMESTAMP);
    }

    private void fillSymbolicOptions() {
        ck_clearHist.setSelected(ConstGeneral.CLEAR_HIST);
        tf_fallThPagehinkey.setText(ConstGeneral.PERCENT_QUEDA_THRESHOULD + "");
        tf_maxWinLegth.setText(Parameters.MAX_WINDOW_LENGTH + "");
        tf_minWinLegth.setText(Parameters.MIN_WINDOW_LENGTH + "");
        tf_maxSymbol.setText(Parameters.MAX_SYMBOL + "");
        tf_maxWordLegth.setText(Parameters.MAX_WORD_LENGTH + "");
        tf_minWordLength.setText(Parameters.MIN_WORD_LENGTH + "");
    }

}
