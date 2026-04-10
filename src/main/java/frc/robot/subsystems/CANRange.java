package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.signals.*;
import frc.robot.Constants.CANRangeConstants;
//import edu.wpi.first.math.MathUtil;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//idk if we need all of these but i'm importing them just in case


public class CANRange extends SubsystemBase {

  //Initialize the CAN range
  private final CANBus kCANBus = new CANBus("rio");
  private final CANrange CANrange = new CANrange(CANRangeConstants.CAN_RANGE_ID,kCANBus);
  private CANrangeConfiguration CANrangeConfig = new CANrangeConfiguration();
    
  public CANRange() {

    CANrangeConfig.ToFParams.UpdateMode = UpdateModeValue.ShortRange100Hz; // Make the CANrange update as fast as possible at 100 Hz. This requires short-range mode.

    CANrange.getConfigurator().apply(CANrangeConfig);
  
  }

  public void periodic() {
     SmartDashboard.putBoolean("CANrange Status", detectsFuel());
     SmartDashboard.putNumber("CANrange Distance",getDistance());
     SmartDashboard.putNumber("CANrange Strength",getStrength());
  }

  public boolean detectsFuel() {
    return getDistance() < CANRangeConstants.DISTANCE && getStrength() > 2500;
  }

  public double getDistance(){
    return CANrange.getDistance().getValueAsDouble();
  }

  public double getStrength(){
    return CANrange.getSignalStrength().getValueAsDouble();
  }

}