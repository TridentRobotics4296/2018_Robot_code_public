package org.wildstang.hardware.raspberrypi.inputs;

import org.wildstang.framework.io.inputs.InputType;

public enum RPiInputType implements InputType
{

   REMOTE_ANALOG("RemoteAnalogInput"),
   REMOTE_DIGITAL("RemoteDigitalInput"),
   CAMERA("Camera"),
   POT("Pot"),
   SWITCH("Switch"),
   NULL("Null");

   private String m_typeStr;

   RPiInputType(String p_typeStr)
   {
      m_typeStr = p_typeStr;
   }

   @Override
   public String toString()
   {
      return m_typeStr;
   }
}
