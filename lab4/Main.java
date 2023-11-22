import objects.*;
import enums.*;
import interfaces.Book;
import exceptions.*;

public class Main{

    public static void main(String[] args) throws InvalidNumberException {
        // Одушевленные объекты
        Entity dunno = new Entity("Незнайка");
        Entity clops = new Entity("Клопс");
        Entity barking = new Entity("Лай");
        Entity dogs = new Entity("Собаки");
        Entity moonShorties = new Entity("Лунные коротышки");
        Entity nobody = new Entity("Никто");
        // Неодушевленные объекты
        Inanimate fence = new Inanimate("Забор");
        Inanimate houses = new Inanimate("Дома");
        Inanimate shops = new Inanimate("Магазины");
        Inanimate fist = new Inanimate("Кулак");
        Inanimate baldHead = new Inanimate("Лысина");
        Inanimate lights = new Inanimate("Фонари");
        // Места (окружение)
        Environment street = new Environment("Улица");
        Environment sidewalk = new Environment("Тротуар");
        Environment lowerFloors = new Environment("Нижние этажи");
        Environment home = new Environment("Дом");
        // Новая глава книги
        Book chapter = new Book() {
                @Override
                public void newChapter(int number, String title) throws InvalidNumberException{
                        if (number < 0) {
                                throw new InvalidNumberException("Номер главы меньше 0?", number);
                        }
                        else { 
                                System.out.println("Глава " + number + ". " + title + ".");
                        }
                }
        };
        // Создание объекта - ходячей части сущности
        Entity.Walker clopsFeet = clops.new Walker();
        Entity.Walker dunnoFeet = dunno.new Walker();
        Entity.Walker moonShortiesFeet = moonShorties.new Walker();
//------0-предложение-------------------------------------
        clops.shoutSmthm("Ах, ты так!");
        clops.overBreath();
        clops.shoutSmthm("Это тебе даром не пройдет!");
        clops.shoutSmthm("Я тебе ёщё покажу!");
        clops.shoutSmthm("Ты у меня попляшешь!");
        clops.shake(fist, Prepositions.ABOVE, baldHead);
        clopsFeet.go(home, Prepositions.TO);
        GeneralObj.Tool.forWhat("посчитать убытки");
//------новая-глава---------------------------------------
        try{
                chapter.newChapter(-9, "Как Незнайка встретился с Фиглем и Миглем");
        }
        catch(InvalidNumberException exc){
                exc.printStackTrace();
                chapter.newChapter(exc.getAbsNumber(), "Как Незнайка встретился с Фиглем и Миглем");
        }
//------1-предложение-------------------------------------        
        dunno.breakOut("преследования");
        dunnoFeet.run(street);
        street.surrounded(fence, Prepositions.AROUND);
//------2-предложение-------------------------------------
        barking.distributed(fence, Prepositions.FROM);
        try {
                dunno.think(dogs, "гонятся за ним");
        } catch (ThinkNothingException exc) {
                exc.printStackTrace();
        }
//------3-предложение-------------------------------------
        dunno.wasScared();
        dunno.notMention("где бежал");
        dunno.comeRound(street);
//------4-предложение-------------------------------------
        dunno.saw(dogs, "пропали");
        moonShortiesFeet.go(sidewalk, Prepositions.ACROSS);
//------5-предложение-------------------------------------
        nobody.doNthnAbout(dunno);
//------6-предложение-------------------------------------
        fence.lack();
//------7-предложение-------------------------------------
        street.surrounded(houses, Prepositions.BYTWOSIDES);
        shops.located(lowerFloors);
//------8-предложение-------------------------------------
        GeneralObj.changeTimeTo("вечер");
//------9-предложение-------------------------------------
        lights.lightedup(Prepositions.EVERYWHERE);
    }
}