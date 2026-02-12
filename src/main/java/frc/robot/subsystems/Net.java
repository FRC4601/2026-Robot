// will have ONE piston (MAYBE two in the future)

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
// ^ differentiates between whether we have a CTREPCM pneumatics type or a REVPH. I need to ask donald which we have.
import frc.robot.Constants.PneumaticsConstants;

public class Net extends SubsystemBase {

  public final DoubleSolenoid piston;

  public Net() {
    piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, 
                                PneumaticsConstants.NET_FORWARD_ID, PneumaticsConstants.NET_REVERSE_ID);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void openNet() {
    piston.set(DoubleSolenoid.Value.kForward);
    // no clue if forward or reverse opens the net.
    // tbh probably need a better verb than "open" but idrc
  }
  
  public void closeNet() {
    piston.set(DoubleSolenoid.Value.kReverse);
  }

  public void stopNet() {
    piston.set(DoubleSolenoid.Value.kOff);
    // no clue if we will have to stop it manually or if it will stop automatically
    // will test... eventually
  }
  
}

