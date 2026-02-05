package frc.robot;

public final class Constants {

  public static class ArmConstants {
    public static final int ARM_MOTOR_ID = 20; //needs to be set when arm motor is added
    public static final double tolerance = 0.0; // Tolerance in degrees for arm position
    public static final double kd = 0;
    public static final double ki = 0;
    public static final double kp = .0067;
    public static final double ks = 0;
    public static final double kg = .02;
    public static final double kv = 0;
    public static final double ka = 0;
  
  }

    public static class IntakeConstants {
    public static final int INTAKE_MOTOR_ID = 19; //needs to be set when intake motor added

  }

  public static class LEDConstants {
    public static final int BLINKIN_PWM_PORT = 0;
  }
}
