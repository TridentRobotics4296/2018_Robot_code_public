package org.trident.yearly.robot;
/*----------------------------------------------------------------------------*/

/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.trident.yearly.auto.programs.*;
import org.trident.yearly.auto.testprograms.*;
import org.trident.yearly.robot.vision.VisionServer;
import org.trident.yearly.subsystems.Drive;
import org.trident.yearly.subsystems.drive.DriveConstants;
import org.trident.yearly.subsystems.poseables.PoseableConstants;
import org.wildstang.framework.auto.AutoManager;
import org.wildstang.framework.core.Core;
import org.wildstang.framework.logger.StateLogger;
import org.wildstang.framework.timer.ProfilingTimer;
import org.wildstang.hardware.crio.RoboRIOInputFactory;
import org.wildstang.hardware.crio.RoboRIOOutputFactory;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.wpilibj.Watchdog;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot
{

   public static boolean LOG_STATE = false;
   //TODO: Remove these variables after done tuning PIDs for lift
   public static double pUp,iUp,dUp,pDown,iDown,dDown,pHold,iHold,dHold;

   private static long lastCycleTime = 0;
   private StateLogger m_stateLogger = null;
   private Core m_core = null;
   private static Logger s_log = Logger.getLogger(Robot.class.getName());

   private static VisionServer m_visionServer = new VisionServer(5800);

   private boolean exceptionThrown = false;

   private boolean m_firstDisabled = true;

   private boolean AutoFirstRun = true;
   private boolean firstRun = true;
   private double oldTime = System.currentTimeMillis();

   static boolean teleopPerodicCalled = false;

   private static final String ERROR_MSG_KEY = "Last error";
   
   private void startloggingState()
   {
      Writer outputWriter = null;

      String dateStr = (new SimpleDateFormat("YYYY-MM-dd-HH-mm")).format(new Date());
      outputWriter = getFileWriter(dateStr);
      // outputWriter = getNetworkWriter("10.1.11.12", 17654);

      m_stateLogger.setWriter(outputWriter);

      // Set the interval between writes to the file. Try 100ms
      m_stateLogger.setWriteInterval(100);
      m_stateLogger.start();

      Thread t = new Thread(m_stateLogger);
      t.start();
   }

   private FileWriter getFileWriter(String p_date)
   {
      FileWriter output = null;

      try
      {
         File outputFile;
         String osname = System.getProperty("os.name");
         if (osname.startsWith("Windows"))
         {
            outputFile = new File("./../../log.txt");
         }
         else if (osname.startsWith("Mac"))
         {
            outputFile = new File("./../../log.txt");
         }
         else
         {
            outputFile = new File("/home/lvuser/log-" + p_date + ".txt");
         }
         if (outputFile.exists())
         {
            outputFile.delete();
         }
         outputFile.createNewFile();
         output = new FileWriter(outputFile);
      }
      catch (IOException e)
      {
         SmartDashboard.putString(ERROR_MSG_KEY, "Failed to open log file for writing");
         e.printStackTrace();
      }

      return output;
   }

   
   public void robotInit()
   {
      startupTimer.startTimingSection();
      
      m_core = new Core(RoboRIOInputFactory.class, RoboRIOOutputFactory.class);
      m_stateLogger = new StateLogger(Core.getStateTracker());

      // Load the config
      loadConfig();

      // Create application systems
      m_core.createInputs(WSInputs.values());
      m_core.createOutputs(WSOutputs.values());

      // 1. Add subsystems
      m_core.createSubsystems(WSSubsystems.values());

      startloggingState();

      // 2. Add Auto programs
      // TEST auto programs
//      AutoManager.getInstance().addProgram(new TestDriveDistance());
//      AutoManager.getInstance().addProgram(new TestMotionMagic());
////      AutoManager.getInstance().addProgram(new TESTNavXTurn());
////      AutoManager.getInstance().addProgram(new TestClawSteps());
//      AutoManager.getInstance().addProgram(new TESTMoveToPose());
//      
//      
//      // REAL Auto programs
//      AutoManager.getInstance().addProgram(new Baseline());
//      AutoManager.getInstance().addProgram(new SwitchOnlyAuto());

      
      if (m_visionServer != null)
      {
         m_visionServer.startVisionServer();
      }


      s_log.logp(Level.ALL, this.getClass().getName(), "robotInit", "Startup Completed");

      // Processing should not be necessary, but code is here for reference.
      // Code used is to resize for bandwidth restrictions in case setResolution() is not sufficient.
      new Thread(() -> {
         UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
         camera.setVideoMode(VideoMode.PixelFormat.kYUYV, 352, 288, 30);
         camera.setResolution(352, 288);
         
         CvSink cvSink = CameraServer.getInstance().getVideo();
         CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 320, 240);
         
         Mat source = new Mat();
         Mat output = new Mat();
         
         while(!Thread.interrupted()) {
             cvSink.grabFrame(source);
             outputStream.putFrame(output);
                try
               {
                  Thread.sleep(100);
               }
               catch (InterruptedException e)
               {
                  e.printStackTrace();
               }
         }
     }).start();
      
      startupTimer.endTimingSection();

   }

   private void loadConfig()
   {
      File configFile;
      String osname = System.getProperty("os.name");
      if (osname.startsWith("Windows"))
      {
         configFile = new File("./Config/ws_config.txt");
      }
      else if (osname.startsWith("Mac"))
      {
         configFile = new File("./Config/ws_config.txt");
      }
      else
      {
         configFile = new File("/ws_config.txt");
      }

      BufferedReader reader = null;

      try
      {
         reader = new BufferedReader(new FileReader(configFile));
         Core.getConfigManager().loadConfig(reader);

      }
      catch (FileNotFoundException e)
      {
         SmartDashboard.putString(ERROR_MSG_KEY, "Couldn't find config file to load");
         e.printStackTrace();
      }

      if (reader != null)
      {
         try
         {
            reader.close();
         }
         catch (IOException e)
         {
            SmartDashboard.putString(ERROR_MSG_KEY, "Failed to close config file");
            e.printStackTrace();
         }
      }
   }

   ProfilingTimer durationTimer = new ProfilingTimer("Periodic method duration", 50);
   ProfilingTimer periodTimer = new ProfilingTimer("Periodic method period", 50);
   ProfilingTimer startupTimer = new ProfilingTimer("Startup duration", 1);
   ProfilingTimer initTimer = new ProfilingTimer("Init duration", 1);

   public void disabledInit()
   {
      initTimer.startTimingSection();
      AutoManager.getInstance().clear();

      initTimer.endTimingSection();
      s_log.logp(Level.ALL, this.getClass().getName(), "disabledInit", "Disabled Init Complete");
   }

   public void disabledPeriodic()
   {

      // Stop and remove any current path
//      if (((Drive) Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName())).getPathFollower() != null)
//      {
//         if (((Drive) Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName())).getPathFollower().isActive())
//         {
//            ((Drive) Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName())).pathCleanup();
//         }
//      }

      // If we are finished with teleop, finish and close the log file
      if (teleopPerodicCalled)
      {
         m_stateLogger.stop();
      }

      resetRobotState();
   }

   /**
    * This should be called to reset any robot state between runs, without
    * having to restart robot code.
    * 
    */
   private void resetRobotState()
   {
      AutoFirstRun = true;
      firstRun = true;
   }

   public void autonomousInit()
   {
      // Reset any subsystem state
      Core.getSubsystemManager().resetState();

      m_core.setAutoManager(AutoManager.getInstance());
      AutoManager.getInstance().startCurrentProgram();
   }

   public void autonomousPeriodic()
   {
      // Update all inputs, outputs and subsystems
//      m_core.executeUpdate();
//
//      double time = System.currentTimeMillis();
//      SmartDashboard.putNumber("Cycle Time", time - oldTime);
//      oldTime = time;
      
      long start = System.currentTimeMillis();
      m_core.executeUpdate();
      long end = System.currentTimeMillis();
      
      SmartDashboard.putNumber("Cycle Time", (end - start));
      
      try
      {
         long sleepTime = 20 - (end - start);
         Thread.sleep(sleepTime > 0 ? sleepTime : 0);
      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      if (AutoFirstRun)
      {
         AutoFirstRun = false;
      }
   }

   /**
    * This function is called periodically during operator control
    */
   public void teleopInit()
   {
      // Remove the AutoManager from the Core
      m_core.setAutoManager(null);

      // Reset any subsystem state
      Core.getSubsystemManager().resetState();

      Drive driveBase = ((Drive) Core.getSubsystemManager().getSubsystem(WSSubsystems.DRIVE_BASE.getName()));
      driveBase.setOpenLoopDrive();
      driveBase.setBrakeMode(true);

      periodTimer.startTimingSection();
   }

   public void teleopPeriodic()
   {

      if (firstRun)
      {
         teleopPerodicCalled = true;
         firstRun = false;
      }

      //TODO: Remove this after values are tuned
      // PID values for lift on dashboard for quick testing
      pUp = SmartDashboard.getNumber("pUp", 0);
      iUp = SmartDashboard.getNumber("iUp", 0);
      dUp = SmartDashboard.getNumber("dUp", 0);
      pDown = SmartDashboard.getNumber("pDown", 0);
      iDown = SmartDashboard.getNumber("iDown", 0);
      dDown = SmartDashboard.getNumber("dDown", 0);
      pHold = SmartDashboard.getNumber("pHold", 0);
      iHold = SmartDashboard.getNumber("iHold", 0);
      dHold = SmartDashboard.getNumber("dHold", 0);
      
      SmartDashboard.putNumber("pUp", pUp);
      SmartDashboard.putNumber("iUp", iUp);
      SmartDashboard.putNumber("dUp", dUp);
      SmartDashboard.putNumber("pDown", pDown);
      SmartDashboard.putNumber("iDown", iDown);
      SmartDashboard.putNumber("dDown", dDown);
      SmartDashboard.putNumber("pHold", pHold);
      SmartDashboard.putNumber("iHold", iHold);
      SmartDashboard.putNumber("dHold", dHold);
      
      PoseableConstants.LIFT_UP_P_GAIN = pUp;
      PoseableConstants.LIFT_UP_I_GAIN = iUp;
      PoseableConstants.LIFT_UP_D_GAIN = dUp;
      PoseableConstants.LIFT_DOWN_P_GAIN = pDown;
      PoseableConstants.LIFT_DOWN_I_GAIN = iDown;
      PoseableConstants.LIFT_DOWN_D_GAIN = dDown;
      PoseableConstants.LIFT_HOLD_P_GAIN = pHold;
      PoseableConstants.LIFT_HOLD_I_GAIN = iHold;
      PoseableConstants.LIFT_HOLD_D_GAIN = dHold;
      
      try
      {

         // Update all inputs, outputs and subsystems
         long start = System.currentTimeMillis();
         m_core.executeUpdate();
         long end = System.currentTimeMillis();
         
         SmartDashboard.putNumber("Cycle Time", (end - start));
         
         try
         {
            long sleepTime = 20 - (end - start);
            Thread.sleep(sleepTime > 0 ? sleepTime : 0);
         }
         catch (Exception e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      catch (Throwable e)
      {
         SmartDashboard.putString(ERROR_MSG_KEY, "Exception thrown during teleopPeriodic");
         SmartDashboard.putString("Exception thrown", e.toString());
         exceptionThrown = true;
         throw e;
      }
      finally
      {
         SmartDashboard.putBoolean("ExceptionThrown", exceptionThrown);
      }
   }

   public void testInit()
   {

   }

   /**
    * This function is called periodically during test mode
    */
   public void testPeriodic()
   {
      // Watchdog.getInstance().feed();
   }

   public static VisionServer getVisionServer()
   {
      return m_visionServer;
   }
}
