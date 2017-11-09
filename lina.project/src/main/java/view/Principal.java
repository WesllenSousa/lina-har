package view;

import controle.weka.WekaUtil;
import datasets.generic.HandleGenericDataset;
import datasets.generic.GenericRowBean;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import constants.ConstDataset;
import constants.ConstGeneral;
import constants.Parameters;
import datasets.memory.WordInterval;
import datasets.memory.WordRecord;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import util.FileUtil;
import util.Messages;
import util.SwingUtil;
import util.Validation;
import view.manualviews.BarGraphic;
import view.manualviews.LineGraphic;
import view.viewControler.ComponentView;
import view.viewControler.DesktopView;
import view.viewControler.TrainView;
import view.manualviews.TextFilter;
import view.manualviews.WordTableModel;
import view.viewControler.SymbolicView;
import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 *
 * @author Wesllen Sousa
 */
public class Principal extends javax.swing.JFrame {

    private final ComponentView componentView = new ComponentView();
    private final DesktopView desktopView = new DesktopView();
    private final TrainView trainView = new TrainView();
    private final Messages messages = new Messages();

    private SymbolicView symbolicView;
    private WordTableModel wordTableModel;
    private LineGraphic lineGraphic;
    private BarGraphic barGraphic;
    private Thread play;
    private boolean paused = false;

    private boolean createInternalfame = true;

    public Principal() {
        ConstGeneral.TELA_PRINCIPAL = this;
        initComponents();
        screenDimension();

        menuCollapsed();
        resetSignalSelection();
        resetProcessingLists();
        sp_trainTest.setVisible(false);

        desktopView.fillAlgorithmsList(lt_algorithms);
        SwingUtil.listFilesList(lt_rawData, ConstDataset.DS_RAW);

        addLineGraphic();
        addBarGraphic();
        wordTableModel = new WordTableModel(new ArrayList<>());
        tb_words.setModel(wordTableModel);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pp_graphic = new javax.swing.JPopupMenu();
        ppi_cascade = new javax.swing.JMenuItem();
        ppi_sideBySide = new javax.swing.JMenuItem();
        pp_box = new javax.swing.JPopupMenu();
        ppi_viewDatasetBox = new javax.swing.JMenuItem();
        ppi_removeBox = new javax.swing.JMenuItem();
        pp_file = new javax.swing.JPopupMenu();
        ppi_editDatasetFile = new javax.swing.JMenuItem();
        ppi_deleteDatasetFile = new javax.swing.JMenuItem();
        ppi_renameDatasetFile = new javax.swing.JMenuItem();
        bg_graphic = new javax.swing.ButtonGroup();
        pp_classifier = new javax.swing.JPopupMenu();
        ppi_saveClassifier = new javax.swing.JMenuItem();
        ppi_renameClassifier = new javax.swing.JMenuItem();
        ppi_deleteClassifier = new javax.swing.JMenuItem();
        gp_settings = new javax.swing.ButtonGroup();
        gp_pageHinkley = new javax.swing.ButtonGroup();
        gp_numerosityReduction = new javax.swing.ButtonGroup();
        gp_alingment = new javax.swing.ButtonGroup();
        gp_model = new javax.swing.ButtonGroup();
        gp_algDiscretization = new javax.swing.ButtonGroup();
        gp_normalized = new javax.swing.ButtonGroup();
        tb_principal = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        pn_menu = new javax.swing.JPanel();
        tb_1 = new javax.swing.JToggleButton();
        pn_1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lt_rawData = new javax.swing.JList<>();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        lt_signalSelectionLeft = new javax.swing.JList<>();
        bt_signalSelectionRight = new javax.swing.JButton();
        bt_signalSelectionLeft = new javax.swing.JButton();
        jScrollPane19 = new javax.swing.JScrollPane();
        lt_signalSelectionRight = new javax.swing.JList<>();
        bt_signalSelectionLeft1 = new javax.swing.JButton();
        bt_signalSelectionLeft2 = new javax.swing.JButton();
        jPanel39 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        tf_windowSize = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        sl_overlap = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        tf_frequency = new javax.swing.JTextField();
        tb_2 = new javax.swing.JToggleButton();
        pn_2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        lt_filtersLeft = new javax.swing.JList<>();
        bt_filtersRight = new javax.swing.JButton();
        bt_filtersLeft = new javax.swing.JButton();
        jScrollPane22 = new javax.swing.JScrollPane();
        lt_filtersRight = new javax.swing.JList<>();
        bt_signalSelectionLeft3 = new javax.swing.JButton();
        bt_signalSelectionLeft4 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        lt_principalFeaturesLeft = new javax.swing.JList<>();
        bt_principalFeatureRight = new javax.swing.JButton();
        bt_principalFeatureLeft = new javax.swing.JButton();
        jScrollPane27 = new javax.swing.JScrollPane();
        lt_principalFeaturesRight = new javax.swing.JList<>();
        bt_signalSelectionLeft5 = new javax.swing.JButton();
        bt_signalSelectionLeft6 = new javax.swing.JButton();
        tb_3 = new javax.swing.JToggleButton();
        pn_3 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        lt_timeFeaturesLeft = new javax.swing.JList<>();
        bt_timeFeatureRight = new javax.swing.JButton();
        bt_timeFeatureLeft = new javax.swing.JButton();
        jScrollPane24 = new javax.swing.JScrollPane();
        lt_timeFeaturesRight = new javax.swing.JList<>();
        bt_signalSelectionLeft7 = new javax.swing.JButton();
        bt_signalSelectionLeft8 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane21 = new javax.swing.JScrollPane();
        lt_frequencyFeaturesLeft = new javax.swing.JList<>();
        bt_frequencyFeatureRight = new javax.swing.JButton();
        bt_frequencyFeatureLeft = new javax.swing.JButton();
        jScrollPane25 = new javax.swing.JScrollPane();
        lt_frequencyFeaturesRight = new javax.swing.JList<>();
        bt_signalSelectionLeft9 = new javax.swing.JButton();
        bt_signalSelectionLeft10 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        tb_desktop = new javax.swing.JTabbedPane();
        dp_preprocessing = new javax.swing.JDesktopPane();
        pn_box = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jSplitPane6 = new javax.swing.JSplitPane();
        jPanel20 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        lt_trainData = new javax.swing.JList<>();
        jPanel36 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        lt_algorithms = new javax.swing.JList<>();
        pn_trainTest = new javax.swing.JPanel();
        sp_trainTest = new javax.swing.JScrollPane();
        lt_trainDataTest = new javax.swing.JList<>();
        ck_trainUserTest = new javax.swing.JCheckBox();
        jPanel34 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        lt_classifier = new javax.swing.JList<>();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tp_resultTrain = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jSplitPane5 = new javax.swing.JSplitPane();
        jPanel19 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        lt_modelClassification = new javax.swing.JList<>();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        lt_testData = new javax.swing.JList<>();
        jPanel32 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tp_resultClassification = new javax.swing.JTextPane();
        jPanel5 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lt_timeSeriesData = new javax.swing.JList<>();
        jPanel28 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jPanel26 = new javax.swing.JPanel();
        rd_sax = new javax.swing.JRadioButton();
        rd_sfa = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        tf_symOffset = new javax.swing.JTextField();
        tf_symWordLength = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tf_symSymbolAlphabet = new javax.swing.JTextField();
        tf_symWindow = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        rd_numReductionYes = new javax.swing.JRadioButton();
        rd_numReductionNo = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        rd_alingmentYes = new javax.swing.JRadioButton();
        rd_alingmentNo = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        rd_normYes = new javax.swing.JRadioButton();
        td_normNo = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        rd_algSAXVSM = new javax.swing.JRadioButton();
        rd_algBOSS = new javax.swing.JRadioButton();
        rd_algBOSSVS = new javax.swing.JRadioButton();
        rd_algWeasel = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        pn_graphicLine = new javax.swing.JPanel();
        pn_graphicBar = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane16 = new javax.swing.JScrollPane();
        ta_symbolic = new javax.swing.JTextArea();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tb_words = new javax.swing.JTable();
        lb_tableInfo = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btt_openRawData = new javax.swing.JButton();
        btt_openModel = new javax.swing.JButton();
        btt_mergeDataset = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btt_edit = new javax.swing.JButton();
        btt_delete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btt_executar = new javax.swing.JButton();
        btt_executar2 = new javax.swing.JButton();
        btt_reset = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btt_pause = new javax.swing.JButton();
        btt_stop = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btt_exit = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        sx_busy = new org.jdesktop.swingx.JXBusyLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        ppi_cascade.setText("Cascate");
        ppi_cascade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppi_cascadeActionPerformed(evt);
            }
        });
        pp_graphic.add(ppi_cascade);

        ppi_sideBySide.setText("Side by Side");
        ppi_sideBySide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppi_sideBySideActionPerformed(evt);
            }
        });
        pp_graphic.add(ppi_sideBySide);

        ppi_viewDatasetBox.setText("View dataset");
        ppi_viewDatasetBox.setToolTipText("");
        ppi_viewDatasetBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppi_viewDatasetBoxActionPerformed(evt);
            }
        });
        pp_box.add(ppi_viewDatasetBox);

        ppi_removeBox.setText("Remove");
        ppi_removeBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppi_removeBoxActionPerformed(evt);
            }
        });
        pp_box.add(ppi_removeBox);

        ppi_editDatasetFile.setText("Edit");
        ppi_editDatasetFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppi_editDatasetFileActionPerformed(evt);
            }
        });
        pp_file.add(ppi_editDatasetFile);

        ppi_deleteDatasetFile.setText("Delete");
        ppi_deleteDatasetFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppi_deleteDatasetFileActionPerformed(evt);
            }
        });
        pp_file.add(ppi_deleteDatasetFile);

        ppi_renameDatasetFile.setText("Rename");
        ppi_renameDatasetFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppi_renameDatasetFileActionPerformed(evt);
            }
        });
        pp_file.add(ppi_renameDatasetFile);

        ppi_saveClassifier.setText("Save");
        ppi_saveClassifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppi_saveClassifierActionPerformed(evt);
            }
        });
        pp_classifier.add(ppi_saveClassifier);

        ppi_renameClassifier.setText("Rename");
        ppi_renameClassifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppi_renameClassifierActionPerformed(evt);
            }
        });
        pp_classifier.add(ppi_renameClassifier);

        ppi_deleteClassifier.setText("Delete");
        ppi_deleteClassifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppi_deleteClassifierActionPerformed(evt);
            }
        });
        pp_classifier.add(ppi_deleteClassifier);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("LINA");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tb_principal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tb_principalStateChanged(evt);
            }
        });

        jSplitPane1.setDividerLocation(300);

        jScrollPane13.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tb_1.setText("Datasets");
        tb_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tb_1ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Raw Dataset"));

        lt_rawData.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Acelerometer", "Gyroscope", "Gravity", "Magntometer", "Rotation" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_rawData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_rawDataMouseClicked(evt);
            }
        });
        lt_rawData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lt_rawDataKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(lt_rawData);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Signal Selection"));

        lt_signalSelectionLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_signalSelectionLeftMouseClicked(evt);
            }
        });
        jScrollPane18.setViewportView(lt_signalSelectionLeft);

        bt_signalSelectionRight.setText("<");
        bt_signalSelectionRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionRightActionPerformed(evt);
            }
        });

        bt_signalSelectionLeft.setText(">");
        bt_signalSelectionLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeftActionPerformed(evt);
            }
        });

        lt_signalSelectionRight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_signalSelectionRightMouseClicked(evt);
            }
        });
        jScrollPane19.setViewportView(lt_signalSelectionRight);

        bt_signalSelectionLeft1.setText(">>");
        bt_signalSelectionLeft1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeft1ActionPerformed(evt);
            }
        });

        bt_signalSelectionLeft2.setText("<<");
        bt_signalSelectionLeft2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeft2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bt_signalSelectionLeft1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionLeft2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(bt_signalSelectionLeft1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_signalSelectionLeft)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_signalSelectionRight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_signalSelectionLeft2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane19)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder("Segmentation"));

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Windows size (sec):");

        tf_windowSize.setText("10");

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("Overlap (%):");

        sl_overlap.setMajorTickSpacing(10);
        sl_overlap.setMaximum(90);
        sl_overlap.setPaintLabels(true);
        sl_overlap.setPaintTicks(true);
        sl_overlap.setValue(0);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Frequency (Hz):");

        tf_frequency.setText("50");

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tf_windowSize, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                    .addComponent(tf_frequency)))
            .addComponent(sl_overlap, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tf_windowSize)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_frequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sl_overlap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pn_1Layout = new javax.swing.GroupLayout(pn_1);
        pn_1.setLayout(pn_1Layout);
        pn_1Layout.setHorizontalGroup(
            pn_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pn_1Layout.setVerticalGroup(
            pn_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tb_2.setText("Signal Processing");
        tb_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tb_2ActionPerformed(evt);
            }
        });

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Filters"));

        lt_filtersLeft.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Gausian Lowpass", "Kalman", "Moving Average Filter (Yang 2009)" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_filtersLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_filtersLeftMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(lt_filtersLeft);

        bt_filtersRight.setText("<");
        bt_filtersRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_filtersRightActionPerformed(evt);
            }
        });

        bt_filtersLeft.setText(">");
        bt_filtersLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_filtersLeftActionPerformed(evt);
            }
        });

        lt_filtersRight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_filtersRightMouseClicked(evt);
            }
        });
        jScrollPane22.setViewportView(lt_filtersRight);

        bt_signalSelectionLeft3.setText(">>");
        bt_signalSelectionLeft3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeft3ActionPerformed(evt);
            }
        });

        bt_signalSelectionLeft4.setText("<<");
        bt_signalSelectionLeft4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeft4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bt_filtersLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_filtersRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionLeft3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionLeft4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane22, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(bt_signalSelectionLeft3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_filtersLeft)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_filtersRight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_signalSelectionLeft4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane22)
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Principal Features"));

        lt_principalFeaturesLeft.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Magnitude ", "FFT", "Vertical", "Horizontal", "Wavelet" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_principalFeaturesLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_principalFeaturesLeftMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(lt_principalFeaturesLeft);

        bt_principalFeatureRight.setText("<");
        bt_principalFeatureRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_principalFeatureRightActionPerformed(evt);
            }
        });

        bt_principalFeatureLeft.setText(">");
        bt_principalFeatureLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_principalFeatureLeftActionPerformed(evt);
            }
        });

        lt_principalFeaturesRight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_principalFeaturesRightMouseClicked(evt);
            }
        });
        jScrollPane27.setViewportView(lt_principalFeaturesRight);

        bt_signalSelectionLeft5.setText(">>");
        bt_signalSelectionLeft5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeft5ActionPerformed(evt);
            }
        });

        bt_signalSelectionLeft6.setText("<<");
        bt_signalSelectionLeft6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeft6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bt_principalFeatureLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_principalFeatureRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionLeft5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionLeft6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane27, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(bt_signalSelectionLeft5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_principalFeatureLeft)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_principalFeatureRight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_signalSelectionLeft6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane27)
        );

        javax.swing.GroupLayout pn_2Layout = new javax.swing.GroupLayout(pn_2);
        pn_2.setLayout(pn_2Layout);
        pn_2Layout.setHorizontalGroup(
            pn_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pn_2Layout.setVerticalGroup(
            pn_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_2Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );

        tb_3.setText("Features Extraction");
        tb_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tb_3ActionPerformed(evt);
            }
        });

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Time Features"));

        lt_timeFeaturesLeft.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Mean", "Standard Deviation", "FFT", "Root Mean Square (RMS)", "Mean of Absolute Error", "Correlation", "Zero Crossing Ratio (ZCR)", "Quartile 25%", "Quartile 50%", "Quartile 75%", "Orientation-Invariant" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_timeFeaturesLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_timeFeaturesLeftMouseClicked(evt);
            }
        });
        jScrollPane20.setViewportView(lt_timeFeaturesLeft);

        bt_timeFeatureRight.setText("<");
        bt_timeFeatureRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_timeFeatureRightActionPerformed(evt);
            }
        });

        bt_timeFeatureLeft.setText(">");
        bt_timeFeatureLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_timeFeatureLeftActionPerformed(evt);
            }
        });

        lt_timeFeaturesRight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_timeFeaturesRightMouseClicked(evt);
            }
        });
        jScrollPane24.setViewportView(lt_timeFeaturesRight);

        bt_signalSelectionLeft7.setText(">>");
        bt_signalSelectionLeft7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeft7ActionPerformed(evt);
            }
        });

        bt_signalSelectionLeft8.setText("<<");
        bt_signalSelectionLeft8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeft8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bt_timeFeatureLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionLeft7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionLeft8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_timeFeatureRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(bt_signalSelectionLeft7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_timeFeatureLeft)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_timeFeatureRight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_signalSelectionLeft8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder("Frequency Features"));

        lt_frequencyFeaturesLeft.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Frequency Range Power", "Spectral Energy", "Spectral Entropy" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_frequencyFeaturesLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_frequencyFeaturesLeftMouseClicked(evt);
            }
        });
        jScrollPane21.setViewportView(lt_frequencyFeaturesLeft);

        bt_frequencyFeatureRight.setText("<");
        bt_frequencyFeatureRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_frequencyFeatureRightActionPerformed(evt);
            }
        });

        bt_frequencyFeatureLeft.setText(">");
        bt_frequencyFeatureLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_frequencyFeatureLeftActionPerformed(evt);
            }
        });

        lt_frequencyFeaturesRight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_frequencyFeaturesRightMouseClicked(evt);
            }
        });
        jScrollPane25.setViewportView(lt_frequencyFeaturesRight);

        bt_signalSelectionLeft9.setText(">>");
        bt_signalSelectionLeft9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeft9ActionPerformed(evt);
            }
        });

        bt_signalSelectionLeft10.setText("<<");
        bt_signalSelectionLeft10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_signalSelectionLeft10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bt_frequencyFeatureLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_frequencyFeatureRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionLeft9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_signalSelectionLeft10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane25, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(bt_signalSelectionLeft9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_frequencyFeatureLeft)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_frequencyFeatureRight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_signalSelectionLeft10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pn_3Layout = new javax.swing.GroupLayout(pn_3);
        pn_3.setLayout(pn_3Layout);
        pn_3Layout.setHorizontalGroup(
            pn_3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pn_3Layout.setVerticalGroup(
            pn_3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_3Layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout pn_menuLayout = new javax.swing.GroupLayout(pn_menu);
        pn_menu.setLayout(pn_menuLayout);
        pn_menuLayout.setHorizontalGroup(
            pn_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tb_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tb_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pn_1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pn_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tb_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pn_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pn_menuLayout.setVerticalGroup(
            pn_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_menuLayout.createSequentialGroup()
                .addComponent(tb_1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tb_2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tb_3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane13.setViewportView(pn_menu);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(jPanel1);

        tb_desktop.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tb_desktopStateChanged(evt);
            }
        });

        dp_preprocessing.setBackground(new java.awt.Color(0, 204, 204));
        dp_preprocessing.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dp_preprocessingMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dp_preprocessingMouseReleased(evt);
            }
        });
        tb_desktop.addTab("Graphic", dp_preprocessing);

        pn_box.setBackground(new java.awt.Color(153, 255, 153));

        javax.swing.GroupLayout pn_boxLayout = new javax.swing.GroupLayout(pn_box);
        pn_box.setLayout(pn_boxLayout);
        pn_boxLayout.setHorizontalGroup(
            pn_boxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 724, Short.MAX_VALUE)
        );
        pn_boxLayout.setVerticalGroup(
            pn_boxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 761, Short.MAX_VALUE)
        );

        tb_desktop.addTab("Without Graphic", pn_box);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tb_desktop)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tb_desktop, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        tb_principal.addTab("Preprocessing", jPanel7);

        jSplitPane6.setDividerLocation(300);

        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder("Train Datasets"));

        lt_trainData.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Train 1", "Train 2", "Train 3", "Train 4" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_trainData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_trainDataMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(lt_trainData);

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10)
        );

        jPanel36.setBorder(javax.swing.BorderFactory.createTitledBorder("Algorithms"));

        lt_algorithms.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Decision Tree", "Naive Bayes", "SVM", "KNN" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane17.setViewportView(lt_algorithms);

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane17)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane17)
        );

        pn_trainTest.setBorder(javax.swing.BorderFactory.createTitledBorder("Test Datasets"));

        lt_trainDataTest.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Test 1", "Test 2", "Test 3", "Test 4" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_trainDataTest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_trainDataTestMouseClicked(evt);
            }
        });
        sp_trainTest.setViewportView(lt_trainDataTest);

        ck_trainUserTest.setText("Use test data");
        ck_trainUserTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ck_trainUserTestActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pn_trainTestLayout = new javax.swing.GroupLayout(pn_trainTest);
        pn_trainTest.setLayout(pn_trainTestLayout);
        pn_trainTestLayout.setHorizontalGroup(
            pn_trainTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp_trainTest)
            .addGroup(pn_trainTestLayout.createSequentialGroup()
                .addComponent(ck_trainUserTest)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pn_trainTestLayout.setVerticalGroup(
            pn_trainTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_trainTestLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ck_trainUserTest)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sp_trainTest))
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pn_trainTest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_trainTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(214, Short.MAX_VALUE))
        );

        jPanel20Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel31, jPanel36});

        jSplitPane6.setLeftComponent(jPanel20);

        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder("Classifiers"));

        lt_classifier.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Model 1 (98%)", "Model 2 (77%)", "Model 3 (89%)" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_classifier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_classifierMouseClicked(evt);
            }
        });
        lt_classifier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lt_classifierKeyReleased(evt);
            }
        });
        jScrollPane12.setViewportView(lt_classifier);

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Result"));

        tp_resultTrain.setEditable(false);
        jScrollPane7.setViewportView(tp_resultTrain);

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSplitPane6.setRightComponent(jPanel34);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane6, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane6, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tb_principal.addTab("Train", jPanel18);

        jSplitPane5.setDividerLocation(300);

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder("Models"));

        lt_modelClassification.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Model 1", "Model 2", "Model 3", "Model 4" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_modelClassification.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_modelClassificationMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(lt_modelClassification);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9)
        );

        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder("Test Dataset"));

        lt_testData.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Dataset 1", "Dataset 2", "Dataset 3" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_testData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_testDataMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(lt_testData);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14)
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(429, Short.MAX_VALUE))
        );

        jSplitPane5.setLeftComponent(jPanel19);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Result"));

        jScrollPane11.setViewportView(tp_resultClassification);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSplitPane5.setRightComponent(jPanel32);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane5, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane5, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tb_principal.addTab("Classification", jPanel10);

        jSplitPane2.setDividerLocation(300);

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Time Series"));

        lt_timeSeriesData.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Time Series 1", "Time Series 2" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lt_timeSeriesData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lt_timeSeriesDataMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(lt_timeSeriesData);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));

        jLabel6.setText("Speed:");

        gp_settings.add(jRadioButton1);
        jRadioButton1.setText("Slow");

        gp_settings.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Fast");

        gp_settings.add(jRadioButton3);
        jRadioButton3.setText("None");

        jButton2.setText("Show Page Hinkley");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setText("Page Hinkley:");

        gp_pageHinkley.add(jRadioButton6);
        jRadioButton6.setSelected(true);
        jRadioButton6.setText("Yes");

        gp_pageHinkley.add(jRadioButton7);
        jRadioButton7.setText("No");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder("Discretization"));

        gp_algDiscretization.add(rd_sax);
        rd_sax.setText("SAX");

        gp_algDiscretization.add(rd_sfa);
        rd_sfa.setSelected(true);
        rd_sfa.setText("SFA");

        jLabel4.setText("Offset:");

        tf_symOffset.setText("1");

        tf_symWordLength.setText("8");

        jLabel3.setText("Word length:");

        jLabel2.setText("Symbols:");

        tf_symSymbolAlphabet.setText("4");

        tf_symWindow.setText("25");

        jLabel1.setText("Windows:");

        jLabel9.setText("N. reduction:");

        gp_numerosityReduction.add(rd_numReductionYes);
        rd_numReductionYes.setSelected(true);
        rd_numReductionYes.setText("Yes");

        gp_numerosityReduction.add(rd_numReductionNo);
        rd_numReductionNo.setText("No");

        jLabel10.setText("Alingment:");

        gp_alingment.add(rd_alingmentYes);
        rd_alingmentYes.setSelected(true);
        rd_alingmentYes.setText("Yes");

        gp_alingment.add(rd_alingmentNo);
        rd_alingmentNo.setText("No");

        jLabel7.setText("Algoritmos:");

        jLabel12.setText("Normalized:");

        gp_normalized.add(rd_normYes);
        rd_normYes.setSelected(true);
        rd_normYes.setText("Yes");

        gp_normalized.add(td_normNo);
        td_normNo.setText("No");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(rd_sax, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rd_sfa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(tf_symWindow)
                    .addComponent(tf_symSymbolAlphabet)
                    .addComponent(tf_symWordLength)
                    .addComponent(tf_symOffset)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(rd_alingmentYes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rd_numReductionYes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rd_normYes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(td_normNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rd_numReductionNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rd_alingmentNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rd_sax)
                    .addComponent(rd_sfa)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_symWindow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tf_symSymbolAlphabet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tf_symWordLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tf_symOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rd_normYes)
                    .addComponent(td_normNo))
                .addGap(5, 5, 5)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rd_numReductionYes)
                    .addComponent(rd_numReductionNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rd_alingmentYes))
                    .addComponent(rd_alingmentNo))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Models"));

        gp_model.add(rd_algSAXVSM);
        rd_algSAXVSM.setText("SAX-VSM");

        gp_model.add(rd_algBOSS);
        rd_algBOSS.setText("BOSS");

        gp_model.add(rd_algBOSSVS);
        rd_algBOSSVS.setText("BOSS-VS");

        gp_model.add(rd_algWeasel);
        rd_algWeasel.setSelected(true);
        rd_algWeasel.setText("WEASEL");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(rd_algBOSSVS, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rd_algSAXVSM, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rd_algBOSS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rd_algWeasel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rd_algSAXVSM)
                    .addComponent(rd_algBOSS))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rd_algBOSSVS)
                    .addComponent(rd_algWeasel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(118, 118, 118))
        );

        jScrollPane4.setViewportView(jPanel11);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
        );

        jSplitPane2.setLeftComponent(jPanel6);

        pn_graphicLine.setBorder(javax.swing.BorderFactory.createTitledBorder("."));

        javax.swing.GroupLayout pn_graphicLineLayout = new javax.swing.GroupLayout(pn_graphicLine);
        pn_graphicLine.setLayout(pn_graphicLineLayout);
        pn_graphicLineLayout.setHorizontalGroup(
            pn_graphicLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pn_graphicLineLayout.setVerticalGroup(
            pn_graphicLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 165, Short.MAX_VALUE)
        );

        pn_graphicBar.setBorder(javax.swing.BorderFactory.createTitledBorder("."));

        javax.swing.GroupLayout pn_graphicBarLayout = new javax.swing.GroupLayout(pn_graphicBar);
        pn_graphicBar.setLayout(pn_graphicBarLayout);
        pn_graphicBarLayout.setHorizontalGroup(
            pn_graphicBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 407, Short.MAX_VALUE)
        );
        pn_graphicBarLayout.setVerticalGroup(
            pn_graphicBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ta_symbolic.setColumns(20);
        ta_symbolic.setRows(5);
        jScrollPane16.setViewportView(ta_symbolic);

        jTabbedPane1.addTab("Log", jScrollPane16);

        tb_words.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tb_words.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_wordsMouseClicked(evt);
            }
        });
        tb_words.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tb_wordsKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tb_words);

        lb_tableInfo.setText("jLabel1");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
            .addComponent(lb_tableInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_tableInfo))
        );

        jTabbedPane1.addTab("Words", jPanel17);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pn_graphicLine, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_graphicBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(pn_graphicLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pn_graphicBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1)))
        );

        jSplitPane2.setRightComponent(jPanel8);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );

        tb_principal.addTab("Symbolic", jPanel5);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btt_openRawData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/open136.png"))); // NOI18N
        btt_openRawData.setToolTipText("Open Raw Data");
        btt_openRawData.setFocusable(false);
        btt_openRawData.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_openRawData.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_openRawData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_openRawDataActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_openRawData);

        btt_openModel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/open436.png"))); // NOI18N
        btt_openModel.setToolTipText("Open Model");
        btt_openModel.setFocusable(false);
        btt_openModel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_openModel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_openModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_openModelActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_openModel);

        btt_mergeDataset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/merge36.png"))); // NOI18N
        btt_mergeDataset.setToolTipText("Merge dataset");
        btt_mergeDataset.setFocusable(false);
        btt_mergeDataset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_mergeDataset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_mergeDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_mergeDatasetActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_mergeDataset);
        jToolBar1.add(jSeparator4);

        btt_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit36.png"))); // NOI18N
        btt_edit.setToolTipText("Edit");
        btt_edit.setFocusable(false);
        btt_edit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_edit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_editActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_edit);

        btt_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete36.png"))); // NOI18N
        btt_delete.setToolTipText("Delete");
        btt_delete.setFocusable(false);
        btt_delete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_delete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_deleteActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_delete);
        jToolBar1.add(jSeparator1);

        btt_executar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/executar136.png"))); // NOI18N
        btt_executar.setFocusable(false);
        btt_executar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_executar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_executar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_executarActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_executar);

        btt_executar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/executar236.png"))); // NOI18N
        btt_executar2.setFocusable(false);
        btt_executar2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_executar2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_executar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_executar2ActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_executar2);

        btt_reset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/resetar36.png"))); // NOI18N
        btt_reset.setToolTipText("Reset");
        btt_reset.setFocusable(false);
        btt_reset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_reset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_resetActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_reset);
        jToolBar1.add(jSeparator3);

        btt_pause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pause36.png"))); // NOI18N
        btt_pause.setFocusable(false);
        btt_pause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_pause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_pauseActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_pause);

        btt_stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/stop36.png"))); // NOI18N
        btt_stop.setFocusable(false);
        btt_stop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_stop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_stopActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_stop);
        jToolBar1.add(jSeparator2);

        btt_exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit36.png"))); // NOI18N
        btt_exit.setToolTipText("Exit");
        btt_exit.setFocusable(false);
        btt_exit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btt_exit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btt_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btt_exitActionPerformed(evt);
            }
        });
        jToolBar1.add(btt_exit);
        jToolBar1.add(jSeparator5);
        jToolBar1.add(sx_busy);

        jMenu3.setText("Options");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Date/Time Format");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem2.setText("Separator Column");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem4.setText("Class Name");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem5.setText("Timestamp Name");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Symbolic");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem6.setText("PageHinkley");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("Help");

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem9.setText("Options");
        jMenu2.add(jMenuItem9);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem3.setText("About");
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tb_principal)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tb_principal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dp_preprocessingMousePressed(MouseEvent evt) {//GEN-FIRST:event_dp_preprocessingMousePressed
        if (evt.isPopupTrigger()) {
            pp_graphic.show(this, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_dp_preprocessingMousePressed

    private void dp_preprocessingMouseReleased(MouseEvent evt) {//GEN-FIRST:event_dp_preprocessingMouseReleased
        if (evt.isPopupTrigger()) {
            pp_graphic.show(this, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_dp_preprocessingMouseReleased

    private void ppi_cascadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppi_cascadeActionPerformed
        desktopView.janelaCascata(dp_preprocessing);
    }//GEN-LAST:event_ppi_cascadeActionPerformed

    private void ppi_sideBySideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppi_sideBySideActionPerformed
        desktopView.janelaLadoLado(dp_preprocessing);
    }//GEN-LAST:event_ppi_sideBySideActionPerformed

    private void ppi_removeBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppi_removeBoxActionPerformed
        desktopView.removeBox(pn_box, ConstGeneral.CURRENT_BOX.getName());
    }//GEN-LAST:event_ppi_removeBoxActionPerformed

    private void tb_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tb_1ActionPerformed
        apperHideMenu(tb_1, pn_1);
    }//GEN-LAST:event_tb_1ActionPerformed

    private void tb_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tb_2ActionPerformed
        apperHideMenu(tb_2, pn_2);
    }//GEN-LAST:event_tb_2ActionPerformed

    private void tb_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tb_3ActionPerformed
        apperHideMenu(tb_3, pn_3);
    }//GEN-LAST:event_tb_3ActionPerformed

    private void tb_principalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tb_principalStateChanged
        updateHandleTabedPane();
    }//GEN-LAST:event_tb_principalStateChanged

    private void lt_classifierMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_classifierMouseClicked
        if (lt_classifier.getSelectedIndex() != -1 && evt.getButton() == MouseEvent.BUTTON3) {
            pp_classifier.show(lt_classifier, evt.getX(), evt.getY());
        } else {
            showEvaluation();
        }
    }//GEN-LAST:event_lt_classifierMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeWindow();
    }//GEN-LAST:event_formWindowClosing

    private void btt_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_exitActionPerformed
        closeWindow();
    }//GEN-LAST:event_btt_exitActionPerformed

    private void btt_openRawDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_openRawDataActionPerformed
        String currentFile = componentView.openFileChooser(this, new TextFilter());
        if (currentFile != null) {
            switch (tb_principal.getSelectedIndex()) {
                case 0:
                    FileUtil.copyFile(currentFile, ConstDataset.DS_RAW);
                    SwingUtil.listFilesList(lt_rawData, ConstDataset.DS_RAW);
                    break;
                case 1:
                    FileUtil.copyFile(currentFile, ConstDataset.DS_TRAIN);
                    SwingUtil.listFilesList(lt_trainData, ConstDataset.DS_TRAIN);
                    break;
                case 2:
                    FileUtil.copyFile(currentFile, ConstDataset.DS_TEST);
                    SwingUtil.listFilesList(lt_testData, ConstDataset.DS_TEST);
                    break;
                case 3:
                    FileUtil.copyFile(currentFile, ConstDataset.DS_TIME_SERIES);
                    SwingUtil.listFilesList(lt_timeSeriesData, ConstDataset.DS_TIME_SERIES);
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btt_openRawDataActionPerformed

    private void btt_openModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_openModelActionPerformed
        String currentFile = componentView.openFileChooser(this, new TextFilter());
        if (currentFile != null) {
            FileUtil.copyFile(currentFile, ConstDataset.DS_MODEL);
            SwingUtil.listFilesList(lt_modelClassification, ConstDataset.DS_MODEL);
        }
    }//GEN-LAST:event_btt_openModelActionPerformed

    private void lt_rawDataMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_rawDataMouseClicked
        if (lt_rawData.getSelectedIndex() != -1) {
            if (evt.getButton() == MouseEvent.BUTTON3) {
                pp_file.show(lt_rawData, evt.getX(), evt.getY());
            } else if (evt.getButton() == MouseEvent.BUTTON1) {
                menuCollapsed();
                fillSignalList();
            }
        }
    }//GEN-LAST:event_lt_rawDataMouseClicked

    private void lt_trainDataMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_trainDataMouseClicked
        if (lt_trainData.getSelectedIndex() != -1 && evt.getButton() == MouseEvent.BUTTON3) {
            pp_file.show(lt_trainData, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_lt_trainDataMouseClicked

    private void lt_testDataMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_testDataMouseClicked
        if (lt_testData.getSelectedIndex() != -1 && evt.getButton() == MouseEvent.BUTTON3) {
            pp_file.show(lt_testData, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_lt_testDataMouseClicked

    private void ppi_editDatasetFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppi_editDatasetFileActionPerformed
        editDeleteDataset(ConstGeneral.PP_EDIT);
    }//GEN-LAST:event_ppi_editDatasetFileActionPerformed

    private void btt_mergeDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_mergeDatasetActionPerformed
        switch (tb_principal.getSelectedIndex()) {
            case 0:
                if (lt_rawData.getSelectedIndices().length == 2) {
                    String source1 = ConstDataset.DS_RAW + lt_rawData.getSelectedValuesList().get(0);
                    String source2 = ConstDataset.DS_RAW + lt_rawData.getSelectedValuesList().get(1);
                    MergeDataset view = new MergeDataset(this, true, source1, source2);
                    view.setVisible(true);
                } else {
                    messages.aviso("Select two raw datasets!");
                }
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btt_mergeDatasetActionPerformed

    private void ppi_deleteDatasetFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppi_deleteDatasetFileActionPerformed
        editDeleteDataset(ConstGeneral.PP_DELETE);
    }//GEN-LAST:event_ppi_deleteDatasetFileActionPerformed

    private void ppi_renameDatasetFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppi_renameDatasetFileActionPerformed
        switch (tb_principal.getSelectedIndex()) {
            case 0:
                componentView.renameDataset(lt_rawData, ConstDataset.DS_RAW);
                break;
            case 1:
                componentView.renameDataset(lt_trainData, ConstDataset.DS_TRAIN);
                break;
            case 2:
                if (lt_modelClassification.isFocusable()) {
                    componentView.renameDataset(lt_modelClassification, ConstDataset.DS_MODEL);
                } else if (lt_testData.isFocusable()) {
                    componentView.renameDataset(lt_testData, ConstDataset.DS_TEST);
                }
                break;
            case 3:
                componentView.renameDataset(lt_timeSeriesData, ConstDataset.DS_TIME_SERIES);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_ppi_renameDatasetFileActionPerformed

    private void lt_rawDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lt_rawDataKeyReleased
        menuCollapsed();
        fillSignalList();
    }//GEN-LAST:event_lt_rawDataKeyReleased

    private void bt_signalSelectionLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeftActionPerformed
        SwingUtil.changePickListLeftRight(lt_signalSelectionLeft, lt_signalSelectionRight);
    }//GEN-LAST:event_bt_signalSelectionLeftActionPerformed

    private void bt_signalSelectionRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionRightActionPerformed
        SwingUtil.changePickListRightLeft(lt_signalSelectionRight, lt_signalSelectionLeft);
    }//GEN-LAST:event_bt_signalSelectionRightActionPerformed

    private void bt_filtersLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_filtersLeftActionPerformed
        SwingUtil.changePickListLeftRight(lt_filtersLeft, lt_filtersRight);
    }//GEN-LAST:event_bt_filtersLeftActionPerformed

    private void bt_filtersRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_filtersRightActionPerformed
        SwingUtil.changePickListRightLeft(lt_filtersRight, lt_filtersLeft);
    }//GEN-LAST:event_bt_filtersRightActionPerformed

    private void bt_timeFeatureLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_timeFeatureLeftActionPerformed
        SwingUtil.changePickListLeftRight(lt_timeFeaturesLeft, lt_timeFeaturesRight);
    }//GEN-LAST:event_bt_timeFeatureLeftActionPerformed

    private void bt_timeFeatureRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_timeFeatureRightActionPerformed
        SwingUtil.changePickListRightLeft(lt_timeFeaturesRight, lt_timeFeaturesLeft);
    }//GEN-LAST:event_bt_timeFeatureRightActionPerformed

    private void bt_frequencyFeatureLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_frequencyFeatureLeftActionPerformed
        SwingUtil.changePickListLeftRight(lt_frequencyFeaturesLeft, lt_frequencyFeaturesRight);
    }//GEN-LAST:event_bt_frequencyFeatureLeftActionPerformed

    private void bt_frequencyFeatureRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_frequencyFeatureRightActionPerformed
        SwingUtil.changePickListRightLeft(lt_frequencyFeaturesRight, lt_frequencyFeaturesLeft);
    }//GEN-LAST:event_bt_frequencyFeatureRightActionPerformed

    private void lt_signalSelectionLeftMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_signalSelectionLeftMouseClicked
        if (evt.getClickCount() == 2) {
            SwingUtil.changePickListLeftRight(lt_signalSelectionLeft, lt_signalSelectionRight);
        }
    }//GEN-LAST:event_lt_signalSelectionLeftMouseClicked

    private void lt_signalSelectionRightMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_signalSelectionRightMouseClicked
        if (evt.getClickCount() == 2) {
            SwingUtil.changePickListRightLeft(lt_signalSelectionRight, lt_signalSelectionLeft);
        }
    }//GEN-LAST:event_lt_signalSelectionRightMouseClicked

    private void lt_filtersLeftMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_filtersLeftMouseClicked
        if (evt.getClickCount() == 2) {
            SwingUtil.changePickListLeftRight(lt_filtersLeft, lt_filtersRight);
        }
    }//GEN-LAST:event_lt_filtersLeftMouseClicked

    private void lt_filtersRightMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_filtersRightMouseClicked
        if (evt.getClickCount() == 2) {
            SwingUtil.changePickListRightLeft(lt_filtersRight, lt_filtersLeft);
        }
    }//GEN-LAST:event_lt_filtersRightMouseClicked

    private void lt_timeFeaturesRightMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_timeFeaturesRightMouseClicked
        if (evt.getClickCount() == 2) {
            SwingUtil.changePickListRightLeft(lt_timeFeaturesRight, lt_timeFeaturesLeft);
        }
    }//GEN-LAST:event_lt_timeFeaturesRightMouseClicked

    private void lt_frequencyFeaturesLeftMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_frequencyFeaturesLeftMouseClicked
        if (evt.getClickCount() == 2) {
            SwingUtil.changePickListLeftRight(lt_frequencyFeaturesLeft, lt_frequencyFeaturesRight);
        }
    }//GEN-LAST:event_lt_frequencyFeaturesLeftMouseClicked

    private void lt_frequencyFeaturesRightMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_frequencyFeaturesRightMouseClicked
        if (evt.getClickCount() == 2) {
            SwingUtil.changePickListRightLeft(lt_frequencyFeaturesRight, lt_frequencyFeaturesLeft);
        }
    }//GEN-LAST:event_lt_frequencyFeaturesRightMouseClicked

    private void lt_timeFeaturesLeftMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_timeFeaturesLeftMouseClicked
        if (evt.getClickCount() == 2) {
            SwingUtil.changePickListLeftRight(lt_timeFeaturesLeft, lt_timeFeaturesRight);
        }
    }//GEN-LAST:event_lt_timeFeaturesLeftMouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        String format = messages.inserirDadosComValorInicial("Inform the date format (dd-MM-yyyy HH:mm:ss)", ConstGeneral.DATE_FORMAT);
        if (!Validation.isEmptyString(format)) {
            ConstGeneral.DATE_FORMAT = format;
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btt_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_editActionPerformed
        editDeleteDataset(ConstGeneral.PP_EDIT);
    }//GEN-LAST:event_btt_editActionPerformed

    private void btt_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_deleteActionPerformed
        editDeleteDataset(ConstGeneral.PP_DELETE);
    }//GEN-LAST:event_btt_deleteActionPerformed

    private void lt_principalFeaturesLeftMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_principalFeaturesLeftMouseClicked
        if (evt.getClickCount() == 2) {
            SwingUtil.changePickListLeftRight(lt_principalFeaturesLeft, lt_principalFeaturesRight);
        }
    }//GEN-LAST:event_lt_principalFeaturesLeftMouseClicked

    private void bt_principalFeatureRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_principalFeatureRightActionPerformed
        SwingUtil.changePickListRightLeft(lt_principalFeaturesRight, lt_principalFeaturesLeft);
    }//GEN-LAST:event_bt_principalFeatureRightActionPerformed

    private void bt_principalFeatureLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_principalFeatureLeftActionPerformed
        SwingUtil.changePickListLeftRight(lt_principalFeaturesLeft, lt_principalFeaturesRight);
    }//GEN-LAST:event_bt_principalFeatureLeftActionPerformed

    private void lt_principalFeaturesRightMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_principalFeaturesRightMouseClicked
        if (evt.getClickCount() == 2) {
            SwingUtil.changePickListRightLeft(lt_principalFeaturesRight, lt_principalFeaturesLeft);
        }
    }//GEN-LAST:event_lt_principalFeaturesRightMouseClicked

    private void lt_modelClassificationMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lt_modelClassificationMouseClicked
        if (lt_modelClassification.getSelectedIndex() != -1 && evt.getButton() == MouseEvent.BUTTON3) {
            pp_file.show(lt_modelClassification, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_lt_modelClassificationMouseClicked

    private void ppi_saveClassifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppi_saveClassifierActionPerformed
        trainView.saveModel(lt_classifier.getSelectedValue());
        updateClassifierList();
    }//GEN-LAST:event_ppi_saveClassifierActionPerformed

    private void ppi_renameClassifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppi_renameClassifierActionPerformed
        trainView.renameClassifier(lt_classifier.getSelectedValue());
        updateClassifierList();
    }//GEN-LAST:event_ppi_renameClassifierActionPerformed

    private void ppi_deleteClassifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppi_deleteClassifierActionPerformed
        trainView.deleteClassifier(lt_classifier.getSelectedValue());
        updateClassifierList();
    }//GEN-LAST:event_ppi_deleteClassifierActionPerformed

    private void lt_classifierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lt_classifierKeyReleased
        showEvaluation();
    }//GEN-LAST:event_lt_classifierKeyReleased

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        String separator = messages.inserirDadosComValorInicial("Inform the Separator type (','';' ')", ConstDataset.SEPARATOR);
        if (!Validation.isEmptyString(separator)) {
            ConstDataset.SEPARATOR = separator;
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        String classe = messages.inserirDadosComValorInicial("Inform the Class column name", ConstDataset.CLASS);
        if (!Validation.isEmptyString(classe)) {
            ConstDataset.CLASS = classe;
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void tb_desktopStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tb_desktopStateChanged
        resetProcessingLists();
    }//GEN-LAST:event_tb_desktopStateChanged

    private void ppi_viewDatasetBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppi_viewDatasetBoxActionPerformed
        desktopView.editData();
    }//GEN-LAST:event_ppi_viewDatasetBoxActionPerformed

    private void btt_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_resetActionPerformed
        switch (tb_principal.getSelectedIndex()) {
            case 0:
                Thread thread = new Thread(this::executeReset);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btt_resetActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String currentFile = componentView.openFileChooser(this, new TextFilter());
        if (currentFile != null) {
            FileUtil.saveFile(tp_resultTrain.getText(), currentFile + ".txt");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        String classe = messages.inserirDadosComValorInicial("Inform the Timestamp column name", ConstDataset.TIMESTAMP);
        if (!Validation.isEmptyString(classe)) {
            ConstDataset.TIMESTAMP = classe;
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void bt_signalSelectionLeft1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeft1ActionPerformed
        SwingUtil.changePickListLeftRightAll(lt_signalSelectionLeft, lt_signalSelectionRight);
    }//GEN-LAST:event_bt_signalSelectionLeft1ActionPerformed

    private void bt_signalSelectionLeft2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeft2ActionPerformed
        SwingUtil.changePickListRightLeftAll(lt_signalSelectionRight, lt_signalSelectionLeft);
    }//GEN-LAST:event_bt_signalSelectionLeft2ActionPerformed

    private void bt_signalSelectionLeft3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeft3ActionPerformed
        SwingUtil.changePickListLeftRightAll(lt_filtersLeft, lt_filtersRight);
    }//GEN-LAST:event_bt_signalSelectionLeft3ActionPerformed

    private void bt_signalSelectionLeft4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeft4ActionPerformed
        SwingUtil.changePickListRightLeftAll(lt_filtersRight, lt_filtersLeft);
    }//GEN-LAST:event_bt_signalSelectionLeft4ActionPerformed

    private void bt_signalSelectionLeft5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeft5ActionPerformed
        SwingUtil.changePickListLeftRightAll(lt_principalFeaturesLeft, lt_principalFeaturesRight);
    }//GEN-LAST:event_bt_signalSelectionLeft5ActionPerformed

    private void bt_signalSelectionLeft6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeft6ActionPerformed
        SwingUtil.changePickListRightLeftAll(lt_principalFeaturesRight, lt_principalFeaturesLeft);
    }//GEN-LAST:event_bt_signalSelectionLeft6ActionPerformed

    private void bt_signalSelectionLeft7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeft7ActionPerformed
        SwingUtil.changePickListLeftRightAll(lt_timeFeaturesLeft, lt_timeFeaturesRight);
    }//GEN-LAST:event_bt_signalSelectionLeft7ActionPerformed

    private void bt_signalSelectionLeft8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeft8ActionPerformed
        SwingUtil.changePickListRightLeftAll(lt_timeFeaturesRight, lt_timeFeaturesLeft);
    }//GEN-LAST:event_bt_signalSelectionLeft8ActionPerformed

    private void bt_signalSelectionLeft9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeft9ActionPerformed
        SwingUtil.changePickListLeftRightAll(lt_frequencyFeaturesLeft, lt_frequencyFeaturesRight);
    }//GEN-LAST:event_bt_signalSelectionLeft9ActionPerformed

    private void bt_signalSelectionLeft10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_signalSelectionLeft10ActionPerformed
        SwingUtil.changePickListRightLeftAll(lt_frequencyFeaturesRight, lt_frequencyFeaturesLeft);
    }//GEN-LAST:event_bt_signalSelectionLeft10ActionPerformed

    private void lt_timeSeriesDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lt_timeSeriesDataMouseClicked
        if (lt_timeSeriesData.getSelectedIndex() != -1 && evt.getButton() == MouseEvent.BUTTON3) {
            pp_file.show(lt_timeSeriesData, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_lt_timeSeriesDataMouseClicked

    private void btt_pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_pauseActionPerformed
        pausePlay();
    }//GEN-LAST:event_btt_pauseActionPerformed

    private void btt_executarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_executarActionPerformed
        switch (tb_principal.getSelectedIndex()) {
            case 0:
                if (play == null || !play.isAlive()) {
                    createInternalfame = true;
                    play = new Thread(this::executeData);
                    play.setPriority(Thread.MAX_PRIORITY);
                    play.start();
                } else {
                    messages.aviso("Streaming running!");
                }
                break;
            case 1:
                if (play == null || !play.isAlive()) {
                    Thread thread1 = new Thread(this::prepareTrain);
                    thread1.setPriority(Thread.MAX_PRIORITY);
                    thread1.start();
                } else {
                    messages.aviso("Streaming running!");
                }
                break;
            case 2:
                if (play == null || !play.isAlive()) {
                    Thread thread2 = new Thread(this::executeClassification);
                    thread2.setPriority(Thread.MAX_PRIORITY);
                    thread2.start();
                } else {
                    messages.aviso("Streaming running!");
                }
                break;
            case 3:
                if (play == null || !play.isAlive()) {
                    play = new Thread(this::executeSymbolic);
                    play.setPriority(Thread.MAX_PRIORITY);
                    play.start();
                    paused = false;
                } else {
                    messages.aviso("Streaming running!");
                }
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btt_executarActionPerformed

    private void btt_executar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_executar2ActionPerformed
        switch (tb_principal.getSelectedIndex()) {
            case 0:
                Thread thread = new Thread(this::executeUpdateData);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btt_executar2ActionPerformed

    private void btt_stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btt_stopActionPerformed
        if (play != null && play.isAlive()) {
            play.stop();
        }
        switch (tb_principal.getSelectedIndex()) {
            case 0:
                stopInternalFrameProcess();
                break;
            case 3:
                clearLineGraphic();
                clearBarGraphic();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btt_stopActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        String format = messages.inserirDadosComValorInicial("Inform the percentage to fall threshold",
                ConstGeneral.PERCENT_QUEDA_THRESHOULD + "");
        if (format != null && Validation.isDouble(format)) {
            ConstGeneral.PERCENT_QUEDA_THRESHOULD = Double.parseDouble(format);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void tb_wordsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_wordsKeyReleased
        paintWordInterval();
    }//GEN-LAST:event_tb_wordsKeyReleased

    private void tb_wordsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_wordsMouseClicked
        paintWordInterval();
    }//GEN-LAST:event_tb_wordsMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        symbolicView.showPageHinkleyChanges();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void lt_trainDataTestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lt_trainDataTestMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lt_trainDataTestMouseClicked

    private void ck_trainUserTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck_trainUserTestActionPerformed
        if (ck_trainUserTest.isSelected()) {
            sp_trainTest.setVisible(true);
            SwingUtil.listFilesList(lt_trainDataTest, ConstDataset.DS_TEST);
        } else {
            sp_trainTest.setVisible(false);
        }
        pn_trainTest.updateUI();
    }//GEN-LAST:event_ck_trainUserTestActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bg_graphic;
    private javax.swing.JButton bt_filtersLeft;
    private javax.swing.JButton bt_filtersRight;
    private javax.swing.JButton bt_frequencyFeatureLeft;
    private javax.swing.JButton bt_frequencyFeatureRight;
    private javax.swing.JButton bt_principalFeatureLeft;
    private javax.swing.JButton bt_principalFeatureRight;
    private javax.swing.JButton bt_signalSelectionLeft;
    private javax.swing.JButton bt_signalSelectionLeft1;
    private javax.swing.JButton bt_signalSelectionLeft10;
    private javax.swing.JButton bt_signalSelectionLeft2;
    private javax.swing.JButton bt_signalSelectionLeft3;
    private javax.swing.JButton bt_signalSelectionLeft4;
    private javax.swing.JButton bt_signalSelectionLeft5;
    private javax.swing.JButton bt_signalSelectionLeft6;
    private javax.swing.JButton bt_signalSelectionLeft7;
    private javax.swing.JButton bt_signalSelectionLeft8;
    private javax.swing.JButton bt_signalSelectionLeft9;
    private javax.swing.JButton bt_signalSelectionRight;
    private javax.swing.JButton bt_timeFeatureLeft;
    private javax.swing.JButton bt_timeFeatureRight;
    private javax.swing.JButton btt_delete;
    private javax.swing.JButton btt_edit;
    private javax.swing.JButton btt_executar;
    private javax.swing.JButton btt_executar2;
    private javax.swing.JButton btt_exit;
    private javax.swing.JButton btt_mergeDataset;
    private javax.swing.JButton btt_openModel;
    private javax.swing.JButton btt_openRawData;
    private javax.swing.JButton btt_pause;
    private javax.swing.JButton btt_reset;
    private javax.swing.JButton btt_stop;
    private javax.swing.JCheckBox ck_trainUserTest;
    private javax.swing.JDesktopPane dp_preprocessing;
    private javax.swing.ButtonGroup gp_algDiscretization;
    private javax.swing.ButtonGroup gp_alingment;
    private javax.swing.ButtonGroup gp_model;
    private javax.swing.ButtonGroup gp_normalized;
    private javax.swing.ButtonGroup gp_numerosityReduction;
    private javax.swing.ButtonGroup gp_pageHinkley;
    private javax.swing.ButtonGroup gp_settings;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JSplitPane jSplitPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lb_tableInfo;
    private javax.swing.JList<String> lt_algorithms;
    private javax.swing.JList<String> lt_classifier;
    private javax.swing.JList<String> lt_filtersLeft;
    private javax.swing.JList<String> lt_filtersRight;
    private javax.swing.JList<String> lt_frequencyFeaturesLeft;
    private javax.swing.JList<String> lt_frequencyFeaturesRight;
    private javax.swing.JList<String> lt_modelClassification;
    private javax.swing.JList<String> lt_principalFeaturesLeft;
    private javax.swing.JList<String> lt_principalFeaturesRight;
    private javax.swing.JList<String> lt_rawData;
    private javax.swing.JList<String> lt_signalSelectionLeft;
    private javax.swing.JList<String> lt_signalSelectionRight;
    private javax.swing.JList<String> lt_testData;
    private javax.swing.JList<String> lt_timeFeaturesLeft;
    private javax.swing.JList<String> lt_timeFeaturesRight;
    private javax.swing.JList<String> lt_timeSeriesData;
    private javax.swing.JList<String> lt_trainData;
    private javax.swing.JList<String> lt_trainDataTest;
    private javax.swing.JPanel pn_1;
    private javax.swing.JPanel pn_2;
    private javax.swing.JPanel pn_3;
    private javax.swing.JPanel pn_box;
    private javax.swing.JPanel pn_graphicBar;
    private javax.swing.JPanel pn_graphicLine;
    private javax.swing.JPanel pn_menu;
    private javax.swing.JPanel pn_trainTest;
    private javax.swing.JPopupMenu pp_box;
    private javax.swing.JPopupMenu pp_classifier;
    private javax.swing.JPopupMenu pp_file;
    private javax.swing.JPopupMenu pp_graphic;
    private javax.swing.JMenuItem ppi_cascade;
    private javax.swing.JMenuItem ppi_deleteClassifier;
    private javax.swing.JMenuItem ppi_deleteDatasetFile;
    private javax.swing.JMenuItem ppi_editDatasetFile;
    private javax.swing.JMenuItem ppi_removeBox;
    private javax.swing.JMenuItem ppi_renameClassifier;
    private javax.swing.JMenuItem ppi_renameDatasetFile;
    private javax.swing.JMenuItem ppi_saveClassifier;
    private javax.swing.JMenuItem ppi_sideBySide;
    private javax.swing.JMenuItem ppi_viewDatasetBox;
    private javax.swing.JRadioButton rd_algBOSS;
    private javax.swing.JRadioButton rd_algBOSSVS;
    private javax.swing.JRadioButton rd_algSAXVSM;
    private javax.swing.JRadioButton rd_algWeasel;
    private javax.swing.JRadioButton rd_alingmentNo;
    private javax.swing.JRadioButton rd_alingmentYes;
    private javax.swing.JRadioButton rd_normYes;
    private javax.swing.JRadioButton rd_numReductionNo;
    private javax.swing.JRadioButton rd_numReductionYes;
    private javax.swing.JRadioButton rd_sax;
    private javax.swing.JRadioButton rd_sfa;
    private javax.swing.JSlider sl_overlap;
    private javax.swing.JScrollPane sp_trainTest;
    private org.jdesktop.swingx.JXBusyLabel sx_busy;
    private javax.swing.JTextArea ta_symbolic;
    private javax.swing.JToggleButton tb_1;
    private javax.swing.JToggleButton tb_2;
    private javax.swing.JToggleButton tb_3;
    private javax.swing.JTabbedPane tb_desktop;
    private javax.swing.JTabbedPane tb_principal;
    private javax.swing.JTable tb_words;
    private javax.swing.JRadioButton td_normNo;
    private javax.swing.JTextField tf_frequency;
    private javax.swing.JTextField tf_symOffset;
    private javax.swing.JTextField tf_symSymbolAlphabet;
    private javax.swing.JTextField tf_symWindow;
    private javax.swing.JTextField tf_symWordLength;
    private javax.swing.JTextField tf_windowSize;
    private javax.swing.JTextPane tp_resultClassification;
    private javax.swing.JTextPane tp_resultTrain;
    // End of variables declaration//GEN-END:variables

    private void closeWindow() {
        Messages msg = new Messages();
        if (msg.confirmacao("Are you sure close window?")) {
            System.exit(0);
        }
    }

    private void screenDimension() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (screenSize.getWidth() * 0.75f),
                (int) (screenSize.getHeight() * 0.75f));
        setLocationRelativeTo(null);
    }

    private void menuCollapsed() {
        tb_1.setSelected(true);
        pn_1.setVisible(true);
        tb_2.setEnabled(false);
        pn_2.setVisible(false);
        tb_3.setEnabled(false);
        pn_3.setVisible(false);
    }

    private void apperHideMenu(JToggleButton tb, JPanel panel) {
        if (tb.isSelected()) {
            panel.setVisible(true);
        } else {
            panel.setVisible(false);
        }
    }

    private void resetSignalSelection() {
        lt_signalSelectionLeft.setModel(new DefaultListModel<>());
        lt_signalSelectionRight.setModel(new DefaultListModel<>());
    }

    private void resetProcessingLists() {
        desktopView.fillFiltersList(lt_filtersLeft);
        lt_filtersRight.setModel(new DefaultListModel<>());
        desktopView.fillPrincipalFeaturesList(lt_principalFeaturesLeft);
        lt_principalFeaturesRight.setModel(new DefaultListModel<>());
        desktopView.fillTimeFeaturesListLeft(lt_timeFeaturesLeft);
        lt_timeFeaturesRight.setModel(new DefaultListModel<>());
        desktopView.fillFrequencyFeaturesListLeft(lt_frequencyFeaturesLeft);
        lt_frequencyFeaturesRight.setModel(new DefaultListModel<>());
    }

    private void fillSignalList() {
        LinkedList<String> columns = HandleGenericDataset.extractNamesColumnFromFile(ConstDataset.SEPARATOR,
                ConstDataset.DS_RAW + lt_rawData.getSelectedValue());
        SwingUtil.fillList(lt_signalSelectionLeft, columns);
        lt_signalSelectionRight.setModel(new DefaultListModel<>());
    }

    /*
        Publics methods
     */
    public void updateHandleTabedPane() {
        switch (tb_principal.getSelectedIndex()) {
            case 0:
                btt_openModel.setVisible(false);
                btt_mergeDataset.setVisible(true);
                btt_executar.setVisible(true);
                btt_executar2.setVisible(true);
                btt_reset.setVisible(true);
                btt_pause.setVisible(true);
                btt_stop.setVisible(true);
                SwingUtil.listFilesList(lt_rawData, ConstDataset.DS_RAW);
                break;
            case 1:
                btt_openModel.setVisible(false);
                btt_mergeDataset.setVisible(false);
                btt_executar.setVisible(true);
                btt_executar2.setVisible(false);
                btt_reset.setVisible(false);
                btt_pause.setVisible(true);
                btt_stop.setVisible(true);
                SwingUtil.listFilesList(lt_trainData, ConstDataset.DS_TRAIN);
                updateClassifierList();
                break;
            case 2:
                btt_openModel.setVisible(true);
                btt_mergeDataset.setVisible(false);
                btt_executar.setVisible(true);
                btt_executar2.setVisible(false);
                btt_reset.setVisible(false);
                btt_pause.setVisible(true);
                btt_stop.setVisible(true);
                SwingUtil.listFilesList(lt_modelClassification, ConstDataset.DS_MODEL);
                SwingUtil.listFilesList(lt_testData, ConstDataset.DS_TEST);
                break;
            case 3:
                btt_openModel.setVisible(false);
                btt_mergeDataset.setVisible(true);
                btt_executar.setVisible(true);
                btt_executar2.setVisible(false);
                btt_reset.setVisible(false);
                btt_pause.setVisible(true);
                btt_stop.setVisible(true);
                SwingUtil.listFilesList(lt_timeSeriesData, ConstDataset.DS_TIME_SERIES);
                break;
            default:
                break;
        }
    }

    public void updateConfigList(String nameDataset, List<String> signalSelection, List<String> filters, List<String> principalFeatures,
            List<String> timeFeatures, List<String> frequencyFeatures) {
        lt_rawData.setSelectedValue(nameDataset, true);
        resetSignalSelection();
        resetProcessingLists();
        fillSignalList();
        SwingUtil.fillPickListRight(lt_signalSelectionRight, lt_signalSelectionLeft, signalSelection);
        if (!filters.isEmpty() || !principalFeatures.isEmpty()) {
            SwingUtil.fillPickListRight(lt_filtersRight, lt_filtersLeft, filters);
            SwingUtil.fillPickListRight(lt_principalFeaturesRight, lt_principalFeaturesLeft, principalFeatures);
            tb_2.setSelected(true);
            pn_2.setVisible(true);
        } else {
            tb_2.setSelected(false);
            pn_2.setVisible(false);
        }
        if (!timeFeatures.isEmpty() || !frequencyFeatures.isEmpty()) {
            SwingUtil.fillPickListRight(lt_timeFeaturesRight, lt_timeFeaturesLeft, timeFeatures);
            SwingUtil.fillPickListRight(lt_frequencyFeaturesRight, lt_frequencyFeaturesLeft, frequencyFeatures);
            tb_3.setSelected(true);
            pn_3.setVisible(true);
        } else {
            tb_3.setSelected(false);
            pn_3.setVisible(false);
        }
    }

    public void updateClassifierList() {
        LinkedList<String> list = new LinkedList<>();
        for (String classif : trainView.getListClassiers().keySet()) {
            list.add(classif);
        }
        SwingUtil.fillList(lt_classifier, list);
    }

    public void updateSymbolicTab(WordRecord wordRecord, int qtdeWords) {
        if (!wordTableModel.getLinhas().contains(wordRecord)) {
            wordTableModel.getLinhas().add(wordRecord);
        }
        tb_words.updateUI();
        lb_tableInfo.setText(qtdeWords + " words");
    }

    public void updateSymbolicLog(String text) {
        if (text != null) {
            ta_symbolic.append(text + "\n");
        }
    }

    /*
        Toolbar
     */
    private void editDeleteDataset(int type) {
        switch (tb_principal.getSelectedIndex()) {
            case 0:
                componentView.editDeleteDataset(lt_rawData, ConstDataset.DS_RAW, type);
                break;
            case 1:
                componentView.editDeleteDataset(lt_trainData, ConstDataset.DS_TRAIN, type);
                break;
            case 2:
                if (lt_modelClassification.isFocusable()) {
                    componentView.editDeleteDataset(lt_modelClassification, ConstDataset.DS_MODEL, type);
                } else if (lt_testData.isFocusable()) {
                    componentView.editDeleteDataset(lt_testData, ConstDataset.DS_TEST, type);
                }
                break;
            case 3:
                componentView.editDeleteDataset(lt_timeSeriesData, ConstDataset.DS_TIME_SERIES, type);
                break;
            default:
                break;
        }
    }

    private void pausePlay() {
        if (play != null && play.isAlive()) {
            if (paused) {
                play.resume();

                paused = false;
            } else {
                play.suspend();
                paused = true;
            }
        }
    }

    private void stopInternalFrameProcess() {
        ConstGeneral.CURRENT_INTERNAL_FRAME.cleanLists();
        ConstGeneral.CURRENT_INTERNAL_FRAME.dispose();
        createInternalfame = false;
        menuCollapsed();
        fillSignalList();
        sx_busy.setBusy(false);
    }

    /*
        Pre-processing
     */
    private void executeReset() {
        if (desktopView.isSelectedData()) {
            desktopView.getDataFeatures().clear();
            desktopView.getClasses().clear();
            createInternalfame = false;
            executeData();
        }
    }

    private void executeData() {
        if (lt_rawData.getSelectedIndices().length != 0 && lt_signalSelectionRight.getModel().getSize() != 0) {
            sx_busy.setBusy(true);

            resetProcessingLists();
            prepareRawData();
            updateConfigLists();

            tb_2.setEnabled(true);
            tb_2.setSelected(false);
            tb_3.setEnabled(true);
            tb_3.setSelected(false);

            sx_busy.setBusy(false);
        } else {
            messages.aviso("Select a signal!");
        }
    }

    private void executeUpdateData() {
        if (desktopView.isSelectedData()) {
            sx_busy.setBusy(true);

            Float window = 0.0f;
            Integer hertz = 0;
            try {
                window = Float.parseFloat(tf_windowSize.getText());
                hertz = Integer.parseInt(tf_frequency.getText());
            } catch (NumberFormatException ex) {
                messages.bug("Invalid window or frequency values!");
                sx_busy.setBusy(false);
                return;
            }

            Integer overlap = sl_overlap.getValue();
            Integer offset = Math.round((overlap / 100f) * window * hertz);

            desktopView.signalProcessing(lt_filtersRight, window, hertz, offset);
            desktopView.principalFeatureProcessing(lt_principalFeaturesRight, window, hertz, offset);
            desktopView.featureExtraction(lt_timeFeaturesRight, lt_frequencyFeaturesRight, window, hertz, offset);

            desktopView.updateGraphic();
            updateConfigLists();

            sx_busy.setBusy(false);
        }
    }

    private void prepareRawData() {
        LinkedHashSet<GenericRowBean> data = HandleGenericDataset.bufferFileInMemory(ConstDataset.SEPARATOR,
                ConstDataset.DS_RAW + lt_rawData.getSelectedValue());
        HandleGenericDataset.setColumnClass(data);
        HandleGenericDataset.setColumnTimestamp(data);

        for (int i = 0; i < lt_signalSelectionLeft.getModel().getSize(); i++) {
            String signalUnselected = lt_signalSelectionLeft.getModel().getElementAt(i);
            Integer number = HandleGenericDataset.getNumberColumnByName(data, signalUnselected);
            HandleGenericDataset.removeColumnFromBuffer(data, number);
        }

        if (createInternalfame) {
            if (tb_desktop.getSelectedIndex() == 0) {
                desktopView.showInternalFrame(dp_preprocessing, lt_rawData.getSelectedValue());
                desktopView.addDataInternalFrame(data);
            } else if (tb_desktop.getSelectedIndex() == 1) {
                desktopView.addBox(pn_box, pp_box, lt_rawData.getSelectedValue());
                desktopView.addDataBox(data);
            }
        } else {
            desktopView.setData(data);
        }
    }

    private void updateConfigLists() {
        if (desktopView.isSelectedData()) {
            desktopView.updateConfigLists(SwingUtil.getElementsList(lt_signalSelectionRight), SwingUtil.getElementsList(lt_filtersRight),
                    SwingUtil.getElementsList(lt_principalFeaturesRight), SwingUtil.getElementsList(lt_timeFeaturesRight),
                    SwingUtil.getElementsList(lt_frequencyFeaturesRight));
        }
    }

    /*
     *   Train
     */
    private void prepareTrain() {
        sx_busy.setBusy(true);

        String train, algorithm, test;

        if (lt_trainData.getSelectedIndex() == -1 || lt_algorithms.getSelectedIndex() == -1) {
            messages.aviso("Select a train data and the classification algorithm!");
            return;
        }
        train = lt_trainData.getSelectedValue();
        algorithm = lt_algorithms.getSelectedValue();

        if (sp_trainTest.isVisible()) {
            if (lt_trainDataTest.getSelectedIndex() == -1) {
                messages.aviso("Select a test data!");
                return;
            }
            test = lt_trainDataTest.getSelectedValue();
            trainView.executeTrain(train, test, algorithm);
        } else {
            trainView.executeTrain(train, algorithm);
        }
        updateClassifierList();

        sx_busy.setBusy(false);
    }

    private void showEvaluation() {
        String classifier = lt_classifier.getSelectedValue();
        tp_resultTrain.setText(trainView.getListEvaluation().get(classifier));
    }

    /*
     *   Classification
     */
    private void executeClassification() {
        sx_busy.setBusy(true);

        if (lt_testData.getSelectedValue() == null || lt_modelClassification.getSelectedValue() == null) {
            messages.aviso("Select one test data and model!");
            return;
        }

        String testDir = ConstDataset.DS_TEST + lt_testData.getSelectedValue();
        String modelDir = ConstDataset.DS_MODEL + lt_modelClassification.getSelectedValue();

        WekaUtil classifySamples = new WekaUtil();
        int numberOfColumns = HandleGenericDataset.extractNamesColumnFromFile(ConstDataset.SEPARATOR, testDir).size();
        classifySamples.readData(testDir, numberOfColumns);
        Classifier classifier = classifySamples.readModel(modelDir);

        for (Instance instance : classifySamples.getData()) {
            tp_resultClassification.setText(tp_resultClassification.getText() + "\n" + classifySamples.classify(classifier, instance));
        }

        sx_busy.setBusy(false);
    }

    /*
     *   Symbolic Representation
     */
    private void executeSymbolic() {
        if (lt_timeSeriesData.getSelectedIndex() != -1) {
            clearLineGraphic();
            clearBarGraphic();
            ta_symbolic.setText("");
            setParameters();

            TimeSeries[] data = TimeSeriesLoader.loadVerticalData("0",
                    ConstDataset.DS_TIME_SERIES + lt_timeSeriesData.getSelectedValue(), false,
                    ConstDataset.SEPARATOR);

            symbolicView = new SymbolicView(lineGraphic, barGraphic, data);
            symbolicView.runDataset();
        } else {
            messages.aviso("Select a signal!");
        }
    }

    private void setParameters() {
        Parameters.WINDOW_SIZE = Integer.parseInt(tf_symWindow.getText());
        Parameters.WORD_LENGTH_PAA = Integer.parseInt(tf_symWordLength.getText());
        Parameters.SYMBOLS_ALPHABET_SIZE = Integer.parseInt(tf_symSymbolAlphabet.getText());
        Parameters.OFFSET = Integer.parseInt(tf_symOffset.getText());
        if (rd_sax.isSelected()) {
            Parameters.SFA = false;
        } else {
            Parameters.SFA = true;
        }
        if (rd_normYes.isSelected()) {
            Parameters.NORM = true;
        } else {
            Parameters.NORM = false;
        }
        if (rd_numReductionYes.isSelected()) {
            Parameters.NUM_REDUCTION = true;
        } else {
            Parameters.NUM_REDUCTION = false;
        }
        if (rd_alingmentYes.isSelected()) {
            Parameters.ALINGMENT = true;
        } else {
            Parameters.ALINGMENT = false;
        }
        if (rd_algBOSS.isSelected()) {
            Parameters.MODEL = ConstGeneral.AL_BOSS_MODEL;
        } else if (rd_algBOSSVS.isSelected()) {
            Parameters.MODEL = ConstGeneral.AL_BOSS_VS;
        } else if (rd_algWeasel.isSelected()) {
            Parameters.MODEL = ConstGeneral.AL_WEASEL;
        } else if (rd_algSAXVSM.isSelected()) {
            Parameters.MODEL = ConstGeneral.AL_SAX_VSM;
        }
    }

    private void addLineGraphic() {
        pn_graphicLine.removeAll();
        lineGraphic = new LineGraphic("Data Streaming", true);
        lineGraphic.setPreferredSize(pn_graphicLine.getSize());
        pn_graphicLine.setLayout(new BorderLayout());
        pn_graphicLine.add(lineGraphic, BorderLayout.CENTER);
        pn_graphicLine.updateUI();
    }

    private void addBarGraphic() {
        pn_graphicBar.removeAll();
        barGraphic = new BarGraphic("Words Histogram");
        barGraphic.setPreferredSize(pn_graphicBar.getSize());
        pn_graphicBar.setLayout(new BorderLayout());
        pn_graphicBar.add(barGraphic, BorderLayout.CENTER);
        pn_graphicBar.updateUI();
    }

    private void paintWordInterval() {
        if (lineGraphic != null) {
            WordRecord wordRecord = (WordRecord) wordTableModel.getValueAt(tb_words.getSelectedRow(), -1);
            lineGraphic.getTimeseriesPlot().clearDomainMarkers();
            for (WordInterval interval : wordRecord.getIntervals()) {
                lineGraphic.addMarker(interval.getPositionInit(), interval.getPositionEnd(), Color.blue);
            }
        }
    }

    private void clearLineGraphic() {
        lineGraphic.getSeriesCollection().removeAllSeries();
        lineGraphic.repaint();
    }

    public void clearBarGraphic() {
        barGraphic.getDatasetCollection().clear();
        barGraphic.repaint();
        wordTableModel.getLinhas().clear();
        tb_words.repaint();
    }

}
