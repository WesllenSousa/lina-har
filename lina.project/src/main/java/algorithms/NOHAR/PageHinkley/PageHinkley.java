/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR.PageHinkley;

/**
 *
 * @author Wesllen Sousa
 */
public class PageHinkley {

    final double PERCENT_ALPHA = 0.25;

    private double UT = 0., lastUT = 0., minUT = Double.MAX_VALUE, PHu = 0.;
    private double LT = 0., lastLT = 0., maxLT = Double.MIN_VALUE, PHl = 0.;
    private double threshold = 0., mean = 0., alpha = 0., sum = 0., maxValueTS = Double.MIN_VALUE;
    private int countChange = 0;

    public void runTs(double[] ts) {
        for (int position = 1; position < ts.length; position++) {
            //System.out.println(ts[position]);
            runStreaming(ts[position], position);
        }
    }

    public void runStreamingWindow(double[] values, int position) {
        for (int i = 0; i < values.length; i++) {
            runStreaming(values[i], i + position);
        }
    }

    public boolean runStreaming(double value, int position) {
        //log(position + " - " + value + "\n");

        if (value > maxValueTS) { //Acumula o maximo valor da timeseries
            maxValueTS = value;
            threshold = maxValueTS;
            alpha = (threshold * PERCENT_ALPHA);
        }

        sum += value;
        countChange++;
        mean = sum / countChange;

        UT = (((position - 1) * lastUT) / position) + (value - mean - alpha);
        if (UT < minUT) {
            minUT = UT;
        }
        LT = (((position - 1) * lastLT) / position) + (value - mean + alpha);
        if (LT > maxLT) {
            maxLT = LT;
        }

        PHu = UT - minUT;
        PHl = maxLT - LT;

        lastUT = UT;
        lastLT = LT;

        if (PHu >= threshold || PHl >= threshold) {
            resetVariables();
//            System.out.println(" >> " + position + " - " + threshold + "\n");
            return true;
        }

        return false;
    }

    private void resetVariables() {
        countChange = 0;
        sum = 0.;

        UT = 0.;
        lastUT = 0.;
        minUT = Double.MAX_VALUE;
        PHu = 0.;
        LT = 0.;
        lastLT = 0.;
        maxLT = Double.MIN_VALUE;
        PHl = 0.;
    }

}
