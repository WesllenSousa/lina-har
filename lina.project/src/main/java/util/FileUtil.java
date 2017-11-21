package util;

import constants.ConstDataset;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.TreeSet;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Wesllen Sousa Lima
 */
public class FileUtil {

    public static void main(String[] args) {
        String inputFile = "C:\\Users\\Wesllen Sousa\\Lina\\Datasets\\Raw\\train_shoaib.txt";
        String outputDir = "C:\\Users\\Wesllen Sousa\\Lina\\Datasets\\Raw";

        divideFile(inputFile, outputDir, 230000);
    }

    public static BufferedReader readFile(String diretorio) {
        try {
            File f = new File(diretorio);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            return buffer;
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public static Boolean saveFile(String dir, String texto) {
        try (OutputStream output = new FileOutputStream(new File(dir));
                OutputStreamWriter osw = new OutputStreamWriter(output);
                BufferedWriter write = new BufferedWriter(osw)) {
            write.write(texto);
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

    public static Boolean saveFile(String dir, InputStream contents) {
        try (InputStreamReader input = new InputStreamReader(contents);
                BufferedReader reader = new BufferedReader(input);
                OutputStream output = new FileOutputStream(new File(dir));
                OutputStreamWriter osw = new OutputStreamWriter(output);
                BufferedWriter write = new BufferedWriter(osw);) {

            String s = reader.readLine();
            while (s != null) {
                write.write(s);
                s = reader.readLine();
            }

            reader.close();
            input.close();
            write.close();
            osw.close();
            output.close();

            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static void replaceInFile(String source, String fileInput, String fileOutput, String oldString, String newString) {
        try (BufferedReader buffer = FileUtil.readFile(source + fileInput);
                OutputStream output = new FileOutputStream(new File(source + fileOutput));
                OutputStreamWriter osw = new OutputStreamWriter(output);
                BufferedWriter write = new BufferedWriter(osw)) {
            String linha = buffer.readLine();
            while (linha != null) {
                String novaLinha = linha.replace(oldString, newString) + "\n";
                write.write(novaLinha);
                linha = buffer.readLine();
            }
            write.close();
            osw.close();
            output.close();
            buffer.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static void removeColumnFromFile(String separador, String inputFile, String outputFile, int coluna) {
        try (BufferedReader buffer = FileUtil.readFile(inputFile);
                OutputStream output = new FileOutputStream(new File(outputFile));
                OutputStreamWriter osw = new OutputStreamWriter(output);
                BufferedWriter write = new BufferedWriter(osw)) {

            String novaLinha = "";
            int cont = 1;
            String linha = buffer.readLine();

            if (FileUtil.getFileExtension(inputFile).equals("arff")) {
                while (linha != null) {
                    if (linha.contains("@ATTRIBUTE")) {
                        if (cont == coluna) {
                            continue;
                        }
                        novaLinha = linha + "\n";
                        linha = buffer.readLine();
                        cont += 1;
                    } else if (linha.contains("@DATA")) {
                        novaLinha = linha + "\n";
                        linha = buffer.readLine();
                        break;
                    } else {
                        novaLinha = linha + "\n";
                        linha = buffer.readLine();
                    }
                }
            }

            while (linha != null) {
                String[] colunas = linha.split(separador);
                for (int i = 0; i < colunas.length; i++) {
                    if (i == (coluna - 1)) {
                        continue;
                    }
                    if ((i + 1) == colunas.length) {
                        novaLinha += colunas[i] + "\n";
                    } else if (coluna == colunas.length && (i + 2) == colunas.length) {
                        novaLinha += colunas[i] + "\n";
                    } else {
                        novaLinha += colunas[i] + ConstDataset.SEPARATOR;
                    }
                }

                write.write(novaLinha);
                linha = buffer.readLine();
            }
            write.close();
            osw.close();
            output.close();
            buffer.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static LinkedList<String> extractNamesColumnFromFile(String separador, String inputSource) {
        try (BufferedReader buffer = FileUtil.readFile(inputSource)) {
            LinkedList<String> nameColumns = new LinkedList<>();

            String linha = buffer.readLine();
            if (FileUtil.getFileExtension(inputSource).equals("arff")) {
                while (linha != null) {
                    if (linha.contains("@ATTRIBUTE")) {
                        String[] column = linha.split(" ")[1].replace(".", ",").split(","); // Need Review for char '\t'
                        nameColumns.add(column[column.length - 1]);
                        linha = buffer.readLine();
                    } else if (linha.contains("@DATA")) {
                        break;
                    } else {
                        linha = buffer.readLine();
                    }
                }
            } else {
                for (String s : linha.split(separador)) {
                    nameColumns.add(s);
                }
            }

            buffer.close();
            return nameColumns;
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    /**
     * Método responsável por divir um arquivo em n-SubArquivo recebendo como
     * parâmetro o caminho do arquivo que será divido, o diretorio onde será
     * criado os subArquivos e o numero de divisões
     *
     * @param inputFile
     * @param outputDir
     * @param NUMBER_LINE - quantidade de linhas necessária para o arquivo
     */
    public static void divideFile(String inputFile, String outputDir, int NUMBER_LINE) {
        int CURRENT_LINE = 0, cont = 1;

        try (BufferedReader buffer = readFile(inputFile)) {
            OutputStream output = new FileOutputStream(new File(outputDir + File.separator + cont + ".csv"));
            OutputStreamWriter osw = new OutputStreamWriter(output);
            BufferedWriter write = new BufferedWriter(osw);

            String linha = buffer.readLine();
            if (linha != null) {
                write.write(linha);
            }

            linha = buffer.readLine();
            while (linha != null) {
                if (CURRENT_LINE == NUMBER_LINE - 1) {
                    write.close();
                    osw.close();
                    output.close();

                    cont++;
                    output = new FileOutputStream(new File(outputDir + File.separator + cont + ".csv"));
                    osw = new OutputStreamWriter(output);
                    write = new BufferedWriter(osw);

                    write.write(linha);
                    linha = buffer.readLine();
                    CURRENT_LINE = 0;
                    continue;
                }

                write.write("\n" + linha);
                linha = buffer.readLine();
                CURRENT_LINE++;
            }
            buffer.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Método respnsável por escrever em um arquivo destino a junção de
     * sub_Arquivos
     *
     * @param inputDir //é o caminho do arquivo que possuirá a junção dos
     * sub_Arquivos
     * @param outputFile// é Diretorio que possui os sub_Arquivos
     */
    public static void joinFile(String outputFile, String inputDir) {
        try (OutputStream output = new FileOutputStream(new File(outputFile));
                OutputStreamWriter osw = new OutputStreamWriter(output);
                BufferedWriter write = new BufferedWriter(osw)) {

            LinkedHashSet<String> listFiles = FileUtil.listFiles(new File(inputDir));
            TreeSet<Integer> listOrderedFiles = new TreeSet<>();
            for (String inputFile : listFiles) {
                listOrderedFiles.add(Integer.parseInt(FileUtil.extractNameFile(inputFile)));
            }

            for (Integer inputFile : listOrderedFiles) {
                try (BufferedReader buffer = readFile(inputDir + File.separator + inputFile + ".csv")) {
                    String linha = buffer.readLine();
                    if (linha != null) {
                        write.write(linha);
                    }

                    linha = buffer.readLine();
                    while (linha != null) {
                        write.write("\n" + linha);
                        linha = buffer.readLine();
                    }

                    write.write("\n");
                    buffer.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }

            write.close();
            osw.close();
            output.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static void renameFile(String source, String newName) {
        File fTmp = new File(source);
        File fTxt = new File(extractParentPathFile(source) + File.separator + newName + "." + FileUtil.getFileExtension(source));
        fTmp.renameTo(fTxt);
    }

    public static Boolean deleteEmptyFolderFile(File dirArq) {
        if (dirArq.isFile()) {
            return deleteFile(dirArq);
        } else if (dirArq.isDirectory()) {
            return deleteFolder(dirArq);
        }
        return false;
    }

    public static Boolean deleteFolder(File diretorio) {
        boolean result = true;
        if (diretorio.exists() && diretorio.isDirectory()) {
            File[] files = diretorio.listFiles();
            File file;
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                if (file.isFile()) {
                    result = file.delete() && result;
                } else if (file.isDirectory()) {
                    result = deleteFolder(file) && result;
                }
            }
            result = diretorio.delete() && result;
        } else {
            result = false;
        }
        return result;
    }

    public static Boolean deleteFile(File file) {
        if (file.isFile()) {
            return file.delete();
        }
        return false;
    }

    public static Boolean deleteFullFolder(File diretorio) {
        boolean result = true;
        if (diretorio.exists() && diretorio.isDirectory()) {
            File[] files = diretorio.listFiles();
            File file;
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                if (file.isFile()) {
                    result = file.delete() && result;
                } else if (file.isDirectory()) {
                    result = deleteFolder(file) && result;
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    public static String extractParentPathFile(String source) {
        File f = new File(source);
        return f.getParent();
    }

    public static String extractNameFile(String path) {
        String ArqDir = "";
        for (int i = (path.length() - 1); i >= 0; i--) {
            char caracter = path.charAt(i);
            if (caracter == '/' || caracter == '\\') {
                break;
            }
            ArqDir += caracter;
        }
        String r = "";
        for (int i = (ArqDir.length() - 1); i >= 0; i--) {
            char caracter = ArqDir.charAt(i);
            r += caracter;
        }
        return r.replace(".", ",").split(",")[0];
    }

    public static LinkedHashSet<String> listFilesAndFolders(File diretorio) {
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        File[] arquivos = diretorio.listFiles();
        if (arquivos != null) {
            int length = arquivos.length;
            for (int i = 0; i < length; ++i) {
                File f = arquivos[i];
                if (f.isFile()) {
                    hashSet.add(f.getName());
                } else if (f.isDirectory()) {
                    hashSet.add(f.getName());
                }
            }
        }
        return hashSet;
    }

    public static LinkedHashSet<String> listFolders(File diretorio) {
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        File[] arquivos = diretorio.listFiles();
        if (arquivos != null) {
            int length = arquivos.length;
            for (int i = 0; i < length; ++i) {
                File f = arquivos[i];
                if (f.isDirectory()) {
                    hashSet.add(f.getName());
                }
            }
        }
        return hashSet;
    }

    public static LinkedHashSet<String> listFiles(File diretorio) {
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        File[] arquivos = diretorio.listFiles();
        if (arquivos != null) {
            int length = arquivos.length;
            for (int i = 0; i < length; ++i) {
                File f = arquivos[i];
                if (f.isFile()) {
                    hashSet.add(f.getName());
                }
            }
        }
        return hashSet;
    }

    public static TreeNode listFoldersJTree(DefaultMutableTreeNode raiz, File diretorio) {
        File[] files = diretorio.listFiles();
        File file;
        for (int i = 0; i < files.length; i++) {
            file = files[i];
            if (file.isFile()) {
                DefaultMutableTreeNode filho = new DefaultMutableTreeNode(file.getName());
                filho.setAllowsChildren(false);
                raiz.add(filho);
            } else if (file.isDirectory()) {
                DefaultMutableTreeNode filho = new DefaultMutableTreeNode(file.getName());
                raiz.add(filho);
                listFoldersJTree(filho, file);
            }
        }
        return raiz;
    }

    public static void copyFile(String origem, String destino) {
        int index = origem.lastIndexOf("\\");
        String fileName = origem.substring(index + 1);
        destino = destino + fileName;
        FileChannel o = null;
        FileChannel d = null;
        try {
            o = new FileInputStream(new File(origem)).getChannel();
            d = new FileOutputStream(new File(destino)).getChannel();
            d.transferFrom(o, 0, o.size());
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                if (o != null && o.isOpen()) {
                    o.close();
                }
                if (d != null && d.isOpen()) {
                    d.close();
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    public static void copyFile2(String origem, String destino) {
        FileReader fis = null;
        try {
            File arquivoOrigem = new File(origem);
            fis = new FileReader(arquivoOrigem);
            BufferedReader bufferedReader = new BufferedReader(fis);
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            fis.close();
            bufferedReader.close();
            File arquivoDestino = new File(destino);
            FileWriter writer = new FileWriter(arquivoDestino);
            writer.write(buffer.toString());
            writer.flush();
            writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    public static Integer countFiles(File diretorio) {
        Integer cont = 0;
        File[] arquivos = diretorio.listFiles();
        if (arquivos != null) {
            int length = arquivos.length;
            for (int i = 0; i < length; ++i) {
                File f = arquivos[i];
                if (f.isFile()) {
                    cont++;
                }
            }
        }
        return cont;
    }

    public static String getFileExtension(String arquivo) {
        return arquivo.substring(arquivo.lastIndexOf('.') + 1);
    }

    public static String getFileExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static Boolean saveTextToBytes(String texto, File diretorio) {
        byte bytes[] = texto.getBytes();
        try (FileOutputStream local = new FileOutputStream(diretorio)) {
            local.write(bytes);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

}
