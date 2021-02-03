package os.AlgorithmScheduling;

import java.util.Queue;
import os.Task;

public class RR extends Algorithm {
    public static final int INIT_QUANTOM=2;
    public static boolean isRR=false;
    private Task[] assigendTasks=new Task[4];
    private int[] remainingTime=new int[4];
    
    @Override
    public void runScheduling(Queue<Task> readyQueue) {
        
        
    }
}
