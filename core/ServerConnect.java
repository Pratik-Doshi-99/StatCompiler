package core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Pratik Doshi
 */

public class ServerConnect {
    /**
     * @param args the command line arguments
     */
    private DynamicCalculation DYNAMIC;
    public ServerConnect(){
        this.DYNAMIC = null;
    }
    public ServerConnect(DynamicCalculation dynamic){
        this.DYNAMIC = dynamic;
    }
    
    public void getCSV(QueryParameter param, File target){
        try {
            URL url = new URL(param.getQuery());
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Cookie",param.getCookie());
            conn.connect();
            InputStreamReader ir = new InputStreamReader(conn.getInputStream(),"UTF-8");
            FileOutputStream fos = null;
            if(target != null){
                fos = new FileOutputStream(target);
                fos.write((param.getSymbol() + "\n").getBytes());
            }
            int i =0;
            String row = "";
            while((i = ir.read())>=0){
                if(fos != null){
                    fos.write((byte)i);
                }
                if((char)i == '\n' && this.DYNAMIC != null){
                    this.DYNAMIC.calculate(row);
                    row = "";
                    continue;
                }
                if(this.DYNAMIC != null){
                    row += (char)i;
                }
                
            }

            if(fos != null){
                fos.close();
            }
            if(this.DYNAMIC != null){
                this.DYNAMIC.calculate(row);
            }
            ir.close();
            
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error: " +  ex.toString());
            ex.printStackTrace();
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
        
    }
    
 
    public static void main(String[] args) {
        
        try {
            File f = new File("Z:\\Desktop\\CSV6.csv");
            QueryParameter param = new QueryParameter("ARVIND.NS",
                    new Date(1,7,2018),
                    new Date(17,7,2018),
                    Interval.DAILY);
            //new ServerConnect().getCSV(param, f);
            param.setSymbol("SBIN.NS");
            //new ServerConnect().getCSV(param, f);
            DynamicCalculation calc = (String row) -> System.out.println(row);
            new ServerConnect(calc).getCSV(param,f);
            
            
        } catch (InvalidDateException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    
}
