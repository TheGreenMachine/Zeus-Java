package com.edinarobotics.zeus;

import com.edinarobotics.utils.gamepad.FilterSet;
import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.gamepad.filters.DeadzoneFilter;
import com.edinarobotics.utils.gamepad.filters.QuarticScalingFilter;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Zeus extends SimpleRobot {
    public double rightSpeed;
    public double leftSpeed;
    public boolean runDumper;
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
        runDumper = false;
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
         runDumper = gamepad.getRawButton(Gamepad.RIGHT_BUMPER);
         
         //Handle Conveyor and Collector
         runConveyor = gamepad.getRawButton(Gamepad.LEFT_BUMPER);
         
         mechanismSet();
         Timer.delay(0.005);
       }
    }
    
    public void mechanismSet(){
        Components robotParts = Components.getInstance();
        
        //Handle Driving
        robotParts.leftDrive.set(leftSpeed*-1);
        robotParts.rightDrive.set(rightSpeed);
        
        //Handle Dumper
            robotParts.dumper.set(
		    runDumper? .05 : 0
		    );
        
        //Handle Conveyor
        robotParts.conveyor.set(
                runConveyor? Relay.Value.kReverse : Relay.Value.kOff
                );
        robotParts.collector.set(
               runConveyor? Relay.Value.kForward : Relay.Value.kOff
                );  
    }
    
}
