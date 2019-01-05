package org.trident.yearly.auto.steps;

import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.AutoParallelStepGroup;

public class DrivePoseParallelStepGroup extends AutoParallelStepGroup
{

   public DrivePoseParallelStepGroup(int driveDistance, Pose pose) {
      addStep(new MotionMagicStraightLine(driveDistance));
      addStep(new MoveToPoseStep(pose));
   }
   
}
