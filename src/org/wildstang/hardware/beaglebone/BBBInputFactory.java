package org.wildstang.hardware.beaglebone;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.wildstang.framework.core.Inputs;
import org.wildstang.framework.hardware.InputFactory;
import org.wildstang.framework.hardware.WsRemoteAnalogInputConfig;
import org.wildstang.framework.hardware.WsRemoteDigitalInputConfig;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.NullDigitalInput;
import org.wildstang.framework.io.inputs.RemoteAnalogInput;
import org.wildstang.framework.io.inputs.RemoteDigitalInput;
import org.wildstang.hardware.beaglebone.inputs.BBBAnalogInput;
import org.wildstang.hardware.beaglebone.inputs.BBBDigitalInput;
import org.wildstang.hardware.beaglebone.inputs.BBBInputType;
import org.wildstang.hardware.beaglebone.inputs.config.BBBAnalogInputConfig;
import org.wildstang.hardware.beaglebone.inputs.config.BBBDigitalInputConfig;
import org.wildstang.hardware.opencv.inputs.OpenCVInput;

public class BBBInputFactory implements InputFactory
{

   private static Logger s_log = Logger.getLogger(BBBInputFactory.class.getName());
   private static final String s_className = "BBBInputFactory";
   private boolean s_initialised = false;

   public BBBInputFactory()
   {

   }

   public void init()
   {
      if (s_log.isLoggable(Level.FINER)) s_log.entering(s_className, "init");

      if (!s_initialised)
      {
         s_initialised = true;
      }

      if (s_log.isLoggable(Level.FINER)) s_log.exiting(s_className, "init");
   }

   public Input createInput(Inputs p_input)
   {
      if (s_log.isLoggable(Level.FINER)) s_log.entering(s_className, "createDigitalInput");

      Input in = null;

      if (s_log.isLoggable(Level.FINE))
      {
         s_log.fine("Creating digital input: Name = " + p_input.getName()
               + ", type = " + p_input.getType() + ", port = "
               + p_input.getConfig());
      }

      switch ((BBBInputType) p_input.getType())
      {
         case CAMERA:
            in = new OpenCVInput(p_input.getName());
            break;
         case SWITCH:
            in = new BBBDigitalInput(BBBManager.getInstance().getBoard(), p_input.getName(), ((BBBDigitalInputConfig) p_input.getConfig()).getPort(), ((BBBDigitalInputConfig) p_input.getConfig()).getPullup());
            break;
         case POT:
            in = new BBBAnalogInput(BBBManager.getInstance().getBoard(), p_input.getName(), ((BBBAnalogInputConfig) p_input.getConfig()).getPort());
            break;
         case REMOTE_DIGITAL:
            in = new RemoteDigitalInput(p_input.getName(), ((WsRemoteDigitalInputConfig) p_input.getConfig()).getTableName());
            break;
         case REMOTE_ANALOG:
            in = new RemoteAnalogInput(p_input.getName(), ((WsRemoteAnalogInputConfig) p_input.getConfig()).getTableName());
            break;
         case NULL:
         default:
            in = new NullDigitalInput(p_input.getName());
            break;
      }

      if (s_log.isLoggable(Level.FINER)) s_log.exiting(s_className, "createDigitalInput");

      return in;
   }
}
