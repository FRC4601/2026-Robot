package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
import frc.robot.Constants.*;
import edu.wpi.first.wpilibj.Timer;


// Shoots at a specific rate (NO limelight)

public class Shoot extends Command {

    private final Agitator agitator;
    private final Shooter shooter;
    private final Stager stager;  
    private final Arm arm;
    private final CANRange canRange;

    private final double rpm;
    private final double wheelspeed;
    private final double end;
    //private final Timer oscillatingTimer;
    private final Timer shootTimer;
    public final Timer canRangeTimer;
    public boolean needToUnjam;

    public Shoot(Agitator agitator, Shooter shooter, Stager stager, Arm arm, CANRange canRange,
                        double rpm, double wheelspeed, double end) {
        
        this.agitator = agitator;
        this.shooter = shooter;
        this.stager = stager;
        this.arm = arm;
        this.canRange = canRange;

        this.rpm = rpm;
        this.end = end;
        this.wheelspeed = wheelspeed;
        //oscillatingTimer = new Timer();
        shootTimer = new Timer();
        canRangeTimer = new Timer();
        needToUnjam = false;

        addRequirements(agitator, shooter, stager, canRange); // Declare subsystem dependencies
    
    }

    @Override
    public void initialize() {

        agitator.startTimer();
        arm.startOscillate();

        //oscillatingTimer.reset();
        //oscillatingTimer.start();
        shootTimer.reset();
        shootTimer.start();
        canRangeTimer.reset();
        canRangeTimer.start();

        //arm.moveArmToPosition(90);

    }

    @Override
    public void execute() {
        
        // We had code to automatically unjam it for 1.5 seconds every 5 seconds,
        // but our shooter got better so we don't need it anymore!
        /*if (shootTimer.get() % 5 < 1.5) {
            agitator.setAgitatorSpeed(-wheelspeed);
        } else {
            agitator.setAgitatorSpeed(wheelspeed);
            shooter.setVelocity(rpm);
            stager.setStagerSpeed(0.65*wheelspeed);
        }*/
    
        // moves arm back and forth to help with feeding
        /*if (oscillatingTimer.hasElapsed(3)){
            arm.oscillate();
        }*/

       // if (canRange.detectsFuel()) {
           // needToUnjam = false;
           // canRangeTimer.reset();
           // canRangeTimer.start();
       // } else if (needToUnjam == false && canRangeTimer.hasElapsed(CANRangeConstants.LONG_TIME)) {
            //needToUnjam = true;
           // canRangeTimer.reset();
           // canRangeTimer.start();
        //} else if (needToUnjam == true && canRangeTimer.hasElapsed(CANRangeConstants.UNJAM_TIME)) {
          //  needToUnjam = false;
          //  canRangeTimer.reset();
          //  canRangeTimer.start();
       // }

       // if (needToUnjam) {
        //    agitator.setAgitatorSpeed(-wheelspeed);
        //    stager.stopStager();
       // } else {
            if (shootTimer.hasElapsed(0.5)) {
                agitator.setAgitatorSpeed(wheelspeed);
                stager.setStagerSpeed(0.65*wheelspeed);
            }
            shooter.setVelocity(rpm);
       // }
        

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

