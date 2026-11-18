package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class Relocalizer {

    private boolean lastUp;
    private boolean lastDown;
    private boolean lastLeft;
    private boolean lastRight;
    private String status = "none";

    public boolean update(Follower follower, boolean up, boolean down, boolean left, boolean right) {
        boolean changed = false;
        Pose pose = follower.pose();

        if (up && !lastUp) {
            if (Double.isFinite(RobotConfig.BOTTOM_LEFT_X_IN) && Double.isFinite(RobotConfig.BOTTOM_LEFT_Y_IN)) {
                follower.setPose(new Pose(RobotConfig.BOTTOM_LEFT_X_IN, RobotConfig.BOTTOM_LEFT_Y_IN, pose.heading()));
                status = "bottom left";
                changed = true;
            } else {
                status = "set bottom left coordinates";
            }
        } else if (down && !lastDown) {
            if (Double.isFinite(RobotConfig.BOTTOM_RIGHT_X_IN) && Double.isFinite(RobotConfig.BOTTOM_RIGHT_Y_IN)) {
                follower.setPose(new Pose(RobotConfig.BOTTOM_RIGHT_X_IN, RobotConfig.BOTTOM_RIGHT_Y_IN, pose.heading()));
                status = "bottom right";
                changed = true;
            } else {
                status = "set bottom right coordinates";
            }
        } else if (left && !lastLeft) {
            if (Double.isFinite(RobotConfig.SIDE_WALL_X_IN)) {
                follower.setX(RobotConfig.SIDE_WALL_X_IN);
                status = "side wall X";
                changed = true;
            } else {
                status = "set side wall X";
            }
        } else if (right && !lastRight) {
            if (Double.isFinite(RobotConfig.BACK_WALL_Y_IN)) {
                follower.setY(RobotConfig.BACK_WALL_Y_IN);
                status = "back wall Y";
                changed = true;
            } else {
                status = "set back wall Y";
            }
        }

        lastUp = up;
        lastDown = down;
        lastLeft = left;
        lastRight = right;
        return changed;
    }

    public String getStatus() {
        return status;
    }
}
