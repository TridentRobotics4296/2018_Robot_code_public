package org.wildstang.hardware.crio.inputs;


public class WsAbsoluteEncoder extends WsAnalogInput
{
   int m_maxVoltage = 5;
   
   public WsAbsoluteEncoder(String p_name, int channel, int p_maxVoltage)
   {
      super(p_name, channel);
      m_maxVoltage = p_maxVoltage;
   }

   public double readRawValue()
   {
      double rawValue = super.readRawValue();
      
      double position = (rawValue / m_maxVoltage) * 360;

      return position;
   }

}
