import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SenderS3Simple implements Sender {

    public void openConnection(String url, Map<String,String> options) throws SenderOperationError {

    }

    public long writePart(InputStream reader, Map<String,String> options) throws SenderOperationError {

        long totalWritten = 0;

        // temporary just saving to file until replaced with S3 upload implementation
        File buffer = new File("temp_file");

        try (FileOutputStream writer = new FileOutputStream(buffer)) {
            int bytesRead = 0;
            byte[] buf = new byte[1024];
            while ((bytesRead = reader.read(buf, 0, 1024)) > 0) {
                writer.write(buf, 0, bytesRead);
                totalWritten += bytesRead;
            }
        } catch (IOException e) {
            throw new SenderOperationError(e.getMessage());
        }

        return totalWritten;
    }

    public void cancel() throws SenderOperationError {

    }

    public void closeConnection() throws SenderOperationError {

    }
}
