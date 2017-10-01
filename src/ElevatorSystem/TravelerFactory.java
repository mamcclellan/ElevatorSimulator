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
public class TravelerFactory {
    
    public static Traveler createTraveler(int id, int start, int destination) throws InvalidParameterException
    {
        return new PersonImpl(id, start, destination);
    }
}
