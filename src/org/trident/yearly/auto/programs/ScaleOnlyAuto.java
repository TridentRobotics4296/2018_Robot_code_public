package org.trident.yearly.auto.programs;

import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.auto.steps.DriveDistanceStraightStep;
import org.trident.yearly.auto.steps.DriveZeroPoseParallelStepGroup;
import org.trident.yearly.auto.steps.IntakeOffStep;
import org.trident.yearly.auto.steps.IntakeOutStep;
import org.trident.yearly.auto.steps.MotionMagicStraightLine;
import org.trident.yearly.auto.steps.MoveToPoseStep;
import org.trident.yearly.auto.steps.ScaleAutoStepGroup;
import org.trident.yearly.auto.steps.SetBrakeModeStep;
import org.trident.yearly.auto.steps.SetHighGearStep;
import org.trident.yearly.auto.steps.SwitchAutoStepGroup;
import org.trident.yearly.auto.steps.TurnByNDegreesNavXStep;
import org.trident.yearly.auto.steps.ZeroManipulatorAutoStepGroup;
import org.trident.yearly.auto.testprograms.TurnTesting;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class ScaleOnlyAuto extends BaseAutoProgram
{
   
   private double m_turnSpeed = 0.2;
   
   @Override
   protected void defineSteps()
   {  
      if (startOnLeft())
      {
         if (scaleOnLeft())
         {
            // Switch is on our side - place cube on switch
        //    addStep(new ScaleAutoStepGroup(DistanceConstants.DEGREES_TO_SCALE, -DistanceConstants.DEGREES_TO_CUBE, m_turnSpeed));
         }
         else
         {
            // Cross the line
            addStep(new DriveZeroPoseParallelStepGroup(-DistanceConstants.VERTICAL_TO_AUTO, 0.5, Pose.SWITCH));
         }
      }
      else
      {
         // We're on the right
         if (scaleOnLeft())
         {
            // Cross the line
            addStep(new DriveZeroPoseParallelStepGroup(-DistanceConstants.VERTICAL_TO_AUTO, 0.5, Pose.SWITCH));
         }
         else
         {
            // Switch is on our side - place cube on switch
           // addStep(new ScaleAutoStepGroup(-DistanceConstants.DEGREES_TO_SCALE, DistanceConstants.DEGREES_TO_CUBE, 0.3));
         }
      }
      addStep(new SetHighGearStep(false));
      addStep(new SetBrakeModeStep(true));
      addStep(new AutoStepStopAutonomous());
   }

   @Override
   public String toString()
   {
      return "Scale Only";
   }

}
