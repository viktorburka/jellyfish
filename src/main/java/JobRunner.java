import java.io.*;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class JobRunner extends Thread {

    private Job job;
    private String error = "";
    private CountDownLatch latch;

    JobRunner(Job job) {
        this.job = job;
    }

    synchronized boolean isError() {
        return !error.isEmpty();
    }

    synchronized String getError() {
        return this.error;
    }

    void setSignalOnStart(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        // signal that the thread has started
        if (latch != null)
            latch.countDown();

        System.out.println("transferring from " + job.srcUrl + " to " + job.dstUrl);

        String srcScheme;
        String dstScheme;

        try {
            srcScheme = SchemeParser.getScheme(job.srcUrl);
            dstScheme = SchemeParser.getScheme(job.dstUrl);
        } catch (Exception e) {
            setError(e.getMessage());
            return;
        }

        Receiver probe = Receiver.getProbeForScheme(srcScheme);
        if (probe == null) {
            setError(String.format("unsupported scheme: %s", srcScheme));
            return;
        }

        FileInfo info;
        try {
            System.out.println("query src file header information...");
            info = probe.getFileInfo(job.srcUrl, new HashMap<>());
            System.out.println("file size: " + info.size);
        } catch (Receiver.ReceiverOperationError e) {
            setError(e.getMessage());
            return;
        }

        Receiver receiver = Receiver.getReceiverForScheme(srcScheme, info.size);
        if (receiver == null) {
            setError(String.format("unsupported scheme: %s", srcScheme));
            return;
        }

        Sender sender = Sender.getSenderForScheme(dstScheme, info.size);
        if (sender == null) {
            setError(String.format("unsupported scheme: %s", dstScheme));
            return;
        }

        Downloader downloader = Downloader.getDownloader();
        Uploader uploader = Uploader.getUploader();

        File buffer = new File("buffer.file");

        try {
            System.out.println("start downloading...");
            FileOutputStream writer = new FileOutputStream(buffer);
            downloader.download(job.srcUrl, new HashMap<>(), receiver, writer);
            writer.close();
            System.out.println("download finished");

            System.out.println("start uploading...");
            FileInputStream reader = new FileInputStream(buffer);
            uploader.upload(job.dstUrl, new HashMap<>(), sender, reader);
            reader.close();
            System.out.println("upload finished");

        } catch (FileNotFoundException e) {
            setError("can't crate buffer file for download");
            return;
        } catch (Exception e) {
            setError(String.format("download error: %s", e.getMessage()));
            return;
        }
    }

    private synchronized void setError(String error) {
        this.error = error;
    }
}
