package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Agitator;
import edu.wpi.first.wpilibj.Timer; 


public class RunAgitator extends Command {

    private final Agitator m_agitator;
    private final double m_wheelspeed;
    private final Timer agitatorTimer;
    private final double m_end;


    public RunAgitator(Agitator agitator,double wheelspeed, double end) {
        m_agitator = agitator;
        m_wheelspeed = wheelspeed;
        m_end = end;
        agitatorTimer = new Timer();
        addRequirements(agitator);
    }

    public void initialize() {
        agitatorTimer.reset();
        agitatorTimer.start();
    }

    @Override
    public void execute() {
        m_agitator.setAgitatorSpeed(m_wheelspeed);
    }

    @Override
    public void end(boolean interrupted) {
        m_agitator.stopAgitator();
        
    }

    // Command runs until the button is released (whileTrue handles this)
    @Override
    public boolean isFinished() {
        return agitatorTimer.hasElapsed(m_end);
    }
}
