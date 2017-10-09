package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wesllen Sousa
 */
public class DateUtil {

    public static String dateToTimestampSec(Date date) {
        String s = date.getTime() + "";
        return s.substring(0, 10);
    }

    public static String timeInterval(Long inicio, Long fim) {
        HashMap<Integer, String> intervalo = new HashMap<>();
        Long interval = fim - inicio;
        if (interval >= 1000) {
            Float segundo = interval / 1000f;
            if (segundo >= 60) {
                Float minuto = segundo / 60;
                return minuto.intValue() + " minutos";
            } else {
                return segundo.intValue() + " segundos";
            }
        } else {
            return interval.intValue() + " milisegundos";
        }
    }

    public static Date timestampToDate(long timestamp) {
        return new Date(timestamp * 1000);
    }

    public static Float millisecondIntervalToSecond(Long inicio, Long fim) {
        return (fim - inicio) / 1000f;
    }

    public static Integer millisecondIntervalToMinute(Long inicio, Long fim) {
        Float segundo = (fim - inicio) / 1000f;
        return (int) (segundo % 3600) / 60;
    }

    public static Calendar adicionaDias(Calendar data, Integer dias) {
        Date d = data.getTime();
        d.setTime(d.getTime() + dias * 1000 * 60 * 60 * 24);
        data.setTime(d);
        return data;
    }

    public static Date adicionaMilisegundos(Date data, Integer milisegundos) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.MILLISECOND, milisegundos);
        return calendar.getTime();
    }

    public static Date adicionaSegundos(Date data, Integer segundos) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.SECOND, segundos);
        return calendar.getTime();
    }

    public static Date adicionaMinutos(Date data, Integer minutos) {
        data.setTime(data.getTime() + minutos * 1000 * 60);
        return data;
    }

    public static Date adicionaHoras(Date data, Integer horas) {
        data.setTime(data.getTime() + horas * 1000 * 60 * 60);
        return data;
    }

    public static Date adicionaDias(Date data, Integer dias) {
        data.setTime(data.getTime() + dias * 1000 * 60 * 60 * 24);
        return data;
    }

    public static Date diminuirDias(Date data, Integer dias) {
        Calendar calendarData = Calendar.getInstance();
        calendarData.setTime(data);
        int numeroDiasParaSubtrair = dias * -1;
        calendarData.add(Calendar.DATE, numeroDiasParaSubtrair);
        return calendarData.getTime();
    }

    public static Calendar dateParaCalendar(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        return cal;
    }

    public static int quantidadeDiasEntreDuasDatas(Date inicial, Date finall) {
        int diffDays = (int) ((inicial.getTime() - finall.getTime()) / (24 * 60 * 60 * 1000));  // 7
        return diffDays;
    }

    public static int quantidadeSegundosEntreDuasDatas(Date inicial, Date finall) {
        int diffDays = (int) ((inicial.getTime() - finall.getTime()) / (60 * 1000));  // 7
        return diffDays;
    }

    public static String quantidadeHoraMinutoSegundoEntreDuasDatas(Calendar inicial, Calendar finall) {
        int difMili = (int) ((finall.getTimeInMillis() - inicial.getTimeInMillis()) / 1000);
        int diffHor = difMili / 3600;
        int diffMin = difMili % 3600 / 60;
        int diffSeg = (difMili % 3600) % 60;
        return diffHor + ":" + diffMin + ":" + diffSeg;
    }

    public static String getDataDia() {
        Date date = new Date();
        DateFormat formatData = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return formatData.format(date);
    }

    public static String getHoraDia() {
        Date date = new Date();
        DateFormat formatData = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        return formatData.format(date);
    }

    public static Boolean verificarDataMaiorDataAtual(Calendar data) {
        Date date = new Date();
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(date);
        long dataHoje = hoje.getTimeInMillis();
        long dataTeste = data.getTimeInMillis();
        return dataTeste > dataHoje;
    }

    public static Boolean verificarDataMaior(Calendar data1, Calendar data2) {
        long _data1 = data1.getTimeInMillis();
        long _data2 = data2.getTimeInMillis();
        return _data1 > _data2;
    }

    public static Boolean verificarDataMaior(Date data1, Date data2) {
        long _data1 = data1.getTime();
        long _data2 = data2.getTime();
        return _data1 > _data2;
    }

    public static Calendar preparaDataInicio(Calendar data) {
        if (data != null) {
            data.set(Calendar.HOUR_OF_DAY, 00);
            data.set(Calendar.MINUTE, 00);
            data.set(Calendar.SECOND, 00);
            return data;
        }
        return null;
    }

    public static Calendar preparaDataFinal(Calendar data) {
        if (data != null) {
            data.set(Calendar.HOUR_OF_DAY, 23);
            data.set(Calendar.MINUTE, 59);
            data.set(Calendar.SECOND, 59);
            return data;
        }
        return null;
    }

    public static Calendar setaHoraCalendar(Calendar data, Integer h, Integer m, Integer s) {
        if (data != null) {
            data.set(Calendar.HOUR_OF_DAY, h);
            data.set(Calendar.MINUTE, m);
            data.set(Calendar.SECOND, s);
            return data;
        }
        return null;
    }

    /*
     Formatos
     */
    public static Date parseDateUTC(String date) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            return isoFormat.parse(date);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Date parseDateDDmmYYYYhhMMss(String date) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            return isoFormat.parse(date);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String getFormattedData(Calendar data) {
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
        String retorno = sp.format(data.getTime()) + " at ";
        sp.applyPattern("hh : mm");
        return retorno + sp.format(data.getTime());
    }

    public static Date convertToDate(String data) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        try {
            return dataFormat.parse(data);
        } catch (ParseException ex) {
        }
        return null;
    }

    public static Calendar convertToCalendar(String data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(data));
            return cal;
        } catch (ParseException e) {
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public static String formataData_yyyyMMdd(Date data) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dataFormat.format(data);
    }

    public static String formataData_yyyyMMddHHmmSS(Date data) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dataFormat.format(data);
    }

    public static String formata_HHmm(Date data) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm");
        return dataFormat.format(data);
    }

    public static String formataData_ddMMyyy(Date data) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dataFormat.format(data.getTime());
    }

    public static String formataData_ddMMyyyHHmmSS(Date data) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        return (dataFormat.format(data.getTime()));
    }

    public static String formataData_ss(Date data) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("ss");
        return (dataFormat.format(data.getTime()));
    }

}
