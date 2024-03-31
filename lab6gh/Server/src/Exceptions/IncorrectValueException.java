package Exceptions;

/*
 * thrown if some parameter in set() is wrong
 */

public class IncorrectValueException extends Throwable{
    
    public IncorrectValueException(String message){
        super(message);
    }
}
