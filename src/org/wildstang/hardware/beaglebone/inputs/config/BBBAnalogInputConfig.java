package org.wildstang.hardware.beaglebone.inputs.config;

import org.wildstang.framework.hardware.InputConfig;

public class BBBAnalogInputConfig implements InputConfig
{
   private String m_port;
   
   public BBBAnalogInputConfig(String p_port)
   {
      m_port = p_port;
   }

   public String getPort()
   {
      return m_port;
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
