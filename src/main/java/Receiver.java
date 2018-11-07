import java.io.FileOutputStream;
import java.util.Map;

public interface Receiver {
    FileInfo getFileInfo(String url, DownloadOptions options);
    boolean openConnection(String url, DownloadOptions options);
    boolean isConnectionOpen();
    long readPart(FileOutputStream writer, Map<String,String> options);
    boolean cancel();
    boolean closeConnection();
}
