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
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Timer; 


// The arm with the intake wheel on it

public class Arm extends SubsystemBase {

  // Initializing the motor
  private final SparkMax armMotor;
  private final SparkMaxConfig armConfig;

  // Initializing the PID
  private final PIDController armPIDController;
  private final DutyCycleEncoder absoluteEncoder; //REV throughbore encoder.

  // Helps with the oscillating during shooting (NOT instance variables)
  private enum ArmState { POSITION_A, POSITION_B }
  private ArmState currentArmState = ArmState.POSITION_A;
  private Timer armTimer = new Timer();
  
  // The constructor--configures the motor and PID
  public Arm() {

    armMotor = new SparkMax(ArmConstants.ARM_MOTOR_ID, MotorType.kBrushless);
    armConfig = new SparkMaxConfig();
    armConfig.idleMode(IdleMode.kBrake);
    armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    absoluteEncoder = new DutyCycleEncoder(ArmConstants.ARM_ABSOLUTE_ENCODER_PORT);

    armPIDController = new PIDController(ArmConstants.kp, ArmConstants.ki, ArmConstants.kd);
    armPIDController.setTolerance(ArmConstants.tolerance);

  }

  public boolean isAtSetpoint() {
    // Check if the arm is within the tolerance of the target position
    return armPIDController.atSetpoint();
  } 


  public double ArmPosition() {
    // Convert arm angle to degrees
    return 360 - absoluteEncoder.get()*360;
  }

  public void setArmSpeed(double speed) {

    if ((ArmPosition() >= ArmConstants.ARM_EXTENDED_POSITION && speed > 0) 
         || (ArmPosition() <= ArmConstants.ARM_RETRACTED_POSITION && speed < 0)) {
      armMotor.set(0);
    } else {
      armMotor.set(speed);
    }

  }

  public void stopArm() {
    armMotor.set(0);
  }

  // Method to set arm to target position with PID control. 
  public void moveArmToPosition(double targetPosition) {
    double currentPosition = ArmPosition();
    double output = armPIDController.calculate(currentPosition, targetPosition);


    if (currentPosition < 120 || output < 0){
      // when moving the arm in an out, limit the power to prevent violent movements.
      output = MathUtil.clamp(output, -0.25, 0.25);
    }
    else{
      //when arm is extended to intake position, apply more power to hold position against incoming fuel
      output = MathUtil.clamp(output, -1, 1);
    }
     
    
    // Prevent moving beyond the extended position
    if ((output > 0 && currentPosition >= ArmConstants.ARM_EXTENDED_POSITION) 
            || (output < 0 && currentPosition <= ArmConstants.ARM_RETRACTED_POSITION)) {
      output = 0;
    }
       
    setArmSpeed(output);
  }

  public void startOscillate() {
    armTimer.reset();
    armTimer.start();
    currentArmState = ArmState.POSITION_A; // start from a known position
  }
  


  public void oscillate() {
    switch (currentArmState) {

        case POSITION_A:
            moveArmToPosition(ArmConstants.POSITION_A_DEGREES);

            if (armTimer.hasElapsed(0.75)) { // time spent at each position
                currentArmState = ArmState.POSITION_B;
                armTimer.reset();
            }
            break;

        case POSITION_B:
            moveArmToPosition(ArmConstants.POSITION_B_DEGREES);

            if (armTimer.hasElapsed(0.75)) {
                currentArmState = ArmState.POSITION_A;
                armTimer.reset();
            }
            break;
    }
  }

  // Runs every 20ms while the robot is running
  @Override
  public void periodic() {
    SmartDashboard.putNumber("Arm Angle", ArmPosition());  
    SmartDashboard.putNumber("Arm Speed", armMotor.get());
    SmartDashboard.putBoolean("Arm At Setpoint", armPIDController.atSetpoint());
    SmartDashboard.putNumber("Arm Target Position", armPIDController.getSetpoint());
  }

}
