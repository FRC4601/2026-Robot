// Basic constants to help with readability in other files

package frc.robot;

public final class Constants {

  public static boolean isBlueAlliance = true;

  public static class ArmConstants {
    public static final int ARM_MOTOR_ID = 25;
    public static final double tolerance = 0.0; // Tolerance in rotations for arm position
    public static final double kd = 0; 
    public static final double ki = 0;
    public static final double kp = .017; // PID Values for the arm position controller. 
    public static final double ks = 0;
    public static final double kg = .02;
    public static final double kv = 0;
    public static final double ka = 0;

    public static final double ARM_EXTENDED_POSITION = 140;
    public static final double ARM_RETRACTED_POSITION = 3; // in motor rotations. needs to be set through testing
    public static final double ARM_GEAR_RATIO = 0; // may use if we need to convert from motor rotations to arm angle in degrees
    public static final int ARM_ABSOLUTE_ENCODER_PORT = 4; // needs to be set when absolute encoder is added

    public static final double POSITION_A_DEGREES = 70; 
    public static final double POSITION_B_DEGREES = 40;
  
  }

  public static class IntakeConstants {
    public static final int INTAKE_MOTOR_ID = 23;
  }

  public static class AgitatorConstants {
    public static final int AGITATOR_MOTOR_ID = 22;
    public static final double FEED_SPEED = 1;
    public static final double AGITATOR_SPEED = 0.75;
  }

  public static class StagerConstants {
    public static final int STAGER_MOTOR_ID = 24;
    public static final double STAGER_SPEED = 0.7;
  }

  public static class ShooterConstants {
    public static final int SHOOTER_MOTOR_ID_1 = 19;
    public static final int SHOOTER_MOTOR_ID_2 = 20;
    public static final double RPM_TO_RPS = 1.0 / 60.0;
    // Tolerance in RPM to consider shooter "at speed" and ready to fire
    public static final double AT_SPEED_TOLERANCE_RPM = 300.0;
  }

 public static class TurretConstants {
    public static final int TURRET_MOTOR_ID = 21;
    public static final double kd = 0;
    public static final double ki = 0;
    public static final double kp = 0.005; // PID Values for the turret position controller. These will need to be tuned.
    public static final double MAX_ANGLE_DEGREES = 90.0; // Maximum allowed turret angle in degrees (adjust as needed)
    public static final double MIN_ANGLE_DEGREES = -90.0; // Minimum allowed turret angle in degrees (adjust as needed)
    public static final double GEAR_RATIO = 81;
    public static final double MOTOR_ROTATIONS_PER_DEGREE = GEAR_RATIO / 360.0;
    public static final double ALIGNMENT_TOLERANCE_DEGREES = 2.0; // Adjust this tolerance as needed
  }


  public static class PneumaticsConstants {
    public static final int HOPPER_FORWARD_ID = 10;
    public static final int HOPPER_REVERSE_ID = 11;

  }

  public static class LEDConstants {
    public static final int BLINKIN_PWM_PORT = 0;
  }

  public static class CANRangeConstants {
    public static final int CAN_RANGE_ID = 26; // needs to be set when CANrange is added
    public static final double DISTANCE = 0.06;
    public static final double LONG_TIME = 2.5; // tune?
    public static final double UNJAM_TIME = 1.5;
  }

}
