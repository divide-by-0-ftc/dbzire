package org.firstinspires.ftc.teamcode.teleop;

public final class RobotConfig {

    private RobotConfig() {}

    public static String FRONT_INTAKE_MOTOR = "frontIntake";
    public static String BACK_INTAKE_MOTOR = "backIntake";
    public static String FRONT_INTAKE_SERVO = "frontIntakeServo";
    public static String BACK_INTAKE_SERVO = "backIntakeServo";
    public static String HOLD_SERVO = "holdServo";
    public static String FLOWER_SERVO = "flowerServo";
    public static String HOOD_SERVO = "hoodServo";
    public static String SHOOTER_LEFT = "shooterLeft";
    public static String SHOOTER_RIGHT = "shooterRight";
    public static String TURRET_ENCODER = "turretEncoder";
    public static String TURRET_SERVO_1 = "turret1";
    public static String TURRET_SERVO_2 = "turret2";
    public static String TURRET_SERVO_3 = "turret3";
    public static String[] BALL_SENSOR_NAMES = {"ball1", "ball2", "ball3", "ball4"};

    public static boolean FRONT_INTAKE_REVERSED = false;
    public static boolean BACK_INTAKE_REVERSED = false;
    public static boolean SHOOTER_LEFT_REVERSED = true;
    public static boolean SHOOTER_RIGHT_REVERSED = false;
    public static boolean TURRET_1_REVERSED = false;
    public static boolean TURRET_2_REVERSED = true;
    public static boolean TURRET_3_REVERSED = false;

    public static double INTAKE_POWER = 1.0;
    public static double FRONT_UP = 0.50;
    public static double FRONT_DOWN = 0.50;
    public static double BACK_UP = 0.50;
    public static double BACK_DOWN = 0.50;
    public static double HOLD_BLOCKED = 0.50;
    public static double HOLD_RELEASED = 0.50;
    public static double FLOWER_UP = 0.50;
    public static double FLOWER_DOWN = 0.50;
    public static double HOOD_MIN = 0.15;
    public static double HOOD_MAX = 0.85;
    public static double HOOD_HOME = 0.50;

    public static boolean[] BALL_PRESENT_WHEN_HIGH = {false, false, false, false};
    public static double[] BALL_THRESHOLDS_V = {1.50, 1.50, 1.50, 1.50};
    public static double BALL_SENSOR_HYSTERESIS_V = 0.08;
    public static double FULL_CONFIRM_SECONDS = 0.08;

    public static double SHOOTER_TICKS_PER_REV = 28.0;
    public static double SHOOTER_KF = 1.0 / 5000.0;
    public static double SHOOTER_KP = 0.00015;
    public static double SHOOTER_RPM_MAX = 5000.0;
    public static double SHOOTER_READY_RPM = 100.0;
    public static double HOOD_SETTLE_SECONDS = 0.18;

    public static boolean TURRET_ENABLED = false;
    public static double TURRET_ENCODER_ZERO_V = 0.0;
    public static double TURRET_ENCODER_FULL_SCALE_V = 3.3;
    public static double TURRET_FORWARD_ZERO_RAD = 0.0;
    public static double TURRET_KP = 1.5;
    public static double TURRET_KD = 0.05;
    public static double TURRET_MAX_POWER = 0.40;
    public static double TURRET_READY_DEG = 3.0;

    public static double TARGET_X_IN = Double.NaN;
    public static double TARGET_Y_IN = Double.NaN;
    public static double RELEASE_DELAY_SECONDS = 0.10;
    public static double TURRET_FORWARD_OFFSET_IN = 0.0;
    public static double TURRET_LEFT_OFFSET_IN = 0.0;
    public static double MIN_SHOT_DISTANCE_IN = 25.0;
    public static double MAX_SHOT_DISTANCE_IN = 160.0;

    public static double BOTTOM_LEFT_X_IN = Double.NaN;
    public static double BOTTOM_LEFT_Y_IN = Double.NaN;
    public static double BOTTOM_RIGHT_X_IN = Double.NaN;
    public static double BOTTOM_RIGHT_Y_IN = Double.NaN;
    public static double SIDE_WALL_X_IN = Double.NaN;
    public static double BACK_WALL_Y_IN = Double.NaN;
    public static double TELEMETRY_SECONDS = 0.15;
}
