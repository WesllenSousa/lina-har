/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import constants.ConstGeneral;
import constants.Parameters;
import controle.SAX.Params;
import controle.SAX.SAX;
import controle.pageHinkley.PageHinkley;
import controle.pageHinkley.PageHinkleyBean;
import datasets.memory.WordInterval;
import datasets.memory.WordRecord;
import datasets.timeseries.TimeSeries;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import view.manualviews.BarGraphic;
import view.manualviews.LineGraphic;

/**
 *
 * @author Wesllen Sousa
 */
public class SymbolicView {

    private TimeSeries[] data;
    private ArrayList<WordRecord> wordsRecord = new ArrayList<>();
    ArrayList<PageHinkley> listPH = new ArrayList<>();

    private final LineGraphic lineGraphic;
    private final BarGraphic barGraphic;
    private final Params params;

    private String previousWord = null;

    public SymbolicView(LineGraphic lineGraphic, BarGraphic barGraphic, TimeSeries[] data) {
        this.lineGraphic = lineGraphic;
        this.barGraphic = barGraphic;
        this.data = data;

        this.lineGraphic.prepareStream(data);

        params = new Params(Parameters.WINDOW_SIZE, Parameters.SAX_PAA, Parameters.SAX_ALPHABET,
                Parameters.NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.NONE);
    }

    public void runDataset() {
        //Initial data from the first window
        ArrayList<TimeSeries> initialData = new ArrayList<>();
        Random randomCor = new Random();
        int col = 0;
        for (TimeSeries timeSeries : data) {
            //Get first subsequence
            TimeSeries ts = timeSeries.getSubsequence(0, params.getWindowSize());
            initialData.add(ts);
            //Apply page hinkley in the first subsequence
            PageHinkley ph = new PageHinkley(new Color(randomCor.nextInt(255), randomCor.nextInt(255), randomCor.nextInt(255)));
            ph.runTs(ts.getData());
            listPH.add(col, ph);
            col++;
        }
        lineGraphic.addSubsequenceData(initialData.toArray(new TimeSeries[]{}));

        //Remaining of the data for each value - streaming
        for (int position = params.getWindowSize(); position < (data[0].getLength() - params.getWindowSize()); position++) {
            double[] values = new double[data.length];
            col = 0;
            for (TimeSeries ts : data) {
                double value = ts.getData(position);
                values[col] = value;
                processValue(value, position, col);
                col++;
            }
            lineGraphic.addData(values);
            lineGraphic.espera(10);
        }
    }

    private void processValue(double value, int position, int col) {
        listPH.get(col).runStreaming(value, position);

        //                discretize(ts.getData(), position);
    }

    private void discretize(double[] window, int position) {
        String word = SAX.serieToWord(window, params);

        //verificar distancia entre as palavras...
        WordRecord wordRecord = getWordRecord(word, position);
        updateWordsRecord(wordRecord);
    }

    private WordRecord getWordRecord(String word, int position) {
        WordInterval wordInterval = new WordInterval();
        wordInterval.setPositionInit(position - params.getWindowSize());
        wordInterval.setPositionEnd(position);

        WordRecord wordRecord = new WordRecord();
        wordRecord.setWord(word);
        wordRecord.setFrequency(barGraphic.getWordFrequency(word));
        wordRecord.getIntervals().add(wordInterval);

        return wordRecord;
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

    public void showPageHinkleyChanges() {
        for (PageHinkley ph : listPH) {
            for (PageHinkleyBean bean : ph.getListChanges()) {
                lineGraphic.addMarker(bean.getPosition(), bean.getPosition(), bean.getCor());
            }
        }
    }

}
