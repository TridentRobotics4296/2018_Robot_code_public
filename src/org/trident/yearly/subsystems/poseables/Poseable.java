package org.trident.yearly.subsystems.poseables;

public interface Poseable
{
   public void moveTo(int position, PoseEndAction p_endAction);
   
   public void cancelPose();

   public boolean inPosition();
   
   public void holdPosition();
   
   public void turnOff();
   public void setMovingToPose(boolean isPosing);
   public boolean isMovingToPose();
   
   public int getCurrentPosition();
   public int getAllowableError();
   public int getTargetPosition();

   public void atUpperLimit();
   public void atLowerLimit();

}
