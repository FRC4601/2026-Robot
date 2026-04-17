package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
import frc.robot.Constants.ArmConstants;
import edu.wpi.first.wpilibj.Timer;
import java.lang.Math;


public class PositionArm extends Command {

    private final Arm arm;
    private final Hopper hopper;
    private final Intake intake;
    private final double targetPosition;
    private final Timer armTimer;
    private final double end;

    public PositionArm(Arm arm, Hopper hopper, Intake intake, double targetPosition, double end) {
        this.arm = arm;
        this.hopper = hopper;
        this.intake = intake;
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
        
        
        if (!armTimer.hasElapsed(1.0) && targetPosition < 70) {
            intake.runIntake(0.5);
        } else {
            intake.stopIntake();
        }
    }

    @Override
    public boolean isFinished() {
        return (armTimer.hasElapsed(end));
                    // || (Math.abs(arm.ArmPosition() - targetPosition) <= 1);
    }

    public void end(boolean interrupted) {
        arm.stopArm();
        intake.stopIntake();
    }

}