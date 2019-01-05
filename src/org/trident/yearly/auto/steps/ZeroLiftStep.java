package org.trident.yearly.auto.steps;

import org.trident.yearly.robot.WSSubsystems;
import org.trident.yearly.subsystems.Manipulator;
import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;

public class ZeroLiftStep extends AutoStep
{
   private double m_liftSpeed;
   private Manipulator m_manipulator;
   
   public ZeroLiftStep(double p_liftSpeed)
   {
      m_liftSpeed = p_liftSpeed;
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
     if(m_manipulator.getLift().isAtLowerLimit()) 
     {
        m_manipulator.setArmSpeed(0);
        setFinished(true);
     }
     else
     {
        m_manipulator.moveLiftDown(m_liftSpeed);
     }
   }

   @Override
   public String toString()
   {
      return "Zero Lift";
   }

}
