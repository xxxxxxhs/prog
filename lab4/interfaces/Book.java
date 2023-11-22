package interfaces;

import exceptions.InvalidNumberException;

public interface Book {
    
    public void newChapter(int number, String title) throws InvalidNumberException;
}
