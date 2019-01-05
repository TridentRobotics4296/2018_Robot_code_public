package org.wildstang.framework.io.outputs;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class RemoteAnalogOutput extends AnalogOutput
{

   NetworkTable remoteIOTable;

   public RemoteAnalogOutput(String name, String p_networkTbl, double p_default)
   {
      super(name, p_default);
      remoteIOTable = NetworkTable.getTable(p_networkTbl);
      System.out.println("Got Table");
   }

   public void notifyConfigChange()
   {
      // Nothing to update here, since the config value only affect the
      // start state.
   }

   @Override
   protected void sendDataToOutput()
   {
      remoteIOTable.putNumber(getName(), getValue());
   }

}