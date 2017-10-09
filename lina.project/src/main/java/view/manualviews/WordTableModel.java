package view.manualviews;

import datasets.memory.WordRecord;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Wesllen Sousa Lima
 */
public class WordTableModel extends AbstractTableModel {

    private ArrayList linhas = null;
    private String[] colunas = {"Index", "Word", "Frequency"};

    public WordTableModel(ArrayList<WordRecord> dados) {
        setLinhas(dados);
    }

    public String[] getColunas() {
        return colunas;
    }

    public ArrayList<WordRecord> getLinhas() {
        return linhas;
    }

    public void setColunas(String[] strings) {
        colunas = strings;
    }

    private void setLinhas(ArrayList<WordRecord> list) {
        linhas = list;
    }

    //Retorna o numero de colunas no modelo
    //@see javax.swing.table.TableModel#getColumnCount()
    @Override
    public int getColumnCount() {
        return getColunas().length;
    }

    //Retorna o numero de linhas existentes no modelo
    //@see javax.swing.table.TableModel#getRowCount()
    @Override
    public int getRowCount() {
        return getLinhas().size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        WordRecord bean = (WordRecord) linhas.get(rowIndex);
        if (columnIndex != -1) {
            switch (columnIndex) {
                case 0:
                    return rowIndex;
                case 1:
                    return bean.getWord();
                case 2:
                    return bean.getFrequency();
            }
        } else if (columnIndex == -1) {
            return bean;
        }
        return null;
    }

}
