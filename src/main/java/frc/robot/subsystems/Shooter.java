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

public class Shooter extends SubsystemBase {

  private TalonFX leaderMotor;
  private TalonFX followerMotor;
  private TalonFXConfiguration leaderConfig;
  private TalonFXConfiguration followerConfig;

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
        leaderConfig.Slot0.kP = 0.1; // Placeholder values — these will need to be tuned for your robot
        leaderConfig.Slot0.kI = 0;
        leaderConfig.Slot0.kD = 0;
        leaderConfig.Slot0.kV = 0.02; // Velocity feedforward term — also needs tuning

    


    followerMotor.setControl(new Follower(ShooterConstants.SHOOTER_MOTOR_ID_1, MotorAlignmentValue.Opposed));
 

    leaderConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    followerConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;


    leaderMotor.getConfigurator().apply(leaderConfig);


    
  }

  // Since I don't know how the shooter works, I can't do the methods rn... :/

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
