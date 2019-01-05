package org.trident.yearly.auto.steps;

import org.trident.yearly.robot.WSSubsystems;
import org.trident.yearly.subsystems.Manipulator;
import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;

public class ZeroArmStep extends AutoStep
{
   private double m_armSpeed;
   private Manipulator m_manipulator;
   
   public ZeroArmStep(double p_armSpeed)
   {
      m_armSpeed = p_armSpeed;
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
     if(m_manipulator.getArm().isAtLowerLimit()) 
     {
        m_manipulator.setArmSpeed(0);
        setFinished(true);
     }
     else
     {
        m_manipulator.setArmSpeed(m_armSpeed);
     }
   }

   @Override
   public String toString()
   {
      return "Zero Arm";
   }

}

