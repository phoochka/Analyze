import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by gaurav on 10/10/14.
 */
public class McsData {

    HashMap<Integer, String[]> map = new HashMap<>();

    public McsData() {

    }

    public void run(String fileName) throws IOException {
        CSVReader reader = openFile(fileName);
        String[] nextLine;

        int[] mcs_values = new int[24];
        for (int i = 0; i < 24; i++) {
            mcs_values[i] = i;
        }

        while ((nextLine = reader.readNext()) != null) {
            if (!nextLine[9].isEmpty() && nextLine[5].contains("Data")) {
                int second = ((int) Double.parseDouble(nextLine[1]));
                int length = Integer.parseInt(nextLine[4]);
                int mcs_index = Integer.parseInt(nextLine[9]);
                String channel_type = nextLine[10];
                int channel = channel_type.contains("20") ? 20 : 40;

                int colCounter = 1;

                for (int i = 0; i < mcs_values.length; i++) {
                    if (mcs_index == mcs_values[i] && channel == 20) {
                        addValue(second, colCounter, length);
                    }
                    colCounter += 2;
                    if (mcs_index == mcs_values[i] && channel == 40) {
                        addValue(second, colCounter, length);
                    }
                    colCounter += 2;
                }
            }
        }

        if (!this.map.isEmpty()) {
            CSVWriter writer = new CSVWriter(new FileWriter("mcs_output.csv"), ',', '\0');

            String[] headers = new String[97];

            headers[0] = "Second";
            int c = 1;
            for (int i = 0; i < mcs_values.length; i++) {
                headers[c] = "COUNT mcs " + i + " channel 20";
                c++;
                headers[c] = "SIZE mcs " + i + " channel 20";
                c++;
                headers[c] = "Count mcs " + i + "channel 40";
                c++;
                headers[c] = "SIZE mcs " + i + " channel 40";
                c++;
            }
            writer.writeNext(headers);

            for (int m : this.map.keySet()) {
                String[] finals = this.map.get(m);
                writer.writeNext(finals);
            }
            writer.close();
        }
    }

    public void addValue(int second, int col, int fSize) {
        if (this.map.containsKey(second)) {
            updateCount(second, col);
            updateSize(second, fSize, col + 1);
        } else {
            String[] newInt = new String[97];
            newInt[0] = second + "";
            for (int i = 1; i < 97; i++) {
                newInt[i] = 0 + "";
            }
            this.map.put(second, newInt);
            updateCount(second, col);
        }
    }

    public void updateCount(int second, int col) {
        String[] arr = this.map.get(second);
        int oldCount = Integer.parseInt(arr[col]);
        arr[col] = oldCount + 1 + "";
        this.map.put(second, arr);

        //System.out.println("updated count col "+col);

    }

    public void updateSize(int second, int fSize, int col) {
        String[] arr = this.map.get(second);
        int oldSize = Integer.parseInt(arr[col]);
        arr[col] = oldSize + fSize + "";
        this.map.put(second, arr);

        //System.out.println("updated size col "+col);
    }

    public CSVReader openFile(String fileName) throws FileNotFoundException {
        return new CSVReader(new FileReader(fileName));
    }
}
