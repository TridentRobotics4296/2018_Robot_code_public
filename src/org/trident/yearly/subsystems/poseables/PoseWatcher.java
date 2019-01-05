package org.trident.yearly.subsystems.poseables;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PoseWatcher implements Runnable
{
   private boolean m_moving = false;
   private boolean m_interrupted = false;
   
   private Poseable m_poseable;
   private PoseEndAction m_endAction;
   
   public PoseWatcher(Poseable p_poseable, PoseEndAction p_endAction)
   {
      m_poseable = p_poseable;
      m_endAction = p_endAction;
   }
   
   public void run()
   {
      m_moving = true;
      while (m_moving)
      {
         if (m_poseable.inPosition())
         {
            m_moving = false;
            m_poseable.setMovingToPose(false);
            if (m_endAction == PoseEndAction.HOLD)
            {
               m_poseable.holdPosition();
            }
            else if (m_endAction == PoseEndAction.OFF)
            {
               m_poseable.turnOff();
            }
         }
         
         try
         {
            Thread.sleep(10);
         }
         catch (InterruptedException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         
      }
   }
   
   public void start()
   {
   }
   
   public void stop()
   {
      m_moving = false;
      m_interrupted = true;
      m_poseable.setMovingToPose(false);

      if (m_endAction == PoseEndAction.HOLD)
      {
         m_poseable.holdPosition();
      }
      else if (m_endAction == PoseEndAction.OFF)
      {
         m_poseable.turnOff();
      }
   }
   
   public boolean isInterrupted()
   {
      return m_interrupted;
   }
   
   public boolean isMoving()
   {
      return m_moving;
   }
   
}
