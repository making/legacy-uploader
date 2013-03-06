package am.ik.uploader.domain.exception;

@SuppressWarnings("serial")
public class BussinessException extends RuntimeException {

    public BussinessException() {
        super();
    }

    public BussinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BussinessException(String message) {
        super(message);
    }

    public BussinessException(Throwable cause) {
        super(cause);
    }

}
