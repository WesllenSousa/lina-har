package datasets.generic;

import controle.arff.DataToARFF;
import controle.arff.FilePatternARFF;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import constants.ConstDataset;
import constants.ConstGeneral;
import constants.Parameters;
import java.util.HashMap;
import util.FileUtil;
import util.Messages;
import util.Validation;

/**
 *
 * @author Wesllen Sousa
 */
public class HandleGenericDataset {

    public static LinkedList<GenericRowBean> convertToHorizontalFormat(LinkedList<GenericRowBean> data) {
        LinkedList<GenericRowBean> horizList = new LinkedList<>();
        LinkedList<String> tuplaHoriz = new LinkedList<>();
        LinkedList<String> classes = new LinkedList<>();

        int tsSize = (int) (Math.abs(Parameters.WINDOW_SEC) * Parameters.FREQUENCY);
        int row = 0;
        for (GenericRowBean bean : data) {
            if (row == 0) {
                row++;
                continue;
            }
            classes.add(bean.getClasse());
            for (String value : bean.getTupla()) {
                tuplaHoriz.addLast(value);
            }
            if (row % tsSize == 0) {
                String classe = stringMoreFrequency(classes, true);
                //Ingnoring noise columns
                if (!classe.equals(ConstGeneral.NOISE)) {
                    tuplaHoriz.addFirst(classe);
                    GenericRowBean newBean = new GenericRowBean();
                    newBean.setTupla((LinkedList<String>) tuplaHoriz.clone());
                    horizList.add(newBean);
                }

                tuplaHoriz.clear();
                classes.clear();
            }
            row++;
        }
        return horizList;
    }

    public static LinkedList<GenericRowBean> convertToVerticalFormat(LinkedList<GenericRowBean> data) {
        LinkedList<GenericRowBean> vertList = new LinkedList<>();

        for (GenericRowBean bean : data) {
            String classe = "";
            int col = 0;
            for (String value : bean.getTupla()) {
                if (col == 0) {
                    classe = value;
                } else {
                    LinkedList<String> tupla = new LinkedList<>();
                    tupla.add(value);

                    GenericRowBean newBean = new GenericRowBean();
                    newBean.setClasse(classe);
                    newBean.setTupla(tupla);
                    vertList.add(newBean);
                }
                col++;
            }
        }

        return vertList;
    }

    public static LinkedList<GenericRowBean> bufferFileInMemory(String separador, String inputSource) {
        LinkedList<GenericRowBean> list = new LinkedList<>();
        GenericRowBean nameColumns = new GenericRowBean();
        nameColumns.setTupla(FileUtil.extractNamesColumnFromFile(separador, inputSource));
        list.add(nameColumns);

        try (BufferedReader buffer = FileUtil.readFile(inputSource)) {
            String linha = buffer.readLine();

            if (FileUtil.getFileExtension(inputSource).equals("arff")) {
                while (linha != null) {
                    if (linha.contains("@") || linha.isEmpty()) {
                        linha = buffer.readLine();
                    } else {
                        break;
                    }
                }
            }
            if (Validation.hasLetraAlfabeto(linha)) {
                linha = buffer.readLine();
            }

            int cont = 0; //current timestamp 1477325697
            while (linha != null) {
                String[] colunas = linha.split(separador);

                LinkedList<String> columns = new LinkedList<>();
                for (int i = 0; i < colunas.length; i++) {
                    columns.add(colunas[i].trim());
                }

                GenericRowBean bean = new GenericRowBean();
                bean.setTupla(columns);
                bean.setId((long) cont);
                list.add(bean);

                linha = buffer.readLine();
                cont++;
            }

            buffer.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return list;
    }

    public static Boolean saveBufferToCSV(String diretorio, String nameFile, LinkedList<GenericRowBean> data, String separador) {
        try (OutputStream output = new FileOutputStream(new File(diretorio + File.separator + nameFile + ".csv"));
                OutputStreamWriter osw = new OutputStreamWriter(output);
                BufferedWriter write = new BufferedWriter(osw)) {

            for (GenericRowBean bean : data) {
                String linha = "";
                Iterator<String> tuplas = bean.getTupla().iterator();
                while (tuplas.hasNext()) {
                    String value = tuplas.next();
                    if (tuplas.hasNext()) {
                        linha += value + separador;
                    } else if (bean.getClasse() != null || bean.getTimestamp() != null) {
                        linha += value + separador;
                    } else {
                        linha += value;
                    }
                }
                if (bean.getTimestamp() != null && bean.getClasse() != null) {
                    linha += bean.getTimestamp() + separador + bean.getClasse();
                } else if (bean.getClasse() != null) {
                    linha += bean.getClasse();
                } else if (bean.getTimestamp() != null) {
                    linha += bean.getTimestamp();
                }
                write.write(linha + "\n");
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

    public static Boolean saveBufferToARFF(String diretorio, String nameFile, LinkedList<GenericRowBean> data) {
        FilePatternARFF arffFile = new FilePatternARFF();

        //Prepare HEAD
        LinkedHashMap<String, String> columnTitleType = new LinkedHashMap<>();
        LinkedHashMap<String, LinkedList<String>> nominalValues = new LinkedHashMap<>();

        Iterator<GenericRowBean> beanIterHEAD = data.iterator();
        if (beanIterHEAD.hasNext()) {
            GenericRowBean beanFirst = beanIterHEAD.next();
            for (String value : beanFirst.getTupla()) {
                columnTitleType.put(value, DataToARFF.NUMERIC);
            }
            if (beanFirst.getClasse() != null) {
                columnTitleType.put(ConstDataset.CLASS, DataToARFF.NOMINAL);
                nominalValues.put(ConstDataset.CLASS, new LinkedList<>());
            }
        }

        //Prepare DATA
        LinkedHashMap<Integer, LinkedList<String>> row = new LinkedHashMap<>();

        Iterator<GenericRowBean> beanIterDATA = data.iterator();
        beanIterDATA.next(); //Pula o nome das colunas

        String lastClass = null;
        int contRow = 0;
        while (beanIterDATA.hasNext()) {
            GenericRowBean bean = beanIterDATA.next();

            LinkedList<String> novaTupla = new LinkedList<>();
            for (String value : bean.getTupla()) {
                novaTupla.add(value);
            }

            if (bean.getClasse() != null) {
                novaTupla.add(bean.getClasse());
                //Captura os diferentes valores para o tipo de dados nominal
                LinkedList<String> list = nominalValues.get(ConstDataset.CLASS);
                if (!list.contains(bean.getClasse())) {
                    list.add(bean.getClasse());
                }
                lastClass = bean.getClasse();
            } else if (lastClass != null) {
                novaTupla.add(lastClass);
            }

            row.put(++contRow, novaTupla);
        }

        //Record file
        arffFile.setColumnTitleType(columnTitleType);
        arffFile.setNominalValues(nominalValues);
        arffFile.setRow(row);
        DataToARFF arff = new DataToARFF();
        arff.createARFFile(diretorio, nameFile, arffFile);
        return true;
    }

    public static void replaceInBuffer(LinkedList<GenericRowBean> data, String word, String newWord, int column) {
        for (GenericRowBean bean : data) {
            int col = 0;
            for (String value : bean.getTupla()) {
                if (col == column && value.equals(word)) {
                    bean.getTupla().set(col, newWord);
                }
                col++;
            }
            int columnClass = data.getFirst().getTupla().size();
            if (columnClass == column && bean.getClasse() != null) {
                if (bean.getClasse().equals(word)) {
                    bean.setClasse(newWord);
                }
            }
        }
    }

    public static LinkedList<GenericRowBean> merge(LinkedList<GenericRowBean> data1, LinkedList<GenericRowBean> data2) {
        int numberRows = 0;
        if (data1.size() < data2.size()) {
            numberRows = data1.size();
        } else {
            numberRows = data2.size();
        }
        LinkedList<GenericRowBean> newBuffer = new LinkedList<>();

        Iterator<GenericRowBean> beanIter1 = data1.iterator();
        Iterator<GenericRowBean> beanIter2 = data2.iterator();

        for (int i = 0; i < numberRows; i++) {
            GenericRowBean bean1 = beanIter1.next();
            GenericRowBean bean2 = beanIter2.next();

            LinkedList<String> newColumns = new LinkedList<>();
            for (String column : bean1.getTupla()) {
                newColumns.add(column);
            }
            if (bean1.getClasse() != null) {
                newColumns.add(bean1.getClasse());
            }
            for (String column : bean2.getTupla()) {
                newColumns.add(column);
            }
            if (bean2.getClasse() != null) {
                newColumns.add(bean2.getClasse());
            }

            GenericRowBean newSample = new GenericRowBean();
            newSample.setId((long) i);
            newSample.setTupla(newColumns);
            newBuffer.add(newSample);
        }
        return newBuffer;
    }

    public static void removeColumnFromBuffer(LinkedList<GenericRowBean> data, Integer column) {
        if (column != -1) {
            for (GenericRowBean bean : data) {
                for (int i = 0; i < bean.getTupla().size(); i++) {
                    if (i == (column - 1)) {
                        bean.getTupla().remove(i);
                    }
                }
            }
        }
    }

    public static LinkedList<String> extractNamesColumnFromBuffer(LinkedList<GenericRowBean> data) {
        LinkedList<String> nameColumns = new LinkedList<>();
        Iterator<GenericRowBean> beanIter = data.iterator();
        if (beanIter.hasNext()) {
            GenericRowBean bean = beanIter.next();
            for (String value : bean.getTupla()) {
                nameColumns.add(value);
            }
            if (bean.getClasse() != null) {
                nameColumns.add(bean.getClasse());
            }
        }
        return nameColumns;
    }

    public static Boolean containColumn(LinkedList<GenericRowBean> data, String column) {
        Iterator<GenericRowBean> beanIter = data.iterator();
        if (beanIter.hasNext()) {
            GenericRowBean bean = beanIter.next();
            for (String value : bean.getTupla()) {
                if (column.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Integer getCountColumn(LinkedList<GenericRowBean> data) {
        int cont = 0;
        Iterator<GenericRowBean> beanIter = data.iterator();
        if (beanIter.hasNext()) {
            GenericRowBean bean = beanIter.next();
            Iterator<String> tupla = bean.getTupla().iterator();
            while (tupla.hasNext()) {
                cont++;
                tupla.next();
            }
        }
        return cont;
    }

    public static Integer getNumberColumnByName(LinkedList<GenericRowBean> data, String column) {
        int cont = 1;
        Iterator<GenericRowBean> beanIter = data.iterator();
        if (beanIter.hasNext()) {
            GenericRowBean bean = beanIter.next();
            Iterator<String> tupla = bean.getTupla().iterator();
            while (tupla.hasNext()) {
                String value = tupla.next();
                if (column.equals(value)) {
                    return cont;
                }
                cont++;
            }
        }
        return -1;
    }

    public static LinkedList<String> getColumnByName(LinkedList<GenericRowBean> data, String name) {
        LinkedList<String> classe = new LinkedList<>();
        int numberColumn = getNumberColumnByName(data, name);
        if (numberColumn != -1) {
            for (GenericRowBean bean : data) {
                int col = 1;
                for (String value : bean.getTupla()) {
                    if (col == numberColumn) {
                        classe.add(value);
                    }
                    col++;
                }
            }
        }
        return classe;
    }

    public static String getColumnNameByNumber(LinkedList<GenericRowBean> data, Integer column) {
        Iterator<GenericRowBean> beanIter = data.iterator();
        if (beanIter.hasNext()) {
            GenericRowBean bean = beanIter.next();
            int cont = 1;
            for (String value : bean.getTupla()) {
                if (cont == column) {
                    return value;
                }
                cont++;
            }
        }
        return "";
    }

    public static void setColumnClass(LinkedList<GenericRowBean> data) {
        int numberColumn = getNumberColumnByName(data, ConstDataset.CLASS);
        if (numberColumn != -1) {
            for (GenericRowBean bean : data) {
                int col = 1;
                for (String value : bean.getTupla()) {
                    if (col == numberColumn) {
                        bean.setClasse(value);
                    }
                    col++;
                }
            }
        }
        removeColumnFromBuffer(data, numberColumn);
    }

    public static void setColumnTimestamp(LinkedList<GenericRowBean> data) {
        int numberColumn = getNumberColumnByName(data, ConstDataset.TIMESTAMP);
        if (numberColumn != -1) {
            for (GenericRowBean bean : data) {
                int col = 1;
                for (String value : bean.getTupla()) {
                    if (col == numberColumn) {
                        bean.setTimestamp(value);
                    }
                    col++;
                }
            }
        }
        removeColumnFromBuffer(data, numberColumn);
    }

    public static void addClassToTupla(LinkedList<GenericRowBean> data) {
        Iterator<GenericRowBean> beanIter = data.iterator();
        if (beanIter.hasNext()) {
            GenericRowBean beanFirst = beanIter.next();
            if (beanFirst.getClasse() != null && !containColumn(data, ConstDataset.CLASS)) {
                for (GenericRowBean bean : data) {
                    bean.getTupla().add(bean.getClasse());
                }
            }
        }
    }

    public static LinkedList<Long> getTimestampColumn(LinkedList<GenericRowBean> data) {
        LinkedList<Long> timestamp = new LinkedList<>();
        for (int i = 0; i < data.size(); i++) {
            timestamp.add((long) i);
        }
        return timestamp;
    }

    public static LinkedList<Float> getColumnFloatByNumber(LinkedList<GenericRowBean> data, Integer numberColumn) {
        LinkedList<Float> column = new LinkedList<>();
        for (GenericRowBean bean : data) {
            int col = 1;
            for (String value : bean.getTupla()) {
                if (col == numberColumn) {
                    try {
                        column.add(Float.parseFloat(value));
                    } catch (NumberFormatException ex) {
                        Messages msg = new Messages();
                        msg.aviso("This column is not a numeric column!");
                        break;
                    }
                }
                col++;
            }
        }
        return column;
    }

    public static LinkedList<GenericRowBean> convertColumnsToGenericData(LinkedList<LinkedList<String>> lineColumns) {
        System.out.println("Convert line columns to generic data...");
        LinkedList<GenericRowBean> newData = new LinkedList<>();
        Iterator<LinkedList<String>> lineColIter = lineColumns.iterator();
        if (lineColIter.hasNext()) {
            LinkedList<String> lineColumnFist = lineColIter.next();
            int i = 0;
            for (String row : lineColumnFist) {
                GenericRowBean newRow = new GenericRowBean();
                LinkedList<String> newColumns = new LinkedList<>();
                newColumns.add(row);
                newRow.setTupla(newColumns);
                newRow.setId((long) i);
                newData.add(newRow);
                i++;
            }
        }
        while (lineColIter.hasNext()) {
            LinkedList<String> lineColumn = lineColIter.next();
            Iterator<GenericRowBean> genericIter = newData.iterator();
            for (String row : lineColumn) {
                GenericRowBean bean = genericIter.next();
                bean.getTupla().add(row);
            }
        }
        return newData;
    }

    public static LinkedList<GenericRowBean> convertColumnToGenericData(LinkedList<String> column) {
        LinkedList<GenericRowBean> newData = new LinkedList<>();
        for (String value : column) {
            GenericRowBean newRow = new GenericRowBean();
            LinkedList<String> newColumns = new LinkedList<>();
            newColumns.add(value);
            newRow.setTupla(newColumns);
            newData.add(newRow);
        }
        return newData;
    }

    public static void addClassToGenericData(LinkedList<GenericRowBean> data, LinkedList<String> classes) {
        System.out.println("Adding class to generic data...");
        if (data.size() <= classes.size()) {
            Iterator<String> classesIter = classes.iterator();
            if (classesIter.hasNext()) {
                for (GenericRowBean bean : data) {
                    bean.setClasse(classesIter.next());
                }
            }
        }
    }

    public static String stringMoreFrequency(LinkedList<String> vector, boolean noise) {
        HashMap<String, Integer> frequency = new HashMap<>();
        for (String value : vector) {
            if (frequency.containsKey(value)) {
                int cont = frequency.get(value);
                frequency.put(value, cont++);
            } else {
                frequency.put(value, 1);
            }
        }
        if (noise && frequency.size() > 1) {
            return ConstGeneral.NOISE;
        }
        int maior = 0;
        String classe = "";
        for (String value : frequency.keySet()) {
            if (frequency.get(value) > maior) {
                maior = frequency.get(value);
                classe = value;
            }
        }
        return classe;
    }

}
