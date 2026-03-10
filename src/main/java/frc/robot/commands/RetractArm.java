package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Hopper;
import frc.robot.Constants.ArmConstants;





public class RetractArm extends Command {

    private final Arm arm;
    private final Hopper hopper;

    public RetractArm(Arm arm, Hopper hopper) {
        this.arm = arm;
        this.hopper = hopper;
        addRequirements(arm, hopper); 
       
    }
@Override
public void initialize() {

 // Open the hopper to allow the arm to retract

    }

@Override
public void execute() {
    
    if (arm.ArmPosition() > ArmConstants.ARM_RETRACTED_POSITION) {
        arm.moveArmToPosition(ArmConstants.ARM_RETRACTED_POSITION);
    }
}

public void end(boolean interrupted) {
    hopper.closeHopper(); // Close the hopper after retracting the arm      

}
}

