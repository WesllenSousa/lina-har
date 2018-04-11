package algorithms.ADWIN;

import controle.constants.ConstDataset;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;

public class AdwinTest {
    
    public static void main(String[] args) {

        //0 = all lines 
        //sinusoid, 215218, sinusoidLacunas
        TimeSeries[] timeSeries = TimeSeriesLoader.loadVerticalData("0", ConstDataset.DS_STREAM + "215218.csv", false, ",");
        
        ADWIN adwin = new ADWIN(.01); // Init Adwin with delta=.01
        for (int s = 0; s < timeSeries.length; s++) {
            for (int i = 0; i < timeSeries[s].getLength(); i++) {
                //System.out.println(timeSeries[s].getData(i));
                if (adwin.setInput(timeSeries[s].getData(i))) //Input data into Adwin
                {
                    System.out.println("---------- Change Detected: " + i);
                }
            }
        }
        //Get information from Adwin
        System.out.println("Mean:" + adwin.getEstimation());
        System.out.println("Variance:" + adwin.getVariance());
        System.out.println("Stand. dev:" + Math.sqrt(adwin.getVariance()));
        System.out.println("Width:" + adwin.getWidth());
    }
    
}
