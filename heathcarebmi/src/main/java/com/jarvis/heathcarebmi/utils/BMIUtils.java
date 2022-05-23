package com.jarvis.heathcarebmi.utils;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Calendar;

public class BMIUtils implements Consts {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(6, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double calculateBmiByKgCm(double weight, double height) {
        if (height <= 0) {
            return 0;
        }
        return (weight / height / height) * 10000;
    }

    public static double getBmi(double weight, double height) {
        return BMIUtils.calculateBmiByKgCm(weight, height);
    }

    /**
     * birthday: time in milliseconds
     */
    public static double getBmi(long birthday, int gender, double weight, double height) {
        int ageInMonth = calculateAgeInMonth(birthday);
        if (ageInMonth <= MAX_CHILD_AGE_IN_MONTH) {
            return calculateBmiForChildByAgeMonth(ageInMonth, gender, weight, height);
        }
        return calculateBmiByKgCm(weight, height);
    }

    private static double calculateZScore(double bmi, double m, double s, double l) {
        double zind = (Math.pow(bmi / m, l) - 1) / (s * l);
        if (zind > 3) {
            double sd3Pos = m * Math.pow(1 + l * s * 3, 1 / l);
            double sd23Pos = sd3Pos - m * Math.pow(1 + l * s * 2, 1 / l);
            return 3 + ((bmi - sd3Pos) / sd23Pos);
        }
        if (zind < -3) {
            double sd3Neg = m * Math.pow(1 + l * s * (-3), 1 / l);
            double sd23Neg = m * Math.pow(1 + l * s * (-2), 1 / l) - sd3Neg;
            return -3 + ((bmi - sd3Neg) / sd23Neg);
        }
        return zind;
    }

    private static double calculateBmiForChildByBirthday(long birthdayInMilliSeconds, int gender, double weight, double height) {
        int ageInMonth = calculateAgeInMonth(birthdayInMilliSeconds);
        return calculateBmiForChildByAgeMonth(ageInMonth, gender, weight, height);
    }

    private static double calculateBmiForChildByAgeMonth(int ageInMonth, int gender, double weight, double height) {
        if (gender < 0 || gender > 1) {
            gender = GENDER_MALE;
        }
        ZScore zScore = new ZScore();
        ZScore.LMS lmsByAgeMonth = zScore.getLMSByAgeMonth(ageInMonth, gender);
        if (lmsByAgeMonth == null) {
            return 0;
        }
        double bmi = getBmi(weight, height);
        double zscore = calculateZScore(bmi, lmsByAgeMonth.m, lmsByAgeMonth.s, lmsByAgeMonth.l);
        NormalDistribution normalDistribution = new NormalDistribution(0, 1);
        return normalDistribution.cumulativeProbability(zscore) * 100;
    }

    /**
     * birthday: time in milliseconds
     */
    public static int calculateAgeInMonth(long birthday) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(birthday);

        Calendar today = Calendar.getInstance();

        int monthsBetween = 0;
        int dateDiff = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrrow = today.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (today.get(Calendar.DAY_OF_MONTH) + borrrow) - dob.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        } else {
            monthsBetween++;
        }
        monthsBetween += today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
        monthsBetween += (today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)) * 12;
        return monthsBetween;
    }

}

