package backup.whatsapp;

public class UnsupportedMessageType extends RuntimeException {
    public UnsupportedMessageType(String message) {
        super(message);
    }
}
