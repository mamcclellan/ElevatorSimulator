/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElevatorSystem.util;

import java.text.SimpleDateFormat;

/**
 *
 * @author Michael
 */
public class Time {
    
    private static long startTime = 0;

    public static String getCurrentTime() {
        String s = new SimpleDateFormat("00:mm:ss.SSS").
                format(System.currentTimeMillis() - startTime);
        return s + "    ";
    }
    
    public static void setStartTime(long time) {
        startTime = time;
    }
}
