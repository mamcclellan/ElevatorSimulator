/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem;

import ElevatorSystem.util.InvalidParameterException;
import ElevatorSystem.util.InvalidRequestException;
import ElevatorSystem.algs.SimulationOutputter;
import ElevatorSystem.util.Time;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class Building 
{
    private volatile static Building instance;
    public ElevatorController controller;
    private ArrayList<Floor> floorList = new ArrayList<>();
    private ArrayList<Elevator> elevatorList = new ArrayList<>();
    
    public static Building getInstance()
    {
        if (instance == null)
        {
            synchronized (Building.class)
            {
                if (instance == null) instance = new Building();
            }
        }
        return instance;
    }
    
    private Building() {}
    
    public void loadRiders(Elevator e, int floor)
    {
        floorList.get(floor).exchangeTravelers(e);
    }
    
    public int getNumFloors()
    {
        return floorList.size();
    }
    
    public int getNumElevators()
    {
        return elevatorList.size();
    }
    
    public void addFloor(Floor f)
    {
        floorList.add(f.getFloorNumber() - 1, f);
    }
    
    public void addTraveler(int floorNo, Traveler t) throws InvalidParameterException, InvalidRequestException
    {
        if (floorNo < 0 || floorNo > this.getNumFloors()) throw new InvalidParameterException("Invalid number of floors");
        if (t == null) throw new InvalidParameterException("Traveler not valid");
        floorList.get(floorNo - 1).addTraveler(t);
    }
    
    public synchronized void addElevator(Elevator e)
    {
        elevatorList.add(e.getElevatorNumber() - 1, e);
    }
    
    public boolean elevatorsAtDefault()
    {
        boolean result = true;
        for (Elevator e: elevatorList)
        {
            if (e.getCurrentFloorNumber() != e.getDefaultFloorNumber()) result = false;
        }
        return result;
    }
    
    public boolean verifySuccess(int peopleCreated) throws InvalidParameterException
    {
        if (peopleCreated < 0) throw new InvalidParameterException("How did this become negative?");
        int result = 0;
        for (Floor f: floorList) result += f.verifyFinishedList();
        System.out.println(Time.getCurrentTime() + "Total completed travelers found: " + result);
        if (result == peopleCreated) return true;
        else return false;
    }
    
    public void printSimulationResults()
    {
        SimulationOutputter outputter = new SimulationOutputter(floorList);
        outputter.printResults();
    }
    
}
    
  
