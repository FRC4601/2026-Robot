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


  private static final double GEAR_RATIO = 100.0;
  private static final double MOTOR_ROTATIONS_PER_DEGREE = GEAR_RATIO / 360.0;
  private static final double ALIGNMENT_TOLERANCE_DEGREES = 2.0; // Adjust this tolerance as needed
  private double targetAngleDegrees; // Stores the current target angle for alignment checks

  /** Creates a new Turret. */
  public Turret() {
    turretMotor = new SparkMax(TurretConstants.TURRET_MOTOR_ID, MotorType.kBrushless);
    turretConfig = new SparkMaxConfig();
    turretConfig.idleMode(IdleMode.kBrake);
    turretMotor.configure(turretConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
    encoder = turretMotor.getEncoder();
    encoder.setPosition(0); // Reset encoder position to 0 at startup (assuming the turret starts at the home position when powered on)

    // Initialize the PID controller with some example constants (these will need to be tuned)
    turretPIDController = new PIDController(TurretConstants.kp, TurretConstants.ki, TurretConstants.kd); // Replace with actual PID constants
    turretPIDController.setTolerance(1.0); // Set an appropriate tolerance for the turret's position
  }

  //Method to auto align the turret to a specific angle in degrees. This will be used in the auto-alignment command that uses the limelight's tx offset to compute the target angle.

  public void setTurretAngle(double degrees) {
    targetAngleDegrees = degrees;
    //the encoder reads in motor rotations, but we want to control the turret in degrees, 
    //so we need to convert the target angle from degrees to motor rotations using the gear ratio.
    double targetPositionRotations = targetAngleDegrees * MOTOR_ROTATIONS_PER_DEGREE;
    double currentPositionRotations = encoder.getPosition();
    double output = turretPIDController.calculate(currentPositionRotations, targetPositionRotations);
    output = MathUtil.clamp(output, -0.5, 0.5) ; // Clamp the output to prevent excessive speed. Adjust these limits as necessary.
    turretMotor.set(output);
  }

   /**
     * Adjust turret angle by a delta (e.g. from limelight tx offset).
     * Useful for continuous tracking without computing absolute angle.
     */

     //Instead of setting the turret to an absolute angle, 
     //this method allows us to adjust the current target angle by a certain delta.
     // This is useful for continuous tracking based on the limelight's tx offset, where we want to keep adjusting the turret angle as the target moves, rather than setting it to a fixed position.
    
   public void adjustAngle(double deltaDegrees) {
        setTurretAngle(getAngleDegrees() + deltaDegrees);
    }


    // Method to get the current turret angle in degrees by converting the encoder's motor rotations using the gear ratio.
    public double getAngleDegrees() {
    double currentPositionRotations = encoder.getPosition();
    return currentPositionRotations / MOTOR_ROTATIONS_PER_DEGREE;
  }

    public boolean isAligned() {
        return Math.abs(targetAngleDegrees - getAngleDegrees()) < ALIGNMENT_TOLERANCE_DEGREES;
    }


     /**
     * Stop the turret motor immediately.
     */
    public void stop() {
        turretMotor.stopMotor();
    }

    /**
     * Zero the encoder — call this if turret is at a known home position.
     */
    public void resetEncoder() {
        encoder.setPosition(0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Turret/AngleDegrees", getAngleDegrees());
        SmartDashboard.putNumber("Turret/TargetDegrees", targetAngleDegrees);
        SmartDashboard.putBoolean("Turret/IsAligned", isAligned());
    }

  @Override
    public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
