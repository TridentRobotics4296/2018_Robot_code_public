package org.wildstang.framework.subsystems;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.timer.StopWatch;

public class WsMJPEGstreamer implements Subsystem, Runnable
{

   public static int FPS = 1000 / 10;
   public final int PORT = 8887;

   private String m_name = "";

   public BlockingQueue<SocketWorker> workers = new LinkedBlockingQueue<>();
   private ServerSocket serverSocket;
   private boolean running = false;
   private Thread t = null;
   public StopWatch sw = null;

   @Override
   public void inputUpdate(Input source)
   {
      // TODO Auto-generated method stub

   }

   @Override
   public void init()
   {
      m_name = "WsMJPEGstreamer";
      sw = new StopWatch();
      if (!running)
      {
         running = true;
         t = new Thread(this);
         t.start();
      }

   }

   
   
   @Override
   public void resetState()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void selfTest()
   {
      // TODO Auto-generated method stub

   }

   @Override
   public void update()
   {
      // TODO Auto-generated method stub

   }

   public void send(Mat img)
   {
      // Convert from mat to byte stream
      if (img != null && !img.empty())
      {
         MatOfByte bytemat = new MatOfByte();
         Highgui.imencode(".jpg", img, bytemat);
         byte[] bytes = bytemat.toArray();
         for (SocketWorker w : workers)
         {
            w.send(bytes);
         }
      }
   }

   @Override
   public String getName()
   {
      // TODO Auto-generated method stub
      return m_name;
   }

   public String getPort()
   {
      return Integer.toString(this.PORT);
   }

   @Override
   public void run()
   {
      ExecutorService exeSvc = Executors.newFixedThreadPool(20);
      try
      {

         serverSocket = new ServerSocket(PORT, 10);
         int counter = 0;
         while (true)
         {
            // wait for client connection
            System.out.println("Waiting for connection");
            serverSocket.setPerformancePreferences(1, 2, 1);
            Socket connection = serverSocket.accept();
            connection.setTcpNoDelay(true);
            System.out.println("Connection received from "
                  + connection.getInetAddress().getHostName());

            SocketWorker sw = new SocketWorker(counter++, connection, this);
            exeSvc.execute(sw);

            workers.put(sw);
         }
      }
      catch (IOException ioException)
      {
         ioException.printStackTrace();
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
      finally
      {
         try
         {
            exeSvc.shutdown();
            serverSocket.close();
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
      }

   }

}
