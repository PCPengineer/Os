package os;

import java.util.logging.Level;
import java.util.logging.Logger;

import os.AlgorithmScheduling.MultilevelQueue;
import os.Enum.StateCore;

public class CPU {

    private Core[] cores;
    private int counter = 0;

    CPU() {
        cores = new Core[4];
        for (int i = 0; i < cores.length; i++) {
            cores[i] = new Core();
        }
    }

    public Core[] getCores() {
        return cores;
    }

    public void setCores(Core[] cores) {
        this.cores = cores;
    }

    public void runCores() {
        Task tempTask;
        StateCore tempStateCore;
        int tempQuantom;
        int tempQuantumMulti = 0;
        for (int i = 0; i < cores.length; i++) {
            try {
                cores[i].start();
                cores[i].join();
                tempTask = cores[i].getActiveTask();
                tempStateCore = cores[i].getStateCore();
                tempQuantom = cores[i].getQuantom();
                if (MultilevelQueue.isMultilevelRR)
                    tempQuantumMulti = cores[i].getQuantumMultilevel();
                cores[i] = new Core();
                cores[i].setActiveTask(tempTask);
                cores[i].setStateCore(tempStateCore);
                cores[i].setQuantom(tempQuantom);
                if (MultilevelQueue.isMultilevelRR)
                    cores[i].setQuantumMultilevel(tempQuantumMulti);
            } catch (InterruptedException ex) {
                Logger.getLogger(CPU.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void printCoresData() {
        int coreNumber = 0;
        System.out.println("time: " + Time.time);
        System.out.println("Waiting Queue: " + Queues.waitingTask);
        if (Main.isMultilevel) {
            System.out.println("Background FCFS Queue: " + Queues.backgroundFCFS);
            System.out.println("Foreground RR Queue: " + Queues.foregroundRR);


        } else
            System.out.println("Ready Queue: " + Queues.readyTask);
        System.out.println("Terminated Queue: " + Queues.terminateTask);
        for (Core core : cores) {
            System.out.println("Core " + coreNumber++ + ": state=" + core.getStateCore() + " task=" + core.getActiveTask());
        }
        System.out.println();
        coreNumber = 0;
    }
}
