package ljfa.tntutils.exception;

public class InvalidConfigValueException extends RuntimeException {
    private static final long serialVersionUID = -7206620619144191952L;

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
