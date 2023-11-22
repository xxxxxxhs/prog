package objects;

import enums.Prepositions;

public class Environment extends GeneralObj{
    
    public Environment(String name){
        super(name);
    }
    public void surrounded(GeneralObj obj, Prepositions prep){
        System.out.println(obj.getName() + " cтоит " + prep.getValue() + " " + this.getName());
    }
}
