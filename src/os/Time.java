/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;


/**
 *
 * @author Aref
 */
public class Time {
    public static int time=0;
    
    public void nextTime(){
        ntWaitingQueue();
        time++;
    }
    
    private void ntWaitingQueue(){
        for (Task task : Queues.waitingTask) {
            task.setLastWaitingTime(task.getLastWaitingTime()+1);
        }
    }
    
}
