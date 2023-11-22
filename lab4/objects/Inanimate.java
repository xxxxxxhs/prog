package objects;

import enums.Prepositions;

public class Inanimate extends GeneralObj{
    
    public Inanimate(String name){
        super(name);
    }
    public void lack(){
        System.out.println(this.getName() + " здесь отсутствовал");
    }
    public void located(Environment place){
        System.out.println(this.getName() + " находится на " + place.getName());
    }
    public void lightedup(Prepositions prep){
        System.out.println(prep.getValue() + " зажёгся " + this.getName());
    }
}
