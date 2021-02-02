/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import java.util.HashMap;
import os.Enum.Resource;
import os.Enum.StateCore;
import os.Enum.StateTask;

/**
 *
 * @author Aref
 */
public class Assigner {

    public static synchronized void readyToRunning(Core c, Task headTask) {
        headTask.setAssigned(true);
        headTask.setState(StateTask.Running);
        c.assignTask(headTask);
        c.setStateCore(StateCore.WORKING);
        //TODO: reduce resources when they used
    }

    public static void runingToReady(Task task) {
        task.setAssigned(false);
        task.setState(StateTask.READY);
        Queues.readyTask.add(task);
    }

    public static void readyToWaiting(Task task) {
        task.setAssigned(false);
        task.setState(StateTask.WAITING);
        Queues.waitingTask.add(task);
    }

    public static void waitingToReady(Task task) {
        task.setAssigned(false);
        task.setState(StateTask.READY);
        Queues.readyTask.add(task);
    }

    public static void runingToTerminate(Task task) {
        task.setState(StateTask.Terminate);
        task.setAssigned(false);
        Queues.terminateTask.add(task); 
    }

}
