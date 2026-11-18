
package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.follower.Follower;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    // Mecanum drivetrain configuration
    public static MecanumConfig drivetrainConfig =
            new MecanumConfig(c -> {
                c.frontLeftName.set("frontLeft");
                c.frontRightName.set("frontRight");
                c.backLeftName.set("backLeft");
                c.backRightName.set("backRight");

                c.frontLeftDirection.set(
                        DcMotorSimple.Direction.FORWARD);
                c.frontRightDirection.set(
                        DcMotorSimple.Direction.REVERSE);
                c.backLeftDirection.set(
                        DcMotorSimple.Direction.FORWARD);
                c.backRightDirection.set(
                        DcMotorSimple.Direction.REVERSE);

                c.manualBrakeMode.set(true);
            });

    // goBilda Pinpoint localization configuration
    public static PinpointConfig localizerConfig =
            new PinpointConfig(c -> {
                c.name.set("pinpoint");

                c.podType.set(
                        GoBildaPinpointDriver.GoBildaOdometryPods
                                .goBILDA_4_BAR_POD);

                c.xPodOffset.set(0.0);
                c.yPodOffset.set(0.0);

                c.xPodDirection.set(
                        GoBildaPinpointDriver.EncoderDirection.FORWARD);
                c.yPodDirection.set(
                        GoBildaPinpointDriver.EncoderDirection.FORWARD);

                c.globalDistanceUnit.set(DistanceUnit.INCH);
                c.offsetUnits.set(DistanceUnit.INCH);
            });

    // Foresight algorithm configuration

    public static ForesightConfig foresightConfig =
            new ForesightConfig(c -> {
                c.brakeAggression.set(1.0);
                c.maxBrakingPower.set(0.2);
                c.maxAccelerationConstraint.set(
                        ForesightConfig.Constraint.NONE);
                c.maxVelocityConstraint.set(
                        ForesightConfig.Constraint.NONE);
                c.maxDecelerationConstraint.set(
                        ForesightConfig.Constraint.NONE);
                c.coastDownToVelocity.set(0.0);
            });
//hi

    // Create and return the follower
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}
