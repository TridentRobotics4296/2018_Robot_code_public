package org.wildstang.hardware.beaglebone.outputs;

import org.bulldog.core.Signal;
import org.bulldog.core.platform.Board;

import org.wildstang.framework.io.outputs.DigitalOutput;

public class BBBDigitalOutput extends DigitalOutput
{

   private org.bulldog.core.gpio.DigitalOutput m_output;

   public BBBDigitalOutput(Board board, String p_name, String p_port, boolean p_default)
   {
      super(p_name, p_default);
      m_output = board.getPin(p_port).as(org.bulldog.core.gpio.DigitalOutput.class);
   }

   @Override
   protected void sendDataToOutput()
   {
      if (getValue())
      {
         m_output.write(Signal.High);
      }
      else
      {
         m_output.write(Signal.Low);
      }

   }

}
