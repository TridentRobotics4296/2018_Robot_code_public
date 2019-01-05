package org.wildstang.framework.io.inputs;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NullDiscreteInput extends DiscreteInput
{

   private static Logger s_log = Logger.getLogger(NullDiscreteInput.class.getName());
   private static final String s_className = "NullDiscreteInput";

   public NullDiscreteInput(String p_name)
   {
      super(p_name);
   }

   public NullDiscreteInput(String p_name, int p_default)
   {
      super(p_name, p_default);
   }

   

   @Override
   protected int readRawValue()
   {
      return 0;
   }

}
