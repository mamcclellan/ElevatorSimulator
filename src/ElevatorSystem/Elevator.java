/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem;

import ElevatorSystem.util.InvalidParameterException;
import ElevatorSystem.util.InvalidRequestException;

/**
 *
 * @author Michael
 */
public interface Elevator
{
    
    void addFloorRequest(int request, String direction) throws InvalidRequestException;
    
    void addRiderRequest(int request)throws InvalidRequestException;
    
    void addRider(Traveler rider);
    
    boolean isDoorOpen();
    
    boolean hasRiderRequest(int floor) throws InvalidParameterException;
    
    boolean hasFloorRequest(int floor) throws InvalidParameterException;
    
    boolean isFull();
    
    int getElevatorNumber();
    
    int getCurrentFloorNumber();
    
    int getDefaultFloorNumber();
    
    int getElevatorCapacity();
    
    String getDirection();
    
    void setDefaultFloor(int defaultFloorNo) throws InvalidParameterException;
    
    
}
