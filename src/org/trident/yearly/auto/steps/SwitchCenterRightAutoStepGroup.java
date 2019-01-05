   package org.trident.yearly.auto.steps;

   import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.subsystems.Drive;
import org.trident.yearly.subsystems.poseables.Pose;
   import org.wildstang.framework.auto.steps.AutoSerialStepGroup;
   import org.wildstang.framework.auto.steps.control.AutoStepDelay;

public class SwitchCenterRightAutoStepGroup extends AutoSerialStepGroup
{

      public SwitchCenterRightAutoStepGroup(double minSpeed)

      {
         addStep(new SetHighGearStep(true));
         addStep(new SetBrakeModeStep(true));
         
         addStep(new DriveZeroParallelStepGroup(20));

         addStep(new TurnByNDegreesNavXStep(DistanceConstants.CENTER_ANGLE_TO_SWITCH, minSpeed));
         addStep(new SetBrakeModeStep(false));
         addStep(new DrivePoseParallelStepGroup(DistanceConstants.CENTER_ANGLED_DISTANCE_TO_SWITCH, Pose.SWITCH));
         addStep(new SetBrakeModeStep(true));
         addStep(new AutoStepDelay(50));
         
         addStep(new IntakeOutStep());
         addStep(new AutoStepDelay(1000));
         
         addStep(new IntakeOffStep());
      }


   }
