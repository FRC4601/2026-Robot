package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Arm; 
import edu.wpi.first.wpilibj.Timer; 


public class IntakeCommand extends Command {

    private final Intake m_intake;
    private final Arm m_arm;
    private final double end;
    private final Timer intakeTimer;


    public IntakeCommand(Intake intake,Arm arm, double end) {
        m_intake = intake;
        m_arm = arm;
        this.end = end;
        intakeTimer = new Timer();
        addRequirements(m_arm);
    }

    

    @Override
    public void initialize() {
        intakeTimer.start();
        m_arm.moveArmToPosition(140);
    }
    @Override
    public void execute() {
            m_intake.runIntake(1);
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stopIntake();
        intakeTimer.reset();
    }

    @Override
    public boolean isFinished() {
        return intakeTimer.hasElapsed(end);
    }
}
