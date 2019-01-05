package org.trident.yearly.auto.programs;

import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.auto.steps.DriveZeroParallelStepGroup;
import org.trident.yearly.auto.steps.DriveZeroPoseParallelStepGroup;
import org.trident.yearly.auto.steps.GetCubeFromScaleAutoStepGroup;
import org.trident.yearly.auto.steps.MotionMagicStraightLine;
import org.trident.yearly.auto.steps.MoveToPoseStep;
import org.trident.yearly.auto.steps.ScaleAutoStepGroup;
import org.trident.yearly.auto.steps.SetBrakeModeStep;
import org.trident.yearly.auto.steps.SetHighGearStep;
import org.trident.yearly.auto.steps.SwitchAutoStepGroup;
import org.trident.yearly.auto.steps.SwitchBackwardsAutoStepGroup;
import org.trident.yearly.auto.steps.SwitchFromCubeAutoStepGroup;
import org.trident.yearly.auto.steps.ZeroManipulatorAutoStepGroup;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class SwitchOrScale extends BaseAutoProgram
{

   private double m_turnSpeed = 0.2;
   
   @Override
   protected void defineSteps()
   {
      if (startOnLeft())
      {
         if (switchOnLeft())
         {
            // Switch only on our side - place cube on switch
            addStep(new SwitchBackwardsAutoStepGroup(-DistanceConstants.DEGREES_TO_SWITCH, m_turnSpeed));
         }
         else if(scaleOnLeft())
         {
            // Scale only on our side - place cube on scale
            addStep(new ScaleAutoStepGroup(0, -(DistanceConstants.DEGREES_TO_CUBE), 0, m_turnSpeed));
         }
         else
         {
            // Nothing on our side - cross auto
            addStep(new DriveZeroPoseParallelStepGroup(-DistanceConstants.VERTICAL_TO_AUTO, 0.5, Pose.SWITCH));
         }
      }
      else
      {
         if (!switchOnLeft())
         {
            // Switch only on our side - place cube on switch
            addStep(new SwitchBackwardsAutoStepGroup(DistanceConstants.DEGREES_TO_SWITCH, m_turnSpeed));
         }
         else if(!scaleOnLeft())
         {
            // Scale only on our side - place cube on scale
            addStep(new ScaleAutoStepGroup(-(DistanceConstants.DEGREES_TO_SCALE), DistanceConstants.DEGREES_TO_CUBE, 25, m_turnSpeed)); //Originally 16
         }
         else
         {
            // Nothing on our side - cross auto
            addStep(new DriveZeroPoseParallelStepGroup(-DistanceConstants.VERTICAL_TO_AUTO, 0.5, Pose.SWITCH));
         }
      }
      addStep(new SetHighGearStep(false));
      addStep(new SetBrakeModeStep(true));
      addStep(new AutoStepStopAutonomous());
   }

   @Override
   public String toString()
   {
      return "Switch Priority Auto";
   }
}
