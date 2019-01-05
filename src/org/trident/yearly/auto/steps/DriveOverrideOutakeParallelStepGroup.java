package org.trident.yearly.auto.steps;

import org.wildstang.framework.auto.steps.AutoParallelFinishedOnAnyStepGroup;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;

public class DriveOverrideOutakeParallelStepGroup extends AutoParallelFinishedOnAnyStepGroup
{

   public DriveOverrideOutakeParallelStepGroup(int driveDistance, int waitTime) {
      addStep(new MotionMagicStraightLine(driveDistance));
      addStep(new AutoStepDelay(waitTime));
   }
   
}
