package org.trident.yearly.subsystems.poseables;



import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;


public class Arm extends AbstractPoseable
{

   
   public static final int ENCODER_SAFE_LIMIT = PoseSetpoints.ARM_SWITCH + 100;
   
   public Arm(TalonSRX p_talon)
   {
      super("Arm", p_talon, PoseableConstants.ARM_ALLOWABLE_ERROR);
   }
   
   public void setupTalon()
   {
      m_masterTalon.setNeutralMode(NeutralMode.Brake);
      m_masterTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 10, 0);

      m_masterTalon.configClosedloopRamp(PoseableConstants.ARM_CLOSED_RAMP_RATE, 0);
      m_masterTalon.configOpenloopRamp(PoseableConstants.ARM_OPEN_RAMP_RATE, 0);
      
      // Configure sensor
      m_masterTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
      int absolutePos = m_masterTalon.getSensorCollection().getPulseWidthPosition();
      m_masterTalon.setSelectedSensorPosition(absolutePos, 0, 10);

   }

   public double getSpeedFactor()
   {
      return 0.6;
   }
   
   @Override
   public void atLowerLimit()
   {
      // Reset both the absolute and relative values. If absolute is also zeroed, we can
      // seed the relative encoder with its value on startup, so we have a known position.
      // Re-zeroing here means we don't have to run any one-off routines outside of a match
      m_masterTalon.getSensorCollection().setPulseWidthPosition(0,  0);
      m_masterTalon.setSelectedSensorPosition(0, 0, 0);
   }

   public boolean isAtLowerLimit() {
      return m_masterTalon.getSensorCollection().isRevLimitSwitchClosed();
   }
   
   public int getCurrentPosition()
   {
      return m_masterTalon.getSelectedSensorPosition(0);
   }

   
   public boolean heightSafeToMoveLift()
   {
      return (getCurrentPosition() < ENCODER_SAFE_LIMIT);
   }
   
   public void configureHoldPID()
   {
      setTalonPID(m_masterTalon,
            PoseableConstants.ARM_HOLD_P_GAIN, 
            PoseableConstants.ARM_HOLD_I_GAIN, 
            PoseableConstants.ARM_HOLD_D_GAIN, 
            PoseableConstants.ARM_HOLD_F_GAIN);
      m_masterTalon.configAllowableClosedloopError(0, 0, 0);
   }


   
   public void configureUpPID()
   {
      setTalonPID(m_masterTalon,
            PoseableConstants.ARM_UP_P_GAIN, 
            PoseableConstants.ARM_UP_I_GAIN, 
            PoseableConstants.ARM_UP_D_GAIN, 
            PoseableConstants.ARM_UP_F_GAIN);
      m_masterTalon.configAllowableClosedloopError(0, PoseableConstants.ARM_ALLOWABLE_ERROR, 0);
   }


   public void configureDownPID()
   {
      setTalonPID(m_masterTalon,
            PoseableConstants.ARM_DOWN_P_GAIN, 
            PoseableConstants.ARM_DOWN_I_GAIN, 
            PoseableConstants.ARM_DOWN_D_GAIN, 
            PoseableConstants.ARM_DOWN_F_GAIN);
      m_masterTalon.configAllowableClosedloopError(0, PoseableConstants.ARM_ALLOWABLE_ERROR, 0);
   }
}
