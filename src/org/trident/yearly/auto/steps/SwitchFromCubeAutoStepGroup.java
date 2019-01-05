package org.trident.yearly.auto.steps;

import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.AutoSerialStepGroup;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;

public class SwitchFromCubeAutoStepGroup extends AutoSerialStepGroup
{

   public SwitchFromCubeAutoStepGroup(int rotation, double speed) {
      //Back up and throw over side of switch
      addStep(new DrivePoseParallelStepGroup(-5, Pose.SWITCH)); //TODO check value 5
      addStep(new AutoStepDelay(50));
      
      addStep(new TurnByNDegreesNavXStep(rotation, speed));
      addStep(new MotionMagicStraightLine(5));
      
      addStep(new IntakeOutStep());
      addStep(new AutoStepDelay(2000));
      addStep(new IntakeOffStep());
   }
   
}
