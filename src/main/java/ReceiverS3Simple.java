import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class ReceiverS3Simple implements Receiver {

    private String bucketName;
    private String keyName;
    private AmazonS3 s3;

    @Override
    public FileInfo getFileInfo(String url, Map<String,String> options) throws ReceiverOperationError {
        FileInfo fileInfo;
        openConnection(url, options);
        fileInfo = fetchFileInfo(); // can be null but need to call closeConnection()
        closeConnection();
        return fileInfo;
    }

    @Override
    public void openConnection(String url, Map<String,String> options) throws ReceiverOperationError {

        AmazonS3URI s3uri = new AmazonS3URI(url);
        String bucketAndKey = s3uri.getKey(); // for some reason AWS returns the path

        int dividerIdx = bucketAndKey.indexOf('/');
        if (dividerIdx == -1)
            throw new ReceiverOperationError(String.format("can't parse path [%s]", bucketAndKey));

        this.bucketName = bucketAndKey.substring(0, dividerIdx);
        this.keyName = bucketAndKey.substring(dividerIdx+1);
        this.s3 = AmazonS3ClientBuilder.defaultClient();
    }

    @Override
    public long readPart(OutputStream writer, Map<String,String> options) throws ReceiverOperationError {

        long totalRead = 0;

        try {
            S3Object o = s3.getObject(bucketName, keyName);
            S3ObjectInputStream s3is = o.getObjectContent();
            byte[] buf = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = s3is.read(buf)) != -1) {
                writer.write(buf, 0, bytesRead);
                totalRead += bytesRead;
            }
            System.out.println(String.format("received total of %d bytes", totalRead));
            s3is.close();
            writer.close();
        } catch (AmazonServiceException e) {
            String err = String.format("error downloading s3 object: %s", e.getErrorMessage());
            throw new ReceiverOperationError(err);
        } catch (IOException e) {
            String err = String.format("error downloading s3 object: %s", e.getMessage());
            throw new ReceiverOperationError(err);
        }

        return totalRead;
    }

    @Override
    public void cancel() {
        closeConnection();
    }

    @Override
    public void closeConnection() {
        this.s3 = null;
    }

    private FileInfo fetchFileInfo() throws ReceiverOperationError {
        ObjectMetadata md;
        try {
            md = s3.getObjectMetadata(bucketName, keyName);
        } catch (AmazonServiceException e) {
            System.out.println(e);
            String error = String.format("can't retrieve source file header information: %s", e.getErrorMessage());
            throw new ReceiverOperationError(error);
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.size = md.getContentLength();
        return fileInfo;
    }
}
