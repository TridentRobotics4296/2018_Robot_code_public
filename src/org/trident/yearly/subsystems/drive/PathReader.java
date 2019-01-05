package org.trident.yearly.subsystems.drive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.trident.yearly.subsystems.Drive;

import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class PathReader
{

   /**
    * Find enum value if supported.
    * @param durationMs
    * @return enum equivalent of durationMs
    */
   private static TrajectoryDuration getTrajectoryDuration(int durationMs)
   {   
      /* create return value */
      TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;
      /* convert duration to supported type */
      retval = retval.valueOf(durationMs);
      /* check that it is valid */
      if (retval.value != durationMs) {
         DriverStation.reportError("Trajectory Duration not supported - use configMotionProfileTrajectoryPeriod instead", false);      
      }
      /* pass to caller */
      return retval;
   }
   
   private static double feetToTics(double feet)
   {
      return ((feet * 12) / (Drive.WHEEL_DIAMETER_INCHES * Math.PI)) * Drive.ENCODER_CPR;
   }
   
   private static double feetSecToNativeVelocity(double feetPerSecond)
   {
      return (feetToTics(feetPerSecond) / 10);
   }
   
   public static Trajectory readTrajectory(File p_path)
   {
      SmartDashboard.putBoolean("started reading", true);
      Trajectory trajectory = new Trajectory();

      ArrayList<String> rawData = new ArrayList<String>();
      
      // Open the file
      BufferedReader reader = null;

      try
      {
         reader = new BufferedReader(new FileReader(p_path));
      }
      catch (FileNotFoundException e)
      {
         SmartDashboard.putBoolean("Path Reading Exception1", true);
         e.printStackTrace();
      }
      
    
      
      if (reader != null)
      {
         SmartDashboard.putBoolean("Reading Traj Data", true);
         String line;
         try
         {
            
            // Read all the lines.  Sort into left and right paths
            while ((line = reader.readLine()) != null)
            {
               rawData.add(line);
               SmartDashboard.putString("Trajectory data", line); 
            }
         }
         catch (IOException e)
         {
            SmartDashboard.putBoolean("Path Reading Exception", true);
            e.printStackTrace();
         }
      }
      
      double[][] dataPoints = new double[rawData.size()][];
      TrajectoryPoint mpPoint = new TrajectoryPoint();
      ArrayList<TrajectoryPoint> trajPoints = new ArrayList<TrajectoryPoint>();
      
      // Parse into numbers
      for (int i = 1; i < rawData.size(); i++)
      {
         String tempLine = rawData.get(i);
         String[] entry = tempLine.split(",");

         dataPoints[i] = new double[3];
         double rotations = feetToTics(Double.parseDouble(entry[3]));
         double velocity = feetSecToNativeVelocity(Double.parseDouble(entry[4]));
         double interval = Double.parseDouble(entry[0]) * 1000;
         
         dataPoints[i][0] = rotations;
         dataPoints[i][1] = velocity;
         dataPoints[i][2] = interval;
         
         //TODO: update this
         mpPoint.headingDeg = 0;
         mpPoint.profileSlotSelect0 = 0;
         mpPoint.profileSlotSelect1 = 0;
         
         // Create a TrajectoryPoint for the Talon - do this while reading the file
         mpPoint = new TrajectoryPoint();
         mpPoint.position = rotations;
         mpPoint.velocity = velocity;
         mpPoint.timeDur = getTrajectoryDuration((int) interval);
         //mpPoint.profileSlotSelect = 0; // which set of gains would you like to use?
//         mpPoint.velocityOnly = false; // set true to not do any position servo, just velocity feedforward
         mpPoint.zeroPos = false;

         if (i == 0)
         {
            mpPoint.zeroPos = true; // set this to true on the first point
         }
         
         mpPoint.isLastPoint = false;

         if ((i + 1) == rawData.size())
         {
            mpPoint.isLastPoint = true; // set this to true on the last point
         }
         
         trajPoints.add(mpPoint);
      }
      
      trajectory.setTrajectoryPoints(dataPoints);
      trajectory.setTalonPoints(trajPoints);
      
      return trajectory;
   }
}
