package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Wesllen Sousa Lima
 */
public class NumeroUtil {

    public static String formataMoeda(Float valor) {
        return NumberFormat.getCurrencyInstance().format(valor);
    }

    public static String formataMoeda(Double valor) {
        return NumberFormat.getCurrencyInstance().format(valor);
    }

    public static String formataDecimal2Casas(Double valor) {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(valor);
    }

    public static String formataDecimal2Casas(Float valor) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(valor);
    }

    public static String formataDecimal1Casas(Float valor) {
        DecimalFormat df = new DecimalFormat("##0.0");
        return df.format(valor);
    }

    //retorna 0 - para igual, -1 - para menor e 1 - para maior
    public static Integer comparaBigDecimalZero(BigDecimal b) {
        int teste = (b.compareTo(BigDecimal.ZERO));
        return teste;

    }
    
}
