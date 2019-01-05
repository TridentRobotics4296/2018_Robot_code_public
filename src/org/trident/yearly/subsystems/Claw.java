package org.trident.yearly.subsystems;

import org.wildstang.framework.core.Core;
import org.wildstang.framework.io.Input;
import org.wildstang.framework.io.inputs.AnalogInput;
import org.wildstang.framework.io.inputs.DigitalInput;
import org.wildstang.framework.subsystems.Subsystem;
import org.wildstang.hardware.crio.outputs.WsSolenoid;
import org.trident.yearly.robot.CANConstants;
import org.trident.yearly.robot.WSInputs;
import org.trident.yearly.robot.WSOutputs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Claw implements Subsystem
{

   public static final boolean CLAW_OPEN = true;
   public static final double IN_SPEED = 1.0;
   public static final double OUT_SPEED = -0.5;
   
   // If the forward direction is out instead of in, set this to true
   public static final boolean INVERTED = true;
   
   // add variables here
   private AnalogInput m_clawInButton;
   private AnalogInput m_clawOutButton;
   private DigitalInput m_clawArmButton;
   private DigitalInput m_cubeSwitch;
   
   private VictorSPX m_leftIntakeMotor;
   private VictorSPX m_rightIntakeMotor;
   private WsSolenoid m_clawArms;
   

   // State that needs to be reset
   private boolean m_clawOpen = false;
   private boolean m_clawArmCurrent = false;
   private boolean m_clawArmPrev = false;
   private boolean m_hasCube = false;
   
   private double m_motorSpeed = 0;

   
   @Override
   public void resetState()
   {
      // Default to off
      m_motorSpeed = 0;
      
      m_clawArmCurrent = false;
      m_clawArmPrev = false;
      
      // Default to closed
      m_clawOpen = !CLAW_OPEN;
   }

   @Override
   public void selfTest()
   {
   }

   @Override
   public String getName()
   {
      return "Claw";
   }

   @Override
   public void init()
   {
      // Setup any local variables with intial values
      resetState();

      m_leftIntakeMotor = new VictorSPX(CANConstants.LEFT_INTAKE_ID);
      m_rightIntakeMotor = new VictorSPX(CANConstants.RIGHT_INTAKE_ID);
      
      m_leftIntakeMotor.setInverted(INVERTED);
      m_rightIntakeMotor.setInverted(INVERTED);
      
      m_leftIntakeMotor.setNeutralMode(NeutralMode.Brake);
      m_rightIntakeMotor.setNeutralMode(NeutralMode.Brake);
      
      m_clawArms = (WsSolenoid)Core.getOutputManager().getOutput(WSOutputs.CLAW.getName());

      m_clawInButton = (AnalogInput) Core.getInputManager().getInput(WSInputs.CLAW_IN.getName());
      m_clawInButton.addInputListener(this);

      m_clawOutButton = (AnalogInput) Core.getInputManager().getInput(WSInputs.CLAW_OUT.getName());
      m_clawOutButton.addInputListener(this);

      m_cubeSwitch = (DigitalInput) Core.getInputManager().getInput(WSInputs.CUBE_DETECT.getName());
      m_cubeSwitch.addInputListener(this);

      m_clawArmButton = (DigitalInput) Core.getInputManager().getInput(WSInputs.CLAW_ARMS.getName());
      m_clawArmButton.addInputListener(this);
   }
   
   
   @Override
   public void inputUpdate(Input p_source)
   {
    
      // Intake motor buttons are momentary
      // Out is first to take precendence, in case both are pressed
      if (p_source == m_clawOutButton)
      {
         if (Math.abs(m_clawOutButton.getValue()) > 0)
         {
            clawOut();
         }
         else
         {
            clawOff();
         }
      }
      else if (p_source == m_clawInButton)
      {
         if (Math.abs(m_clawInButton.getValue()) > 0)
         {
            clawIn();
         }
         else
         {
            clawOff();
         }
      }

      // Claw arm open/close is a toggle
      else if (p_source == m_clawArmButton)
      {
         m_clawArmCurrent = m_clawArmButton.getValue();

         if (m_clawArmCurrent && !m_clawArmPrev)
         {
            m_clawOpen = !m_clawOpen;
         }
         m_clawArmPrev = m_clawArmCurrent;
      }
      
      else if (p_source == m_cubeSwitch)
      {
         m_hasCube = m_cubeSwitch.getValue();
      }
   }

   @Override
   public void update()
   {
      // If we have a cube, do not allow the motors to run in
      if (hasCube() && m_motorSpeed > 0)
      {
         m_motorSpeed = 0;
      }
      
      m_leftIntakeMotor.set(ControlMode.PercentOutput, -m_motorSpeed);
      m_rightIntakeMotor.set(ControlMode.PercentOutput, m_motorSpeed);
      
      m_clawArms.setValue(m_clawOpen);

      SmartDashboard.putBoolean("Have cube", hasCube());
      SmartDashboard.putBoolean("Arms open", m_clawOpen);
   }

   public void clawIn()
   {
      m_motorSpeed = IN_SPEED;
   }
   
   public void clawOut()
   {
      m_motorSpeed = OUT_SPEED;
   }
   
   public void clawOff()
   {
      m_motorSpeed = 0;
   }
   
   public boolean hasCube()
   {
      return m_hasCube;
   }
   
   public void openClaw()
   {
      m_clawOpen = CLAW_OPEN;
   }
   
   public void closeClaw()
   {
      m_clawOpen = !CLAW_OPEN;
   }
   

}
