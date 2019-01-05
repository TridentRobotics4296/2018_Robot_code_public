package org.trident.yearly.robot;

// expand this and edit if trouble with Ws
import org.wildstang.framework.core.Outputs;
import org.wildstang.framework.hardware.OutputConfig;
import org.wildstang.framework.io.outputs.OutputType;
import org.wildstang.hardware.crio.outputs.WSOutputType;
import org.wildstang.hardware.crio.outputs.WsDoubleSolenoidState;
import org.wildstang.hardware.crio.outputs.config.WsI2COutputConfig;
import org.wildstang.hardware.crio.outputs.config.WsSolenoidConfig;
import org.wildstang.hardware.crio.outputs.config.WsVictorConfig;
import org.wildstang.hardware.crio.outputs.config.WsDigitalOutputConfig;
import org.wildstang.hardware.crio.outputs.config.WsDoubleSolenoidConfig;

import edu.wpi.first.wpilibj.I2C;

public enum WSOutputs implements Outputs
{
   // ********************************
   // PWM Outputs
   // ********************************
   //---------------------------------             
   // Motors
   //---------------------------------             
   // All CAN controllers are being used                                          
   
   //---------------------------------             
   // Servos
   //---------------------------------             

   // ********************************
   // DIO Outputs                             
   // ********************************

   // ********************************
   // Solenoids
   // ********************************
   SHIFTER("Shifter",        WSOutputType.SOLENOID_DOUBLE, new WsDoubleSolenoidConfig(0, 0, 1, WsDoubleSolenoidState.FORWARD), false),
   CLAW("Claw",               WSOutputType.SOLENOID_SINGLE, new WsSolenoidConfig(0, 5, false), false);
//   CLIMB_ASSIST("Climb assist",                WSOutputType.SOLENOID_DOUBLE, new WsDoubleSolenoidConfig(0, 2, 3, WsDoubleSolenoidState.FORWARD), false);

   // ********************************
   // Relays
   // ********************************
   //RELAY_0("Relay 0",                        WSOutputType.RELAY, new WsRelayConfig(0, WsRelayState.RELAY_OFF), false), // Relay 0, Both Off
   //RELAY_1("Relay 1",                        WSOutputType.RELAY, new WsRelayConfig(1, WsRelayState.RELAY_OFF), false), // Relay 1, Both Off
   //RELAY_2("Relay 2",                        WSOutputType.RELAY, new WsRelayConfig(2, WsRelayState.RELAY_OFF), false), // Relay 2, Both Off
   //RELAY_3("Relay 3",                        WSOutputType.RELAY, new WsRelayConfig(3, WsRelayState.RELAY_OFF), false), // Relay 3, Both Off

   // ********************************
   // Others ...
   // ********************************
//   LED("LEDs",                               WSOutputType.I2C, new WsI2COutputConfig(I2C.Port.kMXP, 0x10), false);

   
   private String m_name;
   private OutputType m_type;
   private OutputConfig m_config;
   private boolean m_trackingState;
   
   private static boolean isLogging = true;

   WSOutputs(String p_name, OutputType p_type, OutputConfig p_config, boolean p_trackingState)
   {
      m_name = p_name;
      m_type = p_type;
      m_config = p_config;
      m_trackingState = p_trackingState;
   }
   
  
   @Override
   public String getName()
   {
      return m_name;
   }
   
   @Override
   public OutputType getType()
   {
      return m_type;
   }
   
   public OutputConfig getConfig()
   {
      return m_config;
   }

   public boolean isTrackingState()
   {
      return m_trackingState;
   }
   
   public static boolean getLogging()
   {
      return isLogging;
   }

}
