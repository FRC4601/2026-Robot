// will have ONE piston (MAYBE two in the future)

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
// ^ differentiates between whether we have a CTREPCM pneumatics type or a REVPH. I need to ask donald which we have.

public class Hopper extends SubsystemBase {

  public final DoubleSolenoid piston;

  public Hopper() {
    piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0, 1);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void openHopper(){
    piston.set(DoubleSolenoid.Value.kForward);
    // ^ NO CLUE what this does rn. will figure out tomorrow (thursday)
  }
  
  public void closeHopper(){
    piston.set(Value.kReverse);
    // ^ see above
  }
  
}
