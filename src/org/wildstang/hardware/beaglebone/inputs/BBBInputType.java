package org.wildstang.hardware.beaglebone.inputs;

import org.wildstang.framework.io.inputs.InputType;

public enum BBBInputType implements InputType
{

   REMOTE_ANALOG("RemoteAnalogInput"),
   REMOTE_DIGITAL("RemoteDigitalInput"),
   CAMERA("Camera"),
   POT("Pot"),
   SWITCH("Switch"),
   NULL("Null");

   private String m_typeStr;

   BBBInputType(String p_typeStr)
   {
      m_typeStr = p_typeStr;
   }

   @Override
   public String toString()
   {
      return m_typeStr;
   }
}
