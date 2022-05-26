package Controller;

import java.io.Serializable;
import java.time.chrono.ChronoLocalDate;

public class Auxiliary implements Serializable {

    public static Object[] trim(Object[] a, int i, int n) {
        if (n >= a.length)
            n = a.length-1;
        Object[] result = new Object[n+1-i];
        for (int j=0; i < n;j++,i++) {
            result[0] = a[i];
        }
        return result;
    }

    public static boolean PT(String number) {
        final int max=9;
        //check if is numeric and has 9 numbers
        if (!number.matches("\\d+") || number.length()!=max) return false;
        int checkSum=0;
        //calculate checkSum
        for (int i=0; i<max-1; i++){
            checkSum+=(number.charAt(i)-'0')*(max-i);
        }
        int checkDigit=11-(checkSum % 11);
        //if checkDigit is higher than 9 set it to zero
        if (checkDigit>9) checkDigit=0;
        //compare checkDigit with the last number of NIF
        return checkDigit==number.charAt(max-1)-'0';
    }

    public static boolean between(Number min,Number r,Number max) {
        return r.doubleValue() >= min.doubleValue() && r.doubleValue() <= max.doubleValue();
    }

    public static boolean between(Number min,String r,Number max) {
        try {
            Double.parseDouble(r);
        } catch(Exception e) {return false;}
        return Double.parseDouble(r) >= min.doubleValue() && Double.parseDouble(r) <= max.doubleValue();
    }

    public static boolean betweenDay(ChronoLocalDate min, ChronoLocalDate r, ChronoLocalDate max) {
        return (r.isAfter(min) && r.isBefore(max) || r.equals(min)) && (max.equals(min) || max.isAfter(min));
    }

}
