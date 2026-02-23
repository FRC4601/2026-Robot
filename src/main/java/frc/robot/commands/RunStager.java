package frc.robot.commands;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
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

public class RunStager extends Command {

    private final Stager stager;
    private final Supplier<Boolean> readyToShoot;

    public RunStager(Stager stager, Supplier<Boolean> readyToShoot) {
        this.stager = stager;
        this.readyToShoot = readyToShoot;
        addRequirements(stager); // Declare subsystem dependencies
    }
@Override
public void initialize() {

    }

@Override
public void execute() {
    if (readyToShoot.get()) {
        stager.setStagerSpeed(0.5); // Set to desired staging speed (tune as needed)
    } else {
        stager.setStagerSpeed(0.0); // Stop stager if not ready to shoot
    }
    }
}

