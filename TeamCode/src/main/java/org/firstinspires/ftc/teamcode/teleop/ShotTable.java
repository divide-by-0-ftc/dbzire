package org.firstinspires.ftc.teamcode.teleop;

public final class ShotTable {

    private ShotTable() {}

    public static final double[] DISTANCE = {36, 60, 84, 108, 132, 156};
    public static final double[] RPM = {2500, 2700, 2900, 3150, 3450, 3750};
    public static final double[] HOOD = {0.60, 0.51, 0.43, 0.36, 0.30, 0.25};
    public static final double[] TIME = {0.42, 0.51, 0.60, 0.70, 0.81, 0.92};

    private static final double[] rpmFit = fit(RPM);
    private static final double[] hoodFit = fit(HOOD);

    public static double rpm(double distance, boolean regression) {
        return regression ? predict(rpmFit, distance) : interpolate(RPM, distance);
    }

    public static double hood(double distance, boolean regression) {
        return regression ? predict(hoodFit, distance) : interpolate(HOOD, distance);
    }

    public static double time(double distance) {
        return interpolate(TIME, distance);
    }

    private static double interpolate(double[] values, double distance) {
        if (distance <= DISTANCE[0]) {
            return values[0];
        }
        for (int i = 1; i < DISTANCE.length; i++) {
            if (distance <= DISTANCE[i]) {
                double t = (distance - DISTANCE[i - 1]) / (DISTANCE[i] - DISTANCE[i - 1]);
                return values[i - 1] + t * (values[i] - values[i - 1]);
            }
        }
        return values[values.length - 1];
    }

    private static double predict(double[] coefficients, double distance) {
        double clamped = Math.max(DISTANCE[0], Math.min(distance, DISTANCE[DISTANCE.length - 1]));
        double x = (clamped - 96.0) / 60.0;
        return coefficients[0] + coefficients[1] * x + coefficients[2] * x * x;
    }

    private static double[] fit(double[] values) {
        double[] sums = new double[5];
        double[] products = new double[3];

        for (int i = 0; i < DISTANCE.length; i++) {
            double x = (DISTANCE[i] - 96.0) / 60.0;
            double power = 1;
            for (int j = 0; j < sums.length; j++) {
                sums[j] += power;
                if (j < products.length) {
                    products[j] += power * values[i];
                }
                power *= x;
            }
        }

        double[][] matrix = {
            {sums[0], sums[1], sums[2], products[0]},
            {sums[1], sums[2], sums[3], products[1]},
            {sums[2], sums[3], sums[4], products[2]}
        };

        for (int col = 0; col < 3; col++) {
            int pivot = col;
            for (int row = col + 1; row < 3; row++) {
                if (Math.abs(matrix[row][col]) > Math.abs(matrix[pivot][col])) {
                    pivot = row;
                }
            }
            double[] temp = matrix[col];
            matrix[col] = matrix[pivot];
            matrix[pivot] = temp;

            double divisor = matrix[col][col];
            for (int j = col; j < 4; j++) {
                matrix[col][j] /= divisor;
            }
            for (int row = 0; row < 3; row++) {
                if (row == col) {
                    continue;
                }
                double factor = matrix[row][col];
                for (int j = col; j < 4; j++) {
                    matrix[row][j] -= factor * matrix[col][j];
                }
            }
        }
        return new double[]{matrix[0][3], matrix[1][3], matrix[2][3]};
    }
}
