package org.trident.yearly.auto.steps;

import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;

public class ZeroPoseStepGroup extends AutoSerialStepGroup
{

   public ZeroPoseStepGroup(double zeroSpeed, Pose pose)
   {
      addStep(new ZeroManipulatorAutoStepGroup(zeroSpeed));
      addStep(new MoveToPoseStep(pose));
   }
   
}
