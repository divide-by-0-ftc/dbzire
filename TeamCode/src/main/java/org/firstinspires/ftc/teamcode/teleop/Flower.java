package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Flower {

    private final Servo servo;
    private boolean raised;
    private boolean lastTriangle;

    public Flower(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, RobotConfig.FLOWER_SERVO);
        servo.setPosition(RobotConfig.FLOWER_DOWN);
    }

    public void update(boolean triangle) {
        if (triangle && !lastTriangle) {
            raised = !raised;
            servo.setPosition(raised ? RobotConfig.FLOWER_UP : RobotConfig.FLOWER_DOWN);
        }
        lastTriangle = triangle;
    }

    public boolean isRaised() {
        return raised;
    }
}
