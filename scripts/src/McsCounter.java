import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by gaurav on 10/10/14.
 */
public class McsCounter {

    HashMap<String, Integer> counter = new HashMap<String, Integer>();

    public McsCounter() {

    }

    public void run(String fileName) throws IOException {
        CSVReader reader = openFile(fileName);
        String[] nextLine;

        while ((nextLine = reader.readNext()) != null) {
            if (!nextLine[10].isEmpty() && !nextLine[9].equals("MCS Index")) {
                String channel = nextLine[10];
                if (this.counter.containsKey(channel)) {
                    int oldValue = this.counter.get(channel);
                    counter.put(channel, oldValue + 1);
                } else {
                    counter.put(channel, 1);
                }
            }
        }
        for (String s : counter.keySet()) {
            System.out.println(s + " " + counter.get(s));
        }

    }

    public CSVReader openFile(String fileName) throws FileNotFoundException {
        return new CSVReader(new FileReader(fileName));
    }
}
