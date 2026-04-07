package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
import frc.robot.Constants.ArmConstants;
import edu.wpi.first.wpilibj.Timer;
import java.lang.Math;


public class PositionArm extends Command {

    private final Arm arm;
    private final Hopper hopper;
    private final double targetPosition;
    private final Timer armTimer;
    private final double end;

    public PositionArm(Arm arm, Hopper hopper, double targetPosition, double end) {
        this.arm = arm;
        this.hopper = hopper;
        this.targetPosition = targetPosition;
        this.end = end;
        armTimer = new Timer();
        addRequirements(arm, hopper); 
    }

    @Override
    public void initialize() {
        hopper.openHopper(); // Opens the hopper to allow the arm to extend
        armTimer.reset();
        armTimer.start();
    }

    @Override
    public void execute() {
        arm.moveArmToPosition(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return (armTimer.hasElapsed(end))
                    || (Math.abs(arm.ArmPosition() - targetPosition) <= 1);
    }

}