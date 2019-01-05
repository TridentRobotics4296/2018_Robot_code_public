package org.wildstang.framework.io.inputs;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class RemoteAnalogInput extends AnalogInput
{

   NetworkTable remoteIOTable;

   public RemoteAnalogInput(String p_name, String p_networkTbl)
   {
      super(p_name);
      remoteIOTable = NetworkTable.getTable(p_networkTbl);
   }

   public double readRawValue()
   {
      // System.out.println("Getting: " + getName() + " From "
      // + remoteIOTable.toString());
      return remoteIOTable.getNumber(getName(), 0);
   }

}