package core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pratik Doshi
 */
public class QueryParameter {
    private final long DATE_REMAINDER = 66600;
    private final int [] MONTH_DAYS = {31,28,31,30,31,30,31,31,30,31,30,31};
    
    private final String BASIC_QUERY = "https://query1.finance."
            + "yahoo.com/v7/finance/download/";
    
    //Other non user url query parameters
    
    private final String EVENTS = "history";
    
    
    private String SYMBOL;
    private long PERIOD1;
    private long PERIOD2;
    private String INTERVAL;
    private String COOKIE;
    private String CRUMB;
    
    private QueryParameter(){
        
    }
    
    
    public QueryParameter(String script, Date startDate, Date endDate, Interval interval) 
            throws InvalidDateException{
        this.SYMBOL = script.trim();
        this.PERIOD1 = this.getDate(startDate,-1);
        this.PERIOD2 = this.getDate(endDate,0);
        if(PERIOD2<PERIOD1) throw new InvalidDateException("End date cannot"
                + " occur after start date");
        this.setInterval(interval);
        this.updateRequest();
        
    }
    
    public void setInterval(Interval interval){
        switch(interval){
            case DAILY:
                this.INTERVAL = "1d";
                break;
            case WEEKLY:
                this.INTERVAL = "1wk";
                break;
            case MONTHLY:
                this.INTERVAL = "1mo";
                break;
            default:
                break;
        }
        this.updateRequest();
    }
    
    public Interval getInterval(){
        if(this.INTERVAL.equals("1d"))
            return Interval.DAILY;
        if(this.INTERVAL.equals("1wk"))
            return Interval.WEEKLY;
        return Interval.MONTHLY;
    }
    
    public void setSymbol(String symbol){
        this.SYMBOL = symbol;
        this.updateRequest();
    }
    public String getSymbol(){
        String symbol = SYMBOL.substring(0,SYMBOL.indexOf('.'));
        return symbol;
    }
    public void setStartDate(Date start) throws InvalidDateException{
        this.PERIOD1 = this.getDate(start,-1);
        if(PERIOD2<PERIOD1) throw new InvalidDateException("End date cannot"
                + " occur after start date");
        this.updateRequest();
        String x = "https://query1.finance.yahoo.com/v7/finance/download/ACC.NS?period1=1556123325&period2=1587745725&interval=1d&events=history";
        x = "https://query1.finance.yahoo.com/v7/finance/download/BANKNIFTY,,.NS?period1=1556044200&period2=1587753000&interval=1d&events=history&crumb=";
    }
    public void setEndDate(Date end) throws InvalidDateException{
        this.PERIOD2 = this.getDate(end,0);
        if(PERIOD2<PERIOD1) throw new InvalidDateException("End date cannot"
                + " occur after start date");
        this.updateRequest();
    } 
    public String getQuery(){
        String query = BASIC_QUERY;
        query += SYMBOL + "?" +
                "period1=" + PERIOD1 +
                "&period2=" + PERIOD2 +
                "&interval=" + INTERVAL + 
                "&events=" + EVENTS +
                "&crumb=" + CRUMB;
        
        return query;
                
    }
    public String getCookie(){
        return this.COOKIE;
    }
    
    
    
    private long getDate(Date d2, int adjFactor){
        //returns the number of days since epooch
        Date d1 = new Date(1,1,1970);
        long days = this.yearCount(d1.getYear(), d2.getYear()) +
                this.tillYearEnd(d1.getDay(), d1.getMonth(), d1.getYear()) +
                this.fromYearStart(d2.getDay(), d2.getMonth(), d2.getYear()) + adjFactor;
        long seconds = days * 86400  +  this.DATE_REMAINDER;
        return seconds;
        
    }
    private int yearCount(int y1, int y2){
        int dayCount = 0;
        for(int i =y1+1;i<y2;i++){
            dayCount += 365;
            if(i%4==0)dayCount += 1;
        }
        return dayCount;
    }
    private int tillYearEnd(int d, int m, int y){
        int dayCount = MONTH_DAYS[m-1] - d;
        for (int i = m; i < MONTH_DAYS.length; i++) {
            dayCount += MONTH_DAYS[i];
        }
        if(y%4==0){
            if(m<2)dayCount++;
            if(m==2 && d<29)dayCount++;
        }
        
        return dayCount;
    }
    private int fromYearStart(int d, int m, int y){
        int dayCount = d;
        for (int i = 0; i < (m-1); i++) {
            dayCount += MONTH_DAYS[i]; 
        }
        if(y%4==0){
            if(m>2)dayCount++;
        }
        return dayCount;
    }
    
    private void updateRequest(){
        //GIVE ACTUAL COOKIE VALUES TAKEN FROM BROWSER
        //this method will later have to be updated to access cookie data from a file
        //when this is actually implemented on server side. The number of cookies needed will be much more than
        //10 (as in this case). there will have to be a system in place that will keep track of cookie expiry and 
        // create new cookies (even if it is manually)
        String[] cookie = {"APID=UPe5270c4a-9f42-11e9-8374-06ba32cdb226;"
                    + " PRF=t%3DSBIN.NS%252BHDFCBANK.NS;"
                    + " APIDTS=1562391147;"
                    + " B=4ibr7utehuv23&b=3&s=l3",
        
                        "APID=UPd4ad9cf9-a646-11e9-b281-064dcd50edee;"
                    + " PRF=t%3DINR%253DX%252BSBIN.NS%252BJSWSTEEL.NS%252BHDFCBANK.NS;"
                    + " APIDTS=1563627011;"
                    + " B=6sshhtleimga9&b=3&s=6f",
        
                        "B=60n8si1f7k57g&b=3&s=v5;"
                    + " PRF=t%3DKOTAKBANK.NS"};
        String[] crumb = {"Dl4ap4VvgK5","8LHMipHkVdb"};        
        
        
        
        double d= Math.random();
        int i = (int)(d*10000);
        i = i%(10^cookie.length);
        while(i>=cookie.length || i<0){
            d= Math.random();
            i = (int)(d*10000);
            i = i%(10^cookie.length);
        }
        
        this.COOKIE = "";//cookie[1];
        this.CRUMB = "";//crumb[i];
    }
    
    
    //create methods to allow user of this class to change parameters
         
    
    public static void main(String[] args) {
        try {
            QueryParameter param = new QueryParameter("SBIN.NS",
                    new Date(2,7,2019),
                    new Date(15,7,2019),
                    Interval.DAILY);
            System.out.println(param.getSymbol());
        } catch (InvalidDateException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
}
