package org.trident.yearly.auto.steps;

import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.AutoParallelStepGroup;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;

public class DriveZeroPoseParallelStepGroup extends AutoParallelStepGroup
{

   public DriveZeroPoseParallelStepGroup(int driveDistance, double zeroSpeed, Pose pose) 
   {
      addStep(new MotionMagicStraightLine(driveDistance));
      addStep(new ZeroPoseStepGroup(zeroSpeed, pose));
   }
   
}
