package org.trident.yearly.auto.steps;

import org.trident.yearly.robot.WSSubsystems;
import org.trident.yearly.subsystems.Drive;
import org.trident.yearly.subsystems.drive.DriveConstants;
import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MotionMagicStraightLine extends AutoStep
{


   private double m_ticks;
   private Drive m_drive;
   private boolean m_started = false;
   
   private static final double ONE_ROTATION_INCHES = Drive.WHEEL_DIAMETER_INCHES * Math.PI;
   
   // Tolerance - in rotations. The numerator is in inches
   private static final double TOLERANCE = Drive.ENCODER_CPR; //Changed to 10 times larger
   
   public MotionMagicStraightLine(double p_inches)
   {
      m_ticks = (p_inches / ONE_ROTATION_INCHES) * Drive.ENCODER_CPR;
   }
   
   @Override
   public void initialize()
   {
      m_drive = (Drive)Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName());

      m_drive.setMotionMagicMode(false, DriveConstants.MM_DRIVE_F_GAIN);
      m_drive.resetEncoders();
      /*m_drive.setHighGear(true);
      m_drive.setBrakeMode(true);*/
   }

   @Override
   public void update()
   {
      super.update();
      SmartDashboard.putBoolean("At Motion Magic Target", Math.abs((Math.abs(m_ticks) - (Math.abs(m_drive.getRightEncoderValue() / 4096)))) <= TOLERANCE);
      if (!m_started)
      {
         m_drive.setMotionMagicTargetAbsolute(m_ticks, m_ticks);
         m_started = true;
      }
      else
      {
         SmartDashboard.putNumber("MM target", m_ticks);
         SmartDashboard.putNumber("MM Difference", Math.abs((Math.abs(m_ticks) - (Math.abs(m_drive.getLeftEncoderValue() / 4096)))));
         SmartDashboard.putNumber("Tolerance", TOLERANCE);
         SmartDashboard.putNumber("Encoder Velocity", m_drive.getRightEncoderVelocity()); 

         // Check if we've gone far enough
//         if (Math.abs((m_drive.getRightSensorValue() / 4096)) >= m_ticks)
         if (Math.abs((Math.abs(m_ticks) - (Math.abs(m_drive.getRightEncoderValue())))) <= TOLERANCE)
         {
            //m_drive.setOpenLoopDrive();
            m_drive.setBrakeMode(true);
            setFinished(true);
         }
      }
   }

   @Override
   public String toString()
   {
      return "Motion Magic Straight Drive";
   }

}
