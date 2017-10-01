/*
 * Michael McClellan
 * 2015 - 2017
 */
package ElevatorSystem;

import ElevatorSystem.gui.UserInterface;
import ElevatorSystem.util.InputHolder;
import ElevatorSystem.util.InvalidParameterException;
import ElevatorSystem.util.Time;

/**
 * Simulates a building with a number of floors and elevators, and generates
 * people on random floors for the elevators to transport. The Building class
 * is a singleton to which floors attach. The Elevators run on separate threads
 * and operate concurrently. The various components will output simple text
 * events as they happen. The simulation lasts for at least as long as the user
 * specifies, and terminates only when all the generated people arrive at their
 * destination and the elevators have returned to their default floor after a
 * timeout.
 * 
 * @author Michael
 */
public class Simulator {

    private Building testBuilding;
    private int peopleCreated = 0;

    private final InputHolder inputs;

    public Simulator(InputHolder inputs) {
        this.inputs = inputs;
    }

    public void runSimulation() {

        Time.setStartTime(System.currentTimeMillis());
        testBuilding = buildSimulation();

        try {
            int personsPerMinute = inputs.getPersonsPerMinute();
            int simulationDurationMinutes = inputs.getSimulationDuration();
            peopleCreated = personsPerMinute * simulationDurationMinutes;

            int personCounter = 1;
            for (int i = 0; i < simulationDurationMinutes; i++) {
                for (int j = 0; j < personsPerMinute; j++) {
                    int b = 0, y = 0;
                    //Create random start and destination floors
                    // Make sure Traveler start floor is not the same as destination floor
                    while (b == y) {
                        double a = Math.ceil(Math.random() * testBuilding.getNumFloors());
                        b = (int) a;
                        double x = Math.ceil(Math.random() * testBuilding.getNumFloors());
                        y = (int) x;
                    }
                    testBuilding.addTraveler(b, TravelerFactory.createTraveler(personCounter++, b, y));
                    if (personCounter <= peopleCreated) {
                        Thread.sleep((long) (60000.00 / personsPerMinute));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Determine when to stop simulation
        boolean stillRunning = true;
        while (stillRunning) {
            if (testBuilding.elevatorsAtDefault()) {
                try {
                    Thread.sleep(2000);
                    // Assumes all travelers will, eventually, get to their
                    // destination floor
                    if (testBuilding.elevatorsAtDefault() && 
                        testBuilding.verifySuccess(peopleCreated)) {
                    stillRunning = false;
                }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }
        }

        // Determine simulation success based on rider count
        // Outdated with check above
//        try {
//            Thread.sleep(1000);
//            if (testBuilding.verifySuccess(peopleCreated)) {
//                System.out.println(Time.getCurrentTime() + "Simulation success - all persons at destination");
//            } else {
//                System.out.println(Time.getCurrentTime() + "Simulation failed!");
//            }
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        } catch (InvalidParameterException ipe) {
//            ipe.printStackTrace();
//        }

        testBuilding.printSimulationResults();
    }

    
    private Building buildSimulation() {

        // Create simulation building
        testBuilding = Building.getInstance();
        System.out.println(Time.getCurrentTime() + "Created Building...");

        // Create building floors
        for (int i = 1; i <= inputs.getNumFloors(); i++) {
            try {
                new Floor(i);
            } catch (InvalidParameterException ex) {
                ex.printStackTrace();
            }
        }

        // Create elevators and run them
        for (int j = 1; j <= inputs.getNumElevators(); j++) {
            try {
                Thread t = new Thread((Runnable) ElevatorFactory.createElevator(j,
                        inputs.getDefaultElevatorFloor(),
                        inputs.getElevatorCapacity(),
                        inputs.getIdleTime(),
                        inputs.getBetweenFloorsTime(),
                        inputs.getDoorOpenCloseTime(),
                        inputs.getNumFloors()));
                t.setDaemon(true);
                t.start();
                t.sleep(50);
            } catch (InvalidParameterException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println(Time.getCurrentTime() + "Building created - " + testBuilding.getNumFloors() + " floors, " + testBuilding.getNumElevators() + " elevators");
        return testBuilding;
    }

}
