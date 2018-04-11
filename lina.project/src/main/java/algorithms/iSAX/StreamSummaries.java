/**
 * COPYRIGHT NOTICE
 *
 *  MrMotif algorithm - (c)Nuno Castro (2010)
 *
 *  This software package is provided "as is", without express or implied warranty.
 *
 *  Published work that uses the code should cite the paper that describes it:
 *
 *  Nuno Castro and Paulo Azevedo, "Multiresolution Motif Discovery in Time Series",
 *  in Proceedings of the Tenth SIAM International Conference on Data Mining (2010).
 *
 *
 *  This software is free to non-comercial use. You are free to modify, extend or distribute this code, as long
 *    as this copyright notice is included whole and unchanged.
 *
 *  Please report bugs and comments to castro@di.uminho.pt .
 *
 *  END COPYRIGHT NOTICE
 */
package algorithms.iSAX;

import controle.constants.Parameters;
import isax.Word;
import java.util.HashMap;
import motif.StreamSummary;

public class StreamSummaries extends HashMap<Integer, StreamSummary> {

    private static final long serialVersionUID = 3343361732300513545L;

    public StreamSummaries() {
        for (int k = Parameters.INITIAL_CARDINALITY; k <= Parameters.MAX_CARDINALITY; k *= 2) {
            initCardinality(k);
        }
    }

    public void initCardinality(int card) {
        put(card, new StreamSummary());
    }

    public void updateBucket(Word word, int card, Integer count) {
        StreamSummary oneStreamSummary = get(card);
        if (oneStreamSummary == null) {
            oneStreamSummary = new StreamSummary();
            put(card, oneStreamSummary);
        }
        oneStreamSummary.update(word, count);
    }

}
