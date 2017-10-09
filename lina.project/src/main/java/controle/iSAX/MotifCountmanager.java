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
package controle.iSAX;

import constants.Parameters;
import isax.Word;
import java.util.HashMap;
import motif.MotifCount;

public class MotifCountmanager extends HashMap<Integer, MotifCount> {

    private static final long serialVersionUID = -2215813024595599443L;

    public MotifCountmanager() {
        for (int i = Parameters.INITIAL_CARDINALITY; i <= Parameters.MAX_CARDINALITY; i *= 2) {
            put(i, new MotifCount());
        }
    }

    public void incCount(int card, Word saxWord) {
        if (get(card) == null) {
            put(card, new MotifCount());
        }
        get(card).incCount(saxWord);
    }

}
