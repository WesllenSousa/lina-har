/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.weka;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author Wesllen Sousa
 */
public class FilePatternARFF {

    private LinkedHashMap<String, String> columnTitleType; //Name, Type (DataToARFF.NOMINAL)
    private LinkedHashMap<Integer, LinkedList<String>> row;
    private LinkedHashMap<String, LinkedList<String>> nominalValues;

    public LinkedHashMap<String, String> getColumnTitleType() {
        return columnTitleType;
    }

    public void setColumnTitleType(LinkedHashMap<String, String> columnTitleType) {
        this.columnTitleType = columnTitleType;
    }

    public LinkedHashMap<Integer, LinkedList<String>> getRow() {
        return row;
    }

    public void setRow(LinkedHashMap<Integer, LinkedList<String>> row) {
        this.row = row;
    }

    public LinkedHashMap<String, LinkedList<String>> getNominalValues() {
        return nominalValues;
    }

    public void setNominalValues(LinkedHashMap<String, LinkedList<String>> nominalValues) {
        this.nominalValues = nominalValues;
    }

}
