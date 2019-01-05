package org.trident.yearly.auto.steps;

import org.trident.yearly.robot.WSSubsystems;
import org.trident.yearly.subsystems.Drive;
import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;


public class DriveToCubeStep extends AutoStep
{
   double distance;
   double xCorrection;
   private Drive m_drive;

   @Override
   public void initialize()
   {
      m_drive = (Drive) Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName());
      m_drive.setCubeFollowMode();
      m_drive.setHighGear(true);
   }

   @Override
   public void update()
   {
      super.update();
      if (m_drive.isCubeFollowFinished())
      {
         setFinished(true);
         m_drive.setOpenLoopDrive();
      }
   }

   @Override
   public String toString()
   {
      return "Drive to cube Step";
   }

}
