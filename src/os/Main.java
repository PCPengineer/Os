package os;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import os.AlgorithmScheduling.*;
import os.Enum.Priority;
import os.Enum.Resource;
import os.Enum.StateCore;
import os.Enum.StateTask;

import java.util.*;

public class Main {

    private static final int allTime = 0;
    private static final Scanner sc = new Scanner(System.in);
    private static Algorithm algorithm;
    public static boolean isMultilevel = false;

    public static void main(String[] args) {
        init();
        start();
    }

    /*
    t1 Y 6
    t2 Y 5
    t3 Y 4
    t4 Y 3
    t5 Y 2
    t6 Y 1
        */
    private static void start() {
        CPU cpu = new CPU();
        Time time = new Time();
        WaitingScheduler waitingScheduling = new WaitingScheduler();
        cpu.printCoresData();
        Time.time++;
        if (algorithm instanceof FCFS || algorithm instanceof SJF) {
            algorithm.runScheduling(Queues.readyTask);
        } else if (algorithm instanceof RR) {
            RR.isRR = true;
        }
        while (!isReadyEmpty() || !Queues.waitingTask.isEmpty() || existIdleCore(cpu)) {
            if (existIdleCore(cpu) && checkWaitingTasksResources()) {
                waitingScheduling.runScheduling(Queues.waitingTask);
                Task waitedTask = Queues.waitingTask.peek();
                assert waitedTask != null;
                Assigner.waitingToReady(waitedTask);
                Queues.waitingTask.remove();
            }
            if (algorithm instanceof HRRN) {
                algorithm.runScheduling(Queues.readyTask);
            }
            for (Core core : cpu.getCores()) {
                if (core.getStateCore().equals(StateCore.IDLE)) {
                    if (isMultilevel) {
                        Task headTask;
                        if (Time.time % 5 >= 3) {
                            MultilevelQueue.isMultilevelRR = false;
                            headTask = Queues.backgroundFCFS.peek();
                            if (headTask == null) {
                                continue;
                            }
                            if (headTask.checkResource(ResourceMap.map)) {
                                Assigner.readyToRunning(core, headTask);
                                Queues.backgroundFCFS.remove();
                            } else {
                                Assigner.readyToWaiting(headTask);
                                Queues.backgroundFCFS.remove();
                            }
                        } else {
                            MultilevelQueue.isMultilevelRR = true;
                            headTask = Queues.foregroundRR.peek();
                            if (headTask == null) {
                                continue;
                            }
                            if (headTask.checkResource(ResourceMap.map)) {
                                Assigner.readyToRunning(core, headTask);
                                Queues.foregroundRR.remove();
                            } else {
                                Assigner.readyToWaiting(headTask);
                                Queues.foregroundRR.remove();
                            }
                        }
                    } else {
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
        sc.nextLine();
        initScheduling();

        if (isMultilevel) {
            System.out.println("Please Enter Number of Tasks Foreground");
            int numberTaskForeground = sc.nextInt();
            System.out.println("Please Enter Information of Tasks Foreground");
            for (int i = 0; i < numberTaskForeground; i++) {
                if (i == 0) {
                    sc.nextLine();
                }
                String eachTask = sc.nextLine();
                String[] eachInfo = eachTask.split(" ");//0 name  1 priority 2 taskDuration
                int taskDuration = Integer.parseInt(eachInfo[2]);
                Queues.foregroundRR.add(new Task(StateTask.READY,
                        eachInfo[1].equals("X") ? Priority.X : (eachInfo[1].equals("Y") ? Priority.Y : (eachInfo[1].equals("Z") ? Priority.Z : Priority.X)),
                        eachInfo[0], taskDuration));
            }
            System.out.println("Please Enter Number of Tasks Background");
            int numberTaskBackground = sc.nextInt();
            System.out.println("Please Enter Information of Tasks Background");
            for (int i = 0; i < numberTaskBackground; i++) {
                if (i == 0) {
                    sc.nextLine();
                }
                String eachTask = sc.nextLine();
                String[] eachInfo = eachTask.split(" ");//0 name  1 priority 2 taskDuration
                int taskDuration = Integer.parseInt(eachInfo[2]);
                Queues.backgroundFCFS.add(new Task(StateTask.READY,
                        eachInfo[1].equals("X") ? Priority.X : (eachInfo[1].equals("Y") ? Priority.Y : (eachInfo[1].equals("Z") ? Priority.Z : Priority.X)),
                        eachInfo[0], taskDuration));
            }


        } else {
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
        }
    }

    private static void initScheduling() {
        System.out.println("Please Enter Your Algorithm :\n 1)FCFS  \n 2)RR  \n 3)SJF \n 4)Multilevel Queue \n 5)HRRN");
        int temp = sc.nextInt();
        switch (temp) {
            case 1:
                algorithm = new FCFS();
                break;
            case 2:
                algorithm = new RR();
                break;
            case 3:
                algorithm = new SJF();
                break;
            case 4:
                algorithm = new MultilevelQueue();
                isMultilevel = true;
                break;
            case 5:
                algorithm = new HRRN();
                break;
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
    
    private static boolean isReadyEmpty(){
        if (isMultilevel) {
            return Queues.foregroundRR.isEmpty()&&Queues.backgroundFCFS.isEmpty();
        }else {
            return Queues.readyTask.isEmpty();
        }
    }

}
