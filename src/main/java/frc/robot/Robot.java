package frc.robot;

import com.ctre.phoenix6.HootAutoReplay;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {

    private Command autonomousCommand;

    private final RobotContainer robotContainer;

    /* log and replay timestamp and joystick data */
    private final HootAutoReplay timeAndJoystickReplay = new HootAutoReplay()
        .withTimestampReplay()
        .withJoystickReplay();

    public Robot() {
        this.robotContainer = new RobotContainer();
    }

    @Override
    public void robotPeriodic() {
        timeAndJoystickReplay.update();
        CommandScheduler.getInstance().run(); 
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void disabledExit() {}

    @Override
    public void autonomousInit() {

        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            Constants.isBlueAlliance = alliance.get() == DriverStation.Alliance.Blue;
            
        }

        //robotContainer.getVision().setActiveGoalTags();


        
        autonomousCommand = robotContainer.getAutonomousCommand();

        if (autonomousCommand != null) {
            CommandScheduler.getInstance().schedule(autonomousCommand);
        }



    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    @Override
    public void teleopInit() {

        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
                Constants.isBlueAlliance = alliance.get() == DriverStation.Alliance.Blue;
               
            }

        //robotContainer.getVision().setActiveGoalTags();

        if (autonomousCommand != null) {
                CommandScheduler.getInstance().cancel(autonomousCommand);
             }


    }
    

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void testExit() {}

    @Override
    public void simulationPeriodic() {}
}
