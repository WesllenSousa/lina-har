package controle.features;

import datasets.generic.GenericRowBean;
import datasets.generic.HandleGenericDataset;
import java.util.Iterator;
import java.util.LinkedList;
import constants.ConstDataset;
import constants.ConstGeneral;
import util.Messages;
import view.viewControler.DesktopView;

/**
 *
 * @author Wesllen Sousa
 */
public class ProcessingFeatures {

    private DesktopView desktopView;

    public ProcessingFeatures(DesktopView graphicView) {
        this.desktopView = graphicView;
    }

    public LinkedList<LinkedList<String>> applyPreprocessing(LinkedList<GenericRowBean> data, LinkedList<String> methods,
            Float windowsSize, int offset, int hertz) {
        System.out.println("Applying preprocessing...");
        Iterator<GenericRowBean> beanIter = data.iterator();
        if (beanIter.hasNext()) {
            GenericRowBean beanFirst = beanIter.next();
            LinkedList<String> columnsNames = new LinkedList<>();
            for (String value : beanFirst.getTupla()) {
                columnsNames.add(value);
            }
            LinkedList<LinkedList<String>> lineColumns = createNewColumns(methods, columnsNames);
            populaLineColumns(data, lineColumns, windowsSize, offset, hertz);
            return lineColumns;
        }
        return null;
    }

    private LinkedList<LinkedList<String>> createNewColumns(LinkedList<String> methods, LinkedList<String> tuplas) {
        LinkedList<LinkedList<String>> lineColumns = new LinkedList<>();
        for (String method : methods) {
            if (method.equals(ConstGeneral.TF_BinnedDistribution)) {
                for (String tupla : tuplas) {
                    for (int i = 0; i < 10; i++) {
                        LinkedList<String> newColumn = new LinkedList<>();
                        newColumn.add(method + i + "_" + tupla);
                        lineColumns.add(newColumn);
                    }
                }
            } else if (method.equals(ConstGeneral.PF_MAGNITUDE) || method.equals(ConstGeneral.PF_VerticalAxis) || method.equals(ConstGeneral.PF_HorizontalAxis)
                    || method.equals(ConstGeneral.TF_AREA) || method.equals(ConstGeneral.TF_AbsoluteArea)
                    || method.equals(ConstGeneral.TF_SignalMagnitudeMean) || method.equals(ConstGeneral.TF_SignalMagnitudeArea)) {
                String eixos = "";
                for (String value : tuplas) {
                    eixos += "_" + value;
                }
                LinkedList<String> newColumn = new LinkedList<>();
                newColumn.add(method + eixos);
                lineColumns.add(newColumn);
            } else if (method.equals(ConstGeneral.TF_Correlation) || method.equals(ConstGeneral.TF_CrossCorrelation)) {
                if (tuplas.size() == 2) {
                    LinkedList<String> newColumn = new LinkedList<>();
                    newColumn.add(method + "_" + tuplas.get(0));
                    lineColumns.add(newColumn);
                } else {
                    for (String tupla : tuplas) {
                        LinkedList<String> newColumn = new LinkedList<>();
                        newColumn.add(method + "_" + tupla);
                        lineColumns.add(newColumn);
                    }
                }
            } else {
                for (String tupla : tuplas) {
                    LinkedList<String> newColumn = new LinkedList<>();
                    newColumn.add(method + "_" + tupla);
                    lineColumns.add(newColumn);
                }
            }
        }
        return lineColumns;
    }

    private void populaLineColumns(LinkedList<GenericRowBean> data, LinkedList<LinkedList<String>> lineColumns,
            Float windowsSize, int overlap, int hertz) {
        LinkedList<LinkedList<Float>> dataWindow = new LinkedList<>();
        LinkedList<String> classes = new LinkedList<>();
        LinkedList<String> classWindow = new LinkedList<>();
        GenericRowBean beanFirst = null;
        int row = 1;

        //verifica o primeiro elemento
        Iterator<GenericRowBean> beanIter = data.iterator();
        if (beanIter.hasNext()) {
            beanFirst = beanIter.next();
            if (beanFirst.getClasse() != null) {
                classes.add(ConstDataset.CLASS);
                desktopView.setClasses(classes);
            }
            for (int j = 0; j < beanFirst.getTupla().size(); j++) {
                dataWindow.add(new LinkedList<>());
            }
        }

        //verifica o resto dos elementos
        while (beanIter.hasNext()) {
            GenericRowBean bean = beanIter.next();

            //Monta janela de tempo
            int column = 0;
            for (String value : bean.getTupla()) {
                try {
                    dataWindow.get(column).add(Float.parseFloat(value));
                } catch (IndexOutOfBoundsException | NumberFormatException ex) {
                    Messages msg = new Messages();
                    msg.bug("Inconsist file! (" + bean.getTupla() + "), line: " + row + "\n Ignoring this line...");
                    dataWindow.get(column).add(dataWindow.get(column).getLast());
                }
                column++;
            }
            if (!classes.isEmpty()) {
                classWindow.add(bean.getClasse());
            }

            //Verifica se chegou no limite da janela para fazer o processamento
            if (dataWindow.get(0).size() >= windowsSize) {
//                System.out.println("Processando janela...");

                //Faz o processamento da janela
                boolean reduceWindow = processDataWindow(lineColumns, dataWindow, beanFirst, hertz);
                //atualiza coluna classe
                if (!classWindow.isEmpty()) {
                    if (reduceWindow) {
                        classes.add(HandleGenericDataset.stringMoreFrequency(classWindow, false));
                    } else {
                        for (String classe : classWindow) {
                            classes.add(classe);
                        }
                    }
                }

                //Limpa o vetor de janela
                for (LinkedList<Float> vector : dataWindow) {
                    int size = vector.size();
                    for (int i = 0; i < size; i++) {
                        if (overlap == 0) {
                            vector.removeFirst();
                        } else if (i < windowsSize - overlap) {
                            vector.removeFirst();
                        }
                    }
                }

                //Limpa o vetor da classe 
                int size = classWindow.size();
                for (int i = 0; i < size; i++) {
                    if (overlap == 0) {
                        classWindow.removeFirst();
                    } else if (i < windowsSize - overlap) {
                        classWindow.removeFirst();
                    }
                }
                //overlap
                row = row - overlap;
            }
            row++;
        }
    }

    private boolean processDataWindow(LinkedList<LinkedList<String>> lineColumns, LinkedList<LinkedList<Float>> dataWindow,
            GenericRowBean nameColumns, int hertz) {
        boolean reduceWindow = false;
        Iterator<LinkedList<String>> lineColumnsIter = lineColumns.iterator();
        while (lineColumnsIter.hasNext()) {
            LinkedList<String> newColumn = lineColumnsIter.next();

            //Separa nome do método com a coluna
            String legend[] = newColumn.get(0).split("_");
            String method = legend[0];
            String nameLineColumn = legend[1];
            for (int l = 2; l < legend.length; l++) {
                nameLineColumn += "_" + legend[l];
            }
            int nameCol = 0;
//            System.out.println("       >> Método: " + method);

            //processamento em uma coluna
            if (method.contains(ConstGeneral.TF_BinnedDistribution)) {
                nameCol = 0;
                for (String nameColumn : nameColumns.getTupla()) {
                    if (nameLineColumn.equals(nameColumn)) {
                        LinkedList<Double> baluesBinned = DiscreteFeatures.BinnedDistribution(dataWindow.get(nameCol));
                        int cont = 0;
                        for (Double value : baluesBinned) {
                            newColumn.add(String.valueOf(value));
                            if (cont != (baluesBinned.size() - 1) && lineColumnsIter.hasNext()) {
                                newColumn = lineColumnsIter.next();
                            }
                            cont++;
                        }
                    }
                    nameCol++;
                }
            } //processamento de fusao de colunas 
            else if (method.equals(ConstGeneral.PF_MAGNITUDE)
                    || method.equals(ConstGeneral.PF_VerticalAxis)
                    || method.equals(ConstGeneral.PF_HorizontalAxis)) {
                addLineColumnsMatrix(method, dataWindow, newColumn);
            } //pricessamento de varias colunas em janelas
            else if (method.equals(ConstGeneral.TF_AREA)
                    || method.equals(ConstGeneral.TF_AbsoluteArea)
                    || method.equals(ConstGeneral.TF_SignalMagnitudeMean)
                    || method.equals(ConstGeneral.TF_SignalMagnitudeArea)) {
                newColumn.add(getValueMatrix(method, dataWindow, -1));
                reduceWindow = true;
            } else { //processamento em varias colunas
                nameCol = 0;
                for (String nameColumn : nameColumns.getTupla()) {
                    if (nameLineColumn.equals(nameColumn)) {
                        if (method.equals(ConstGeneral.SP_SingleLowPass)
                                || method.equals(ConstGeneral.SP_FourStageLowPass)
                                || method.equals(ConstGeneral.SP_bandPass)
                                || method.equals(ConstGeneral.SP_HighPass)
                                || method.equals(ConstGeneral.SP_ButterworthLowpass)
                                || method.equals(ConstGeneral.PF_FastFourierTrasform)
                                || method.equals(ConstGeneral.PF_HaarWavelet)
                                || method.equals(ConstGeneral.SP_MovingAverageFilter)) {
                            addLineColumnsVector(method, dataWindow.get(nameCol), newColumn, hertz);
                        } else if (method.equals(ConstGeneral.TF_Correlation) || method.equals(ConstGeneral.TF_CrossCorrelation)) {
                            newColumn.add(getValueMatrix(method, dataWindow, nameCol));
                            reduceWindow = true;
                        } else {
                            String newValue = getValueVector(method, dataWindow.get(nameCol));
                            if (newValue.equals("-Infinity") || newValue.equals("Infinity") || newValue.equals("NaN")) {
                                newColumn.add("0");
                            } else {
                                newColumn.add(newValue);
                            }
                            reduceWindow = true;
                        }
                    }
                    nameCol++;
                }
            }
        }
        return reduceWindow;
    }

    private void addLineColumnsMatrix(String nameMethod, LinkedList<LinkedList<Float>> windows, LinkedList<String> column) {
        if (nameMethod.equals(ConstGeneral.PF_MAGNITUDE)) {
            PrincipalFeatures.Magnitude(windows).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.PF_VerticalAxis)) {
            LinkedList<Float> gravity = ExtraAlgorithms.gravity(windows);
            PrincipalFeatures.VerticalAxis(windows, gravity).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.PF_HorizontalAxis)) {
            LinkedList<Float> gravity = ExtraAlgorithms.gravity(windows);
            PrincipalFeatures.HorizontalAxis(windows, gravity).stream().forEach((value) -> {
                column.add(value.toString());
            });
        }
    }

    private void addLineColumnsVector(String nameMethod, LinkedList<Float> window, LinkedList<String> column, float frequency) {
        if (nameMethod.equals(ConstGeneral.SP_GausianLowpass)) {
            FiltersSignalProcessing.GausianLowpass(window).stream().forEach((value) -> {
                column.add(value);
            });
        } else if (nameMethod.equals(ConstGeneral.SP_ButterworthLowpass)) {
            FiltersSignalProcessing.ButterworthLowpass2(window).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.SP_SingleLowPass)) {
            FiltersSignalProcessing.SingleLowPass(window, frequency).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.SP_FourStageLowPass)) {
            FiltersSignalProcessing.FourStageLowPass(window, frequency).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.SP_bandPass)) {
            FiltersSignalProcessing.BandPass(window, frequency).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.SP_HighPass)) {
            FiltersSignalProcessing.HighPass(window, frequency).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.SP_KALMAN)) {
            FiltersSignalProcessing.Kalman(window).stream().forEach((value) -> {
                column.add(value);
            });
        } else if (nameMethod.equals(ConstGeneral.PF_ForwardAxis)) {
            PrincipalFeatures.forwardAxis(window).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.PF_SidewardAxis)) {
            PrincipalFeatures.sidewardAxis(window).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.PF_FastFourierTrasform)) {
            PrincipalFeatures.FastFourierTransform(window, true).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.PF_HaarWavelet)) {
            PrincipalFeatures.HaarWaveletTransform(window, true).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else if (nameMethod.equals(ConstGeneral.PF_TiltAngle)) {
            PrincipalFeatures.tiltAngle(window).stream().forEach((value) -> {
                column.add(value);
            });
        } else if (nameMethod.equals(ConstGeneral.SP_MovingAverageFilter)) {
            FiltersSignalProcessing.MovingAverageFilter(window, 3).stream().forEach((value) -> {
                column.add(value.toString());
            });
        } else {
            window.stream().forEach((_item) -> {
                column.add(0 + "");
            });
        }
    }

    private String getValueMatrix(String nameMethod, LinkedList<LinkedList<Float>> windows, int j) {
        int sizeColumn = windows.size();
        if (nameMethod.equals(ConstGeneral.TF_Correlation)) {
            if (j < (sizeColumn - 1)) {
                return TimeFeature.Correlation(windows.get(j), windows.get(j + 1)) + "";
            } else if (j == (sizeColumn - 1) && sizeColumn != 2) {
                return TimeFeature.Correlation(windows.get(0), windows.get(sizeColumn - 1)) + "";
            }
        } else if (nameMethod.equals(ConstGeneral.TF_CrossCorrelation)) {
            if (j < (sizeColumn - 1)) {
                return TimeFeature.CrossCorrelation(windows.get(j), windows.get(j + 1),
                        TimeFeature.Mean(windows.get(j)), TimeFeature.Mean(windows.get(j + 1))) + "";
            } else if (j == (sizeColumn - 1) && sizeColumn != 2) {
                return TimeFeature.CrossCorrelation(windows.get(0), windows.get(sizeColumn - 1),
                        TimeFeature.Mean(windows.get(0)), TimeFeature.Mean(windows.get(sizeColumn - 1))) + "";
            }
        } else if (nameMethod.equals(ConstGeneral.TF_AREA)) {
            return TimeFeature.Area(windows) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_AbsoluteArea)) {
            return TimeFeature.AbsoluteArea(windows) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_SignalMagnitudeMean)) {
            return TimeFeature.SignalMagnitudeMean(windows) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_SignalMagnitudeArea)) {
            return TimeFeature.SignalMagnitudeArea(windows) + "";
        }
        return "0";
    }

    private String getValueVector(String nameMethod, LinkedList<Float> window) {
        if (nameMethod.equals(ConstGeneral.TF_SUM)) {
            return TimeFeature.Sum(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_Energy)) {
            return TimeFeature.Energy(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_EnergyLog)) {
            return TimeFeature.EnergyLog(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_Power)) {
            return TimeFeature.Power(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_AbsoluteSum)) {
            return TimeFeature.AbsoluteSum(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_MEAN)) {
            return TimeFeature.Mean(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_AbsoluteMean)) {
            return TimeFeature.AbsoluteMean(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_AbsoluteMeanDeviation)) {
            return TimeFeature.AbsoluteMeanDeviation(window, TimeFeature.Mean(window)) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_SumSquareError)) {
            return TimeFeature.SumSquareError(window, TimeFeature.Mean(window)) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_Variance)) {
            return TimeFeature.Variance(window, TimeFeature.Mean(window)) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_StandardDeviation)) {
            return TimeFeature.StandardDeviation(window, TimeFeature.Mean(window)) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_PearsonCoefficientVariation)) {
            return TimeFeature.PearsonCoefficientVariation(window, TimeFeature.Mean(window)) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_MIN)) {
            return TimeFeature.Min(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_MAX)) {
            return TimeFeature.Max(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_AMPLITUDE)) {
            return TimeFeature.Amplitude(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_PeakAmplitude)) {
            return TimeFeature.PeakAmplitude(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_EuclideanNorm)) {
            return TimeFeature.EuclideanNorm(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_RootMeanSquare)) {
            return TimeFeature.RootMeanSquare(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_AutoCorrelation)) {
            return TimeFeature.AutoCorrelation(window, TimeFeature.Mean(window)) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_ZeroCrossingRatio)) {
            return TimeFeature.ZeroCrossingRation(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_Quartile25)) {
            return TimeFeature.Quartile25(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_Quartile50)) {
            return TimeFeature.Quartile50(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_Quartile75)) {
            return TimeFeature.Quartile75(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_InterQuartileRange)) {
            return TimeFeature.InterQuartileRange(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_AverateMagnitudeDifferenceFuction)) {
            return TimeFeature.AverageMagnitudeDifferenceFunction(window) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_Skewness)) {
            return TimeFeature.Skewness(window, TimeFeature.Mean(window)) + "";
        } else if (nameMethod.equals(ConstGeneral.TF_Kurtosis)) {
            return TimeFeature.Kurtosis(window, TimeFeature.Mean(window)) + "";
        } else if (nameMethod.equals(ConstGeneral.FF_SpectralCentroid)) {
            return FrequencyFeatures.SpectralCentroid(window) + "";
        } else if (nameMethod.equals(ConstGeneral.FF_SpectralEnergy)) {
            return FrequencyFeatures.SpectralEnergy(window) + "";
        } else if (nameMethod.equals(ConstGeneral.FF_SpectralEnergyNormalized)) {
            return FrequencyFeatures.SpectralEnergyNormalized(window) + "";
        } else if (nameMethod.equals(ConstGeneral.FF_SpectralPower)) {
            return FrequencyFeatures.SpectralPower(window) + "";
        } else if (nameMethod.equals(ConstGeneral.FF_SpectralEntropy)) {
            return FrequencyFeatures.SpectralEntropy(window) + "";
        } else if (nameMethod.equals(ConstGeneral.FF_CoefficientsSum)) {
            return FrequencyFeatures.CoefficientsSum(window) + "";
        } else if (nameMethod.equals(ConstGeneral.FF_Peaks)) {
            return FrequencyFeatures.PeakTime(window) + "";
        } else if (nameMethod.equals(ConstGeneral.FF_DComponents)) {
            return FrequencyFeatures.DComponents(window) + "";
        } else if (nameMethod.equals(ConstGeneral.FF_DominantFrequency)) {
            return FrequencyFeatures.DominantFrequency(window) + "";
        }
        return "0";
    }

    public void ajustLineColumns(LinkedList<LinkedList<String>> lineColumns) {
        int menorSize = Integer.MAX_VALUE;
        for (LinkedList<String> column : lineColumns) {
            if (column.size() < menorSize) {
                menorSize = column.size();
            }
        }
        for (LinkedList<String> column : lineColumns) {
            if (column.size() > menorSize) {
                for (int i = column.size() - 1; i >= menorSize; i--) {
                    column.remove(i);
                }
            }
        }
    }

    /*
    * Data Fusion 
     */
    public LinkedList<LinkedList<String>> applyDataFusionProcessing(LinkedList<String> methodsFusion,
            LinkedList<GenericRowBean> data, String nameDataset) {
        LinkedList<LinkedList<String>> lineColumns = new LinkedList<>();

        for (String method : methodsFusion) {
            if (method.equals(ConstGeneral.DF_MAGNITUDE)) {
                LinkedList<String> magnitude = new LinkedList<>();
                DataFusion.Magnitude(data).stream().forEach((value) -> {
                    magnitude.add(value.toString());
                });
                lineColumns.add(magnitude);
            } else if (method.equals(ConstGeneral.DF_PCA)) {
                LinkedList<String> pca = new LinkedList<>();
                DataFusion.PCA(ConstDataset.DS_RAW, nameDataset).stream().forEach((value) -> {
                    pca.add(value.toString());
                });
                lineColumns.add(pca);
            }
        }

        return lineColumns;
    }

    public LinkedList<String> getClassesFromData(LinkedList<GenericRowBean> data) {
        LinkedList<String> classes = new LinkedList<>();
        for (GenericRowBean bean : data) {
            if (bean.getClasse() != null) {
                classes.add(bean.getClasse());
            }
        }
        return classes;
    }
}
