package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
//import edu.wpi.first.math.MathUtil;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//idk if we need all of these but i'm importing them just in case

/**
 * Agitator subsystem — stirs balls in the hopper and feeds them toward the stager.
 * Runs whenever the robot is aligned and ready to shoot, producing a continuous
 * ball stream: Agitator → Stager → Shooter.
 */
public class CANRange extends SubsystemBase {

    //Initialize the CAN range
  private final CANBus kCANBus = new CANBus("rio");
  private final CANrange intakeCANrange = new CANrange(IntakeConstants.CAN_RANGE_ID,kCANBus);
  private CANrangeConfiguration CANrangeConfig = new CANrangeConfiguration();


    
    public Intake() {

    IntakeMotor.setNeutralMode(NeutralModeValue.Brake);
    IntakeMotor.getConfigurator().apply(IntakeTalonFXConfig);

    CANrangeConfig.ProximityParams.MinSignalStrengthForValidMeasurement = 2500; // If CANrange has a signal strength of at least 2500, it is a valid measurement.
    CANrangeConfig.ProximityParams.ProximityThreshold = 0.05; // If CANrange detects an object within 0.05 meters, it will trigger the "isDetected" signal.

    CANrangeConfig.ToFParams.UpdateMode = UpdateModeValue.ShortRange100Hz; // Make the CANrange update as fast as possible at 100 Hz. This requires short-range mode.

    intakeCANrange.getConfigurator().apply(CANrangeConfig);
  
  }

//Run once per scheduler run
/* I copied this from last year, so i'm commenting out all of this because
it's mostly detect coral stuff from last year
  @Override
  public void periodic() {

    DetectCoral();
     SmartDashboard.putBoolean("CANrange Status", DetectCoral());
     SmartDashboard.putNumber("CANrange Distance",getSensorDistance());
     SmartDashboard.putNumber("CANrange Signal Strength",getSignalStrength());




  }


  // Get the status signal of the CANRange sensor (True if coral is within .05 meters of sensor )

  public boolean DetectCoral(){
    return intakeCANrange.getDistance().getValueAsDouble() < .05 && getSignalStrength() > 2500;
  }

  public double getSensorDistance(){
    return intakeCANrange.getDistance().getValueAsDouble();
  }

  public double getSignalStrength(){
    return intakeCANrange.getSignalStrength().getValueAsDouble();
  }


  public void RunIntake(double speed) {
    IntakeMotor.set(-speed);
  }

  public void stopIntake() {
    IntakeMotor.set(0);
  }*/

}