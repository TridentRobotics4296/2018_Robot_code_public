/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.framework.auto.steps;

/**
 *
 * 
 */
public abstract class AutoStep
{

   private boolean finished;
   public int timeout;
   public long startTime;
   public long currentTime;

   public AutoStep()
   {
      // initialize variables
      finished = false; // A step can't finish before it starts.
      timeout = -1; // If timeout < 0, then there is no timeout set
      startTime = -1; //Set time to -1 to that we know that the startTime has not been set
   }
   
   public abstract void initialize(); // This method is called once, when the
   // step is first run. Use this method to
   // set up anything that is necessary for
   // the step.

   //public abstract void update(); // This method is called on the active step,
   // once per call to
   // RobotTemplate.autonomousPeriodic().
   // Steps will continue to have this method called until they set finished to
   // true.
   // Note: this method is first called right after initialize(), with no delay
   // in between.s
   
   public void update()
   {
      //If a timeout was not set, exit the method
      if(timeout <= 0)
         return;
         
      //If a startTime was not set, set it
      if(startTime < 0)
         startTime = System.currentTimeMillis();
      
      currentTime = System.currentTimeMillis();
      
      //If the timeout has passed, finish the step
      if(currentTime - startTime >= timeout)
         setFinished(true);
   }
   
   public boolean isFinished()
   {
      return finished;
   }
   
   public void setFinished(boolean isFinished)
   {
      finished = isFinished;
   }

   public void setTimeout(int timeout) // Timeout is in milliseconds
   {
      this.timeout = timeout;
   }
   
   public abstract String toString(); // Please use future tense (NOT present
   // tense!) when naming steps.
}
