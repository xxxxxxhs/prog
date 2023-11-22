package exceptions;

import java.lang.Math;

public class InvalidNumberException extends Exception{

    private int number;
    
    public InvalidNumberException(){super();}
    public InvalidNumberException(String message){super(message);}
    public InvalidNumberException(String message, int number){
        super(message);
        this.number = number;
    }

    public int getAbsNumber(){
        return Math.abs(number);
    }
}
