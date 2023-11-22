package objects;

public abstract class GeneralObj {

    private String name;

    public GeneralObj(){
        this.name = "Some object";
    }
    public GeneralObj(String name){
        this.name = name;
    }
    @Override
    public boolean equals(Object obj){
        return this.hashCode() == obj.hashCode();
    }
    @Override
    public int hashCode(){
        return super.hashCode() + this.getName().hashCode();
    }
    public String getName(){
        return name;
    }

    public static class Tool{
        
        public static void forWhat(String reason){
        System.out.println("Для того, чтобы " + reason);
        }
    }

    public static void changeTimeTo(String time){

        class PeriodOfTime{
            String name;

            public PeriodOfTime(String period){
                this.name = period;
            }
            public String getName(){
                return name;
            }
        }

        PeriodOfTime period = new PeriodOfTime(time);
        System.out.println("наступил " + period.getName());
    }
}
