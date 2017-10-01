/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem;

/**
 *
 * @author Michael
 */
public interface Traveler {
    
    int getId();
    
    int getStartFloor();
    
    int getDestination();
    
    long getWaitTime();
    
    long getRideTime();
    
    void endWaitTime();
    
    void endRideTime();
    
}
