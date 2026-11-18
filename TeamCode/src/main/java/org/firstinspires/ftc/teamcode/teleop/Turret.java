package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class Turret {

    private final CRServo servo1;
    private final CRServo servo2;
    private final CRServo servo3;
    private final AnalogInput encoder;
    private double angle;
    private double lastRaw;
    private double target;
    private double previousError;
    private boolean initialized;

    public Turret(HardwareMap hardwareMap) {
        servo1 = hardwareMap.get(CRServo.class, RobotConfig.TURRET_SERVO_1);
        servo2 = hardwareMap.get(CRServo.class, RobotConfig.TURRET_SERVO_2);
        servo3 = hardwareMap.get(CRServo.class, RobotConfig.TURRET_SERVO_3);
        encoder = hardwareMap.get(AnalogInput.class, RobotConfig.TURRET_ENCODER);

        servo1.setDirection(RobotConfig.TURRET_1_REVERSED ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        servo2.setDirection(RobotConfig.TURRET_2_REVERSED ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        servo3.setDirection(RobotConfig.TURRET_3_REVERSED ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        updateEncoder();
        target = angle;
        stop();
    }

    private static double wrap(double value) {
        return Math.atan2(Math.sin(value), Math.cos(value));
    }

    private void updateEncoder() {
        double raw = (encoder.getVoltage() - RobotConfig.TURRET_ENCODER_ZERO_V)
            * 2.0 * Math.PI / RobotConfig.TURRET_ENCODER_FULL_SCALE_V;

        if (!initialized) {
            angle = wrap(raw);
            initialized = true;
        } else {
            angle += wrap(raw - lastRaw);
        }
        lastRaw = raw;
    }

    public void aim(double fieldAngle, double robotHeading, double omega) {
        double headingAtRelease = robotHeading + omega * RobotConfig.RELEASE_DELAY_SECONDS;
        double desired = wrap(fieldAngle - headingAtRelease + RobotConfig.TURRET_FORWARD_ZERO_RAD);
        target = angle + wrap(desired - angle);
    }

    public void update(double dt) {
        updateEncoder();
        double error = target - angle;
        double derivative = dt > 0.001 && dt < 0.2 ? (error - previousError) / dt : 0;
        double power = Range.clip(
            RobotConfig.TURRET_KP * error + RobotConfig.TURRET_KD * derivative,
            -RobotConfig.TURRET_MAX_POWER,
            RobotConfig.TURRET_MAX_POWER
        );

        if (!RobotConfig.TURRET_ENABLED) {
            power = 0;
        }

        servo1.setPower(power);
        servo2.setPower(power);
        servo3.setPower(power);
        previousError = error;
    }

    public boolean ready() {
        return RobotConfig.TURRET_ENABLED && Math.abs(target - angle) <= Math.toRadians(RobotConfig.TURRET_READY_DEG);
    }

    public double getErrorDegrees() {
        return Math.toDegrees(target - angle);
    }

    public double getAngleDegrees() {
        return Math.toDegrees(angle);
    }

    public void stop() {
        servo1.setPower(0);
        servo2.setPower(0);
        servo3.setPower(0);
    }
}
