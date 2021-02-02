package os;

import os.Enum.Priority;
import os.Enum.Resource;
import os.Enum.StateTask;

import java.util.HashMap;

public class Task {

    private boolean isAssigned = false;
    private StateTask state;
    private int untilTime;
    private int lastWaitingTime = 0;
    private Priority priority;
    private String name;
    private final HashMap<Resource, Integer> needed;
    private int taskDuration;

    public Task(StateTask state, Priority priority, String name, int taskDuration) {
        this.name = name;
        this.state = state;
        this.priority = priority;
        this.taskDuration = taskDuration;
        needed = new HashMap<>();
        if (priority.equals(Priority.X)) {
            needed.put(Resource.A, 1);
            needed.put(Resource.B, 1);
        } else if (priority.equals(Priority.Y)) {
            needed.put(Resource.C, 1);
            needed.put(Resource.B, 1);
        } else if (priority.equals(Priority.Z)) {
            needed.put(Resource.A, 1);
            needed.put(Resource.C, 1);
        }


    }

    public StateTask getState() {
        return state;
    }

    public void setState(StateTask state) {
        this.state = state;
    }

    public int getUntilTime() {
        return untilTime;
    }

    public void setUntilTime(int untilTime) {
        this.untilTime = untilTime;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Resource, Integer> getNeeded() {
        return needed;
    }


    public int getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(int taskDuration) {
        this.taskDuration = taskDuration;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public boolean checkResource(HashMap<Resource, Integer> currentResource) {
        int a,b,c;
        if (needed.get(Resource.A)==null) {
            a=0;
        }else{
            a=needed.get(Resource.A);
        }
        if (needed.get(Resource.B)==null) {
            b=0;
        }else{
            b=needed.get(Resource.B);
        }
        if (needed.get(Resource.C)==null) {
            c=0;
        }else{
            c=needed.get(Resource.C);
        }
        boolean a1=false,b1=false,c1=false;
        if (a==0||(a!=0 && a<=currentResource.get(Resource.A))) {
            a1=true;
        }
        if (b==0||(b!=0 && b<=currentResource.get(Resource.B))) {
            b1=true;
        }
        if (c==0||(c!=0 && c<=currentResource.get(Resource.C)) ) {
            c1=true;
        }
        return a1&&b1&&c1;

    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public int getLastWaitingTime() {
        return lastWaitingTime;
    }

    public void setLastWaitingTime(int lastWaitingTime) {
        this.lastWaitingTime = lastWaitingTime;
    }

    @Override
    public String toString() {
        return "os.Task{" +
                "name='" + name + '\'' +
                '}';
    }
}
