// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package datasets.timeseries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.carrotsearch.hppc.DoubleArrayList;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import net.seninp.util.StackTrace;

public class TimeSeriesLoader {

    final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static TimeSeries[] loadVerticalData(String limitStr, String dataFileName, String separator) {
        return loadVerticalData(limitStr, dataFileName, false, separator);
    }

    public static TimeSeries[] loadVerticalData(String limitStr, String dataFileName, Boolean normalize, String separator) {

        // check if everything is ready
        if ((null == dataFileName) || dataFileName.isEmpty()) {
            System.out.println("unable to load data - no data source selected yet");
            return null;
        }

        // make sure the path exists
        Path path = Paths.get(dataFileName);
        if (!(Files.exists(path))) {
            System.out.println("file " + dataFileName + " doesn't exist.");
            return null;
        }

        // read the input
        ArrayList<LinkedList<Double>> dataset = new ArrayList<>();

        try {
            // set the lines limit
            long loadLimit = 0l;
            if (!(null == limitStr) && !(limitStr.isEmpty())) {
                loadLimit = Long.parseLong(limitStr);
            }

            // open the reader
            BufferedReader reader = Files.newBufferedReader(path, DEFAULT_CHARSET);

            // read by the line in the loop from reader
            String line = null;
            long lineCounter = 0;
            int colCount = 1;
            while ((line = reader.readLine()) != null) {
                //ingnore line
                if (line.startsWith("@") || !isNonEmptyColumn(line)) {
                    continue;
                }

                String[] lineSplit = line.trim().split(separator);

                if (lineCounter == 0) {
                    lineCounter++;
                    if (isNonEmptyColumn(lineSplit[0])) {
                        //Cria a quantidade de objetos TS para o numero igual de colunas
                        colCount = lineSplit.length;
                        for (int i = 0; i < colCount; i++) {
                            LinkedList<Double> newTs = new LinkedList<>();
                            dataset.add(i, newTs);
                        }
                        continue;
                    }
                }

                //check if data os consistent
                if (lineSplit.length != colCount) {
                    lineCounter++;
                    System.out.println("Data inconsistent, line: " + lineCounter);
                    continue;
                }

                // we read only first column
                for (int i = 0; i < colCount; i++) {
                    try {
                        double value = new BigDecimal(lineSplit[i]).doubleValue();
                        dataset.get(i).add(value);
                    } catch (NumberFormatException ex) {
                        System.out.println("loadVerticalData " + ex + ", line file: " + lineCounter);
                    }
                }

                lineCounter++;

                // break the load if needed
                if ((loadLimit > 0) && (lineCounter > loadLimit)) {
                    break;
                }
            }
            reader.close();
        } catch (NumberFormatException | IOException e) {
            String stackTrace = StackTrace.toString(e);
            System.err.println(StackTrace.toString(e));
            System.out.println("error while trying to read data from " + dataFileName + ":\n" + stackTrace);
        } finally {
            assert true;
        }

        // convert to simple doubles array and clean the variable
        ArrayList<TimeSeries> samples = new ArrayList<>();
        double[] ts = new double[]{};
        if (!dataset.isEmpty()) {
            for (LinkedList<Double> coluna : dataset) {
                ts = new double[coluna.size()];
                int i = 0;
                for (Double value : coluna) {
                    ts[i] = value;
                    i++;
                }
                TimeSeries timeSeries = new TimeSeries(ts, null);
                if (normalize) {
                    timeSeries.norm();
                }
                samples.add(timeSeries);
            }
        }
        System.out.println("loaded " + ts.length + " points from " + dataFileName);

        return samples.toArray(new TimeSeries[]{});
    }

    /**
     * Loads the time series from a csv-file of the UCR time series archive.
     *
     * @param dataset
     * @param splitColumn
     * @param normalize
     * @return
     */
    public static TimeSeries[] loadHorizontalData(String dataset, String splitColumn, Boolean normalize) {
        LinkedList<TimeSeries> samples = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dataset))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("@")) {
                    continue;
                }
                String[] column = line.split(splitColumn);
                TimeSeries ts = getTimeSeriesObject(column, normalize);
                if (ts != null) {
                    ts.setLabel(Double.parseDouble(column[0]));
                    samples.add(ts);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        if (samples.size() > 0) {
            System.out.println("Done reading from " + dataset + " samples " + samples.size() + " length " + samples.get(0).getLength());
        } else {
            System.out.println("Data format incorrect!");
        }

        return samples.toArray(new TimeSeries[]{});
    }

    /**
     * Loads the time series from a csv-file of the UCR time series archive.
     *
     * @param dataset
     * @param splitColumn
     * @param normalize
     * @param qtdeDimension
     * @param derivatives
     * @return
     */
    public static MultiVariateTimeSeries[] loadHorizontalDataMultiDimensional(String dataset, String splitColumn, Boolean normalize,
            int qtdeDimension, boolean derivatives) {
        LinkedList<MultiVariateTimeSeries> samplesMD = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dataset))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("@")) {
                    continue;
                }
                String[] columns = line.split(splitColumn);

                int tsSize = (columns.length - 1) / qtdeDimension;
                LinkedList<TimeSeries> samples = new LinkedList<>();
                for (int i = 0; i < qtdeDimension; i++) {

                    String[] column = new String[tsSize];
                    for (int w = 0; w < tsSize; w++) {
                        int index = (i * tsSize) + w;
                        column[w] = columns[index];
                    }
                    TimeSeries ts = getTimeSeriesObject(column, normalize);
                    if (ts != null) {
                        ts.setLabel(Double.parseDouble(columns[0]));
                        samples.add(ts);
                    }
                }

                MultiVariateTimeSeries tsMD = new MultiVariateTimeSeries(samples.toArray(new TimeSeries[]{}));
                tsMD.setLabel(Double.parseDouble(columns[0]));
                samplesMD.add(tsMD);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        if (samplesMD.size() > 0) {
            System.out.println("Done reading from " + dataset + " samples " + samplesMD.size() + " length " + samplesMD.get(0).getLength());
        } else {
            System.out.println("Data format incorrect!");
        }

        MultiVariateTimeSeries[] m = samplesMD.toArray(new MultiVariateTimeSeries[]{});
        return (derivatives) ? getDerivatives(m) : m;
    }

    public static TimeSeries getTimeSeriesObject(String[] columns, Boolean normalize) {

        double[] data = new double[columns.length];
        int j = 0;

        // next the data
        for (String column1 : columns) {
            String column = column1.trim();
            try {
                if (isNonEmptyColumn(column)) {
                    data[j++] = Double.parseDouble(column);
                }
            } catch (NumberFormatException nfe) {
                System.out.println(nfe);
            }
        }

        if (j > 0) {
            TimeSeries ts = new TimeSeries(Arrays.copyOfRange(data, 0, j));
            if (normalize) {
                ts.norm();
            }
            return ts;
        }
        return null;
    }

    private static MultiVariateTimeSeries[] getDerivatives(MultiVariateTimeSeries[] mtsSamples) {
        for (MultiVariateTimeSeries mts : mtsSamples) {
            TimeSeries[] deltas = new TimeSeries[2 * mts.timeSeries.length];
            TimeSeries[] samples = mts.timeSeries;
            for (int a = 0; a < samples.length; a++) {
                TimeSeries s = samples[a];
                double[] d = new double[s.getLength() - 1];
                for (int i = 1; i < s.getLength(); i++) {
                    d[i - 1] = s.getData()[i] - s.getData()[i - 1];
                }
                deltas[2 * a] = samples[a];
                deltas[2 * a + 1] = new TimeSeries(d, mts.getLabel());
            }
            mts.timeSeries = deltas;
        }
        return mtsSamples;
    }

    public static TimeSeries readSampleSubsequence(File dataset) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(dataset))) {
            DoubleArrayList data = new DoubleArrayList();
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("[ \\t]");
                if (values.length > 0) {
                    for (String value : values) {
                        try {
                            if (isNonEmptyColumn(value.trim())) {
                                data.add(Double.parseDouble(value));
                            }
                        } catch (NumberFormatException nfe) {
                            // Parse-Exception ignorieren
                        }
                    }
                }
            }
            return new TimeSeries(data.toArray());
        }
    }

    public static TimeSeries[] readSamplesQuerySeries(String dataset) throws IOException {
        return readSamplesQuerySeries(new File(dataset));
    }

    public static TimeSeries[] readSamplesQuerySeries(File dataset) throws IOException {
        List<TimeSeries> samples = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataset))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                DoubleArrayList data = new DoubleArrayList();
                String[] values = line.trim().split("[ \\t]");
                if (values.length > 0) {
                    for (String value : values) {
                        try {
                            if (isNonEmptyColumn(value.trim())) {
                                data.add(Double.parseDouble(value));
                            }
                        } catch (NumberFormatException nfe) {
                            // Parse-Exception ignorieren
                        }
                    }
                    samples.add(new TimeSeries(data.toArray()));
                }
            }
        }
        return samples.toArray(new TimeSeries[]{});
    }

    public static boolean isNonEmptyColumn(String column) {
        return column != null && !"".equals(column) && !"NaN".equals(column) && !"\t".equals(column);
    }

    public static TimeSeries generateRandomWalkData(int maxDimension, Random generator) {
        double[] data = new double[maxDimension];

        // Gaussian Distribution 
        data[0] = generator.nextGaussian();

        for (int d = 1; d < maxDimension; d++) {
            data[d] = data[d - 1] + generator.nextGaussian();
        }

        return new TimeSeries(data);
    }

    public static Boolean saveVectorToCSV(String diretorio, String nameFile, double[] data) {
        try (OutputStream output = new FileOutputStream(new File(diretorio + File.separator + nameFile + ".csv"));
                OutputStreamWriter osw = new OutputStreamWriter(output);
                BufferedWriter write = new BufferedWriter(osw)) {
            for (double c : data) {
                write.write(c + "\n");
            }
            write.close();
            osw.close();
            output.close();
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public static Boolean saveVectorToCSV(String diretorio, String nameFile, int[] data) {
        try (OutputStream output = new FileOutputStream(new File(diretorio + File.separator + nameFile + ".csv"));
                OutputStreamWriter osw = new OutputStreamWriter(output);
                BufferedWriter write = new BufferedWriter(osw)) {
            for (int c : data) {
                write.write(c + "\n");
            }
            write.close();
            osw.close();
            output.close();
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return false;
    }
}
