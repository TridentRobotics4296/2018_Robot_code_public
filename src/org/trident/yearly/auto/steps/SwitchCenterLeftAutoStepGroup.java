   package org.trident.yearly.auto.steps;

   import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.subsystems.Drive;
import org.trident.yearly.subsystems.poseables.Pose;
   import org.wildstang.framework.auto.steps.AutoSerialStepGroup;
   import org.wildstang.framework.auto.steps.control.AutoStepDelay;

import com.sun.glass.ui.Robot;

public class SwitchCenterLeftAutoStepGroup extends AutoSerialStepGroup
{

      private int m_distanceToSwitch = (int)(DistanceConstants.VERTICAL_TO_SWITCH - Drive.ROBOT_LENGTH);
   
      public SwitchCenterLeftAutoStepGroup(int degrees, double minSpeed)

      {
         addStep(new SetHighGearStep(true));
         addStep(new SetBrakeModeStep(true));
         
         addStep(new DriveZeroParallelStepGroup(m_distanceToSwitch - 60));
         
         addStep(new TurnByNDegreesNavXStep(degrees, minSpeed));
         addStep(new DrivePoseParallelStepGroup((int) (130 - Drive.ROBOT_WIDTH), Pose.SWITCH)); //Changed from 139
         addStep(new AutoStepDelay(50));

         addStep(new TurnByNDegreesNavXStep(-degrees, minSpeed));
         addStep(new SetBrakeModeStep(false));                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
         addStep(new DriveOverrideOutakeParallelStepGroup(66, 5000));
         addStep(new AutoStepDelay(500));
         addStep(new SetBrakeModeStep(true));
         
         addStep(new IntakeOutStep());
         addStep(new AutoStepDelay(1000));
         
         addStep(new IntakeOffStep());
      }


   }
