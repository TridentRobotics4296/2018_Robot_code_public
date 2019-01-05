package org.trident.yearly.auto.steps;

import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;

public class ScaleFromCubeAutoStepGroup extends AutoSerialStepGroup
{

   public ScaleFromCubeAutoStepGroup(int angle, double speed) 
   {
      addStep(new SetBrakeModeStep(true));
      addStep(new DrivePoseParallelStepGroup(-104 + DistanceConstants.VERTICAL_SCALE_TO_CUBE, Pose.SCALE));
      addStep(new TurnByNDegreesNavXStep(angle, speed));
      addStep(new MotionMagicStraightLine(-DistanceConstants.VERTICAL_SCALE_TO_CUBE));
   }
   
}
