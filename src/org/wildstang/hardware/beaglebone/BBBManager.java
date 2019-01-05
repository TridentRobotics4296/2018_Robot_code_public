package org.wildstang.hardware.beaglebone;

import org.bulldog.core.platform.Board;
import org.bulldog.core.platform.Platform;

public class BBBManager
{
   private static BBBManager s_instance;

   private Board m_board;

   static
   {
      s_instance = new BBBManager();
      s_instance.init();
   }

   private BBBManager()
   {

   }

   private void init()
   {
      // Create the Board instance and any other initialisation of the BBB
      m_board = Platform.createBoard();
   }

   public static BBBManager getInstance()
   {
      return s_instance;
   }

   public Board getBoard()
   {
      return m_board;
   }
}
