/*
 * Michael McClellan
 * 2015 - 2017
 */
package ElevatorSystem.gui;

import ElevatorSystem.Simulator;
import ElevatorSystem.util.InputHolder;
import ElevatorSystem.util.XMLParser;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * A simple UI for configuring the parameters of the simulator. All simulator
 * output occurs through System output. This UI does not block while the 
 * Simulator is running, and is configured to allow only one simulation at a 
 * time. It will allow another simulation after the current one is complete.
 * 
 * @author Michael
 */
public class UserInterface extends Application {

    private InputHolder inputs;
    private final UserInterface ui = this;

    private TextField numFloors;
    private TextField numElevators;
    private TextField elevatorCapacity;
    private TextField timeBetweenFloors;
    private TextField timeDoorOpenClose;
    private TextField idleTime;
    private TextField personsPerMinute;
    private TextField defaultFloor;
    private TextField simulationDuration;

    private Text actiontarget;
    private Button StartButton;

    @Override
    public void start(Stage primaryStage) {

        try {
            inputs = XMLParser.getInputs();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("XML parsing failed; resorting to defaults");
            inputs = new InputHolder(21, 3, 8, 500, 500, 15000, 15, 1, 1);
        }

        primaryStage.setTitle("Elevator Simulator");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Enter Simulation Inputs:");
        scenetitle.setId("welcome-text");
        grid.add(scenetitle, 0, 0, 2, 1);

        grid.add(new Label("Number of Floors:"), 0, 1);
        grid.add(new Label("Number of Elevators:"), 0, 2);
        grid.add(new Label("Elevator Capacity:"), 0, 3);
        grid.add(new Label("Time Between Floors (ms):"), 0, 4);
        grid.add(new Label("Door Operation Time (ms):"), 0, 5);
        grid.add(new Label("Elevator Idle Time (ms):"), 0, 6);
        grid.add(new Label("Persons Per Minute:"), 0, 7);
        grid.add(new Label("Default Elevator Floor:"), 0, 8);
        grid.add(new Label("Simulation Duration (min):"), 0, 9);

        numFloors = new TextField("" + inputs.getNumFloors());
        numElevators = new TextField("" + inputs.getNumElevators());
        elevatorCapacity = new TextField("" + inputs.getElevatorCapacity());
        timeBetweenFloors = new TextField("" + inputs.getBetweenFloorsTime());
        timeDoorOpenClose = new TextField("" + inputs.getDoorOpenCloseTime());
        idleTime = new TextField("" + inputs.getIdleTime());
        personsPerMinute = new TextField("" + inputs.getPersonsPerMinute());
        defaultFloor = new TextField("" + inputs.getDefaultElevatorFloor());
        simulationDuration = new TextField("" + inputs.getSimulationDuration());

        grid.add(numFloors, 1, 1);
        grid.add(numElevators, 1, 2);
        grid.add(elevatorCapacity, 1, 3);
        grid.add(timeBetweenFloors, 1, 4);
        grid.add(timeDoorOpenClose, 1, 5);
        grid.add(idleTime, 1, 6);
        grid.add(personsPerMinute, 1, 7);
        grid.add(defaultFloor, 1, 8);
        grid.add(simulationDuration, 1, 9);

        StartButton = new Button("Start Simulation");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(StartButton);
        grid.add(hbBtn, 1, 11);

        Text actiontarget = new Text();
        actiontarget.setId("actiontarget");
        grid.add(actiontarget, 1, 13);

        //grid.setGridLinesVisible(true);
        StartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setText("Simulator running; see output");
                StartButton.setVisible(false);
                final InputHolder finalInputs = new InputHolder(
                        Integer.parseInt(numFloors.getText()),
                        Integer.parseInt(numElevators.getText()),
                        Integer.parseInt(elevatorCapacity.getText()),
                        Long.parseLong(timeBetweenFloors.getText()),
                        Long.parseLong(timeDoorOpenClose.getText()),
                        Long.parseLong(idleTime.getText()),
                        Integer.parseInt(personsPerMinute.getText()),
                        Integer.parseInt(defaultFloor.getText()),
                        Integer.parseInt(simulationDuration.getText()));

                
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        new Simulator(finalInputs).runSimulation();
                        return null;
                    }
                };
                
                task.setOnSucceeded(t -> {
                    actiontarget.setText("Simulation Complete! View results or start again.");
                    StartButton.setVisible(true);
                });
                
                new Thread(task).start();
            }
        });

        Scene scene = new Scene(grid, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
