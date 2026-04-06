package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Agitator;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Stager;
import frc.robot.subsystems.Turret;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;


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

public class AimLL extends Command {

    private final Turret turret;
    private final Shooter shooter;
    private final Vision vision;
    private final Arm arm;
    private final Timer aimTimer;
    private final int start_ms;
    private final double end;
    





    public AimLL(Turret turret, Shooter shooter, Vision vision,Arm arm, int start, double end) {
        this.turret = turret;
        this.shooter = shooter;
        this.vision = vision;
        this.arm = arm;
        this.start_ms = start;
        this.end = end;
        aimTimer = new Timer();
    
        
        

        addRequirements(turret, shooter); // Declare subsystem dependencies
    
}
@Override
public void initialize() {
        // Ensure we're on the right Limelight pipeline for April Tags
        //vision.setPipeline(Vision.PIPELINE_APRILTAG); // TODO: Set to your April Tag pipeline index
        
        //arm.startOscillate();

        aimTimer.reset();
        aimTimer.start();

       


    //arm.moveArmToPosition(90);

    }

@Override
public void execute() {

        if (!vision.hasTarget()) {
            // No target visible — hold current turret position, keep shooter spinning
            // at last known speed so we're ready when target reappears
            return;
        }

        else{
            // Target is visible — read tx and ty for aiming and speed control

            double tx = vision.getTx();
            //double ty = vision.getTy();

        // --- Turret alignment ---
        // tx is the horizontal error. We drive the turret proportionally to close that error.
        // Positive tx = target is to the right = turret needs to rotate right = positive delta

            turret.trackTarget(tx);

                    // --- Shooter speed ---
        // Convert ty to distance, then look up the correct RPM

            //double distance = Shooter.tyToDistance(ty);
            //shooter.setVelocityFromDistance(distance);

            //arm.oscillate();

        }

        updateDashboard();

    }

    public boolean isReadyToShoot() {
        
        return turret.isAligned(vision.getTx()) && shooter.isAtSpeed();
    }

        @Override
        public void end(boolean interrupted) {
            turret.stop();
            
        }

    public boolean isFinished() {
        return aimTimer.hasElapsed(end);
    }
    
    public void updateDashboard() {

        SmartDashboard.putBoolean("Ready To Shoot", isReadyToShoot());
        SmartDashboard.putBoolean("Turret Aligned ", turret.isAligned(vision.getTx()));    
        SmartDashboard.putNumber("Timer", aimTimer.get());

    }   


}


