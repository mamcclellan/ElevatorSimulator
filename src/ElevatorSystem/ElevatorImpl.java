/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem;

import ElevatorSystem.util.*;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class ElevatorImpl implements Elevator, Runnable {
    
    private int elevatorNumber;
    private int currentFloorNumber;
    private String direction;
    private boolean[] floorRequestList;
    private boolean[] riderRequestList;
    private ArrayList<Traveler> riderList;
    private int defaultFloor;
    private int riderCapacity;
    private boolean doorOpen;
    private long idleTime;
    private long timeBetweenFloors;
    private long timeDoorsOpenAndClose;
    private boolean isRunning;
    private String neutralDirection;
    
    public ElevatorImpl
        (int elevatorNo, int defaultFloorNo, int capacity, long idle, long betweenFloors, 
         long doorsOpenAndClose, int numFloors) throws InvalidParameterException
    {
        floorRequestList = new boolean[numFloors];
        for (int i=0; i < numFloors; i++) floorRequestList[i] = false;
        riderRequestList = new boolean[numFloors];
        for (int j=0; j < numFloors; j++) floorRequestList[j] = false;
        riderList = new ArrayList<>();
        
        setElevatorNumber(elevatorNo);
        setDefaultFloor(defaultFloorNo);
        setRiderCapacity(capacity);
        setIdleTime(idle);
        setTimeBetweenFloors(betweenFloors);
        setTimeDoorsOpenAndClose(doorsOpenAndClose);
        
        doorOpen = false;
        currentFloorNumber = defaultFloor - 1;
        isRunning = true;
        direction = "Idle";
        
    }
        
    public void run()
    {
        
        Building.getInstance().addElevator(this);
        ElevatorController.getInstance().addElevator(this);
        
        while (isRunning)
        {
            while (direction.equals("Idle"))
            {
                try
                {
                    
                    synchronized (this)
                    {
                        this.wait(idleTime);
                    }
                        
                    
                    if (!direction.equals("Idle")) break;
                    
                    try
                    {
                        if (currentFloorNumber != defaultFloor - 1) 
                        {
                            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " times out - returning to default floor " + getDefaultFloorNumber());
                            synchronized (this)
                            {
                            this.addRiderRequest(defaultFloor);
                            }
                        }
                        
                    }
                    catch (InvalidRequestException e)
                    {
                        isRunning = false;
                        break;
                    }
                }
                catch (InterruptedException e)
                {
                    isRunning = false;
                }
            }
            
            try
            {
                changeFloor();
            }
            catch (InterruptedException e)
            {
                isRunning = false;
            }
            
            
        }
        
    }
    
    public void stop()
    {
        isRunning = false;
    }
    
    public void addRider(Traveler t)
    {
        riderList.add(t);
        System.out.println(Time.getCurrentTime() + "Person " + t.getId() + " entered Elevator " + elevatorNumber + " " + printRiderList());
    }
    
    @Override
    public void addFloorRequest(int request, String d) throws InvalidRequestException
    {
        request = request - 1;
        if (request >= floorRequestList.length || request < 0) throw new InvalidRequestException("Invalid Floor given to addFloorRequest");
        if (!d.equals("Up") && !d.equals("Down")) throw new InvalidRequestException("Invalid direction given to addFloorRequest");
        try
        {
             if (request == currentFloorNumber && !direction.equals("Idle")) {doorsOpenAndClose();}
        }
        catch (InterruptedException e)
        {
            isRunning = false;
        }
        
        if (direction.equals("Idle"))
        {
            floorRequestList[request] = true;
            if (request == currentFloorNumber && d.equals("Down"))
            {
                synchronized (this)
                {
                    direction = "Down";
                    try
                    {
                       doorsOpenAndClose();
                    }
                    catch (InterruptedException e) {e.printStackTrace();}
                    this.notify();
                }
                return;
            }
            if (request == currentFloorNumber && d.equals("Up"))
            {
                synchronized (this)
                {
                    direction = "Up";
                    try
                    {
                       doorsOpenAndClose();
                    }
                    catch (InterruptedException e) {e.printStackTrace();}
                    this.notify();
                }
                return;
            }
            //Handle cases where floor request direction is the opposite of the direction elevator needs to go to get to that floor
            if (request > currentFloorNumber && d.equals("Down"))
            {
                synchronized (this)
                {
                    direction = "Neutral"; //temporary direction that's neither Idle nor Up nor Down; Elevator focuses on getting to requested floor
                    System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " going UP to Floor " + (request + 1) + " for DOWN request" + printFloorRequestNumbers() + printRiderRequestNumbers());
                    this.notify();
                }
                return;
            }
            if (request < currentFloorNumber && d.equals("Up"))
            {
                synchronized (this)
                {
                    direction = "Neutral"; //temporary direction that's neither Idle nor Up nor Down; Elevator focuses on getting to requested floor
                    System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " going DOWN to Floor " + (request + 1) + " for UP request" + printFloorRequestNumbers() + printRiderRequestNumbers());
                    this.notify();
                }
                return;
            }
            if (request > currentFloorNumber || currentFloorNumber == 0) 
            {
                synchronized (this)
                {
                    direction = "Up";
                    System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " going UP to Floor " + (request + 1) + " for UP request" + printFloorRequestNumbers() + printRiderRequestNumbers());
                    this.notify();
                }
                return;
                
            }
            if  (request < currentFloorNumber || currentFloorNumber == Building.getInstance().getNumFloors() - 1)
            {
                synchronized (this)
                {
                    direction = "Down";
                    System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " going DOWN to Floor " + (request + 1) + " for DOWN request" + printFloorRequestNumbers() + printRiderRequestNumbers());
                    this.notify();
                }
                return;
            }
        }
        
        if (direction.equals("Up") && (request > currentFloorNumber))
        {
            if (d.equals("Up"))
            {
            floorRequestList[request] = true;
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " going UP to Floor " + (request + 1) + " for UP request" + printFloorRequestNumbers() + printRiderRequestNumbers());
            return;
            }
            if (d.equals("Down"))
            {
            floorRequestList[request] = true;
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " going UP to Floor " + (request + 1) + " for DOWN request" + printFloorRequestNumbers() + printRiderRequestNumbers());
            return;
            }
        }
        
        if (direction.equals("Down") && (request < currentFloorNumber))
        {
            if (d.equals("Down"))
            {
            floorRequestList[request] = true;
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " going DOWN to Floor " + (request + 1) + " for DOWN request" + printFloorRequestNumbers() + printRiderRequestNumbers());
            return;
            }
            if (d.equals("Up"))
            {
            floorRequestList[request] = true;
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " going DOWN to Floor " + (request + 1) + " for UP request" + printFloorRequestNumbers() + printRiderRequestNumbers());
            return;
            }
        }
    }
    
    @Override
    public void addRiderRequest(int request) throws InvalidRequestException
    {
        request = request - 1;
        if (request > riderRequestList.length || request < 0) throw new InvalidRequestException("Floor doesn't exist...");
        try
        {
             if (request == currentFloorNumber) {doorsOpenAndClose(); return;}
        }
        catch (InterruptedException e)
        {
            isRunning = false;
        }
        
        if (direction.equals("Idle"))
        {
            riderRequestList[request] = true;
            if (request > currentFloorNumber) 
            {
                synchronized (this)
                {
                    direction = "Up";
                    System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " Rider Request made for Floor " + (request + 1) + " " + printFloorRequestNumbers() + printRiderRequestNumbers());
                    this.notify();
                }
                return;
            }
            else 
            {
                synchronized (this)
                {
                    direction = "Down";
                    System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " Rider Request made for Floor " + (request + 1) + " " + printFloorRequestNumbers() + printRiderRequestNumbers());
                    this.notify();
                }
                return;
            }
        }
        if (direction.equals("Up") && (request > currentFloorNumber))
        {
            riderRequestList[request] = true;
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " Rider Request made for Floor " + (request + 1) + " " + printFloorRequestNumbers() + printRiderRequestNumbers());
            return;
        }
        if (direction.equals("Down") && (request < currentFloorNumber))
        {
            riderRequestList[request] = true;
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " Rider Request made for Floor " + (request + 1) + " " + printFloorRequestNumbers() + printRiderRequestNumbers());
            return;
        }
        System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " Rider Request made for Floor " + (request + 1) + " - WRONG DIRECTION - Ignoring Request");
    }
    
    @Override
    public boolean isFull()
    {
        return (riderList.size() >= riderCapacity);
    }
    
    @Override
    public int getElevatorNumber()
    {
        int tempNo = elevatorNumber;
        return tempNo;
    }
    
    @Override
    public int getCurrentFloorNumber()
    {
        int tempNo = currentFloorNumber + 1;
        return tempNo;
    }
    
    @Override
    public String getDirection()
    {
        if ("Up".equals(direction)) return "Up";
        if ("Down".equals(direction)) return "Down";
        if ("Neutral".equals(direction)) return "Neutral";
        return "Idle";
    }
    
    @Override
    public int getDefaultFloorNumber()
    {
        int temp = defaultFloor;
        return temp;
    }
    
    @Override
    public int getElevatorCapacity()
    {
        int temp = riderCapacity;
        return temp;
    }
    
    @Override
    public boolean isDoorOpen()
    {
        if (doorOpen) return true;
        return false;
    }
    
    @Override
    public boolean hasRiderRequest(int floor) throws InvalidParameterException
    {
        if (floor < 1 || floor > floorRequestList.length) throw new InvalidParameterException("Of course the elevator doesn't have that floor!");
        return riderRequestList[floor-1];
    }
    
    @Override
    public boolean hasFloorRequest(int floor) throws InvalidParameterException
    {
        if (floor < 1 || floor > floorRequestList.length) throw new InvalidParameterException("Of course the elevator doesn't have that floor!");
        return floorRequestList[floor-1];
    }
    
    @Override
    public void setDefaultFloor(int defaultFloorNo) throws InvalidParameterException
    {
        if (defaultFloorNo <= 0) throw new InvalidParameterException("Default Floor number must be greater than zero");
        if (defaultFloorNo > floorRequestList.length) throw new InvalidParameterException("Default Floor can't be greater than highest floor");
        defaultFloor = defaultFloorNo;
    }
    
    private void changeFloor() throws InterruptedException
    {
        if (floorRequestList[currentFloorNumber] || riderRequestList[currentFloorNumber]) doorsOpenAndClose();
        if (direction.equals("Up")) 
        {
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " moving from Floor " + getCurrentFloorNumber() + " to Floor " + (getCurrentFloorNumber() + 1) + printFloorRequestNumbers() + printRiderRequestNumbers());
            Thread.sleep(timeBetweenFloors);
            currentFloorNumber++;
        }
        if (direction.equals("Down"))
        {
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " moving from Floor " + getCurrentFloorNumber() + " to Floor " + (getCurrentFloorNumber() - 1) + printFloorRequestNumbers() + printRiderRequestNumbers());
            Thread.sleep(timeBetweenFloors);
            currentFloorNumber--;
        }
        
        int x = 0;
        if (direction.equals("Neutral"))
        {
            for (int i=0; i<floorRequestList.length; i++)
            {
                if (floorRequestList[i]) {x = i; break;}
            }
            
            if (x > currentFloorNumber)
            {
                System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " moving from Floor " + getCurrentFloorNumber() + " to Floor " + (getCurrentFloorNumber() + 1) + printFloorRequestNumbers() + printRiderRequestNumbers());
                Thread.sleep(timeBetweenFloors);
                //Set to opposite of current true direction of travel
                neutralDirection = "Down";
                currentFloorNumber++;
            }
            if (x < currentFloorNumber)
            {
                System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " moving from Floor " + getCurrentFloorNumber() + " to Floor " + (getCurrentFloorNumber() - 1) + printFloorRequestNumbers() + printRiderRequestNumbers());
                Thread.sleep(timeBetweenFloors);
                //Set to opposite of current true direction of travel
                neutralDirection = "Up";
                currentFloorNumber--;
            }
        }
        
        if (x == currentFloorNumber && direction.equals("Neutral")) direction = neutralDirection;
        
        if (floorRequestList[currentFloorNumber] || riderRequestList[currentFloorNumber]) doorsOpenAndClose();
        floorRequestList[currentFloorNumber] = false;
        riderRequestList[currentFloorNumber] = false;
        
        boolean changeDirection = true;
        if (direction.equals("Up"))
        {
           for (int i = currentFloorNumber; i < floorRequestList.length; i++)
           {
               if (floorRequestList[i] || riderRequestList[i]) {changeDirection = false; break;}
           }
           if (changeDirection) direction = "Idle";
        }
        if (direction.equals("Down"))
        {
            for (int i = currentFloorNumber; i >= 0; i--)
            {
                if (floorRequestList[i] || riderRequestList[i]) {changeDirection = false; break;}
            }
            if (changeDirection) direction = "Idle";
        }
        if (direction.equals("Idle"))
        {
            for (int i = currentFloorNumber; i < floorRequestList.length; i++)
            {
                if (floorRequestList[i] || riderRequestList[i]) {direction = "Up"; return;}
            }
            for (int i = currentFloorNumber; i >= 0; i--)
            {
                if (floorRequestList[i] || riderRequestList[i]) {direction = "Down"; return;}
            }
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " has no requests" + printFloorRequestNumbers() + printRiderRequestNumbers());
            ElevatorController.getInstance().processPendingRequests();
        }
      
    }
    
    private void doorsOpenAndClose() throws InterruptedException
    {
        if (direction.equals("Up") && floorRequestList[currentFloorNumber])
        {
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " has arrived at Floor " + getCurrentFloorNumber() + " for UP request");
        }
        if (direction.equals("Down") && floorRequestList[currentFloorNumber])
        {
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " has arrived at Floor " + getCurrentFloorNumber() + " for DOWN request");
        }
        if (direction.equals("Up") && riderRequestList[currentFloorNumber])
        {
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " has arrived at Floor " + getCurrentFloorNumber() + " for Rider request");
        }
        if (direction.equals("Down") && riderRequestList[currentFloorNumber])
        {
            System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " has arrived at Floor " + getCurrentFloorNumber() + " for Rider request");
        }
        
        doorOpen = true;
        System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " Doors Open");
        
        exchangeTravelers();
        Thread.sleep(timeDoorsOpenAndClose);
        
        doorOpen = false;
        System.out.println(Time.getCurrentTime() + "Elevator " + getElevatorNumber() + " Doors Close");
        
        
    }
    
    private synchronized void exchangeTravelers()
    {
        ArrayList<Traveler> toTransfer = new ArrayList<>();
        for (Traveler t: riderList)
        {
            try
            {
                if (t.getDestination() == currentFloorNumber + 1) 
                {
                    Building.getInstance().addTraveler(currentFloorNumber + 1, t);
                    toTransfer.add(t);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        for (Traveler t: toTransfer) riderList.remove(t);
        Building.getInstance().loadRiders(this, currentFloorNumber);
        for (Traveler t: riderList)
        {
            riderRequestList[t.getDestination() - 1] = true;
        }
    }
    
    private String printFloorRequestNumbers()
    {
        String numbers = "";
        for (int i = 0; i < floorRequestList.length; i++)
        {
            if (floorRequestList[i] == true) numbers += (i + 1) + ", ";
        }
        return " [Floor Requests: " + numbers + "]";
    }
    
    private String printRiderRequestNumbers()
    {
        String numbers = "";
        for (int i = 0; i < riderRequestList.length; i++)
        {
            if (riderRequestList[i]) numbers += (i + 1) + ", ";
        }
        return " [Rider Requests: " + numbers + "]";
    }
    
    private String printRiderList()
    {
        String riders = "";
        for (Traveler t: riderList)
        {
            riders += "R" + t.getId() + ", ";
        }
        return " [Riders: " + riders + "]";
    }
        
    private void setElevatorNumber(int elevatorNo) throws InvalidParameterException
    {
        if (elevatorNo <= 0) throw new InvalidParameterException("Elevator Number must be greater than zero");
        elevatorNumber = elevatorNo;
    }
    
    private void setRiderCapacity(int capacity) throws InvalidParameterException
    {
        if (capacity <= 0) throw new InvalidParameterException("Elevator rider capacity must be greater than zero");
        riderCapacity = capacity;
    }
    
    private void setIdleTime(long idle) throws InvalidParameterException
    {
        if (idle < 0) throw new InvalidParameterException("Idle time must be 0 milliseconds or greater");
        idleTime = idle;
    }
    
    private void setTimeBetweenFloors(long betweenFloors) throws InvalidParameterException
    {
        if (betweenFloors < 0) throw new InvalidParameterException("Time between floors must be 0 milliseconds or greater");
        timeBetweenFloors = betweenFloors;
    }
    
    private void setTimeDoorsOpenAndClose(long doorsOpenAndClose) throws InvalidParameterException
    {
        if (doorsOpenAndClose < 0) throw new InvalidParameterException("Door open/close time must be 0 milliseconds or greater");
        timeDoorsOpenAndClose = doorsOpenAndClose;
    }
    
            
}
