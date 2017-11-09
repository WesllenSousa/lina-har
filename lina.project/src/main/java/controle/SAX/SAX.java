/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.SAX;

import controle.SAX.saxvsm.text.TextProcessor;
import controle.SAX.saxvsm.text.WordBag;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.seninp.jmotif.sax.SAXException;
import net.seninp.jmotif.sax.SAXProcessor;
import net.seninp.jmotif.sax.TSProcessor;
import net.seninp.jmotif.sax.alphabet.Alphabet;
import net.seninp.jmotif.sax.alphabet.NormalAlphabet;
import net.seninp.jmotif.sax.datastructure.SAXRecords;
import net.seninp.jmotif.sax.parallel.ParallelSAXImplementation;
import net.seninp.util.UCRUtils;
import util.Messages;

/**
 *
 * @author Wesllen Sousa
 */
public class SAX {

    public static String serieToWord(double[] ts, Params params) {
        try {
            TSProcessor tp = new TSProcessor();
            // Z normalize it
            double[] tsZNorm = tp.znorm(ts, params.nThreshold);
            // perform PAA conversion if needed
            double[] paa = tp.paa(tsZNorm, params.paaSize);
            // Convert the PAA to a string.
            Alphabet na = new NormalAlphabet();
            char[] word = tp.ts2String(paa, na.getCuts(params.alphabetSize));
            return String.valueOf(word);
        } catch (SAXException ex) {
            Messages msg = new Messages();
            msg.bug("SAX - serieToWord: " + ex.toString());
        }
        return null;
    }

    public static SAXRecords slideWindow(double[] ts, Params params) {
        SAXProcessor sp = new SAXProcessor();
        SAXRecords sAXRecords = new SAXRecords();
        try {
            NormalAlphabet na = new NormalAlphabet();
            sAXRecords = sp.ts2saxViaWindow(ts, params.windowSize,
                    params.paaSize, na.getCuts(params.alphabetSize),
                    params.nrStartegy, params.nThreshold);
            return sAXRecords;
        } catch (SAXException ex) {
            Messages msg = new Messages();
            msg.bug("SAX - slideWindow: " + ex.toString());
        }
        return new SAXRecords();
    }

    public static SAXRecords slideWindowParallel(double[] ts, Params params) {
        try {
            ParallelSAXImplementation ps = new ParallelSAXImplementation();
            SAXRecords parallelRes = ps.process(ts, 2, params.windowSize, params.paaSize,
                    params.alphabetSize, params.nrStartegy, params.nThreshold);
            return parallelRes;
        } catch (SAXException ex) {
            Messages msg = new Messages();
            msg.bug("ParallelSAX - slideWindowParallel: " + ex.toString());
        }
        return new SAXRecords();
    }

    public static String SAX_VSM(String train_file, String test_file, Params params) {

        try {
            Map<String, List<double[]>> trainData = UCRUtils.readUCRData(train_file);
            Map<String, List<double[]>> testData = UCRUtils.readUCRData(test_file);

            TextProcessor tp = new TextProcessor();
            // making training bags collection
            List<WordBag> bags = tp.labeledSeries2WordBags(trainData, params);
            // getting TFIDF done
            HashMap<String, HashMap<String, Double>> tfidf = tp.computeTFIDF(bags);
            // classifying
            int testSampleSize = 0;
            int positiveTestCounter = 0;
            for (String label : tfidf.keySet()) {
                List<double[]> testD = testData.get(label);
                for (double[] series : testD) {
                    positiveTestCounter = positiveTestCounter
                            + tp.classify(label, series, tfidf, params);
                    testSampleSize++;
                }
            }

            // accuracy and error
            double accuracy = (double) positiveTestCounter / (double) testSampleSize;
            double error = 1.0d - accuracy;

            // report results
            String results = "classification results: \n" + toLogStr(params, accuracy, error);
            System.out.println(results);
            
            return results;

        } catch (IOException | NumberFormatException | SAXException ex) {
            Messages msg = new Messages();
            msg.bug("SAX - SAX_VSM: " + ex.toString());
        }
        return null;
    }

    private static String toLogStr(Params params, double accuracy, double error) {
        DecimalFormat fmt = new DecimalFormat("0.00###");
        StringBuffer sb = new StringBuffer();
        sb.append(" strategy ").append(params.getNrStartegy().toString()).append(",");
        sb.append(" window ").append(params.getWindowSize()).append(",");
        sb.append(" PAA ").append(params.getPaaSize()).append(",");
        sb.append("alphabet ").append(params.getAlphabetSize()).append(",");
        sb.append(" accuracy ").append(fmt.format(accuracy)).append(",");
        sb.append(" error ").append(fmt.format(error));
        return sb.toString();
    }

}
