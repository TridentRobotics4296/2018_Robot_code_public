package org.trident.yearly.auto.steps;

import org.trident.yearly.robot.WSSubsystems;
import org.trident.yearly.subsystems.Claw;
import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;

//This is an autonomous step which turns on the belt/feed 

public class IntakeInStep extends AutoStep
{
   private Claw claw;

   public void initialize()
   {
      claw = (Claw) Core.getSubsystemManager().getSubsystem(WSSubsystems.CLAW.getName());
   }

   @Override
   public void update()
   {
      super.update();
      claw.clawIn();
      setFinished(true);
   }

   @Override
   public String toString()
   {
      return "Intake On Step";
   }

}
