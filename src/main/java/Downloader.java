import java.io.OutputStream;
import java.util.Map;

public interface Downloader {

    void download(String url, Map<String,String> options, Receiver rc, OutputStream writer) throws Exception;

    static Downloader getDownloader() {
        return new DownloaderSimple();
    }
}
