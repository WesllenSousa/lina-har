/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import constants.ConstGeneral;
import controle.SAX.Params;
import controle.SAX.SAX;
import controle.pageHinkley.PageHinkley;
import controle.pageHinkley.PageHinkleyBean;
import datasets.generic.GenericRowBean;
import datasets.memory.WordInterval;
import datasets.memory.WordRecord;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import view.manualviews.BarGraphic;
import view.manualviews.LineGraphic;

/**
 *
 * @author Wesllen Sousa
 */
public class SymbolicView {

    private static int WINDOW_SIZE = 28;

    private LinkedHashSet<GenericRowBean> data;
    private ArrayList<WordRecord> wordsRecord;
    private PageHinkley pageHinkley;

    private final LineGraphic lineGraphic;
    private final BarGraphic barGraphic;
    private final Params params;

    private String previousWord = null;

    public SymbolicView(LineGraphic lineGraphic, BarGraphic barGraphic, LinkedHashSet<GenericRowBean> data) {
        this.lineGraphic = lineGraphic;
        this.barGraphic = barGraphic;
        this.data = data;

        pageHinkley = new PageHinkley();
        wordsRecord = new ArrayList<>();

        this.lineGraphic.prepareStream(data, true);

        int SAX_PAA = 7;
        int SAX_ALPHABET = 4;
        Double NORMALIZATION_THRESHOLD = 0.05;
        params = new Params(WINDOW_SIZE, SAX_PAA, SAX_ALPHABET,
                NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.NONE);
    }

    public void runDataset() {
        LinkedList<Double> window = new LinkedList<>();
        pageHinkley = new PageHinkley();

        int position = 0;
        for (GenericRowBean bean : data) {
            if (position == 0) {
                position++;
                continue;
            } else {
                Double value = Double.parseDouble(bean.getTupla().get(0));
                window.add(value);

                lineGraphic.addData(bean);
                lineGraphic.espera(10);
                pageHinkley.runStreaming(value, position);

                if (position > WINDOW_SIZE) {
                    discretize(convertToVector(window), position);
                    window.removeFirst(); //offset de 1
                }
            }
            position++;
        }
    }

    private void discretize(double[] window, int position) {
        String word = SAX.serieToWord(window, params);

        //verificar distancia entre as palavras...
        WordRecord wordRecord = getWordRecord(word, position);
        updateWordsRecord(wordRecord);
    }

    private void updateWordsRecord(WordRecord wordRecord) {
        //Aqui já é a redução de numerozidade por EXACT Strategy
        if (wordsRecord.contains(wordRecord)) {
            for (WordRecord word : wordsRecord) {
                if (word.getWord().equals(wordRecord.getWord())) {
                    word.getIntervals().add(wordRecord.getIntervals().get(0));
                    word.setFrequency(wordRecord.getFrequency());
                }
            }
        } else {
            wordsRecord.add(wordRecord);
            ConstGeneral.TELA_PRINCIPAL.updateSymbolicTab(wordRecord, wordsRecord.size());
        }
        //Atualiza tabela das palavras
        ConstGeneral.TELA_PRINCIPAL.tb_words.updateUI();
        barGraphic.addUpdateData(wordRecord.getWord(), wordRecord.getFrequency());
    }

    private WordRecord getWordRecord(String word, int position) {
        WordInterval wordInterval = new WordInterval();
        wordInterval.setPositionInit(position - WINDOW_SIZE);
        wordInterval.setPositionEnd(position);

        WordRecord wordRecord = new WordRecord();
        wordRecord.setWord(word);
        wordRecord.setFrequency(barGraphic.getWordFrequency(word));
        wordRecord.getIntervals().add(wordInterval);

        return wordRecord;
    }

    private double[] convertToVector(LinkedList<Double> list) {
        double[] vetor = new double[list.size()];
        int i = 0;
        for (Double valor : list) {
            vetor[i] = valor;
            i++;
        }
        return vetor;
    }

    public void espera(int miliSec) {
        try {
            Thread.sleep(miliSec);
        } catch (InterruptedException ex) {
        }
    }

    public void showPageHinkleyChanges() {
        for (PageHinkleyBean bean : pageHinkley.getListChanges()) {
            lineGraphic.addMarker(bean.getPosition(), bean.getPosition(), Color.black);
        }
    }

}
