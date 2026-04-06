package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.Stager;
import edu.wpi.first.wpilibj.Timer; 

// Runs the stager.

public class RunStager extends Command {

    private final Stager stager;
    private final double wheelspeed;
    private final double end;
    private final Timer stagerTimer;

    public RunStager(Stager stager, double wheelspeed, double end) {
        this.stager = stager;
        this.wheelspeed = wheelspeed;
        this.end = end;
        this.stagerTimer = new Timer();
        addRequirements(stager);
    }

    @Override
    public void initialize() {
        stagerTimer.reset();
        stagerTimer.start();
    }

    @Override
    public void execute() {
        stager.setStagerSpeed(wheelspeed);
    }

    @Override
    public void end(boolean interrupted) {
        stager.stopStager();
    }

    @Override
    public boolean isFinished() {
        return stagerTimer.hasElapsed(end);
    }
}

