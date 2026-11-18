
package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp(name = "teleop")
public class teleop extends OpMode {

    private Follower follower;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);

        telemetry.addData("Follower initialized", follower != null);
        telemetry.update();
    }

    @Override
    public void loop() {
        if (follower == null) {
            telemetry.addLine("ERROR: Follower is null");
            telemetry.update();
            return;
        }

        follower.update();

        if (follower.pose() == null) {
            telemetry.addLine("ERROR: Pose is null");
            telemetry.update();
            return;
        }

        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                follower.pose().heading()
        );

        follower.manual(powers);

        telemetry.addData(
                "Heading (rad)",
                follower.pose().heading()
        );
        telemetry.update();
    }
}
