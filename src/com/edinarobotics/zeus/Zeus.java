package com.edinarobotics.zeus;

import com.edinarobotics.utils.gamepad.FilterSet;
import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.gamepad.filters.DeadzoneFilter;
import com.edinarobotics.utils.gamepad.filters.QuarticScalingFilter;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Zeus extends SimpleRobot {
    public double rightSpeed;
    public double leftSpeed;
    public int dumperDirection;
    public boolean runConveyor;
    
    public void robotInit(){
        Components.getInstance();
    }
    
    protected void disabled(){
         stop();
         getWatchdog().setEnabled(false); //Disable the watchdog when disabled.
     }
    
    public void stop(){
        leftSpeed = 0;
        rightSpeed = 0;
        runConveyor = false;
        dumperDirection = 0;
        mechanismSet();
    }
    
    public void autonomous() {
      //No Autonomous    
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
       Gamepad gamepad = new Gamepad(1);
       FilterSet gamepadFilters = new FilterSet();
       gamepadFilters.addFilter(new DeadzoneFilter(.5));
       gamepadFilters.addFilter(new QuarticScalingFilter());
       
       
       while(isEnabled()){
         
         //Handle Driving  
         rightSpeed = gamepadFilters.filter(gamepad.getJoysticks()).getRightY();
         leftSpeed = gamepadFilters.filter(gamepad.getJoysticks()).getLeftY();
         
         //Handle Dumping
         if(gamepad.getRawButton(Gamepad.RIGHT_BUMPER)){
             dumperDirection = 1;
         }
         else if(gamepad.getRawButton(Gamepad.RIGHT_TRIGGER)){
             dumperDirection = -1;
         }
         else{
             dumperDirection = 0;
         }
         
         //Handle Conveyor and Collector
         runConveyor = gamepad.getRawButton(Gamepad.LEFT_BUMPER);
         
         mechanismSet();
         Timer.delay(0.005);
       }
    }
    
    public void mechanismSet(){
        Components robotParts = Components.getInstance();
        
        //Handle Driving
        robotParts.leftDrive.set(leftSpeed);
        robotParts.rightDrive.set(rightSpeed);
        
        //Handle Dumper
        if(dumperDirection == 0){
            robotParts.dumper.set(0);
        }
        else{
            robotParts.dumper.set(
                    dumperDirection==1 ? 30 : -70
                    );
        }
        
        //Handle Conveyor
        robotParts.conveyor.set(
                runConveyor? 50 : 0
                );
        robotParts.collector.set(
                runConveyor? 50 : 0
                );  
    }
    
}
