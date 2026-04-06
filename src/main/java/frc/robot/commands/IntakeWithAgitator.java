package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.Timer; 

// Abandoned. Runs the intake and agitator together

public class IntakeWithAgitator extends Command {

    private final Agitator agitator;
    private final Intake intake;
    private final double wheelspeed;
    private final double end;
    private final Timer timer;
    public IntakeWithAgitator(Agitator agitator, Intake intake, double wheelspeed, double end) {
        this.agitator = agitator;
        this.intake = intake;
        this.wheelspeed = wheelspeed;
        this.end = end;
        this.timer = new Timer();
        addRequirements(agitator, intake);
    }

    public void initialize() {
        timer.start();
    }

    @Override
    public void execute() {
        intake.runIntake(wheelspeed);
        agitator.setAgitatorSpeed(0.5 * wheelspeed);
    }

    @Override
    public void end(boolean interrupted) {
        agitator.stopAgitator();
        intake.stopIntake();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(end);
    }
}
