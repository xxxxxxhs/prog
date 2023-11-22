package interfaces;

import enums.Prepositions;
import objects.Environment;
import objects.GeneralObj;

public interface Move {
    
    public void run(GeneralObj obj);
    public void go(Environment place, Prepositions prep);
}
