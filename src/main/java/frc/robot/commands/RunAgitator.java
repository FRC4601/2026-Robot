package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Agitator;
import java.util.function.BooleanSupplier;

/**
 * Runs the agitator whenever the robot is aligned and ready to shoot.
 * Mirrors the RunStager pattern exactly so both can be triggered together.
 *
 * Ball flow when ready: Agitator (hopper) → Stager → Shooter
 */
public class RunAgitator extends Command {

    private final Agitator m_agitator;
    private final BooleanSupplier m_readyToShoot;
    private final double m_wheelspeed;

    /**
     * @param agitator       The agitator subsystem
     * @param readyToShoot   Supplier that returns true when turret is aligned
     *                       AND shooter is at speed (use aimAndSetSpeed::isReadyToShoot)
     */
    public RunAgitator(Agitator agitator, BooleanSupplier readyToShoot,double wheelspeed) {
        m_agitator = agitator;
        m_readyToShoot = readyToShoot;
        m_wheelspeed = wheelspeed;
        addRequirements(agitator);
    }

    @Override
    public void execute() {
        if (m_readyToShoot.getAsBoolean()) {
            m_agitator.setAgitatorSpeed(m_wheelspeed); // Set to desired agitator speed (tune as needed);
        } else {
            m_agitator.stopAgitator();
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_agitator.stopAgitator();
    }

    // Command runs until the button is released (whileTrue handles this)
    @Override
    public boolean isFinished() {
        return false;
    }
}
