import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.HashMap;

public class LogAnalysis 
{
    private LinkedList<Log> logQueue;
    private LinkedList<Log> errorStack;
    private LinkedList<Log> newErrors;
    private HashMap<String, Integer> logMap;
    private int warningCount;

    public LogAnalysis() 
    {
        logQueue = new LinkedList<>();
        errorStack = new LinkedList<>();
        logMap = new HashMap<>();
        newErrors = new LinkedList<>();
        warningCount = 0;
    }

    public void readFile(String filename) 
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) 
        {
            String line;
            while ((line = br.readLine()) != null) 
            {
                if (line.startsWith("Log Entry") || line.trim().isEmpty()) 
                {
                    continue;
                }
                String[] entry = line.split("] ", 2);
                String timestamp = entry[0].substring(1);
                String[] levelAndMessage = entry[1].split(" ", 2);
                String level = levelAndMessage[0];
                String message = levelAndMessage[1];
                
                logQueue.offer(new Log(timestamp, level, message));
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void processLogs() 
    {
        while (!logQueue.isEmpty()) 
        {
            Log entry = logQueue.poll();
            
            logMap.put(entry.level, logMap.getOrDefault(entry.level, 0) + 1);
            
            if (entry.level.equals("ERROR")) 
            {
                errorStack.addFirst(entry);
                if (newErrors.size() >= 100) 
                {
                    newErrors.removeLast();
                }
                newErrors.addFirst(entry);
            }
            
            if (entry.level.equals("WARN") && entry.message.contains("Memory")) 
            {
                warningCount++;
            }
        }
    }

    public void printLogs() 
    {
        System.out.println("Log Levels:");
        for (String level : logMap.keySet()) 
        {
            System.out.println(level + ": " + logMap.get(level));
        }
        
        System.out.println("\nLast 100 Errors:");
        for (Log error : newErrors) 
        {
            System.out.println("[" + error.timestamp + "] " + error.level + " " + error.message);
        }
        
        System.out.println("\nMemory Warnings: " + warningCount);
    }

    public static void main(String[] args) 
    {
        LogAnalysis system = new LogAnalysis();
        system.readFile("src//log-data.csv");
        system.processLogs();
        system.printLogs();
    }
}