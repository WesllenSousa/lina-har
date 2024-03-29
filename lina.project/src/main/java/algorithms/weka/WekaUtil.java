package algorithms.weka;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import util.FileUtil;
import util.Messages;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author Wesllen Sousa
 */
public class WekaUtil {

    private Instances trainData = null;
    private Instances testData = null;

    public WekaUtil() {
    }

    public WekaUtil(String trainFile, Integer numberColumnClass) {
        this.trainData = readData(trainFile, numberColumnClass);
    }

    public WekaUtil(String trainFile, String testFile, Integer numberColumnClass) {
        this.trainData = readData(trainFile, numberColumnClass);
        this.testData = readData(testFile, numberColumnClass);
    }

    private Instances readData(String dir, Integer numberColumnClass) {
        try {
            String extension = FileUtil.getFileExtension(dir);
            if (extension.equals("arff")) {
                DataSource fonte = new DataSource(dir);
                Instances data = fonte.getDataSet();
                data.setClassIndex(numberColumnClass - 1);
                return data;
            } else {
                Messages messages = new Messages();
                messages.bug("File format should be arff!");
            }
        } catch (Exception ex) {
            System.out.println("readData: " + ex);
            Messages messages = new Messages();
            messages.bug(ex.toString());
        }
        return null;
    }

    public Classifier buildClassify(Classifier classifier) {
        if (trainData == null) {
            return null;
        }
        try {
            classifier.buildClassifier(trainData);
            return classifier;
        } catch (Exception ex) {
            System.out.println("buildClassfy: " + ex);
            Messages messages = new Messages();
            messages.bug(ex.toString());
        }
        return null;
    }

    public void saveModel(Classifier classifier, String outputDir) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(
                    new FileOutputStream(outputDir));
            output.writeObject(classifier);
            output.flush();
            output.close();
            Messages messages = new Messages();
            messages.sucesso("Classifier Saved!");
        } catch (IOException ex) {
            System.out.println("saveModel: " + ex);
            Messages messages = new Messages();
            messages.bug(ex.toString());
        }
    }

    public Classifier readModel(String inputDir) {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(inputDir));
            return (Classifier) input.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("readModel: " + ex);
            Messages messages = new Messages();
            messages.bug(ex.toString());
        }
        return null;
    }

    public String classify(Classifier classifier, Instance newInstance) {
        try {
            double predict = classifier.classifyInstance(newInstance);
            return trainData.attribute(trainData.classIndex()).value((int) predict);
        } catch (Exception ex) {
            System.out.println("classify: " + ex);
            Messages messages = new Messages();
            messages.bug(ex.toString());
        }
        return null;
    }

    public String evaluation(Classifier classifier) {
        String result = classifier.toString() + "\n";
        Evaluation eval = null;
        try {
            if (testData != null) {
                eval = new Evaluation(trainData);
                eval.evaluateModel(classifier, testData);
            } else {
                eval = new Evaluation(trainData);
                eval.crossValidateModel(classifier, trainData, 10, new Random(1));
            }
            result += "Total Cost: " + eval.totalCost() + "\n";
            result += eval.toSummaryString("Summary", true);
        } catch (Exception ex) {
            Messages messages = new Messages();
            messages.bug(ex.toString());
        }
//        try {
//            result += eval.toCumulativeMarginDistributionString();
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
        try {
            result += eval.toClassDetailsString("Class Detail");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            result += eval.toMatrixString("Matrix");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return result;
    }

    /*
        Getters and Setters
     */
    public Instances getData() {
        return trainData;
    }

}
