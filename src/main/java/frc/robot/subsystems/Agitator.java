package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.Constants.AgitatorConstants;

/**
 * Agitator subsystem — stirs balls in the hopper and feeds them toward the stager.
 * Runs whenever the robot is aligned and ready to shoot, producing a continuous
 * ball stream: Agitator → Stager → Shooter.
 */
public class Agitator extends SubsystemBase {

    private final SparkMax agitatorMotor;
    private final SparkMaxConfig agitatorConfig;


    
    public Agitator() {
        agitatorMotor = new SparkMax(AgitatorConstants.AGITATOR_MOTOR_ID, MotorType.kBrushless);
        agitatorConfig = new SparkMaxConfig();

        // Coast mode so the agitator doesn't jam balls against a stopped stager on disable
        agitatorConfig.idleMode(IdleMode.kCoast);

        agitatorMotor.configure(agitatorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    /**
     * Run the agitator at the specified speed (positive = toward stager).
     * Typical value: AgitatorConstants.AGITATOR_SPEED (e.g. 0.5)
     */
    public void setAgitatorSpeed(double speed) {
        agitatorMotor.set(speed);
    }

    /** Stop the agitator immediately. */
    public void stopAgitator() {
        agitatorMotor.set(0);
    }



    @Override
    public void periodic() {
        
        SmartDashboard.putNumber("Agitator/Speed", agitatorMotor.get());
    }
}
