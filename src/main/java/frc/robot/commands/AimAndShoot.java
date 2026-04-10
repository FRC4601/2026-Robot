package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.Timer;


// Abandoned. Split into seperate aimLL and shootLL commands.
// Reads the limelight to aim and calculate shooting rpm

public class AimAndShoot extends Command {

    private final Agitator agitator;
    private final Shooter shooter;
    private final Stager stager;
    private final Arm arm;
    private final Turret turret;

    private final double rpm;
    private final double wheelspeed;
    private final double angle;
    private final Timer timer;

    public AimAndShoot(Agitator agitator, Shooter shooter, Stager stager, Arm arm, Turret turret, double rpm, double wheelspeed, double angle) {
        this.agitator = agitator;
        this.shooter = shooter;
        this.stager = stager;
        this.arm = arm;
        this.turret = turret;

        this.rpm = rpm;
        this.wheelspeed = wheelspeed;
        this.angle = angle;
        timer = new Timer();

        addRequirements(turret, shooter, stager, agitator); // Declare subsystem dependencies 
    }

    @Override
    public void initialize() {

        agitator.startTimer();
        arm.startOscillate();

        timer.reset();
        timer.start();

        arm.moveArmToPosition(90);

    }

    @Override
    public void execute() {     

        turret.setTargetAngleDegrees(angle);

        shooter.setVelocity(rpm);
            
        stager.setStagerSpeed(wheelspeed);
        agitator.setAgitatorSpeed(wheelspeed);
            
        //agitator.feedPeriodic(); method to run agitator and unjam if necessary

        if (timer.hasElapsed(1)){
            arm.oscillate();
        }

    }


    @Override
    public void end(boolean interrupted) {
        turret.stop();
        shooter.stopShooter();
        stager.stopStager();
        agitator.stopAgitator();
        arm.moveArmToPosition(80);
    }

}


