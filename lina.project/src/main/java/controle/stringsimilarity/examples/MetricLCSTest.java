package controle.stringsimilarity.examples;

import controle.stringsimilarity.MetricLCS;
import controle.stringsimilarity.examples.testutil.NullEmptyTests;
import controle.stringsimilarity.interfaces.NormalizedStringDistance;
import org.junit.Test;

public class MetricLCSTest {

    @Test
    public final void testDistance() {
        MetricLCS instance = new MetricLCS();
        NullEmptyTests.testDistance((NormalizedStringDistance) instance);

        MetricLCS lcs = new MetricLCS();

        String s1 = "ABCDEFG";
        String s2 = "ABCDEFHJKL";
        // LCS: ABCDEF => length = 6
        // longest = s2 => length = 10
        // => 1 - 6/10 = 0.4
        System.out.println(lcs.distance(s1, s2));

        // LCS: ABDF => length = 4
        // longest = ABDEF => length = 5
        // => 1 - 4 / 5 = 0.2
        System.out.println(lcs.distance("ABDEF", "ABDIF"));

        // TODO: regular (non-null/empty) distance tests
    }
}
