package org.trident.yearly.auto.steps;

import org.wildstang.framework.auto.steps.AutoParallelStepGroup;

public class ZeroManipulatorAutoStepGroup extends AutoParallelStepGroup
{

   public ZeroManipulatorAutoStepGroup(double speed) {
      addStep(new ZeroWristStep(speed));
      addStep(new ZeroArmStep(-speed));
      addStep(new ZeroLiftStep(-speed));
   }
   
}
