package com.jarvis.heathcarebmi.utils;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class HealthIndexUtils {

    public static String roundBmiValue(double bmi) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);

        return numberFormat.format(roundBmiValueToFloat(bmi));
    }

    public static String roundBmiValueChild(double bmi) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setRoundingMode(RoundingMode.DOWN);

        return numberFormat.format(roundBmiValueToFloat(bmi));
    }

    public static float roundBmiValueToFloat(double bmi) {
        float value = (float) ((int)(bmi * 10) / 10.0);
        return value;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(6, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    //Tinh BMI
    public static double calculateBmiByKgCm(double weight, double height) {
        if (height <= 0) {
            return 0;
        }
        return (weight / height / height) * 10000;
    }

    //Tinh weight từ BMI và chiều cao
    private static double calculateWeightFromBmi(double bmi, double height) {
        if (bmi <= 0) {
            return 0;
        }
        return (bmi * Math.pow(height, 2)) / 10000;
    }

    //Tinh BMI
    public static double getBmi(double weight, double height) {
        return HealthIndexUtils.calculateBmiByKgCm(weight, height);
    }

    /**
     * birthday: time in milliseconds
     */
    public static double getBmi(long birthday, int gender, double weight, double height) {
        int ageInMonth = calculateAgeInMonth(birthday);
        if (isChild(ageInMonth)) {
            return calculateBmiForChildByAgeMonth(ageInMonth, gender, weight, height);
        }
        return calculateBmiByKgCm(weight, height);
    }

    public static double getBmi(long birthday, long selectedDay, int gender, double weight, double height) {
        int ageInMonth = calculateAgeInMonth(birthday, selectedDay);
        if (isChild(ageInMonth)) {
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
            gender = Consts.GENDER_MALE;
        }
        ZScore zScore = new ZScore();
        ZScore.LMS lmsByAgeMonth = zScore.getLMSByAgeMonth(ageInMonth, gender);
        if (lmsByAgeMonth == null) {
            return 0;
        }
        double bmi = getBmi(weight, height);
        double zscore = calculateZScore(bmi, lmsByAgeMonth.m, lmsByAgeMonth.s, lmsByAgeMonth.l);
        NormalDistribution normalDistribution = new NormalDistribution(0, 1);

        if(normalDistribution.cumulativeProbability(zscore) * 100 >= 99){
            return 99.0;
        }else {
            return normalDistribution.cumulativeProbability(zscore) * 100;
        }
    }

    /**
     * Tính Healthy Weight cho trẻ em
     * return Pair với first = min weight, second = max weight
     */
    private static HealthyWeight calculateHealthyWeightForChild(int ageInMonth, int gender, double height) {
        if (gender < 0 || gender > 1) {
            gender = Consts.GENDER_MALE;
        }
        ZScore zScore = new ZScore();
        ZScore.LMS lmsByAgeMonth = zScore.getLMSByAgeMonth(ageInMonth, gender);
        if (lmsByAgeMonth == null) {
            return new HealthyWeight(0.0, 0.0);
        }
        double minPercentile = 15.0;
        double maxPercentile = 85.0;
        NormalDistribution normalDistribution = new NormalDistribution(0, 1);
        double minZscore = normalDistribution.inverseCumulativeProbability(minPercentile / 100f);
        double maxZscore = normalDistribution.inverseCumulativeProbability(maxPercentile / 100f);
        double minBmi = calculateBmiFromZScore(minZscore, lmsByAgeMonth.m, lmsByAgeMonth.s, lmsByAgeMonth.l);
        double maxBmi = calculateBmiFromZScore(maxZscore, lmsByAgeMonth.m, lmsByAgeMonth.s, lmsByAgeMonth.l);
        double minWeight = calculateWeightFromBmi(minBmi, height);
        double maxWeight = calculateWeightFromBmi(maxBmi, height);
        return new HealthyWeight(minWeight, maxWeight);
    }

    private static double calculateBmiFromZScore(double zscore, double m, double s, double l) {
        double bmi = m * Math.pow((zscore * s * l + 1), 1 / l);

        if (zscore > 3) {
            double sd3Pos = m * Math.pow(1 + l * s * 3, 1 / l);
            double sd23Pos = sd3Pos - m * Math.pow(1 + l * s * 2, 1 / l);
            return sd23Pos * (zscore - 3) + sd3Pos;
        }
        if (zscore < -3) {
            double sd3Neg = m * Math.pow(1 + l * s * (-3), 1 / l);
            double sd23Neg = m * Math.pow(1 + l * s * (-2), 1 / l) - sd3Neg;
            return sd3Neg + sd23Neg * (zscore + 3);
        }
        return bmi;
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

    public static int calculateAgeInMonth(long birthday, long selectedDay) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(birthday);

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(selectedDay);

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

    /**
     * @return : làm tròn HALF_UP
     */
    public static double getBmr(double weightInKg, double heightInCm, long birthdayInMs, int gender) {
        if (gender < 0 || gender > 1) {
            gender = Consts.GENDER_MALE;
        }
        int ageInYear = calculateAgeInYear(birthdayInMs);
        double bmr;
        if (gender == Consts.GENDER_MALE) {
            bmr = 10 * weightInKg + 6.25 * heightInCm - 5 * ageInYear + 5;
        } else {
            bmr = 10 * weightInKg + 6.25 * heightInCm - 5 * ageInYear - 161;
        }
        return bmr;
    }

    public static double getBodyFatPercentage(double bmi, int gender, long birthdayInMs) {
        int ageInMonth = calculateAgeInMonth(birthdayInMs);
        int ageInYear = calculateAgeInYear(birthdayInMs);

        if (gender < 0 || gender > 1) {
            gender = Consts.GENDER_MALE;
        }
        if (ageInMonth <= Consts.MAX_CHILD_AGE_IN_MONTH) {
            if (gender == Consts.GENDER_MALE) {
                return 1.51 * bmi - 0.7 * ageInYear - 2.2;
            } else {
                return 1.51 * bmi - 0.7 * ageInYear + 1.4;
            }
        } else {
            if (gender == Consts.GENDER_MALE) {
                return 1.2 * bmi + 0.23 * ageInYear - 16.2;
            } else {
                return 1.2 * bmi + 0.23 * ageInYear - 5.4;
            }
        }
    }

    /**
     * @param bmr : @getBmr
     */
    public static double getTdee(double bmr, @ActivityLevel int activityLevel) {
        double r = 0;
        switch (activityLevel) {
            case ActivityLevel.SEDENTARY:
                r = 1.2;
                break;
            case ActivityLevel.LIGHTLY:
                r = 1.375;
                break;
            case ActivityLevel.MODERATELY:
                r = 1.55;
                break;
            case ActivityLevel.VERY_ACTIVE:
                r = 1.725;
                break;
            case ActivityLevel.EXTREMELY_ACTIVE:
                r = 1.925;
                break;
        }
        return bmr * r;
    }

    /**
     * @param tdee : @
     */
    public static double getCaloRequired(double tdee, @GoalType int goalType) {
        if (goalType == GoalType.STRICT_LOSS_WEIGHT) {
            return tdee * 0.56;
        } else if (goalType == GoalType.NORMAL_LOSS_WEIGHT) {
            return tdee * 0.78;
        } else if (goalType == GoalType.COMFORTABLE_LOSS_WEIGHT) {
            return tdee * 0.89;
        } else if (goalType == GoalType.NORMAL_GAIN_WEIGHT) {
            return tdee + 500;
        } else if (goalType == GoalType.STRICT_GAIN_WEIGHT) {
            return tdee + 1000;
        }
        return tdee;
    }

    /**
     * Tính Healthy Weight cho người lớn
     */
    public static HealthyWeight getHealthyWeight(double heightInCm) {
        HealthyWeight healthyWeight = new HealthyWeight();
        healthyWeight.healthyWeightFrom = 18.5 * heightInCm / 100 * heightInCm / 100;
        healthyWeight.healthyWeightTo = 24.9 * heightInCm / 100 * heightInCm / 100;
        return healthyWeight;
    }

    /**
     * Tính Healthy Weight cho cả trẻ em và người lớn
     */
    public static HealthyWeight getHealthyWeight(long birthdayInMilliSeconds, int gender, double heightInCm) {
        int ageInMonth = calculateAgeInMonth(birthdayInMilliSeconds);
        if (isChild(ageInMonth)) {
            return calculateHealthyWeightForChild(ageInMonth, gender, heightInCm);
        }
        return getHealthyWeight(heightInCm);
    }

    public static int calculateAgeInYear(long birthdayInMilliSeconds) {
        int ageInMonth = calculateAgeInMonth(birthdayInMilliSeconds);
        int age;
        if (ageInMonth % 12 == 0) {
            age = ageInMonth / 12;
        } else {
            age = ageInMonth / 12 + 1;
        }
        return age;
    }

    public static boolean isChild(int ageInMonth) {
        return ageInMonth <= Consts.MAX_CHILD_AGE_IN_MONTH;
    }

    public static boolean isChild(long birthdayInMilliSeconds) {
        return isChild(calculateAgeInMonth(birthdayInMilliSeconds));
    }

    public static class HealthyWeight {
        public double healthyWeightFrom;
        public double healthyWeightTo;

        public HealthyWeight() {
        }

        public HealthyWeight(double healthyWeightFrom, double healthyWeightTo) {
            this.healthyWeightFrom = healthyWeightFrom;
            this.healthyWeightTo = healthyWeightTo;
        }
    }
}

