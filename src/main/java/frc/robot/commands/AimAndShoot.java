package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Agitator;
import frc.robot.subsystems.Stager;


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

public class AimAndShoot extends Command {

    private final Turret turret;
    private final Shooter shooter;
    private final Arm arm;
    private final double rpm;
    private final double angle;
    private final Timer timer;
    private final Agitator agitator;
    private final Stager stager;
    private final CommandSwerveDrivetrain drivetrain;
    private final double feedspeed;



    public AimAndShoot(Agitator agitator,Shooter shooter,Stager stager, CommandSwerveDrivetrain drivetrain,Arm arm,double rpm,double feedspeed,Turret turret,double angle) {
        this.agitator = agitator;
        this.turret = turret;
        this.shooter = shooter;
        this.stager = stager;
        this.arm = arm;
        this.rpm = rpm;
        this.feedspeed = feedspeed;
        this.angle = angle;
        this.drivetrain = drivetrain;
        timer = new Timer();

        addRequirements(turret, shooter); // Declare subsystem dependencies
    
}
@Override
public void initialize() {

    agitator.startTimer();
    arm.startOscillate();

    timer.reset();
    timer.start();


    arm.moveArmToPosition(90);

    }

@Override
public void execute() {     

    turret.setTargetAngleDegrees(angle);

    shooter.setVelocity(rpm);
        
    stager.setStagerSpeed(-feedspeed);
    agitator.setAgitatorSpeed(-feedspeed);
        
    //agitator.feedPeriodic(); method to run agitator and unjam if necessary

    if (timer.hasElapsed(1)){
        arm.oscillate();
    }




        }


        @Override
        public void end(boolean interrupted) {
            turret.stop();
            shooter.stopShooter();
            stager.stopStager();
            agitator.stopAgitator();
            arm.moveArmToPosition(80);
        }

    public boolean isFinished() {
        return false; // Runs until interrupted
    }
    
    public void updateDashboard() {

  

    }   


}


