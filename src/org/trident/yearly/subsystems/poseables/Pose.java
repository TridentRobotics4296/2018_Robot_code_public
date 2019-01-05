package org.trident.yearly.subsystems.poseables;

public enum Pose
{
   NONE(0, PoseEndAction.OFF, 0, PoseEndAction.OFF, 0, PoseEndAction.OFF),
   START(PoseSetpoints.LIFT_START, PoseEndAction.OFF, PoseSetpoints.ARM_START, PoseEndAction.OFF, PoseSetpoints.WRIST_START, PoseEndAction.HOLD),
   PICKUP(PoseSetpoints.LIFT_PICKUP, PoseEndAction.OFF, PoseSetpoints.ARM_PICKUP, PoseEndAction.OFF, PoseSetpoints.WRIST_PICKUP, PoseEndAction.HOLD),
   SWITCH(PoseSetpoints.LIFT_SWITCH, PoseEndAction.OFF, PoseSetpoints.ARM_SWITCH, PoseEndAction.OFF, PoseSetpoints.WRIST_SWITCH, PoseEndAction.HOLD),
   SCALE(PoseSetpoints.LIFT_SCALE, PoseEndAction.HOLD, PoseSetpoints.ARM_SCALE, PoseEndAction.HOLD, PoseSetpoints.WRIST_SCALE, PoseEndAction.HOLD),
   CLIMB(PoseSetpoints.LIFT_CLIMB, PoseEndAction.HOLD, PoseSetpoints.ARM_CLIMB, PoseEndAction.HOLD, PoseSetpoints.WRIST_CLIMB, PoseEndAction.OFF);
   
   private int m_liftPos;
   private int m_armPos;
   private int m_wristPos;
   private PoseEndAction m_liftEndAction;
   private PoseEndAction m_armEndAction;
   private PoseEndAction m_wristEndAction;
   
   Pose(int p_liftPos, PoseEndAction p_liftEnd, int p_armPos, PoseEndAction p_armEnd, int p_wristPos, PoseEndAction p_wristEnd)
   {
      m_liftPos = p_liftPos;
      m_armPos = p_armPos;
      m_wristPos = p_wristPos;
   }

   public int getLiftPos()
   {
      return m_liftPos;
   }

   public int getArmPos()
   {
      return m_armPos;
   }

   public int getWristPos()
   {
      return m_wristPos;
   }

   public PoseEndAction getLiftEndAction()
   {
      return m_liftEndAction;
   }

   public PoseEndAction getArmEndAction()
   {
      return m_armEndAction;
   }

   public PoseEndAction getWristEndAction()
   {
      return m_wristEndAction;
   }
   
   
}


