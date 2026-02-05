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

public class Agitator extends SubsystemBase {

  private final SparkMax agitatorMotor;
  private final SparkMaxConfig agitatorConfig;
  
  public Agitator() {

    agitatorMotor = new SparkMax(0, MotorType.kBrushless); // 0 is a placeholder
    agitatorConfig = new SparkMaxConfig();
    agitatorConfig.idleMode(IdleMode.kBrake);
      //.encoder.positionConversionFactor(2.2*OperatorConstants.inchesPerMotorRotation);
      // Why do we have a conversion factor here when the idleMode command doesn't require any actual measurements?

    agitatorMotor.configure(agitatorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
  }

  @Override
  public void periodic() {
    updateDashboard();
  }

  public void setAgitatorSpeed(double speed) {
    agitatorMotor.set(speed);
  }

  public void stopAgitator() {
    agitatorMotor.set(0);
  }

  public boolean isAgitatorOn() {
    return agitatorMotor.get() != 0;
  }

  public void updateDashboard() {
    SmartDashboard.putBoolean("Is Agitator On?", isAgitatorOn());
  }
}
