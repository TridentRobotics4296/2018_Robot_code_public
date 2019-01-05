package org.wildstang.framework.auto;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.trident.yearly.auto.programs.AllOnSideAuto;
import org.trident.yearly.auto.programs.BaseLineFromCenterAuto;
import org.trident.yearly.auto.programs.Baseline;
import org.trident.yearly.auto.programs.ScaleOnlyAuto;
import org.trident.yearly.auto.programs.ScaleOrSwitch;
import org.trident.yearly.auto.programs.SwitchOrScale;
import org.trident.yearly.auto.programs.TestPath;
import org.trident.yearly.auto.programs.TestScale;
import org.trident.yearly.auto.programs.TestSwitchBackwards;
import org.trident.yearly.auto.programs.TestSwitchCenterLeft;
import org.trident.yearly.auto.programs.TestSwitchCenterRight;
import org.trident.yearly.auto.programs.SwitchOnlyAuto;
import org.trident.yearly.auto.testprograms.TESTMoveToPose;
import org.trident.yearly.auto.testprograms.TESTNavXTurn;
import org.trident.yearly.auto.testprograms.TestClawSteps;
import org.trident.yearly.auto.testprograms.TestDriveDistance;
import org.trident.yearly.auto.testprograms.TestMotionMagic;
import org.trident.yearly.auto.testprograms.VisionTest;
import org.wildstang.framework.auto.program.Sleeper;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class AutoManager
{

   private List<AutoProgram> programs = new ArrayList<AutoProgram>();
   private AutoProgram runningProgram;
   private boolean programFinished, programRunning;
   private static AutoManager instance = null;
   private AutoStartPositionEnum currentPosition;
   private SendableChooser chooser;
   private SendableChooser lockinChooser;
   private SendableChooser positionChooser;
   private static Logger s_log = Logger.getLogger(AutoManager.class.getName());

   private AutoManager()
   {
      currentPosition = AutoStartPositionEnum.UNKNOWN;
      chooser = new SendableChooser();
      lockinChooser = new SendableChooser();
      lockinChooser.addDefault("Unlocked", false);
      lockinChooser.addObject("Locked", true);

      positionChooser = new SendableChooser();
      positionChooser.addDefault("Left", "left");
      positionChooser.addObject("Right", "right");
      positionChooser.addObject("Center", "center");

      definePrograms();

      clear();

      SmartDashboard.putData("Select Autonomous Program", chooser);
      SmartDashboard.putData("Lock in auto program", lockinChooser);
      SmartDashboard.putData("Auto start position", positionChooser);
   }

   
   public void update()
   {
      if (programFinished)
      {
         runningProgram.cleanup();
         programFinished = false;
         startSleeper();
      }
      runningProgram.update();
      if (runningProgram.isFinished())
      {
         programFinished = true;
      }
   }

   public void startCurrentProgram()
   {
      if ((Boolean) lockinChooser.getSelected())
      {
         runningProgram = (AutoProgram) chooser.getSelected();
      }
      else
      {
         runningProgram = programs.get(0);
      }

      s_log.logp(Level.ALL, "Auton", "Running Autonomous Program", runningProgram.toString());
      runningProgram.initialize((String)positionChooser.getSelected());
      SmartDashboard.putString("Running Autonomous Program", runningProgram.toString());
   }

   public void startSleeper()
   {
      runningProgram = programs.get(0);
      runningProgram.initialize("");
   }

   public void clear()
   {
      programFinished = false;
      programRunning = false;
      if (runningProgram != null)
      {
         runningProgram.cleanup();
      }
      runningProgram = (AutoProgram) programs.get(0);
      SmartDashboard.putString("Running Autonomous Program", "No Program Running");
      SmartDashboard.putString("Current Start Position", currentPosition.toString());
   }

   public AutoProgram getRunningProgram()
   {
      if (programRunning)
      {
         return runningProgram;
      }
      else
      {
         return (AutoProgram) null;
      }
   }

   public String getRunningProgramName()
   {
      return runningProgram.toString();
   }

   /*
    * public String getSelectedProgramName() { return
    * programs.get(currentProgram).toString(); }
    * 
    * public String getLockedProgramName() { return
    * programs.get(lockedProgram).toString(); }
    */

   public AutoStartPositionEnum getStartPosition()
   {
      return currentPosition;
   }

   public static AutoManager getInstance()
   {
      if (AutoManager.instance == null)
      {
         AutoManager.instance = new AutoManager();
      }
      return AutoManager.instance;
   }

   /*
    * public void setProgram(int index) { if (index >= programs.size() || index
    * < 0) { index = 0; } currentProgram = index; lockedProgram =
    * currentProgram; }
    */

   public void setPosition(int index)
   {
      if (index >= AutoStartPositionEnum.POSITION_COUNT)
      {
         index = 0;
      }
      currentPosition = AutoStartPositionEnum.getEnumFromValue(index);
   }

   private void definePrograms()
   {
      AutoProgram sleeper = new Sleeper();
      chooser.addDefault("Sleeper", sleeper);
      programs.add(sleeper);
//      addProgram(new Sleeper()); // Always leave Sleeper as
                                 // 0. Other parts of the
                                 // code assume 0 is Sleeper.
      
      //Test Programs
      //addProgram(new TestScale());
      //addProgram(new TestSwitchBackwards());
      //addProgram(new TESTNavXTurn());
      //addProgram(new TestSwitchCenterLeft());
      //addProgram(new TestSwitchCenterRight());
      
      addProgram(new Baseline());
      //addProgram(new BaseLineFromCenterAuto());
      
      addProgram(new SwitchOnlyAuto());
      addProgram(new ScaleOnlyAuto());
      addProgram(new SwitchOrScale());
      addProgram(new ScaleOrSwitch());
      
      addProgram(new TestPath());
      //addProgram(new AllOnSideAuto()); TODO enable after fixing camera
   }

   public void addProgram(AutoProgram program)
   {
      programs.add(program);
      chooser.addObject(program.toString(), program);
   }
}
