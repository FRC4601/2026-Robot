package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.Timer;


// Shoots at a specific rate (NO limelight)

public class Shoot extends Command {

    private final Agitator agitator;
    private final Shooter shooter;
    private final Stager stager;  
    private final Arm arm;

    private final double rpm;
    private final double wheelspeed;
    private final double end;
    private final Timer oscillatingTimer;
    private final Timer shootTimer;

    public Shoot(Agitator agitator, Shooter shooter, Stager stager, Arm arm, double rpm, double wheelspeed, double end) {
        
        this.agitator = agitator;
        this.shooter = shooter;
        this.stager = stager;
        this.arm = arm;

        this.rpm = rpm;
        this.end = end;
        this.wheelspeed = wheelspeed;
        oscillatingTimer = new Timer();
        shootTimer = new Timer();

        addRequirements(agitator, shooter, stager); // Declare subsystem dependencies
    
    }

    @Override
    public void initialize() {

        agitator.startTimer();
        arm.startOscillate();

        oscillatingTimer.reset();
        oscillatingTimer.start();
        shootTimer.reset();
        shootTimer.start();

        //arm.moveArmToPosition(90);

    }

    @Override
    public void execute() {
        
        // We had code to automatically unjam it for 1.5 seconds every 5 seconds,
        // but our shooter got better so we don't need it anymore!
        /*if (shootTimer.get() % 5 < 1.5) {
            agitator.setAgitatorSpeed(-wheelspeed);
        } else {*/
            agitator.setAgitatorSpeed(wheelspeed);
            shooter.setVelocity(rpm);
            stager.setStagerSpeed(0.65*wheelspeed);
        //}
    
        // moves arm back and forth to help with feeding
        /*if (oscillatingTimer.hasElapsed(3)){
            arm.oscillate();
        }*/

    }

    public boolean isFinished() {
        return shootTimer.hasElapsed(end);
    }

    public void end(boolean interrupted) {
        shooter.stopShooter();
        stager.stopStager();
        agitator.stopAgitator();
        //arm.moveArmToPosition(80);
    }

}

