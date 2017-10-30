package controle.iSAX;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import constants.Parameters;
import ex.AlphabetTooLargeException;
import isax.Word;
import java.util.Arrays;

import isax.WordElem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import motif.IntPair;
import motif.MotifCount;
import motif.MotifTree;
import motif.MotifTreeElement;

import net.seninp.jmotif.sax.datastructure.SAXRecords;

/**
 *
 * @author Wesllen Sousa
 */
public class iSAX {

    private final MotifTree motifTree = new MotifTree();
    private final StreamSummaries streamSummary = new StreamSummaries();

    public MotifCountmanager count = new MotifCountmanager();
    private HashMap<IntPair, Integer> collision;

    public SAXRecords ts2iSaxViaWindow(double[] ts, int windowSize) throws AlphabetTooLargeException {
        SAXRecords sAXRecords = new SAXRecords();

        for (int i = 0; i <= ts.length - windowSize; i++) {
            // fix the current subsection
            double[] subSection = Arrays.copyOfRange(ts, i, i + windowSize);

            Word iSaxWord = new Word(subSection, Parameters.WORD_LENGTH_PAA, Parameters.INITIAL_CARDINALITY);
            sAXRecords.add(iSAXwordToChar(iSaxWord), i);

            processWord(iSaxWord, subSection);

            System.out.println("---");
            System.out.println(" >>> " + iSaxWord.toDiskString());
            System.out.println(motifTree);

            for (Integer element : motifTree.keySet()) {
                String words = iSaxWord.toString();
                MotifTreeElement motifTreeElement = motifTree.get(element);
                for (Word word : motifTreeElement.keySet()) {
                    if (word.equals(iSaxWord)) {
                        words += " = " + motifTreeElement.get(word);
                    }
                }
                System.out.println(" >> " + words);
            }

        }

        return sAXRecords;
    }

    public void processWord(Word iSaxWord, double[] buf) throws AlphabetTooLargeException {
        int currentCardinality = Parameters.INITIAL_CARDINALITY;
        Word parentWord = iSaxWord;
        while (currentCardinality <= Parameters.MAX_CARDINALITY) {
            Word childWord = incrementWord(buf, currentCardinality);
            if (currentCardinality > Parameters.INITIAL_CARDINALITY) {
                motifTree.update(currentCardinality / 2, parentWord, childWord);
            }
            currentCardinality *= 2;
            parentWord = childWord;
        }
    }

    private Word incrementWord(double[] series, int cardinality) throws AlphabetTooLargeException {
        Word promotedWord = new Word(series, Parameters.WORD_LENGTH_PAA, cardinality);

        if (count.get(cardinality).containsKey(promotedWord)) {
            // increment count.
            this.count.incCount(cardinality, promotedWord);
        } else {
            incrementNotMonitored(promotedWord, cardinality);
        }

        this.streamSummary.updateBucket(promotedWord, cardinality, this.count
                .get(cardinality).get(promotedWord));

        return promotedWord;
    }

    private void incrementNotMonitored(Word saxWord, int currentCardinality) {
        // check if the list is full
        MotifCount motifCount = count.get(currentCardinality);

        if (motifCount.size() >= Parameters.TOP_K) {
            // update last and increment
            Word lw = streamSummary.get(currentCardinality).getLast();

            int lastCount = 0;
            if (lw != null) {
                /**
                 * Things to do when the list is full and you want to delete the
                 * last element of it.
                 */
                lastCount = motifCount.get(lw);
                motifCount.remove(lw);
                streamSummary.get(currentCardinality).get(lastCount).remove(lw);

                if (motifTree.get(currentCardinality) != null) {
                    if (motifTree.get(currentCardinality).containsKey(lw)) {
                        motifTree.get(currentCardinality).remove(lw);
                    } else {
                        System.err.println("current card = null");
                    }
                }

            } else {
                System.err.println("LW should not occur");
            }
            motifCount.put(saxWord, lastCount + 1);

        } else { // not full. simply insert and update last.
            count.incCount(currentCardinality, saxWord);
        }
    }

    public void pValue(ArrayList<Word> words) {

        /**
         * Choose B positions and which columns to act as a mask///
         */
        Random r = new Random();
        int b = 7;//r.nextInt(params.getWordLength()-params.bMin)+params.bMin;
        ArrayList<Integer> cols = new ArrayList<Integer>();

        for (int i = 0; i < b; i++) {
            int tentativeColumn = r.nextInt(Parameters.WORD_LENGTH_PAA);

            if (!cols.contains(tentativeColumn)) {
                cols.add(tentativeColumn);
            } else {
                i--;
            }
        }

        Collections.sort(cols);

        System.out.println("b=" + b);
        System.out.println("Mask columns: " + cols);
        /////////////////////////////////////////////////////////////

        //FOR  !I! ITERATIONS
        System.out.println("WORD LIST SIZE: " + words.size());
        for (int i = 0; i < words.size(); i++) {

            Word wiMask = words.get(i).mask(cols);
            Word wj = null;

            for (int j = 0; j < words.size(); j++) {

                if (i == j) {
                    continue;
                }

                wj = words.get(j);

                if (wiMask.maskEquals(wj, cols)) {
                    //WE have a match, increment collision matrix positions
                    IntPair match = new IntPair(i, j);
                    //System.out.println(match);
                    Integer count = collision.get(match);
                    if (count != null) {
                        collision.put(match, count + 1);
                    } else {
                        collision.put(match, 1);
                    }

                } else {
                    //We DONT have a match, keep on 
                }
            }
        }
    }

    private char[] iSAXwordToChar(Word iSaxWord) {
        char[] res = new char[iSaxWord.size];

        for (int i = 0; i < iSaxWord.word.size(); i++) {
            WordElem elem = iSaxWord.word.get(i);
            res[i] = (char) elem.value.intValue();
        }

        return res;
    }

}
