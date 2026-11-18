package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class BallSensors {

    private final AnalogInput[] sensors = new AnalogInput[4];
    private final boolean[] detected = new boolean[4];
    private final double[] voltages = new double[4];
    private int count;

    public BallSensors(HardwareMap hardwareMap) {
        for (int i = 0; i < sensors.length; i++) {
            sensors[i] = hardwareMap.get(AnalogInput.class, RobotConfig.BALL_SENSOR_NAMES[i]);
        }
    }

    public void update() {
        count = 0;

        for (int i = 0; i < sensors.length; i++) {
            double voltage = sensors[i].getVoltage();
            double threshold = RobotConfig.BALL_THRESHOLDS_V[i];
            double hysteresis = RobotConfig.BALL_SENSOR_HYSTERESIS_V;
            boolean high = RobotConfig.BALL_PRESENT_WHEN_HIGH[i];

            voltages[i] = voltage;

            if (high) {
                detected[i] = detected[i] ? voltage >= threshold - hysteresis : voltage >= threshold + hysteresis;
            } else {
                detected[i] = detected[i] ? voltage <= threshold + hysteresis : voltage <= threshold - hysteresis;
            }

            if (detected[i]) {
                count++;
            }
        }
    }

    public boolean isPresent(int index) {
        return detected[index];
    }

    public int count() {
        return count;
    }

    public boolean isFull() {
        return count == sensors.length;
    }

    public double getVoltage(int index) {
        return voltages[index];
    }
}
