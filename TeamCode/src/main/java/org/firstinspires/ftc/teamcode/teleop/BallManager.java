package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class BallManager {

    private final BallSensors sensors;
    private final Intake intake;
    private int mode;
    private boolean full;
    private boolean lastR1;
    private boolean lastL1;
    private double fullSince = Double.NaN;

    public BallManager(HardwareMap hardwareMap) {
        sensors = new BallSensors(hardwareMap);
        intake = new Intake(hardwareMap);
    }

    public void update(boolean r1, boolean l1, boolean shooting, double now) {
        sensors.update();

        if (sensors.isFull()) {
            if (Double.isNaN(fullSince)) {
                fullSince = now;
            }
            if (now - fullSince >= RobotConfig.FULL_CONFIRM_SECONDS) {
                full = true;
            }
        } else {
            fullSince = Double.NaN;
        }

        if (shooting && !sensors.isFull()) {
            full = false;
        }

        boolean frontPressed = r1 && !lastR1;
        boolean backPressed = l1 && !lastL1;

        if (r1 && l1) {
            mode = 0;
        } else if (frontPressed) {
            mode = mode == 1 ? 0 : 1;
        } else if (backPressed) {
            mode = mode == -1 ? 0 : -1;
        }

        lastR1 = r1;
        lastL1 = l1;

        int activeMode = full ? 0 : mode;
        intake.apply(shooting ? 0 : activeMode, shooting);
    }

    public int getMode() {
        return mode;
    }

    public int getBallCount() {
        return sensors.count();
    }

    public boolean isFull() {
        return full;
    }

    public double getSensorVoltage(int index) {
        return sensors.getVoltage(index);
    }

    public void stop() {
        mode = 0;
        intake.stop();
    }
}
