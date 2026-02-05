package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.Constants.ArmConstants;

public class Stager extends SubsystemBase {

  private final SparkMax stagerMotor;
  private final SparkMaxConfig stagerConfig;
  
  public Stager() {

    stagerMotor = new SparkMax(0, MotorType.kBrushless); // 0 is a placeholder
    stagerConfig = new SparkMaxConfig();
    stagerConfig.idleMode(IdleMode.kBrake);
      //.encoder.positionConversionFactor(2.2*OperatorConstants.inchesPerMotorRotation);
      // Why do we have a conversion factor here when the idleMode command doesn't require any actual measurements?

    stagerMotor.configure(stagerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
  }

  @Override
  public void periodic() {
    updateDashboard();
  }

  public void setStagerSpeed(double speed) {
    stagerMotor.set(speed);
  }

  public void stopStager() {
    stagerMotor.set(0);
  }

  public boolean isStagerOn() {
    return stagerMotor.get() != 0;
  }

  public void updateDashboard() {
    SmartDashboard.putBoolean("Is Stager On?", isStagerOn());
  }
}
