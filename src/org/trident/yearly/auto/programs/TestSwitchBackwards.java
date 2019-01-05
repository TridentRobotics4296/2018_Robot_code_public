package org.trident.yearly.auto.programs;

import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.auto.steps.DriveZeroPoseParallelStepGroup;
import org.trident.yearly.auto.steps.ScaleAutoStepGroup;
import org.trident.yearly.auto.steps.SetBrakeModeStep;
import org.trident.yearly.auto.steps.SetHighGearStep;
import org.trident.yearly.auto.steps.SwitchBackwardsAutoStepGroup;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class TestSwitchBackwards extends BaseAutoProgram
{

   private double m_turnSpeed = 0.2;
   
   @Override
   protected void defineSteps()
   {
      if (startOnLeft())
      {
            // Switch only on our side - place cube on switch
            addStep(new SwitchBackwardsAutoStepGroup(-DistanceConstants.DEGREES_TO_SWITCH, m_turnSpeed));
      }
      else
      {
            // Switch only on our side - place cube on switch
            addStep(new SwitchBackwardsAutoStepGroup(DistanceConstants.DEGREES_TO_SWITCH, m_turnSpeed));
      }
      addStep(new SetHighGearStep(false));
      addStep(new SetBrakeModeStep(true));
      addStep(new AutoStepStopAutonomous());
   }

   @Override
   public String toString()
   {
      return "Test Switch Backwards";
   }
}
