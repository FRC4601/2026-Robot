// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import java.util.Map;
import java.util.HashMap;

public class Vision extends SubsystemBase {

  //10.0.4061.11:5801 type into broswer to see the limelight's web interface. This is useful for debugging and tuning the limelight settings. The limelight's network table entries are updated based on the settings configured in the web interface, so it's important to make sure those settings are correct for our use case (e.g. correct pipeline, correct mounting height and angle, etc.). The web interface also allows us to see what the limelight is currently seeing, which can help with troubleshooting if we're not getting the expected values in our code.

  //table where all data came from the limelight is stored. We will be using the "tx", "ty", and "tv" entries to get the horizontal offset, vertical offset, and target validity respectively.
  private final NetworkTable table;
  //private int[] activeGoalTags; // Array to hold the active April Tag IDs for the current alliance

  public static final int PIPELINE_APRILTAG = 0;

  

  public Vision() {
    
    // Initialize the NetworkTable for vision processing
    table = NetworkTableInstance.getDefault().getTable("limelight");

    //setPipeline(PIPELINE_APRILTAG); 
    
    NetworkTableInstance.getDefault().getTable("").getKeys().forEach(key -> 
    System.out.println("NT Key: " + key)
);
    

  }


  // Method to get the horizontal offset (tx) from the vision system. 
  // This is the horizontal angle between the limelight crosshair and the center of the April Tag in degrees.
  // A positive value means the target is to the right of the crosshair, and a negative value means it's to the left.
  // This value will be used to align the turrent 

   //public double getTx() {
        //return table.getEntry("tx").getDouble(0.0);
    //}

    public double getTx() {
    return LimelightHelpers.getTX("limelight");
    }


    /* 
    public Map<Integer,Double> getTagTXMap(){
      LimelightHelpers.RawFiducial[] tags = LimelightHelpers.getRawFiducials("limelight");
      Map<Integer, Double> tagTXMap = new HashMap<>();

      if (tags == null){
        return tagTXMap; // Return empty map if no tags are detected
      }

      for (LimelightHelpers.RawFiducial tag : tags) {
          tagTXMap.put(tag.id, tag.txnc);
      }

      return tagTXMap;


    }

    
    public void setActiveGoalTags(){
      if (Constants.isBlueAlliance){
        activeGoalTags = new int[]{1, 2, 3}; // Example tag IDs for blue alliance goals
      } else {
        activeGoalTags = new int[]{4, 5, 6}; // Example tag IDs for red alliance goals
      }
    }
    */




    // Method to get the vertical offset (ty) from the vision system.
    // This is the vertical angle in degrees between the limelight crosshair and the center of the April Tag in degrees.
    // Positive means the target is above the crosshair

    public double getTy() {
        return LimelightHelpers.getTY("limelight");
    }


    // Method to detect whether the limelight has a valid target (April Tag) in view.
    // The limelight sets the "tv" entry to 1 if it has a valid target, and 0 if it does not.
    public boolean hasTarget() {
        return LimelightHelpers.getTV("limelight");
    }

    //public double getTargetID() {
        //return LimelightHelpers.getTargetID("limelight" );
    //}
    
    /* 
    public boolean isOnGoalTag() {
    int tid = (int) table.getEntry("tid").getDouble(-1);
    for (int id : activeGoalTags) {
        if (id == tid) return true;
    }
    return false;
   }   
      */
 

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
    SmartDashboard.putNumber("tid", table.getEntry("tid").getDouble(-1));
    SmartDashboard.putBoolean("NT Connected", NetworkTableInstance.getDefault().isConnected());
    SmartDashboard.putString("NT Address", table.getEntry("tx").getString("no entry"));
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
