package org.trident.yearly.subsystems.poseables;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Wrist extends AbstractPoseable
{


   
   public Wrist(TalonSRX p_talon)
   {
      super("Wrist", p_talon, PoseableConstants.WRIST_ALLOWABLE_ERROR);
   }

   public void setupTalon()
   {
      m_masterTalon.setNeutralMode(NeutralMode.Brake);
      m_masterTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 10, 0);
      m_masterTalon.setSensorPhase(true);

      m_masterTalon.configClosedloopRamp(PoseableConstants.WRIST_CLOSED_RAMP_RATE, 0);
      m_masterTalon.configOpenloopRamp(PoseableConstants.WRIST_OPEN_RAMP_RATE, 0);
      
      // Configure sensor
      m_masterTalon.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 0);
   }

   public int getCurrentPosition()
   {
      return m_masterTalon.getSelectedSensorPosition(0);
   }

   public double getSpeedFactor()
   {
      return 0.6;
   }
   
   
   public boolean isAtUpperLimit() {
      return m_masterTalon.getSensorCollection().isFwdLimitSwitchClosed();
   }
   
   @Override
   public void atUpperLimit()
   {
      m_masterTalon.setSelectedSensorPosition(0, 0, 0);
   }

   public void configureHoldPID()
   {
      setTalonPID(m_masterTalon,
            PoseableConstants.WRIST_HOLD_P_GAIN, 
            PoseableConstants.WRIST_HOLD_I_GAIN, 
            PoseableConstants.WRIST_HOLD_D_GAIN, 
            PoseableConstants.WRIST_HOLD_F_GAIN);
      m_masterTalon.configAllowableClosedloopError(0, 0, 0);
      }


   public void configureUpPID()
   {
      setTalonPID(m_masterTalon,
            PoseableConstants.WRIST_UP_P_GAIN, 
            PoseableConstants.WRIST_UP_I_GAIN, 
            PoseableConstants.WRIST_UP_D_GAIN, 
            PoseableConstants.WRIST_UP_F_GAIN);
      m_masterTalon.configAllowableClosedloopError(0, PoseableConstants.WRIST_ALLOWABLE_ERROR, 0);
   }


   public void configureDownPID()
   {
      setTalonPID(m_masterTalon,
            PoseableConstants.WRIST_DOWN_P_GAIN, 
            PoseableConstants.WRIST_DOWN_I_GAIN, 
            PoseableConstants.WRIST_DOWN_D_GAIN, 
            PoseableConstants.WRIST_DOWN_F_GAIN);
      m_masterTalon.configAllowableClosedloopError(0, PoseableConstants.WRIST_ALLOWABLE_ERROR, 0);
   }

}
