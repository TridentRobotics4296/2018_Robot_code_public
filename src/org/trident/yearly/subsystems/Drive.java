package org.trident.yearly.subsystems;

import java.io.IOException;
import java.util.logging.Level;

import org.trident.yearly.robot.*;
import org.trident.yearly.subsystems.drive.*;
import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.AnalogInput;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.crio.outputs.WsDoubleSolenoid;
import org.wildstang.hardware.crio.outputs.WsDoubleSolenoidState;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.*;
import com.kauailabs.navx.frc.AHRS;
import com.sun.istack.internal.logging.Logger;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive implements Subsystem
{
   // Constants
   public static final double WHEEL_DIAMETER_INCHES = 6.218; //This must be as accurate as possible
   public static final double ENCODER_CPR = 4096;
   private static final double TICKS_TO_INCHES = 0.00460194236365692368915426276848;//WHEEL_DIAMETER_INCHES * Math.PI / ENCODER_CPR;
   public static final double ROBOT_WIDTH = 34.25;
   public static final double ROBOT_LENGTH = 39.25;
   
   private static final double CORRECTION_HEADING_LEVEL = 0.25;

   private static final double ANTI_TURBO_FACTOR = 0.5;
   private static final double TURBO_FACTOR = 1.0;
   private static final double NORMAL_FACTOR = 0.75;
   

   // Hold a reference to the input test for fast equality test during
   // inputUpdate()
   private AnalogInput m_headingInput;
   private AnalogInput m_throttleInput;
   private WsDoubleSolenoid m_shifterSolenoid;

   private DigitalInput m_baseLockInput;
   private DigitalInput m_shifterInput;
   private DigitalInput m_cubeFollowInput;
   private DigitalInput m_antiTurboInput;

   private AHRS m_ahrs;


   // Talons for output
   private TalonSRX m_leftMaster;
   private TalonSRX m_rightMaster;
   private TalonSRX m_leftFollower1;
   private TalonSRX m_leftFollower2;
   private TalonSRX m_rightFollower1;
   private TalonSRX m_rightFollower2;

   private CheesyDriveHelper m_cheesyHelper = new CheesyDriveHelper();

   // ALL variables below here are state that needs to be reset in resetState()
   private boolean m_cubeFollowFinished = false;
   
   // Values from inputs
   private double m_throttleValue;
   private double m_headingValue;
   private boolean m_quickTurn;

   private boolean m_shifterCurrent = false;
   private boolean m_shifterPrev = false;
   private boolean m_highGear = false;

   private boolean m_cubeFollowCurrent = false;
   private boolean m_cubeFollowPrev = false;
   private boolean m_cubeFollowMode = false;

   private boolean m_antiTurbo = false;
   
   private DriveType m_driveMode = DriveType.CHEESY;
   private PathFollower m_pathFollower;

   double maxSpeed = 0;
   double maxAccel = 0;
   private boolean m_brakeMode = true;

   // While this is really a temporary variable, declared here to prevent
   // constant stack allocation
   private DriveSignal m_driveSignal;

   double m_visionX;
   double m_visionDistance;

   SerialPort m_port;
   VisionReader m_visionReader;
   String m_message;
   
   @Override
   public void init()
   {
      // Add any additional items to track in the logger
      if (Robot.LOG_STATE)
      {
         Core.getStateTracker().addIOInfo("Left speed (RPM)", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Right speed (RPM)", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Left output", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Right output", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Left 1 current", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Left 2 current", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Right 1 current", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Right 2 current", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Left 1 voltage", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Left 2 voltage", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Right 1 voltage", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Right 2 voltage", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Drive heading", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Drive throttle", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Vision distance", "Drive", "Input", null);
         Core.getStateTracker().addIOInfo("Vision correction", "Drive", "Input", null);
      }
      
      // Drive
      m_headingInput = (AnalogInput) Core.getInputManager().getInput(WSInputs.DRV_HEADING.getName());
      m_headingInput.addInputListener(this);

      m_throttleInput = (AnalogInput) Core.getInputManager().getInput(WSInputs.DRV_THROTTLE.getName());
      m_throttleInput.addInputListener(this);

      m_shifterInput = (DigitalInput) Core.getInputManager().getInput(WSInputs.SHIFT.getName());
      m_shifterInput.addInputListener(this);
      
      m_antiTurboInput = (DigitalInput) Core.getInputManager().getInput(WSInputs.ANTITURBO.getName());
      m_antiTurboInput.addInputListener(this);

      m_baseLockInput = (DigitalInput) Core.getInputManager().getInput(WSInputs.BASE_LOCK.getName());
      m_baseLockInput.addInputListener(this);

      m_cubeFollowInput = (DigitalInput) Core.getInputManager().getInput(WSInputs.CUBE_FOLLOW.getName());
      m_cubeFollowInput.addInputListener(this);

      m_shifterSolenoid = (WsDoubleSolenoid) Core.getOutputManager().getOutput(WSOutputs.SHIFTER.getName());

      initDriveTalons();
      
      try {
          m_port = new SerialPort(115200, SerialPort.Port.kUSB);
          m_visionReader = new VisionReader(m_port);
          Thread t = new Thread(m_visionReader);
          t.start();
      }catch(Throwable t) {
         t.printStackTrace();
         SmartDashboard.putString("Vision Throwable", t.toString());
      }
      
      m_ahrs = new AHRS(I2C.Port.kMXP);
      m_ahrs.zeroYaw();  // Optional?

      resetState();
   }

   
   @Override
   public void resetState()
   {
      resetEncoders();
      setBrakeMode(false);
      
      setOpenLoopDrive();
      
      setThrottle(0);
      setHeading(0);
      m_quickTurn = false;

      setHighGear(false);
      m_shifterCurrent = false;
      m_shifterPrev = false;

      m_cubeFollowCurrent = false;
      m_cubeFollowPrev = false;
      m_cubeFollowMode = false;
      
      m_antiTurbo = false;

      maxSpeed = 0;
   }



   public void initDriveTalons()
   {
      m_leftMaster = new TalonSRX(CANConstants.LEFT_MASTER_TALON_ID);
      m_leftFollower1 = new TalonSRX(CANConstants.LEFT_FOLLOWER_1_TALON_ID);
      m_leftFollower2 = new TalonSRX(CANConstants.LEFT_FOLLOWER_2_TALON_ID);
      m_rightMaster = new TalonSRX(CANConstants.RIGHT_MASTER_TALON_ID);
      m_rightFollower1 = new TalonSRX(CANConstants.RIGHT_FOLLOWER_1_TALON_ID);
      m_rightFollower2 = new TalonSRX(CANConstants.RIGHT_FOLLOWER_2_TALON_ID);
      
      m_leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, 0);
      m_rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, 0);

      // Start in open loop mode
      m_leftMaster.set(ControlMode.PercentOutput, 0);
      m_leftMaster.setNeutralMode(NeutralMode.Brake);
      m_leftFollower1.set(ControlMode.Follower, CANConstants.LEFT_MASTER_TALON_ID);
      m_leftFollower2.set(ControlMode.Follower, CANConstants.LEFT_MASTER_TALON_ID);

      m_rightMaster.set(ControlMode.PercentOutput, 0);
      m_rightMaster.setNeutralMode(NeutralMode.Brake);
      m_rightFollower1.set(ControlMode.Follower, CANConstants.RIGHT_MASTER_TALON_ID);
      m_rightFollower2.set(ControlMode.Follower, CANConstants.RIGHT_MASTER_TALON_ID);

      m_leftMaster.configNominalOutputForward(0.0, 0);
      m_leftMaster.configNominalOutputReverse(0.0, 0);
      m_leftMaster.configPeakOutputForward(1, 0);
      m_leftMaster.configPeakOutputReverse(-1, 0);

      m_rightMaster.configNominalOutputForward(0.0, 0);
      m_rightMaster.configNominalOutputReverse(0.0, 0);
      m_rightMaster.configPeakOutputForward(1, 0);
      m_rightMaster.configPeakOutputReverse(-1, 0);

      // Set up the encoders
      m_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
      m_leftMaster.setSensorPhase(true);
      
      m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
//      m_rightMaster.setStatusFrameRateMs(StatusFrameRate.Feedback, 10);

      // Load PID profiles
      setTalonPID(m_leftMaster, 
            DriveConstants.MM_QUICK_P_GAIN, 
            DriveConstants.MM_QUICK_I_GAIN, 
            DriveConstants.MM_QUICK_D_GAIN, 
            DriveConstants.MM_QUICK_F_GAIN);
      setTalonPID(m_rightMaster, 
            DriveConstants.MM_QUICK_P_GAIN, 
            DriveConstants.MM_QUICK_I_GAIN, 
            DriveConstants.MM_QUICK_D_GAIN, 
            DriveConstants.MM_QUICK_F_GAIN);
      
   }

   @Override
   public void inputUpdate(Input p_source)
   {

      if (p_source == m_throttleInput)
      {
         m_throttleValue = m_throttleInput.getValue();
      }
      else if (p_source == m_headingInput)
      {
         m_headingValue = m_headingInput.getValue();
      }
      else if (p_source == m_shifterInput)
      {
         m_shifterCurrent = m_shifterInput.getValue();
         // Check and toggle shifter state
         toggleShifter();
      }
      else if (p_source == m_cubeFollowInput)
      {
         m_cubeFollowCurrent = m_cubeFollowInput.getValue();
         // Check and toggle auto gear drop state
//         toggleCubeFollow();
         m_cubeFollowMode = m_cubeFollowCurrent;
         if (m_cubeFollowMode)
         {
            setCubeFollowMode();
         }
         else
         {
            exitCubeFollowMode();
            setOpenLoopDrive();
            setHeading(0);
            setThrottle(0);
         }
      }
      else if (p_source == m_antiTurboInput)
      {
         m_antiTurbo = m_antiTurboInput.getValue();
      }
      else if (p_source == m_baseLockInput)
      {
         // Determine drive state override
         if (m_baseLockInput.getValue())
         {
            setFullBrakeMode();
         }
         else
         {
            setOpenLoopDrive();
            setHeading(0);
            setThrottle(0);
         }
      }
   }

   @Override
   public void selfTest()
   {
      // DO NOT IMPLEMENT
   }

   @Override
   public void update()
   {    
      SmartDashboard.putNumber("Max Encoder Speed", m_leftMaster.getSelectedSensorVelocity(0));
      // Set shifter output before driving
      // NOTE: The state of m_highGear needs to be set prior to update being
      // called. This is either in inputUpdate() (for teleop)
      // or by an auto program by calling setHighGear()
      
      if (!m_highGear)
      {
         m_shifterSolenoid.setValue(WsDoubleSolenoidState.FORWARD.ordinal());
      }
      else
      {
         m_shifterSolenoid.setValue(WsDoubleSolenoidState.REVERSE.ordinal());
      }

      switch (m_driveMode)
      {
         case PATH:
            break;

         case CHEESY:
            double effectiveThrottle = m_throttleValue;
            if (m_antiTurbo)
            {
               effectiveThrottle = m_throttleValue * ANTI_TURBO_FACTOR;
            }
            else
            {
               effectiveThrottle = m_throttleValue * NORMAL_FACTOR;
            }
            
            m_quickTurn = m_cheesyHelper.handleDeadband(m_throttleValue, CheesyDriveHelper.kThrottleDeadband) == 0.0;

            m_driveSignal = m_cheesyHelper.cheesyDrive(effectiveThrottle, m_headingValue, m_quickTurn);
            setMotorSpeeds(m_driveSignal);
            
            if (Robot.LOG_STATE)
            {
               double tempMax = Math.max(Math.abs(m_leftMaster.getSelectedSensorVelocity(0)), Math.abs(m_rightMaster.getSelectedSensorVelocity(0)));
               if (tempMax > maxSpeed)
               {
                  maxSpeed = tempMax;
               }
                
               SmartDashboard.putNumber("Max Encoder Speed", maxSpeed);
            }
            break;
         case FULL_BRAKE:
            break;
         case AUTO_CUBE_FOLLOW:
            autoCubeFollow();
            break;
         case MAGIC:
            break;
         case RAW:
         default:
            // Raw is default
            m_driveSignal = new DriveSignal(m_throttleValue, m_throttleValue);
            break;
      }
      
      updateDashboard();
      
      if (/*Robot.LOG_STATE*/true)
      {
//         Core.getStateTracker().addState("Drive heading", "Drive", m_headingValue);
//         Core.getStateTracker().addState("Drive throttle", "Drive", m_throttleValue);
//         Core.getStateTracker().addState("Vision distance", "Drive", m_visionDistance);
//         Core.getStateTracker().addState("Vision correction", "Drive", m_visionX);
//         
         Core.getStateTracker().addState("Left output", "Drive", m_leftMaster.getMotorOutputPercent());
         Core.getStateTracker().addState("Right output", "Drive", m_rightMaster.getMotorOutputPercent());
//
         Core.getStateTracker().addState("Left speed (RPM)", "Drive", encoderVelocityToRPM(m_leftMaster.getSelectedSensorVelocity(0)));
         Core.getStateTracker().addState("Right speed (RPM)", "Drive", encoderVelocityToRPM(m_rightMaster.getSelectedSensorVelocity(0)));
   
         Core.getStateTracker().addState("Left 1 voltage", "Drive", m_leftMaster.getMotorOutputVoltage());
         Core.getStateTracker().addState("Left 2 voltage", "Drive", m_leftFollower1.getMotorOutputVoltage());
         Core.getStateTracker().addState("Right 1 voltage", "Drive", m_rightMaster.getMotorOutputVoltage());
         Core.getStateTracker().addState("Right 2 voltage", "Drive", m_rightFollower1.getMotorOutputVoltage());

         Core.getStateTracker().addState("Left 1 current", "Drive", m_leftMaster.getOutputCurrent());
         Core.getStateTracker().addState("Left 2 current", "Drive", m_leftFollower1.getOutputCurrent());
         Core.getStateTracker().addState("Right 1 current", "Drive", m_rightMaster.getOutputCurrent());
         Core.getStateTracker().addState("Right 2 current", "Drive", m_rightFollower1.getOutputCurrent());
      }
   }


   private double encoderVelocityToRPM(int encoderVel)
   {
      return (encoderVel * 10 * 60 / ENCODER_CPR);
   }

   public void setAntiTurbo(boolean p_antiTurbo)
   {
      m_antiTurbo = p_antiTurbo;
   }
   
   private void toggleShifter()
   {
      if (m_shifterCurrent && !m_shifterPrev)
      {
         m_highGear = !m_highGear;
      }
      m_shifterPrev = m_shifterCurrent;
      
      // TODO: Remove this
      maxSpeed = 0; // Easy way to reset max speed.
   }

   private void toggleCubeFollow()
   {
      if (m_cubeFollowCurrent && !m_cubeFollowPrev)
      {
         m_cubeFollowMode = !m_cubeFollowMode;
      }
      m_cubeFollowPrev = m_cubeFollowCurrent;
   }

   
   
   private void autoCubeFollow()
   {
      m_message = m_port.readString();

      //Get last line
      String rawInput = m_message;
      
      if (rawInput != null && !rawInput.equals("") && rawInput.length() > 1)
      {
         int lastIndex = rawInput.lastIndexOf('\n');
         int secondLast = rawInput.lastIndexOf('\n', lastIndex - 1);
         m_visionX = Double.parseDouble(rawInput.substring(secondLast + 1, lastIndex));
      }
      m_visionX = m_visionReader.getLatest();
      
      setHeading(m_visionX * CORRECTION_HEADING_LEVEL);
      setThrottle(.35);

      if (((Claw)Core.getSubsystemManager().getSubsystem(WSSubsystems.CLAW.getName())).hasCube())
      {
         setThrottle(0);
         m_cubeFollowFinished = true;
      }
      
      m_driveSignal = m_cheesyHelper.cheesyDrive(m_throttleValue, m_headingValue, m_quickTurn);
      setMotorSpeeds(m_driveSignal);
   }
   
   public boolean isCubeFollowFinished()
   {
      return m_cubeFollowFinished;
   }

   public void setHighGear(boolean p_high)
   {
      m_highGear = p_high;
   }

   public void setBrakeMode(boolean p_brakeOn)
   {
      if (m_brakeMode != p_brakeOn)
      {
         NeutralMode mode = p_brakeOn ? NeutralMode.Brake : NeutralMode.Coast;
         
         m_leftMaster.setNeutralMode(mode);
         m_leftFollower1.setNeutralMode(mode);
         m_leftFollower2.setNeutralMode(mode);
         
         m_rightMaster.setNeutralMode(mode);
         m_rightFollower1.setNeutralMode(mode);
         m_rightFollower2.setNeutralMode(mode);
         
         m_brakeMode = p_brakeOn;
      }

   }
   
   public void setMotionMagicMode(boolean p_quickTurn, double f_gain)
   {
      // Stop following any current path
      stopPathFollowing();

      if (m_driveMode != DriveType.MAGIC)
      {
         // Set up Talons for the Motion Magic mode

         m_leftMaster.selectProfileSlot(DriveConstants.MM_PROFILE_SLOT, 0);
         m_rightMaster.selectProfileSlot(DriveConstants.MM_PROFILE_SLOT, 0);

         if (p_quickTurn)
         {
            int speed = rpmToVelocityTicksPer100ms(350);
            
            m_leftMaster.configMotionAcceleration(speed, 0);
            m_leftMaster.configMotionCruiseVelocity(speed, 0);
            setTalonPID(m_leftMaster, 
                  DriveConstants.MM_QUICK_P_GAIN, 
                  DriveConstants.MM_QUICK_I_GAIN, 
                  DriveConstants.MM_QUICK_D_GAIN, 
                  DriveConstants.MM_QUICK_F_GAIN);

            m_rightMaster.configMotionAcceleration(speed, 0);
            m_rightMaster.configMotionCruiseVelocity(speed, 0);
            setTalonPID(m_rightMaster, 
                  DriveConstants.MM_QUICK_P_GAIN, 
                  DriveConstants.MM_QUICK_I_GAIN, 
                  DriveConstants.MM_QUICK_D_GAIN, 
                  DriveConstants.MM_QUICK_F_GAIN);
         }
         else
         {
            int accel = rpmToVelocityTicksPer100ms(500);
            int vel = rpmToVelocityTicksPer100ms(300);

            m_leftMaster.configMotionAcceleration(accel, 0);
            m_leftMaster.configMotionCruiseVelocity(vel, 0);
            setTalonPID(m_leftMaster, 
                  DriveConstants.MM_DRIVE_P_GAIN, 
                  DriveConstants.MM_DRIVE_I_GAIN, 
                  DriveConstants.MM_DRIVE_D_GAIN, 
                  DriveConstants.MM_DRIVE_F_GAIN);

            m_rightMaster.configMotionAcceleration(accel, 0);
            m_rightMaster.configMotionCruiseVelocity(vel, 0);
            setTalonPID(m_rightMaster, 
                  DriveConstants.MM_DRIVE_P_GAIN,
                  DriveConstants.MM_DRIVE_I_GAIN,
                  DriveConstants.MM_DRIVE_D_GAIN,
                  DriveConstants.MM_DRIVE_F_GAIN);
         }

         resetEncoders();
         
         m_driveMode = DriveType.MAGIC;

         setBrakeMode(true);
      }
   }

   private int rpmToVelocityTicksPer100ms(int rpm)
   {
      return (int)((rpm / 60) * ENCODER_CPR) / 10;
   }

   public void setTalonPID(TalonSRX p_talon, double p, double i, double d, double f)
   {
      p_talon.config_kP(0, p, 0);
      p_talon.config_kI(0, i, 0);
      p_talon.config_kD(0, d, 0);
      p_talon.config_kF(0, f, 0);
   }

   public double getLeftEncoderValue()
   {
      return m_leftMaster.getSelectedSensorPosition(0);
   }
   
   public double getRightEncoderValue()
   {
      return m_rightMaster.getSelectedSensorPosition(0);
   }
   
   public void setMotionMagicTargetAbsolute(double p_leftTarget, double p_rightTarget)
   {
      m_leftMaster.set(ControlMode.MotionMagic, p_leftTarget);
      m_rightMaster.set(ControlMode.MotionMagic, p_rightTarget);
   }

   public void setMotionMagicTargetDelta(double p_leftDelta, double p_rightDelta)
   {
      m_leftMaster.set(ControlMode.MotionMagic, getLeftEncoderValue() + p_leftDelta);
      m_rightMaster.set(ControlMode.MotionMagic, getRightEncoderValue() + p_rightDelta);
   }

   private void stopPathFollowing()
   {
      if (m_driveMode == DriveType.PATH)
      {
         abortFollowingPath();
         pathCleanup();
      }
   }

   public void setMotorSpeeds(DriveSignal p_signal)
   {
      SmartDashboard.putNumber("Left motor output", p_signal.leftMotor);
      SmartDashboard.putNumber("Right motor output", p_signal.rightMotor);

      // Set left and right speeds
      m_leftMaster.set(ControlMode.PercentOutput, p_signal.leftMotor);
      m_rightMaster.set(ControlMode.PercentOutput, p_signal.rightMotor);
   }

   public void setCubeFollowMode()
   {
      stopPathFollowing();

      m_driveMode = DriveType.AUTO_CUBE_FOLLOW;

      
      setBrakeMode(true);
      setHighGear(false);
      m_cubeFollowFinished = false;
   }
   
   public void exitCubeFollowMode()
   {
      if (Robot.getVisionServer() != null)
      {
         Robot.getVisionServer().stopVideoLogging();
      }
   }
   
   public void setPathFollowingMode()
   {

      m_driveMode = DriveType.PATH;

      // Go as fast as possible
      setHighGear(true);

      // Use brake mode to stop quickly at end of path, since Talons will put
      // output to neutral
      setBrakeMode(true);
   }
   
   public void resetEncoders()
   {
      m_leftMaster.setSelectedSensorPosition(0, 0, 0);
      m_rightMaster.setSelectedSensorPosition(0, 0, 0);
   }

   public void setOpenLoopDrive()
   {
      stopPathFollowing();

      m_driveMode = DriveType.CHEESY;

      m_cubeFollowFinished = true;
      
      setBrakeMode(true);
   }

//   public void setRawDrive()
//   {
//      setOpenLoopDrive();
//
//      m_driveMode = DriveType.RAW;
//
//   }

   public void setFullBrakeMode()
   {
      stopPathFollowing();

      // Set talons to hold their current position
      if (m_driveMode != DriveType.FULL_BRAKE)
      {
         // Set up Talons to hold their current position as close as possible
         m_leftMaster.selectProfileSlot(DriveConstants.BASE_LOCK_PROFILE_SLOT, 0);
         m_leftMaster.configAllowableClosedloopError(0, DriveConstants.BRAKE_MODE_ALLOWABLE_ERROR, 0);
         m_leftMaster.set(ControlMode.Position, getLeftEncoderValue());

         m_rightMaster.selectProfileSlot(DriveConstants.BASE_LOCK_PROFILE_SLOT, 0);
         m_rightMaster.configAllowableClosedloopError(0, DriveConstants.BRAKE_MODE_ALLOWABLE_ERROR, 0);
         m_rightMaster.set(ControlMode.Position, getRightEncoderValue());
         
         setTalonPID(m_leftMaster, 
               DriveConstants.BASE_LOCK_P_GAIN, 
               DriveConstants.BASE_LOCK_I_GAIN, 
               DriveConstants.BASE_LOCK_D_GAIN, 
               DriveConstants.BASE_LOCK_F_GAIN);
         setTalonPID(m_rightMaster, 
               DriveConstants.BASE_LOCK_P_GAIN, 
               DriveConstants.BASE_LOCK_I_GAIN, 
               DriveConstants.BASE_LOCK_D_GAIN, 
               DriveConstants.BASE_LOCK_F_GAIN);

         m_driveMode = DriveType.FULL_BRAKE;

         setBrakeMode(true);
      }
      setHighGear(false);
   }

   public void setPath(Path p_path)
   {
      if (m_pathFollower != null)
      {
         if (m_pathFollower.isActive())
         {
            throw new IllegalStateException("One path is already active!");
         }
      }

      m_pathFollower = new PathFollower(p_path, m_leftMaster, m_rightMaster);
   }

   public PathFollower getPathFollower()
   {
      return m_pathFollower;
   }

   public void startFollowingPath()
   {
      if (m_pathFollower == null)
      {
         throw new IllegalStateException("No path set");
      }

      if (m_pathFollower.isActive())
      {
         throw new IllegalStateException("Path is already active");
      }

      m_pathFollower.start();
   }

   public void abortFollowingPath()
   {
      if (m_pathFollower != null)
      {
         m_pathFollower.stop();
      }
   }

   public void pathCleanup()
   {
      if (m_pathFollower != null)
      {
         m_pathFollower.stop();
         m_pathFollower = null;
      }
   }

   @Override
   public String getName()
   {
      return "Drive Base";
   }
   

   public void setHeading(double newHeading)
   {
      m_headingValue = newHeading;
   }

   public void setThrottle(double newThrottle)
   {
      m_throttleValue = newThrottle;
   }
   
   
   
   public void setQuickTurn(boolean p_quickTurn)
   {
      m_quickTurn = p_quickTurn;
   }

   
   public int getYaw()
   {
      return (int)m_ahrs.getYaw();
   }


   /**
    * Returns distance traveled since encoders were set to zero, in inches.
    * @return
    */
   public int getEncoderDistanceInches()
   {
      long leftTick = (long)Math.abs(getLeftEncoderValue());
      long rightTick = (long)Math.abs(getRightEncoderValue());

      return (int) (((leftTick + rightTick) / 2) * TICKS_TO_INCHES);
   }
   
   /**
    * Returns right encoder velocity
    * @return right encoder velocity
    */
   public int getRightEncoderVelocity() {
      return m_rightMaster.getSelectedSensorVelocity(0);
   }
   
   private void updateDashboard()
   {
      
      SmartDashboard.putBoolean("Anti-turbo", m_antiTurbo);
      SmartDashboard.putBoolean("High Gear", m_highGear);
      SmartDashboard.putNumber("Throttle", m_throttleValue);
      SmartDashboard.putNumber("Heading", m_headingValue);
      SmartDashboard.putBoolean("Brake mode", m_brakeMode);
      SmartDashboard.putBoolean("Cube follow", m_cubeFollowMode);
      SmartDashboard.putString("Drive mode", m_driveMode.name());
      SmartDashboard.putNumber("Left Encoder", getLeftEncoderValue());
      SmartDashboard.putNumber("Right Encoder", getRightEncoderValue());

      
//      Logger.getLogger(getClass()).log(Level.WARNING, String.format("Quad left: %d\tQuad right: %d\tEnc left: %d\tEnc right: %d\tVel: %d\n", 
//            m_leftMaster.getSensorCollection().getQuadraturePosition(),
//            m_rightMaster.getSensorCollection().getQuadraturePosition(),
//            m_leftMaster.getSelectedSensorPosition(0),
//            m_rightMaster.getSelectedSensorPosition(0),
//            m_rightMaster.getSelectedSensorVelocity(0)));
//            Matt boi
      
      SmartDashboard.putNumber("Yaw", getYaw());
      SmartDashboard.putNumber("Vision X", m_visionX);
      
   }
   
   
   
   
}
