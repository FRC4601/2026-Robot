// will have ONE piston (MAYBE two in the future)

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.Constants.PneumaticsConstants;


// Holds the fuel until ready to shoot

public class Hopper extends SubsystemBase {

  // Initializing the piston (pneumatics-based so not motor)
  public final DoubleSolenoid piston;

  // The constructor--configures the piston
  public Hopper() {
    piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, 
                                PneumaticsConstants.HOPPER_FORWARD_ID, PneumaticsConstants.HOPPER_REVERSE_ID);
  }

  @Override
  public void periodic() {}

  public void openHopper() {
    piston.set(DoubleSolenoid.Value.kForward);
  }
  
  public void closeHopper() {
    piston.set(DoubleSolenoid.Value.kReverse);
  }

  public void stopHopper() {
    piston.set(DoubleSolenoid.Value.kOff);
  }

  public boolean isOpen() {
    return piston.get() == DoubleSolenoid.Value.kForward;
  }

  public boolean isClosed() {
    return piston.get() == DoubleSolenoid.Value.kReverse;
  }
  
}
