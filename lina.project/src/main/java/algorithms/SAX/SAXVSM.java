/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.SAX;

import algorithms.SAX.saxvsm.text.TextProcessor;
import algorithms.SAX.saxvsm.text.WordBag;
import datasets.memory.WordRecord;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.seninp.jmotif.sax.SAXException;
import util.FileUtil;
import util.Messages;

/**
 *
 * @author Wesllen Sousa
 */
public class SAXVSM {

    public String eval(Map<String, List<double[]>> trainData, Map<String, List<double[]>> testData, Params params) {
        try {
            long init = System.currentTimeMillis();

            TextProcessor tp = new TextProcessor();
            // making training bags collection
            List<WordBag> bags = tp.labeledSeriesToWordBags(trainData, params);

            long end = System.currentTimeMillis();
            long time = end - init;
            System.out.println("Create words time: " + time);

            //printWordsBytes(bags);
            init = System.currentTimeMillis();
            // getting TFIDF done
            HashMap<String, HashMap<String, Double>> tfidf = tp.computeTFIDF(bags);

            end = System.currentTimeMillis();
            time = end - init;
            System.out.println("Train time: " + time);

            init = System.currentTimeMillis();

            // classifying train
            int testSampleSize = 0;
            int positiveTestCounter = 0;
            for (String label : tfidf.keySet()) {
                List<double[]> testD = trainData.get(label);
                for (double[] series : testD) {
                    positiveTestCounter = positiveTestCounter
                            + tp.classify(label, series, tfidf, params);
                    testSampleSize++;
                }
            }

            end = System.currentTimeMillis();
            time = end - init;
            System.out.println("Test time: " + time);

            // accuracy and error
            double accuracyTrain = (double) positiveTestCounter / (double) testSampleSize;
            double errorTrain = 1.0d - accuracyTrain;

            // report results
            String results = "SAX-VSM: \n " + toLogStr(params, accuracyTrain, errorTrain) + "\n";

            // classifying test
            testSampleSize = 0;
            positiveTestCounter = 0;
            for (String label : tfidf.keySet()) {
                List<double[]> testD = testData.get(label);
                for (double[] series : testD) {
                    positiveTestCounter = positiveTestCounter
                            + tp.classify(label, series, tfidf, params);
                    testSampleSize++;
                }
            }
            // accuracy and error
            double accuracyTest = (double) positiveTestCounter / (double) testSampleSize;
            double errorTest = 1.0d - accuracyTest;

            // report results
            results += toLogStr(params, accuracyTest, errorTest);
            System.out.println(results);

            return results;

        } catch (NumberFormatException | SAXException ex) {
            Messages msg = new Messages();
            msg.bug("SAX - SAX_VSM: " + ex.toString());
        }
        return null;
    }

    public WordBag wordsToWordBag(List<WordRecord> words, Double label) {
        WordBag bag = new WordBag(label.toString());
        for (WordRecord word : words) {
            bag.addWord(word.getWord(), word.getFrequency());
        }
        return bag;
    }

    public HashMap<String, HashMap<String, Double>> getTfIdfFromWordBags(List<WordBag> listWords) {
        TextProcessor tp = new TextProcessor();
        HashMap<String, HashMap<String, Double>> tfidf = tp.computeTFIDF(listWords);
        return tfidf;
    }

    public String predict(WordBag bag, HashMap<String, HashMap<String, Double>> matrixSaxVsm) {
        TextProcessor tp = new TextProcessor();
        String result = tp.classify(bag, matrixSaxVsm);
        return result;
    }

    public void saveWordBags(Map<String, List<double[]>> data, Params params, String dir) {
        try {
            TextProcessor tp = new TextProcessor();
            // making training bags collection
            List<WordBag> bags = tp.labeledSeriesToWordBags(data, params);

            String dataset = "";
            for (WordBag word : bags) {
                //System.out.println("Label: " + word.getLabel());
                dataset += word.getLabel() + "\n";
                for (String w : word.getWordSet()) {
                    //System.out.println(">> " + w + ": " + word.getWordFrequency(w));
                    for (int i = 0; i < word.getWordFrequency(w); i++) {
                        dataset += w + " ";
                    }
                }
                dataset += "\n";
            }

            FileUtil.saveFile(dir, dataset);

        } catch (NumberFormatException | SAXException ex) {
            Messages msg = new Messages();
            msg.bug("SAX - SAX_VSM: " + ex.toString());
        }
    }

    public void printWordsBytes(List<WordBag> bags) {
        String dataset = "";
        for (WordBag word : bags) {
            for (String w : word.getWordSet()) {
                dataset += w + "," + word.getWordFrequency(w) + ",";
            }
        }
        System.out.println(dataset);
        System.out.println(dataset.getBytes().length);
        FileUtil.saveFile("E:\\dataset.txt", dataset);
    }

    private String toLogStr(Params params, double accuracy, double error) {
        DecimalFormat fmt = new DecimalFormat("0.00###");
        StringBuilder sb = new StringBuilder();
        sb.append(" Strategy: ").append(params.getNrStartegy().toString()).append(",");
        sb.append("\n Window ").append(params.getWindowSize()).append(",");
        sb.append("\n PAA ").append(params.getPaaSize()).append(",");
        sb.append("\n Alphabet ").append(params.getAlphabetSize()).append(",");
        sb.append("\n>> Accuracy ").append(fmt.format(accuracy)).append(",");
        sb.append("\n>> Error ").append(fmt.format(error));
        return sb.toString();
    }
}
