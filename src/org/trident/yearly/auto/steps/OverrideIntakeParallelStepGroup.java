package org.trident.yearly.auto.steps;

import org.wildstang.framework.auto.steps.AutoParallelFinishedOnAnyStepGroup;
import org.wildstang.framework.auto.steps.AutoParallelStepGroup;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;

public class OverrideIntakeParallelStepGroup extends AutoParallelFinishedOnAnyStepGroup
{

   public OverrideIntakeParallelStepGroup(AutoSerialStepGroup autoProgram) 
   {
      addStep(new OverrideIntakeStep());
      addStep(autoProgram);
   }
   
}
