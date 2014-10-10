
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ScanLogs {

    HashMap<Integer, String[]> map = new HashMap<Integer, String[]>();
    //String[] store = new String[5];

    public ScanLogs() {

    }

    public void run(String fileName) throws IOException {
        CSVReader reader = openFile(fileName);
        String[] nextLine; int i = 0;
        while((nextLine = reader.readNext()) != null) {
            if (!(nextLine[1].equals("Time"))) {
                int second = ((int) Double.parseDouble(nextLine[1]));
                System.out.println("Second: " + second);
                System.out.println("i value: " + i);
                System.out.println("Checking if " + nextLine[5] + " contains Ack");
                int length = Integer.parseInt(nextLine[4]);
                if (nextLine[5].contains("Ack")) {
                    addValue(second, length, 'A');
                } else if (nextLine[5].contains("Clear")) {
                    addValue(second, length, 'C');
                } else if (nextLine[5].contains("Request")) {
                    addValue(second, length, 'R');
                } else if (nextLine[6].contains("Data")) {
                    
                }
            }
        }

        if(!this.map.isEmpty()){
            CSVWriter writer = new CSVWriter(new FileWriter("output.csv"), ',');
            for(int m: map.keySet()) {
                String[] finals = map.get(m);
                writer.writeNext(finals);
            }
            writer.close();

        }
    }

    public void addValue(int second, int fSize, char type){
        if(this.map.containsKey(second)){
            updateArray(second, fSize, type);
        } else {
            String[] newString = {"0","0","0","0","0","0","0","0","0"};
            this.map.put(second, newString);
        }
    }

    public void updateArray(int second, int fSize, char type) {
        System.out.println("Adding value type "+type+" size "+fSize);
        String[] sArray=  this.map.get(second);
        switch (type) {
            case 'A':
                int countA= Integer.parseInt(sArray[0]);
                countA ++;
                setValue(second, 0, countA + "");
                int sumA = Integer.parseInt(sArray[1]) + fSize;
                setValue(second, 1, sumA+"");

            case 'C':
                int countC= Integer.parseInt(sArray[2]);
                countC ++;
                setValue(second, 2, countC+"");
                int sumC = Integer.parseInt(sArray[3]) + fSize;
                setValue(second, 3, sumC+"");

            case 'R':
                int countR= Integer.parseInt(sArray[4]);
                countR++;
                setValue(second, 4, countR+"");
                int sumR = Integer.parseInt(sArray[5]) + fSize;
                setValue(second, 5, sumR+"");
        }
    }

    public void setValue (int second, int array_i, String value) {
        System.out.println("Setting value "+value+" for index "+array_i);
        String[] array = this.map.get(second);
        array[array_i] = value;
        this.map.put(second, array);
    }



    public CSVReader openFile(String fileName) throws FileNotFoundException {
        return new CSVReader(new FileReader(fileName));
    }
}
