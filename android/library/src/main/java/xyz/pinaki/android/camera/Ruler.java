package xyz.pinaki.android.camera;

import android.graphics.Point;

import java.util.List;

/**
 * Created by security on 1/31/2018.
 */

public class Ruler {

    private Ruler(){}

    /**
     * Based on a list of 4 points, computes the distance between the last 2 using the first 2 as a
     * reference.
     * @param points A List of 4 points
     * @param distance The value of the distance between the first 2 points
     * @return The value of the distance between the last 2 points
     */
    public static double compute(List<Point> points, double distance){
        if(points.size() < 2) return -1;

        //Get reference points
        Point ref1 = points.get(0);
        Point ref2 = points.get(1);




        double reference = getDistance(ref1, ref2);

        double result = reference * distance   / 250 ;


        return result;
    }

    /**
     * Get the distance between 2 points
     * @param p1 First point
     * @param p2 Second point
     * @return Distance between the 2 points
     */
    private static double getDistance(Point p1, Point p2){
        double x = Math.pow(p2.x - p1.x, 2);
        double y = Math.pow(p2.y - p1.y, 2);
        return Math.sqrt(x+y);
    }

    /**
     * Converts between units of length.
     * @param refUnit The unit of the reference size
     * @param reference The reference size
     * @param meaUnit The unit of the measurement size
     * @param measurement The measurement size
     * @return measurement converted to refUnit
     */
    private static double convertUnits(int refUnit, double reference, int meaUnit, double measurement){
        if(refUnit == meaUnit)
            return measurement;

        measurement = toMeters(measurement, refUnit);
        switch (meaUnit){
            case 0:
                return measurement;
            case 1:
                return Utils.metersToCentimeters(measurement);
            case 2:
                return Utils.metersToMillimeters(measurement);
            case 3:
                return Utils.metersToInch(measurement);
            case 4:
                return Utils.metersToFeet(measurement);
            case 5:
                return Utils.metersToYards(measurement);
            default:
                return -1;
        }
    }

    /**
     * Converts a value in a given unit to meters.
     * @param measurement The length value.
     * @param refUnit The original unit.
     * @return The length value in meters
     */
    private static double toMeters(double measurement, int refUnit){
        switch (refUnit){
            case 0:
                return measurement;
            case 1:
                return Utils.centimetersToMeters(measurement);
            case 2:
                return Utils.millimetersToMeters(measurement);
            case 3:
                return Utils.inchesToMeters(measurement);
            case 4:
                return Utils.yardsToMeters(measurement);
            default:
                return -1;
        }
    }
}
