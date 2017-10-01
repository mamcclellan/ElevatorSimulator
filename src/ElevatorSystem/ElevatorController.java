/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem;

import ElevatorSystem.util.Request;
import ElevatorSystem.algs.ElevatorCallDelegate;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import ElevatorSystem.util.InvalidRequestException;
import ElevatorSystem.util.InvalidParameterException;
import ElevatorSystem.util.Time;

/**
 *
 * @author Michael
 */
public class ElevatorController
{
    
    private volatile static ElevatorController instance;
    private ArrayList<Floor> floorList = new ArrayList<>();
    private ArrayList<Elevator> elevatorList = new ArrayList<>();
    private Queue<Request> pendingRequestList = new LinkedList<>();
    private boolean isRunning = true;
    
    private ElevatorController() {}
    
    public static ElevatorController getInstance() 
    {
        if (instance == null)
        {
            synchronized (ElevatorController.class)
            {
                if (instance == null) instance = new ElevatorController();
            }
        }
        return instance;
    }
    
    
    public void addFloor(Floor f)
    {
        floorList.add(f.getFloorNumber() - 1, f);
    }
    
    public synchronized void addElevator(Elevator e)
    {
        elevatorList.add(e.getElevatorNumber() - 1, e);
    }
    
    public synchronized void processFloorRequest(int floor, String direction) throws InvalidRequestException, InvalidParameterException
    {
        if (floor > Building.getInstance().getNumFloors() || floor < 1) throw new InvalidRequestException("Floor doesn't exist");
        if (!direction.equals("Up") && !direction.equals("Down")) throw new InvalidRequestException("Direction for floor request must be 'Up' or 'Down'");
       
        ElevatorCallDelegate.getInstance().processRequest(elevatorList, floor, direction, false);
        
    }
    
    public synchronized void processPendingRequests()
    {
        //I didn't feel this needed its own delegate, since I process pending requests the same way as initial requests
        int numPendingRequests = pendingRequestList.size();
        for (int i=0; i < numPendingRequests; i++)
        {
            Request current = pendingRequestList.remove();
            try
            {
                ElevatorCallDelegate.getInstance().processRequest(elevatorList, current.getFloor(), current.getDirection(), true);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        if (!pendingRequestList.isEmpty())
        {
            System.out.println(Time.getCurrentTime() + "All elevators busy! Requests pending: " + pendingRequestList.size());
        }
    }
    
    public synchronized void addPendingRequest(Request r)
    {
        pendingRequestList.add(r);
    }
    
    public synchronized void removeOutdatedPendingRequest(Floor f) {
        ArrayList<Request> toRemove = new ArrayList<>();
        for (Request r: this.pendingRequestList) {
            if (r.getFloor() == f.getFloorNumber())
                toRemove.add(r);
        }
        for (Request r: toRemove) {
            pendingRequestList.remove(r);
        }
    }
    
    public int getNumPendingRequests()
    {
        int temp = pendingRequestList.size();
        return temp;
    }
    
    
}
