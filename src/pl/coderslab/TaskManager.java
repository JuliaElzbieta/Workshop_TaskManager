package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    //private static File tasks = new File("tasks.csv");
    private static Path taskPath = Paths.get("tasks.csv");
    private static Scanner user = new Scanner(System.in);
    private static String[][] fileArray = new String[0][];
    private static String userChoice = "";

    public static void main(String[] args) {
// wczytywanie danych z pliku i zapisywanie ich do tablicy
        try {
            for (String s : Files.readAllLines(taskPath)) {
                fileArray = Arrays.copyOf(fileArray, fileArray.length + 1);
                fileArray[fileArray.length - 1] = s.split(", ");
            }
        } catch (IOException e) {
            e.getStackTrace();
        }

//menu
        do {
            while(true){
                System.out.print(ConsoleColors.BLUE + "Please select an option:");
                System.out.println(ConsoleColors.RESET + "\nadd \nremove \nlist \nexit");
                userChoice = user.nextLine(); //łapie wybór użytkownika + enter, który przechodzi do kolejnego polecenia
                if(userChoice.equals("add") || userChoice.equals("remove") || userChoice.equals("list") || userChoice.equals("exit")){
                    break;
                }
            }

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
        String addAnswer;
        String[] addToList = new String[3];

        addToList[0] = stringAnswer("Please add task description", user);
        addToList[1] = stringAnswer("Please add task due date", user);
        while(true){
        addAnswer = stringAnswer("Is task important: true / false", user);
        if(addAnswer.equals("true") || addAnswer.equals("false")){
            break;}
        }
        addToList[2] = addAnswer;
        fileArray = Arrays.copyOf(fileArray, fileArray.length + 1);
        fileArray[fileArray.length - 1] = addToList;

        return fileArray;
    }

    //usuwanie wiersza
    public static String[][] removeTask (String[][] fileArray, Scanner user) {
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
                removeAnswer = correctNumber("Please select correct number to remove", user);
            } while ((removeAnswer >= fileArray.length) || (removeAnswer < 0)); // <-- sprawdzamy warunek,
            // jeśli sie zgadza to powtarzamy pętle do{},
            // jesli się nie zgadza wychodzimy z pętli Z PRZYPISANĄ już wartościa
            //removeAnswer = user.nextInt();  <-- tutaj juz nie trzeba, bo przypisana zostanła 2 linijki wcześniej
            fileArray = ArrayUtils.remove(fileArray, removeAnswer);
        } else {
            fileArray = ArrayUtils.remove(fileArray, removeAnswer);
        }
        user.nextLine();
        return fileArray;
    }

    // nadpisywanie pliku csv
    public static void saveTaskList (String[][] fileArray, Path taskPath) {
        List<String> outList = new ArrayList<>();
        for (int k = 0; k < fileArray.length; k++) {
            String s = StringUtils.join(fileArray[k], ", ");
            outList.add(s);
        }
        try {
            Files.write(taskPath, outList);
        } catch (IOException e) {
            System.out.println("Nie można zapisać pliku.");
        }
    }

    //wyświetlanie danych z aktualnej tablicy
    public static void taskList (String[][] fileArray) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < fileArray.length; i++) {
            for (int j = 0; j < fileArray[i].length; j++) {
                str.append(fileArray[i][j] + "  ");
            }
            System.out.println(String.valueOf(i) + " : " + str);
            str.setLength(0);
        }
    }

    public static String stringAnswer (String message, Scanner scan){
        System.out.println(message);
        String answer = scan.nextLine();
        return answer;
    }

    public static int correctNumber (String message, Scanner user){
        while (!user.hasNextInt()) {
            user.next();
            System.out.println(message);
        }
        int answer = user.nextInt();
        return answer;
    }
}
