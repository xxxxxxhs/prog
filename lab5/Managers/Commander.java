package Managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

/*
 * class command-interpreter and executor
 */

public class Commander {
    
    private Console console;
    private CollectionManager collectionManager;
    private ArrayList<String> lastCommands = new ArrayList<String>();
    private ArrayList<String> exHelper = new ArrayList<String>();
    private final String sym = "> ";
    private final String helpText = "help : вывести справку по доступным командам\n" +
            "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
            "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
            "add {element} : добавить новый элемент в коллекцию\n" +
            "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
            "remove_by_id id : удалить элемент из коллекции по его id\n" +
            "clear : очистить коллекцию\n" +
            "save : сохранить коллекцию в файл\n" +
            "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" + //
            "exit : завершить программу (без сохранения в файл)\n" +
            "remove_first : удалить первый элемент из коллекции\n" +
            "head : вывести первый элемент коллекции\n" +
            "history : вывести последние 13 команд (без их аргументов)\n" +
            "count_by_golden_palm_count goldenPalmCount : вывести количество элементов, значение поля goldenPalmCount которых равно заданному\n" +
            "print_unique_oscars_count : вывести уникальные значения поля oscarsCount всех элементов в коллекции\n" +
            "print_field_descending_mpaa_rating : вывести значения поля mpaaRating всех элементов в порядке убывания";

    public Commander(Console console, CollectionManager collectionManager) {
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /*
     * waiter for a next command
     */
    public void asker() {
        String line = "";
        while (true) {
            console.print(sym);
            line = console.readln().trim();
            if (line.equals("exit")) {
                System.exit(1);
            } else {
                String[] command = line.split(" ");
                manageLastCommands(command[0]);
                interpet(command);
            }
        }
    }

    /*
     * @param command
     * interpret command and it's argument
     */
    private void interpet(String[] command) {
        if (command.length == 1 && command[0].equals("help")) {console.println(helpText);}
        else if (command.length == 1 && command[0].equals("info")) {console.println(collectionManager.getInfo());;}
        else if (command.length == 1 && command[0].equals("show")) {console.println(collectionManager.show());;}
        else if (command.length == 1 && command[0].equals("add")) {try {collectionManager.add(Ask.newMovie(console));} catch (Exception e) {}}
        else if (command.length == 2 && command[0].equals("update")) {
            try {long id = Long.parseLong(command[1]);
            collectionManager.updateById(Ask.newMovie(console), id);}
            catch(NumberFormatException e) {console.printError("command format is incorrect");}}
        else if (command.length == 2 && command[0].equals("remove_by_id")) {
            try {long id = Long.parseLong(command[1]);
            collectionManager.removeById(id);}
            catch(NumberFormatException e) {console.printError("command format is incorrect");}}
        else if (command.length == 1 && command[0].equals("clear")) {collectionManager.clear();}
        else if (command.length == 1 && command[0].equals("save")) {collectionManager.save();}
        else if (command.length == 1 && command[0].equals("remove_first")) {collectionManager.removeFirst();}
        else if (command.length == 1 && command[0].equals("exit")) {System.exit(1);}
        else if (command.length == 1 && command[0].equals("head")) {console.println(collectionManager.head().toString());}
        else if (command.length == 2 && command[0].equals("count_by_golden_palm_count")) {
            try {long gpc = Long.parseLong(command[1]);
            System.out.println(collectionManager.countByGoldenPalmCount(gpc));}
            catch(NumberFormatException e) {console.printError("command format is incorrect");}}
        else if (command.length == 1 && command[0].equals("print_unique_oscars_count")) {console.println(collectionManager.printUniqueOscarsCount());}
        else if (command.length == 1 && command[0].equals("print_field_descending_mpaa_rating")) {console.println(collectionManager.printFieldDescendingMpaaRating());}
        else if (command.length == 1 && command[0].equals("history")) {for (String lastCommand : lastCommands) {console.println(lastCommand);}}
        else if (command.length == 2 && command[0].equals("execute_script")) {
            try {
                String path = new File(command[1]).getAbsolutePath();
                if (!isInHelper(path)) {
                    exHelper.add(path);
                    exIntepreter(command[1]);
                    exHelper.remove(path);
                } else {
                    console.printError("Recoursion has been occured");
                    exHelper.clear();
                }
            } catch (NullPointerException e) {console.printError("path is null");}
        }
        else {
            console.println("wrong command");
            lastCommands.removeLast();    
        }
    }

    /*
     * @param command
     * adds entered command into the history-collection 
     */
    private void manageLastCommands(String command) {
        if (lastCommands.size() >= 13) {
            while (lastCommands.size() >= 13) {lastCommands.removeFirst();}}
        lastCommands.add(command);
    }

    /*
     * @param fileName
     * interprets commands, which are in the file from execute_command {file}
     */
    private void exIntepreter(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while (true) {
                if ((line = reader.readLine()) == null) {break;}
                line = line.trim();
                console.println("executon:" + line.split(" ")[0]);
                interpet(line.split(" "));
                lastCommands.add(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {console.printError("file not found");}
        catch (IOException e) {console.printError("something went wrong");}
    }

    /*
     * @param s file-name
     * @return bool-value if file with such name have already been executed
     */
    private Boolean isInHelper(String s) {
        if (exHelper.size() == 0) {return false;}
        else {
            for (String el : exHelper) {if (el.equals(s)) {return true;}}
            return false;
        }
    }
}
