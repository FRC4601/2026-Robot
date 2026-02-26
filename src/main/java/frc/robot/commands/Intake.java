package frc.robot.commands;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Stager;



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
