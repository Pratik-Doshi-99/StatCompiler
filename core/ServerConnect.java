package core;

import Practise.ICICIDirect;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pratik Doshi
 */

public class ServerConnect {
    
    public static void getCSV(QueryParameter param, File target){
        try {
            URL url = new URL(param.getQuery());
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Cookie",param.getCookie());
            conn.connect();
            InputStreamReader ir = new InputStreamReader(conn.getInputStream(),"UTF-8");
            FileOutputStream fos = new FileOutputStream(target);
            int i =0;
            fos.write((param.getSymbol() + "\n").getBytes());
            while((i = ir.read())>=0){
                fos.write((byte)i);
            }
            fos.close();
            ir.close();
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(ICICIDirect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Error: " +  ex.toString());
        } catch (NullPointerException ex){
            Logger.getLogger(ICICIDirect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
 	//testing the code
    public static void main(String[] args) {
        
        try {
            File f = new File("C:\\Users\\Pratik Doshi\\Desktop\\CSV5.csv");
            QueryParameter param = new QueryParameter("CGPOWER.NS",
                    new Date(2,7,2019),
                    new Date(15,7,2019),
                    Interval.DAILY);
            ServerConnect.getCSV(param, f);
            param.setSymbol("SBIN.NS");
            ServerConnect.getCSV(param, f);
        } catch (InvalidDateException ex) {
            System.out.println(ex.getMessage());
        }
        
    } 
}
