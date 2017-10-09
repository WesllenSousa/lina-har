
import constants.ConstDataset;
import datasets.generic.GenericRowBean;
import datasets.generic.HandleGenericDataset;
import java.util.LinkedHashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wesllen Sousa
 */
public class Test {
    
    public static void main(String[] args) {
        
        LinkedHashSet<GenericRowBean> data = HandleGenericDataset.bufferFileInMemory(ConstDataset.SEPARATOR, 
                ConstDataset.DS_TIME_SERIES + "sinusoid.csv");
        
        System.out.println(data.size());
        
    }
    
}
