/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.SAX;

import net.seninp.jmotif.sax.SAXException;
import net.seninp.jmotif.sax.SAXProcessor;
import net.seninp.jmotif.sax.TSProcessor;
import net.seninp.jmotif.sax.alphabet.Alphabet;
import net.seninp.jmotif.sax.alphabet.NormalAlphabet;
import net.seninp.jmotif.sax.datastructure.SAXRecords;
import net.seninp.jmotif.sax.parallel.ParallelSAXImplementation;
import util.Messages;

/**
 *
 * @author Wesllen Sousa
 */
public class SAX {
    
    private final Params params;
    
    public SAX(Params params) {
        this.params = params;
    }

    public String serieToWord(double[] ts) {
        try {
            TSProcessor tp = new TSProcessor();
            // Z normalize it
            double[] tsZNorm = tp.znorm(ts, params.nThreshold);
            // perform PAA conversion if needed
            double[] paa = tp.paa(tsZNorm, params.paaSize);
            // Convert the PAA to a string.
            Alphabet na = new NormalAlphabet();
            char[] word = tp.ts2String(paa, na.getCuts(params.alphabetSize));
            return String.valueOf(word);
        } catch (SAXException ex) {
            Messages msg = new Messages();
            msg.bug("SAX - serieToWord: " + ex.toString());
        }
        return null;
    }

    public SAXRecords slideWindow(double[] ts) {
        SAXProcessor sp = new SAXProcessor();
        SAXRecords sAXRecords = new SAXRecords();
        try {
            NormalAlphabet na = new NormalAlphabet();
            sAXRecords = sp.ts2saxViaWindow(ts, params.windowSize,
                    params.paaSize, na.getCuts(params.alphabetSize),
                    params.nrStartegy, params.nThreshold);
            return sAXRecords;
        } catch (SAXException ex) {
            Messages msg = new Messages();
            msg.bug("SAX - slideWindow: " + ex.toString());
        }
        return new SAXRecords();
    }

    public SAXRecords slideWindowParallel(double[] ts) {
        try {
            ParallelSAXImplementation ps = new ParallelSAXImplementation();
            SAXRecords parallelRes = ps.process(ts, 2, params.windowSize, params.paaSize,
                    params.alphabetSize, params.nrStartegy, params.nThreshold);
            return parallelRes;
        } catch (SAXException ex) {
            Messages msg = new Messages();
            msg.bug("ParallelSAX - slideWindowParallel: " + ex.toString());
        }
        return new SAXRecords();
    }

}
