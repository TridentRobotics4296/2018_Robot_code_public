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

public class TestScale extends BaseAutoProgram
{

   private double m_turnSpeed = 0.2;
   
   @Override
   protected void defineSteps()
   {
      if (startOnLeft())
      {
           // Scale only on our side - place cube on scale
           addStep(new ScaleAutoStepGroup(0, -(DistanceConstants.DEGREES_TO_CUBE), 0, m_turnSpeed));
      }
      else
      {
           // Scale only on our side - place cube on scale
           addStep(new ScaleAutoStepGroup(-(DistanceConstants.DEGREES_TO_SCALE), DistanceConstants.DEGREES_TO_CUBE, 25, m_turnSpeed)); //Originally 16
      }
      addStep(new SetHighGearStep(false));
      addStep(new SetBrakeModeStep(true));
      addStep(new AutoStepStopAutonomous());
   }

   @Override
   public String toString()
   {
      return "Test Scale";
   }
}
