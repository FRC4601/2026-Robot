// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
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
