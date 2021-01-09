package RoundRobin;

import java.util.*;

public class run {

    static int timeQuantum;
    static int processesNum;
    static int contextSwitch;
    static int clock = 0;
    static  float avgWaiting = 0;
    static float avgTurnAround = 0;
    static process p = null;
    static List<process> processes = new ArrayList<>();
    static Queue<process> execute = new LinkedList<>();
    static Scanner input = new Scanner(System.in);

    public static void set(int num) {
        System.out.println("Enter Process Name, Arrival Time, Execution Time");
        for (int i = 0; i < num; i++) {
            String name = input.next();
            int arr = input.nextInt();
            int exe = input.nextInt();
            p = new process(name, arr, exe);
            processes.add(p);
        }
    }

    public static void sort() {
        processes.sort(Comparator.comparing(process::getArrivalTime));
    }

    public static void calcTurnAround()
    {
        for (RoundRobin.process process : processes)
            process.turnAroundTime += process.waitingTime + process.extime;
    }

    public static void calcCompletion()
    {
        for (RoundRobin.process process : processes)
            process.completionTime = process.turnAroundTime + process.getArrivalTime();
    }

    public static void calcAvgWaiting()
    {
        for (RoundRobin.process process : processes)
            avgWaiting += process.waitingTime;

        avgWaiting = avgWaiting / processes.size();
    }

    public static void calcAvgTurnAround()
    {
        for (RoundRobin.process process : processes)
            avgTurnAround += process.turnAroundTime;

        avgTurnAround = avgTurnAround / processes.size();
    }

    public  static boolean areExecuted()
    {
        boolean isRemaining = true;
        for (RoundRobin.process process : processes) {
            if (process.ExecutionTime != 0) {
                isRemaining = false;
                break;
            }
        }
        return isRemaining;
    }

    public static void checkExecutions()
    {
        for (RoundRobin.process process : processes) {
            if (!(process.getArrivalTime() > clock) && !process.isDone) {
                process.waitingTime = clock - process.getArrivalTime();
                execute.add(process);
                process.isDone = true;
            }
        }
    }

    public static void intoTheQueue(process current)
    {
        if (processes.contains(current))
            execute.add(processes.get(processes.indexOf(current)));
    }

    public static void addWait(int time, int context)
    {
        for (RoundRobin.process process : processes) {
            if (execute.contains(process))
                process.waitingTime += time + context;
        }
    }

    public static void calculations( int quantum, int context)
    {

        process current  = null;
        while(!areExecuted())
        {
            checkExecutions();
            if(current != null)
            {
                intoTheQueue(current);
                current = null;
            }
            if(!execute.isEmpty())
            {
                if(execute.peek().ExecutionTime > quantum)
                {
                    clock += quantum;
                    current = execute.peek();
                    execute.peek().ExecutionTime -= quantum;
                    processes.get(processes.indexOf(execute.peek())).turnAroundTime += context;
                    execute.poll();
                    addWait(quantum, context);
                }
                else
                {
                    int tempExecution = execute.peek().ExecutionTime;
                    clock+= execute.peek().ExecutionTime;
                    execute.peek().ExecutionTime = 0;
                    processes.get(processes.indexOf(execute.peek())).turnAroundTime += context;
                    execute.poll();
                    addWait(tempExecution, context);
                }
                clock += context;
            }
            else
                clock++;
        }
       calcTurnAround();
       calcCompletion();
       calcAvgWaiting();
       calcAvgTurnAround();
    }

    public static void print()
    {
        System.out.println("Process | Arrival Time | Execution Time | Completion Time | Waiting Time | Turn Around Time");
        for (RoundRobin.process process : processes) {
            System.out.println(process.name + " \t\t|\t"
                    + process.getArrivalTime() + "\t\t\t|\t\t"
                    + process.extime + "\t\t|\t\t"
                    + process.completionTime + "\t\t|\t\t"
                    + process.waitingTime + "\t\t|\t\t"
                    + process.turnAroundTime);
        }

        System.out.println("\nAverage Waiting time = " + avgWaiting);
        System.out.println("Average Turn Around Time = " + avgTurnAround);
    }


    public static void main(String[] args) {

        System.out.println("Enter number of processes");
        processesNum = input.nextInt();
        set(processesNum);
        sort();

        System.out.println("Enter time Quantum");
        timeQuantum = input.nextInt();
        System.out.println("Enter context switch ");
        contextSwitch = input.nextInt();

        calculations(timeQuantum, contextSwitch);
        print();




    }

}
