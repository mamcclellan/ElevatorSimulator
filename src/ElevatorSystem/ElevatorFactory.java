/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem;

import ElevatorSystem.util.InvalidParameterException;

/**
 *
 * @author Michael
 */
public class ElevatorFactory {
    
    public static Elevator createElevator(int elevatorNo, int defaultFloorNo, int riderCapacity, long idle, long betweenFloors, 
         long doorsOpenAndClose, int numFloors) throws InvalidParameterException
    {
        return new ElevatorImpl(elevatorNo, defaultFloorNo, riderCapacity, idle, betweenFloors, doorsOpenAndClose, numFloors);
    }
}
