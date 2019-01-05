package org.trident.yearly.auto.steps;

import org.trident.yearly.robot.WSSubsystems;
import org.trident.yearly.subsystems.Manipulator;
import org.trident.yearly.subsystems.poseables.Pose;
import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;

public class MoveToPoseStep extends AutoStep
{
   private Manipulator m_manipulator;
   private Pose m_pose;
   
   public MoveToPoseStep(Pose p_pose)
   {
      m_pose = p_pose;
   }

   @Override
   public void initialize()
   {
      m_manipulator = (Manipulator)Core.getSubsystemManager().getSubsystem(WSSubsystems.MANIPULATOR.getName());
   }

   @Override
   public void update()
   {
      super.update();
      m_manipulator.moveToPose(m_pose);
      if (m_manipulator.inCurrentPose())
      {
         setFinished(true);
      }
   }

   @Override
   public String toString()
   {
      return "MoveToPoseStep";
   }

}
