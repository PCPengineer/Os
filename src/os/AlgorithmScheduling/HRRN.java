package os.AlgorithmScheduling;

import os.Queues;
import os.Task;
import os.Time;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class HRRN extends Algorithm {
    @Override
    public void runScheduling(Queue<Task> readyQueue) {
        ArrayList<Task> tasks = new ArrayList<>(readyQueue);
        int[] probity = new int[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            probity[i] = (tasks.get(i).getTaskDuration() + Time.time) / tasks.get(i).getTaskDuration();

        }
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (probity[i] < probity[j]) {
                    Task temp = tasks.get(i);
                    tasks.set(i, tasks.get(j));
                    tasks.set(j, temp);

                }
            }
        }
        readyQueue = new LinkedList<>(tasks);
        Queues.readyTask = readyQueue;

    }
}
