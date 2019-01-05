package org.trident.yearly.auto.programs;

import org.trident.yearly.auto.steps.SwitchCenterLeftAutoStepGroup;
import org.trident.yearly.auto.steps.SwitchCenterRightAutoStepGroup;

public class TestSwitchCenterRight extends BaseAutoProgram
{

   @Override
   protected void defineSteps()
   {
       addStep(new SwitchCenterRightAutoStepGroup(0.2)); 
   }

   @Override
   public String toString()
   {
      // TODO Auto-generated method stub
      return "Test Right Switch from Center";
   }

}
