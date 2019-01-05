package org.wildstang.hardware.beaglebone;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.wildstang.framework.core.Outputs;
import org.wildstang.framework.hardware.OutputFactory;
import org.wildstang.framework.hardware.WsRemoteAnalogOutputConfig;
import org.wildstang.framework.hardware.WsRemoteDigitalOutputConfig;
import org.wildstang.framework.io.Output;
import org.wildstang.framework.io.outputs.NullDigitalOutput;
import org.wildstang.framework.io.outputs.RemoteAnalogOutput;
import org.wildstang.framework.io.outputs.RemoteDigitalOutput;
import org.wildstang.hardware.beaglebone.outputs.BBBDigitalOutput;
import org.wildstang.hardware.beaglebone.outputs.BBBOutputType;
import org.wildstang.hardware.beaglebone.outputs.BBBServo;
import org.wildstang.hardware.beaglebone.outputs.config.BBBDigitalOutputConfig;
import org.wildstang.hardware.beaglebone.outputs.config.BBBServoConfig;

public class BBBOutputFactory implements OutputFactory
{

   private static Logger s_log = Logger.getLogger(BBBOutputFactory.class.getName());
   private static final String s_className = "BBBOutputFactory";

   private boolean s_initialised = false;

   public BBBOutputFactory()
   {

   }

   @Override
   public void init()
   {
      if (s_log.isLoggable(Level.FINER)) s_log.entering(s_className, "init");

      if (!s_initialised)
      {
         s_initialised = true;
      }

      if (s_log.isLoggable(Level.FINER)) s_log.exiting(s_className, "init");
   }

   @Override
   public Output createOutput(Outputs p_output)
   {
      if (s_log.isLoggable(Level.FINER)) s_log.entering(s_className, "createDigitalInput");

      Output out = null;

      if (s_log.isLoggable(Level.FINE))
      {
         s_log.fine("Creating digital output: Name = " + p_output.getName()
               + ", type = " + p_output.getType() + ", port = "
               + p_output.getConfig());
      }

      switch ((BBBOutputType) p_output.getType())
      {
         case DIGITAL_OUTPUT:
            out = new BBBDigitalOutput(BBBManager.getInstance().getBoard(), p_output.getName(), ((BBBDigitalOutputConfig) p_output.getConfig()).getPort(), ((BBBDigitalOutputConfig) p_output.getConfig()).getDefault());
            break;
         case SERVO:
            out = new BBBServo(BBBManager.getInstance().getBoard(), p_output.getName(), ((BBBServoConfig) p_output.getConfig()).getPort(), ((BBBServoConfig) p_output.getConfig()).getDefault());
            break;
         case REMOTE_DIGITAL:
            out = new RemoteDigitalOutput(p_output.getName(), ((WsRemoteDigitalOutputConfig) p_output.getConfig()).getTableName(), ((WsRemoteDigitalOutputConfig) p_output.getConfig()).getDefault());
            break;
         case REMOTE_ANALOG:
            out = new RemoteAnalogOutput(p_output.getName(), ((WsRemoteAnalogOutputConfig) p_output.getConfig()).getTableName(), ((WsRemoteAnalogOutputConfig) p_output.getConfig()).getDefault());
            break;
         case NULL:
         default:
            out = new NullDigitalOutput(p_output.getName());
      }

      if (s_log.isLoggable(Level.FINER)) s_log.exiting(s_className, "createDigitalInput");

      return out;
   }

}
