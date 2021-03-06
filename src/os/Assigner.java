/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import java.util.HashMap;
import java.util.Map;

import os.AlgorithmScheduling.MultilevelQueue;
import os.Enum.Resource;
import os.Enum.StateCore;
import os.Enum.StateTask;

/**
 * @author Aref
 */
public class Assigner {

    public static synchronized void readyToRunning(Core c, Task headTask) {
        headTask.setAssigned(true);
        headTask.setState(StateTask.Running);
        c.assignTask(headTask);
        c.setStateCore(StateCore.WORKING);
        for (Map.Entry me : headTask.getNeeded().entrySet()) {
            int valueMap = ResourceMap.map.get(me.getKey());
            int valueNeed = (int) me.getValue();
            ResourceMap.map.put((Resource) me.getKey(), valueMap - valueNeed);
        }
    }

    public static void runingToReady(Task task) {
        selectedQueue(task);
        for (Map.Entry me : task.getNeeded().entrySet()) {
            int valueMap = ResourceMap.map.get(me.getKey());
            int valueNeed = (int) me.getValue();
            ResourceMap.map.put((Resource) me.getKey(), valueMap + valueNeed);
        }
    }

    private static void selectedQueue(Task task) {
        task.setAssigned(false);
        task.setState(StateTask.READY);
        if (Main.isMultilevel && MultilevelQueue.isMultilevelRR) {
            Queues.foregroundRR.add(task);
            return;
        }
        if (Main.isMultilevel) {
            Queues.backgroundFCFS.add(task);
            return;
        }
        Queues.readyTask.add(task);
    }

    public static void readyToWaiting(Task task) {
        task.setAssigned(false);
        task.setState(StateTask.WAITING);
        Queues.waitingTask.add(task);
    }

    public static void waitingToReady(Task task) {
        selectedQueue(task);
    }

    public static void runingToTerminate(Task task) {
        task.setState(StateTask.Terminate);
        task.setAssigned(false);
        Queues.terminateTask.add(task);
        for (Map.Entry me : task.getNeeded().entrySet()) {
            int valueMap = ResourceMap.map.get(me.getKey());
            int valueNeed = (int) me.getValue();
            ResourceMap.map.put((Resource) me.getKey(), valueMap + valueNeed);
        }
    }

}
