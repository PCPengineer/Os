package os;

import java.util.logging.Level;
import java.util.logging.Logger;
import os.AlgorithmScheduling.RR;

import os.Enum.StateCore;

public class Core extends Thread {

    private StateCore stateCore = StateCore.IDLE;
    private Task activeTask = null;
    private boolean interrupt = false;
    private int quantom=RR.INIT_QUANTOM;

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
    private boolean reduceAndCheckQuantom(){
        quantom--;
        if (quantom==0) {
            quantom=RR.INIT_QUANTOM;
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
        if (activeTask.getTaskDuration() == activeTask.getUntilTime()) {
            Assigner.runingToTerminate(activeTask);
            stateCore = StateCore.IDLE;
            activeTask = null;
        }
        
        if (RR.isRR) {
            System.out.println("ksabfksjdbfksbdjfbsjdvfjshdvfjshdvfjsbdfjhsdkfbsjdfhbsjdfbsjdhvbfj");
            quantom--;
            if (quantom==0) {
                quantom=RR.INIT_QUANTOM;
                interrupt=true;
            }
        }
        
        if (interrupt) {
            Assigner.runingToReady(activeTask);
            interrupt = false;
            stateCore = StateCore.IDLE;
            activeTask = null;
        }
    }
}
