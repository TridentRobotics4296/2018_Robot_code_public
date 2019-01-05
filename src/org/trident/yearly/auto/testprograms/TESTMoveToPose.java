package org.trident.yearly.auto.testprograms;

import org.trident.yearly.auto.steps.MoveToPoseStep;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class TESTMoveToPose extends AutoProgram
{

   @Override
   protected void defineSteps()
   {
      addStep(new MoveToPoseStep(Pose.SWITCH));
      addStep(new AutoStepDelay(1000));
      addStep(new MoveToPoseStep(Pose.PICKUP));
      addStep(new AutoStepDelay(1000));
      addStep(new MoveToPoseStep(Pose.START));
      addStep(new AutoStepStopAutonomous());

   }

   @Override
   public String toString()
   {
      return "TESTMoveToPose";
   }

}
