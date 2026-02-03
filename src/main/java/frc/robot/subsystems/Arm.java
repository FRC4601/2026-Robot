package frc.robot.subsystems;

//import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import edu.wpi.first.wpilibj.DigitalInput; (used for bottom limit on 2025's elevator)
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
//import com.revrobotics.spark.SparkClosedLoopController;
//import com.revrobotics.spark.ClosedLoopSlot;
//import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.ResetMode;
//import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
//import edu.wpi.first.math.controller.PIDController; (please don't do this the math is way too complex)

public class Arm extends SubsystemBase {

  private final SparkMax armMotor;
  private final SparkMaxConfig armConfig;
  
  public Arm() {

    armMotor = new SparkMax(OperatorConstants.0, MotorType.kBrushless); // 0 is a placeholder since we don't know the true ID
    armConfig = new SparkMaxConfig();
    armConfig.idleMode(idleMode.kBrake);
      //.encoder.positionConversionFactor(2.2*OperatorConstants.inchesPerMotorRotation);
      // Why do we have a conversion factor here when the idleMode command doesn't require any actual measurements?

    armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
  }

  @Override
  public void periodic() {
    updateDashboard();
  }

  public double getArmAngle() {
    return armMotor.getEncoder().getPosition();
      // we might have to add a conversion factor to this for a more useful measurement
  }

  public void setArmSpeed(double speed) {
    armMotor.set(speed);
  }

  public void stopArm() {
    armMotor.set(0);
  }

  public void updateDashboard() {
    SmartDashboard.putNumber("Arm Rotation", getArmAngle());
    SmartDashboard.putNumber("Arm Speed", leaderMotor.get());
  }
}
