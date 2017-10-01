/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem.util;

import ElevatorSystem.Building;
import ElevatorSystem.util.InvalidRequestException;

/**
 *
 * @author Michael
 */
public class Request {
    private int floor;
    private String direction;
    
    public Request(int floor, 
            String direction) throws InvalidRequestException
    {
        setFloor(floor);
        setDirection(direction);
    }
    
    public int getFloor()
    {
        int temp = floor;
        return temp;
    }
    
    public String getDirection()
    {
        String temp = direction;
        return direction;
    }
    
    private void setFloor(int f) throws InvalidRequestException
    {
        if (f < 1 || f > Building.getInstance().getNumFloors()) throw new InvalidRequestException("Invalid floor given to request");
        floor = f;
    }
    
    private void setDirection(String d) throws InvalidRequestException
    {
        if (!d.equals("Up") && !d.equals("Down")) throw new InvalidRequestException("Invalid direction given to request");
        direction = d;
    }
}
