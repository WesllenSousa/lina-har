

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

    public static void main(String[] args) throws Exception {
        
        float c = 50;
        int test = (int) Math.round(0.15);
        System.out.println(test);
        c += Math.round(0.15);
        System.out.println(c);
        
        System.out.println(System.getProperty("user.home"));
        
        String teste = "SIT_TO_LIE";
        
        System.out.println(teste.contains("_TO_p"));

//        String dataset = "MPU_WEKA.arff";
//
//        LinkedList<String> columns = FileUtil.extractNamesColumnFromFile(ConstDataset.SEPARATOR,
//                ConstDataset.DS_RAW + dataset);
//        WekaUtil wekaUtil = new WekaUtil(ConstDataset.DS_RAW + dataset, columns.size());
//
//        PrincipalComponents pca = new PrincipalComponents();
//        pca.setMaximumAttributeNames(3);
//        pca.setMaximumAttributes(1);
//        pca.setVarianceCovered(0.95);
//        pca.setInputFormat(wekaUtil.getData());
//
//        Instances newData = Filter.useFilter(wekaUtil.getData(), pca);
//
//        for (Instance instance : newData) {
//            System.out.println(instance);
//        }

    }

}
