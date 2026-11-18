package org.firstinspires.ftc.teamcode.teleop;

public class MovingShot {

    private double distance;
    private double fieldAngle;
    private double rpm;
    private double hood;
    private double flightTime;
    private boolean valid;

    public void update(double x, double y, double heading, double vx, double vy, double omega, boolean regression) {
        valid = false;

        if (!Double.isFinite(RobotConfig.TARGET_X_IN) || !Double.isFinite(RobotConfig.TARGET_Y_IN)) {
            return;
        }

        double delay = RobotConfig.RELEASE_DELAY_SECONDS;
        double releaseX = x + vx * delay;
        double releaseY = y + vy * delay;
        double releaseHeading = heading + omega * delay;

        double cos = Math.cos(releaseHeading);
        double sin = Math.sin(releaseHeading);
        double offsetX = RobotConfig.TURRET_FORWARD_OFFSET_IN * cos - RobotConfig.TURRET_LEFT_OFFSET_IN * sin;
        double offsetY = RobotConfig.TURRET_FORWARD_OFFSET_IN * sin + RobotConfig.TURRET_LEFT_OFFSET_IN * cos;
        double muzzleX = releaseX + offsetX;
        double muzzleY = releaseY + offsetY;

        double staticDistance = Math.hypot(RobotConfig.TARGET_X_IN - muzzleX, RobotConfig.TARGET_Y_IN - muzzleY);
        flightTime = ShotTable.time(staticDistance);

        double muzzleVx = vx - omega * offsetY;
        double muzzleVy = vy + omega * offsetX;
        double compensatedX = RobotConfig.TARGET_X_IN - muzzleVx * flightTime;
        double compensatedY = RobotConfig.TARGET_Y_IN - muzzleVy * flightTime;
        double dx = compensatedX - muzzleX;
        double dy = compensatedY - muzzleY;

        distance = Math.hypot(dx, dy);
        fieldAngle = Math.atan2(dy, dx);
        rpm = ShotTable.rpm(distance, regression);
        hood = ShotTable.hood(distance, regression);

        valid = Double.isFinite(rpm) && Double.isFinite(hood)
            && distance >= RobotConfig.MIN_SHOT_DISTANCE_IN
            && distance <= RobotConfig.MAX_SHOT_DISTANCE_IN;
    }

    public boolean isValid() {
        return valid;
    }

    public double getDistance() {
        return distance;
    }

    public double getFieldAngle() {
        return fieldAngle;
    }

    public double getRpm() {
        return rpm;
    }

    public double getHood() {
        return hood;
    }

    public double getFlightTime() {
        return flightTime;
    }
}
