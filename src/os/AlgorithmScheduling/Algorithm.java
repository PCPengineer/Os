package os.AlgorithmScheduling;

import java.util.Queue;
import os.Task;

public abstract class Algorithm {
    public abstract void runScheduling(Queue<Task> readyQueue);
}
