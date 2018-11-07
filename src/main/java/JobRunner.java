import java.io.*;
import java.util.HashMap;

public class JobRunner extends Thread {

    private Job job;
    private String error = "";

    JobRunner(Job job) {
        this.job = job;
    }

    public synchronized boolean isError() {
        return error.isEmpty();
    }

    public void run() {
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
            info = probe.getFileInfo(job.srcUrl, new HashMap<>());
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

        try {
            File buffer = new File("key_name");
            FileOutputStream writer = new FileOutputStream(buffer);
            FileInputStream reader = new FileInputStream(buffer);

            downloader.download(job.srcUrl, new HashMap<>(), receiver, writer);
            uploader.upload(job.dstUrl, new HashMap<>(), sender, reader);

        } catch (FileNotFoundException e) {
            setError("can't crate buffer file for download");
            return;
        } catch (Exception e) {
            setError(String.format("download error: %s", e.getMessage()));
            return;
        }
    }

    private synchronized String getError() {
        return this.error;
    }

    private synchronized void setError(String error) {
        this.error = error;
    }
}
