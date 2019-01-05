package org.wildstang.framework.io.outputs;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class RemoteDigitalOutput extends DigitalOutput
{

   NetworkTable remoteIOTable;

   public RemoteDigitalOutput(String name, String p_networkTbl,
         boolean p_default)
   {
      super(name, p_default);
      System.out.println("Getting Network Table: " + p_networkTbl);
      remoteIOTable = NetworkTable.getTable(p_networkTbl);
   }

   public void notifyConfigChange()
   {
      // Nothing to update here, since the config value only affect the
      // start state.
   }

   @Override
   protected void sendDataToOutput()
   {
      remoteIOTable.putBoolean(getName(), getValue());
   }

}