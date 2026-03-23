// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class Vision extends SubsystemBase {

  //table where all data came from the limelight is stored. We will be using the "tx", "ty", and "tv" entries to get the horizontal offset, vertical offset, and target validity respectively.
  private final NetworkTable table;
  private int[] activeGoalTags; // Array to hold the active April Tag IDs for the current alliance

  public static final int PIPELINE_APRILTAG = 0;

  

  public Vision() {
    
    // Initialize the NetworkTable for vision processing
    table = NetworkTableInstance.getDefault().getTable("limelight");

    setPipeline(PIPELINE_APRILTAG); 
    

  }


  // Method to get the horizontal offset (tx) from the vision system. 
  // This is the horizontal angle between the limelight crosshair and the center of the April Tag in degrees.
  // A positive value means the target is to the right of the crosshair, and a negative value means it's to the left.
  // This value will be used to align the turrent 

   public double getTx() {
        return table.getEntry("tx").getDouble(0.0);
    }

    
    public void setActiveGoalTags(){
      if (Constants.isBlueAlliance){
        activeGoalTags = new int[]{1, 2, 3}; // Example tag IDs for blue alliance goals
      } else {
        activeGoalTags = new int[]{4, 5, 6}; // Example tag IDs for red alliance goals
      }
    }





    // Method to get the vertical offset (ty) from the vision system.
    // This is the vertical angle in degrees between the limelight crosshair and the center of the April Tag in degrees.
    // Positive means the target is above the crosshair

    public double getTy() {
        return table.getEntry("ty").getDouble(0.0);
    }


    // Method to detect whether the limelight has a valid target (April Tag) in view.
    // The limelight sets the "tv" entry to 1 if it has a valid target, and 0 if it does not.
    public boolean hasTarget() {
        return table.getEntry("tv").getDouble(0.0) == 1.0;
    }
     
    public boolean isOnGoalTag() {
    int tid = (int) table.getEntry("tid").getDouble(-1);
    for (int id : activeGoalTags) {
        if (id == tid) return true;
    }
    return false;
}   
 

     /**
     * Switch the active Limelight pipeline.
     * @param pipelineIndex 0-indexed pipeline number configured in Limelight UI
     */

    public void setPipeline(int pipelineIndex) {
        table.getEntry("pipeline").setNumber(pipelineIndex);
    }

    // Method to get the current active pipeline index from the limelight. This is useful for debugging to confirm we're on the right pipeline.
    public int getCurrentPipeline() {
        return (int) table.getEntry("getpipe").getDouble(0.0);
    }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    
    SmartDashboard.putNumber("Limelight TX", getTx());
    SmartDashboard.putNumber("Limelight TY", getTy());
    SmartDashboard.putBoolean("Limelight Has Target", hasTarget());
    SmartDashboard.putNumber("Limelight Pipeline", getCurrentPipeline());
    //SmartDashboard.putBoolean("On Goal Tag", isOnGoalTag());
    SmartDashboard.putNumber("tid", table.getEntry("tid").getDouble(-1));;
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
