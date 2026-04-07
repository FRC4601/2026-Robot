package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.Timer;

// Runs the intake

public class IntakeCommand extends Command {

    private final Intake intake;
    private final double end;
    private final Timer intakeTimer;

    public IntakeCommand(Intake intake, double end) {
        this.intake = intake;
        this.end = end;
        this.intakeTimer = new Timer();
    }

    public void initialize() {
        intakeTimer.reset();
        intakeTimer.start();
    }
    
    @Override
    public void execute() {
        intake.runIntake(1);
    }

    public boolean isFinished() {
        return intakeTimer.hasElapsed(end);
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopIntake();
    }

}
