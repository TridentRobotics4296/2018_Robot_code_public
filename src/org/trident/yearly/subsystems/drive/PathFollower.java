package org.trident.yearly.subsystems.drive;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.trident.yearly.robot.WSSubsystems;
import org.trident.yearly.subsystems.Drive;
import org.wildstang.framework.core.Core;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PathFollower
{
   
   private boolean m_running = false;

   private Path m_path;
   private TalonSRX m_left;
   private TalonSRX m_right;
   
   private SetValueMotionProfile m_mpEnable = SetValueMotionProfile.Disable;
   private MotionProfileStatus m_leftStatus = new MotionProfileStatus();
   private MotionProfileStatus m_rightStatus = new MotionProfileStatus();

   private double pValue = 1.2;
   private double iValue = 0;
   private double dValue = 20;
   
   private double leftError;
   private double leftTarget;
   private double leftPos;
   private double leftVel;
 
   private double rightError;
   private double rightTarget;
   private double rightPos;
   private double rightVel;
   
   private BufferedWriter writer;
   private long startTime;
   
   public PathFollower(Path p_path, TalonSRX p_left, TalonSRX p_right)
   {
      m_path = p_path;
      m_left = p_left;
      m_right = p_right;

      m_left.changeMotionControlFramePeriod(10);
      m_right.changeMotionControlFramePeriod(10);

      // Path following profile
//      m_left.setProfile(0);
//      m_right.setProfile(0);
    ((Drive)(Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName()))).setTalonPID(p_left, DriveConstants.PATH_P_GAIN, DriveConstants.PATH_I_GAIN, DriveConstants.PATH_D_GAIN, DriveConstants.PATH_F_GAIN);
    ((Drive)(Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName()))).setTalonPID(p_right, DriveConstants.PATH_P_GAIN, DriveConstants.PATH_I_GAIN, DriveConstants.PATH_D_GAIN, DriveConstants.PATH_F_GAIN);
//
    writer = null;
    startTime = System.currentTimeMillis();
    String path = "/home/lvuser/logs/pathErrorTest.csv";
    
    try
   {
      writer = new BufferedWriter(new FileWriter(path));
      writer.write("test");
      SmartDashboard.putBoolean("Writting", true);
   }
   catch (IOException e)
   {
      // TODO Auto-generated catch block
      e.printStackTrace();
   }
    
      fillPathBuffers();
   }
   
   public void start()
   {
      //System.out.println("PathFollower.start() called");
//      Thread t = new Thread(this);
      m_running = true;
//      t.start();

      m_notifer.startPeriodic(0.005);

      m_mpEnable = SetValueMotionProfile.Enable;
      m_left.set(ControlMode.MotionProfile, m_mpEnable.value);
      m_right.set(ControlMode.MotionProfile, m_mpEnable.value);

   }
   
   public void stop()
   {

      try
      {
         writer.flush();
         writer.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      
      m_running = false;

      /*
       * Let's clear the buffer just in case user decided to disable in the
       * middle of an MP, and now we have the second half of a profile just
       * sitting in memory.
       */
      m_left.clearMotionProfileTrajectories();
      m_right.clearMotionProfileTrajectories();
      
      /* When we do re-enter motionProfile control mode, stay disabled. */
      m_mpEnable = SetValueMotionProfile.Disable;
      
      m_left.set(ControlMode.MotionProfile, m_mpEnable.value);
      m_right.set(ControlMode.MotionProfile, m_mpEnable.value);
      
      m_notifer.stop();
   }
   

   public void update()
   {
      //System.out.println("PathFollower.update() called");

//      while (m_running)
//      {
//         System.out.println("PathFollower.run() running");

//         SmartDashboard.putNumber("PATH P", pValue);
//         SmartDashboard.putNumber("PATH I", iValue);
//         SmartDashboard.putNumber("PATH D", dValue);

         leftError = m_left.getClosedLoopError(0);
         leftTarget = m_left.getClosedLoopTarget(0);
         leftPos = m_left.getActiveTrajectoryPosition();
         leftVel = m_left.getActiveTrajectoryVelocity();
     
         rightError = m_right.getClosedLoopError(0);
         rightTarget = m_right.getClosedLoopTarget(0);
         rightPos = m_right.getActiveTrajectoryPosition();
         rightVel = m_right.getActiveTrajectoryVelocity();
     
         SmartDashboard.putNumber("Path Following Left Error", leftError);
         SmartDashboard.putNumber("Path Following Right Error", rightError);
 
         try
         {
            writer.write(System.currentTimeMillis() - startTime + "," + leftError + "," + rightError +"\n");
         }
         catch (IOException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         
         pValue = SmartDashboard.getNumber("PATH P", 1.2);
         iValue = SmartDashboard.getNumber("PATH I", 0.0);
         dValue = SmartDashboard.getNumber("PATH D", 20);
      
         DriveConstants.PATH_P_GAIN = pValue;
         DriveConstants.PATH_I_GAIN = iValue;
         DriveConstants.PATH_D_GAIN = dValue;
         
         ((Drive)(Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName()))).setTalonPID(m_left, DriveConstants.PATH_P_GAIN, DriveConstants.PATH_I_GAIN, DriveConstants.PATH_D_GAIN, DriveConstants.PATH_F_GAIN);
         ((Drive)(Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName()))).setTalonPID(m_right, DriveConstants.PATH_P_GAIN, DriveConstants.PATH_I_GAIN, DriveConstants.PATH_D_GAIN, DriveConstants.PATH_F_GAIN);
         
         m_left.getMotionProfileStatus(m_leftStatus);
         m_right.getMotionProfileStatus(m_rightStatus);
         
         if (m_leftStatus.hasUnderrun)
         {
            System.out.println("Left Talon has buffer underrun");
         }
         if (m_rightStatus.hasUnderrun)
         {
            System.out.println("Right Talon has buffer underrun");
         }

         if (m_leftStatus.activePointValid && m_leftStatus.isLast)
         {
            SmartDashboard.putBoolean("Last Point", true);
            m_running = false;
         }
         
         
//      }
      
   }
   
   public boolean isActive()
   {
      return m_running;
   }
   
   class PeriodicRunnable implements java.lang.Runnable
   {
      public void run()
      {
         m_left.processMotionProfileBuffer();
         //System.out.println("Top buffer size: " + m_left.getMotionProfileTopLevelBufferCount());
         m_right.processMotionProfileBuffer();
      }
   }

   Notifier m_notifer = new Notifier(new PeriodicRunnable());

   
   private void fillPathBuffers()
   {
      fillPathBuffers(m_path.getLeft().getTalonPoints(), m_path.getRight().getTalonPoints(), m_path.getLeft().getTrajectoryPoints().length - 1);
   }

   private void fillPathBuffers(ArrayList<TrajectoryPoint> leftPoints, ArrayList<TrajectoryPoint> rightPoints, int totalCnt)
   {
      SmartDashboard.putNumber("NumPoints", totalCnt);
      /* create an empty point */
      //System.out.println("PathFollower.fillPathBuffers() called");

      /* did we get an underrun condition since last time we checked ? */
      if (m_leftStatus.hasUnderrun)
      {
         DriverStation.reportError("Left drive has underrun", false);
         m_left.clearMotionProfileHasUnderrun(0);
      }
      if (m_rightStatus.hasUnderrun)
      {
         DriverStation.reportError("Right drive has underrun", false);
         m_right.clearMotionProfileHasUnderrun(0);
      }
      
      /*
       * just in case we are interrupting another MP and there is still buffer
       * points in memory, clear it.
       */
      m_left.clearMotionProfileTrajectories();
      m_right.clearMotionProfileTrajectories();

      /* This is fast since it's just into our TOP buffer */
      for (int i = 0; i < totalCnt; ++i)
      {
         m_left.pushMotionProfileTrajectory(leftPoints.get(i));
         m_right.pushMotionProfileTrajectory(rightPoints.get(i));
      }
      
      //System.out.println("PathFollower.fillPathBuffers(): added " + m_left.getMotionProfileTopLevelBufferCount());

   }
   
}
