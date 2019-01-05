package org.trident.yearly.auto.testprograms;

import org.trident.yearly.auto.steps.ClawCloseStep;
import org.trident.yearly.auto.steps.ClawOpenStep;
import org.trident.yearly.auto.steps.IntakeInStep;
import org.trident.yearly.auto.steps.IntakeOffStep;
import org.trident.yearly.auto.steps.IntakeOutStep;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class TestClawSteps extends AutoProgram
{

   @Override
   protected void defineSteps()
   {
      addStep(new IntakeInStep());
      addStep(new AutoStepDelay(1000));
      addStep(new IntakeOffStep());
      addStep(new AutoStepDelay(500));
      addStep(new IntakeOutStep());
      addStep(new AutoStepDelay(1000));
      addStep(new IntakeOffStep());
      addStep(new AutoStepDelay(500));
      addStep(new ClawOpenStep());
      addStep(new AutoStepDelay(1000));
      addStep(new ClawCloseStep());
      addStep(new AutoStepStopAutonomous());

   }

   @Override
   public String toString()
   {
      return "Test Claw";
   }

}
