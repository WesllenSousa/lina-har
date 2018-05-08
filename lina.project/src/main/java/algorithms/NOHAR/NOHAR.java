/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR;

import algorithms.NOHAR.ADWIN.Adwin;
import algorithms.NOHAR.Polygon.PolygonInfo;
import algorithms.NOHAR.Polygon.PolygonLabel;
import algorithms.NOHAR.PageHinkley.PageHinkley;
import algorithms.SAX.Params;
import algorithms.SAX.SAX;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import controle.constants.ConstGeneral;
import controle.constants.Parameters;
import datasets.memory.BufferStreaming;
import datasets.memory.WordInterval;
import datasets.memory.WordRecord;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import view.viewControler.SymbolicView;

/**
 *
 * @author Wesllen Sousa
 */
public class NOHAR {

    private final SymbolicView symbolicView;
    private final Adwin adwin;
    private final PageHinkley pageHinkley;
    private final Params params;

    private WordRecord previousWord;
    private int lastPosPreviusWord = 0;

    public NOHAR(SymbolicView symbolicView) {
        this.symbolicView = symbolicView;

        adwin = new Adwin(.1, Parameters.BOP_SIZE);
        pageHinkley = new PageHinkley();
        params = new Params(Parameters.WINDOW_SIZE, Parameters.WORD_LENGTH_PAA,
                Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.EXACT);
    }

    public void runStream(BufferStreaming buffer, double currentValue, int position) {
        //Monitor changin in the data
        if (!changeDetected(currentValue, position)) {
            //Discretize
            WordRecord word = discretize(buffer, position);

            //Update Histogram
            updateHistogram(buffer, word);

            //Handle model
            if (position % Parameters.BOP_SIZE == 0) { //*** change threhold to BOP 
                //Convert histogram to Polygon
                Collections.sort(buffer.getHistogram());
                Polygon polygon = convertHistogramToPolygon(buffer.getHistogram());

                //Learning
                learning(buffer, polygon);

                symbolicView.clearCurrentHistogram(buffer);
                symbolicView.addPolygons((ArrayList<PolygonInfo>) buffer.getPolygons());
                symbolicView.addMarkerGraphLine(position, Color.BLACK);
            }
        }
    }

    /*
     *   Detecção de mudança
     */
    private boolean changeDetected(double currentValue, int position) {
        if (Parameters.CHANGE_DETECTION == 0) {
            if (adwin.setInput(currentValue)) {
                symbolicView.updateLog("Change Detected: " + position);
                symbolicView.addMarkerGraphLine(position, Color.RED);
                return true;
            }
        } else if (pageHinkley.runStreaming(currentValue, position)) {
            symbolicView.updateLog("Change Detected: " + position);
            symbolicView.addMarkerGraphLine(position, Color.RED);
            return true;
        }
        return false;
    }

    /*
     *   Discretização
     */
    private WordRecord discretize(BufferStreaming buffer, int position) {
        SAX sax = new SAX(params);
        String word = sax.serieToWord(buffer.getSubSequence().getData());
        WordRecord wordRecord = buffer.populaWordRecord(word, position - Parameters.WINDOW_SIZE);
        return wordRecord;
    }

    /*
     *   Criação de histogramas
     */
    private void updateHistogram(BufferStreaming buffer, WordRecord word) {
        if (word == null) {
            return;
        }
        //Redução de numerozidade por EXACT Strategy 
        if (ConstGeneral.NUM_REDUCTION && previousWord != null && lastPosPreviusWord < Parameters.WINDOW_SIZE
                && previousWord.getWord().equals(word.getWord())) {
            //For word that repeat always, we update the frequency
            lastPosPreviusWord++;
            return;
        }
        lastPosPreviusWord = 0;

        //Verify if word is in the buffer BOP
        if (buffer.getHistogram().contains(word)) { //Bloco de código passivel de futuras otimizacoes
            for (WordRecord wordRecord : buffer.getHistogram()) {
                if (wordRecord.getWord().equals(word.getWord())) {
                    //Verify interval overlaped to same words: alingments
                    if (ConstGeneral.ALINGMENT && !overlap(wordRecord, word)) {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.incrementFrequency();
                    } else {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.incrementFrequency();
                    }
                    previousWord = word;
                    symbolicView.updateCurrentHistogram(buffer, wordRecord);
                }
            }
        } else {
            //Add word in buffer
            previousWord = word;
            buffer.getHistogram().add(word);
            symbolicView.updateCurrentHistogram(buffer, word);
        }
    }

    //O overlap já é uma espécie de alinhamento de palavras iguais
    private boolean overlap(WordRecord words, WordRecord word) {
        int init = word.getIntervals().get(0).getPositionInit();
        int end = word.getIntervals().get(0).getPositionEnd();
        for (WordInterval interval : words.getIntervals()) {
            int cInit = interval.getPositionInit();
            int cEnd = interval.getPositionEnd();
            if ((init >= cInit && init <= cEnd) || (end >= cInit && end <= cEnd)
                    || (cInit >= init && cInit <= end) || (cEnd >= init && cEnd <= end)) {
                return true;
            }
        }
        return false;
    }

    /*
     *   Features
     */
    public Polygon convertHistogramToPolygon(List<WordRecord> histogram) {
        //Get frequency polygon
        Coordinate[] coords = new Coordinate[histogram.size() + 3];
        int index = 0;
        for (WordRecord word : histogram) {
            Coordinate coordinate = new Coordinate((index + 1) /** Parameters.SCALA*/, word.getFrequency() /** Parameters.SCALA*/);
            coords[index] = coordinate;
            index++;
        }
        //Close histogram polygon
        Coordinate coordinate = new Coordinate(index /** Parameters.SCALA*/, 0);
        coords[index] = coordinate;
        Coordinate coordinate2 = new Coordinate(1 /** Parameters.SCALA*/, 0);
        coords[index + 1] = coordinate2;
        coords[index + 2] = coords[0];

        LinearRing linearRing = new GeometryFactory().createLinearRing(coords);
        Polygon polygon = new GeometryFactory().createPolygon(linearRing, null);
        polygon.normalize();

        return polygon;
    }

    /*
     *   Aprendizagem
     */
    private void learning(BufferStreaming buffer, Polygon pTest) {

        if (buffer.getPolygons().isEmpty()) {
            newClass(buffer, pTest);
        } else {
            boolean updated = false, classified = false;
            for (PolygonInfo pClass : buffer.getPolygons()) {

                Geometry diffPClass = pClass.getPolygon().difference(pTest);

                if (!diffPClass.isEmpty() && diffPClass.getArea() <= pClass.getPolygon().getArea() * 0.1) { //10%
                    classify(pClass);
                    classified = true;
                } else {
                    Geometry pClassBorder = PolygonLabel.getPolygonBuffer(pClass.getPolygon(), Parameters.DISTANCE_BORDER);
                    Geometry pOnlyBorder = pClassBorder.difference(pClass.getPolygon());
                    Geometry diffTestBorder = pClassBorder.difference(pTest);

                    if (diffTestBorder.getArea() <= pOnlyBorder.getArea()) { //Within
                        update(pClass, pTest);
                        updated = true;
                    }
                }
            }
            if (!classified && !updated) {
                newClass(buffer, pTest);
            }
        }
    }

    private void classify(PolygonInfo polygon) {

        polygon.setCountClassified(polygon.getCountClassified() + 1);
        polygon.setCreated(Calendar.getInstance());

        System.out.println("Classify polygon");
        symbolicView.updateLog("Classify polygon");
    }

    private void update(PolygonInfo polygon, Polygon pTest) {

        polygon.setPolygon(pTest);
        polygon.setCountUpdated(polygon.getCountUpdated() + 1);
        polygon.setCreated(Calendar.getInstance());

        System.out.println("Update polygon");
        symbolicView.updateLog("Update polygon");
    }

    private void newClass(BufferStreaming buffer, Polygon pTest) {

        PolygonInfo polygonInfo = new PolygonInfo();
        polygonInfo.setPolygon(pTest);
        polygonInfo.setName("new");
        buffer.getPolygons().add(polygonInfo);

        System.out.println("New polygon");
        symbolicView.updateLog("New polygon");
    }

}
