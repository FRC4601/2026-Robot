package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.Timer; 

// Ejects fuel out through the intake arm

public class Eject extends Command {

    private final Agitator agitator;
    private final Intake intake;
    private final double wheelspeed;
    private final double end;
    private final Timer ejectTimer;

    public Eject(Agitator agitator, Intake intake, double wheelspeed, double end) {
        this.agitator = agitator;
        this.intake = intake;
        this.wheelspeed = wheelspeed;
        this.end = end;
        this.ejectTimer = new Timer();
        addRequirements(agitator, intake);
    }

    public void initialize() {
        ejectTimer.start();
    }

    @Override
    public void execute() {
        intake.runIntake(wheelspeed);
        agitator.setAgitatorSpeed(wheelspeed);
    }

    @Override
    public void end(boolean interrupted) {
        agitator.stopAgitator();
        intake.stopIntake();
    }

    @Override
    public boolean isFinished() {
        return ejectTimer.hasElapsed(end);
    }
}
