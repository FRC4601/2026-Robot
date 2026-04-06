package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.Timer; 

// Unjams the balls if stuck in the agitator

public class Unjam extends Command {

    private final Agitator agitator;
    private final double end;
    private final Timer unjamTimer;


    public Unjam(Agitator agitator, double end) {
        this.agitator = agitator;
        this.end = end;
        unjamTimer = new Timer();
        addRequirements(agitator);
    }

    @Override
    public void initialize() {
        unjamTimer.reset();
        unjamTimer.start();
    }

    @Override
    public void execute() {
        agitator.setAgitatorSpeed(-1);
    }

    @Override
    public void end(boolean interrupted) {
        agitator.stopAgitator();
    }

    @Override
    public boolean isFinished() {
        return unjamTimer.hasElapsed(end);
    }
}
