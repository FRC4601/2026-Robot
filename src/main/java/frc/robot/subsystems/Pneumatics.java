// hi idk what i'm doing

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Pneumatics extends SubsystemBase {
	// this makes a new class for pneumatics
	DoubleSolenoid pneumatics;
	// i genuinely do not know what any of this means i'm just following a random youtube tutorial
	public Pneumatics {
		pneumatics = new DoubleSolenoid(PneumaticsModuleType.REVPH, forwardChannel: 0, reverseChannel: 0);
		// we might need to change the port numbers later idk which one's we're gonna use
	}

}
