package org.trident.yearly.auto.steps;

import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;

public class GetCubeFromScaleAutoStepGroup extends  AutoSerialStepGroup
{

   public GetCubeFromScaleAutoStepGroup(int rotation, double speed)
   {
      
      addStep(new SetBrakeModeStep(true));
      addStep(new SetHighGearStep(false));
      
      addStep(new MoveToPoseStep(Pose.PICKUP));
      
      addStep(new TurnByNDegreesNavXStep(rotation, speed));
      addStep(new AutoStepDelay(100));
      
      //Drive to cube
      addStep(new IntakeInStep());
      addStep(new DriveToCubeStep());
      //addStep(new IntakeOffStep()); Don't add so if it slips, it is pulled back
   }
   
}
