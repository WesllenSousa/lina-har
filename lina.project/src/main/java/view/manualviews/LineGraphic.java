package view.manualviews;

import datasets.generic.GenericRowBean;
import java.awt.BorderLayout;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

/**
 * Wesllen Sousa
 */
public class LineGraphic extends JPanel implements ChartProgressListener {
    
    private JFreeChart chart;
    private XYPlot timeseriesPlot;
    private final XYSeriesCollection seriesCollection = new XYSeriesCollection();
    
    private boolean autoRange = true;
    
    public LineGraphic(String title) {
        this.setLayout(new BorderLayout());
        createChart(title);
        final ChartPanel chartPanel = new ChartPanel(chart);
        this.add(chartPanel, BorderLayout.CENTER);
    }
    
    private void createChart(String title) {
        // set the renderer
        XYLineAndShapeRenderer xyRenderer = new XYLineAndShapeRenderer(true, false);
        xyRenderer.setSeriesPaint(0, new Color(0, 0, 0));
        xyRenderer.setBaseStroke(new BasicStroke(3));

        // X - the time axis
        NumberAxis timeAxis = new NumberAxis("Item. (zoom: select with mouse; panning: Ctrl+mouse)");

        // Y axis
        NumberAxis valueAxis = new NumberAxis("Values");
        valueAxis.setAutoRangeIncludesZero(false);
        
        timeseriesPlot = new XYPlot(seriesCollection, timeAxis, valueAxis, xyRenderer);
        timeseriesPlot.setDomainPannable(true);
        timeseriesPlot.setRangePannable(true);
        timeseriesPlot.setDomainGridlinesVisible(false); 
        
        chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, timeseriesPlot, false);
        chart.addProgressListener(this);
        chart.setNotify(true);
    }
    
    public void prepareStream(LinkedHashSet<GenericRowBean> data, boolean autoRange) {
        this.autoRange = autoRange;
        if (!data.isEmpty()) {
            LinkedList<XYSeries> timeSeries = new LinkedList<>();
            //Cria timeseries vazias
            for (GenericRowBean bean : data) {
                for (String column : bean.getTupla()) {
                    timeSeries.add(new XYSeries(column));
                }
                break;
            }
            //Adiciona a time series a coleção de time series 
            seriesCollection.removeAllSeries();
            for (XYSeries time : timeSeries) {
                seriesCollection.addSeries(time);
            }
            fitAxis();
        }
    }
    
    private void fitAxis() {
        ValueAxis valueAxis = timeseriesPlot.getDomainAxis();
        valueAxis.setAutoRange(autoRange);
    }
    
    @Override
    public void chartProgress(ChartProgressEvent cpe) {
        
    }
    
    public void addMarker(int startVal, int endVal, Color color) {
        IntervalMarker marker = new IntervalMarker(startVal, endVal);
        marker.setLabelOffsetType(LengthAdjustmentType.EXPAND);
        marker.setPaint(new Color(134, 254, 225));
        marker.setAlpha((float) 0.60);
        marker.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        marker.setLabelPaint(Color.green);
        marker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        marker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        
        timeseriesPlot.addDomainMarker(marker, Layer.BACKGROUND);
        
        ValueMarker markStart = new ValueMarker(startVal, color, new BasicStroke(2.0f));
        ValueMarker markEnd = new ValueMarker(endVal, color, new BasicStroke(2.0f));
        timeseriesPlot.addDomainMarker(markStart, Layer.BACKGROUND);
        timeseriesPlot.addDomainMarker(markEnd, Layer.BACKGROUND);
    }
    
    public void addData(GenericRowBean bean) {
        for (int i = 0; i < seriesCollection.getSeriesCount(); i++) {
            XYSeries timeSerie = (XYSeries) seriesCollection.getSeries().get(i);
            int nextItem = timeSerie.getItemCount() + 1;
            String value = bean.getTupla().get(i);
            timeSerie.add(nextItem, Double.parseDouble(value));

            //addMarker(nextItem, nextItem, Color.CYAN);
        }
    }
    
    public void espera(int miliSec) {
        try {
            Thread.sleep(miliSec);
        } catch (InterruptedException ex) {
        }
    }

    public XYPlot getTimeseriesPlot() {
        return timeseriesPlot;
    }
      
}
