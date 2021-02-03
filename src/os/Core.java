package os;

import java.util.logging.Level;
import java.util.logging.Logger;

import os.Enum.StateCore;

public class Core extends Thread {

    private StateCore stateCore = StateCore.IDLE;
    private Task activeTask = null;
    private boolean interrupt = false;

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
        this.interrupt = true;
    }

    @Override
    public void run() {
        super.run();
        if (activeTask == null) {
            return;
        }
        activeTask.setUntilTime(activeTask.getUntilTime() + 1);
        if (activeTask.getTaskDuration() == activeTask.getUntilTime()) {
            Assigner.runingToTerminate(activeTask);
            stateCore = StateCore.IDLE;
            activeTask = null;
        }
        if (interrupt) {
            Assigner.runingToReady(activeTask);
            interrupt = false;
            stateCore = StateCore.IDLE;
            activeTask = null;
        }
    }
}
