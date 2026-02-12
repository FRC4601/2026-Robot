// will have ONE piston (MAYBE two in the future)

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
// ^ differentiates between whether we have a CTREPCM pneumatics type or a REVPH. I need to ask donald which we have.
import frc.robot.Constants.PneumaticsConstants;

public class Hopper extends SubsystemBase {

  public final DoubleSolenoid piston;

  public Hopper() {
    piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, 
                                PneumaticsConstants.HOPPER_FORWARD_ID, PneumaticsConstants.HOPPER_REVERSE_ID);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void openHopper() {
    piston.set(DoubleSolenoid.Value.kForward);
    // the names MIGHT be backwards. idk if forward is open or close
  }
  
  public void closeHopper() {
    piston.set(DoubleSolenoid.Value.kReverse);
  }

  public void stopHopper() {
    piston.set(DoubleSolenoid.Value.kOff);
    // no clue if we will need to stop it when it's fully out/fully in or if it's done automatically
    // will test eventually
  }
  
}
