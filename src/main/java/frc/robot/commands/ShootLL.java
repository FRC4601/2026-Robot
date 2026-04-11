package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
import frc.robot.Constants.*;
//import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer; 

// Reads the limelight to determine shooting rpm

public class ShootLL extends Command {

    private final Agitator agitator;
    private final Shooter shooter;
    private final Stager stager;    
    //private final CommandSwerveDrivetrain drivetrain;
    private final Vision vision;
    private final CANRange canRange;

    private final double end;
    private final double wheelspeed; 
    //private final Timer oscillatingTimer;
    public final Timer shootTimer;
    public final Timer canRangeTimer;
    public boolean needToUnjam;

    public ShootLL(Agitator agitator, Shooter shooter, Stager stager, CommandSwerveDrivetrain drivetrain,
                    Vision vision, CANRange canRange, double end, double wheelspeed) {

        this.agitator = agitator;
        this.shooter = shooter;
        this.stager = stager;
        //this.drivetrain = drivetrain;
        this.vision = vision;
        this.canRange = canRange;

        this.end = end;
        this.wheelspeed = wheelspeed;
        //this.oscillatingTimer = new Timer();
        this.shootTimer = new Timer();
        this.canRangeTimer = new Timer();
        this.needToUnjam = false;

        addRequirements(agitator, shooter, stager, vision, canRange); 
    
    }

    @Override
    public void initialize() {

        agitator.startTimer();

        //arm.startOscillate();
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

        /*Pose2d pose = drivetrain.getState().Pose;
        double x = pose.getX();
        double y = pose.getY(); // Get the robot's current position on the field, which can be used for distance-based adjustments to shooting
        double rotation = pose.getRotation().getDegrees(); // Get the robot's current rotation in degrees relative to the field, can be used to aim turret*/

        
        if (!vision.hasTarget()) {
            // If you can't find a target, do nothing
            return;
        } else{

        double ty = vision.getTy();
        double distance = shooter.tyToDistance(ty);

            agitator.setAgitatorSpeed(wheelspeed);
            shooter.setVelocityFromDistance(distance);
            stager.setStagerSpeed(0.65*wheelspeed);
        }

        // We had code to automatically unjam it for 1.5 seconds every 5 seconds,
        // but our shooter got better so we don't need it anymore!
        /*if (shootTimer.get() % 5 < 1.5) {
            agitator.setAgitatorSpeed(-wheelspeed);
        } else {
            agitator.setAgitatorSpeed(wheelspeed);
            shooter.setVelocityFromDistance(distance);
            stager.setStagerSpeed(0.65*wheelspeed);
        }*/

        // this logic feels unnecessarily complex... but it works (i think)

        
        //if (canRange.detectsFuel()) {
           // needToUnjam = false;
           // canRangeTimer.reset();
        //} else if ((canRangeTimer.get() > CANRangeConstants.LONG_TIME) && needToUnjam == false) {
          //  needToUnjam = true;
          //  canRangeTimer.reset();
       // } else if ((canRangeTimer.get() > CANRangeConstants.UNJAM_TIME) && needToUnjam == true) {
         //   needToUnjam = false;
          //  canRangeTimer.reset();
        //}

        //if (needToUnjam) {
        //    agitator.setAgitatorSpeed(-wheelspeed);
        //} else {

        //}
    //}
        
        
        //agitator.feedPeriodic(); method to run agitator and unjam if necessary

        // moves arm back and forth to help with feeding
        /*if (oscillatingTimer.hasElapsed(1)){
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

