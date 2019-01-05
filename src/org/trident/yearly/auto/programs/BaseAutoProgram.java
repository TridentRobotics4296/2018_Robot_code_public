package org.trident.yearly.auto.programs;

import org.wildstang.framework.auto.AutoProgram;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class BaseAutoProgram extends AutoProgram
{
   private boolean m_startOnLeft = true;
   private boolean m_startOnRight = true;
   private boolean m_startCenter = true;
   
   private boolean m_switchOnLeft = false;
   private boolean m_scaleOnLeft = false;
   private boolean m_validGameData = false;

   @Override
   public void initialize(String position)
   {
      // Get the game data for positions of switch and scale
      String gameData = DriverStation.getInstance().getGameSpecificMessage();
      
      if (gameData != null && gameData.length() > 0)
      {
         m_switchOnLeft = gameData.charAt(0) == 'L' || gameData.charAt(0) == 'l';
         m_scaleOnLeft = gameData.charAt(1) == 'L' || gameData.charAt(1) == 'l';
      }
      else
      {
         m_validGameData = false;
      }
      
      // Get our start position
      m_startOnLeft = position.equalsIgnoreCase("left");
      m_startOnRight = position.equalsIgnoreCase("right");
      m_startCenter = position.equalsIgnoreCase("center");

      // For reference
      SmartDashboard.putString("Game data", gameData == null ? "null" : gameData);

      // IMPORTANT: Call initialize last, as it calls defineSteps(), which needs the above information
      super.initialize(position);

   }

   public boolean hasValidGameData()
   {
      return m_validGameData;
   }

   public boolean switchOnLeft()
   {
      return m_switchOnLeft;
   }
   
   public boolean scaleOnLeft()
   {
      return m_scaleOnLeft;
   }
   
   public boolean startOnLeft()
   {
      return m_startOnLeft;
   }
   
   public boolean startOnRight()
   {
      return m_startOnRight;
   }
   public boolean startCenter()
   {
      return m_startCenter;
   }


}
