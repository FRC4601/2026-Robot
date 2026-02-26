package frc.robot.commands;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;


public class Intake extends Command {

    private final Hopper hopper;
    private final Arm arm;
    private final intake Intake;

    
    @Override
    public void initialize(Hopper hopper, Arm arm, Intake intake) {
    
        this.hopper = hopper;
        this.arm = arm;
        this.intake = intake;
            
    }

    @Override
    public void execute() {

        // i need to make it so the hopper extends but tbh i have no clue how to do this

        // a lot of stuff happened at robotics on thursday so i was unfortunately unable to code a majority of this
        // hopefully it will be finished/substantially improved on friday
    }
}
