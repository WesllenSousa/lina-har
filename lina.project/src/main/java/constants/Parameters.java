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
    public static int WORD_LENGTH_PAA = 8;
    public static int SYMBOLS_ALPHABET_SIZE = 4;

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

    /**
     * SFA Parameters
     */
    public static int MCB_SIZE = 50;
    public static int BOP_SIZE = 250;

    /**
     * BOSS Parameters
     */
    public static int MAX_WINDOW_LENGTH = 500;
    public static int MIN_WINDOW_LENGTH = 50;
    public static int MAX_SYMBOL = 4;
    public static int MAX_WORD_LENGTH = 12;
    public static int MIN_WORD_LENGTH = 6;

}
