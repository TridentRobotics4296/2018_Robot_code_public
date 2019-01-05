package org.trident.yearly.auto.testprograms;

import org.trident.yearly.auto.steps.SetBrakeModeStep;
import org.trident.yearly.auto.steps.DriveToCubeStep;
import org.trident.yearly.auto.steps.IntakeInStep;
import org.wildstang.framework.auto.AutoProgram;

public class VisionTest extends AutoProgram
{

   @Override
   protected void defineSteps()
   {
      // TODO Auto-generated method stub
      addStep(new SetBrakeModeStep(true));
      addStep(new IntakeInStep());
      addStep(new DriveToCubeStep());
   }

   @Override
   public String toString()
   {
      // TODO Auto-generated method stub
      return "Vision Test";
   }

}
