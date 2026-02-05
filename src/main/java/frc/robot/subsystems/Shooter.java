package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.*;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {

  private TalonFX leaderMotor;
  private TalonFX followerMotor;
  private TalonFXConfiguration leaderConfig;
  private TalonFXConfiguration followerConfig;
  private DutyCycleEncoder shooterEncoder; // i don't know what this does so idk if it's necessary
  private double absolutePosition; // no clue what this and motorRotations do
  private double motorRotations;   // so idk if they should be instance variables or not
  
  public Shooter() {

    leaderMotor = new TalonFX(ShooterConstants.SHOOTER_MOTOR_ID_1);
    followerMotor = new TalonFX(ShooterConstants.SHOOTER_MOTOR_ID_2);
    
    leaderConfig = new TalonFXConfiguration();
    followerConfig = new TalonFXConfiguration();

    shooterEncoder = new DutyCycleEncoder(2); // again, idk what this is. 2 is a placeholder

    followerMotor.setControl(new Follower(leaderMotor.getDeviceID(), true));
    // no clue what getDeviceID means i just copied it from the documentation
    // the boolean makes them go the opposite way (i'm 99% sure it's supposed to be inverted)

    leaderConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    followerConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    // ^ idk if this is necessary since the follower follows, but rn it's better to be safe than sorry
    // also this is a shooter so i presume it doesn't need a brake mode (motors are either on
    // 100% or 0%) but i'm not completely sure...

    leaderMotor.getConfigurator().apply(leaderConfig);
    followerMotor.getConfigurator().apply(followerConfig);
    // idk if this second line is necessary as the follower motor follows anyway
    
    absolutePosition = shooterEncoder.get();
    motorRotations = absolutePosition*3; // replace the placeholder 67 with ArmConstants.ARM_GEAR_RATIO idk
    shooterMotor.setPosition(motorRotations);
    // ^ idk if this is necessary but it seems to make sense
    // but idk if this was an arm-exclusive thing (it seems to be)
    // i need to understand how the shooter works to write the shooter subsystem lol
    
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
