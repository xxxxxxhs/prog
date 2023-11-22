package objects;

import enums.*;
import exceptions.ThinkNothingException;
import interfaces.*;

public class Entity extends GeneralObj implements Thoughts{

    public Entity(String name){
        super(name);
    }
    
    @Override
    public String toString(){
        return "Entity{name=" + this.getName() + "}";
    }

    public class Walker implements Move{
        @Override
        public void go(Environment place, Prepositions prep){
            System.out.println(Entity.this.getName() + " шёл " + prep.getValue() + " " + place.getName());
        }
        @Override
        public void run(GeneralObj obj){
            System.out.println(Entity.this.getName() + " помчался по " + obj.getName());
        }
    }

    public void breakOut(String addition){
        System.out.println(this.getName() + " избавился от " + addition);
    }
    public void distributed(GeneralObj obj, Prepositions prep){
        System.out.println(this.getName() + " раздавался " + prep.getValue() + " " + obj.getName());
    }
    @Override
    public void think(GeneralObj obj, String action) throws ThinkNothingException{
        if (!action.equals("")){
            System.out.println(this.getName() + " думал, что " + obj.getName() + " " + action);
        }
        else{
            throw new ThinkNothingException("Не сказано, что сущность думает об объекте");
        }
    }
    public void notMention(String action) {
        System.out.println(this.getName() + " не замечал, " + action);
    }
    public void wasScared(){
        System.out.println(this.getName() + " боялся");
    }
    public void comeRound(Environment place){
        System.out.println(this.getName() + " начал приходить в себя на " + place.getName());
    }
    public void saw(GeneralObj obj, String action){
        System.out.println(this.getName() + " увидел, что " + obj.getName() + " " + action);
    }
    public void doNthnAbout(GeneralObj obj){
        System.out.println(this.getName() + " ничего не делал, не наносил вред " + obj.getName());
    }
    public void shoutSmthm(String phrase){
        System.out.println(this.getName() + " закричал \"" + phrase + "\"");
    }
    public void overBreath(){
        System.out.println(this.getName() + " задыхался");
    }
    public void shake(GeneralObj obj, Prepositions prep, GeneralObj where){
        System.out.println(this.getName() + " потряс " + obj.getName() + " " + prep.getValue() + " " + where.getName());
    }
    public void spit(){
        System.out.println(this.getName() + " плюнул");
    }
}
