/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem;

import ElevatorSystem.util.InvalidParameterException;
import ElevatorSystem.util.Time;
/**
 *
 * @author Michael
 */
public class PersonImpl implements Traveler {
    
    private int personId;
    private int startFloor;
    private int personDestination;
    private long start;
    private long waitTime;
    private long rideTime;
    
    public PersonImpl(int id, int startf, int destination) throws InvalidParameterException
    {
        if (startf == destination) throw new InvalidParameterException("Person created is already at destination! (start floor == destination");
        setPersonId(id);
        setStartFloor(startf);
        setPersonDestination(destination);
        
        start = System.currentTimeMillis();
        if (startFloor > personDestination)
        {
            System.out.println(Time.getCurrentTime() + "Person " + personId + " created on Floor " + startFloor + ", wants to go DOWN to Floor " + personDestination);
            System.out.println(Time.getCurrentTime() + "Person " + personId + " presses DOWN button on Floor " + startFloor);
        }
        else
        {
            System.out.println(Time.getCurrentTime() + "Person " + personId + " created on Floor " + startFloor + ", wants to go UP to Floor " + personDestination);
            System.out.println(Time.getCurrentTime() + "Person " + personId + " presses UP button on Floor " + startFloor);
        }
    }
    
    @Override
    public int getId()
    {
        int temp = personId;
        return temp;
    }
    
    @Override
    public int getStartFloor()
    {
        int temp = startFloor;
        return temp;
    }
    
    @Override
    public int getDestination()
    {
        int temp = personDestination;
        return temp;
    }
    
    @Override
    public long getWaitTime()
    {
        long temp = waitTime;
        return temp;
    }
    
     @Override
    public long getRideTime()
    {
        long temp = rideTime;
        return temp;
    }
    
    @Override
    public void endWaitTime()
    {
        waitTime = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
    }
    
    @Override
    public void endRideTime()
    {
        rideTime = System.currentTimeMillis() - start;
    }
    
    private  void setPersonId(int id) throws InvalidParameterException
    {
        if (id < 0) throw new InvalidParameterException("How did ID become negative?");
        personId = id;
    }
    
    private void setStartFloor(int start) throws InvalidParameterException
    {
        if (start > Building.getInstance().getNumFloors() || start < 1) throw new InvalidParameterException("Person start floor not valid");
        startFloor = start;
    }
    private void setPersonDestination(int destination) throws InvalidParameterException
    {
        if (destination > Building.getInstance().getNumFloors() || destination < 1) throw new InvalidParameterException("Person destination not valid");
        personDestination = destination;
    }
    
}
