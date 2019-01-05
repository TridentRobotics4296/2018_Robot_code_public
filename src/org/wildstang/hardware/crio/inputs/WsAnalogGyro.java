package org.wildstang.hardware.crio.inputs;

import org.wildstang.framework.io.inputs.AnalogInput;

import edu.wpi.first.wpilibj.AnalogGyro;

public class WsAnalogGyro extends AnalogInput
{
   private AnalogGyro m_gyro;
   private boolean m_driftCompensation;
   private long startTime;

   private final double DRIFT_PER_NANO_FIXED = .903; //GOOD DEFAULT VALUE TO USE
   
   private double DRIFT_PER_NANO = DRIFT_PER_NANO_FIXED;

   public WsAnalogGyro(String p_name, int p_channel, boolean p_driftCompensation)
   {
      super(p_name);
      m_gyro = new AnalogGyro(p_channel);
      m_driftCompensation = p_driftCompensation;
      
      m_gyro.calibrate();

      startTime = System.nanoTime();
      double angle1 = m_gyro.getAngle();

      try
      {
         Thread.sleep(500);
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
      
      long endtTime = System.nanoTime();
      double angle2 = m_gyro.getAngle();
      
      DRIFT_PER_NANO = (angle2 - angle1) / (endtTime - startTime);
      
   }

   @Override
   public double readRawValue()
   {
      if (m_driftCompensation)
      {
         return m_gyro.getAngle() - ((System.nanoTime() - startTime) * DRIFT_PER_NANO);
      }
      else
      {
         return m_gyro.getAngle();
      }
   }

   public void calibrate()
   {
      m_gyro.calibrate();
   }
}
