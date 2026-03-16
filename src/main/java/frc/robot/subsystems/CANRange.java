package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANRangeConstants;
//import edu.wpi.first.math.MathUtil;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//idk if we need all of these but i'm importing them just in case

/**
 * add what this file does here
 */
public class CANRange extends SubsystemBase {

    //Initialize the CAN range
  private final CANBus kCANBus = new CANBus("rio");
  private final CANrange CANrange = new CANrange(CANRangeConstants.CAN_RANGE_ID,kCANBus); // 67 67 67 67 67 67 ... 67
  private CANrangeConfiguration CANrangeConfig = new CANrangeConfiguration();


    
  public CANRange() {

    CANrangeConfig.ProximityParams.MinSignalStrengthForValidMeasurement = 2500; // If CANrange has a signal strength of at least 2500, it is a valid measurement.
    CANrangeConfig.ProximityParams.ProximityThreshold = 0.05; // If CANrange detects an object within 0.05 meters, it will trigger the "isDetected" signal.

    CANrangeConfig.ToFParams.UpdateMode = UpdateModeValue.ShortRange100Hz; // Make the CANrange update as fast as possible at 100 Hz. This requires short-range mode.

    CANrange.getConfigurator().apply(CANrangeConfig);
  
  }


  @Override
  public void periodic() {

     DetectFuel();
     SmartDashboard.putBoolean("CANrange Status", DetectFuel());
     SmartDashboard.putNumber("CANrange Distance",getSensorDistance());
     SmartDashboard.putNumber("CANrange Signal Strength",getSignalStrength());


  }




  public boolean DetectFuel(){
    return CANrange.getDistance().getValueAsDouble() < .05 && getSignalStrength() > 2500;
  }

  public double getSensorDistance(){
    return CANrange.getDistance().getValueAsDouble();
  }

  public double getSignalStrength(){
    return CANrange.getSignalStrength().getValueAsDouble();
  }



}