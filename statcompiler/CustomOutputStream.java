package statcompiler;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 *
 * @author Pratik Doshi
 */
public class CustomOutputStream extends OutputStream{
    private JTextArea JTA;
    CustomOutputStream(JTextArea jta){
        this.JTA = jta;
    }
    
    @Override
    public void write(int b) throws IOException {
        this.JTA.append(String.valueOf((char)b));
    }
    
}
