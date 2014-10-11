import java.io.IOException;

/**
 * Created by gaurav on 10/9/14.
 */
public class Runner {
    public static void main(String[] wut){
        ScanLogs sLogs = new ScanLogs();
        McsData sData = new McsData();
        McsCounter counter = new McsCounter();
        try {
            sData.run("UniLib_2Oct.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
