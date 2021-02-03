/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.AlgorithmScheduling;

import java.util.ArrayList;
import java.util.Queue;

import org.omg.IOP.TAG_ALTERNATE_IIOP_ADDRESS;
import os.Queues;
import os.ResourceMap;
import os.Task;


public class WaitingScheduler extends Algorithm {

    @Override
    public void runScheduling(Queue<Task> waitingQueue) {
        ArrayList<Task> tasksNotAllowResource = new ArrayList<>();
        ArrayList<Task> taskAllowResource = new ArrayList<>();
        for (Task task : waitingQueue) {
            if (task.checkResource(ResourceMap.map)) {
                taskAllowResource.add(waitingQueue.poll());
            } else {
                tasksNotAllowResource.add(waitingQueue.poll());
            }
        }

        for (int i = 0; i < taskAllowResource.size(); i++) {
            int wait1 = taskAllowResource.get(i).getLastWaitingTime();
            int duration1 = taskAllowResource.get(i).getTaskDuration();
            for (int j = i + 1; j < taskAllowResource.size(); j++) {
                int wait2 = taskAllowResource.get(j).getTaskDuration();
                int duration2 = taskAllowResource.get(j).getTaskDuration();
                if ((wait1 - wait2) <= 0 && (duration1 - duration2) >= 0) {
                    Task temp = taskAllowResource.get(i);
                    taskAllowResource.set(i, taskAllowResource.get(j));
                    taskAllowResource.set(j, temp);

                }
                if ((wait1 - wait2) >= 0 && (duration1 - duration2) <= 0) {
                    continue;
                }
                if (((wait1 - wait2) < 0 && (duration1 - duration2) < 0)) {

                    int wait = wait1 - wait2;
                    int duration = duration1 - duration2;
                    if (wait * -2 > duration * -1) {
                        Task temp = taskAllowResource.get(i);
                        taskAllowResource.set(i, taskAllowResource.get(j));
                        taskAllowResource.set(j, temp);
                    }

                }
                if ((wait1 - wait2) > 0 && (duration1 - duration2) > 0) {
                    int wait = wait1 - wait2;
                    int duration = duration1 - duration2;
                    if (wait * 2 > duration) {
                        Task temp = taskAllowResource.get(i);
                        taskAllowResource.set(i, taskAllowResource.get(j));
                        taskAllowResource.set(j, temp);
                    }
                }
            }
        }

        waitingQueue.addAll(taskAllowResource);
        waitingQueue.addAll(tasksNotAllowResource);

        
    }

}
