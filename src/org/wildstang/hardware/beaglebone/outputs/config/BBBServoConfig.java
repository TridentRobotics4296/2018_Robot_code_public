package org.wildstang.hardware.beaglebone.outputs.config;

import org.wildstang.framework.hardware.OutputConfig;

public class BBBServoConfig implements OutputConfig
{
   private String m_port;
   private double m_default;
   
   public BBBServoConfig(String p_port, double p_default)
   {
      m_port = p_port;
      m_default = p_default;
   }

   public String getPort()
   {
      return m_port;
   }

   public double getDefault()
   {
      return m_default;
   }
   
   @Override
   public String toString()
   {
      StringBuffer buf = new StringBuffer();
      
      buf.append("{\"port\": \"");
      buf.append(m_port);
      buf.append("\"}");
      
      return buf.toString();
   }

}
