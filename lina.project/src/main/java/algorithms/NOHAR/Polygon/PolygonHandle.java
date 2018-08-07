/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR.Polygon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.buffer.BufferOp;
import datasets.memory.WordRecord;
import java.util.List;
import org.geotools.geometry.jts.JTS;

/**
 *
 * @author Wesllen Sousa
 */
public class PolygonHandle {
    
    /*
        //Convert histogram to Polygon
        Collections.sort(symbolicView.getBuffer().getBOP());
        Polygon polygon = polygonHandle.convertHistogramToPolygon(symbolicView.getBuffer().getBOP());
    */

    public static Geometry getSmoothedPolygon(com.vividsolutions.jts.geom.Polygon polygon, double fit) {
        return JTS.smooth(polygon, fit);
    }

    public static Geometry getPolygonBuffer(com.vividsolutions.jts.geom.Polygon polygon, int distanceBorder) {
        BufferOp op = new BufferOp(polygon);
        op.setEndCapStyle(BufferOp.CAP_SQUARE);
        op.setQuadrantSegments(4); //ver item 5.3 JTS Developer Guide - Quadrant Approximation
        return op.getResultGeometry(distanceBorder);
    }

    public PolygonInfo newPolygon(Polygon pTest) {
        PolygonInfo polygonInfo = new PolygonInfo();
        polygonInfo.setPolygon(pTest);
        //polygonInfo.setName("" + symbolicView.getBuffer().getAmountPolygons());
        //symbolicView.updateLog("New unknown polygon: " + polygonInfo.getName());
        return polygonInfo;
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
    * 1) Verificar o poligono de maior area
      2) Tirar a diferença do maior para o menor
      3) Se a area da diferenca for menor que 10% da area do maior entao considera igual 
      4) Se não verifica a folga do poligono da classe e retorna para o passo 2
     */
    public int testPolygon(Polygon pModel, Polygon pTest) {
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
    
    /*

     *   Classification
   
    private boolean classify(PolygonInfo polygon) {
        polygon.incrementCountClassified();
        symbolicView.updateLog("Classify polygon: " + polygon.getName() + " class: " + polygon.getClasse());
        if (polygon.getClasse() == label) {
            eval.incrementHists();
            symbolicView.updateLog(">> Acertou");
            return true;
        } else {
            eval.incrementErrors();
            symbolicView.updateLog(">> Errou: " + label);
            return false;
        }
    }

    
     *   Learning

    private void learning(BufferStreaming buffer, Polygon pTest) {
//        Geometry pSmoothed = PolygonLabel.getSmoothedPolygon(pTest, Parameters.SMOOTHED);
        if (buffer.getBufferBOP().isEmpty()) {
            buffer.getPolygonUnknown().add(polygonHandle.newPolygon(pTest));
        } else if (!verififyPolygons(buffer.getPolygonKnown(), pTest)) {
            if (verififyPolygons(buffer.getPolygonNovel(), pTest)) {
                checkNovel(buffer);
            } else if (!checkUnknown(buffer, pTest)) {
                buffer.getPolygonUnknown().add(polygonHandle.newPolygon(pTest));
            }
        }
        checkForget();
    }

    private boolean verififyPolygons(List<PolygonInfo> polygons, Polygon pTest) {
        for (PolygonInfo pModel : polygons) {
            //Verify who are within from each other
            int action = polygonHandle.testPolygon(pModel.getPolygon(), pTest);
            switch (action) {
                case 1:
                    classify(pModel);
                    return true;
                case 2:
                    update(pModel, pTest);
                    return true;
                default:
                    //else Verifica the difference with slack border
                    Geometry pClassBorder = PolygonHandle.getPolygonBuffer(pModel.getPolygon(), Parameters.DISTANCE_BORDER);
                    action = polygonHandle.testPolygon((Polygon) pClassBorder, pTest);
                    if (action > 0) {
                        symbolicView.updateLog("> Concept drift");
                        update(pModel, pTest);
                        return true;
                    }
            }
        }
        return false;
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
            if (polygonHandle.testPolygon(pUnknown.getPolygon(), pTest) > 0) {
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

    */

}
