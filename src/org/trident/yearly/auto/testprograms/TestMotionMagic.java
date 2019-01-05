package org.trident.yearly.auto.testprograms;

import org.trident.yearly.auto.steps.MotionMagicStraightLine;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class TestMotionMagic extends AutoProgram
{

   @Override
   protected void defineSteps()
   {
      addStep(new MotionMagicStraightLine(96));
      addStep(new AutoStepDelay(2000));
      addStep(new MotionMagicStraightLine(48));
      addStep(new AutoStepStopAutonomous());

   }

   @Override
   public String toString()
   {
      return "Test Motion Magic";
   }

}
