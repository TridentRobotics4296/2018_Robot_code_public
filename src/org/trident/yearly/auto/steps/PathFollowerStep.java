package org.trident.yearly.auto.steps;

import java.io.File;

import org.trident.yearly.robot.WSSubsystems;
import org.trident.yearly.subsystems.Drive;
import org.trident.yearly.subsystems.drive.Path;
import org.trident.yearly.subsystems.drive.PathFollower;
import org.trident.yearly.subsystems.drive.PathReader;
import org.trident.yearly.subsystems.drive.Trajectory;
import org.wildstang.framework.auto.steps.AutoStep;
import org.wildstang.framework.core.Core;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PathFollowerStep extends AutoStep
{

   private String m_filePath;
   private Path m_path;
   private Drive m_drive;
   private PathFollower m_pathFollower;
   
   private boolean m_started = false;


   public PathFollowerStep(String p_path)
   {
      m_filePath = p_path;
   }
   
   @Override
   public void initialize()
   {
      m_path = new Path();
      File leftFile = new File(m_filePath + ".left");
      File rightFile = new File(m_filePath + ".right");
      Trajectory leftTrajectory;
      Trajectory rightTrajectory;
      
      leftTrajectory = PathReader.readTrajectory(leftFile);
      rightTrajectory = PathReader.readTrajectory(rightFile);
      
      m_path.setLeft(leftTrajectory);
      m_path.setRight(rightTrajectory);
      
      m_drive = (Drive)Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName());
      m_drive.setHighGear(true);
   }

   @Override
   public void update()
   {
      super.update();
      //SmartDashboard.putBoolean("Path Running", false);
      if (!isFinished())
      {
         SmartDashboard.putBoolean("Path Running", true);
         if (!m_started)
         {
            // TODO: Can next 3 lines be moved to init() ??
            m_drive.setPathFollowingMode();
            m_drive.setPath(m_path);
            m_pathFollower = m_drive.getPathFollower();

            m_drive.startFollowingPath();
            m_drive.resetEncoders();
            m_started = true;
         }
         else
         {
            if (m_pathFollower.isActive())
            {
               m_pathFollower.update();
            }
            else
            {
               m_drive.pathCleanup();
               setFinished(true);
            }
         }
      }            
      //setFinished(true);
   }

   @Override
   public String toString()
   {
      return "Path Follower";
   }

}
