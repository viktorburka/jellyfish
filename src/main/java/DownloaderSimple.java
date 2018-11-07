import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class DownloaderSimple implements Downloader {

    @Override
    public void download(String url, Map<String,String> options, Receiver rc, OutputStream writer) throws Exception {
        try {
            rc.openConnection(url, options);
            rc.readPart(writer, new HashMap<>());
            rc.closeConnection();
        } catch (Receiver.ReceiverOperationError e) {
            throw new Exception(e);
        }
    }
}
