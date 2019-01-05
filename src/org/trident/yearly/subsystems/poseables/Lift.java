package org.trident.yearly.subsystems.poseables;


import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Lift extends AbstractPoseable
{
   private static final int ENCODER_SAFE_LIMIT = 100;

   public Lift(TalonSRX p_talon)
   {
      super("Lift", p_talon, PoseableConstants.LIFT_ALLOWABLE_ERROR);
   }

   public void setupTalon()
   {
      m_masterTalon.setNeutralMode(NeutralMode.Brake);
      m_masterTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 10, 0);

      m_masterTalon.configClosedloopRamp(PoseableConstants.LIFT_CLOSED_RAMP_RATE, 0);
      m_masterTalon.configOpenloopRamp(PoseableConstants.LIFT_OPEN_RAMP_RATE, 0);
      
      // Configure sensor
      m_masterTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
   }

   public int getCurrentPosition()
   {
      // Check correct encoder reading
      return m_masterTalon.getSelectedSensorPosition(0);
   }

   public double getSpeedFactor()
   {
      return 1.0;
   }
   
   @Override
   public void atLowerLimit()
   {
      m_masterTalon.setSelectedSensorPosition(0, 0, 0);
   }

   public boolean isAtLowerLimit()
   {
      return m_masterTalon.getSensorCollection().isRevLimitSwitchClosed();
   }
   
   public void configureHoldPID()
   {
      setTalonPID(m_masterTalon,
            PoseableConstants.LIFT_HOLD_P_GAIN, 
            PoseableConstants.LIFT_HOLD_I_GAIN, 
            PoseableConstants.LIFT_HOLD_D_GAIN, 
            PoseableConstants.LIFT_HOLD_F_GAIN);
      m_masterTalon.configAllowableClosedloopError(0, 0, 0);
   }

   
   public void configureUpPID()
   {
      setTalonPID(m_masterTalon,
            PoseableConstants.LIFT_UP_P_GAIN, 
            PoseableConstants.LIFT_UP_I_GAIN, 
            PoseableConstants.LIFT_UP_D_GAIN, 
            PoseableConstants.LIFT_UP_F_GAIN);
      m_masterTalon.configAllowableClosedloopError(0, PoseableConstants.LIFT_ALLOWABLE_ERROR, 0);
   }


   public void configureDownPID()
   {
      setTalonPID(m_masterTalon,
            PoseableConstants.LIFT_DOWN_P_GAIN, 
            PoseableConstants.LIFT_DOWN_I_GAIN, 
            PoseableConstants.LIFT_DOWN_D_GAIN, 
            PoseableConstants.LIFT_DOWN_F_GAIN);
      m_masterTalon.configAllowableClosedloopError(0, PoseableConstants.LIFT_ALLOWABLE_ERROR, 0);
   }
}
