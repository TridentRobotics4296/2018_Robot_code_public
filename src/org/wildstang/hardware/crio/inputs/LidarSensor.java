package org.wildstang.hardware.crio.inputs;

import java.util.TimerTask;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LidarSensor
{
   private I2C i2c;
   private byte[] distance;
   private java.util.Timer updater;
   private Integer[] recordedDistances = new Integer[6];

   private final int LIDAR_ADDR;
   private final int LIDAR_CONFIG_REGISTER = 0x00;
   private final int LIDAR_DISTANCE_REGISTER = 0x8f;

   public LidarSensor(Port port, int address)
   {
      LIDAR_ADDR = address;
      i2c = new I2C(port, LIDAR_ADDR);

      distance = new byte[2];

      updater = new java.util.Timer();
   }

   // Distance in cm
   public int getDistance()
   {
      return 0; // (int) Integer.toUnsignedLong(distance[0] << 8) +
                // Byte.toUnsignedInt(distance[1]);
   }

   // Returns average of the last 5 readings
   public int getSmoothedDistance()
   {
      int accumulator = 0;
      int numValidElements = 0;
      for (int i = 0; i < recordedDistances.length; i++)
      {
         if (recordedDistances[i] != null)
         {
            accumulator += recordedDistances[i].intValue();
            numValidElements++;
         }
      }

      // Avoid divide by zero errors
      if (numValidElements > 0)
      {
         return (int) accumulator / numValidElements;
      }
      else
      {
         return 0;
      }
   }

   public double pidGet()
   {
      return getDistance();
   }

   // Start 10Hz polling
   public void start()
   {
      updater.scheduleAtFixedRate(new LIDARUpdater(), 0, 100);
   }

   // Start polling for period in milliseconds
   public void start(int period)
   {
      updater.scheduleAtFixedRate(new LIDARUpdater(), 0, period);
   }

   public void stop()
   {
      updater.cancel();
      updater = new java.util.Timer();
   }

   // Update distance variable
   public void update()
   {
      i2c.write(LIDAR_CONFIG_REGISTER, 0x04); // Initiate measurement
      Timer.delay(0.04); // Delay for measurement to be taken
      i2c.read(LIDAR_DISTANCE_REGISTER, 2, distance); // Read in measurement
      Timer.delay(0.005); // Delay to prevent over polling

      // Store most recent results in a sliding window
      for (int i = 0; i < recordedDistances.length; i++)
      {
         if (i < recordedDistances.length - 1)
         {
            recordedDistances[i] = recordedDistances[i + 1];
         }
         else
         {
            recordedDistances[i] = new Integer(getDistance());
         }
      }
   }

   // Timer task to keep distance updated
   private class LIDARUpdater extends TimerTask
   {
      public void run()
      {
         while (true)
         {
            update();
            try
            {
               Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
               e.printStackTrace();
            }
         }
      }
   }
}
