/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem.util;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Michael
 */
public class XMLParser {
    
    public static InputHolder getInputs() 
            throws SAXException, ParserConfigurationException, IOException {
        
        File simulationInputs = new File("SimulationInputs.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(simulationInputs);

        doc.getDocumentElement().normalize();

        NodeList inputList = doc.getElementsByTagName("Inputs");
        Node inputNode = inputList.item(0);
        Element inputElement = (Element) inputNode;

        // Parse XML inputs
        int numFloors = Integer.parseInt(inputElement.getElementsByTagName("Floors").item(0).getTextContent());
        int numElevators = Integer.parseInt(inputElement.getElementsByTagName("Elevators").item(0).getTextContent());
        int defaultElevatorFloor = Integer.parseInt(inputElement.getElementsByTagName("DefaultElevatorFloor").item(0).getTextContent());
        int elevatorCapacity = Integer.parseInt(inputElement.getElementsByTagName("ElevatorCapacity").item(0).getTextContent());
        long idleTime = Long.parseLong(inputElement.getElementsByTagName("ElevatorIdleTime").item(0).getTextContent());
        long betweenFloorsTime = Long.parseLong(inputElement.getElementsByTagName("TimeBetweenFloors").item(0).getTextContent());
        long doorOpenCloseTime = Long.parseLong(inputElement.getElementsByTagName("DoorOperationTime").item(0).getTextContent());
        int personsPerMinute = Integer.parseInt(inputElement.getElementsByTagName("PersonsPerMinute").item(0).getTextContent());
        int simulationDuration = Integer.parseInt(inputElement.getElementsByTagName("SimulationDuration").item(0).getTextContent());
        
        
        return new InputHolder(numFloors, numElevators, elevatorCapacity,
            betweenFloorsTime, doorOpenCloseTime, idleTime, personsPerMinute,
            defaultElevatorFloor, simulationDuration);
    }
    
}
