/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem.util;

/**
 *
 * @author Michael
 */
public class InputHolder {
    
    private final int numFloors;
    private final int numElevators;
    private final int elevatorCapacity;
    private final long betweenFloorsTime;
    private final long doorOpenCloseTime;
    private final long idleTime;
    private final int personsPerMinute;
    private final int defaultElevatorFloor;
    private final int simulationDuration;
    
    public InputHolder(int numFloors, int numElevators, int elevatorCapacity,
            long betweenFloorsTime, long doorOpenCloseTime, long idleTime,
            int personsPerMinute, int defaultElevatorFloor,
            int simulationDuration) {
        
        this.numFloors = numFloors;
        this.numElevators = numElevators;
        this.elevatorCapacity = elevatorCapacity;
        this.betweenFloorsTime = betweenFloorsTime;
        this.doorOpenCloseTime = doorOpenCloseTime;
        this.idleTime = idleTime;
        this.personsPerMinute = personsPerMinute;
        this.defaultElevatorFloor = defaultElevatorFloor;
        this.simulationDuration = simulationDuration;
    }

    /**
     * @return the numFloors
     */
    public int getNumFloors() {
        return numFloors;
    }

    /**
     * @return the numElevators
     */
    public int getNumElevators() {
        return numElevators;
    }

    /**
     * @return the elevatorCapacity
     */
    public int getElevatorCapacity() {
        return elevatorCapacity;
    }

    /**
     * @return the betweenFloorsTime
     */
    public long getBetweenFloorsTime() {
        return betweenFloorsTime;
    }

    /**
     * @return the doorOpenCloseTime
     */
    public long getDoorOpenCloseTime() {
        return doorOpenCloseTime;
    }

    /**
     * @return the idleTime
     */
    public long getIdleTime() {
        return idleTime;
    }

    /**
     * @return the personsPerMinute
     */
    public int getPersonsPerMinute() {
        return personsPerMinute;
    }

    /**
     * @return the defaultElevatorFloor
     */
    public int getDefaultElevatorFloor() {
        return defaultElevatorFloor;
    }

    /**
     * @return the simulationDuration
     */
    public int getSimulationDuration() {
        return simulationDuration;
    }
    
}
