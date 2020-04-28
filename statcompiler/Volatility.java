package statcompiler;

import core.Date;
import core.DynamicCalculation;
import core.Interval;
import core.InvalidDateException;
import core.QueryParameter;
import core.ServerConnect;
import java.io.File;
import java.util.Scanner;

public class Volatility implements DynamicCalculation{
    private double PREV_PRICE;
    private double term1;
    private double term2;
    private double STD;
    private double N; //No of closing prices.
    
    /*
        HPR = Holding Period Return = daily return
        For calc of Std for population, denominator is this.N-1 as no of HPRs = No of close prices - 1
        For calc of Std for sample,  denominator is this.N-2 as deno of sample std = deno of population std -1
    */
    
    Volatility(){
        this.PREV_PRICE = 0;
        this.STD = 0;
        this.term1 = 0;
        this.term2 = 0;
        this.N = 0;
    }
    
    @Override
    public void calculate(String row) {
        //System.out.print(row);
        try{
            if(this.N == 0){
                this.PREV_PRICE = this.getPrice(row);
                
                this.N++;
            }
            else{
                double curr_price = this.getPrice(row);
                double hpr = curr_price/this.PREV_PRICE -1;
                this.term1 += hpr*hpr;
                this.term2 += hpr;
                this.PREV_PRICE = curr_price;
                this.N++;
                
            }
            
        }catch(NumberFormatException ex){
            System.out.println("Heading Row/Null Row Enountered: Successfully Ignored in Calculation");
        }
    }
    
    
    private double getPrice(String row) throws NumberFormatException{
        Scanner s = new Scanner(row);
        s.useDelimiter(",");
        for (int i = 0; i < 5; i++) {
            s.next();
        }
        String t = s.next().trim();
        s.close();
        return Double.parseDouble(t);
        
    }

    public double getSTD(boolean issample, boolean isvariance){
        
        try{
            if(this.N == 0){
                return 0;
            }
            double var = (term1 + Math.pow(term2/(this.N-1),2)*(this.N-1) - 2*term2*term2/(this.N-1));
            var = issample?var/(this.N-2):var/(this.N-1);
            var = isvariance?var:Math.sqrt(var);
            return var;
        } catch(Exception e){
            return 0;
        }
    }
    
    public double getMean(){
        try{
            if(N<=0) return 0; 
            return term2/(N-1);
        } catch(Exception e){
            return 0;
        }
        
    }
    
    //testing the code
    public static void main(String[] args) {
        
        try {
            File f = new File("Z:\\Desktop\\CSV6.csv");
            QueryParameter param = new QueryParameter("ARVIND.NS",
                    new Date(2,7,2018),
                    new Date(15,7,2019),
                    Interval.DAILY);    
            //new ServerConnect().getCSV(param, f);
            param.setSymbol("SBIN.NS");
            //new ServerConnect().getCSV(param, f);
            Volatility vol = new Volatility();
            new ServerConnect(vol).getCSV(param,f);
            System.out.println(vol.getSTD(false,false));
            
            
        } catch (InvalidDateException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    
}
