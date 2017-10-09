/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.iSAX;

import ex.AlphabetTooLargeException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.seninp.jmotif.sax.datastructure.SAXRecord;
import net.seninp.jmotif.sax.datastructure.SAXRecords;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;

/**
 *
 * @author Wesllen Sousa
 */
public class iSAXExample {

    public static void main(String[] args) {
        try {
            int WINDOW_SIZE = 28;

            //0 = all lines
            TimeSeries timeSeries = TimeSeriesLoader.loadVerticalData("0", "samples/TimeSeries/sinusoid.csv", false);

            iSAX iSAX = new iSAX();
            SAXRecords sAXRecords = iSAX.ts2iSaxViaWindow(timeSeries.getData(), WINDOW_SIZE);

            for (SAXRecord sAXRecord : sAXRecords) {
                System.out.println(sAXRecord.toString());
            }

        } catch (AlphabetTooLargeException ex) {
            Logger.getLogger(iSAX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
