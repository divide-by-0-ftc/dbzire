package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Intake {

    private final DcMotorEx frontMotor;
    private final DcMotorEx backMotor;
    private final Servo frontServo;
    private final Servo backServo;
    private final Servo holdServo;

    public Intake(HardwareMap hardwareMap) {
        frontMotor = hardwareMap.get(DcMotorEx.class, RobotConfig.FRONT_INTAKE_MOTOR);
        backMotor = hardwareMap.get(DcMotorEx.class, RobotConfig.BACK_INTAKE_MOTOR);
        frontServo = hardwareMap.get(Servo.class, RobotConfig.FRONT_INTAKE_SERVO);
        backServo = hardwareMap.get(Servo.class, RobotConfig.BACK_INTAKE_SERVO);
        holdServo = hardwareMap.get(Servo.class, RobotConfig.HOLD_SERVO);

        frontMotor.setDirection(RobotConfig.FRONT_INTAKE_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        backMotor.setDirection(RobotConfig.BACK_INTAKE_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        frontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        stop();
    }

    public void apply(int mode, boolean shooting) {
        double power = Range.clip(RobotConfig.INTAKE_POWER, 0, 1);
        double frontPower = shooting || mode == 1 ? power : 0;
        double backPower = shooting || mode == -1 ? power : 0;

        frontMotor.setPower(frontPower);
        backMotor.setPower(backPower);
        frontServo.setPosition(mode == 1 ? RobotConfig.FRONT_DOWN : RobotConfig.FRONT_UP);
        backServo.setPosition(mode == -1 ? RobotConfig.BACK_DOWN : RobotConfig.BACK_UP);
        holdServo.setPosition(shooting ? RobotConfig.HOLD_RELEASED : RobotConfig.HOLD_BLOCKED);
    }

    public void stop() {
        frontMotor.setPower(0);
        backMotor.setPower(0);
        frontServo.setPosition(RobotConfig.FRONT_UP);
        backServo.setPosition(RobotConfig.BACK_UP);
        holdServo.setPosition(RobotConfig.HOLD_BLOCKED);
    }
}
