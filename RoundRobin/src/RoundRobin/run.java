package RoundRobin;

import java.util.*;

public class run {

    static int timeQuantum;
    static int processesNum;
    static int contextSwitch;
    static int time = 0;
    static  float avgWaiting = 0;
    static float avgTurnAround = 0;
    static process p = null;
    static List<process> processes = new ArrayList<process>();
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
        Collections.sort(processes, Comparator.comparing(process::getArrivalTime));
    }

    public static void calcturnAround()
    {
        for (int i =0; i<processes.size(); i++)
        {
            processes.get(i).turnAroundTime += processes.get(i).waitingTime + processes.get(i).extime;
        }
    }

    public static void calcCompletion()
    {
        for (int i =0; i<processes.size(); i++)
        {
            processes.get(i).completionTime = processes.get(i).turnAroundTime + processes.get(i).getArrivalTime();
        }
    }

    public  static boolean areExecuted()
    {
        boolean isRemaining = true;
        for (int i=0; i<processes.size(); i++)
        {
            if(processes.get(i).ExecutionTime != 0) {
                isRemaining = false;
                break;
            }
        }
        return isRemaining;
    }


    public static void run( int quantum, int context)
    {
        Queue<process> execute = new LinkedList<process>();
        process current  = null;
        while(!areExecuted())
        {
            System.out.println("while");
            for(int i=0; i<processes.size(); i++)
            {
                if(processes.get(i).getArrivalTime() <= time && processes.get(i).isFirst)
                {
                    System.out.println("if");
                    processes.get(i).waitingTime = time - processes.get(i).getArrivalTime();
                    execute.add(processes.get(i));
                    processes.get(i).isFirst = false;
                }
            }
            if(current != null)
            {
                    if (processes.contains(current))
                    {
                        execute.add(processes.get(processes.indexOf(current)));
                    }
                    current = null;
            }
            if(!execute.isEmpty())
            {
                if(execute.peek().ExecutionTime > quantum)
                {
                    time += quantum;
                    current = execute.peek();
                    execute.peek().ExecutionTime -= quantum;
                    processes.get(processes.indexOf(execute.peek())).turnAroundTime += context;
                    execute.poll();
                    for (int i=0; i<processes.size(); i++)
                    {
                        if(execute.contains(processes.get(i)))
                        {
                            processes.get(i).waitingTime += quantum + context;
                        }
                    }
                    time += context;
                    continue;
                }
                else
                {
                    int tempexe = execute.peek().ExecutionTime;
                    time+= execute.peek().ExecutionTime;
                    execute.peek().ExecutionTime = 0;
                    processes.get(processes.indexOf(execute.peek())).turnAroundTime += context;
                    execute.poll();
                    for (int i=0; i<processes.size(); i++)
                    {
                        if(execute.contains(processes.get(i))) {
                            processes.get(i).waitingTime += tempexe + context;
                        }
                    }
                    time += context;
                    continue;
                }
            }
            else
                time++;
        }
       calcturnAround();
       calcCompletion();
       calcAvgWaiting();
       calcAvgTurnAround();
    }

    public static void calcAvgWaiting()
    {
        for (int i=0; i<processes.size(); i++)
        {
            avgWaiting += processes.get(i).waitingTime;
        }
        avgWaiting = avgWaiting / processes.size();
    }

    public static void calcAvgTurnAround()
    {
        for (int i=0; i<processes.size(); i++)
        {
            avgTurnAround += processes.get(i).turnAroundTime;
        }
        avgTurnAround = avgTurnAround / processes.size();
    }

    public static void print()
    {
        System.out.println("Process | Arrival Time | Execution Time | Completion Time | Waiting Time | Turn Around Time");
        for (int i=0; i<processes.size(); i++)
        {
            System.out.println(processes.get(i).name + " \t\t|\t"
                            + processes.get(i).getArrivalTime() + "\t\t\t|\t\t"
                            + processes.get(i).extime + "\t\t|\t\t"
                            + processes.get(i).completionTime + "\t\t|\t\t"
                            + processes.get(i).waitingTime + "\t\t|\t\t"
                            + processes.get(i).turnAroundTime);
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

        run(timeQuantum, contextSwitch);
        print();




    }

}
