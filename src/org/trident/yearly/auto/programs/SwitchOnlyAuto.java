package org.trident.yearly.auto.programs;

import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.auto.steps.DriveDistanceStraightStep;
import org.trident.yearly.auto.steps.DriveZeroParallelStepGroup;
import org.trident.yearly.auto.steps.IntakeOffStep;
import org.trident.yearly.auto.steps.IntakeOutStep;
import org.trident.yearly.auto.steps.MotionMagicStraightLine;
import org.trident.yearly.auto.steps.MoveToPoseStep;
import org.trident.yearly.auto.steps.SetBrakeModeStep;
import org.trident.yearly.auto.steps.SetHighGearStep;
import org.trident.yearly.auto.steps.SwitchAutoStepGroup;
import org.trident.yearly.auto.steps.SwitchCenterLeftAutoStepGroup;
import org.trident.yearly.auto.steps.SwitchCenterRightAutoStepGroup;
import org.trident.yearly.auto.steps.TurnByNDegreesNavXStep;
import org.trident.yearly.auto.steps.ZeroManipulatorAutoStepGroup;
import org.trident.yearly.auto.testprograms.TurnTesting;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class SwitchOnlyAuto extends BaseAutoProgram
{

   private double m_turnSpeed = 0.2;
   
   @Override
   protected void defineSteps()
   {
      addStep(new SetBrakeModeStep(true));
      addStep(new SetHighGearStep(true));
      
      if (startOnLeft())
      {
         if (switchOnLeft())
         {
            // Switch is on our side - place cube on switch
            addStep(new SwitchAutoStepGroup(DistanceConstants.DEGREES_TO_SWITCH, m_turnSpeed));
         }
         else
         {
            // Cross the line
            addStep(new DriveZeroParallelStepGroup(DistanceConstants.VERTICAL_TO_AUTO));
         }
      }
      else if (startOnRight())
      {
         // We're on the right
         if (switchOnLeft())
         {
            // Cross the line
            addStep(new DriveZeroParallelStepGroup(DistanceConstants.VERTICAL_TO_AUTO));
         }
         else
         {
            // Switch is on our side - place cube on switch
            addStep(new SwitchAutoStepGroup(-(DistanceConstants.DEGREES_TO_SWITCH), m_turnSpeed));
         }
      }
      else if (startCenter())
      {
         if (switchOnLeft()) {
            addStep(new SwitchCenterLeftAutoStepGroup(-90, m_turnSpeed));
         }
         else 
         {
            addStep(new SwitchCenterRightAutoStepGroup(m_turnSpeed)); 
         }
         
       }
      addStep(new SetHighGearStep(false));
      addStep(new SetBrakeModeStep(true));
      addStep(new AutoStepStopAutonomous());
      }
   @Override
   public String toString()
   {
      return "Switch Only";
   }
}
