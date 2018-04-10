// Copyright (c) 2017 - Patrick Schäfer (patrick.schaefer@hu-berlin.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package datasets.timeseries;

public class MultiVariateTimeSeries {

    public TimeSeries[] timeSeries;
    public Double label = -1.0;
    public int numSources = 0;
    public String[] sourcesNames = null;

    public MultiVariateTimeSeries(TimeSeries[] data) {
        this.timeSeries = data;
        this.numSources = data.length;
    }

    public MultiVariateTimeSeries(TimeSeries[] timeSeries, Double label) {
        this.timeSeries = timeSeries;
        this.label = label;
    }

    public MultiVariateTimeSeries(TimeSeries[] data, String[] sourceNames, Double label) {
        this(data, label);
        this.sourcesNames = sourceNames;
        this.numSources = sourceNames.length;
    }

    public int getLength() {
        return this.timeSeries[0].getLength();
    }

    public int getDimensions() {
        return this.timeSeries.length;
    }

    public int getNumSources() {
        return this.timeSeries.length;
    }

    public Double getLabel() {
        return this.label;
    }

    public TimeSeries getTimeSeriesOfOneSource(int idSource) {
        if (idSource >= numSources || idSource < 0) {
            return null;
        } else {
            return new TimeSeries(timeSeries[idSource].data, this.getLabel());
        }
    }

    public void setLabel(Double label) {
        this.label = label;
    }

}
