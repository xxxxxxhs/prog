package interfaces;

import exceptions.ThinkNothingException;
import objects.GeneralObj;

public interface Thoughts{
    public void think(GeneralObj obj, String action) throws ThinkNothingException;

}