package statcompiler;

import core.Date;
import core.DynamicCalculation;
import core.Interval;
import core.InvalidDateException;
import core.QueryParameter;
import core.ServerConnect;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 *
 * @author Pratik Doshi
 */
public class GenerateCSV implements Runnable{
    private File SOURCE;
    private String TARGET_DIR;
    private Date START_DATE;
    private Date END_DATE;
    private boolean DOWNLOAD;
    
    GenerateCSV(String source, String target, Date start, boolean download){
        this.SOURCE = new File(source);
        this.TARGET_DIR = target;        
        this.START_DATE = start;
        LocalDateTime cal = LocalDateTime.now();
        this.END_DATE = new Date(cal.getDayOfMonth(),cal.getMonthValue(),cal.getYear());
        this.DOWNLOAD = download;
    }
    public GenerateCSV(String source, String target, Date start, Date end, boolean download){
        this.SOURCE = new File(source);
        this.TARGET_DIR = target;
        this.START_DATE = start;
        this.END_DATE = end;
        this.DOWNLOAD = download;
    }
    
    
    @Override
    public void run(){
        this.getSample();
    }
    
    private void getSample(){
        
        try{
            
            QueryParameter param = new QueryParameter("ARVIND.NS",
                    this.START_DATE,
                    this.END_DATE,
                    Interval.DAILY);
            Scanner scan = new Scanner(new FileReader(this.SOURCE));
            FileWriter fw = new FileWriter(new File(this.TARGET_DIR + "\\Compiled.csv"));
            fw.write("From:," + this.START_DATE + "\n");
            fw.write("To:," + this.END_DATE + "\n");
            fw.write("HPR:,Daily Percentage Returns\n");
            fw.write("Scrip,Mean_HPR,SD_Sample_HPR,SD_Population_HPR,Sample_Variance_HPR,Populaiton_Variance_HPR");
            int i = 1;
            while(scan.hasNext()){
                String scrip = scan.nextLine();
                scrip = scrip.indexOf(',')>=0?scrip.substring(0,scrip.indexOf(',')):scrip;
                scrip = scrip.toUpperCase();
                System.out.println(i + ") Current Scrip: " + scrip); //Add to logger section on the screen
                i++;
                param.setSymbol(scrip + ".NS");
                Volatility vol = new Volatility();
                File temp = this.DOWNLOAD?new File(this.TARGET_DIR + "\\" + scrip + ".csv"):null;
                new ServerConnect(vol).getCSV(param, temp);
                fw.write("\n" + scrip + ","
                        + vol.getMean() + ","
                        + vol.getSTD(true,false) + ","
                        + vol.getSTD(false,false) + ","
                        + vol.getSTD(true,true) + ","
                        + vol.getSTD(false,true));
                System.out.println("\n\n");
            }
            scan.close();
            fw.close();
            System.out.println("\n\n\nPROCESS COMPLETED");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        
    }
    
    
    public static void main(String[] args) {
        String source = "Z:\\Desktop\\Tickers.csv";
        String target = "Z:\\Desktop\\Samples";
        Date d = new Date(24,4,2019);
        GenerateCSV gcsv = new GenerateCSV(source,target,d,false);
        gcsv.getSample();
    }
    
}
