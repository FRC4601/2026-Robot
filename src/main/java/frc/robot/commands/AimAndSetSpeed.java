package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;


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

public class AimAndSetSpeed extends Command {

    private final Turret turret;
    private final Shooter shooter;
    private final Vision vision;

    // Proportional gain for turret alignment from tx
    // Output = kP_turret * tx (in degrees)
    // Tune this: too high = oscillation, too low = sluggish tracking
    private static final double kP_TURRET = 0.05;

    // Deadband: ignore tx error smaller than this (degrees)
    // Prevents micro-corrections from chattering the turret when it's close enough
    private static final double TX_DEADBAND = 0.5;


    public AimAndSetSpeed(Turret turret, Shooter shooter, Vision vision) {
        this.turret = turret;
        this.shooter = shooter;
        this.vision = vision;
        addRequirements(turret, shooter); // Declare subsystem dependencies
    
}
@Override
public void initialize() {
        // Ensure we're on the right Limelight pipeline for April Tags
        vision.setPipeline(0); // TODO: Set to your April Tag pipeline index
    }

@Override
public void execute() {
        if (!vision.hasTarget()) {
            // No target visible — hold current turret position, keep shooter spinning
            // at last known speed so we're ready when target reappears
            return;
        }

        double tx = vision.getTx();
        double ty = vision.getTy();

        // --- Turret alignment ---
        // tx is the horizontal error. We drive the turret proportionally to close that error.
        // Positive tx = target is to the right = turret needs to rotate right = positive delta
        if (Math.abs(tx) > TX_DEADBAND) {
            double turretDelta = kP_TURRET * tx;
            turret.adjustAngle(turretDelta);
        }

        // --- Shooter speed ---
        // Convert ty to distance, then look up the correct RPM
        double distance = Shooter.tyToDistance(ty);
        shooter.setVelocityFromDistance(distance);
    }

    public boolean isReadyToShoot() {
        // We can shoot when the turret is aligned and the shooter is at speed
        return turret.isAligned() && shooter.isAtSpeed() && vision.hasTarget();
    }

}

