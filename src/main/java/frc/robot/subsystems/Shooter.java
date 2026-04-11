package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.TreeMap;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.*;
import frc.robot.Constants.ShooterConstants;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


// Controls the shooter motors to shoot balls at the target

public class Shooter extends SubsystemBase {

  // Initializing the motors (we have two for more power)
  // The follower motor will be set to copy the leader
  private TalonFX leaderMotor;
  private TalonFX followerMotor;
  private TalonFXConfiguration leaderConfig;
  private TalonFXConfiguration followerConfig;
  private VelocityVoltage velocityRequest;

   
    

  private static final TreeMap<Double, Double> DISTANCE_TO_RPM = new TreeMap<>();

  // Raw data that allows us to calculate the rpm needed to shoot from a given distance
  static {
    // Key: distance in inches, Value: corresponding shooter RPM
    DISTANCE_TO_RPM.put(40.0, 3950.0); // fabricated to help with closer distances
    DISTANCE_TO_RPM.put(83.0,  4500.0);
    DISTANCE_TO_RPM.put(111.0,  5000.0);
    DISTANCE_TO_RPM.put(155.0,  5500.0);
    DISTANCE_TO_RPM.put(206.6, 6000.0); // fabricated to help with farther distances
  }

  // Current RPM setpoint
  private double targetRPM = 0.0;

  // The constructor--configures the motors and PID 
  public Shooter() {

    leaderMotor = new TalonFX(ShooterConstants.SHOOTER_MOTOR_ID_1);
    followerMotor = new TalonFX(ShooterConstants.SHOOTER_MOTOR_ID_2);
    leaderConfig = new TalonFXConfiguration();
    followerConfig = new TalonFXConfiguration();

    // Velocity PID — slot 0
    leaderConfig.Slot0.kP = 0.2; // Placeholder values — these will need to be tuned
    leaderConfig.Slot0.kI = 0.00001;
    leaderConfig.Slot0.kD = 0.001;
    leaderConfig.Slot0.kV = 0.12; // Velocity feedforward term — also needs tuning

    // Setting the follower motor to copy the leader motor
    leaderConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    followerConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    leaderMotor.getConfigurator().apply(leaderConfig);
    followerMotor.getConfigurator().apply(followerConfig);
    followerMotor.setControl(new Follower(ShooterConstants.SHOOTER_MOTOR_ID_1, MotorAlignmentValue.Opposed));

    velocityRequest = new VelocityVoltage(0).withSlot(0);

  }

  public void setVelocity(double rpm) {
    targetRPM = rpm;
    double rps = rpm * ShooterConstants.RPM_TO_RPS;
    leaderMotor.setControl(velocityRequest.withVelocity(rps));
  }



  public void setVelocityFromDistance(double distanceInches) {
    double rpm = interpolateRPM(distanceInches);
    setVelocity(rpm+500);
  }

  // Calculates the target RPM based on the distance
  private double interpolateRPM(double distanceInches) {
    if (distanceInches <= DISTANCE_TO_RPM.firstKey()) {
      return DISTANCE_TO_RPM.firstEntry().getValue();
    }
    if (distanceInches >= DISTANCE_TO_RPM.lastKey()) {
      return DISTANCE_TO_RPM.lastEntry().getValue();
    }

    Double lowerKey = DISTANCE_TO_RPM.floorKey(distanceInches);
    Double upperKey = DISTANCE_TO_RPM.ceilingKey(distanceInches);

    double lowerRPM = DISTANCE_TO_RPM.get(lowerKey);
    double upperRPM = DISTANCE_TO_RPM.get(upperKey);

    // Linear interpolation between the two nearest entries
    double t = (distanceInches - lowerKey) / (upperKey - lowerKey);
    return lowerRPM + t * (upperRPM - lowerRPM);
  }



  public static double tyToDistance(double ty) {
    final double CAMERA_HEIGHT_INCHES = 22.0;    // Height of limelight lens from floor
    final double TARGET_HEIGHT_INCHES = 44.25;    // Height of April Tag center from floor
    final double CAMERA_MOUNT_ANGLE_DEGREES = 5.0; // Upward tilt of limelight on robot

    double angleToTarget = Math.toRadians(CAMERA_MOUNT_ANGLE_DEGREES + ty);
    return (TARGET_HEIGHT_INCHES - CAMERA_HEIGHT_INCHES) / Math.tan(angleToTarget);

  }



  public boolean isAtSpeed() {
    double currentRPM = leaderMotor.getVelocity().getValueAsDouble()*60;  // Convert back to RPM for comparison
    return Math.abs(currentRPM - targetRPM) <= ShooterConstants.AT_SPEED_TOLERANCE_RPM;
  }

  public void stopShooter(){
    leaderMotor.set(0);
    targetRPM = 0.0;
  }

  public void runShooter(double speed){
    leaderMotor.set(speed);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Target RPM", targetRPM);
    SmartDashboard.putNumber("Current RPM", leaderMotor.getVelocity().getValueAsDouble()*60);
    SmartDashboard.putBoolean("Shooter At Speed", isAtSpeed());
  }

}
