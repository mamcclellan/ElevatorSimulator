/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem;

import ElevatorSystem.util.InvalidParameterException;
import ElevatorSystem.util.InvalidRequestException;
import ElevatorSystem.util.Request;
import ElevatorSystem.util.Time;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class Floor
{
    
    private int floorNumber;
    private boolean upPressed;
    private boolean downPressed;
    private ArrayList<Traveler> travelerList;
    private ArrayList<Traveler> finishedList;
    private ArrayList<Long> waitTimesList;
    
    public Floor(int floorNo) throws InvalidParameterException
    {
        setFloorNumber(floorNo);
        upPressed = false;
        downPressed = false;
        travelerList = new ArrayList<>(0);
        finishedList = new ArrayList<>(0);
        waitTimesList = new ArrayList<>(0);
        addToBuilding();
        addToElevatorController();
    }
    
    public int getFloorNumber()
    {
        int temp = floorNumber;
        return temp;
    }
    
    public boolean getUpPressed()
    {
        if (upPressed) return true;
        return false;
    }
    
    public boolean getDownPressed()
    {
        if (downPressed) return true;
        return false;
    }
    
    public int verifyFinishedList()
    {
        int numPeople = 0;
        for (Traveler t: finishedList)
        {
            if (t.getDestination() == floorNumber) numPeople++;
        }
        return numPeople;
    }
    
    public long[] getFinishedWaitTimes()
    {
        long[] toReturn = new long[waitTimesList.size()];
        for (int i = 0; i < waitTimesList.size(); i++) toReturn[i] = waitTimesList.get(i);
        return toReturn;
    }
    
    public ArrayList<Traveler> getFinishedTravellerList()
    {
        ArrayList<Traveler> temp = finishedList;
        return temp;
    }
    
    
    public synchronized void addTraveler(Traveler t) throws InvalidParameterException, InvalidRequestException
    {
        if (t == null) throw new InvalidParameterException("Valid Traveler not recieved");
        if (t.getDestination() == this.floorNumber) 
        {
            finishedList.add(t); 
            t.endRideTime();
            System.out.println(Time.getCurrentTime() + "Person " + t.getId() + " entered Floor " + floorNumber + printTravellerList());
            System.out.println(Time.getCurrentTime() + "Person " + t.getId() + " has arrived at destination floor " + floorNumber);
            return;
        }
        travelerList.add(t);
        if (t.getDestination() > this.floorNumber) {upPressed = true; ElevatorController.getInstance().processFloorRequest(this.floorNumber,"Up");}
        if (t.getDestination() < this.floorNumber) {downPressed = true;ElevatorController.getInstance().processFloorRequest(this.floorNumber,"Down");}
    }
    
    public void addWaitTime(long waitTime)
    {
        waitTimesList.add(waitTime);
    }
    
    
    public synchronized void exchangeTravelers(Elevator e)
    {
        boolean riderCantExchange = false;
        ArrayList<Traveler> toRemove = new ArrayList<>();
        for (Traveler t: travelerList)
        {
            if (t.getDestination() > floorNumber && "Up".equals(e.getDirection()))
            {
                upPressed = false;
                if (e.isFull()) {riderCantExchange = true; upPressed = true; break;}
                {
                    e.addRider(t);
                    t.endWaitTime();
                    this.addWaitTime(t.getWaitTime());
                    toRemove.add(t);
                }
            }
            if (t.getDestination() < floorNumber && "Down".equals(e.getDirection()))
            {
                downPressed = false;
                if (e.isFull()) {riderCantExchange = true; downPressed = true; break;}
                {
                    e.addRider(t);
                    t.endWaitTime();
                    this.addWaitTime(t.getWaitTime());
                    toRemove.add(t);
                }
            }
        }
        //Handle floor requests going in opposite direction
        if (toRemove.isEmpty())
        {
            while (!travelerList.isEmpty()) 
            {
                Traveler newRider = travelerList.remove(0);
                e.addRider(newRider);
                newRider.endWaitTime();
                this.addWaitTime(newRider.getWaitTime());
                toRemove.add(newRider);
            }
        }
        for (Traveler t: toRemove) 
        {
            travelerList.remove(t);
            System.out.println(Time.getCurrentTime() + "Person " + t.getId() + " has left Floor " + floorNumber + printTravellerList());
        }
        if (riderCantExchange)
        {
            try
            {
                if (upPressed) 
                {
                    System.out.println(Time.getCurrentTime() + "Elevator " + e.getElevatorNumber() + " is full! Sending another UP request from Floor " + floorNumber);
                    ElevatorController.getInstance().processFloorRequest(floorNumber, "Up");
                }
                if (downPressed) 
                {
                    System.out.println(Time.getCurrentTime() + "Elevator " + e.getElevatorNumber() + " is full! Sending another DOWN request from Floor " + floorNumber);
                    ElevatorController.getInstance().processFloorRequest(floorNumber, "Down");
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        
        // Remove Unnecessary pending requests
        if (travelerList.isEmpty()) {
            ElevatorController.getInstance().removeOutdatedPendingRequest(this);
        }
       
    }
    
    private void setFloorNumber(int floorNo) throws InvalidParameterException
    {
        if (floorNo <= 0) throw new InvalidParameterException("Floor number must be greater than zero");
        floorNumber = floorNo;
    }
    
    private String printTravellerList()
    {
        String travellers = "";
        for (Traveler t: travelerList)
        {
            travellers += "T" + t.getId() + ", ";
        }
        travellers = " [Waiting travellers: " + travellers + "]";
        String finished = "";
        for (Traveler t: finishedList)
        {
            finished += "T" + t.getId() + ", ";
        }
        finished = "[Finished travellers: " + finished + "]";
        return travellers + finished;
    }
    
    
    private void addToBuilding()
    {
        Building.getInstance().addFloor(this);
    }
    
    private void addToElevatorController()
    {
        ElevatorController.getInstance().addFloor(this);
    }
            
}
