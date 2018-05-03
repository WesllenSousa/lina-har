/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR.Polygon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.buffer.BufferOp;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import java.util.ArrayList;
import org.geotools.geometry.jts.JTS;

/**
 *
 * @author Wesllen Sousa
 */
public class PolygonTest {

    public static void main(String[] args) {

        PolygonView p = new PolygonView();
        p.setVisible(true);

        int scala = 15;

        //Criar coordenadas no esparço
        Coordinate[] coords1 = new Coordinate[]{
            new Coordinate(8 * scala, 0 * scala),
            new Coordinate(4 * scala, 4 * scala),
            new Coordinate(8 * scala, 8 * scala),
            new Coordinate(12 * scala, 4 * scala),
            new Coordinate(8 * scala, 0 * scala)};

        Coordinate[] coords2 = new Coordinate[]{
            new Coordinate(8 * scala, 4 * scala),
            new Coordinate(8 * scala, 8 * scala),
            new Coordinate(12 * scala, 8 * scala),
            new Coordinate(12 * scala, 4 * scala),
            new Coordinate(8 * scala, 4 * scala),};

        //Criar coordenadas no esparço
        Coordinate[] coords3 = new Coordinate[]{
            new Coordinate(8 * scala, 0 * scala),
            new Coordinate(4 * scala, 6 * scala),
            new Coordinate(8 * scala, 8 * scala),
            new Coordinate(12 * scala, 4 * scala),
            new Coordinate(8 * scala, 0 * scala)};

        //Desenhar as linhas e criar poligonos por meio de linhas 
        LineString lineString1 = new GeometryFactory().createLineString(coords1);
        LineString lineString2 = new GeometryFactory().createLineString(coords1);

        System.out.println("Polygonizer");
        Polygonizer polygonizer = new Polygonizer();
        polygonizer.add(lineString1);
        System.out.println(polygonizer.getPolygons());
        System.out.println("===============");

        //Criar um polígono: obrigado ser uma forma fechada
        LinearRing linearRing1 = new GeometryFactory().createLinearRing(coords1);
        LinearRing linearRing2 = new GeometryFactory().createLinearRing(coords2);
        LinearRing linearRing3 = new GeometryFactory().createLinearRing(coords3);

        //Criar poligonos
        Polygon polygon1 = new GeometryFactory().createPolygon(linearRing1, null);
        Polygon polygon2 = new GeometryFactory().createPolygon(linearRing2, null);
        Polygon polygon3 = new GeometryFactory().createPolygon(linearRing3, null);

        ArrayList<Geometry> polygons = new ArrayList<>();
        polygons.add(polygon1);
        polygons.add(polygon2);
        polygons.add(polygon3);
        p.drawPolygons(polygons);

        //Analises entre dois poligonos 
        System.out.println("Analises entre dois poligonos ");
        System.out.println(polygon1.disjoint(polygon2));
        System.out.println(polygon1.intersects(polygon2));
        System.out.println(polygon1.touches(polygon2));
        System.out.println(polygon1.crosses(polygon2));
        System.out.println(polygon1.within(polygon2));
        System.out.println(polygon1.contains(polygon2));
        System.out.println(polygon1.overlaps(polygon2));
        System.out.println(polygon1.convexHull());
        System.out.println("===============");

        //Propriedades
        System.out.println("Propriedades");
        System.out.println("getArea: " + polygon1.getArea());
        System.out.println("getCentroid: " + polygon1.getCentroid());
        System.out.println("getNumPoints: " + (polygon1.getNumPoints() - 1));
        System.out.println("isWithinDistance: " + polygon1.isWithinDistance(polygon3, 0));
        System.out.println("===============");

        //relacao entre formas geométricas
        System.out.println("Diferenca e Interseccao");
        System.out.println(polygon1.getBoundary());
        System.out.println(polygon3.getBoundary());

        Geometry gDiffer = polygon1.difference(polygon3);
        Geometry gInter = polygon1.intersection(polygon3);

        System.out.println("polygon1 - getArea: " + polygon1.getArea());
        System.out.println("polygon3 - getArea: " + polygon3.getArea());

        System.out.println("Area da diferenca: getArea - gDiffer: " + gDiffer.getArea());
        System.out.println("Area da interseccao: getArea - gInter: " + gInter.getArea());
        System.out.println("===============");

        //Relacao - pode ser utilizado como uma forma de comparacao: o resultado significa as dimencoes das interceccoes --- labels
        //http://docs.geotools.org/stable/userguide/library/jts/dim9.html
        System.out.println("Relacao");
        System.out.println(polygon1.relate(polygon3));
        System.out.println(polygon3.relate(polygon1));
        System.out.println(polygon1.relate(polygon2));
        System.out.println(polygon1.relate(polygon3).isWithin());
        System.out.println("===============");

        //Buffer (distancia=1) serve para criar uma forma geometrica em torno do poligono
        //no nosso caso pode ser utilizada para o concept drift
        System.out.println("Buffer");
        int distancia = 1;
        BufferOp op = new BufferOp(polygon2);
        op.setEndCapStyle(BufferOp.CAP_ROUND);
        op.setQuadrantSegments(4); //ver item 5 doc
        Geometry gBuffer = op.getResultGeometry(distancia);

        System.out.println(polygon2.getBoundary());
        System.out.println(gBuffer.getBoundary());
        System.out.println("===============");

        //normalizacao e comparacao de duas formas
        //ideia de ser uma percentagem da frequencia
        System.out.println("Normalizacao");
        System.out.println(polygon1.getBoundary());
        polygon1.normalize();
        System.out.println(polygon1.getBoundary());

        System.out.println(polygon3.getBoundary());
        polygon3.normalize();
        System.out.println(polygon3.getBoundary());

        int tolerancia = 1; //a tolerancia é o numero de pontos?
        if (polygon1.equalsExact(polygon3, tolerancia)) {
            System.out.println("Igual");
        } else {
            System.out.println("Diferente");
        }
        System.out.println("===============");

        //Comparacao exata do buffer das formas
        System.out.println("Comparacoes");
        Geometry bp1 = polygon1.buffer(distancia, 4, BufferOp.CAP_SQUARE);
        System.out.println(bp1.getBoundary());
        System.out.println(polygon1.getBoundary());
        Geometry bp3 = polygon3.buffer(distancia, 4, BufferOp.CAP_SQUARE);
        System.out.println(bp3.getBoundary());
        System.out.println(polygon3.getBoundary());

        if (bp1.equalsExact(polygon3, tolerancia)) {
            System.out.println("Igual");
        } else if (bp1.contains(polygon3)) {
            System.out.println("contains");
        } else if (polygon1.within(bp3)) {
            System.out.println("within");
        } else {
            System.out.println("Diferente");
        }
        System.out.println("===============");

        polygons = new ArrayList<>();
        polygons.add(bp1);
        polygons.add(bp3);
        p.drawPolygons(polygons);

        //Smooth
        System.out.println("Smooth");
        Geometry bs1 = JTS.smooth(polygon1, 0.1);
        Geometry bs3 = JTS.smooth(polygon3, 0.1);
        //System.out.println("===============");

        polygons = new ArrayList<>();
        polygons.add(bp1);
        polygons.add(bp3);
        polygons.add(bs1);
        polygons.add(bs3);
        p.drawPolygons(polygons);

    }

}
