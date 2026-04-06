package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.Timer;

// Runs the intake

public class IntakeCommand extends Command {

    private final Intake intake;
    private final double end;
    private final Timer timer;

    public IntakeCommand(Intake intake, double end) {
        this.intake = intake;
        this.end = end;
        this.timer = new Timer();
    }
    
    @Override
    public void execute() {
            intake.runIntake(1);
    }

    public boolean isFinished() {
        return timer.hasElapsed(end);
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopIntake();
    }

}
