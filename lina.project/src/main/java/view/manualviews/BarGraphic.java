package view.manualviews;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Wesllen Sousa
 */
public class BarGraphic extends JPanel implements ChartProgressListener {

    private JFreeChart chart;
    private DefaultCategoryDataset datasetCollection = new DefaultCategoryDataset();

    private boolean autoRange = true;

    public BarGraphic(String title) {
        this.setLayout(new BorderLayout());
        createChart(title);
        final ChartPanel chartPanel = new ChartPanel(chart);
        this.add(chartPanel, BorderLayout.CENTER);
    }

    private void createChart(String title) {
        // set the renderer
        BarRenderer renderer = new BarRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setSeriesPaint(0, new Color(0, 0, 0), true);

        String xaxis = "Words";
        String yaxis = "Counts";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = true;
        boolean toolTips = false;
        boolean urls = false;

        chart = ChartFactory.createBarChart(title, xaxis, yaxis, datasetCollection, orientation, show, toolTips, urls);
        chart.addProgressListener(this);
        chart.setNotify(true);
        //chart.setBackgroundPaint(Color.white);
    }

    @Override
    public void chartProgress(ChartProgressEvent cpe) {

    }

    public void addUpdateData(String palavra, double frequency) {
        datasetCollection.addValue(frequency, palavra, "category");
        repaint();
    }

    public double getWordFrequency(String palavra) {
        double frequency = 0;
        try {
            frequency = (double) datasetCollection.getValue(palavra, "category");
        } catch (UnknownKeyException ex) {
        }
        return ++frequency;
    }

    public void espera(int miliSec) {
        try {
            Thread.sleep(miliSec);
        } catch (InterruptedException ex) {
        }
    }

}
