package frc.robot;

public final class Constants {

  public static class ArmConstants {
    public static final int ARM_MOTOR_ID = 20; // needs to be set when arm motor is added
    public static final double tolerance = 0.0; // Tolerance in rotations for arm position
    public static final double kd = 0; 
    public static final double ki = 0;
    public static final double kp = .0067; // PID Values for the arm position controller. 
    public static final double ks = 0;
    public static final double kg = .02;
    public static final double kv = 0;
    public static final double ka = 0;

    public static final double ARM_EXTENDED_POSITION = 0; // in motor rotations. needs to be set through testing
    public static final double ARM_GEAR_RATIO = 0; // may use if we need to convert from motor rotations to arm angle in degrees
    
  
  }

  public static class IntakeConstants {
    public static final int INTAKE_MOTOR_ID = 19; // needs to be set when intake motor added
  }

  public static class AgitatorConstants {
    public static final int AGITATOR_MOTOR_ID = 19; // needs to be set when agitator motor added
  }

  public static class StagerConstants {
    public static final int STAGER_MOTOR_ID = 19; // needs to be set when stager motor added
  }

  public static class ShooterConstants {
    public static final int SHOOTER_MOTOR_ID_1 = 19; // needs to be set when shooter motor 1 added
    public static final int SHOOTER_MOTOR_ID_2 = 20; // needs to be set when shooter motor 2 added
  }

  public static class PneumaticsConstants {
    public static final int HOPPER_FORWARD_ID = 67; // needs to be set when hopper piston added
    public static final int HOPPER_REVERSE_ID = 41; // needs to be set when hopper piston added
    public static final int NET_FORWARD_ID = 67; // needs to be set when net piston added
    public static final int NET_REVERSE_ID = 41; // needs to be set when net piston added
  }

  public static class LEDConstants {
    public static final int BLINKIN_PWM_PORT = 0;
  }
}
