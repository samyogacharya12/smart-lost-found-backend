package college.smart_lost_found_backend.exceptions;

public class Invalid extends RuntimeException{

    private String message;

    private Object detail;

    public Invalid(String message, Object detail) {
        this.message = message;
        this.detail=detail;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getDetail() {
        return detail;
    }
}
