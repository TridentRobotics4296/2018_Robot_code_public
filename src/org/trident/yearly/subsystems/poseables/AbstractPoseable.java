package org.trident.yearly.subsystems.poseables;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class AbstractPoseable implements Poseable
{
   protected TalonSRX m_masterTalon;
   
   private int m_allowableError;
   private int m_target;
   private String m_name;
   
//   private PoseWatcher m_watcher;
   private boolean m_movingToPose = false;
   private boolean m_holding = false;

   
   public AbstractPoseable(String p_name, TalonSRX p_talon, int p_allowedError)
   {
      m_masterTalon = p_talon;
      m_allowableError = p_allowedError;
      m_name = p_name;

      setupTalon();
   }
   
   public void manualMove(double p_speed)
   {
      double actualSpeed = p_speed * getSpeedFactor();
      m_masterTalon.set(ControlMode.PercentOutput, actualSpeed);
      
      m_holding = false;
   }
   
   public void holdPosition()
   {
      // Run this once so it isn't chasing a moving encoder position
      if (!m_holding)
      {
         int currentPos = getCurrentPosition();
         
         m_masterTalon.selectProfileSlot(0, 0);
         configureHoldPID();
         
         m_masterTalon.set(ControlMode.Position, currentPos);
         
         m_holding = true;
      }
   }
   
   public void turnOff()
   {
      m_masterTalon.set(ControlMode.PercentOutput, 0);
      
      m_holding = false;
   }

   public abstract double getSpeedFactor();
   
   public abstract void configureHoldPID();
   
   public abstract void configureUpPID();

   public abstract void configureDownPID();
   
   public abstract void setupTalon();
   
   public void atUpperLimit()
   {
      // Do nothing by default - each Poseable should override
   }
   
   public void atLowerLimit()
   {
      // Do nothing by default - each Poseable should override
   }

   
   @Override
   public void moveTo(int position, PoseEndAction p_endAction)
   {
      m_holding = false;
      
      m_target = position;

      m_masterTalon.selectProfileSlot(0,  0);

      if (m_target < getCurrentPosition())
      {
         configureDownPID();
      }
      else
      {
         configureUpPID();
      }
      
//      m_watcher = new PoseWatcher(this, p_endAction);
//      Thread t = new Thread(m_watcher);
//      t.start();

      m_movingToPose = true;
      m_masterTalon.set(ControlMode.Position, m_target);
   }

   public void setMovingToPose(boolean isPosing)
   {
      m_movingToPose = isPosing;
   }
   
   public boolean isMovingToPose()
   {
      return m_movingToPose;
   }
   
   protected void setTalonPID(TalonSRX p_talon, double p, double i, double d, double f)
   {
      p_talon.config_kP(0, p, 0);
      p_talon.config_kI(0, i, 0);
      p_talon.config_kD(0, d, 0);
      p_talon.config_kF(0, f, 0);
   }

   @Override
   public void cancelPose()
   {
      m_movingToPose = false;
//      if (m_watcher != null)
//      {
//         m_watcher.stop();
//      }
   }

   @Override
   public boolean inPosition()
   {
      return Math.abs(getTargetPosition() - getCurrentPosition()) < (getAllowableError() + 300);
   }

   public abstract int getCurrentPosition();

   public int getAllowableError()
   {
      return m_allowableError;
   }
   
   public int getTargetPosition()
   {
      return m_target;
   }

   public void updateDashboard()
   {
      SmartDashboard.putBoolean(m_name + " in position", inPosition());
      SmartDashboard.putBoolean(m_name + " moving to pose", isMovingToPose());
      SmartDashboard.putBoolean(m_name + " holding position", m_holding);
      
      SmartDashboard.putNumber(m_name + " target", getTargetPosition());
      SmartDashboard.putNumber(m_name + " allowed error", getAllowableError());
      SmartDashboard.putNumber(m_name + " current", getCurrentPosition());

      SmartDashboard.putBoolean(m_name + " up limit", m_masterTalon.getSensorCollection().isFwdLimitSwitchClosed());
      SmartDashboard.putBoolean(m_name + " down limit", m_masterTalon.getSensorCollection().isRevLimitSwitchClosed());
      
   }
   
}
