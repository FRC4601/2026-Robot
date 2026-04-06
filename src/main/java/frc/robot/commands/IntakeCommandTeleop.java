package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;


public class IntakeCommandTeleop extends Command {

    private final Intake m_intake;


    public IntakeCommandTeleop(Intake intake) {
        m_intake = intake;
    }

    @Override
    public void initialize() {
    }
    
    @Override
    public void execute() {
            m_intake.runIntake(1);
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stopIntake();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
