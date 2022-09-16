package com.well.unitlibrary;


import android.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.TreeMap;

public class UnitConverter {
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private static final float KM_MILE_CONST = 0.621371192f;
    private static final float CM_IN_CONST = 0.39370f;
    private static final float PACE_CONST = 16.6666667f;// 1 m/s = 16.6666667 minutes/km hoac 1 min/km = 16.666666666667 mps(met per second)
    private static final float FEET_CONST = 3.28084f;// 1 m = 3.28084 feet or 1/0.3048
    private static final float FEET_TO_MILES_CONST = 1 / 5280f;// 1 m = 3.28084 feet or 1/0.3048
    private static final float FOOT_TO_INCH = 12;// 1 foot = 12 inch

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "m");
        suffixes.put(1_000_000_000L, "b");
        suffixes.put(1_000_000_000_000L, "t");
        suffixes.put(1_000_000_000_000_000L, "q");
        suffixes.put(1_000_000_000_000_000_000L, "u");
    }

    public static float formatDoubleToFloat(Double value, int digit) {
        return new BigDecimal(value.toString()).setScale(digit, RoundingMode.HALF_UP).floatValue();
    }

    public static double convertSecondsToHours(int durationInSeconds) {
        return formatDouble(durationInSeconds / 3600.0, 4);
    }

    public static String formatLong(int value) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(value);
    }

    public static String formatLong(long value) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(value);
    }

    public static String formatDoubleToString(Double value, int digit) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(digit);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);

        return numberFormat.format(value);
    }

    public static String formatDoubleToStringDown(Double value, int digit) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(digit);
        numberFormat.setRoundingMode(RoundingMode.HALF_DOWN);
        return numberFormat.format(value);
    }

    public static double formatDouble(double toDouble, int digit) {
        return new BigDecimal(toDouble + "").setScale(digit, RoundingMode.HALF_UP).doubleValue();
    }

    public static double formatFloat(Float value, int digit) {
        return new BigDecimal(value.toString()).setScale(digit, RoundingMode.HALF_UP).doubleValue();
    }

    public static double calculateDistanceTravelledInKM(long stepsTaken, double entityStrideLength) {
        return formatDouble((stepsTaken * entityStrideLength) / 1000, 2);
    }

    public static double calculateDistanceTravelledInMet(long steps, double distanceMetStep) {
        return formatDouble(steps * distanceMetStep, 2);
    }

    public static String convertDecimalToString(Object value) {
        DecimalFormat df = new DecimalFormat("#,##0");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.getDefault()));
        return df.format(value);
    }

    public static double convertHourToMin(double hour) {
        return hour * 60;
    }

    public static double convertMetToKmOrMile(double met, boolean isKm) {
        double km = met / 1000;
        return isKm ? km : km * KM_MILE_CONST;
    }

    public static double convertSpeedMetPerSecToKmPerHour(double speedMetPerSec) {
        return speedMetPerSec * 3.6;
    }

    public static double convertCmToCmOrInch(double cm, boolean isKm) {
        return isKm ? cm : cm * CM_IN_CONST;
    }

    public static double convertPaceMinPerKmToMinPerMile(double paceMinPerKm) {
        return paceMinPerKm / KM_MILE_CONST;
    }

    public static double convertPaceMinPerMileToMinPerKm(double paceMinPerMile) {
        return paceMinPerMile * KM_MILE_CONST;
    }

    public static double convertSpeedKmPerHourToMilePerHour(double speedKmPerHour) {
        return speedKmPerHour * KM_MILE_CONST;
    }

    public static double convertSpeedKmPerHourToMetPerSec(double speedKmPerHour) {
        return speedKmPerHour / 3.6;
    }

    public static double convertSpeedMilePerHourToKmPerHour(double speedMilePerHour) {
        return speedMilePerHour / KM_MILE_CONST;
    }

    public static double convertSpeedMetPSToSpeedKmOrMi(double speedMetPerSec, boolean isKm) {
        if (speedMetPerSec <= 0.01) {
            return 0;
        }
        double speedKmH = speedMetPerSec * 3.6;
        return isKm ? speedKmH : speedKmH * KM_MILE_CONST;
    }

    public static double convertSpeedMetPSToPace(double speedMetPerSec, boolean isKm) {
        if (speedMetPerSec <= 0.01) {
            return 0;
        }
        double paceMinPerKm = PACE_CONST / speedMetPerSec;
        return isKm ? paceMinPerKm : paceMinPerKm / KM_MILE_CONST;//Conversion base : 1 min/km = 1.609344 min/mile
    }

    public static double convertSpeedMetPSToPace(double speedMetPerSec) {
        if (speedMetPerSec <= 0.01) {
            return 0;
        }
        return PACE_CONST / speedMetPerSec;
    }

    public static String convertSpeedMSToPaceOrSpeedString(double speedMs, boolean isCycling, boolean isKm) {
        String paceOrSpeedStr;
        double speedOrPace = UnitConverter.formatDouble(convertSpeedMsToSpeedKmhOrPace(speedMs, isCycling, isKm), 1);
        if (isCycling) {
            paceOrSpeedStr = formatDoubleToString(speedOrPace, 1);
        } else {
            // dinh dang 5:3 min/km or 5:3 min/mi
            int minute = (int) speedOrPace;
            int second = (int) ((speedOrPace - minute) * 60);
            String secondStr = String.valueOf(second);
            String minuteStr = String.valueOf(minute);
            if (second < 10) {
                secondStr = "0" + secondStr;
            }
            if (minute < 10) {
                minuteStr = "0" + minute;
            }
            paceOrSpeedStr = minuteStr + ":" + secondStr;
        }

        return paceOrSpeedStr;
    }

    public static String convertSpeedMSToPaceOrSpeedStrToSpeak(double speedMs, boolean isCycling, boolean isKm) {
        String paceOrSpeedStr;
        double speedOrPace = UnitConverter.formatDouble(convertSpeedMsToSpeedKmhOrPace(speedMs, isCycling, isKm), 1);
        if (isCycling) {
            paceOrSpeedStr = formatDoubleToString(speedOrPace, 1);
        } else {
            // dinh dang 5:3 min/km or 5:3 min/mi
            int minute = (int) speedOrPace;
            String minuteStr = String.valueOf(minute);
            int second = (int) ((speedOrPace - minute) * 60);
            if (second == 0) {
                return minuteStr;
            }
            String secondStr = String.valueOf(second);
            if (second < 10) {
                secondStr = "0" + secondStr;
            }
            paceOrSpeedStr = minuteStr + ":" + secondStr;
        }

        return paceOrSpeedStr;
    }

    public static double convertSpeedMsToSpeedKmhOrPace(double speedMetPerSec, boolean isCycling, boolean isKm) {
        if (speedMetPerSec <= 0.01) {
            return 0;
        }

        if (isCycling) {// neu la dap xe thi tra ra la speed
            return convertSpeedMetPSToSpeedKmOrMi(speedMetPerSec, isKm);//km/h
        }

        // di chay bo se la pace
        return convertSpeedMetPSToPace(speedMetPerSec, isKm);
    }

    public static double convertPaceMinPerKmToSpeedKmPerHour(double paceMinPerKm) {
        if (paceMinPerKm <= 0) {
            return 0;
        }
        return 60 / paceMinPerKm;
    }

    public static double convertPaceMinPerKmToSpeedMeterPerSecond(double paceMinPerKm) {
        if (paceMinPerKm <= 0) {
            return 0;
        }
        return 50 / (3 * paceMinPerKm);
    }

    public static float convertKilometersToMiles(float km) {
        return km * KM_MILE_CONST;
    }

    public static double convertKilometersToMeters(double km) {
        return km * 1000;
    }

    public static double convertMetToMiles(double met) {
        double km = met / 1000;
        return km * KM_MILE_CONST;
    }

    public static double convertMileToMeter(double mile) {
        return mile * 1609.34d;
    }

    public static double convertFeetToInch(double foot) {
        return foot * FOOT_TO_INCH;
    }

    public static double convertInchToFeet(double inch) {
        return inch / FOOT_TO_INCH;
    }

    public static double convertKgToLbs(double kg) {
        return kg * 2.2046226218;
    }

    public static double convertKgToLbsIfNeed(double kg, boolean isMetricUnit) {
        if (isMetricUnit) {
            return kg;
        } else {
            return convertKgToLbs(kg);
        }
    }

    public static double convertLbsToKg(double lbs) {
        return lbs / 2.2046226218;
    }

    public static double convertCmToInch(double cm) {
        return cm * 0.393700787;
    }

    public static double convertCmToFt(double cm) {
        return cm / 30.48f;
    }

    public static String convertCmToFtStringIfNeed(double cm, boolean isKmSetting) {
        if (isKmSetting) {
            return UnitConverter.formatDoubleToString(cm, 0);
        } else {
            return formatInchToFeetString(convertCmToInch(cm));
        }
    }

    public static double convertFtToCm(int ft) {
        return ft * 30.48f;
    }

    public static double convertInchToCm(double inch) {
        return inch / 0.393700787;
    }

    public static Pair<Integer, Integer> convertInchToFeetPair(double inchRawValue) {
        double valueFt = convertInchToFeet(Math.round(inchRawValue));
        int feet = (int) valueFt;
        int inch = (int) Math.round(UnitConverter.convertFeetToInch(valueFt - feet));
        return new Pair<>(feet, inch);
    }

    public static double convertMetToFeet(double met, boolean isKm) {
        return isKm ? met : met * FEET_CONST;
    }

    public static double convertMileToKm(double mile) {
        return mile / KM_MILE_CONST;
    }

    public static String formatDoubleToString(double value) {
        DecimalFormat df = new DecimalFormat("#,##0.#");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.getDefault()));
        return df.format(value);
    }

    public static String formatInchToFeetString(double currentValue) {
        Pair<Integer, Integer> feetPair = convertInchToFeetPair(currentValue);
        return feetPair.first + "\'" + feetPair.second + "\"";
    }

    public static String formatCmToFeetStringEmperic(double cm) {
        double valueInch = convertCmToInch(cm);
        return formatInchToFeetString(valueInch);
    }

    public static double convertTemp(double tempValue, boolean isCelsius) {
        if (isCelsius) {
            return tempValue;
        }
        return (tempValue * 9 / 5) + 32;
    }

    public static double convertFeetToMile(double feet) {
        return feet * FEET_TO_MILES_CONST;
    }

    public static double convertKilometerOrMileToMeter(double value, boolean isKm) {
        if (isKm) {
            return convertKilometersToMeters(value);
        }
        return convertMileToMeter(value);
    }

    public static double convertCmOrInchToCm(double value, boolean isKm) {
        if (isKm) {
            return value;
        }
        return convertInchToCm(value);
    }

}

