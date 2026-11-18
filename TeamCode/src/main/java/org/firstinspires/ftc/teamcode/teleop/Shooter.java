package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Shooter {

    private final DcMotorEx left;
    private final DcMotorEx right;
    private final Servo hood;
    private double targetRpm;
    private double currentRpm;
    private double hoodPosition = RobotConfig.HOOD_HOME;
    private double hoodChangedAt;
    private double now;

    public Shooter(HardwareMap hardwareMap) {
        left = hardwareMap.get(DcMotorEx.class, RobotConfig.SHOOTER_LEFT);
        right = hardwareMap.get(DcMotorEx.class, RobotConfig.SHOOTER_RIGHT);
        hood = hardwareMap.get(Servo.class, RobotConfig.HOOD_SERVO);

        left.setDirection(RobotConfig.SHOOTER_LEFT_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        right.setDirection(RobotConfig.SHOOTER_RIGHT_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        hood.setPosition(hoodPosition);
        stop();
    }

    public void command(double rpm, double position, double time) {
        now = time;
        targetRpm = Range.clip(rpm, 0, RobotConfig.SHOOTER_RPM_MAX);
        double nextHood = Range.clip(position, RobotConfig.HOOD_MIN, RobotConfig.HOOD_MAX);
        if (Math.abs(nextHood - hoodPosition) > 0.003) {
            hood.setPosition(nextHood);
            hoodPosition = nextHood;
            hoodChangedAt = now;
        }
    }

    public void update() {
        currentRpm = Math.abs(left.getVelocity()) * 60.0 / RobotConfig.SHOOTER_TICKS_PER_REV;
        double error = targetRpm - currentRpm;
        double power = targetRpm <= 0 ? 0 : Range.clip(
            RobotConfig.SHOOTER_KF * targetRpm + RobotConfig.SHOOTER_KP * error,
            0, 1
        );
        left.setPower(power);
        right.setPower(power);
    }

    public boolean ready() {
        return targetRpm > 0 && Math.abs(targetRpm - currentRpm) <= RobotConfig.SHOOTER_READY_RPM
            && now - hoodChangedAt >= RobotConfig.HOOD_SETTLE_SECONDS;
    }

    public double getCurrentRpm() {
        return currentRpm;
    }

    public double getTargetRpm() {
        return targetRpm;
    }

    public void stop() {
        targetRpm = 0;
        left.setPower(0);
        right.setPower(0);
    }
}
