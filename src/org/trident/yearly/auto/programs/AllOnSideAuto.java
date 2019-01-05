package org.trident.yearly.auto.programs;

import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.auto.steps.GetCubeFromScaleAutoStepGroup;
import org.trident.yearly.auto.steps.MotionMagicStraightLine;
import org.trident.yearly.auto.steps.MoveToPoseStep;
import org.trident.yearly.auto.steps.ScaleAutoStepGroup;
import org.trident.yearly.auto.steps.ScaleFromCubeAutoStepGroup;
import org.trident.yearly.auto.steps.SetHighGearStep;
import org.trident.yearly.auto.steps.SwitchAutoStepGroup;
import org.trident.yearly.auto.steps.SwitchBackwardsAutoStepGroup;
import org.trident.yearly.auto.steps.SwitchFromCubeAutoStepGroup;
import org.trident.yearly.auto.steps.ZeroManipulatorAutoStepGroup;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class AllOnSideAuto extends BaseAutoProgram
{

   @Override
   protected void defineSteps()
   {
      if (startOnLeft())
      {
         if (switchOnLeft() && scaleOnLeft())
         {
            // Switch and Scale are on our side - place cube on scale then switch
           // addStep(new ScaleAutoStepGroup(38, -10, 0.3));
            addStep(new GetCubeFromScaleAutoStepGroup(-DistanceConstants.DEGREES_TO_CUBE, 0.15));
            addStep(new SwitchFromCubeAutoStepGroup(-30, 0.15)); //TODO check if 30 is a good value
         }
         else if(switchOnLeft())
         {
            // Switch only on our side - place cube on switch
            addStep(new SwitchBackwardsAutoStepGroup(-DistanceConstants.DEGREES_TO_SWITCH, 0.15));
         }
         else if(scaleOnLeft())
         {
            // Scale only on our side - place cube on scale
           // addStep(new ScaleAutoStepGroup(90, 45, 0.15));
            addStep(new GetCubeFromScaleAutoStepGroup(-DistanceConstants.DEGREES_TO_CUBE, 0.15));
            addStep(new ScaleFromCubeAutoStepGroup(DistanceConstants.DEGREES_TO_CUBE, 0.15));
         }
         else
         {
            // Nothing on our side - cross auto
            addStep(new MotionMagicStraightLine(DistanceConstants.VERTICAL_TO_AUTO));
         }
      }
      else
      {
         if (!switchOnLeft() && !scaleOnLeft())
         {
            // Switch and Scale are on our side - place cube on scale then switch
          //  addStep(new ScaleAutoStepGroup(-90, -45, 0.15));
            addStep(new GetCubeFromScaleAutoStepGroup(DistanceConstants.DEGREES_TO_CUBE, 0.15));
            addStep(new SwitchFromCubeAutoStepGroup(30, 0.15));
         }
         else if(!switchOnLeft())
         {
            // Switch only on our side - place cube on switch
            addStep(new SwitchBackwardsAutoStepGroup(DistanceConstants.DEGREES_TO_SWITCH, 0.15));
         }
         else if(!scaleOnLeft())
         {
            // Scale only on our side - place cube on scale
            //addStep(new ScaleAutoStepGroup(-90, -45, 0.15));
            addStep(new GetCubeFromScaleAutoStepGroup(DistanceConstants.DEGREES_TO_CUBE, 0.15));
            addStep(new ScaleFromCubeAutoStepGroup(-DistanceConstants.DEGREES_TO_CUBE, 0.15));
         }
         else
         {
            // Nothing on our side - cross auto
            addStep(new MotionMagicStraightLine(DistanceConstants.VERTICAL_TO_AUTO));
         }
      }
      addStep(new AutoStepStopAutonomous());
   }

   @Override
   public String toString()
   {
      return "All On Side Auto";
   }

}
