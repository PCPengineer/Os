package os.AlgorithmScheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import os.ResourceMap;
import os.Task;

public class SJF extends Algorithm {

    @Override
    public void runScheduling(Queue<Task> readyQueue) {
        ArrayList<Task> tasks = new ArrayList<>(readyQueue);

        for (int i = 0; i < tasks.size(); i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                if (tasks.get(i).getTaskDuration() > tasks.get(j).getTaskDuration()) {
                    Task temp = tasks.get(i);
                    tasks.set(i, tasks.get(j));
                    tasks.set(j, temp);
                }
            }
        }
        readyQueue = new LinkedList<>();
        readyQueue.addAll(tasks);


    }
}
