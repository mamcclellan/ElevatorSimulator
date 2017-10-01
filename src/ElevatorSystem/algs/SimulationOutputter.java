/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem.algs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import ElevatorSystem.Floor;
import ElevatorSystem.Traveler;


/**
 *
 * @author Michael
 */
public class SimulationOutputter 
{
    private ArrayList<Floor> floorList;
    
    public SimulationOutputter(ArrayList<Floor> fList)
    {
        setFloorList(fList);
    }
    
    public void printResults()
    {
        printWaitTimesByFloor();
        printRideTimes();
        printTimesByTraveller();
    }
    
    private void printWaitTimesByFloor()
    {
        System.out.println();
        System.out.format("%-20s%21s%27s%30s%n", "Floor Number", "Average Wait Time", "Min Wait Time", "Max Wait Time");
        for (int i = 0; i < floorList.size(); i++)
        {
            long avgWait;
            long minWait = 99999;
            long maxWait = 0;
            long sum = 0;
            long[] waitTimes = floorList.get(i).getFinishedWaitTimes();
            if (waitTimes.length == 0)
            {
                System.out.format("%s %-2d%24s%29s%29s%n","Floor ", i+1,"-","-","-");
                continue;
            }
            for (int j = 0; j < waitTimes.length; j++) 
            {
                sum += waitTimes[j];
                if (waitTimes[j] < minWait) minWait = waitTimes[j];
                if (waitTimes[j] > maxWait) maxWait = waitTimes[j];
            }
            avgWait = sum / waitTimes.length;
            System.out.format("%s %-2d%20d seconds %20d seconds %20d seconds%n","Floor ",i+1,avgWait/1000,minWait/1000,maxWait/1000);
        }
        System.out.println();
    }
    
    private void printRideTimes()
    {
        System.out.println();
        String[][] avgRideTimes = new String[floorList.size()][floorList.size()];
        String[][] maxRideTimes = new String[floorList.size()][floorList.size()];
        String[][] minRideTimes = new String[floorList.size()][floorList.size()];
        for (int i = 0; i < floorList.size(); i++)
        {
            for (int j = 0; j < floorList.size(); j++)
            {
                if (i == j) {avgRideTimes[i][j] = "X"; maxRideTimes[i][j] = "X"; minRideTimes[i][j] = "X"; continue;}
                ArrayList<Traveler> travellerList = floorList.get(j).getFinishedTravellerList();
                boolean flag = true;
                long sum = 0;
                long divisor = 0;
                long avgWait;
                long maxWait = 0;
                long minWait = 99999;
                for (Traveler t: travellerList)
                {
                    if (t.getStartFloor() == i + 1)
                    {
                        flag = false;
                        sum += t.getRideTime();
                        divisor++;
                        if (t.getRideTime() < minWait) minWait = t.getRideTime();
                        if (t.getRideTime() > maxWait) maxWait = t.getRideTime();
                    }
                }
                if (flag) {avgRideTimes[i][j] = "-"; maxRideTimes[i][j] = "-"; minRideTimes[i][j] = "-"; continue;}
                String avg = String.valueOf((sum / divisor)/1000);
                String max = String.valueOf(maxWait/1000);
                String min = String.valueOf(minWait/1000);
                avgRideTimes[i][j] = avg;
                maxRideTimes[i][j] = max;
                minRideTimes[i][j] = min;
            }
        }
        
        //Print Average Ride Times Floor to Floor
        System.out.println("Average Ride Time from Floor to Floor by Person:");
        System.out.format("%-2s", "Floor ");
        for (int i = 0; i < floorList.size(); i++) System.out.format("%-5s", i + 1);
        System.out.println();
        for (int i = 0; i < floorList.size(); i++)
        {
            System.out.format("%-2d",i+1);
            for (int j = 0; j < floorList.size(); j++)
            {
                System.out.format("%5s", avgRideTimes[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        
        //Print Max Ride Times Floor to Floor
        System.out.println("Max Ride Time from Floor to Floor by Person:");
        System.out.format("%-2s", "Floor ");
        for (int i = 0; i < floorList.size(); i++) System.out.format("%-5s", i + 1);
        System.out.println();
        for (int i = 0; i < floorList.size(); i++)
        {
            System.out.format("%-2d",i+1);
            for (int j = 0; j < floorList.size(); j++)
            {
                System.out.format("%5s", maxRideTimes[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        
        //Print Min Ride Times Floor to Floor
        System.out.println("Min Ride Time from Floor to Floor by Person:");
        System.out.format("%-2s", "Floor ");
        for (int i = 0; i < floorList.size(); i++) System.out.format("%-5s", i + 1);
        System.out.println();
        for (int i = 0; i < floorList.size(); i++)
        {
            System.out.format("%-2d",i+1);
            for (int j = 0; j < floorList.size(); j++)
            {
                System.out.format("%5s", minRideTimes[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private void printTimesByTraveller()
    {
        System.out.println("Wait/Ride/Total Time by Person: ");
        ArrayList<Traveler> travellerList = new ArrayList<>();
        for (Floor f: floorList)
        {
            for (Traveler t: f.getFinishedTravellerList())
            {
                travellerList.add(t);
            }
        }
        
        Collections.sort(travellerList, new Comparator<Traveler>() 
        {
            @Override 
            public int compare(Traveler t1, Traveler t2) 
            {
                return t1.getId() - t2.getId(); 
            }
        });
        
        System.out.format("%s %20s%20s%15s%24s%24s%n","Person ","Start Floor","Destination Floor","Wait Time","Ride Time","Total Time");
        for (Traveler t: travellerList)
        {
            long waitTime = t.getWaitTime() / 1000;
            long rideTime = t.getRideTime() / 1000;
            long totalTime = waitTime + rideTime;
            System.out.format("%s %-2d%15d%15d%15d seconds %15d seconds %15d seconds%n","Person ",t.getId(),t.getStartFloor(),t.getDestination(),waitTime,rideTime,totalTime);
        }
        
        System.out.println();
    }
    
    private void setFloorList(ArrayList<Floor> fList)
    {
        floorList = fList;
    }
    
}
