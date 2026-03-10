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


public class Arm extends SubsystemBase {

  private final SparkMax armMotor;
  private final SparkMaxConfig armConfig;
  private final PIDController armPIDController;
  private final DutyCycleEncoder absoluteEncoder; // For absolute position feedback if needed

   /** Creates a new Arm. */
  

  
  public Arm() {

     // Replace 0 with the actual DIO port number for the limit switch
    armMotor = new SparkMax(ArmConstants.ARM_MOTOR_ID, MotorType.kBrushless);
    armConfig = new SparkMaxConfig();
    armConfig.idleMode(IdleMode.kBrake);
    armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    absoluteEncoder = new DutyCycleEncoder(ArmConstants.ARM_ABSOLUTE_ENCODER_PORT); // Assuming the absolute encoder is connected to a DIO port
    


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
      


  }



  public double ArmPosition() {
    return absoluteEncoder.get()*360; // Assuming the absolute encoder is configured to return distance in motor rotations
  }

  public void setArmSpeed(double speed) {
    if ((ArmPosition() > ArmConstants.ARM_EXTENDED_POSITION && speed < 0) 
         || (ArmPosition() < ArmConstants.ARM_RETRACTED_POSITION && speed > 0)) {
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
    double currentPosition = ArmPosition();
    double output = armPIDController.calculate(currentPosition, targetPosition);
    output = MathUtil.clamp(output, -0.5, 0.5);
    
    if (output > 0 && currentPosition > ArmConstants.ARM_EXTENDED_POSITION) {
      
      output = 0; // Prevent moving beyond the extended position
    } else if (output < 0 && currentPosition < ArmConstants.ARM_RETRACTED_POSITION) {
      output = 0; // Prevent moving beyond the retracted position                                   
    }
       
    setArmSpeed(output);
  }

  

  public void updateDashboard() {
    // When arm is added, we can move the arm manually and see how many motor rotations will fully extend the arm
    SmartDashboard.putNumber("Arm Angle", ArmPosition());  
    SmartDashboard.putNumber("Arm Speed", armMotor.get());
    SmartDashboard.putBoolean("Arm At Setpoint", armPIDController.atSetpoint());
    SmartDashboard.putNumber("Arm Target Position", armPIDController.getSetpoint());
  }


}
