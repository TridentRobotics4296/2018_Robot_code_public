package org.wildstang.hardware.beaglebone.outputs;

import org.wildstang.framework.io.outputs.OutputType;

public enum BBBOutputType implements OutputType
{

   DIGITAL_OUTPUT("Digital"), SERVO("Servo"), REMOTE_DIGITAL(
         "RemoteDigitalOutput"), REMOTE_ANALOG("RemoteAnalogOutput"), NULL(
         "Null");

   private String m_typeStr;

   BBBOutputType(String p_typeStr)
   {
      m_typeStr = p_typeStr;
   }

   @Override
   public String toString()
   {
      return m_typeStr;
   }
}
