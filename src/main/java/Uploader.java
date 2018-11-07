import java.io.InputStream;
import java.util.Map;

public interface Uploader {
    void upload(String url, Map<String,String> options, Sender snd, InputStream reader) throws Exception;

    static Uploader getUploader() {
        return new UploaderSimple();
    }
}
