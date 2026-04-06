package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.signals.UpdateModeValue;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.Constants.AgitatorConstants;
import frc.robot.Constants.CANRangeConstants;


// Feeds balls from the intake into the stager

public class Agitator extends SubsystemBase {

    // Initializing the motor
    private final SparkMax agitatorMotor;
    private final SparkMaxConfig agitatorConfig;
    
    // Initializing the CANrange
    private final CANBus kCANBus = new CANBus("rio");
    private final CANrange canrange = new CANrange(CANRangeConstants.CAN_RANGE_ID,kCANBus); 
    private CANrangeConfiguration canrangeConfig = new CANrangeConfiguration(); 
    public static final double BALL_DETECTION_THRESHOLD = 0.05; // meters, adjust based on testing

    private Timer deadSpotTimer = new Timer();

    private enum FeedState { FEEDING, UNJAMMING }
    private FeedState currentState = FeedState.FEEDING;




    // The constructor--configures the motor and CANrange
    public Agitator() {

        agitatorMotor = new SparkMax(AgitatorConstants.AGITATOR_MOTOR_ID, MotorType.kBrushless);
        agitatorConfig = new SparkMaxConfig();

        
        agitatorConfig.idleMode(IdleMode.kCoast);
        agitatorConfig.smartCurrentLimit(30);
        agitatorConfig.secondaryCurrentLimit(40);

        // The following line will not work post-2026
        agitatorMotor.configure(agitatorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        canrangeConfig.ProximityParams.MinSignalStrengthForValidMeasurement = 2500; // If CANrange has a signal strength of at least 2500, it is a valid measurement.
        canrangeConfig.ProximityParams.ProximityThreshold = BALL_DETECTION_THRESHOLD; // If CANrange detects an object within 0.05 meters, it will trigger the "isDetected" signal.

        canrangeConfig.ToFParams.UpdateMode = UpdateModeValue.ShortRange100Hz; // Make the CANrange update as fast as possible at 100 Hz. This requires short-range mode.

        canrange.getConfigurator().apply(canrangeConfig);

    }


    
    public void startTimer() {
        deadSpotTimer.reset();
        deadSpotTimer.start();
    }

    public void setAgitatorSpeed(double speed) {
        agitatorMotor.set(-speed);
    }

    
    // State machine to run the agitator and unjam if necessary.
    // Runs each loop cycle while the Shoot command is active to feed balls into the shooter and unjam if necessary
    public void feedPeriodic() {
        switch (currentState) {

            case FEEDING:
                agitatorMotor.set(-AgitatorConstants.FEED_SPEED);

                if (!DetectFuel()) { // no ball detected, start the timer
                    if (deadSpotTimer.hasElapsed(1.5)) {
                        currentState = FeedState.UNJAMMING;
                        deadSpotTimer.reset();
                    }
                } else {
                    deadSpotTimer.reset(); // ball detected, keep resetting
                }
                break;

            case UNJAMMING:
                agitatorMotor.set(0.75); // this is positive on purpose, it's going backwards

                if (deadSpotTimer.hasElapsed(2)) {
                    currentState = FeedState.FEEDING;
                    deadSpotTimer.reset();
                }
                break;
        }
    }

    public void stopAgitator() {
        agitatorMotor.set(0);
    }

    public boolean DetectFuel(){
        return (getSensorDistance() < .02 && getSignalStrengthDouble() > 6500);
    }

    public double getSensorDistance(){
        return canrange.getDistance().getValueAsDouble();
    }

    public double getSignalStrengthDouble(){
        return canrange.getSignalStrength().getValueAsDouble();
    }

    // Runs every 20ms while the robot is running
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Speed", agitatorMotor.get());
        SmartDashboard.putBoolean("Fuel in Stager ", DetectFuel());
        SmartDashboard.putNumber("CANRange Distance", getSensorDistance());
        SmartDashboard.putNumber("CANRange SignalStrength", getSignalStrengthDouble());
    }
}
