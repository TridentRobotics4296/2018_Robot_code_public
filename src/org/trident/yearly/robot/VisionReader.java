package org.trident.yearly.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionReader implements Runnable
{

   private boolean m_running;
   private SerialPort m_port;
   private String m_message = "";
   private double m_latest = 0.0;
   
   public VisionReader(SerialPort port)
   {
      m_port = port;
   }
   
   @Override
   public void run()
   {
      m_running = true;
      
      while (m_running)
      {
         m_message = m_port.readString();

         //Get last line
         String rawInput = m_message;
         
         if (rawInput != null && !rawInput.equals("") && rawInput.length() > 1)
         {
            int lastIndex = rawInput.lastIndexOf('\n');
            int secondLast = rawInput.lastIndexOf('\n', lastIndex - 1);
            m_latest = Double.parseDouble(rawInput.substring(secondLast + 1, lastIndex));
            
            SmartDashboard.putString("Serial Port String", m_message);
         }
         
         try
         {
            // Add a delay to not max CPU
            Thread.sleep(50);
         }
         catch (InterruptedException e)
         {
            e.printStackTrace();
         }
      }
   }
   
   public double getLatest()
   {
      return m_latest;
   }
   
   public boolean isRunning()
   {
      return m_running;
   }
   
   public void stop()
   {
      m_running = false;
   }

}
