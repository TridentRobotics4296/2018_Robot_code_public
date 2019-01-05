package org.trident.yearly.auto.testprograms;

import org.trident.yearly.auto.steps.TurnByNDegreesNavXStep;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;


public class TESTNavXTurn extends AutoProgram
{
   public TESTNavXTurn()
   {
//      SmartDashboard.putNumber("Test Turn Angle", 0);
   }
   @Override
   protected void defineSteps()
   {
      addStep(new TurnByNDegreesNavXStep(-60, 0.2));
      addStep(new AutoStepStopAutonomous());
   }

   @Override
   public String toString()
   {
      return "TEST navX Turn";
   }

}
