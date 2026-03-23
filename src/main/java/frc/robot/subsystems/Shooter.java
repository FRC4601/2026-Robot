package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.TreeMap;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.*;
import frc.robot.Constants.ShooterConstants;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends SubsystemBase {

  private TalonFX leaderMotor;
  private TalonFX followerMotor;
  private TalonFXConfiguration leaderConfig;
  private TalonFXConfiguration followerConfig;
  private VelocityVoltage velocityRequest;

    // TalonFX velocity is in rotations/second — convert from RPM
    // rotations per second = RPM / 60
   private static final double RPM_TO_RPS = 1.0 / 60.0;

    // Tolerance in RPM to consider shooter "at speed" and ready to fire
    private static final double AT_SPEED_TOLERANCE_RPM = 75.0;

     // --- Distance to RPM lookup table ---
    // Key: distance in inches from target
    // Value: desired shooter RPM at that distance
    

   private static final TreeMap<Double, Double> DISTANCE_TO_RPM = new TreeMap<>();

    static {
        // Example values — replace with real tuned data
        //Once our robot can shoot. We will find the rpms that allow us to score from different distances and add these pairs to the table
        DISTANCE_TO_RPM.put(60.0,  2400.0);
        DISTANCE_TO_RPM.put(84.0,  2900.0);
        DISTANCE_TO_RPM.put(108.0, 3300.0);
        DISTANCE_TO_RPM.put(132.0, 3700.0);
        DISTANCE_TO_RPM.put(156.0, 4100.0);
    }

    // Current RPM setpoint
    private double targetRPM = 0.0;

  
  public Shooter() {

    

    leaderMotor = new TalonFX(ShooterConstants.SHOOTER_MOTOR_ID_1);
    followerMotor = new TalonFX(ShooterConstants.SHOOTER_MOTOR_ID_2);
    
    leaderConfig = new TalonFXConfiguration();
    followerConfig = new TalonFXConfiguration();

    // Velocity PID — slot 0
        leaderConfig.Slot0.kP = 0.1; // Placeholder values — these will need to be tuned
        leaderConfig.Slot0.kI = 0;
        leaderConfig.Slot0.kD = 0;
        leaderConfig.Slot0.kV = 0.02; // Velocity feedforward term — also needs tuning

    


    leaderConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    followerConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

    leaderMotor.getConfigurator().apply(leaderConfig);
    followerMotor.getConfigurator().apply(followerConfig);



    velocityRequest = new VelocityVoltage(0).withSlot(0);

    followerMotor.setControl(new Follower(ShooterConstants.SHOOTER_MOTOR_ID_1, MotorAlignmentValue.Opposed));
    
    
  }

  public void setVelocity(double rpm) {
        targetRPM = rpm;
        double rps = rpm * RPM_TO_RPS;
        leaderMotor.setControl(velocityRequest.withVelocity(rps));
    }



   public void setVelocityFromDistance(double distanceInches) {
        double rpm = interpolateRPM(distanceInches);
        setVelocity(rpm);
    }

  
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
        final double CAMERA_HEIGHT_INCHES = 24.0;    // Height of limelight lens from floor
        final double TARGET_HEIGHT_INCHES = 57.0;    // Height of April Tag center from floor
        final double CAMERA_MOUNT_ANGLE_DEGREES = 30.0; // Upward tilt of limelight on robot

        double angleToTarget = Math.toRadians(CAMERA_MOUNT_ANGLE_DEGREES + ty);
        return (TARGET_HEIGHT_INCHES - CAMERA_HEIGHT_INCHES) / Math.tan(angleToTarget);

    }



    public boolean isAtSpeed() {
        double currentRPM = leaderMotor.getVelocity().getValueAsDouble()*60;  // Convert back to RPM for comparison
        return Math.abs(currentRPM - targetRPM) <= AT_SPEED_TOLERANCE_RPM;
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
    // This method will be called once per scheduler run
    
    SmartDashboard.putNumber("Shooter/Target RPM", targetRPM);
    SmartDashboard.putNumber("Shooter/Current RPM", leaderMotor.getVelocity().getValueAsDouble()*60);
    SmartDashboard.putBoolean("Shooter/At Speed", isAtSpeed());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
