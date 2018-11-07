import java.io.InputStream;
import java.util.Map;

public interface Sender {
    void openConnection(String url, Map<String,String> options) throws SenderOperationError;
    long writePart(InputStream reader, Map<String,String> options) throws SenderOperationError;
    void cancel() throws SenderOperationError;
    void closeConnection() throws SenderOperationError;

    static Sender getSenderForScheme(String scheme, long fileSize) {
        switch (scheme) {
            case "s3":
                return new SenderS3Simple();
            default:
                return null;
        }
    }

    class SenderOperationError extends Exception {
        SenderOperationError(String error) {
            super(error);
        }
    }
}
