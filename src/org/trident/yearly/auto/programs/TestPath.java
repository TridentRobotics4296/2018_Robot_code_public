package org.trident.yearly.auto.programs;

import org.trident.yearly.auto.steps.MotionMagicStraightLine;
import org.trident.yearly.auto.steps.PathFollowerStep;
import org.trident.yearly.auto.steps.SetHighGearStep;
import org.trident.yearly.auto.steps.TurnByNDegreesNavXStep;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class TestPath extends AutoProgram
{

   private String m_path = PathNameConstants.ARC_LEFT;
   
   @Override
   protected void defineSteps()
   {
      addStep(new SetHighGearStep(true));
      addStep(new AutoStepDelay(1000));
      addStep(new PathFollowerStep(m_path));
      
      addStep(new AutoStepStopAutonomous());
   }

   @Override
   public String toString()
   {
      return "Test Path";
   }

}
