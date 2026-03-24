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


public class Arm extends SubsystemBase {

  private final SparkMax armMotor;
  private final SparkMaxConfig armConfig;
  private final PIDController armPIDController;
  private final DutyCycleEncoder absoluteEncoder; //REV throughbore encoder.
  private enum ArmState { POSITION_A, POSITION_B }
  private ArmState currentArmState = ArmState.POSITION_A;
  private Timer armTimer = new Timer();

 

   /** Creates a new Arm. */
  

  
  public Arm() {

     // Replace 0 with the actual DIO port number for the limit switch
    armMotor = new SparkMax(ArmConstants.ARM_MOTOR_ID, MotorType.kBrushless);
    armConfig = new SparkMaxConfig();
    armConfig.idleMode(IdleMode.kBrake);
    armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    absoluteEncoder = new DutyCycleEncoder(ArmConstants.ARM_ABSOLUTE_ENCODER_PORT); // Need encoder port here
    


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

  public boolean isAtSetpoint() {
    //Check if the arm is within the tolerance of the target position
    return armPIDController.atSetpoint();
  } 



  public double ArmPosition() {
    return absoluteEncoder.get()*360; //Get the arm angle in degrees. The absolute encoder returns a value between 0 and 1.
  }

  public void setArmSpeed(double speed) {

    //Need to debug this to make sure that the arm movement is correctly limited. 

    /* 
    if ((ArmPosition() > ArmConstants.ARM_EXTENDED_POSITION && speed < 0) 
         || (ArmPosition() < ArmConstants.ARM_RETRACTED_POSITION && speed > 0)) {
      // If the limit switch is triggered and we're trying to move the arm down, stop the motor
      armMotor.set(0);
    } else {
      armMotor.set(speed);
    }
      */

      //for now just running the motor with no limits

      armMotor.set(speed);

  }

  public void stopArm() {
    armMotor.set(0);
  }

  //method to set arm to target position with PID control. 

  public void moveArmToPosition(double targetPosition) {
    double currentPosition = ArmPosition();
    double output = armPIDController.calculate(currentPosition, targetPosition);
    output = MathUtil.clamp(output, -0.2, 0.2); //limit to half power at most to prevent violent movements. Adjust as necessary.
    
    if (output > 0 && currentPosition > ArmConstants.ARM_EXTENDED_POSITION) {
      
      output = 0; // Prevent moving beyond the extended position
    } else if (output < 0 && currentPosition < ArmConstants.ARM_RETRACTED_POSITION) {
      output = 0; // Prevent moving beyond the retracted position                                   
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

            if (armTimer.hasElapsed(0.5)) { // time spent at each position
                currentArmState = ArmState.POSITION_B;
                armTimer.reset();
            }
            break;

        case POSITION_B:
            moveArmToPosition(ArmConstants.POSITION_B_DEGREES);

            if (armTimer.hasElapsed(0.5)) {
                currentArmState = ArmState.POSITION_A;
                armTimer.reset();
            }
            break;
    }
}
  

  public void updateDashboard() {
    // When arm is added, we can move the arm manually and see how many motor rotations will fully extend the arm
    SmartDashboard.putNumber("Arm Angle", ArmPosition());  
    SmartDashboard.putNumber("Arm Speed", armMotor.get());
    SmartDashboard.putBoolean("Arm At Setpoint", armPIDController.atSetpoint());
    SmartDashboard.putNumber("Arm Target Position", armPIDController.getSetpoint());
  }


}
