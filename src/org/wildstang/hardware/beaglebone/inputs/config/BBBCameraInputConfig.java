package org.wildstang.hardware.beaglebone.inputs.config;

import org.wildstang.framework.hardware.InputConfig;

public class BBBCameraInputConfig implements InputConfig
{
   private int m_channel = 0;

   public BBBCameraInputConfig(int channel)
   {
      m_channel = channel;
   }

   public int getChannel()
   {
      return m_channel;
   }
}
