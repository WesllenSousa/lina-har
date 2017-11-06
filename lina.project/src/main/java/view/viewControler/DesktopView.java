package view.viewControler;

import datasets.generic.GenericRowBean;
import java.awt.Component;
import java.beans.PropertyVetoException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import constants.ConstGeneral;
import controle.features.ProcessingFeatures;
import datasets.generic.HandleGenericDataset;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import util.Messages;
import view.EditDataset;
import view.manualviews.DropComponentes;
import view.manualviews.InternalFrame;
import view.manualviews.PainelBox;

/**
 *
 * @author Wesllen Sousa
 */
public class DesktopView {

    private final Messages messages = new Messages();
    private final ProcessingFeatures processingFeatures;

    private final int largura = 50, altura = 50, localX = 10, localY = 10;
    private Integer frameDistancia, proxFrameX = 0, proxFrameY = 0, larguraIFrame = 600, alturaIFrame = 500;

    private String selectedFeaturePanel;

    public DesktopView() {
        processingFeatures = new ProcessingFeatures(this);
    }

    /*
     *   Processing
     */
    public void signalProcessing(JList lt_filtersRight, Float window, Integer hertz, Integer offset) {
        LinkedList<String> methodsSignals = new LinkedList<>();
        if (lt_filtersRight.getModel().getSize() != 0) {
            for (int i = 0; i < lt_filtersRight.getModel().getSize(); i++) {
                methodsSignals.add(lt_filtersRight.getModel().getElementAt(i).toString());
            }
        }
        if (!methodsSignals.isEmpty()) {
            LinkedHashSet<LinkedList<String>> lineColumns = processingFeatures.applyPreprocessing(getData(), methodsSignals,
                    window * hertz, offset, hertz);
            processingFeatures.ajustLineColumns(lineColumns);
            if (!lineColumns.isEmpty()) {
                getData().clear();
                setData(HandleGenericDataset.convertLineColumnsToGenericData(lineColumns));
                HandleGenericDataset.addClassToGenericData(getData(), getClasses());
            }
        }
    }

    public void principalFeatureProcessing(JList lt_principalFeaturesRight, Float window, Integer hertz, Integer voltar) {
        LinkedList<String> principalFeatures = new LinkedList<>();
        if (lt_principalFeaturesRight.getModel().getSize() != 0) {
            for (int i = 0; i < lt_principalFeaturesRight.getModel().getSize(); i++) {
                principalFeatures.add(lt_principalFeaturesRight.getModel().getElementAt(i).toString());
            }
        }
        if (!principalFeatures.isEmpty()) {
            LinkedHashSet<LinkedList<String>> lineColumns = processingFeatures.applyPreprocessing(getData(), principalFeatures,
                    window * hertz, voltar, hertz);
            processingFeatures.ajustLineColumns(lineColumns);
            if (!lineColumns.isEmpty()) {
                getData().clear();
                setData(HandleGenericDataset.convertLineColumnsToGenericData(lineColumns));
                HandleGenericDataset.addClassToGenericData(getData(), getClasses());
            }
        }
    }

    public void featureExtraction(JList lt_timeFeaturesRight, JList lt_frequencyFeaturesRight, Float window, Integer hertz,
            Integer voltar) {
        LinkedList<String> methodsFeatures = new LinkedList<>();
        if (lt_timeFeaturesRight.getModel().getSize() != 0) {
            for (int i = 0; i < lt_timeFeaturesRight.getModel().getSize(); i++) {
                methodsFeatures.add(lt_timeFeaturesRight.getModel().getElementAt(i).toString());
            }
        }
        if (lt_frequencyFeaturesRight.getModel().getSize() != 0) {
            for (int i = 0; i < lt_frequencyFeaturesRight.getModel().getSize(); i++) {
                methodsFeatures.add(lt_frequencyFeaturesRight.getModel().getElementAt(i).toString());
            }
        }
        getDataFeatures().clear();
        if (!methodsFeatures.isEmpty()) {
            LinkedHashSet<LinkedList<String>> lineColumns = processingFeatures.applyPreprocessing(getData(), methodsFeatures,
                    window * hertz, voltar, hertz);
            if (!lineColumns.isEmpty()) {
                getDataFeatures().clear();
                setDataFeatures(HandleGenericDataset.convertLineColumnsToGenericData(lineColumns));
                HandleGenericDataset.addClassToGenericData(getDataFeatures(), getClasses());
            }
        }
    }

    /*
     *   INTERNAL FRAME
     */
    public void addDataInternalFrame(LinkedHashSet<GenericRowBean> data) {
        ConstGeneral.CURRENT_INTERNAL_FRAME.configData(data);
    }

    public void showInternalFrame(JDesktopPane dp_preprocessing, String dataset) {
        InternalFrame internalFrame = new InternalFrame(this, dataset);
        dp_preprocessing.add(internalFrame);
        internalFrame.show();

        frameDistancia = internalFrame.getHeight() - internalFrame.getContentPane().getHeight();
        proxFrameX += frameDistancia;
        proxFrameY += frameDistancia;
        if (proxFrameX + larguraIFrame > dp_preprocessing.getWidth()) {
            proxFrameX = 0;
        }
        if (proxFrameY + larguraIFrame > dp_preprocessing.getHeight()) {
            proxFrameY = 0;
        }

        janelaCascata(dp_preprocessing);
        internalFrame.toFront();
    }

    public void janelaCascata(JDesktopPane dp_preprocessing) {
        //retorna um array com todos frames da janela principal.
        JInternalFrame[] frames = dp_preprocessing.getAllFrames();
        int x = 10;
        int y = 10;
        for (int i = frames.length - 1; i >= 0; i--) {
            if (!frames[i].isIcon()) {  //se o frame não estiver iconizado.
                try {
                    frames[i].setMaximum(false); //tirna ele redimencionavel.
                    frames[i].reshape(x, y, larguraIFrame, alturaIFrame); //define sua posicao de tamanho.
                    x += frameDistancia;
                    y += frameDistancia;
                    if (proxFrameX + larguraIFrame > dp_preprocessing.getWidth()) {
                        x = 0;
                    }
                    if (proxFrameY + larguraIFrame > dp_preprocessing.getHeight()) {
                        y = 0;
                    }
                } catch (PropertyVetoException ex) {
                }
            }
        }
    }

    public void janelaLadoLado(JDesktopPane dp_preprocessing) {
        //retorna um array com todos frames da janela principal.
        JInternalFrame[] frames = dp_preprocessing.getAllFrames();
        //conta os quadros que não foram iconizados.
        int contarFrames = 0;
        for (int i = 0; i < frames.length; i++) {
            if (!frames[i].isIcon()) {
                contarFrames++;
            }
        }
        int linhas = (int) Math.sqrt(contarFrames);//as linhas é a raiz quadrada da quantidade de frames.
        int colunas = contarFrames / linhas;      //as colunas é a divisao dos frames pelas linhas;
        int linhasExtra = contarFrames % linhas;  //linhas extras.

        int width_ = dp_preprocessing.getWidth() / colunas; //obtem a largura do dividido pelo numero de colunas.
        int heigth_ = dp_preprocessing.getHeight() / linhas;
        int linh = 0;
        int col = 0;

        for (int i = 0; i < frames.length; i++) {
            if (!frames[i].isIcon()) {
                try {
                    frames[i].setMaximum(false);
                    frames[i].reshape(col * width_, linh * heigth_, width_, heigth_);
                    linh++;
                    if (linh == linhas) {
                        linh = 0; //zera a linha.
                        col++;    //avança uma coluna.
                        if (col == colunas - linhasExtra) {
                            linhas++; //adiciona linha extra.
                            heigth_ = dp_preprocessing.getHeight() / linhas;
                        }
                    }
                } catch (PropertyVetoException ex) {
                }
            }
        }
    }

    /*
        Painel Box
     */
    public void addDataBox(LinkedHashSet<GenericRowBean> data) {
        ConstGeneral.CURRENT_BOX.configData(data);
    }

    public void addBox(JPanel panel, JPopupMenu popup, String nameDataset) {
        PainelBox painelBox = new PainelBox(popup, nameDataset);
        painelBox.setName("" + Math.random()); //ID único do painel. Serve para identificação.
        painelBox.setSize(largura, altura);
        DropComponentes drop = new DropComponentes();
        painelBox.addMouseListener(drop);
        painelBox.addMouseMotionListener(drop);
        panel.add(painelBox);
        organizarBox(panel);
    }

    public void removeBox(JPanel panel, String name) {
        ConstGeneral.CURRENT_BOX.cleanLists();
        ConstGeneral.CURRENT_BOX = null;
        Component[] components = panel.getComponents();
        for (int i = 0; i < components.length; i++) {
            JPanel cenario = (JPanel) components[i];
            if (cenario.getName().equals(name)) {
                panel.remove(i);
                break;
            }
        }
        panel.repaint();
    }

    private void organizarBox(JPanel panel) {
        Integer x = localX, y = localY;
        Double larguraTotal = panel.getSize().getWidth();
        Component[] components = panel.getComponents();
        for (int i = 0; i < components.length; i++) {
            JPanel cenario = (JPanel) components[i];
            if ((x + largura) >= larguraTotal) {
                x = localX;
                y += altura + localY;
            }
            cenario.setLocation(x, y);
            x += largura + localX;
        }
        panel.repaint();
    }

    public String getSelectedFeaturePanel() {
        return selectedFeaturePanel;
    }

    public void setSelectedFeaturePanel(String selectedFeaturePanel) {
        this.selectedFeaturePanel = selectedFeaturePanel;
    }

    public void editData() {
        LinkedHashSet<GenericRowBean> data = new LinkedHashSet<>();
        if (!getDataFeatures().isEmpty()) {
            data = getDataFeatures();
        } else {
            data = getData();
        }
        EditDataset view = new EditDataset(null, true, getNameDataset(), data);
        view.setVisible(true);
    }

    public boolean isSelectedData() {
        if (ConstGeneral.CURRENT_INTERNAL_FRAME == null && ConstGeneral.CURRENT_BOX == null) {
            messages.aviso("Select a desktop data!");
            return false;
        } else {
            return true;
        }
    }

    public void updateGraphic() {
        if (ConstGeneral.CURRENT_INTERNAL_FRAME != null) {
            ConstGeneral.CURRENT_INTERNAL_FRAME.updateGraphic();
        }
    }

    public void updateConfigLists(List<String> signalSelection, List<String> filters, List<String> principalFeatures,
            List<String> timeFeatures, List<String> frequencyFeatures) {
        if (ConstGeneral.CURRENT_INTERNAL_FRAME != null) {
            ConstGeneral.CURRENT_INTERNAL_FRAME.updateConfigLists(signalSelection, filters, principalFeatures, timeFeatures, frequencyFeatures);
        } else if (ConstGeneral.CURRENT_BOX != null) {
            ConstGeneral.CURRENT_BOX.updateConfigLists(signalSelection, filters, principalFeatures, timeFeatures, frequencyFeatures);
        }
    }

    /*
        Getters e Setters
     */
    public LinkedHashSet<GenericRowBean> getData() {
        if (ConstGeneral.CURRENT_INTERNAL_FRAME != null) {
            return ConstGeneral.CURRENT_INTERNAL_FRAME.getData();
        } else if (ConstGeneral.CURRENT_BOX != null) {
            return ConstGeneral.CURRENT_BOX.getData();
        }
        return null;
    }

    public void setData(LinkedHashSet<GenericRowBean> data) {
        if (ConstGeneral.CURRENT_INTERNAL_FRAME != null) {
            ConstGeneral.CURRENT_INTERNAL_FRAME.setData(data);
        } else if (ConstGeneral.CURRENT_BOX != null) {
            ConstGeneral.CURRENT_BOX.setData(data);
        }
    }

    public LinkedHashSet<GenericRowBean> getDataFeatures() {
        if (ConstGeneral.CURRENT_INTERNAL_FRAME != null) {
            return ConstGeneral.CURRENT_INTERNAL_FRAME.getDataFeatures();
        } else if (ConstGeneral.CURRENT_BOX != null) {
            return ConstGeneral.CURRENT_BOX.getDataFeatures();
        }
        return null;
    }

    public void setDataFeatures(LinkedHashSet<GenericRowBean> dataFeatures) {
        if (ConstGeneral.CURRENT_INTERNAL_FRAME != null) {
            ConstGeneral.CURRENT_INTERNAL_FRAME.setDataFeatures(dataFeatures);
        } else if (ConstGeneral.CURRENT_BOX != null) {
            ConstGeneral.CURRENT_BOX.setDataFeatures(dataFeatures);
        }
    }

    public void setClasses(LinkedList<String> classeFeatures) {
        if (ConstGeneral.CURRENT_INTERNAL_FRAME != null) {
            ConstGeneral.CURRENT_INTERNAL_FRAME.setClasseFeatures(classeFeatures);
        } else if (ConstGeneral.CURRENT_BOX != null) {
            ConstGeneral.CURRENT_BOX.setClasses(classeFeatures);
        }
    }

    public LinkedList<String> getClasses() {
        if (ConstGeneral.CURRENT_INTERNAL_FRAME != null) {
            return ConstGeneral.CURRENT_INTERNAL_FRAME.getClasses();
        } else if (ConstGeneral.CURRENT_BOX != null) {
            return ConstGeneral.CURRENT_BOX.getClasses();
        }
        return null;
    }

    public String getNameDataset() {
        if (ConstGeneral.CURRENT_INTERNAL_FRAME != null) {
            return ConstGeneral.CURRENT_INTERNAL_FRAME.getNameDataset();
        } else if (ConstGeneral.CURRENT_BOX != null) {
            return ConstGeneral.CURRENT_BOX.getNameDataset();
        }
        return null;
    }

    /*
        Lists
     */
    public void fillAlgorithmsList(JList list) {
        DefaultListModel defaultListModel = new DefaultListModel<>();
        defaultListModel.addElement(ConstGeneral.AL_DecisionTable);
        defaultListModel.addElement(ConstGeneral.AL_Decision_Stump);
        defaultListModel.addElement(ConstGeneral.AL_J48);
        defaultListModel.addElement(ConstGeneral.AL_Random_Forest);
        defaultListModel.addElement(ConstGeneral.AL_Random_Tree);
        defaultListModel.addElement(ConstGeneral.AL_KNN);
        defaultListModel.addElement(ConstGeneral.AL_NaiveBayes);
        defaultListModel.addElement(ConstGeneral.AL_SVM);
        defaultListModel.addElement(ConstGeneral.AL_Logistic);
        defaultListModel.addElement(ConstGeneral.AL_MultilayerPerceptron);
        defaultListModel.addElement(ConstGeneral.AL_AdaBoost);
        defaultListModel.addElement(ConstGeneral.AL_SAX_VSM);
        defaultListModel.addElement(ConstGeneral.AL_BOSS_MODEL);
        defaultListModel.addElement(ConstGeneral.AL_BOSS_VS);
        defaultListModel.addElement(ConstGeneral.AL_WEASEL);
        list.setModel(defaultListModel);
    }

    public void fillFiltersList(JList list) {
        DefaultListModel defaultListModel = new DefaultListModel<>();
//        defaultListModel.addElement(ConstGeneral.SP_GausianLowpass);
        defaultListModel.addElement(ConstGeneral.SP_ButterworthLowpass);
//        defaultListModel.addElement(ConstGeneral.SP_KALMAN);
        defaultListModel.addElement(ConstGeneral.SP_SingleLowPass);
        defaultListModel.addElement(ConstGeneral.SP_FourStageLowPass);
        defaultListModel.addElement(ConstGeneral.SP_bandPass);
        defaultListModel.addElement(ConstGeneral.SP_HighPass);
        defaultListModel.addElement(ConstGeneral.SP_MovingAverageFilter);
        list.setModel(defaultListModel);
    }

    public void fillPrincipalFeaturesList(JList list) {
        DefaultListModel defaultListModel = new DefaultListModel<>();
        defaultListModel.addElement(ConstGeneral.PF_MAGNITUDE);
        defaultListModel.addElement(ConstGeneral.PF_VerticalAxis);
        defaultListModel.addElement(ConstGeneral.PF_HorizontalAxis);
//        defaultListModel.addElement(ConstGeneral.PF_ForwardAxis);
//        defaultListModel.addElement(ConstGeneral.PF_SidewardAxis);
        defaultListModel.addElement(ConstGeneral.PF_FastFourierTrasform);
        defaultListModel.addElement(ConstGeneral.PF_HaarWavelet);
//        defaultListModel.addElement(ConstGeneral.PF_TiltAngle);
        list.setModel(defaultListModel);
    }

    public void fillTimeFeaturesListLeft(JList list) {
        DefaultListModel defaultListModel = new DefaultListModel<>();
        defaultListModel.addElement(ConstGeneral.TF_SUM);
        defaultListModel.addElement(ConstGeneral.TF_Energy);
        defaultListModel.addElement(ConstGeneral.TF_EnergyLog);
        defaultListModel.addElement(ConstGeneral.TF_Power);
        defaultListModel.addElement(ConstGeneral.TF_AbsoluteSum);
        defaultListModel.addElement(ConstGeneral.TF_MEAN);
        defaultListModel.addElement(ConstGeneral.TF_AbsoluteMean);
        defaultListModel.addElement(ConstGeneral.TF_AbsoluteMeanDeviation);
        defaultListModel.addElement(ConstGeneral.TF_SumSquareError);
        defaultListModel.addElement(ConstGeneral.TF_Variance);
        defaultListModel.addElement(ConstGeneral.TF_StandardDeviation);
        defaultListModel.addElement(ConstGeneral.TF_PearsonCoefficientVariation);
        defaultListModel.addElement(ConstGeneral.TF_MIN);
        defaultListModel.addElement(ConstGeneral.TF_MAX);
        defaultListModel.addElement(ConstGeneral.TF_AMPLITUDE);
        defaultListModel.addElement(ConstGeneral.TF_PeakAmplitude);
        defaultListModel.addElement(ConstGeneral.TF_EuclideanNorm);
        defaultListModel.addElement(ConstGeneral.TF_RootMeanSquare);
        defaultListModel.addElement(ConstGeneral.TF_Correlation);
        defaultListModel.addElement(ConstGeneral.TF_CrossCorrelation);
        defaultListModel.addElement(ConstGeneral.TF_AutoCorrelation);
        defaultListModel.addElement(ConstGeneral.TF_ZeroCrossingRatio);
        defaultListModel.addElement(ConstGeneral.TF_Quartile25);
        defaultListModel.addElement(ConstGeneral.TF_Quartile50);
        defaultListModel.addElement(ConstGeneral.TF_Quartile75);
        defaultListModel.addElement(ConstGeneral.TF_InterQuartileRange);
        defaultListModel.addElement(ConstGeneral.TF_AREA);
        defaultListModel.addElement(ConstGeneral.TF_AbsoluteArea);
        defaultListModel.addElement(ConstGeneral.TF_SignalMagnitudeMean);
        defaultListModel.addElement(ConstGeneral.TF_SignalMagnitudeArea);
        defaultListModel.addElement(ConstGeneral.TF_AverateMagnitudeDifferenceFuction);
        defaultListModel.addElement(ConstGeneral.TF_Skewness);
        defaultListModel.addElement(ConstGeneral.TF_Kurtosis);
        defaultListModel.addElement(ConstGeneral.TF_BinnedDistribution);
        list.setModel(defaultListModel);
    }

    public void fillFrequencyFeaturesListLeft(JList list) {
        DefaultListModel defaultListModel = new DefaultListModel<>();
        defaultListModel.addElement(ConstGeneral.FF_SpectralCentroid);
        defaultListModel.addElement(ConstGeneral.FF_SpectralEnergy);
        defaultListModel.addElement(ConstGeneral.FF_SpectralEnergyNormalized);
        defaultListModel.addElement(ConstGeneral.FF_SpectralPower);
//        defaultListModel.addElement(ConstGeneral.FF_SpectralEntropy);
        defaultListModel.addElement(ConstGeneral.FF_CoefficientsSum);
        defaultListModel.addElement(ConstGeneral.FF_Peaks);
        defaultListModel.addElement(ConstGeneral.FF_DComponents);
//        defaultListModel.addElement(ConstGeneral.FF_DominantFrequency);
        list.setModel(defaultListModel);
    }

}
