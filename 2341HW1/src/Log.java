public class Log 
{
    String timestamp;
    String level;
    String message;

    public Log(String timestamp, String level, String message) 
    {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
    }

    @Override
    public String toString() 
    {
        return "[" + timestamp + "] " + level + " " + message;
    }
}
