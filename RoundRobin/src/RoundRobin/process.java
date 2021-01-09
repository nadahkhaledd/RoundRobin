package RoundRobin;

public class process {

    public String name;
    public int ArrivalTime;
    public int ExecutionTime;
    public int waitingTime;
    public int turnAroundTime;
    public int completionTime;
    public int extime;
    public boolean isDone = false;

    public process(String name, int ArrivalTime, int ExecutionTime) {
        this.name = name;
        this.ArrivalTime = ArrivalTime;
        this.ExecutionTime = ExecutionTime;
        extime = ExecutionTime;
    }

    public int getArrivalTime() {
        return ArrivalTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }
}
