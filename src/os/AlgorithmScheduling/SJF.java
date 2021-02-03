package os.AlgorithmScheduling;

import java.util.*;

import os.Queues;
import os.ResourceMap;
import os.Task;

public class SJF extends Algorithm {

    @Override
    public void runScheduling(Queue<Task> readyQueue) {
        ArrayList<Task> tasks = new ArrayList<>(readyQueue);
        tasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {

                return o1.getTaskDuration() - o2.getTaskDuration();
            }
        });
        readyQueue = new LinkedList<>();
        readyQueue.addAll(tasks);
        Queues.readyTask = readyQueue;


    }
}
