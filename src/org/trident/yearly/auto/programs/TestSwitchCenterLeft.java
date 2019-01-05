package org.trident.yearly.auto.programs;

import org.trident.yearly.auto.steps.SwitchCenterLeftAutoStepGroup;
import org.trident.yearly.auto.steps.SwitchCenterRightAutoStepGroup;

public class TestSwitchCenterLeft extends BaseAutoProgram
{

   @Override
   protected void defineSteps()
   {
         addStep(new SwitchCenterLeftAutoStepGroup(-90, 0.2));
   }

   @Override
   public String toString()
   {
      // TODO Auto-generated method stub
      return "Test Left Switch from Center";
   }

}
