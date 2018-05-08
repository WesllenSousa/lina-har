
import algorithms.NOHAR.Polygon.PolygonLabel;
import algorithms.NOHAR.Polygon.PolygonView;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wesllen Sousa
 */
public class Test {

    public static void main(String[] args) throws Exception {

        int scala = 20;

        Coordinate[] coords1 = new Coordinate[]{
            new Coordinate(8 * scala, 0 * scala),
            new Coordinate(4 * scala, 4 * scala),
            new Coordinate(8 * scala, 8 * scala),
            new Coordinate(12 * scala, 4 * scala),
            new Coordinate(8 * scala, 0 * scala)};

        Coordinate[] coords2 = new Coordinate[]{
            new Coordinate(10 * scala, 2 * scala),
            new Coordinate(6 * scala, 6 * scala),
            new Coordinate(10 * scala, 10 * scala),
            new Coordinate(14 * scala, 6 * scala),
            new Coordinate(10 * scala, 2 * scala)};

        //Criar um polígono: obrigado ser uma forma fechada
        LinearRing linearRing1 = new GeometryFactory().createLinearRing(coords1);
        LinearRing linearRing2 = new GeometryFactory().createLinearRing(coords2);
        Polygon polygon1 = new GeometryFactory().createPolygon(linearRing1, null);
        Polygon polygon2 = new GeometryFactory().createPolygon(linearRing2, null);

        //Process
        int distance = 1;
        Polygon buffer = (Polygon) PolygonLabel.getPolygonBuffer(polygon1, distance);
        Polygon diffP = (Polygon) buffer.difference(polygon2);

//        System.out.println("intersects: " + polygon1.intersection(polygon2).getArea());
//        System.out.println("intersects: " + polygon2.intersection(polygon1).getArea());
        PolygonView p = new PolygonView();
        ArrayList<Geometry> polygons = new ArrayList<>();
        polygons.add(polygon1);
        polygons.add(polygon2);
//        polygons.add(diffP);
//        polygons.add(polygon1);
//        polygons.add(buffer);
        p.drawPolygons(polygons);
        p.setVisible(true);

    }
}
