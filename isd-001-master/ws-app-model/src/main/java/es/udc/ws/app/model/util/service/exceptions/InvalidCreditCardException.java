package es.udc.ws.app.model.util.service.exceptions;

public class InvalidCreditCardException extends Throwable {
    private String creditCardNum;

    public InvalidCreditCardException(String creditCardNum) {
        super("This credit card " + creditCardNum + " is invalid.");
        this.creditCardNum = creditCardNum;
    }

    public String getCreditCardNum() {
        return creditCardNum;
    }

    public void setCreditCardNum(String creditCardNum) {
        this.creditCardNum = creditCardNum;
    }
}
