package os;

import java.util.LinkedList;

public class Queues {
    public static java.util.Queue<Task> waitingTask = new LinkedList<>();
    public static java.util.Queue<Task> readyTask = new LinkedList<>();
    public static java.util.Queue<Task> terminateTask = new LinkedList<>();
}
