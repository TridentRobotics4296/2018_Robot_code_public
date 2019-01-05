package org.wildstang.hardware.opencv.inputs;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.wildstang.framework.io.inputs.ImageInput;
import org.wildstang.framework.timer.StopWatch;
//import org.opencv.highgui.Highgui;
//import org.opencv.highgui.VideoCapture;
//import org.opencv.core.Mat;
//import org.opencv.highgui.Highgui;
//import org.opencv.highgui.VideoCapture;

public class OpenCVInput extends ImageInput
{

   private static Logger s_log = Logger.getLogger(OpenCVInput.class.getName());
   private static final String s_className = "OpenCVInput";

   private VideoCapture camera;
   private double width = 320;
   private double height = 240;
   // Mat imgInput;
   StopWatch timer;
   boolean debug = false;
   Mat imgInput;
   Mat imgClone;

   public OpenCVInput(String p_name)
   {
      super(p_name);
      timer = new StopWatch();
      camera = new VideoCapture(0);
      camera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, width);
      camera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, height);

      width = camera.get(Highgui.CV_CAP_PROP_FRAME_WIDTH);
      height = camera.get(Highgui.CV_CAP_PROP_FRAME_HEIGHT);
      imgInput = new Mat();
      imgClone = new Mat();

   }

   @Override
   public synchronized Mat getValue()
   {
      imgClone = imgInput.clone();
      return imgClone;
   }

   @Override
   public synchronized Mat readRawValue()
   {
      // TODO Auto-generated method stub
      if (s_log.isLoggable(Level.FINER)) s_log.entering(s_className, "readRawValue");
      if (!debug)
      {
         // timer.Reset();
         // timer.Start();
         camera.read(imgInput);
         // timer.Stop();
         // System.out.println("Get Image took " + timer.GetTimeInSec()
         // + "Seconds");
         // debug = true;
      }

      if (s_log.isLoggable(Level.FINER)) s_log.exiting(s_className, "readRawValue");
      return imgInput;
   }

}
