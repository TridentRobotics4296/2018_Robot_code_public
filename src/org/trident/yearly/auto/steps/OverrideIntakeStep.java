package org.trident.yearly.auto.steps;

import org.wildstang.framework.auto.steps.AutoStep;

import edu.wpi.first.wpilibj.DriverStation;

public class OverrideIntakeStep extends AutoStep
{

   private double autoTimer;
   
   @Override
   public void initialize()
   {
      autoTimer = DriverStation.getInstance().getMatchTime();
   }

   @Override
   public void update()
   { 
      super.update();
      autoTimer = DriverStation.getInstance().getMatchTime();
      if(autoTimer > 13)
      {
         setFinished(true);
      }
   }

   @Override
   public String toString()
   {
      // TODO Auto-generated method stub
      return null;
   }
   
}
