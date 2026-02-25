package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.Constants.StagerConstants;

public class Stager extends SubsystemBase {

  private final SparkMax stagerMotor;
  private final SparkMaxConfig stagerConfig;
  
  public Stager() {

    stagerMotor = new SparkMax(StagerConstants.STAGER_MOTOR_ID, MotorType.kBrushless);
    stagerConfig = new SparkMaxConfig();
    stagerConfig.idleMode(IdleMode.kBrake);


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


  public void updateDashboard() {
      SmartDashboard.putNumber("Stager Speed", stagerMotor.get());
  }
}
