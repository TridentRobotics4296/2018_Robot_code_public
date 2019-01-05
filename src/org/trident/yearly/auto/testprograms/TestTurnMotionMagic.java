package org.trident.yearly.auto.testprograms;

import org.trident.yearly.auto.steps.DriveDistanceStraightStep;
import org.trident.yearly.auto.steps.TurnByNDegreesStepMagic;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;

public class TestTurnMotionMagic extends AutoProgram
{

   @Override
   protected void defineSteps()
   {
      addStep(new TurnByNDegreesStepMagic(400));
      addStep(new AutoStepDelay(5000));
      addStep(new TurnByNDegreesStepMagic(-5));
   }

   @Override
   public String toString()
   {
      return "Test Turn Motion Magic";
   }

}
