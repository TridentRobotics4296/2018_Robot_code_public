package org.wildstang.hardware.beaglebone.inputs;

import org.bulldog.core.platform.Board;

import org.wildstang.framework.io.inputs.AnalogInput;

public class BBBAnalogInput extends AnalogInput
{
   private org.bulldog.core.gpio.AnalogInput m_analogInput;

   public BBBAnalogInput(Board board, String p_name, String p_port)
   {

      super(p_name);
      m_analogInput = board.getPin(p_port).as(org.bulldog.core.gpio.AnalogInput.class);

   }
   
   @Override
   protected double readRawValue()
   {
      return m_analogInput.read();
   }

}
