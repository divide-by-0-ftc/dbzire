package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Velocity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp(name = "Main TeleOp")
public class MainTeleOp extends OpMode {

    private Follower follower;
    private BallManager balls;
    private Shooter shooter;
    private Turret turret;
    private Flower flower;
    private Relocalizer relocalizer;
    private final MovingShot shot = new MovingShot();
    private final ElapsedTime timer = new ElapsedTime();
    private double lastTime;
    private double lastTelemetry;
    private boolean regression;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        balls = new BallManager(hardwareMap);
        shooter = new Shooter(hardwareMap);
        turret = new Turret(hardwareMap);
        flower = new Flower(hardwareMap);
        relocalizer = new Relocalizer();
        timer.reset();
        telemetry.addLine("Check hardware directions, calibration and target coordinates before driving");
        telemetry.update();
    }

    @Override
    public void loop() {
        double now = timer.seconds();
        double dt = now - lastTime;
        lastTime = now;

        DrivePowers powers = ManualDrive.fieldCentric(
            -gamepad1.left_stick_y,
            gamepad1.left_stick_x,
            gamepad1.right_stick_x,
            follower.pose().heading()
        );
        follower.manual(powers);
        follower.update();

        boolean relocated = relocalizer.update(
            follower,
            gamepad1.dpad_up,
            gamepad1.dpad_down,
            gamepad1.dpad_left,
            gamepad1.dpad_right
        );

        if (gamepad1.circle) {
            regression = false;
        }
        if (gamepad1.cross) {
            regression = true;
        }

        Pose pose = follower.pose();
        Velocity velocity = follower.velocity();
        double vx = relocated ? 0 : velocity.vx;
        double vy = relocated ? 0 : velocity.vy;
        double omega = relocated ? 0 : velocity.omega;

        shot.update(pose.x(), pose.y(), pose.heading(), vx, vy, omega, regression);

        if (shot.isValid()) {
            shooter.command(shot.getRpm(), shot.getHood(), now);
            turret.aim(shot.getFieldAngle(), pose.heading(), omega);
        } else {
            shooter.command(0, RobotConfig.HOOD_HOME, now);
        }

        shooter.update();
        turret.update(dt);

        boolean shooting = gamepad1.right_trigger > 0.5;
        balls.update(gamepad1.right_bumper, gamepad1.left_bumper, shooting, now);
        flower.update(gamepad1.triangle);

        if (now - lastTelemetry >= RobotConfig.TELEMETRY_SECONDS) {
            telemetry.addData("Pose", "%.1f, %.1f, %.1f", pose.x(), pose.y(), Math.toDegrees(pose.heading()));
            telemetry.addData("Velocity", "%.1f, %.1f, %.1f", vx, vy, Math.toDegrees(omega));
            telemetry.addData("Balls", balls.getBallCount());
            telemetry.addData("Full", balls.isFull());
            telemetry.addData("Intake", balls.getMode());
            telemetry.addData("Shooting", shooting);
            telemetry.addData("Regression", regression);
            telemetry.addData("Shot valid", shot.isValid());
            telemetry.addData("Distance", shot.getDistance());
            telemetry.addData("RPM", "%.0f / %.0f", shooter.getCurrentRpm(), shooter.getTargetRpm());
            telemetry.addData("Shooter ready", shooter.ready());
            telemetry.addData("Turret", "%.1f deg / %.1f deg error", turret.getAngleDegrees(), turret.getErrorDegrees());
            telemetry.addData("Turret ready", turret.ready());
            telemetry.addData("Reloc", relocalizer.getStatus());
            telemetry.update();
            lastTelemetry = now;
        }
    }

    @Override
    public void stop() {
        balls.stop();
        shooter.stop();
        turret.stop();
        follower.stop();
        follower.update();
    }
}
