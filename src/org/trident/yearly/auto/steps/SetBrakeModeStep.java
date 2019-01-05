package org.trident.yearly.auto.steps;

import org.trident.yearly.robot.WSSubsystems;
import org.trident.yearly.subsystems.Drive;
import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;

public class SetBrakeModeStep extends AutoStep
{
   private Drive m_drive;
   private boolean m_brake;
   
   public SetBrakeModeStep(boolean p_brake)
   {
      m_brake = p_brake;
   }
   
   @Override
   public void initialize()
   {
      m_drive = (Drive) Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName());
   }

   @Override
   public void update()
   {
      super.update();
      m_drive.setBrakeMode(m_brake);
      setFinished(true);
   }

   @Override
   public String toString()
   {
      return "Set brake mode step";
   }

}
