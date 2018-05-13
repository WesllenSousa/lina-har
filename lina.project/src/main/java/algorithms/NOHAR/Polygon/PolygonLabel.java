/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR.Polygon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.operation.buffer.BufferOp;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import org.geotools.geometry.jts.JTS;

/**
 *
 * @author Wesllen Sousa
 */
public class PolygonLabel {

    private Integer width = 0, heigth = 0;

    private BufferedImage getBufferImage() {
        BufferedImage surface = new BufferedImage(width, heigth, BufferedImage.OPAQUE);
        Graphics g = surface.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, heigth);
        return surface;
    }

    private JLabel getLabel(BufferedImage surface, String classe) {
        JLabel label = new JLabel(classe, new ImageIcon(surface), SwingConstants.LEFT);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        label.setBorder(border);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JLabel drawBuffer(Graphics g, BufferedImage surface, String classe) {
        if (g != null) {
            g.drawImage(surface, 0, 0, null);
            g.dispose();
            return getLabel(surface, classe);
        } else {
            System.out.println("Erro drawBuffer");
            return null;
        }
    }

    public JLabel drawPolygons(ArrayList<Geometry> geometries, String classe, int scala) {
        width = 0;
        heigth = 0;

        ArrayList<Polygon> polygons = new ArrayList<>();
        for (Geometry geometry : geometries) {
            calcDimension(geometry, scala);
            Polygon p = new Polygon();
            for (Coordinate coord : geometry.getCoordinates()) {
                p.addPoint(Math.round((float) coord.x) * scala, Math.round((float) coord.y) * scala);
            }
            polygons.add(p);
        }

        BufferedImage surface = getBufferImage();
        Graphics g = surface.getGraphics();
        for (Polygon p : polygons) {
            g.setColor(randomColor());
            g.drawPolygon(p);
        }
        return drawBuffer(g, surface, classe);
    }

    public JLabel drawPolygon(Geometry geometry, int scala) {
        width = 0;
        heigth = 0;
        calcDimension(geometry, scala);

        Polygon polygon = new Polygon();
        String tip = geometry.getBoundary().toText();
        for (Coordinate coord : geometry.getCoordinates()) {
            polygon.addPoint(Math.round((float) coord.x) * scala, Math.round((float) coord.y) * scala);
        }

        BufferedImage surface = getBufferImage();
        Graphics g = surface.getGraphics();
        g.setColor(randomColor());
        g.drawPolygon(polygon);
        return drawBuffer(g, surface, tip);
    }

    private Color randomColor() {
        Random random = new Random();
        int r = random.nextInt(100);
        int g = random.nextInt(100);
        int b = random.nextInt(100);
        return new Color(r, g, b);
    }

    public static Geometry getSmoothedPolygon(com.vividsolutions.jts.geom.Polygon polygon, double fit) {
        return JTS.smooth(polygon, fit);
    }

    public static Geometry getPolygonBuffer(com.vividsolutions.jts.geom.Polygon polygon, int distanceBorder) {
        BufferOp op = new BufferOp(polygon);
        op.setEndCapStyle(BufferOp.CAP_SQUARE);
        op.setQuadrantSegments(4); //ver item 5 doc
        return op.getResultGeometry(distanceBorder);
    }

    private void calcDimension(Geometry geometry, int scala) {
        if (geometry.getNumPoints() > width) {
            width = geometry.getNumPoints() * scala;
        }
        heigth = maxValueHeight(geometry) * scala;
    }

    private int maxValueHeight(Geometry geometry) {
        double majorValue = 0;
        for (int i = 0; i < geometry.getCoordinates().length; i++) {
            if (geometry.getCoordinates()[i].y > majorValue) {
                majorValue = geometry.getCoordinates()[i].y;
            }
        }
        return Math.round((float) majorValue + 2);
    }

}
