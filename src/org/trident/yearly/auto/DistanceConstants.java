package org.trident.yearly.auto;

import org.trident.yearly.subsystems.Drive;

public class DistanceConstants
{
   
   //Scale
   public static final int VERTICAL_TO_SCALE = (int)(299.65 - Drive.ROBOT_LENGTH - 3); //Stop 3 in early 
   public static final int HORIZONTAL_SCALE_OFFSET = (int)(65.56 - Drive.ROBOT_WIDTH);
   public static final int DEGREES_TO_SCALE = 55;

   //Cube from scale
   public static final int VERTICAL_SCALE_TO_CUBE = (int)38.53; //This should be slightly larger than the robot depth
   public static final int HORIZONTAL_SCALE_TO_CUBE = (int)(55.56 - Drive.ROBOT_LENGTH);
   public static final int DEGREES_TO_CUBE = 60;
  
   //Switch
   public static final int VERTICAL_TO_SWITCH = 141;
   public static final int HORIZONTAL_TO_SWITCH = (int)(55.56 - Drive.ROBOT_LENGTH) - 2; //stop 2in early to coast;
   public static final int DEGREES_TO_SWITCH = 90;
   public static final int CENTER_TO_SWITCH = (int) (84 + ((36 - Drive.ROBOT_WIDTH) / 2));
   
   //Switch from center
   public static final int CENTER_ANGLED_DISTANCE_TO_SWITCH = (int)(83.25);
   public static final int CENTER_ANGLE_TO_SWITCH = 10; //True value 12.25
   
   //Other
   public static final int VERTICAL_TO_AUTO = 120;  
   
   
}
