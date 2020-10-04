package wang.ismy.push.client;

public class Logger {

    private static final Logger INSTANCE = new Logger();

    private Logger() {}

    public void info(String msg, Object...args){
        System.out.println(msg);
    }

    public static Logger getInstance(){
        return INSTANCE;
    }
}
