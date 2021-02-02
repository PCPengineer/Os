package os;

import java.util.logging.Level;
import java.util.logging.Logger;
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
        for (int i = 0; i < cores.length; i++) {
            try {
                cores[i].start();
                cores[i].join();
                tempTask = cores[i].getActiveTask();
                tempStateCore=cores[i].getStateCore();
                cores[i] = new Core();
                cores[i].setActiveTask(tempTask);
                cores[i].setStateCore(tempStateCore);
            } catch (InterruptedException ex) {
                Logger.getLogger(CPU.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void printCoresData() {
        System.out.println("time: " + Time.time);
        for (Core core : cores) {
            System.out.println("Core " + core.getName() + ": state=" + core.getStateCore() + " task=" + core.getActiveTask());
        }
    }
}
