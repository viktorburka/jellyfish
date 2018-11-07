import java.io.OutputStream;
import java.util.Map;

public interface Receiver {
    void openConnection(String url, Map<String,String> options) throws ReceiverOperationError;
    long readPart(OutputStream writer, Map<String,String> options) throws ReceiverOperationError;
    void cancel() throws ReceiverOperationError;
    void closeConnection() throws ReceiverOperationError;

    FileInfo getFileInfo(String url, Map<String,String> options) throws ReceiverOperationError;

    static Receiver getProbeForScheme(String scheme) {
        return getReceiverForScheme(scheme, 1);
    }

    static Receiver getReceiverForScheme(String scheme, long fileSize) {
        switch (scheme) {
            case "s3":
                return new ReceiverS3Simple();
            default:
                return null;
        }
    }

    class ReceiverOperationError extends Exception {
        ReceiverOperationError(String error) {
            super(error);
        }
    }
}
