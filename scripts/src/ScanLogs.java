import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ScanLogs {

    HashMap<Integer, String[]> map = new HashMap<>();

    public ScanLogs() {

    }

    public void run(String fileName) throws IOException {
        CSVReader reader = openFile(fileName);
        String[] nextLine;

        while ((nextLine = reader.readNext()) != null) {
            if (!(nextLine[1].equals("Time"))) {
                int second = ((int) Double.parseDouble(nextLine[1]));
                int length = Integer.parseInt(nextLine[4]);

                if (nextLine[5].contains("Ack") && (nextLine[7].equals("6.0") || nextLine[7].equals("9.0"))) {
                    addValue(second, 1);
                } else if (nextLine[5].contains("Ack") && (nextLine[7].equals("12.0") || nextLine[7].equals("18.0"))) {
                    addValue(second, 2);
                } else if (nextLine[5].contains("Ack") && (nextLine[7].equals("24.0") || nextLine[7].equals("36.0"))) {
                    addValue(second, 3);
                } else if (nextLine[5].contains("Ack") && (nextLine[7].equals("54.0") || nextLine[7].equals("48.0"))) {
                    addValue(second, 4);
                } else if (nextLine[5].contains("Clear") && (nextLine[7].equals("6.0") || nextLine[7].equals("9.0"))) {
                    addValue(second, 5);
                } else if (nextLine[5].contains("Clear") && (nextLine[7].equals("12.0") || nextLine[7].equals("18.0"))) {
                    addValue(second, 6);
                } else if (nextLine[5].contains("Clear") && (nextLine[7].equals("24.0") || nextLine[7].equals("36.0"))) {
                    addValue(second, 7);
                } else if (nextLine[5].contains("Clear") && (nextLine[7].equals("54.0") || nextLine[7].equals("48.0"))) {
                    addValue(second, 8);
                } else if (nextLine[5].contains("Request") && (nextLine[7].equals("6.0") || nextLine[7].equals("9.0"))) {
                    addValue(second, 9);
                } else if (nextLine[5].contains("Request") && (nextLine[7].equals("12.0") || nextLine[7].equals("18.0"))) {
                    addValue(second, 10);
                } else if (nextLine[5].contains("Request") && (nextLine[7].equals("24.0") || nextLine[7].equals("36.0"))) {
                    addValue(second, 11);
                } else if (nextLine[5].contains("Request") && (nextLine[7].equals("54.0") || nextLine[7].equals("48.0"))) {
                    addValue(second, 12);
                } else if (nextLine[5].contains("Beacon") && (nextLine[7].equals("6.0") || nextLine[7].equals("9.0"))) {
                    addValue(second, 13);
                } else if (nextLine[5].contains("Beacon") && (nextLine[7].equals("12.0") || nextLine[7].equals("18.0"))) {
                    addValue(second, 14);
                } else if (nextLine[5].contains("Beacon") && (nextLine[7].equals("24.0") || nextLine[7].equals("36.0"))) {
                    addValue(second, 15);
                } else if (nextLine[5].contains("Beacon") && (nextLine[7].equals("54.0") || nextLine[7].equals("48.0"))) {
                    addValue(second, 16);
                } else if (nextLine[5].contains("Data") && (nextLine[7].equals("6.0") || nextLine[7].equals("9.0"))) {
                    addValue(second, 17, length);
                } else if (nextLine[5].contains("Data") && (nextLine[7].equals("12.0") || nextLine[7].equals("18.0"))) {
                    addValue(second, 19, length);
                } else if (nextLine[5].contains("Data") && (nextLine[7].equals("24.0") || nextLine[7].equals("36.0"))) {
                    addValue(second, 21, length);
                } else if (nextLine[5].contains("Data") && (nextLine[7].equals("54.0") || nextLine[7].equals("48.0"))) {
                    addValue(second, 23, length);
                }
            }
        }

        if (!this.map.isEmpty()) {
            CSVWriter writer = new CSVWriter(new FileWriter("output.csv"), ',', '\0');
            String[] header = {"Second", "A6", "A12", "A24", "A54", "R6", "R12", "R24", "R54", "C6", "C12",
                    "C24", "C54", "B6", "B12", "B24", "B54", "D C6", "D S6", "D C12", "D S12", "D C24", "D S24", "D C54",
                    "D S54"};
            writer.writeNext(header);

            for (int m : this.map.keySet()) {
                /**
                 String[] finals = Arrays.toString(map.get(m)).split("[\\[\\]]")[1].split(", ");
                 writer.writeNext(m + "" + Arrays.toString(finals));
                 **/

                String[] finals = this.map.get(m);
                writer.writeNext(finals);

            }
            writer.close();

        }
    }


    public void addValue(int second, int col) {
        if (this.map.containsKey(second)) {
            updateCount(second, col);
        } else {
            String[] newInt = new String[26];
            newInt[0] = second + "";
            for (int i = 1; i < 26; i++) {
                newInt[i] = 0 + "";
            }
            this.map.put(second, newInt);
            updateCount(second, col);
        }
    }

    public void addValue(int second, int col, int fSize) {
        if (this.map.containsKey(second)) {
            updateCount(second, col);
            updateSize(second, fSize, col + 1);
        } else {
            String[] newInt = new String[26];
            newInt[0] = second + "";
            for (int i = 1; i < 26; i++) {
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

    }

    public void updateSize(int second, int fSize, int col) {
        String[] arr = this.map.get(second);
        int oldSize = Integer.parseInt(arr[col]);
        arr[col] = oldSize + fSize + "";
        this.map.put(second, arr);
    }

    public CSVReader openFile(String fileName) throws FileNotFoundException {
        return new CSVReader(new FileReader(fileName));
    }
}
