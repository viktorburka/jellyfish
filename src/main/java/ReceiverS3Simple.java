import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ReceiverS3Simple implements Receiver {

    private String error = "";
    private String bucketName;
    private String keyName;
    private AmazonS3 s3;

    @Override
    public FileInfo getFileInfo(String url, DownloadOptions options) {
        FileInfo fileInfo;
        if (!openConnection(url, options))
            return null;
        fileInfo = fetchFileInfo(url, options); // can be null but need to call closeConnection()
        if (!closeConnection())
            return null;
        return fileInfo;
    }

    @Override
    public boolean openConnection(String url, DownloadOptions options) {
        this.s3 = AmazonS3ClientBuilder.defaultClient();
        return true;
    }

    @Override
    public boolean isConnectionOpen() {
        return this.s3 != null;
    }

    @Override
    public long readPart(FileOutputStream writer, Map<String,String> options) {

        String bucketName = options.get("bucket");

        try {
            S3Object o = s3.getObject(bucketName, keyName);
            S3ObjectInputStream s3is = o.getObjectContent();
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                writer.write(read_buf, 0, read_len);
            }
            s3is.close();
            writer.close();
        } catch (AmazonServiceException e) {
            setError(e.getErrorMessage());
            return -1;
        } catch (FileNotFoundException e) {
            setError(e.getMessage());
            return -1;
        } catch (IOException e) {
            setError(e.getMessage());
            return -1;
        }
        return 0;
    }

    @Override
    public boolean cancel() {
        return false;
    }

    @Override
    public boolean closeConnection() {
        return false;
    }

    private FileInfo fetchFileInfo(String url, DownloadOptions options) {
        return new FileInfo();
    }

    private synchronized void setError(String error) {
        this.error = error;
    }
}
