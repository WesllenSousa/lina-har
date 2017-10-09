package controle.arff;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import util.FileUtil;

/**
 *
 * @author Hendrio
 */
public class DataToARFF {

    public static final String ARFF = ".arff";
    public static final String SYMBOL = "@";
    public static final String RELATION = "RELATION";
    public static final String ATTRIBUTE = "ATTRIBUTE";
    public static final String DATA = "DATA";
    public static final String DATE = "DATE \"yyyy-MM-dd HH:mm:ss\"";
    public static final String NUMERIC = "NUMERIC";
    public static final String NOMINAL = "NOMINAL";
    public static final String SPACE = " ";

    private File createFile(String name) {
        File f = new File(name);
        try {
            f.createNewFile();
        } catch (Exception e) {
            System.out.println(e);
        }
        return f;
    }

    public void createARFFile(String dir, String relationName, FilePatternARFF listData) {

        File f = createFile(dir + File.separator + relationName + ARFF);

        if (f.exists()) {
            FileUtil.deleteFile(f);
        }

        System.out.println(f.getAbsolutePath());

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(f.getAbsolutePath());
        } catch (IOException ex) {
            System.out.println(ex);
        }
        System.out.println("Writting... Relation");

        try (BufferedWriter buffer = new BufferedWriter(fileWriter)) {

            /*
             *RELATION NAME
             */
            buffer.write(SYMBOL + RELATION + SPACE + "\"" + relationName + "\"\n\n");

            /*
             *ATTRIBUTE LIST WITH NAME AND TYPES
             */
            System.out.println("Writting... Attributes");
            LinkedHashMap<String, String> attributeMap = listData.getColumnTitleType();
            for (String attribute : attributeMap.keySet()) {
                if (attributeMap.get(attribute).equals(NOMINAL)) {
                    buffer.write(SYMBOL + ATTRIBUTE + SPACE + attribute + SPACE + patternNominalClass(listData.getNominalValues().get(attribute)));
                } else {
                    buffer.write(SYMBOL + ATTRIBUTE + SPACE + attribute + SPACE + attributeMap.get(attribute)); //year
                }
                buffer.write("\n");
            }
            buffer.write("\n");

            /*
             *DATA LIST
             */
            System.out.println("Writting... Data");
            buffer.write(SYMBOL + DATA + "\n");
            LinkedHashMap<Integer, LinkedList<String>> rowMap = listData.getRow();
            for (Integer row : rowMap.keySet()) {
                buffer.write(patternData(rowMap.get(row)));
                buffer.write("\n");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("End of writting...");
    }

    private String patternNominalClass(LinkedList<String> list) {
        String pattern = "";
        if (list != null) {
            int size = list.size();
            int i = 0;
            pattern = "{";
            for (String l : list) {
                if (i < size - 1) {
                    pattern = pattern + l + ",";
                } else {
                    pattern = pattern + l;
                }
                i++;
            }
            pattern = pattern + "}";
        }
        return pattern;
    }

    private String patternData(LinkedList<String> list) {
        String pattern = "";
        int size = list.size();
        int i = 0;
        for (String l : list) {
            if (i < size - 1) {
                pattern = pattern + l + ",";
            } else {
                pattern = pattern + l;
            }
            i++;
        }
        return pattern;
    }

}
