package view;

import controle.constants.ConstDataset;
import controle.constants.ConstGeneral;
import controle.constants.Parameters;
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
        this.setLocationRelativeTo(null);

        fillPreprocessingOptions();
        fillSymbolicOptions();
        fillSegmentation();
    }

    public Options(java.awt.Frame parent, boolean modal, int tab) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);

        tb_pane.setSelectedIndex(tab);

        fillPreprocessingOptions();
        fillSegmentation();
        fillSymbolicOptions();
        fillStreaming();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        tb_pane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tf_dateFormat = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tf_separatorFile = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tf_columnClass = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tf_columnTimestamp = new javax.swing.JTextField();
        ck_horizontalFormatNoise = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        tf_windowSec = new javax.swing.JTextField();
        tf_frequency = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tf_minWinLegth = new javax.swing.JTextField();
        tf_maxWinLegth = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        tf_maxSymbol = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tf_maxWordLegth = new javax.swing.JTextField();
        tf_minWordLength = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        tf_bop = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        tf_scalaPolygon = new javax.swing.JTextField();
        bt_apply = new javax.swing.JButton();

        jButton2.setText("Apply");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Settings");

        jLabel2.setText("Datetime format:");

        jLabel3.setText("Separator file:");

        jLabel4.setText("Column class:");

        jLabel5.setText("Column timestamp:");

        ck_horizontalFormatNoise.setSelected(true);
        ck_horizontalFormatNoise.setText("Horizontal format noise");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tf_separatorFile, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                            .addComponent(tf_dateFormat)
                            .addComponent(tf_columnClass, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tf_columnTimestamp)))
                    .addComponent(ck_horizontalFormatNoise, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ck_horizontalFormatNoise)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        tb_pane.addTab("Preprocessing", jPanel2);

        jLabel12.setText("Windows sec:");

        jLabel13.setText("Frequency:");

        jLabel7.setText("Max window length:");

        jLabel8.setText("Min window length:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tf_windowSec, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(tf_frequency, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tf_minWinLegth)
                    .addComponent(tf_maxWinLegth, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(tf_frequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(tf_windowSec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tf_maxWinLegth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tf_minWinLegth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        tb_pane.addTab("Segmentation", jPanel3);

        jLabel1.setText("Speed streaming:");

        jRadioButton1.setText("Slow");

        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Fast");

        jRadioButton3.setText("None");

        jLabel9.setText("Amount of symbol:");

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
                        .addComponent(jRadioButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                .addGap(28, 28, 28)
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
                .addContainerGap(35, Short.MAX_VALUE))
        );

        tb_pane.addTab("Symbolic", jPanel1);

        jLabel15.setText("BOP Size:");

        jLabel16.setText("Scala Polygon:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_bop, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_scalaPolygon)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(tf_bop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(tf_scalaPolygon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(113, Short.MAX_VALUE))
        );

        tb_pane.addTab("Streaming", jPanel4);

        bt_apply.setText("Apply");
        bt_apply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_applyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tb_pane)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bt_apply, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tb_pane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_apply)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_applyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_applyActionPerformed
        switch (tb_pane.getSelectedIndex()) {
            case 0:
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
                if (ck_horizontalFormatNoise.isSelected()) {
                    ConstGeneral.HORIZONTAL_FORMAT_NOISE = true;
                } else {
                    ConstGeneral.HORIZONTAL_FORMAT_NOISE = false;
                }
                break;
            case 1:
                if (!Validation.isEmptyString(tf_frequency.getText()) && Validation.isInteger(tf_frequency.getText())) {
                    Parameters.FREQUENCY = Integer.parseInt(tf_frequency.getText());
                }
                if (!Validation.isEmptyString(tf_windowSec.getText()) && Validation.isInteger(tf_windowSec.getText())) {
                    Parameters.WINDOW_SEC = Integer.parseInt(tf_windowSec.getText());
                }
                if (tf_maxWinLegth.getText() != null && Validation.isInteger(tf_maxWinLegth.getText())) {
                    Parameters.MAX_WINDOW_LENGTH = Integer.parseInt(tf_maxWinLegth.getText());
                }
                if (tf_minWinLegth.getText() != null && Validation.isInteger(tf_minWinLegth.getText())) {
                    Parameters.MIN_WINDOW_LENGTH = Integer.parseInt(tf_minWinLegth.getText());
                }
                break;
            case 2:
                if (tf_maxSymbol.getText() != null && Validation.isInteger(tf_maxSymbol.getText())) {
                    Parameters.MAX_SYMBOL = Integer.parseInt(tf_maxSymbol.getText());
                }
                if (tf_maxWordLegth.getText() != null && Validation.isInteger(tf_maxWordLegth.getText())) {
                    Parameters.MAX_WORD_LENGTH = Integer.parseInt(tf_maxWordLegth.getText());
                }
                if (tf_minWordLength.getText() != null && Validation.isInteger(tf_minWordLength.getText())) {
                    Parameters.MIN_WORD_LENGTH = Integer.parseInt(tf_minWordLength.getText());
                }
                break;
            case 3:
                if (tf_bop.getText() != null && Validation.isInteger(tf_bop.getText())) {
                    Parameters.BOP_SIZE = Integer.parseInt(tf_bop.getText());
                }
                if (tf_scalaPolygon.getText() != null && Validation.isInteger(tf_scalaPolygon.getText())) {
                    Parameters.SCALA = Integer.parseInt(tf_scalaPolygon.getText());
                }
                break;
            default:
                break;
        }
        dispose();
    }//GEN-LAST:event_bt_applyActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_apply;
    private javax.swing.JCheckBox ck_horizontalFormatNoise;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JTabbedPane tb_pane;
    private javax.swing.JTextField tf_bop;
    private javax.swing.JTextField tf_columnClass;
    private javax.swing.JTextField tf_columnTimestamp;
    private javax.swing.JTextField tf_dateFormat;
    private javax.swing.JTextField tf_frequency;
    private javax.swing.JTextField tf_maxSymbol;
    private javax.swing.JTextField tf_maxWinLegth;
    private javax.swing.JTextField tf_maxWordLegth;
    private javax.swing.JTextField tf_minWinLegth;
    private javax.swing.JTextField tf_minWordLength;
    private javax.swing.JTextField tf_scalaPolygon;
    private javax.swing.JTextField tf_separatorFile;
    private javax.swing.JTextField tf_windowSec;
    // End of variables declaration//GEN-END:variables

    private void fillPreprocessingOptions() {
        tf_dateFormat.setText(ConstGeneral.DATE_FORMAT);
        tf_separatorFile.setText(ConstDataset.SEPARATOR);
        tf_columnClass.setText(ConstDataset.CLASS);
        tf_columnTimestamp.setText(ConstDataset.TIMESTAMP);
        ck_horizontalFormatNoise.setSelected(ConstGeneral.HORIZONTAL_FORMAT_NOISE);
    }

    private void fillSegmentation() {
        tf_frequency.setText(Parameters.FREQUENCY + "");
        tf_windowSec.setText(Parameters.WINDOW_SEC + "");
        tf_maxWinLegth.setText(Parameters.MAX_WINDOW_LENGTH + "");
        tf_minWinLegth.setText(Parameters.MIN_WINDOW_LENGTH + "");
    }

    private void fillSymbolicOptions() {
        tf_maxSymbol.setText(Parameters.MAX_SYMBOL + "");
        tf_maxWordLegth.setText(Parameters.MAX_WORD_LENGTH + "");
        tf_minWordLength.setText(Parameters.MIN_WORD_LENGTH + "");
    }

    private void fillStreaming() {
        tf_bop.setText(Parameters.BOP_SIZE + "");
        tf_scalaPolygon.setText(Parameters.SCALA + "");
    }

}
