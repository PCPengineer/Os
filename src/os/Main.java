package os;

import os.AlgorithmScheduling.Algorithm;
import os.AlgorithmScheduling.FCFS;
import os.AlgorithmScheduling.RR;
import os.AlgorithmScheduling.SJF;
import os.Enum.Priority;
import os.Enum.Resource;
import os.Enum.StateCore;
import os.Enum.StateTask;

import java.util.*;

import os.AlgorithmScheduling.WaitingScheduler;

public class Main {

    private static final int allTime = 0;
    private static final Scanner sc = new Scanner(System.in);
    private static Algorithm algorithm;

    public static void main(String[] args) {
        init();
        start();
    }

    private static void start() {
        CPU cpu = new CPU();
        Time time = new Time();
        WaitingScheduler waitingScheduling = new WaitingScheduler();
        cpu.printCoresData();
        Time.time++;
        if (algorithm instanceof FCFS || algorithm instanceof SJF){
            algorithm.runScheduling(Queues.readyTask);
        }
        while (!Queues.readyTask.isEmpty() || !Queues.waitingTask.isEmpty() || existIdleCore(cpu)) {
            if (existIdleCore(cpu) && checkWaitingTasksResources()) {
                waitingScheduling.runScheduling(Queues.waitingTask);
                Task waitedTask = Queues.waitingTask.peek();
                assert waitedTask != null;
                Assigner.waitingToReady(waitedTask);
                Queues.waitingTask.remove();

            }
            //TODO runrobib scheduling
//            algorithm.runScheduling(Queues.readyTask);
            for (Core core : cpu.getCores()) {
                if (core.getStateCore().equals(StateCore.IDLE)) {
                    Task headTask = Queues.readyTask.peek();
                    if (headTask == null) {
                        continue;
                    }
                    if (headTask.checkResource(ResourceMap.map)) {
                        Assigner.readyToRunning(core, headTask);
                        Queues.readyTask.remove();
                    } else {
                        Assigner.readyToWaiting(headTask);
                        Queues.readyTask.remove();
                    }
                }
            }
            cpu.printCoresData();
            cpu.runCores();
            time.nextTime();
        }
    }

    private static void init() {
        ResourceMap.map = new HashMap<>();
        System.out.println("Welcome to My Scheduler");
        System.out.println("Please Enter Resource A B C");
        ResourceMap.map.put(Resource.A, sc.nextInt());
        ResourceMap.map.put(Resource.B, sc.nextInt());
        ResourceMap.map.put(Resource.C, sc.nextInt());
        System.out.println("Please Enter Number of Tasks");
        int numberTask = sc.nextInt();
        System.out.println("Please Enter Information of Tasks");
        for (int i = 0; i < numberTask; i++) {
            if (i == 0) {
                sc.nextLine();
            }
            String eachTask = sc.nextLine();
            String[] eachInfo = eachTask.split(" ");//0 name  1 priority 2 taskDuration
            int taskDuration = Integer.parseInt(eachInfo[2]);
            Queues.readyTask.add(new Task(StateTask.READY,
                    eachInfo[1].equals("X") ? Priority.X : (eachInfo[1].equals("Y") ? Priority.Y : (eachInfo[1].equals("Z") ? Priority.Z : Priority.X)),
                    eachInfo[0], taskDuration));
        }
        initScheduling();
    }

    private static void initScheduling() {
        System.out.println("Please Enter Your Algorithm :\n  1)FCFS  \n 2)RR  \n 3)SJF");
        int temp = sc.nextInt();
        switch (temp) {
            case 1:
                algorithm = new FCFS();
            case 2:
                algorithm = new RR();
            case 3:
                algorithm = new SJF();
        }

    }

    private static boolean existIdleCore(CPU cpu) {
        for (Core core : cpu.getCores()) {
            if (core.getStateCore() == StateCore.IDLE) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkWaitingTasksResources() {
        if (Queues.waitingTask.isEmpty()) {
            return false;
        }
        for (Task task : Queues.waitingTask) {
            if (task.checkResource(ResourceMap.map)) {
                return true;

            }
        }
        return false;
    }

}
