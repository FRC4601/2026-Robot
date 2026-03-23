package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Hopper;
import frc.robot.Constants.ArmConstants;





public class PositionArm extends Command {

    private final Arm arm;
    private final Hopper hopper;
    private final double targetPosition;

    public PositionArm(Arm arm, Hopper hopper, double targetPosition) {
        this.arm = arm;
        this.hopper = hopper;
        this.targetPosition = targetPosition;
        addRequirements(arm, hopper); 
       
    }
@Override
public void initialize() {

    hopper.openHopper(); // Open the hopper to allow the arm to extend

    }

@Override
public void execute() {
    
    arm.moveArmToPosition(targetPosition);
    
}

@Override
public void end(boolean interrupted) {
     arm.isAtSetpoint(); // Stop the arm once it reaches the target position
}
}

