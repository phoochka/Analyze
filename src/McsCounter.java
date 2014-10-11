import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by gaurav on 10/10/14.
 */
public class McsCounter {

    HashMap<Double, Integer> counter = new HashMap<Double, Integer>();

    public McsCounter() {

    }

    public void run(String fileName) throws IOException {
        CSVReader reader = openFile(fileName);
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            if (!nextLine[5].equals("MCS Index") && nextLine[5].contains("Beacon") && nextLine[12].equals("False")) {
                Double tx_rate = Double.parseDouble(nextLine[7]);
                if (this.counter.containsKey(tx_rate)) {
                    int oldValue = this.counter.get(tx_rate);
                    counter.put(tx_rate, oldValue + 1);
                } else {
                    counter.put(tx_rate, 1);
                }
            }
        }
        for (double d : counter.keySet()) {
            System.out.println(d + ": " + counter.get(d));
        }

    }

    public CSVReader openFile(String fileName) throws FileNotFoundException {
        return new CSVReader(new FileReader(fileName));
    }
}
