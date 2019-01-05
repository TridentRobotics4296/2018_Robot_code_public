package org.trident.yearly.subsystems;


import org.trident.yearly.robot.CANConstants;
import org.trident.yearly.robot.WSInputs;
import org.trident.yearly.subsystems.poseables.Arm;
import org.trident.yearly.subsystems.poseables.Lift;
import org.trident.yearly.subsystems.poseables.Pose;
import org.trident.yearly.subsystems.poseables.PoseEndAction;
import org.trident.yearly.subsystems.poseables.Poseable;
import org.trident.yearly.subsystems.poseables.Wrist;
import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.AnalogInput;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator implements Subsystem
{
   // Deadband so nothing happens if joystick is bumped on accident
   private static double ARM_DEADBAND = 0.015;
   private static double WRIST_DEADBAND = 0.03;
   private final boolean INVERTED = true;


   // Arm Talons
   private TalonSRX m_armMaster;
   private TalonSRX m_armFollower;

   // Wrist Talon
   private TalonSRX m_wristMaster;
   
   // Lift Talons
   private TalonSRX m_liftMaster;
   private TalonSRX m_liftFollower;

      
   private Lift m_lift;
   private Arm m_arm;
   private Wrist m_wrist;
   

   
   // PDP for checking if Feeds are jammed
   private PowerDistributionPanel pdp;

   
   
   // Inputs
   private AnalogInput m_armJoystick;
   private AnalogInput m_wristJoystick;
   
   private DigitalInput m_liftUpButton;
   private DigitalInput m_liftDownButton;
   
   private DigitalInput m_switchButton;
   private DigitalInput m_scaleButton;
   private DigitalInput m_pickupButton;
   private DigitalInput m_neutralButton;
//   private DigitalInput m_climbButton;
   

   
   // ALL variables below here are state that need to be reset in resetState to revert to an initial state
   // For the toggle

   private Pose m_currentPose = Pose.START;
   private Pose m_prevPose = Pose.NONE;
   
   private boolean m_manualArmControl = false;
   private boolean m_manualLiftControl = false;
   private boolean m_manualWristControl = false;
   private boolean m_movingLift = false;
   private boolean m_movingArm = false;
   
   private double m_liftSpeed = 0;
   
   private double m_armJoyValue;
   private double m_wristJoyValue;
 

   @Override
   public void resetState()
   {

      m_armJoyValue = 0;
      m_wristJoyValue = 0;
      m_liftSpeed = 0;
      
      m_manualLiftControl = false;
      m_manualArmControl = false;
      m_manualWristControl = false;
      m_movingLift = false;
      m_movingArm = false;
      
      // DO NOT reset the pose, to avoid erratic behaviour moving from auto to teleop
//      m_currentPose = Pose.START;
//      m_prevPose = Pose.NONE;
      
      
   }

   @Override
   public void selfTest()
   {
      // DO NOT IMPLELMENT
   }

   @Override
   public String getName()
   {
      return "Manipulator";
   }

   @Override
   public void init()
   {
      // Read config values
      readConfigValues();
      
      // CAN talons
      m_armMaster = new TalonSRX(CANConstants.ARM_MASTER_TALON_ID);
      m_armFollower = new TalonSRX(CANConstants.ARM_FOLLOWER_TALON_ID);
      m_armFollower.set(ControlMode.Follower, CANConstants.ARM_MASTER_TALON_ID);

      m_liftMaster = new TalonSRX(CANConstants.LIFT_MASTER_TALON_ID);
      m_liftMaster.setInverted(INVERTED);
      m_liftFollower = new TalonSRX(CANConstants.LIFT_FOLLOWER_TALON_ID);
      m_liftFollower.set(ControlMode.Follower, CANConstants.LIFT_MASTER_TALON_ID);
      m_liftFollower.setInverted(INVERTED);
      
      m_wristMaster = new TalonSRX(CANConstants.WRIST_TALON_ID);

      m_lift = new Lift(m_liftMaster);
      m_arm = new Arm(m_armMaster);
      m_wrist = new Wrist(m_wristMaster);

      // PDP
      pdp = new PowerDistributionPanel();

      // Input Listeners
      m_liftUpButton = (DigitalInput) Core.getInputManager().getInput(WSInputs.LIFT_UP.getName());
      m_liftUpButton.addInputListener(this);
      m_liftDownButton = (DigitalInput) Core.getInputManager().getInput(WSInputs.LIFT_DOWN.getName());
      m_liftDownButton.addInputListener(this);

      m_armJoystick = (AnalogInput) Core.getInputManager().getInput(WSInputs.ARM_ROTATION.getName());
      m_armJoystick.addInputListener(this);
      m_wristJoystick = (AnalogInput) Core.getInputManager().getInput(WSInputs.CLAW_ROTATION.getName());
      m_wristJoystick.addInputListener(this);

      // Pose buttons
      m_neutralButton = (DigitalInput) Core.getInputManager().getInput(WSInputs.START_POS.getName());
      m_neutralButton.addInputListener(this);
      m_switchButton = (DigitalInput) Core.getInputManager().getInput(WSInputs.SWITCH_POS.getName());
      m_switchButton.addInputListener(this);
      m_scaleButton = (DigitalInput) Core.getInputManager().getInput(WSInputs.SCALE_POS.getName());
      m_scaleButton.addInputListener(this);
      m_pickupButton = (DigitalInput) Core.getInputManager().getInput(WSInputs.PICKUP_POS.getName());
      m_pickupButton.addInputListener(this);

      
      resetState();
      
      // This is here so it doesn't get reset after autonomous and so suddenly jump to a new position
      m_prevPose = Pose.NONE;
   }


   @Override
   public void inputUpdate(Input p_source)
   {
      if (p_source == m_liftUpButton)
      {
         if (m_liftUpButton.getValue())
         {
            m_liftSpeed = 1;
            m_manualLiftControl = true;
         }
         else
         {
            m_liftSpeed = 0;
            m_manualLiftControl = true;
         }
      }
      else if (p_source == m_liftDownButton)
      {
         if (m_liftDownButton.getValue())
         {
            m_liftSpeed = -0.75;
            m_manualLiftControl = true;
         }
         else
         {
            m_liftSpeed = 0;
            m_manualLiftControl = true;
         }
      }
    
      else if (p_source == m_armJoystick)
      {
         m_armJoyValue = handleDeadband(m_armJoystick.getValue(), ARM_DEADBAND);
         
         m_manualArmControl = true;//Math.abs(m_armJoyValue) > 0;
      }
      else if (p_source == m_wristJoystick)
      {
         m_wristJoyValue = handleDeadband(m_wristJoystick.getValue(), WRIST_DEADBAND);
         
         m_manualWristControl = true;//Math.abs(m_wristJoyValue) > 0;
      }
      
      // Poses
      else if (p_source == m_neutralButton)
      {
         if (m_neutralButton.getValue())
         {
            moveToPose(Pose.START);
         }
      }
      else if (p_source == m_switchButton)
      {
         if (m_switchButton.getValue())
         {
            moveToPose(Pose.SWITCH);
         }
      }
      else if (p_source == m_pickupButton)
      {
         if (m_pickupButton.getValue())
         {
            moveToPose(Pose.PICKUP);
         }
      }
      else if (p_source == m_scaleButton)
      {
         if (m_scaleButton.getValue())
         {
            moveToPose(Pose.SCALE);
         }
      }
//      else if (p_source == m_climbButton)
////      {
////         if(m_climbButton.getValue())
//           {
//              moveToPose(Pose.CLIMB);
//           }
////      }
   }
   
   public double handleDeadband(double val, double deadband) {
      return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
  }


   @Override
   public void update()
   {
     // Logger.getLogger(getName()).log(Level.WARNING, "Maual " + (m_manualLiftControl || m_manualArmControl || m_manualWristControl));
      if (m_manualLiftControl || m_manualArmControl || m_manualWristControl)
      {
         stopPose();

         m_movingLift = false;
         m_movingArm = false;
         
         updateLift();
         updateArm();
         updateWrist();
      }
      else
      {
         strikeAPose();
      }
      
      checkMovementLimits();

      updateDashboardData();
   }


   private void strikeAPose()
   {
      // TODO!! Most important thing is to know when we have reached our setpoint, and can switch to a hold. We can
      // use that to flag a 'finished pose move' state. We MUST have this for auto, and possibly sequencing in teleop to come
      // down from scale

      SmartDashboard.putString("Previous pose", m_prevPose.toString());
      SmartDashboard.putString("Current pose", m_currentPose.toString());
      
      // Only change if they are different. If the same button is pressed twice, do not interrupt current move
      if (m_currentPose != m_prevPose || !inCurrentPose())
      {
            // If we are in either the climb or scale pose, first move the arm out of the way before the lift
         if ((m_prevPose == Pose.SCALE || m_prevPose == Pose.CLIMB) /*|| !m_arm.heightSafeToMoveLift()*/)
         {
            // Move arm to switch pose to clear the lift
            m_arm.moveTo(Pose.SWITCH.getArmPos(), Pose.SWITCH.getArmEndAction());
            m_movingArm = !m_arm.inPosition();
         }
         
         // TODO: Is this still necessary with the above check??
         // TODO: Consolidate with the above check, and add wait for arm move
         
         // TODO: Use above if statement instead of the following, and add a delay after moving the arm, and
         // this should cover all cases
         if ((m_prevPose == Pose.SCALE || m_prevPose == Pose.CLIMB))
         {
            m_arm.moveTo(m_currentPose.getArmPos(), m_currentPose.getArmEndAction());
            m_wrist.moveTo(m_currentPose.getWristPos(), m_currentPose.getWristEndAction());
            if(!m_movingArm) {
               m_lift.moveTo(m_currentPose.getLiftPos(), m_currentPose.getLiftEndAction());
            }
         }
         
         // If we are going up to switch or scale, make sure the arm and claw do not move until the lift is up
         else if ((m_currentPose == Pose.SCALE || m_currentPose == Pose.CLIMB) &&
            (m_prevPose != Pose.SCALE && m_prevPose != Pose.CLIMB))
         {
            
            // First move lift up
            
            m_lift.moveTo(m_currentPose.getLiftPos(), m_currentPose.getLiftEndAction());
            m_movingLift = !m_lift.inPosition();
            
            // Check state of lift. Only move the arm and wrist once the lift is complete
            if (!m_movingLift)
            {
               m_arm.moveTo(m_currentPose.getArmPos(), m_currentPose.getArmEndAction());
               m_wrist.moveTo(m_currentPose.getWristPos(), m_currentPose.getWristEndAction());
            }
         }
         // Normal case - just move. All mechanisms can move together
         else
         {
            m_lift.moveTo(m_currentPose.getLiftPos(), m_currentPose.getLiftEndAction());
            m_arm.moveTo(m_currentPose.getArmPos(), m_currentPose.getArmEndAction());
            m_wrist.moveTo(m_currentPose.getWristPos(), m_currentPose.getWristEndAction());
         }
         
         if (!m_movingLift && !m_movingArm)
         {
            m_prevPose = m_currentPose;
         }
         else if (m_movingLift)
         {
            m_movingLift = !m_lift.inPosition();
         }
         else if (m_movingArm)
         {
            m_movingArm = !m_arm.heightSafeToMoveLift();
         }
      }
      
      // No change in pose, so check to see if we are in the pose and need to stop

      inPosition(m_lift, m_currentPose.getLiftEndAction());
      inPosition(m_arm, m_currentPose.getArmEndAction());
      inPosition(m_wrist, m_currentPose.getWristEndAction());
      
   }
   

   private void checkMovementLimits()
   {
      // If the wrist up limit switch is pressed
      if (m_wristMaster.getSensorCollection().isFwdLimitSwitchClosed())
      {
         m_wrist.atUpperLimit();
      }

      // If the wrist down limit switch is pressed
      if (m_wristMaster.getSensorCollection().isRevLimitSwitchClosed())
      {
         m_wrist.atLowerLimit();
      }

      // If the arm up limit switch is pressed
      if (m_armMaster.getSensorCollection().isRevLimitSwitchClosed())
      {
         m_arm.atUpperLimit();
      }

      // If the arm down limit switch is pressed
      if (m_armMaster.getSensorCollection().isRevLimitSwitchClosed())
      {
         m_arm.atLowerLimit();
      }

      // If the lift up limit switch is pressed
      if (m_liftMaster.getSensorCollection().isFwdLimitSwitchClosed())
      {
         m_lift.atUpperLimit();
      }

      // If the lift down limit switch is pressed
      if (m_liftMaster.getSensorCollection().isRevLimitSwitchClosed())
      {
         m_lift.atLowerLimit();
      }
   }
   
   private void inPosition(Poseable poseable, PoseEndAction endAction)
   {
      if (poseable.inPosition())
      {
         poseable.setMovingToPose(false);
         if (endAction == PoseEndAction.HOLD)
         {
            poseable.holdPosition();
         }
         else if (endAction == PoseEndAction.OFF)
         {
            poseable.turnOff();
         }
      }
   }
   
   public boolean inCurrentPose()
   {
      return m_lift.inPosition() && m_arm.inPosition() && m_wrist.inPosition();
   }
   
   public void moveToPose(Pose p_pose)
   {
//      if (m_currentPose != p_pose)
//      {
//         m_prevPose = m_currentPose;
         m_currentPose = p_pose;
         m_manualLiftControl = false;
         m_manualArmControl = false;
         m_manualWristControl = false;
         m_movingArm = false;
         m_movingLift = false;
//      }
   }
   
   
   private void stopPose()
   {
      // Hold the current position, no matter where it is
      // Any sequencing of actions would need to cancel the sequence timers
      
      m_lift.cancelPose();
      m_arm.cancelPose();
      m_wrist.cancelPose();
      
      m_movingArm = false;
      m_movingLift = false;
   }
   
   private void updateLift()
   {
      m_lift.manualMove(m_liftSpeed);
      
      if (m_liftSpeed == 0)
      {
         m_lift.holdPosition();
      }
   }
   
   private void updateArm()
   {
      m_arm.manualMove(m_armJoyValue);
      
      if (Math.abs(m_armJoyValue) <= ARM_DEADBAND)
      {
         m_arm.holdPosition();
      }
   }
   
   private void updateWrist()
   {
      m_wrist.manualMove(m_wristJoyValue);
      
      if (Math.abs(m_wristJoyValue) <= WRIST_DEADBAND)
      {
         m_wrist.holdPosition();
      }
      
      // TODO: Reset encoder pos??
   }
   
   public void setWristSpeed(double speed) 
   {
      m_wristJoyValue = speed;
      m_manualWristControl = true;
   }
   
   public void setArmSpeed(double speed)
   {
      m_armJoyValue = speed;
      m_manualArmControl = true;
   }
   
   public void moveLiftDown(double speed)
   {
      m_liftSpeed = speed;
      m_manualLiftControl = true;
   }

   // Shows speeds and states for testing
   public void updateDashboardData()
   {
      SmartDashboard.putString("Pose", m_currentPose.toString());
      SmartDashboard.putBoolean("Pose in position", inCurrentPose());

      SmartDashboard.putBoolean("Lift manual", m_manualLiftControl);
      SmartDashboard.putBoolean("Arm manual", m_manualArmControl);
      SmartDashboard.putBoolean("Wrist manual", m_manualWristControl);
      
      m_lift.updateDashboard();
      m_arm.updateDashboard();
      m_wrist.updateDashboard();
   }
   
   public Wrist getWrist() 
   {
      return m_wrist;
   }
   
   public Arm getArm() 
   {
      return m_arm;
   }
   
   public Lift getLift()
   {
      return m_lift;
   }
   
   private void readConfigValues()
   {
      
   }

}
