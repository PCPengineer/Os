package os;

import os.AlgorithmScheduling.MultilevelQueue;
import os.AlgorithmScheduling.RR;

import os.Enum.StateCore;

public class Core extends Thread {

    private StateCore stateCore = StateCore.IDLE;
    private Task activeTask = null;
    private boolean interrupt = false;
    private int quantom = RR.INIT_QUANTOM;
    private int quantumMultilevel = MultilevelQueue.INIT_QUANTUM;

    public boolean isInterrupt() {
        return interrupt;
    }

    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    public int getQuantumMultilevel() {
        return quantumMultilevel;
    }

    public void setQuantumMultilevel(int quantumMultilevel) {
        this.quantumMultilevel = quantumMultilevel;
    }


    public int getQuantom() {
        return quantom;
    }

    public void setQuantom(int quantom) {
        this.quantom = quantom;
    }


    public StateCore getStateCore() {
        return stateCore;
    }

    public void setStateCore(StateCore stateCore) {
        this.stateCore = stateCore;
    }

    public Task getActiveTask() {
        return activeTask;
    }

    public void setActiveTask(Task activeTask) {
        this.activeTask = activeTask;
    }

    public void assignTask(Task t) {
        setActiveTask(t);
        setStateCore(StateCore.WORKING);
    }

    public void interruptCore() {
        interrupt = true;
    }

    //true if quantom remains
    //false if quantom finished
    private boolean reduceAndCheckQuantom() {
        quantom--;
        if (quantom == 0) {
            quantom = RR.INIT_QUANTOM;
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        super.run();
        if (activeTask == null) {
            return;
        }
        activeTask.setUntilTime(activeTask.getUntilTime() + 1);

        if (MultilevelQueue.isMultilevelRR) {
            quantumMultilevel--;
            if (quantumMultilevel == 0) {
                quantumMultilevel = MultilevelQueue.INIT_QUANTUM;
                interrupt = true;
            }
        }
        if (RR.isRR) {
            quantom--;
            if (quantom == 0) {
                quantom = RR.INIT_QUANTOM;
                interrupt = true;
            }
        }

        if (activeTask.getTaskDuration() == activeTask.getUntilTime()) {
            Assigner.runingToTerminate(activeTask);
            stateCore = StateCore.IDLE;
            activeTask = null;
            return;
        }


        if (interrupt) {
            Assigner.runingToReady(activeTask);
            interrupt = false;
            stateCore = StateCore.IDLE;
            activeTask = null;
        }
    }
}
