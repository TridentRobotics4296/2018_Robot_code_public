package org.wildstang.hardware.beaglebone.inputs;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bulldog.core.Signal;
import org.bulldog.core.platform.Board;

import org.wildstang.framework.io.inputs.DigitalInput;

public class BBBDigitalInput extends DigitalInput
{
   private static Logger s_log = Logger.getLogger(BBBDigitalInput.class.getName());
   private static final String s_className = "BBBDigitalInput";

   private org.bulldog.core.gpio.DigitalInput buttonSignal = null;
   private Signal m_baseState;

   public BBBDigitalInput(Board board, String p_name, String p_port, boolean p_pullup)
   {
      super(p_name);
      
      //Set up a digital input
      buttonSignal = board.getPin(p_port).as(org.bulldog.core.gpio.DigitalInput.class);
      if (p_pullup)
      {
         m_baseState = Signal.High;
      }
      else
      {
         m_baseState = Signal.Low;
      }
   }

   @Override
   protected boolean readRawValue()
   {
      boolean value = false;

      if (s_log.isLoggable(Level.FINER)) s_log.entering(s_className, "readRawValue");

      // Read the input from the BBB
      Signal input = buttonSignal.read();
      value = !input.equals(m_baseState);

      if (s_log.isLoggable(Level.FINER)) s_log.exiting(s_className, "readRawValue");

      return value;
   }

}
