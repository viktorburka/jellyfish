public interface Downloader {

    public FileInfo getFileInfo(String src, DownloadOptions options);

    public boolean download(String url, DownloadOptions options, Receiver rc);

    public static Downloader getProbeForScheme(String scheme) {
        return getDownloaderForScheme(scheme);
    }

    public static Downloader getDownloaderForScheme(String scheme) {
        switch (scheme) {
            case "s3":
                return new DownloaderS3();
            default:
                return null;
        }
    }
}
