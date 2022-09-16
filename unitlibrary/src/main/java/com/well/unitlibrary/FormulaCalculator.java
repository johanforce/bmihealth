package com.well.unitlibrary;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Developed by Luanvv on 26/12/2020 Copyright © 2020.
 */
public class FormulaCalculator {
    private static String KEY_LOSS = "loss", KEY_GAIN = "gain";

    /**
     * CT tính chỉ số cơ bản phản ánh mức độ mỡ thừa trong cơ thể
     *
     * @param weightKilogram: cân nặng quy đổi ra (kg)
     *                        height: chiều cao quy đổi ra (cm)
     * @return trả về chỉ sô BMI (kg/m2)
     */
    public static double BMI(double weightKilogram, double heightKilogram) {
        if (heightKilogram <= 0) {
            return 0.0;
        }
        return (weightKilogram / (heightKilogram * heightKilogram)) * 10000;
    }

    /**
     * @param totalSteps :tổng số bước chân
     *                   time: thời gian quy đổi ra (phút)
     * @return trả về chỉ số cadence (spm )
     */
    public static int cadence(int totalSteps, long timeMinutes) {
        if (timeMinutes <= 0) {
            return 0;
        }
        return (int) (totalSteps / timeMinutes);
    }

    /**
     * CT tính Tổng số met hoặc feet đã đi lên trong 1 buổi chạy hoặc cuộc thi.
     *
     * @param elevationsMeters : Danh sách các điểm kiểm tra
     * @return trả về chỉ số Elevation Gain (mét )
     */
    public static int elevationGain(List<Double> elevationsMeters) {
        long elevationGain = 0;
        if (elevationsMeters != null && !elevationsMeters.isEmpty()) {
            double temp = elevationsMeters.get(0);
            for (double elevation : elevationsMeters) {
                long diff = Math.round(elevation - temp);
                if (diff > 0) {
                    elevationGain += diff;
                }
                temp = elevation;
            }
        }
        return (int) elevationGain;
    }

    /**
     * CT tính Tổng số met hoặc feet đã đi xuống trong 1 buổi chạy hoặc cuộc thi
     *
     * @param elevationsMeters : Danh sách các điểm kiểm tra
     * @return trả về chỉ số Elevation Loss (mét )
     */
    public static int elevationLoss(List<Double> elevationsMeters) {
        long elevationLoss = 0;
        if (elevationsMeters != null && !elevationsMeters.isEmpty()) {
            double temp = elevationsMeters.get(0);
            for (double elevation : elevationsMeters) {
                long diff = Math.round(elevation - temp);
                if (diff < 0) {
                    elevationLoss -= diff;
                }
                temp = elevation;
            }
        }
        return (int) elevationLoss;
    }

    /**
     * CT tính Tổng elecation
     *
     * @param elevations : Danh sách các điểm kiểm tra
     * @return trả về chỉ số Elevation theo map(mét )
     */
    public static Map<String, Integer> elevationMap(List<Double> elevations) {
        HashMap<String, Integer> elevationMap = new HashMap<>();
        if (elevations != null && !elevations.isEmpty()) {
            double temp = elevations.get(0);
            long elevationLoss = 0, elevationGain = 0;
            for (double elevation : elevations) {
                long diff = Math.round(elevation - temp);
                if (diff < 0) {
                    elevationLoss -= diff;
                } else {
                    elevationGain += diff;
                }
                temp = elevation;
            }
            elevationMap.put(KEY_LOSS, (int) elevationLoss);
            elevationMap.put(KEY_GAIN, (int) elevationGain);
        }
        return elevationMap;
    }

    /**
     * CT tính nhịp tim tối đa
     *
     * @param age : tuổi của người cần kiểm tra 0< age< 220
     * @return trả về chỉ số Max Heart Rate
     */
    public static int maxHeartRate(int age) throws RuntimeException {
        if (0 < age && age < 220) {
            return 220 - age;
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * CT tính nhịp tim mục tiêu Mặc định
     *
     * @param age : tuổi của người cần kiểm tra 0< age< 220
     *            intensity: cường độ mục tiêu (%) 0->100
     * @return trả về chỉ số Target Heart Rate
     */
    public static double targetHeartRateDefault(int age, double intensity) throws RuntimeException {
        if (0 > age || age > 220) {
            throw new RuntimeException();
        }
        int maxHeartRate = 220 - age;
        return maxHeartRate * intensity / 100;
    }

    /**
     * CT tính nhịp tim mục tiêu Karvonen
     *
     * @param age : tuổi của người cần kiểm tra  0< age< 220
     *            restingHeartRate : nhịp tim khi nghỉ ngơi
     *            intensity: cường độ mục tiêu (%) 0->100
     * @return trả về chỉ số Target Heart Rate
     */
    public static double targetHeartRateKarvonen(int age, int restingHeartRate, double intensity) throws RuntimeException {
        if (0 > age || age > 220) {
            throw new RuntimeException();
        }
        int maxHeartRate = 220 - age;
        return ((maxHeartRate - restingHeartRate) * intensity / 100) + restingHeartRate;
    }

    /**
     * CT tính Dự trữ nhịp tim
     *
     * @param age : tuổi của người cần kiểm tra 0< age< 220
     *            restingHeartRate : nhịp tim khi nghỉ ngơi
     * @return trả về chỉ số Dự trữ nhịp tim (bpm)
     */
    public static int heartRateReserve(int age, int restingHeartRate) throws RuntimeException {
        if (0 > age || age > 220) {
            throw new RuntimeException();
        }
        int maxHeartRate = 220 - age;
        return maxHeartRate - restingHeartRate;
    }

    /**
     * CT tính Công suất
     *
     * @param weightKilogram : Cân nặng của người cần kiểm tra (kg)
     *                       distance : Quãng đường di chuyển (m)
     *                       time : thời gian di chuyên (s)
     * @return Công suất (w)
     */
    public static double power(double weightKilogram, double distanceMeters, long timeSeconds) {
        if (timeSeconds <= 0) {
            return 0.0;
        }
        return (weightKilogram * distanceMeters * 981 / 100) / timeSeconds;
    }

    /**
     * CT tính Chiều dài sải chân
     *
     * @param totalSteps : Tổng số bước chân
     *                   distance : Quãng đường di chuyển (m)
     * @return Chiều dài sải chân  (mét)
     */
    public static double strideLengthBaseOnDistanceAndTotalSteps(double distanceMeters, int totalSteps) {
        if (totalSteps <= 0) {
            return 0.0;
        }
        return distanceMeters / totalSteps;
    }

    /**
     * CT tính Chiều dài sải chân
     *
     * @param heightCentimeters : Chiều cao (Centimeter)
     * @return Chiều dài sải chân  (mét)
     */
    public static double strideLengthBaseOnHeight(double heightCentimeters, int gender, boolean isRun) {
        double valueConstant;

        switch (gender) {
            case 0:
//                Gender Male
                valueConstant = isRun ? 0.415 : 0.38;
                break;
            case 1:
//                Gender Female
                valueConstant = isRun ? 0.413 : 0.36;
                break;
            case 2:
//                Gender Other
                valueConstant = isRun ? 0.414 : 0.37;
                break;
            default:
                valueConstant = isRun ? 0.415 : 0.38;
        }
        double strideLength = (heightCentimeters * valueConstant) / 100;
        if (strideLength > 1.2) {
            strideLength = 1.2;
        } else if (strideLength < 0.3) {
            strideLength = 0.3;
        }
        return strideLength;
    }

    public static double strideLengthInMetMax(double height) {
        double valueConstantMax = 0.6;

        double distance = (height * valueConstantMax) / 100;
        if (distance > 1.4) {
            distance = 1.4;
        } else if (distance < 0.3) {
            distance = 0.3;
        }
        return distance;
    }

    /**
     * CT tính tốc độ khi chạy
     *
     * @param time : Thời gian chạy
     *             distanceMeter : Quãng đường di chuyển ( met)
     * @return tốc độ khi chạy  (m/s)
     */

    public static double speedMeterPerSeconds(double distanceMeter, double time, TimeUnit timeUnit) {
        double seconds = 0;
        if (time <= 0) {
            return 0.0;
        }
        switch (timeUnit) {
            case MINUTES:
                seconds = time * 60;
                break;
            case HOURS:
                seconds = time * 3600;
                break;
            default:
                seconds = time;
        }
        return distanceMeter / seconds;
    }

    /**
     * CT tính tốc độ khi chạy
     *
     * @param time : Thời gian chạy
     *             distanceMeter : Quãng đường di chuyển ( km)
     * @return tốc độ khi chạy  (km/h)
     */

    public static double speedKilometerPerHours(double distanceMeter, double time, TimeUnit timeUnit) {
        if (time <= 0) {
            return 0.0;
        }
        double speed = speedMeterPerSeconds(distanceMeter, time, timeUnit);
        return UnitConverter.convertSpeedMetPerSecToKmPerHour(speed);
    }

    /**
     * CT tính tốc độ khi chạy
     *
     * @param time : Thời gian chạy
     *             distanceMeter : Quãng đường di chuyển (mi)
     * @return tốc độ khi chạy  (mi/h)
     */

    public static double speedMilesPerHours(double distanceMeter, double time, TimeUnit timeUnit) {
        if (time <= 0) {
            return 0.0;
        }
        double speed = speedKilometerPerHours(distanceMeter, time, timeUnit);
        return UnitConverter.convertSpeedKmPerHourToMilePerHour(speed);
    }

    /**
     * CT tính tốc độ khi chạy /1km
     *
     * @param time : Thời gian chạy
     *             distance : Quãng đường di chuyển (km)
     * @return tốc độ khi chạy  (phút/km)
     */
    public static double paceMinutesPerKilometer(double distanceKilometer, long time, TimeUnit timeUnit) {
        if (distanceKilometer <= 0) {
            return 0.0;
        }
        double minutes = 0;
        switch (timeUnit) {
            case SECONDS:
                minutes = time / 60;
                break;
            case HOURS:
                minutes = time * 60;
                break;
            default:
                minutes = time;
        }
        return minutes / distanceKilometer;
    }

    /**
     * CT tính Calorie
     *
     * @param weightKilogram : cân nặng tính theo kilogram
     *                       totalSteps : Tổng số bước chân
     * @return Lượng calorie tiêu thụ (cal)
     */

    public static String calorieByStep(long totalSteps, double weightKilogram) {
        return String.format(Locale.getDefault(), "%.1f", getCalorieFloatByStep(weightKilogram, totalSteps));
    }

    public static float getCalorieFloatByStep(double weight, float steps) {
        double caolires = steps * 0.6f * weight * 1.036f / 1000;
        return UnitConverter.formatDoubleToFloat(caolires, 2);
    }

}
