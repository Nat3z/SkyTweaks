package com.natia.secretmod.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileUtils {
    public static boolean writeToFile(File fileToWrite, String data) {
        try {
            FileWriter myWriter = new FileWriter(fileToWrite);
            myWriter.write(data);
            myWriter.close();
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    public static String readFile(File filetoRead) {
        try {
            File myObj = filetoRead;
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                return data;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred when reading file.");
            e.printStackTrace();
        }

        return "";
    }
}
