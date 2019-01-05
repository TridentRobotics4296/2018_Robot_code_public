package org.wildstang.hardware.beaglebone.inputs.config;

import org.wildstang.framework.hardware.InputConfig;

public class BBBDigitalInputConfig implements InputConfig
{
   private String m_port;
   private boolean m_pullup = false;
   
   public BBBDigitalInputConfig(String p_port, boolean p_pullup)
   {
      m_port = p_port;
      m_pullup = p_pullup;
   }

   public String getPort()
   {
      return m_port;
   }

   public boolean getPullup()
   {
      return m_pullup;
   }
   
   @Override
   public String toString()
   {
      StringBuffer buf = new StringBuffer();
      
      buf.append("{\"port\": ");
      buf.append(m_port);
      buf.append(",\"pullup\": \"");
      buf.append(m_pullup);
      buf.append("\"}");
      
      return buf.toString();
   }
   
}
