package SoloProject.SocialMediaApp.debug;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Debug {

    private static PrintWriter writer;

    // Initialize the debug file
    public static void initDebugFile(String fileName) {
        try {
            writer = new PrintWriter(new FileWriter(fileName, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void log(String message) {
        if (writer != null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.println(timestamp + " - " + message);
            writer.flush();
        }
    }

    // Close the debug file
    public static void closeDebugFile() {
        if (writer != null) {
            writer.close();
        }
    }

    public static void main(String[] args) {
        initDebugFile("debug.log");


        log("Starting application...");

        try {
            int result = 10 / 0;
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }

        log("Application finished.");

        closeDebugFile();
    }
}