package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Agitator;
import java.util.function.BooleanSupplier;
import frc.robot.subsystems.Intake;

/**
 * Runs the agitator whenever the robot is aligned and ready to shoot.
 * Mirrors the RunStager pattern exactly so both can be triggered together.
 *
 * Ball flow when ready: Agitator (hopper) → Stager → Shooter
 */
public class Eject extends Command {

    private final Agitator m_agitator;
    private final Intake m_intake;
    private final double m_wheelspeed;


    public Eject(Agitator agitator,Intake intake,double wheelspeed) {
        m_agitator = agitator;
        m_intake = intake;
        m_wheelspeed = wheelspeed;
        addRequirements(agitator);
    }

    @Override
    public void execute() {
            m_agitator.setAgitatorSpeed(m_wheelspeed); // Set to desired agitator speed (tune as needed);
            m_intake.runIntake(-m_wheelspeed);
    }

    @Override
    public void end(boolean interrupted) {
        m_agitator.stopAgitator();
        m_intake.stopIntake();  
    }

    // Command runs until the button is released (whileTrue handles this)
    @Override
    public boolean isFinished() {
        return false;
    }
}
