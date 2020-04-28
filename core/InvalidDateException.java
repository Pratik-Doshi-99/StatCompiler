  
package core;

/**
 *
 * @author Pratik Doshi
 */

public class InvalidDateException extends Exception {
    String MESSAGE;
    InvalidDateException(String message){
        this.MESSAGE = message;
    }
    @Override
    public String getMessage(){
        return MESSAGE;
    }
}
