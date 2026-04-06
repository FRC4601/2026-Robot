package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Agitator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Arm;
import edu.wpi.first.wpilibj.Timer; 


public class IntakeWithAgitator extends Command {

    private final Agitator m_agitator;
    private final Intake m_intake;
    private final Arm m_arm;
    private final double m_wheelspeed;
    private final double m_end;
    private final Timer m_timer;

    public IntakeWithAgitator(Agitator agitator, Intake intake, Arm arm, double wheelspeed, double end) {
        m_agitator = agitator;
        m_intake = intake;
        m_arm = arm;
        m_wheelspeed = wheelspeed;
        m_end = end;
        m_timer = new Timer();
        addRequirements(agitator, intake);
    }

    public void initialize() {
        m_timer.start();
        m_arm.moveArmToPosition(140);
    }

    @Override
    public void execute() {
        m_intake.runIntake(m_wheelspeed);
        m_agitator.setAgitatorSpeed(-0.5*m_wheelspeed);
    }

    @Override
    public void end(boolean interrupted) {
        m_agitator.stopAgitator();
        m_intake.stopIntake();
    }

    @Override
    public boolean isFinished() {
        return m_timer.hasElapsed(m_end);
    }
}
