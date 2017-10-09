package datasets.generic;

import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Wesllen Sousa Lima,Hendrio Luis
 */
public class GenericTableModel extends AbstractTableModel {

    private LinkedList linhas = null;
    private String[] colunas = {};

    public GenericTableModel(LinkedList<GenericRowBean> dados) {
        setLinhas(dados);
    }

    public String[] getColunas() {
        return colunas;
    }

    public LinkedList<GenericRowBean> getLinhas() {
        return linhas;
    }

    public void setColunas(LinkedList<String> strings) {
        colunas = new String[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            colunas[i] = strings.get(i);
        }
    }

    private void setLinhas(LinkedList<GenericRowBean> list) {
        linhas = list;
    }

    public void adicionarLinha(GenericRowBean pessoa) {
        linhas.add(pessoa);
    }

    public void removeLinha(GenericRowBean pessoa) {
        linhas.remove(pessoa);
    }

    public void removeTudo() {
        linhas.clear();
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
        GenericRowBean bean = (GenericRowBean) linhas.get(rowIndex);
        if (columnIndex != -1) {
            for (int i = 0; i < bean.getTupla().size(); i++) {
                if (columnIndex == i) {
                    return bean.getTupla().get(columnIndex);
                }
            }
            if (bean.getClasse() != null) {
                if (columnIndex == bean.getTupla().size()) {
                    return bean.getClasse();
                }
            }
        } else if (columnIndex == -1) {
            return bean;
        }
        return null;
    }

}
