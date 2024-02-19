package Managers;

import java.util.NoSuchElementException;
import java.util.Scanner;

/*
 * class for printing and reading text in console
 */

public class Console {

    private static Scanner fileScanner = null;
    private static Scanner defScanner = new Scanner(System.in);

    public void print(Object obj){
        System.out.print(obj);
    }
    public void println(Object obj){
        System.out.println(obj);
    }
    public void printError(Object obj){
        System.out.println("Err: " + obj);
    }
    public String readln() throws NoSuchElementException, IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).nextLine();
    }
    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).hasNextLine();
    }
    public void selectFileScanner(Scanner scanner) {
        this.fileScanner = scanner;
    }
    public void selectConsoleScanner() {
        this.fileScanner = null;
    }
}
