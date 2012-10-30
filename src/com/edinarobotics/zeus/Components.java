package com.edinarobotics.zeus;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author danny
 */
public class Components {

    //Ports
    private final static int LEFT_DRIVE_PORT = 1;
    private final static int RIGHT_DRIVE_PORT = 2;
    private final static int DUMPER_PORT = 3;
    private final static int CONVEYOR_PORT = 4;
    private final static int COLLECTOR_PORT = 5;
    //Components
    public Jaguar rightDrive;
    public Jaguar leftDrive;
    public Jaguar dumper;
    public Victor collector;
    public Victor conveyor;
    
    private static Components componentsInstance;
    
    private Components(){
     rightDrive = new Jaguar(RIGHT_DRIVE_PORT);
     leftDrive = new Jaguar(LEFT_DRIVE_PORT);
     dumper = new Jaguar(DUMPER_PORT);
     collector = new Victor(COLLECTOR_PORT);
     conveyor = new Victor(CONVEYOR_PORT);
    }
    
    public static Components getInstance(){
        if(componentsInstance == null)
            componentsInstance = new Components();
        return componentsInstance;
    }
}
