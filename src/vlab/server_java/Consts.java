package vlab.server_java;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Consts
{
    public static final double initialMatrixMinElement = 0;
    public static final double initialMatrixMaxElement = 1;
    public static final double compositionMatrixSizePoints = 0.1;
    public static final double compositionMatrixPoints = 0.5;
    public static final double tranzitionMatrixPoints = 0.4;

    public static double roundDoubleToNDecimals(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}