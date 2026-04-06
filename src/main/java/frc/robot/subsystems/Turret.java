// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.TurretConstants;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;




public class Turret extends SubsystemBase {


  private final SparkMax turretMotor;
  private final SparkMaxConfig turretConfig;
  private final RelativeEncoder encoder;
  private final PIDController turretPIDController;


  private static final double GEAR_RATIO = 81;
  private static final double MOTOR_ROTATIONS_PER_DEGREE = GEAR_RATIO / 360.0;
  private static final double ALIGNMENT_TOLERANCE_DEGREES = 2.0; // Adjust this tolerance as needed
  private double targetAngleDegrees; // Stores the current target angle for alignment checks

  /** Creates a new Turret. */
  public Turret() {
    turretMotor = new SparkMax(TurretConstants.TURRET_MOTOR_ID, MotorType.kBrushless);
    turretConfig = new SparkMaxConfig();
    turretConfig.smartCurrentLimit(30);
    turretConfig.secondaryCurrentLimit(40);

    turretConfig.idleMode(IdleMode.kBrake);
    turretMotor.configure(turretConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
    encoder = turretMotor.getEncoder();
    encoder.setPosition(0); // Reset encoder position to 0 at startup (assuming the turret starts at the home position when powered on)

    // Initialize the PID controller with some example constants (these will need to be tuned)
    turretPIDController = new PIDController(TurretConstants.kp, TurretConstants.ki, TurretConstants.kd); // Replace with actual PID constants
    turretPIDController.setTolerance(1.0); // Set an appropriate tolerance for the turret's position
  }

  //Method to auto align the turret to be facing the april tag target. This will be used in the AimAndSetSpeed command.
  public void trackTarget(double tx) {
    double output = turretPIDController.calculate(tx, 0.0);
    output = MathUtil.clamp(output, -0.3, 0.3);
    rotate(-output);
}


  public void setTargetAngleDegrees(double targetAngleDegrees) {
  double output = turretPIDController.calculate(getAngleDegrees(), targetAngleDegrees);


  output = MathUtil.clamp(output, -0.5, 0.5);   
  
  rotate(output);


}

    // Method to get the current turret angle in degrees by converting the encoder's motor rotations using the gear ratio.
    public double getAngleDegrees() {
    double currentPositionRotations = encoder.getPosition();
    return currentPositionRotations / MOTOR_ROTATIONS_PER_DEGREE;
  }

    public boolean isAligned(double tx) {
        return Math.abs(tx) < ALIGNMENT_TOLERANCE_DEGREES;
    }


     /**
     * Stop the turret motor immediately.
     */
    public void stop() {
        turretMotor.set(0);
    }

    public void rotate(double speed) {
      if (speed > 0 && getAngleDegrees() >= TurretConstants.MAX_ANGLE_DEGREES) {
        turretMotor.set(0);
        return;
      }
      else if (speed < 0 && getAngleDegrees() <= TurretConstants.MIN_ANGLE_DEGREES) {
        turretMotor.set(0);
        return;
      }
      turretMotor.set(0.5*speed);
    }

    /**
     * Zero the encoder — call this if turret is at a known home position.
     */
    public void resetEncoder() {
        encoder.setPosition(0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Turret Angle ", getAngleDegrees());
        SmartDashboard.putNumber("Turret Target Angle", targetAngleDegrees);
        
;
    }

  @Override
    public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
