package org.trident.yearly.auto.programs;

import org.trident.yearly.auto.DistanceConstants;
import org.trident.yearly.auto.steps.DriveZeroParallelStepGroup;
import org.trident.yearly.auto.steps.MotionMagicStraightLine;
import org.trident.yearly.auto.steps.SetHighGearStep;
import org.trident.yearly.auto.steps.TurnByNDegreesNavXStep;
import org.trident.yearly.auto.steps.ZeroArmStep;
import org.trident.yearly.auto.steps.ZeroLiftStep;
import org.trident.yearly.auto.steps.ZeroWristStep;
import org.trident.yearly.subsystems.drive.DriveConstants;
import org.wildstang.framework.auto.AutoProgram;
import org.wildstang.framework.auto.steps.control.AutoStepDelay;
import org.wildstang.framework.auto.steps.control.AutoStepStopAutonomous;

public class Baseline extends AutoProgram
{

   @Override
   protected void defineSteps()
   {
      //addStep(new DriveZeroParallelStepGroup(DistanceConstants.VERTICAL_TO_AUTO));
      addStep(new MotionMagicStraightLine(-DistanceConstants.VERTICAL_TO_AUTO));
      addStep(new AutoStepStopAutonomous());
   }
   
   @Override
   public String toString()
   {
      return "Baseline";
   }

}
