package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Agitator;
import frc.robot.subsystems.Arm;
import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj.Timer; 


public class Eject extends Command {

    private final Agitator m_agitator;
    private final Arm m_arm;
    private final double end;
    private final Timer ejectTimer;


    public Eject(Agitator agitator,Arm arm, double end) {
        m_agitator = agitator;
        m_arm = arm;
        this.end = end;
        ejectTimer = new Timer();
        addRequirements(agitator, arm);
    }

    @Override
    public void initialize() {
        //m_arm.moveArmToPosition(140);
        ejectTimer.reset();
        ejectTimer.start();
    }

    @Override
    public void execute() {
        m_agitator.setAgitatorSpeed(1);
    }

    @Override
    public void end(boolean interrupted) {
        m_agitator.stopAgitator();
    }

    // Command runs until the button is released (whileTrue handles this)
    @Override
    public boolean isFinished() {
        return ejectTimer.hasElapsed(end);
    }
}
