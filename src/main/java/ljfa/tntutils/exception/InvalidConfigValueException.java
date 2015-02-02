package ljfa.tntutils.exception;

public class InvalidConfigValueException extends RuntimeException {
    public InvalidConfigValueException() {
        
    }
    
    public InvalidConfigValueException(String message) {
        super(message);
    }
    
    public InvalidConfigValueException(Throwable cause) {
        super(cause);
    }
    
    public InvalidConfigValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
