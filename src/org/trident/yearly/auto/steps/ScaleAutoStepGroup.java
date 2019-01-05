package org.trident.yearly.auto.steps;

import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.subsystems.drive.DriveConstants;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;

public class ScaleAutoStepGroup extends AutoSerialStepGroup
{  
   public ScaleAutoStepGroup(int rotation, int rotation1, int verticalDist, double minSpeed)
   {
      addStep(new SetHighGearStep(true));
      addStep(new SetBrakeModeStep(true));
      
      //Stop front of robot 12in before scale
      addStep(new DriveZeroPoseParallelStepGroup(-(DistanceConstants.VERTICAL_TO_SCALE) + 12, 0.7, Pose.SCALE));
      addStep(new AutoStepDelay(100));
      
      addStep(new TurnByNDegreesNavXStep(rotation, minSpeed));
      addStep(new SetBrakeModeStep(false));
      addStep(new DriveOverrideOutakeParallelStepGroup(-verticalDist, 5000));
      addStep(new SetBrakeModeStep(true));
      
      addStep(new IntakeOutStep());
      addStep(new AutoStepDelay(1000));
      addStep(new IntakeOffStep());
      
      //addStep(new MotionMagicStraightLine(10));
      
      addStep(new TurnByNDegreesNavXStep(rotation1, minSpeed));
      
      addStep(new DrivePoseParallelStepGroup(DistanceConstants.VERTICAL_SCALE_TO_CUBE, Pose.START));
   }


}
