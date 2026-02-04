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

public class Intake extends SubsystemBase {

  private final SparkMax intakeMotor;
  private final SparkMaxConfig intakeConfig;
  
  public Intake() {

    intakeMotor = new SparkMax(0, MotorType.kBrushless); // 0 is just a placeholder
    intakeConfig = new SparkMaxConfig();
        //.encoder.positionConversionFactor(2.2*OperatorConstants.inchesPerMotorRotation);
        // ^ same issue as in the arm subsystem

    intakeMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void runIntake(double speed) {
    intakeMotor.set(speed);
    // might have to make this negative depending on how the motors are placed
  }

  public void stopIntake() {
    intakeMotor.set(0);
  }

  //public void updateDashboard() {}
  // ^ might create this in the future if we add more stuff, but as of now, we don't need it
  

}
