package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.Constants.ArmConstants;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;

public class Arm extends SubsystemBase {

  private final SparkMax armMotor;
  private final SparkMaxConfig armConfig;
  private final RelativeEncoder encoder;
  private final DigitalInput limitSwitch; // Assuming a limit switch is used for homing
  private final PIDController armPIDController;
  

  
  public Arm() {

     // Replace 0 with the actual DIO port number for the limit switch
    armMotor = new SparkMax(ArmConstants.ARM_MOTOR_ID, MotorType.kBrushless);
    armConfig = new SparkMaxConfig();
    armConfig.idleMode(IdleMode.kBrake);
    armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    encoder = armMotor.getEncoder();
    encoder.setPosition(0); // Reset encoder position to 0 at startup (assuming the arm starts at the home position when powered on)
    limitSwitch = new DigitalInput(0);


    // Initialize the PID controller with the constants from ArmConstants
    armPIDController = new PIDController(ArmConstants.kp, ArmConstants.ki, ArmConstants.kd);
    armPIDController.setTolerance(ArmConstants.tolerance);


    
  }

  @Override
  public void periodic() {
    //write the current arm position and speed to the dashboard for testing purposes. 
    updateDashboard();

      // Check if the limit switch is triggered to reset the encoder position
      //this prevents the encoder reading from drifting 
      
    if (limitSwitch.get()) {
      encoder.setPosition(0);
    }


  }



  public double getArmPosition() {
    return encoder.getPosition();
  }

  public void setArmSpeed(double speed) {
    if (limitSwitch.get() && speed < 0) {
      // If the limit switch is triggered and we're trying to move the arm down, stop the motor
      armMotor.set(0);
    } else {
      armMotor.set(speed);
    }

  }

  public void stopArm() {
    armMotor.set(0);
  }

  public void moveArmToPosition(double targetPosition) {
    double currentPosition = getArmPosition();
    double output = armPIDController.calculate(currentPosition, targetPosition);
    output = MathUtil.clamp(output, -0.5, 0.5); // Clamp the output to prevent excessive speed (adjust limits as needed)    
    setArmSpeed(output);
  }

  

  public void updateDashboard() {
    // When arm is added, we can move the arm manually and see how many rotations will fully rotate the arm
    SmartDashboard.putNumber("Arm Rotations", encoder.getPosition());  
    SmartDashboard.putNumber("Arm Speed", armMotor.get());
    SmartDashboard.putBoolean("Limit Switch Triggered", limitSwitch.get());
    SmartDashboard.putBoolean("Arm At Setpoint", armPIDController.atSetpoint());
  }
}
