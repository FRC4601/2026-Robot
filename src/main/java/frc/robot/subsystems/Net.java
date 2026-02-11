// will have two or four pistons (either one on opposite corners or one on every corner)
// we still treat it as one double solenoid in the code though!

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
// ^ no idea what module type does but it was in the 2023 code
// import edu.wpi.first.wpilibj.DoubleSolenoid.Value; (this might be needed)

public class Net extends SubsystemBase {

  public final DoubleSolenoid piston;

  public Net() {
    piston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
    // ^ still need to research the double solenoid class
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void openNet(){
    piston.set(Value.kForward);
    // ^ NO CLUE what this does rn. will figure out tomorrow (thursday)
  }
  
  public void closeNet(){
    piston.set(Value.kReverse);
    // ^ see above
  }
  
}

