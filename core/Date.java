package core;

/**
 *
 * @author Pratik Doshi
 */
//This class was created because much of the functionality of java.util.Date is deprecated.
public class Date {
    private int DAY;
    private int MONTH;
    private int YEAR;
    Date(int day, int month, int year){
        DAY = day;
        MONTH = month;
        YEAR = year;
    }
    public void setDay(int d){
        this.DAY = d;
    }
    public void setMonth(int m){
        this.MONTH = m;
    }
    public void setYear(int y){
        this.YEAR = y;
    }
    public int getDay(){
        return DAY;
    }
    public int getMonth(){
        return MONTH;
    }
    public int getYear(){
        return YEAR;
    }
    @Override
    public String toString(){
        return this.getDay() + "-" + this.getMonth() + "-" + this.getYear();
    }
}
