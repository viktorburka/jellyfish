import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploaderSimple implements Uploader {
    public void upload(String url, Map<String,String> options, Sender snd, InputStream input) throws Exception {
        try {
            snd.openConnection(url, options);
            snd.writePart(input, new HashMap<>());
            snd.closeConnection();
        } catch (Sender.SenderOperationError e) {
            throw new Exception(e);
        }
    }
}
