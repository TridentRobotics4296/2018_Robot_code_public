package org.trident.yearly.auto.programs;

import org.trident.yearly.auto.steps.MotionMagicStraightLine;
import org.trident.yearly.auto.steps.SetBrakeModeStep;
import org.trident.yearly.auto.steps.SetHighGearStep;
import org.trident.yearly.auto.steps.TurnByNDegreesNavXStep;
import org.trident.yearly.auto.steps.ZeroManipulatorAutoStepGroup;
import org.trident.yearly.subsystems.Drive;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class BaseLineFromCenterAuto extends AutoProgram
{

   private int m_angledDistance = (int)((Math.cos(15) * (Math.PI / 180)) * (120 - Drive.ROBOT_LENGTH)); 
   
   @Override
   protected void defineSteps()
   {
      addStep(new SetBrakeModeStep(true));
      addStep(new SetHighGearStep(false));
      addStep(new MotionMagicStraightLine(5));
      addStep(new AutoStepDelay(200));
      addStep(new TurnByNDegreesNavXStep(30, 0.15));
      addStep(new AutoStepDelay(200));
      addStep(new MotionMagicStraightLine(m_angledDistance));
      addStep(new ZeroManipulatorAutoStepGroup(0.5));
      addStep(new AutoStepStopAutonomous());
   }

   @Override
   public String toString()
   {
      return "Baseline from Center";
   }

}
