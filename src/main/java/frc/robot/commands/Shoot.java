package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Agitator;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Stager;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.subsystems.Arm;
import edu.wpi.first.wpilibj.Timer; 
import frc.robot.Constants.AgitatorConstants;
import frc.robot.Constants.StagerConstants;


/**
 * AimAndShootCommand
 *
 * Continuously reads limelight data every loop cycle to:
 *   1. Align the turret horizontally using tx
 *   2. Spin up the shooter to the correct RPM using ty → distance → RPM lookup
 *
 * Because it re-reads tx and ty every loop, this command works while the robot
 * is moving — the turret will actively track the target and the shooter will
 * continuously adjust speed. This is the "on-the-fly shooting" behavior.
 *
 * The command never finishes on its own — it runs until the button is released
 * or it is interrupted. Wire up feed/indexer logic separately, gated on
 * isReadyToShoot() returning true.
 */

public class Shoot extends Command {

    private final Agitator agitator;
    private final Shooter shooter;
    private final Stager stager;    
    private final double rpm;
    // should rpm be a double? i think just in case but idrk. not that deep.
    private final CommandSwerveDrivetrain drivetrain;
    private final Arm arm;
    private final Timer oscillatingTimer;
    private final double feedspeed; // Speed for stager and agitator when feeding balls into the shooter, can be tuned



    




    public Shoot(Agitator agitator, Shooter shooter, Stager stager, CommandSwerveDrivetrain drivetrain,Arm arm,double rpm,double feedspeed, int start, double end) {
        this.agitator = agitator;
        this.shooter = shooter;
        this.stager = stager;
        this.rpm = rpm;
        this.drivetrain = drivetrain;
        this.arm = arm;
        oscillatingTimer = new Timer();
        addRequirements(agitator, shooter, stager); // Declare subsystem dependencies
    
}
@Override
public void initialize() {

    agitator.startTimer();
    arm.startOscillate();

    oscillatingTimer.reset();
    oscillatingTimer.start();

     arm.moveArmToPosition(90);

    }

@Override
public void execute() {

        Pose2d pose = drivetrain.getState().Pose;
        double x = pose.getX();
        double y = pose.getY(); // Get the robot's current position on the field, which can be used for distance-based adjustments to shooting
        double rotation = pose.getRotation().getDegrees(); // Get the robot's current rotation in degrees relative to the field, can be used to aim turret

        // if we give it an rpm it can't reach, what will happen?
        // it'll probably just max out at the closest possible rpm, but we should maybe test just in case
        shooter.setVelocity(rpm);
        stager.setStagerSpeed(-StagerConstants.STAGER_SPEED);
        agitator.setAgitatorSpeed(-AgitatorConstants.AGITATOR_SPEED);
        //agitator.feedPeriodic(); method to run agitator and unjam if necessary

        /*if (oscillatingTimer.hasElapsed(3)){
            arm.oscillate();
        }*/
         // Move the arm back and forth while shooting to help with feeding


         //Need to add a method in arm subsystem to move the arm back and forth while shooting to help with feeding */

    }

    public boolean isFinished() {
        return false;
    }

    public void end(boolean interrupted) {
        shooter.stopShooter();
        stager.stopStager();
        agitator.stopAgitator();
        arm.moveArmToPosition(80);
        
    }


}

