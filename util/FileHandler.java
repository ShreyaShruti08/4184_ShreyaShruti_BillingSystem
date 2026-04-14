package util;

import java.io.*;

public class FileHandler {

    // Save bill to file
    public static void saveToFile(String data) {
        try {
            FileWriter fw = new FileWriter("bill.txt");
            fw.write(data);
            fw.close();
        } catch (Exception e) {
            System.out.println("Error saving file");
        }
    }
    public static String loadFromFile() {
        StringBuilder data = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader("bill.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                data.append(line).append("\n");
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading file");
        }

        return data.toString();
    }
}
