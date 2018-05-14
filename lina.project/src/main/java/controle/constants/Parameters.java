/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.constants;

/**
 *
 * @author Wesllen Sousa
 */
public class Parameters {

    public static int WINDOW_SIZE = 50;
    public static int FREQUENCY = 50;
    public static float WINDOW_SEC = 5;

    /**
     * SAX Parameters
     */
    public static int WORD_LENGTH_PAA = 8;
    public static int SYMBOLS_ALPHABET_SIZE = 4;
    public static double NORMALIZATION_THRESHOLD = 0.05;

    /**
     * iSAX Parameters
     */
    public static int INITIAL_CARDINALITY = 4;
    public static int MAX_CARDINALITY = 32;
    public static int TOP_K = 10;

    /**
     * BOSS Parameters
     */
    public static int MAX_WINDOW_LENGTH = 80;
    public static int MIN_WINDOW_LENGTH = 50;
    public static int MAX_SYMBOL = 4;
    public static int MAX_WORD_LENGTH = 8;
    public static int MIN_WORD_LENGTH = 4;

    /**
     * NOHAR
     */
    public static int OFFSET = 1;
    public static boolean NUM_REDUCTION = true;
    public static boolean ALINGMENT = true;
    public static int CHANGE_DETECTION = 1;
    public static int BOP_SIZE = 250;

    //Só serve para exibicao na GUI
    public static int SCALA = 5;
    public static int DISTANCE_BORDER = 1;
    public static double SMOOTHED = 0.5;

}
