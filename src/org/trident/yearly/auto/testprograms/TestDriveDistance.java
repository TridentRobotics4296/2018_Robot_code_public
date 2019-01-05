package org.trident.yearly.auto.testprograms;

import org.trident.yearly.auto.steps.DriveDistanceStraightStep;
import org.trident.yearly.auto.steps.SetBrakeModeStep;
import org.trident.yearly.auto.steps.SetHighGearStep;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class TestDriveDistance extends AutoProgram
{

   @Override
   protected void defineSteps()
   {
//      addStep(new SetHighGearStep(true));
//    addStep(new AutoStepDelay(1000));
//      addStep(new SetHighGearStep(false));
//    addStep(new AutoStepDelay(1000));
      
      addStep(new SetBrakeModeStep(true));
      addStep(new SetHighGearStep(false));
      addStep(new DriveDistanceStraightStep(0.7, 36));
      addStep(new AutoStepDelay(2000));
      addStep(new DriveDistanceStraightStep(0.5, -24));
      addStep(new AutoStepStopAutonomous());

   }

   @Override
   public String toString()
   {
      return "Test Drive Distance";
   }

}

