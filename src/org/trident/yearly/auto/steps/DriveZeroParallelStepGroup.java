package org.trident.yearly.auto.steps;

import org.wildstang.framework.auto.steps.AutoParallelStepGroup;

public class DriveZeroParallelStepGroup extends AutoParallelStepGroup
{
    public DriveZeroParallelStepGroup(int driveDistance) {
       addStep(new ZeroManipulatorAutoStepGroup(0.7));
       addStep(new MotionMagicStraightLine(driveDistance));
    }
}
