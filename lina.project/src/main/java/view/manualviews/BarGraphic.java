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
 * Lina Tool Wesllen Sousa
 */
public class BarGraphic extends JPanel implements ChartProgressListener {

    private JFreeChart chart;
    private DefaultCategoryDataset datasetCollection = new DefaultCategoryDataset();

    private boolean autoRange = true;

    public BarGraphic(String title, boolean showLegend) {
        this.setLayout(new BorderLayout());
        createChart(title, showLegend);
        final ChartPanel chartPanel = new ChartPanel(chart);
        this.add(chartPanel, BorderLayout.CENTER);
    }

    private void createChart(String title, boolean showLegend) {
        // set the renderer
        BarRenderer renderer = new BarRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setSeriesPaint(0, new Color(0, 0, 0), true);

        String xaxis = "Words";
        String yaxis = "Counts";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean toolTips = false;
        boolean urls = false;

        chart = ChartFactory.createBarChart(title, xaxis, yaxis, datasetCollection, orientation, showLegend, toolTips, urls);
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

    public void clearData() {
        datasetCollection.clear();
        repaint();
    }

    public int getWordFrequency(String palavra) {
        Double f = 0.;
        try {
            f = (Double) datasetCollection.getValue(palavra, "category");
        } catch (UnknownKeyException ex) {
        }
        int frequency = f.intValue();
        return ++frequency;
    }

    public void espera(int miliSec) {
        try {
            Thread.sleep(miliSec);
        } catch (InterruptedException ex) {
        }
    }

    public DefaultCategoryDataset getDatasetCollection() {
        return datasetCollection;
    }

}
