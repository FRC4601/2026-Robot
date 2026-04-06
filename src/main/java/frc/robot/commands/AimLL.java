package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;


// Reads the limelight to aim

public class AimLL extends Command {

    private final Turret turret;
    private final Shooter shooter;
    private final Vision vision;
    private final Timer aimTimer;
    private final double end;


    public AimLL(Turret turret, Shooter shooter, Vision vision, double end) {

        this.turret = turret;
        this.shooter = shooter;
        this.vision = vision;
        this.end = end;
        aimTimer = new Timer();

        addRequirements(turret, shooter, vision); // Declare subsystem dependencies
    
    }

    @Override
    public void initialize() {
        aimTimer.reset();
        aimTimer.start();
    }

    @Override
    public void execute() {

        if (!vision.hasTarget()) {
            // If you can't find a target, do nothing
            return;
        } else{
            double tx = vision.getTx();
            turret.trackTarget(tx); // no ty needed cause we only aim left and right
        }

        updateDashboard();

    }

    public boolean isReadyToShoot() {
        return turret.isAligned(vision.getTx()) && shooter.isAtSpeed();
    }

    @Override
    public void end(boolean interrupted) {
        turret.stop();        
    }

    public boolean isFinished() {
        // once [end] seconds have happened, stop the command
        return aimTimer.hasElapsed(end);
    }
    
    public void updateDashboard() {
        SmartDashboard.putBoolean("Ready To Shoot", isReadyToShoot());
        SmartDashboard.putBoolean("Turret Aligned ", turret.isAligned(vision.getTx()));    
        SmartDashboard.putNumber("Timer", aimTimer.get());
    }   

}


