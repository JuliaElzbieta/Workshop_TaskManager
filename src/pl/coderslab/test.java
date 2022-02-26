package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        File tasks = new File("tasks.csv");
        Path taskPath = Paths.get("tasks.csv");
        String[][] fileArray = new String[0][];
        Scanner user = new Scanner(System.in);
        String userChoice = "";

// wczytywanie danych z pliku i zapisywanie ich do tablicy
        try {
            for (String s : Files.readAllLines(taskPath)) {
                fileArray = Arrays.copyOf(fileArray, fileArray.length + 1);
                String[] fileLine = s.split(", ");
                fileArray[fileArray.length - 1] = fileLine;
            }
        } catch (IOException e) {
            e.getStackTrace();
        }

//menu
        do {
            System.out.print(ConsoleColors.BLUE + "Please select an option:");
            System.out.println(ConsoleColors.RESET + "\nadd \nremove \nlist \nexit");
            userChoice = user.nextLine();
            switch (userChoice) {
                case "add": fileArray = addTask(fileArray, user);
                    break;
                case "remove": fileArray = removeTask(fileArray, user);
                    break;
                case "list": taskList(fileArray);
                    break;
                case "exit":
                    System.out.println(ConsoleColors.RED + "Bye, bye");
                    saveTaskList(fileArray, taskPath);
                    break;
            }
        } while (!userChoice.equals("exit"));

    }

//dopisywanie do tablicy
        public static String[][] addTask (String[][] fileArray, Scanner user){
            //Scanner user = new Scanner(System.in);
            String[] addToList = new String[3];

            System.out.println("Please add task description");
            String addAnswer = user.nextLine();
            addToList[0] = addAnswer;
            System.out.println("Please add task due date");
            addAnswer = user.nextLine();
            addToList[1] = addAnswer;
            System.out.println("Is task important: true / false");
            addAnswer = user.nextLine();
            addToList[2] = addAnswer;
            fileArray = Arrays.copyOf(fileArray, fileArray.length + 1);
            fileArray[fileArray.length - 1] = addToList;

            return fileArray;
        }

//usuwanie wiersza
        public static String[][] removeTask (String[][] fileArray, Scanner user) {
            //Scanner user = new Scanner(System.in);
            int removeAnswer = 0;
            System.out.println("Please select number to remove");
            while (!user.hasNextInt()) {
                user.next();
                System.out.println("Please select correct number to remove");
            }
            removeAnswer = user.nextInt();
            if (removeAnswer >= fileArray.length || removeAnswer < 0) {
                do {
                    System.out.println("Number is out of list, choose correct number");
                    while (!user.hasNextInt()) {
                        user.next();
                        System.out.println("Please select correct number to remove");
                    }
                    removeAnswer = user.nextInt();   //<--- przypisujemy liczbe
                } while ((removeAnswer >= fileArray.length) || (removeAnswer < 0)); // <-- sprawdzamy warunek,
                                                                                    // jeśli sie zgadza to powtarzamy pętle do{},
                                                                                    // jesli się nie zgadza wychodzimy z pętli Z PRZYPISANĄ już wartościa
                //removeAnswer = user.nextInt();  <-- tutaj juz nie trzeba, bo przypisana zostanła 2 linijki wcześniej
                fileArray = ArrayUtils.remove(fileArray, removeAnswer);
            }
            return fileArray;
        }

// nadpisywanie pliku csv
        public static void saveTaskList (String[][] fileArray, Path taskPath) {
            List<String> outList = new ArrayList<>();
            //StringBuilder copyLine = new StringBuilder();
            for (int k = 0; k < fileArray.length; k++) {
                //for (int j = 0; j < fileArray[k].length; j++) {
                //copyLine.append(fileArray[k][j] + " ");
                String s = StringUtils.join(fileArray[k], ", ");
                outList.add(s);
            }
            //copyLine.setLength(0);
            try {
                Files.write(taskPath, outList);
            } catch (IOException e) {
                System.out.println("Nie można zapisać pliku.");
            }
        }

//wyświetlanie danych z aktualnej tablicy
        public static void taskList (String[][] fileArray) {
        StringBuilder str = new StringBuilder();
        // StringBuilder str = new StringBuilder();
        for (int i = 0; i < fileArray.length; i++) {
            for (int j = 0; j < fileArray[i].length; j++) {
                //for (String s : fileArray[i]) {
                //System.out.print(i + s + " ");
                str.append(fileArray[i][j] + "  ");
            }
            System.out.println(String.valueOf(i) + " : " + str);
            str.setLength(0);
        }
    }
}
