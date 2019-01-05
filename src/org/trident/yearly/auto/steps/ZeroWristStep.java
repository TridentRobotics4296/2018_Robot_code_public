package org.trident.yearly.auto.steps;

import org.trident.yearly.robot.WSSubsystems;
import org.trident.yearly.subsystems.Claw;
import org.trident.yearly.subsystems.Drive;
import org.trident.yearly.subsystems.Manipulator;
import org.trident.yearly.subsystems.poseables.Wrist;
import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;

public class ZeroWristStep extends AutoStep
{

   private double m_wristSpeed;
   private Manipulator m_manipulator;
   
   public ZeroWristStep(double p_wristSpeed)
   {
      m_wristSpeed = p_wristSpeed;
   }
   
   @Override
   public void initialize()
   {
      m_manipulator = (Manipulator) Core.getSubsystemManager().getSubsystem(WSSubsystems.MANIPULATOR.getName());
   }

   @Override
   public void update()
   {
     super.update();
     if(m_manipulator.getWrist().isAtUpperLimit()) 
     {
        m_manipulator.setWristSpeed(0);
        setFinished(true);
     }
     else
     {
        m_manipulator.setWristSpeed(m_wristSpeed);
     }
   }

   @Override
   public String toString()
   {
      return "Zero Wrist";
   }

}
