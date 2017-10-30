/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

/**
 *
 * @author Wesllen Sousa
 */
public class Parameters {

    public static int WINDOW_SIZE = 50;
    public static int OFFSET = 1;
    public static boolean NORM = true;
    public static boolean NUM_REDUCTION = true;
    
    public static int WORD_LENGTH_PAA = 10;
    public static int SYMBOLS_PAA = 8;
    public static int ALPHABET = 4;

    /**
     * SAX Parameters
     */
    public static double NORMALIZATION_THRESHOLD = 0.05;

    /**
     * iSAX Parameters
     */
    public static int INITIAL_CARDINALITY = 4;
    public static int MAX_CARDINALITY = 32;
    public static int TOP_K = 10;

}
