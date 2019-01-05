package org.trident.yearly.auto.testprograms;

import org.trident.yearly.auto.steps.TurnByNDegreesStep;
import org.wildstang.framework.auto.AutoProgram;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnTesting extends AutoProgram
{
   public TurnTesting()
   {
//      SmartDashboard.putNumber("Test Turn Angle", 0);
   }
   @Override
   protected void defineSteps()
   {
      addStep(new TurnByNDegreesStep(60, 0.38));
   }

   @Override
   public String toString()
   {
      return "Turn Tester";
   }

   

}
