package org.trident.yearly.auto.steps;

import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;

public class SwitchAutoStepGroup extends AutoSerialStepGroup
{

   public SwitchAutoStepGroup(int rotation, double minSpeed)

   {
      addStep(new SetHighGearStep(false));
      addStep(new SetBrakeModeStep(true));
      
      addStep(new DriveZeroPoseParallelStepGroup(DistanceConstants.VERTICAL_TO_SWITCH, 0.5, Pose.SWITCH));
      
//      addStep(new AutoStepDelay(100));
//      addStep(new MotionMagicStraightLine(DistanceConstants.VERTICAL_TO_SWITCH));
//      addStep(new AutoStepDelay(500));
      
      addStep(new TurnByNDegreesNavXStep(rotation, minSpeed));
      addStep(new AutoStepDelay(50));

//      addStep(new MoveToPoseStep(Pose.SWITCH));
//      addStep(new AutoStepDelay(500));
//      addStep(new MotionMagicStraightLine(DistanceConstants.HORIZONTAL_TO_SWITCH));
      
      addStep(new SetBrakeModeStep(false));
      addStep(new DriveOverrideOutakeParallelStepGroup((DistanceConstants.HORIZONTAL_TO_SWITCH + 6), 5000));
      addStep(new SetBrakeModeStep(true));
      
      addStep(new IntakeOutStep());
      addStep(new AutoStepDelay(1000));
      
      addStep(new IntakeOffStep());
   }


}
