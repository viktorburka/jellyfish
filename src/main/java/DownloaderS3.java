public class DownloaderS3 implements Downloader {

    @Override
    public FileInfo getFileInfo(String src, DownloadOptions options) {
        return null;
    }

    @Override
    public boolean download(String url, DownloadOptions options, Receiver rc) {
        return false;
    }
}
