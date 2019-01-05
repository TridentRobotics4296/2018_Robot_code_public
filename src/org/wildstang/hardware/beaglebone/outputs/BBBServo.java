package org.wildstang.hardware.beaglebone.outputs;

import org.bulldog.core.gpio.Pwm;
import org.bulldog.core.platform.Board;
import org.bulldog.core.util.BulldogUtil;
import org.bulldog.devices.servo.Servo;
import org.bulldog.devices.servo.TowerProMicroSG90;

import org.wildstang.framework.io.outputs.AnalogOutput;

public class BBBServo extends AnalogOutput
{
   
   private Pwm m_pwmPin;
   private Servo m_servo;
   
   public BBBServo(Board p_board, String p_name, String p_port, double p_default)
   {
      super(p_name, p_default);

      m_pwmPin = p_board.getPin(p_port).as(Pwm.class);
      m_servo = new TowerProMicroSG90(m_pwmPin);

   }
   
   
   @Override
   protected void sendDataToOutput()
   {
//      Thread t = new Thread(new Runnable()
//      {
//         
//         @Override
//         public void run()
//         {
            m_servo.moveSmoothAsyncTo(getValue());
//            BulldogUtil.sleepMs(20);
//         }
//      });
//      t.start();
   }
   
   

}
