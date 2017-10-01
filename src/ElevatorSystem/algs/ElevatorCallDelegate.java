/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem.algs;

import ElevatorSystem.*;
import ElevatorSystem.util.*;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class ElevatorCallDelegate {
    
    private volatile static ElevatorCallDelegate instance;
    
    private ElevatorCallDelegate()
    {
        
    }
    
    public static ElevatorCallDelegate getInstance() 
    {
        if (instance == null)
        {
            synchronized (ElevatorCallDelegate.class)
            {
                if (instance == null) instance = new ElevatorCallDelegate();
            }
        }
        return instance;
    }
    
    public synchronized void processRequest(ArrayList<Elevator> elevatorList, int floor, String direction, boolean pending) throws InvalidRequestException, InvalidParameterException
    {
        if (floor > Building.getInstance().getNumFloors() || floor < 1) throw new InvalidRequestException("ElevatorCallDelegate received invalid floor");
        if (!direction.equals("Up") && !direction.equals("Down")) throw new InvalidRequestException("ElevatorCallDelegate: Direction for floor request must be 'Up' or 'Down'");
        boolean elevatorFound = false;
        for (Elevator e: elevatorList)
        {
            if (isValidElevator(e, floor, direction)) 
            {
                System.out.println(Time.getCurrentTime() + "Floor " + floor + " " + direction + " Request Accepted - Elevator " + e.getElevatorNumber() + " will handle it");
                e.addFloorRequest(floor, direction); 
                elevatorFound = true; 
                break;
            }
        }
        if (!elevatorFound)
        {
            if (!pending) System.out.println(Time.getCurrentTime() + "All elevators busy! Requests pending: " + (ElevatorController.getInstance().getNumPendingRequests()+1));
            ElevatorController.getInstance().addPendingRequest(new Request(floor, direction));
        }
        
    }
    
    
    private synchronized boolean isValidElevator(Elevator e, int floor, String direction) throws InvalidParameterException
    {
        boolean result = false;
        if ((e.getDirection().equals("Up") && e.getCurrentFloorNumber() < floor) || (e.getDirection().equals("Down") && e.getCurrentFloorNumber() > floor) || e.getDirection().equals("Idle"))
        {
            if (e.getDirection().equals(direction) || e.getDirection().equals("Idle")) result = true;
        }
        return result;
        
    }
    
    
}
