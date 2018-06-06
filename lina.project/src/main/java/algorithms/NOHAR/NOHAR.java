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
import controle.constants.Parameters;
import datasets.memory.BufferStreaming;
import datasets.memory.WordInterval;
import datasets.memory.WordRecord;
import java.awt.Color;
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
    private double label = -1;

    private EvaluationNohar eval;

    public NOHAR(SymbolicView symbolicView) {
        this.symbolicView = symbolicView;

        adwin = new Adwin(.1, Parameters.BOP_SIZE);
        pageHinkley = new PageHinkley();
        params = new Params(Parameters.WINDOW_SIZE, Parameters.WORD_LENGTH_PAA,
                Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.EXACT);
        eval = new EvaluationNohar();
    }

    public EvaluationNohar getEval() {
        return eval;
    }

    public void runStream(BufferStreaming buffer, double currentValue, int position, double label) {
        this.label = label;
        //Monitor changin in the data
        if (changeDetected(currentValue, position)) {
            buffer.incrementChangeHistogram();
        } else {
            //Discretize
            WordRecord word = discretize(buffer, position);
            //Update Histogram
            updateHistogram(buffer, word);
            //Handle model
            if (position % Parameters.BOP_SIZE == 0) { //*** change threhold to BOP 

                if (buffer.getCountChangeHistogram() == 0) {
                    buffer.getHistograms().add(buffer.getHistogram());
                    if (buffer.getHistograms().size() > 2) { //=================deleta os ultimos histogramas para limpar o buffer
                        buffer.getHistograms().remove(0);
                    }

                    //Convert histogram to Polygon
                    Collections.sort(buffer.getHistogram());
                    Polygon polygon = convertHistogramToPolygon(buffer.getHistogram());

                    //Learning
                    learning(buffer, polygon);

                    symbolicView.addHistogramsAndPolygons(buffer);
                }
                buffer.resetChangeHistogram();
                symbolicView.clearCurrentHistogram(buffer);
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
//                symbolicView.updateLog("Change Detected: " + position);
                symbolicView.addMarkerGraphLine(position, Color.RED);
                return true;
            }
        } else if (pageHinkley.runStreaming(currentValue, position)) {
//            symbolicView.updateLog("Change Detected: " + position);
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
        //Redução de numerosidade por EXACT Strategy 
        if (Parameters.NUM_REDUCTION && previousWord != null && lastPosPreviusWord < Parameters.WINDOW_SIZE
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
                    if (Parameters.ALINGMENT && !overlap(wordRecord, word)) {
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
            Coordinate coordinate = new Coordinate((index + 1), word.getFrequency());
            coords[index] = coordinate;
            index++;
        }
        //Close histogram polygon
        Coordinate coordinate = new Coordinate(index, 0);
        coords[index] = coordinate;
        Coordinate coordinate2 = new Coordinate(1, 0);
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
//        Geometry pSmoothed = PolygonLabel.getSmoothedPolygon(pTest, Parameters.SMOOTHED);
        if (buffer.getHistograms().isEmpty()) {
            buffer.getPolygonUnknown().add(newPolygon(buffer, pTest));
        } else if (!verififyPolygons(buffer.getPolygonKnown(), pTest)) {
            if (verififyPolygons(buffer.getPolygonNovel(), pTest)) {
                checkNovel(buffer);
            } else if (!checkUnknown(buffer, pTest)) {
                buffer.getPolygonUnknown().add(newPolygon(buffer, pTest));
            }
        }
        checkForget();
    }

    private PolygonInfo newPolygon(BufferStreaming buffer, Polygon pTest) {
        PolygonInfo polygonInfo = new PolygonInfo();
        polygonInfo.setPolygon(pTest);
        polygonInfo.setName("" + buffer.getAmountPolygons());
        symbolicView.updateLog("New unknown polygon: " + polygonInfo.getName());
        return polygonInfo;
    }

    private boolean verififyPolygons(List<PolygonInfo> polygons, Polygon pTest) {
        for (PolygonInfo pModel : polygons) {
            //Verify who are within from each other
            int action = testPolygon(pModel.getPolygon(), pTest);
            switch (action) {
                case 1:
                    classify(pModel);
                    return true;
                case 2:
                    update(pModel, pTest);
                    return true;
                default:
                    //else Verifica the difference with slack border
                    Geometry pClassBorder = PolygonLabel.getPolygonBuffer(pModel.getPolygon(), Parameters.DISTANCE_BORDER);
                    action = testPolygon((Polygon) pClassBorder, pTest);
                    if (action > 0) {
                        symbolicView.updateLog("> Concept drift");
                        update(pModel, pTest);
                        return true;
                    }
            }
        }
        return false;
    }

    /*
    * 1) Verificar o poligono de maior area
      2) Tirar a diferença do maior para o menor
      3) Se a area da diferenca for menor que 10% da area do maior entao considera igual 
      4) Se não verifica a folga do poligono da classe e retorna para o passo 2
     */
    private int testPolygon(Polygon pModel, Polygon pTest) {
        Geometry pMaior, pMenor;
        if (pModel.getArea() == pTest.getArea()) {
            return 1;
        } else if (pModel.getArea() > pTest.getArea()) {
            pMaior = pModel;
            pMenor = pTest;
        } else {
            pMaior = pTest;
            pMenor = pModel;
        }
        Geometry diff = pMaior.difference(pMenor);
        if (diff.getArea() <= pMaior.getArea() * 0.25) {//======================
            if (pMenor.within(pMaior)) {
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }

    private void classify(PolygonInfo polygon) {
        polygon.incrementCountClassified();
        symbolicView.updateLog("Classify polygon: " + polygon.getName() + " class: " + polygon.getClasse());
        if (polygon.getClasse() == label) {
            eval.incrementHists();
            symbolicView.updateLog(">> Acertou");
        } else {
            eval.incrementErrors();
            symbolicView.updateLog(">> Errou: " + label);
        }
    }

    private void update(PolygonInfo pModel, Polygon pTest) {
        Geometry pUnion = pModel.getPolygon().union(pTest); //==================
        pModel.setPolygon((Polygon) pUnion);
        pModel.incrementCountUpdated();
        pModel.setUpdated(Calendar.getInstance());
        symbolicView.updateLog("Update polygon: " + pModel.getName() + " class: " + pModel.getClasse());
        if (pModel.getClasse() == label) {
            eval.incrementHists();
            symbolicView.updateLog(">> Acertou");
        } else {
            eval.incrementErrors();
            symbolicView.updateLog(">> Errou: " + label);
        }
    }

    //Before active learnig
    private boolean checkUnknown(BufferStreaming buffer, Polygon pTest) {
        //Pensar em um merge aqui?
        for (int i = 0; i < buffer.getPolygonUnknown().size(); i++) {
            PolygonInfo pUnknown = buffer.getPolygonUnknown().get(i);
            if (testPolygon(pUnknown.getPolygon(), pTest) > 0) {
                pUnknown.incrementCountClassified();
                pUnknown.setClasse(label);
                if (pUnknown.getCountClassified() > 3) { //=====================
                    //Active learning
                    //Messages msg = new Messages();
                    //msg.inserirDadosComValorInicial("Inform the activity!", pUnknown.getName());
                    buffer.getPolygonNovel().add(pUnknown);
                    buffer.getPolygonUnknown().remove(i);
                    symbolicView.updateLog("New novel polygon: " + pUnknown.getName() + " class: " + pUnknown.getClasse());
                }
                return true;
            }
        }
        return false;
    }

    //After active learning
    private void checkNovel(BufferStreaming buffer) {
        for (int i = 0; i < buffer.getPolygonNovel().size(); i++) {
            PolygonInfo pNovel = buffer.getPolygonNovel().get(i);
            if (pNovel.getCountClassified() > 5) { //===========================
                buffer.getPolygonKnown().add(pNovel);
                symbolicView.updateLog("New known polygon: " + pNovel.getName() + " class: " + pNovel.getClasse());
                buffer.getPolygonNovel().remove(i);
                break;
            }
        }
    }

    private void checkForget() {
    }

}
