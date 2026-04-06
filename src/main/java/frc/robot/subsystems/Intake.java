package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.Constants.IntakeConstants;


// Intakes balls from the floor into the hopper/agitator

public class Intake extends SubsystemBase {

  // Initializing the motor
  private final SparkMax intakeMotor;
  private final SparkMaxConfig intakeConfig;
  
  // The constructor--configures the motor
  public Intake() {

    intakeMotor = new SparkMax(IntakeConstants.INTAKE_MOTOR_ID, MotorType.kBrushless);
    intakeConfig = new SparkMaxConfig();
    intakeMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
  }

  @Override
  public void periodic() {}

  public void runIntake(double speed) {
    // The motor was mounted backwards so we have to invert the speed
    intakeMotor.set(-speed);
  }

  public void stopIntake() {
    intakeMotor.set(0);
  }

  public void updateDashboard() {}

}
